package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountMessageDel;
import com.zstar.fmp.core.frame.action.FrameAction;

public class GetJsonAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    GetCountMessageDel del = new GetCountMessageDel(this.contex);
    String json = del.eventJson();
    setMsg(json);
    return "empty";
  }
}
