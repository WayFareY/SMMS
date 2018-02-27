package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignCloseAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    
    String[] rids = rid.split(",");
    String ridList = "";
    for (int i = 0; i < rids.length; i++)
    {
      String r = "'" + rids[i] + "'" + ",";
      if (i == rids.length - 1) {
        r = "'" + rids[i] + "'";
      }
      ridList = ridList + r;
    }
    String condition = "RID IN (" + ridList + ")";
    Map ridMap = new HashMap();
    Map mainRidMap = new HashMap();
    ridMap.put("CONDITION", condition);
    ridMap.put("RECTIFY_STATE", "020");
    Map webCase = new HashMap();
    List<Map> urlRid = this.sqlSession.selectList("SmmsEventMain.selectUrlAndRid", ridMap);
    FMPLog.debug("标记已整改关停数据查询数量:" + urlRid.size());
    EventMainDel mainDel = new EventMainDel(this.contex);
    int sum = 0;
    if ((urlRid != null) && (urlRid.size() > 0)) {
      for (Map map : urlRid)
      {
        condition = "RID IN ('" + map.get("EVENT_RID") + "'" + ")";
        ridMap.put("CONDITION", condition);
        ridMap.put("FEEDBACK_TIME", FMPContex.getCurrentTime());
        ridMap.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
        ridMap.put("RECTIFY_MEASURE", "2");
        
        ridMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int result = this.sqlSession.update("SmmsEventMain.update", ridMap);
        condition = "MAIN_RID IN ('" + map.get("EVENT_RID") + "'" + ")";
        mainRidMap.put("CONDITION", condition);
        mainRidMap.put("FEEDBACK_TIME", FMPContex.getCurrentTime());
        mainRidMap.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
        mainRidMap.put("RECTIFY_MEASURE", "2");
        
        mainRidMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        this.sqlSession.update("SmmsPendingEvent.updateFeedbackTime", mainRidMap);
        
        int i = mainDel.insertEventHis(map);
        FMPLog.debug("标记已关停插入历史记录表是否成功：" + i);
        sum += result;
      }
    }
    if (sum > 0)
    {
      setMsg("成功处理" + sum + "条数据");
      return "empty";
    }
    setMsg("处理失败");
    return "empty";
  }
}
