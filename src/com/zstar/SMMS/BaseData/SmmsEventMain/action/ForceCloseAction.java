package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForceCloseAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    String[] kv = rid.split(",");
    String ridList = "";
    for (int j = 0; j < kv.length; j++)
    {
      String s = "'" + kv[j] + "'" + ",";
      ridList = ridList + s;
    }
    ridList = ridList.substring(0, ridList.length() - 1);
    
    Map mapRid = new HashMap();
    mapRid.put("RIDCONDITION", "and ssp.RID IN (" + ridList + ") order by ssp.access_id asc");
    
    mapRid.put("DBTYPE", FMPContex.DBType);
    List<Map<String, Object>> forceCloselist = this.sqlSession.selectList("SmmsEventMain.viewToJson", mapRid);
    EventMainDel mainDel = new EventMainDel(this.contex);
    String message = mainDel.returnForceCloseMessage(forceCloselist);
    setMsg(message);
    return "empty";
  }
}
