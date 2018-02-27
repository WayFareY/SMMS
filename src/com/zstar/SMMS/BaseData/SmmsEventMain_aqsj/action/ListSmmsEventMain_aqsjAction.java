package com.zstar.SMMS.BaseData.SmmsEventMain_aqsj.action;

import com.zstar.fmp.core.frame.action.CommonListAction;
import java.util.HashMap;
import java.util.Map;

public class ListSmmsEventMain_aqsjAction
  extends CommonListAction
{
  private static final String filterType = null;
  
  public void afterBiz()
    throws Exception
  {
    super.afterBiz();
  }
  
  public void beforeBiz()
    throws Exception
  {
    super.beforeBiz();
    String TradeOpType = (String)getWebData("filterType");
    String rid = (String)getWebData("RID");
    
    Map dataMap = new HashMap();
    dataMap.put("RID", TradeOpType);
    if ("1".equals(TradeOpType)) {
      setFreezeCondition(" and THREAT_TYPE1 like '%01%' ");
    } else if ("2".equals(TradeOpType)) {
      setFreezeCondition(" and THREAT_TYPE1 like '%02%' ");
    } else if ("3".equals(TradeOpType)) {
      setFreezeCondition(" and THREAT_TYPE1 like '%03%' ");
    } else if ("4".equals(TradeOpType)) {
      setFreezeCondition(" and THREAT_TYPE1 like '%04%' ");
    }
  }
}
