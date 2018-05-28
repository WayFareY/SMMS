package com.zstar.SMMS.flowInstance.parameterClass;

import com.opensymphony.xwork2.ActionContext;
import com.zstar.fmp.workflow.flowEngine.delegate.FlowParamBaseDel;
import java.util.HashMap;
import java.util.Map;

public class WebCloseAppFlowParameterDel
  extends FlowParamBaseDel
{
  public WebCloseAppFlowParameterDel(ActionContext contex)
  {
    super(contex);
    
    addFlowParamInfo("APP_LEVEL", "审批级别");
  }
  
  public Map getFlowParams()
  {
    Map map = new HashMap();
    
    return map;
  }
}
