package com.zstar.SMMS.BaseData.SmmsPendingRzjz.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.Map;

public class ShowAfSnapshopAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    Map ridMap = new HashMap();
    ridMap.put("RID", getWebData("RID"));
    Map map = (Map)this.sqlSession.selectOne("SmmsPending_rzjz.findSnapshopByRid", ridMap);
    String json = JsonUtil.dataMapToJson(map);
    setWebData("SNAPSHOP", json);
    return "success";
  }
}
