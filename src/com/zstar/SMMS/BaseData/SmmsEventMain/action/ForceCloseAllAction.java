package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ForceCloseAllAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    Map map = new HashMap();
    map.put("DBTYPE", FMPContex.DBType);
    map.put("RIDCONDITION", "order by ssp.access_id asc");
    
    List<Map<String, Object>> forceCloselist = this.sqlSession.selectList("SmmsEventMain.viewToAllJson", map);
    EventMainDel del = new EventMainDel(this.contex);
    String message = del.returnForceCloseMessage(forceCloselist);
    setMsg(message);
    return "empty";
  }
}
