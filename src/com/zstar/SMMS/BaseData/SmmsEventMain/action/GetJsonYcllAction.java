package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountCitySumDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;

public class GetJsonYcllAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    GetCountCitySumDel del = new GetCountCitySumDel(this.contex);
    String json = del.getYcllJson();
    FMPLog.debug("异常流量json:" + json);
    setMsg(json);
    return "empty";
  }
}
