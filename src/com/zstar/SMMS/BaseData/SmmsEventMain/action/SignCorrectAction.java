package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SignCorrectAction
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
    FMPLog.debug("++++++++" + ridList);
    String condition = "RID IN (" + ridList + ")";
    Map ridMap = new HashMap();
    Map mainRidMap = new HashMap();
    ridMap.put("CONDITION", condition);
    ridMap.put("RECTIFY_STATE", "020");
    Map webCase = new HashMap();
    List<Map> urlRid = this.sqlSession.selectList("SmmsEventMain.selectUrlAndRid", ridMap);
    FMPLog.debug("标记已整改数据查询数量:" + urlRid.size());
    EventMainDel mainDel = new EventMainDel(this.contex);
    
    int successSum = 0;
    int failSum = 0;
    String url = "";
    if ((urlRid != null) && (urlRid.size() > 0)) {
      for (Map map : urlRid)
      {
        url = (String)map.get("URL");
        map.put("URL", url.toUpperCase());
        webCase = mainDel.selectInsertPendingInfoByIpOrUrl(map);
        FMPLog.debug(webCase.size());
        if (webCase.containsKey("WEB_CASE_RID"))
        {
          ridMap.put("FEEDBACK_TIME", FMPContex.getCurrentTime());
          ridMap.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
          ridMap.put("RECTIFY_MEASURE", "1");
          condition = "RID IN ('" + map.get("EVENT_RID") + "'" + ")";
          ridMap.put("CONDITION", condition);
          
          ridMap.putAll(webCase);
          
          ridMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
          
          int result = this.sqlSession.update("SmmsEventMain.updateRectify", ridMap);
          condition = "MAIN_RID IN ('" + map.get("EVENT_RID") + "'" + ")";
          mainRidMap.put("CONDITION", condition);
          mainRidMap.put("FEEDBACK_TIME", FMPContex.getCurrentTime());
          mainRidMap.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
          mainRidMap.put("RECTIFY_MEASURE", "1");
          
          mainRidMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
          this.sqlSession.update("SmmsPendingEvent.updateFeedbackTime", mainRidMap);
          
          successSum += result;
          FMPLog.debug("总数" + successSum);
        }
        else
        {
          failSum++;
        }
      }
    }
    setMsg("成功处理" + successSum + "条数据" + "\n" + "处理失败" + failSum + "条数据，失败原因未找到备案信息！！！" + "\n");
    return "empty";
  }
}
