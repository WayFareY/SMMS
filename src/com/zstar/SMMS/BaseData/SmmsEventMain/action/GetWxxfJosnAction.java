package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountMessageDel;
import com.zstar.fmp.core.frame.action.FrameAction;

public class GetWxxfJosnAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    GetCountMessageDel del = new GetCountMessageDel(this.contex);
    
    String json = del.getWxxfJson();
    setMsg(json);
    return "empty";
  }
}
