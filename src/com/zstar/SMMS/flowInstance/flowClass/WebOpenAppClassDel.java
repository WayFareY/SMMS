package com.zstar.SMMS.flowInstance.flowClass;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.workflow.flowEngine.delegate.WorkFlowBaseDel;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class WebOpenAppClassDel
  extends WorkFlowBaseDel
{
  public WebOpenAppClassDel(ActionContext contex)
  {
    super(contex);
  }
  
  public String agree()
  {
    return "000";
  }
  
  public String disagree()
  {
    return "000";
  }
  
  public String disclaim()
  {
    return "000";
  }
  
  public String finish(String approveState)
  {
    Map ridMap = new HashMap();
    
    FMPLog.debug("rid:" + getWebData("RID"));
    FMPLog.debug("bizrid:" + getWebData("bizRid"));
    ridMap.put("RID", getWebData("bizRid"));
    
    Map webOpenMap = (Map)this.sqlSession.selectOne("SmmsWebOpenApp.webOpenInfo", ridMap);
    
    Map dataMap = new HashMap();
    dataMap.put("EVENT_RID", webOpenMap.get("RID"));
    dataMap.put("RID", webOpenMap.get("RID"));
    EventMainDel mainDel = new EventMainDel(getContex());
    try
    {
      String msg = mainDel.bbcBlocking(dataMap);
      if (msg.indexOf("000") != -1)
      {
        ridMap.put("APP_RESULT", "8");
        ridMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int result = this.sqlSession.update("SmmsWebOpenApp.updateAppResult", ridMap);
        FMPLog.debug("更新成功审批结果：" + result);
        
        Map detailMap = new HashMap();
        detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
        detailMap.put("RID", detailMap.get("DETAIL_RID"));
        detailMap.put("RECTIFY_STATE", "900");
        detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int j = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
        FMPLog.debug("更新成功处理状态：" + j);
        
        dataMap.put("RECTIFY_STATE", "900");
        dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int after = this.sqlSession.update("SmmsEventMain.updateReStateAfter", dataMap);
        FMPLog.debug("审批通过后日志事件主表处理状态：" + after);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      return "连接不了BBC服务器";
    }
    return "000";
  }
  
  public String reject()
  {
    Map webOpenMap = new HashMap();
    FMPLog.debug("rid:" + getWebData("RID"));
    FMPLog.debug("bizrid:" + getWebData("bizRid"));
    
    webOpenMap.put("RID", getWebData("bizRid"));
    webOpenMap.put("APP_RESULT", "9");
    webOpenMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int result = this.sqlSession.update("SmmsWebOpenApp.updateAppResult", webOpenMap);
    FMPLog.debug("审批后：" + String.valueOf(result));
    
    Map webOpenEventRid = (Map)this.sqlSession.selectOne("SmmsWebOpenApp.webOpenInfo", webOpenMap);
    Map dataMap = new HashMap();
    dataMap.put("RID", webOpenEventRid.get("RID"));
    
    dataMap.put("RECTIFY_STATE", "888");
    dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int after = this.sqlSession.update("SmmsEventMain.updateReStateAfter", dataMap);
    FMPLog.debug("审批拒接后处理状态：" + after);
    
    Map detailMap = new HashMap();
    detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
    detailMap.put("RID", detailMap.get("DETAIL_RID"));
    detailMap.put("RECTIFY_STATE", "900");
    detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int j = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
    
    return "000";
  }
  
  public String startFlow()
  {
    FMPLog.debug("rid:" + getWebData("RID"));
    Map webOpenMap = new HashMap();
    webOpenMap.put("RID", getWebData("RID"));
    
    Map webOpenEventRid = (Map)this.sqlSession.selectOne("SmmsWebOpenApp.webOpenInfo", webOpenMap);
    if ("2".equals(webOpenEventRid.get("IS_ACCEPT")))
    {
      webOpenMap.put("APP_RESULT", "9");
      
      webOpenMap.put("BIZRID", getWebData("RID"));
      
      webOpenMap.put("APPROVERSET", "900");
      int i = this.sqlSession.update("WfFlowInstance.updatefApprovestate", webOpenMap);
      System.out.println("流程实例是否更新成功:" + i);
    }
    else
    {
      webOpenMap.put("APP_RESULT", "1");
    }
    webOpenMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int result = this.sqlSession.update("SmmsWebOpenApp.updateAppResult", webOpenMap);
    
    FMPLog.debug("审批前更新：" + String.valueOf(result));
    
    return "";
  }
  
  public String untread(String toNodeId)
  {
    return "000";
  }
}
