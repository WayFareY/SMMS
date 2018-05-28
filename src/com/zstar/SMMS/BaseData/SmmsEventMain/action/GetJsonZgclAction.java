package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.BaseData.SptCityRectify.action.del.CityRecityDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;

public class GetJsonZgclAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    CityRecityDel del = new CityRecityDel(this.contex);
    String json = del.selectListJson();
    FMPLog.debug("地市IDC安全信息分类汇总整改处理输据Json:" + json);
    setMsg(json);
    return "empty";
  }
}
