package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountCitySumDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;

public class GetJsonFxFwqAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    GetCountCitySumDel del = new GetCountCitySumDel(this.contex);
    String json = del.getFxFwqJson();
    FMPLog.debug("风险服务器json:" + json);
    setMsg(json);
    return "empty";
  }
}
