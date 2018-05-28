package com.zstar.SMMS.BaseData.SmmsWebCloseApp.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class SubmitExamAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String eventRid = (String)getWebData("RID");
    String level = (String)getWebData("APP_LEVEL");
    String closeReason = (String)getWebData("CLOSE_REASON");
    
    System.out.println("日志事件rid:" + eventRid + "level:" + level + "关停理由：" + closeReason);
    Map eventMap = new HashMap();
    eventMap.put("RID", eventRid);
    
    Map getMap = new HashMap();
    getMap = (Map)this.sqlSession.selectOne("SmmsEventMain.forCloseApply", eventMap);
    Map insertMap = new HashMap();
    String rid = FMPContex.getNewUUID();
    insertMap.put("RID", rid);
    
    insertMap.put("EVENT_RID", eventRid);
    
    insertMap.put("APP_LEVEL", level);
    
    insertMap.put("CLOSE_REASON", closeReason);
    
    insertMap.put("ACCESS_ID", getMap.get("ACCESS_ID"));
    
    insertMap.put("APP_RESULT", "0");
    
    insertMap.put("ISSUE_STATE", "0");
    
    insertMap.put("LIABMAN", getWebData("CURR_USERID"));
    insertMap.put("LIABDEPT", "admin");
    insertMap.put("LIABORG", "admin");
    
    insertMap.put("CREATTIME", FMPContex.getCurrentTime());
    
    insertMap.put("RECORDSTATE", "0");
    
    int result = this.sqlSession.insert("SmmsWebCloseApp.insertSave", insertMap);
    if (result == 1)
    {
      setMsg(rid);
      return "empty";
    }
    setMsg("999");
    return "empty";
  }
}
