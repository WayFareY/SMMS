package com.zstar.SMMS.BaseData.IdcInfo.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.List;

public class GetIdcInfoAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    List list = this.sqlSession.selectList("IdcInfo.viewIdcInfo");
    
    String json = JsonUtil.dataListToJson(list);
    FMPLog.debug("idc信息Json:" + json);
    setMsg(json);
    return "empty";
  }
}
