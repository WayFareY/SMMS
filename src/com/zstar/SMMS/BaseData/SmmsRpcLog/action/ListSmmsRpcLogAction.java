package com.zstar.SMMS.BaseData.SmmsRpcLog.action;

import com.zstar.fmp.core.frame.action.CommonListAction;
import java.util.HashMap;
import java.util.Map;

public class ListSmmsRpcLogAction
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
    
    Map dataMap = new HashMap();
    dataMap.put("RID", TradeOpType);
    if ("1".equals(TradeOpType)) {
      setFreezeCondition(" and srl.creattime =  CURRENT_DATE ");
    } else if ("2".equals(TradeOpType)) {
      setFreezeCondition(" and substring(srl.creattime ,1 ,7) = to_char(now(),'yyyy-mm')");
    } else {
      setFreezeCondition("");
    }
  }
}
