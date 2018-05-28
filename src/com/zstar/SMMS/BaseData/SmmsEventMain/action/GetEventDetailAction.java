package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.Map;

public class GetEventDetailAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("MAIN_RID");
    Map map = new HashMap();
    map.put("RID", rid);
    Map selectMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getEventDetail", map);
    String json = JsonUtil.dataMapToJson(selectMap);
    setMsg(json);
    return "empty";
  }
}
