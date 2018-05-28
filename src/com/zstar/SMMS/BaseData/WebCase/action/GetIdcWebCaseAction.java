package com.zstar.SMMS.BaseData.WebCase.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetIdcWebCaseAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String json = "";
    
    String limit = "";
    limit = (String)getWebData("LIMIT");
    String offset = "";
    offset = (String)getWebData("OFFSET");
    Map map = new HashMap();
    if ((!"".equals(limit)) && (limit != null)) {
      map.put("LIMIT", limit);
    }
    if ((!"".equals(offset)) && (offset != null)) {
      map.put("OFFSET", offset);
    }
    List list = this.sqlSession.selectList("WebCase.selectWebCaseInfoJson", map);
    json = JsonUtil.dataListToJson(list);
    setMsg(json);
    FMPLog.debug("备案json:" + json);
    FMPLog.debug("备案json:" + list.size());
    return "empty";
  }
}
