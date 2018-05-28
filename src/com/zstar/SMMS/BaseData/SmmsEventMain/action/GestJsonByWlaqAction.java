package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.constant.GetCountCitySumDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;

public class GestJsonByWlaqAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String wcl = "";
    wcl = (String)getWebData("WCL");
    String ygt = "";
    ygt = (String)getWebData("YGT");
    String yxf = "";
    yxf = (String)getWebData("YXF");
    String codition = "";
    if (("0".equals(wcl)) && (wcl != null)) {
      codition = " and ( RECTIFY_STATE='000' or RECTIFY_STATE='005' or RECTIFY_STATE='999' ) ";
    }
    if (("1".equals(ygt)) && (ygt != null)) {
      codition = " and ( RECTIFY_STATE='888' or RECTIFY_STATE='020' or RECTIFY_STATE='030' or RECTIFY_STATE='900' or RECTIFY_STATE='899')";
    }
    if (("2".equals(yxf)) && (yxf != null)) {
      codition = " and RECTIFY_STATE='010' ";
    }
    if (("0".equals(wcl)) && (wcl != null) && ("1".equals(ygt)) && (ygt != null)) {
      codition = " and ( RECTIFY_STATE='000' or RECTIFY_STATE='005' or RECTIFY_STATE='999' or RECTIFY_STATE='888' or RECTIFY_STATE='020' or RECTIFY_STATE='030' or RECTIFY_STATE='900' or RECTIFY_STATE='899' )";
    }
    if (("1".equals(ygt)) && (ygt != null) && ("2".equals(yxf)) && (yxf != null)) {
      codition = " and ( RECTIFY_STATE='888' or RECTIFY_STATE='020' or RECTIFY_STATE='030' or RECTIFY_STATE='900' or RECTIFY_STATE='899' or RECTIFY_STATE='010' )";
    }
    if (("0".equals(wcl)) && (wcl != null) && ("1".equals(ygt)) && (ygt != null) && ("2".equals(yxf)) && (yxf != null)) {
      codition = " and ( RECTIFY_STATE='000' or RECTIFY_STATE='005' or RECTIFY_STATE='999' or RECTIFY_STATE='888' or RECTIFY_STATE='020' or RECTIFY_STATE='030' or RECTIFY_STATE='900' or RECTIFY_STATE='899' or RECTIFY_STATE='010' )";
    }
    FMPLog.debug("WCL:" + wcl + "===" + "YGT:" + ygt + "===" + "YXF:" + yxf + "===" + "网络安全的condition：" + codition);
    
    GetCountCitySumDel del = new GetCountCitySumDel(this.contex);
    String json = del.getWlaqJson(codition);
    setMsg(json);
    return "empty";
  }
}
