package com.zstar.SMMS.BaseData.SptCityRectify.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.List;

public class GetCityRectiyToSptAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    List list = this.sqlSession.selectList("SptCityRectify.selectListToSpt");
    String json = JsonUtil.dataListToJson(list);
    FMPLog.debug("省页面整改处理数据Json:" + json);
    setMsg(json);
    return "empty";
  }
}
