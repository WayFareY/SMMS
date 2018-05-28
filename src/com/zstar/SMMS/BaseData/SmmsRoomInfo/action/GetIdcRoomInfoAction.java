package com.zstar.SMMS.BaseData.SmmsRoomInfo.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.List;

public class GetIdcRoomInfoAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String json = "";
    List list = this.sqlSession.selectList("SmmsRoomInfo.selectRoomInfoToJson");
    json = JsonUtil.dataListToJson(list);
    FMPLog.debug("机房json:" + json);
    FMPLog.debug("机房大小:" + list.size());
    setMsg(json);
    return "empty";
  }
}
