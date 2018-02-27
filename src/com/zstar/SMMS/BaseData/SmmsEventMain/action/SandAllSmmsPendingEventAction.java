package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SandAllSmmsPendingEventAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    Map map = new HashMap();
    map.put("DBTYPE", FMPContex.DBType);
    
    List<Map<String, Object>> dataList = this.sqlSession.selectList("SmmsEventMain.viewToAllRectify", map);
    EventMainDel mainDel = new EventMainDel(this.contex);
    String returnMessage = mainDel.returnSandMessage(dataList);
    setMsg(returnMessage);
    return "empty";
  }
}
