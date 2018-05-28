package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountCitySumDel;
import com.zstar.fmp.core.frame.action.FrameAction;

public class GestJsonBygtHfAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    GetCountCitySumDel del = new GetCountCitySumDel(this.contex);
    String json = del.getSgtAndHfJson();
    setMsg(json);
    return "empty";
  }
}
