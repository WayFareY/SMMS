package com.zstar.SMMS.STAT.SptIdcCloseOpen.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.List;

public class GetCityCloseToSptAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    List list = this.sqlSession.selectList("SptIdcCloseOpen.selectListToSptJson");
    String json = JsonUtil.dataListToJson(list);
    FMPLog.debug("省页面关停处理数据Json:" + json);
    setMsg(json);
    return "empty";
  }
}
