package com.zstar.SMMS.flowInstance.flowClass;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.workflow.flowEngine.delegate.WorkFlowBaseDel;
import java.util.HashMap;
import java.util.Map;

public class WebCloseAppClassDel
  extends WorkFlowBaseDel
{
  public WebCloseAppClassDel(ActionContext contex)
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
    
    Map webCloseMap = (Map)this.sqlSession.selectOne("SmmsWebCloseApp.webCloseInfo", ridMap);
    
    Map getMap = (Map)this.sqlSession.selectOne("SmmsEventMain.forCloseApply", webCloseMap);
    
    Long enforceCount = (Long)getMap.get("ENFORCE_COUNT");
    
    enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
    Map dataMap = new HashMap();
    
    Long closeCount = (Long)getMap.get("CLOSE_COUNT");
    closeCount = Long.valueOf(closeCount.longValue() + 1L);
    String mainRid = (String)webCloseMap.get("RID");
    
    dataMap.put("DBTYPE", FMPContex.DBType);
    dataMap.put("EVENT_RID", mainRid);
    dataMap.put("RID", mainRid);
    dataMap.put("ENFORCE_COUNT", enforceCount);
    dataMap.put("CLOSE_COUNT", closeCount);
    mainRid = "'" + mainRid + "'";
    dataMap.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");
    
    dataMap.put("ENFORCE_USER", webCloseMap.get("LIABMAN"));
    
    dataMap.put("ENFORCE_TIME", FMPContex.getCurrentTime());
    Map detailMap = new HashMap();
    
    detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
    dataMap.putAll(detailMap);
    EventMainDel mainDel = new EventMainDel(getContex());
    RpcBusDel rpcBusdel = new RpcBusDel(this.contex);
    try
    {
      String msg = mainDel.bbcForceClose(dataMap, rpcBusdel);
      if (msg.indexOf("000") != -1)
      {
        ridMap.put("APP_RESULT", "8");
        ridMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int result = this.sqlSession.update("SmmsWebCloseApp.updateAppResult", ridMap);
        FMPLog.debug("更新成功审批结果：" + result);
        
        dataMap.put("RECTIFY_STATE", "899");
        dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int after = this.sqlSession.update("SmmsEventMain.updateReStateAfter", dataMap);
        FMPLog.debug("审批通过后日志事件主表处理状态：" + after);
        
        detailMap.put("RID", detailMap.get("DETAIL_RID"));
        detailMap.put("RECTIFY_STATE", "899");
        detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        int i = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
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
    Map webCloseMap = new HashMap();
    FMPLog.debug("test------------");
    webCloseMap.put("RID", getWebData("bizRid"));
    webCloseMap.put("APP_RESULT", "9");
    webCloseMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int result = this.sqlSession.update("SmmsWebCloseApp.updateAppResult", webCloseMap);
    FMPLog.debug("审批后：" + String.valueOf(result));
    
    Map webOpenEventRid = (Map)this.sqlSession.selectOne("SmmsWebCloseApp.webCloseInfo", webCloseMap);
    Map dataMap = new HashMap();
    dataMap.put("RID", webOpenEventRid.get("RID"));
    
    dataMap.put("RECTIFY_STATE", "000");
    dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    dataMap.put("IS_FORCE_CLOSE", "2");
    dataMap.put("SYS_RECTIFY_SUGGEST", "1");
    dataMap.put("FINAL_RECTIFY_SUGGEST", "1");
    int after = this.sqlSession.update("SmmsEventMain.updateCloseReStateAfter", dataMap);
    FMPLog.debug("审批拒接后处理状态：" + after);
    
    Map detailMap = new HashMap();
    detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
    detailMap.put("RID", detailMap.get("DETAIL_RID"));
    detailMap.put("RECTIFY_STATE", "000");
    detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    dataMap.put("IS_FORCE_CLOSE", "2");
    dataMap.put("SYS_RECTIFY_SUGGEST", "1");
    dataMap.put("FINAL_RECTIFY_SUGGEST", "1");
    int j = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
    return "000";
  }
  
  public String startFlow()
  {
    FMPLog.debug("rid:" + getWebData("RID"));
    Map webCloseMap = new HashMap();
    webCloseMap.put("RID", getWebData("RID"));
    webCloseMap.put("APP_RESULT", "1");
    webCloseMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int result = this.sqlSession.update("SmmsWebCloseApp.updateAppResult", webCloseMap);
    FMPLog.debug("审批前更新：" + String.valueOf(result));
    
    Map mapRid = (Map)this.sqlSession.selectOne("SmmsWebCloseApp.selectEventRid", webCloseMap);
    
    mapRid.put("RECTIFY_STATE", "005");
    
    mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int i = this.sqlSession.update("SmmsEventMain.updateQzgtState", mapRid);
    FMPLog.debug("流程启动更新状态为关停中：" + i);
    
    Map detailMap = new HashMap();
    detailMap.put("RID", mapRid.get("EVENT_RID"));
    detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", detailMap);
    detailMap.put("RID", detailMap.get("DETAIL_RID"));
    detailMap.put("RECTIFY_STATE", "005");
    detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int j = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
    return "";
  }
  
  public String untread(String toNodeId)
  {
    return "000";
  }
  
  public void afterBiz(String bizFlag)
    throws Exception
  {
    "startFlow".equals(bizFlag);
  }
  
  public void beforeBiz(String bizFlag)
    throws Exception
  {}
}
