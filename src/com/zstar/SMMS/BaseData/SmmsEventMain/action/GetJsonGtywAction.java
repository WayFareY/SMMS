package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountMessageDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.Map;

public class GetJsonGtywAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    Map map = new HashMap();
    
    String useRid = (String)getWebData("CURR_USERID");
    
    String beginTime = (String)getWebData("beginTime");
    
    String endTime = (String)getWebData("endTime");
    if (useRid.startsWith("IDC")) {
      map.put("ACCESS_ID", useRid);
    } else {
      FMPLog.debug("无结束idc编号参数传入");
    }
    if (beginTime != null) {
      map.put("BEGINTIME", beginTime);
    } else {
      FMPLog.debug("无开始时间参数传入");
    }
    if (endTime != null) {
      map.put("ENDTIME", endTime);
    } else {
      FMPLog.debug("无结束时间参数传入");
    }
    GetCountMessageDel del = new GetCountMessageDel(this.contex);
    String json = del.getGtywEvent(map);
    setMsg(json);
    return "empty";
  }
}
