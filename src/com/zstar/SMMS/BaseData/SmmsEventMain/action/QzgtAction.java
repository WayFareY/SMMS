package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QzgtAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String url = (String)getWebData("URL");
    String ip = (String)getWebData("IP");
    
    Map selectMap = new HashMap();
    selectMap.put("URL", url.toUpperCase());
    selectMap.put("IP", ip);
    String rid = "";
    
    String threat_name = "";
    PendingEventDel del = new PendingEventDel(this.contex);
    EventMainDel mainDel = new EventMainDel(this.contex);
    Map insertMap = new HashMap();
    insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(selectMap);
    if ((insertMap != null) && (insertMap.size() > 0))
    {
      if ("1".equals(insertMap.get("MAPPING_MODE")))
      {
        insertMap.put("URL", url.toLowerCase());
      }
      else if ("2".equals(insertMap.get("MAPPING_MODE")))
      {
        String getUrl = (String)insertMap.get("URL");
        insertMap.put("URL", getUrl.toLowerCase());
        insertMap.put("IP", ip);
      }
    }
    else
    {
      insertMap.put("URL", url.toLowerCase());
      
      String domain = mainDel.findDomain(url.toLowerCase());
      insertMap.put("DOMAIN_NAME", domain);
      insertMap.put("IP", ip);
      insertMap.put("MAPPING_MODE", "0");
    }
    insertMap.put("DBTYPE", FMPContex.DBType);
    if (!insertMap.containsKey("MAPPING_MODE")) {
      insertMap.put("MAPPING_MODE", "0");
    }
    threat_name = (String)getWebData("THREAT_NAME");
    
    insertMap.put("FORCE_CLOSE_DESC", getWebData("FORCE_CLOSE_DESC"));
    
    insertMap.put("CLIENTNAME", getWebData("CURR_USERID"));
    
    insertMap.put("IS_FORCE_CLOSE", "1");
    
    insertMap.put("THREAT_LEVEL", "3");
    
    insertMap.put("EVENT_SOURCE", "3");
    if ("".equals(threat_name)) {
      insertMap.put("THREAT_NAME", "未知");
    } else {
      insertMap.put("THREAT_NAME", getWebData("THREAT_NAME"));
    }
    Long total = null;
    
    Map resultMap = new HashMap();
    
    Map map = new HashMap();
    
    String mainRid = "";
    Map eventMap = mainDel.checkEventMain(insertMap, total, resultMap, map);
    mainRid = (String)eventMap.get("MAIN_RID");
    if (!"".equals(mainRid))
    {
      insertMap.put("MAIN_RID", mainRid);
      
      insertMap.put("RID", eventMap.get("DETAIL_RID"));
      del.insertSmmsPendingEvent(insertMap);
      
      insertMap.put("RID", mainRid);
      
      insertMap.put("SYS_RECTIFY_SUGGEST", "2");
      
      insertMap.put("FINAL_RECTIFY_SUGGEST", "2");
      
      insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      insertMap.put("SEND_TIME", "");
      
      insertMap.put("SEND_TIMESTAMP", "");
      
      insertMap.put("FEEDBACK_TIME", "");
      
      insertMap.put("FEEDBACK_TIMESTAMP", "");
      
      int j = this.sqlSession.update("SmmsEventMain.updateSave", insertMap);
      FMPLog.debug("页面强制关停是否更新：" + j);
    }
    RpcBusDel rpcBusdel = new RpcBusDel(this.contex);
    
    Map mapRid = new HashMap();
    mapRid.put("EVENT_RID", mainRid);
    mainRid = "'" + mainRid + "'";
    mapRid.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");
    
    mapRid.put("DETAIL_RID", eventMap.get("DETAIL_RID"));
    
    Long enforceCount = (Long)eventMap.get("ENFORCE_COUNT");
    
    mapRid.put("DBTYPE", FMPContex.DBType);
    
    List mapUrlList = this.sqlSession.selectList("WebCase.viewDoMainNameAndAccessId", selectMap);
    
    String message = "";
    FMPLog.debug("url查询数量" + mapUrlList.size());
    if ((url != null) && (!"".equals(url)))
    {
      if ((mapUrlList != null) && (mapUrlList.size() > 0))
      {
        enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
        mapRid.put("ENFORCE_COUNT", enforceCount);
        message = mainDel.ForceClose(mapRid);
        setMsg(message);
      }
      else
      {
        try
        {
          enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
          mapRid.put("ENFORCE_COUNT", enforceCount);
          message = mainDel.bbcForceClose(mapRid, rpcBusdel);
          setMsg(message);
        }
        catch (Exception e)
        {
          mapRid.put("RECTIFY_STATE", "000");
          mapRid.put("ENFORCE_COUNT", (Long)eventMap.get("ENFORCE_COUNT"));
          mainDel.updateQzgtState(mapRid);
          e.printStackTrace();
          setMsg("无法连接bbcIDC服务器");
        }
      }
    }
    else
    {
      List ipList = this.sqlSession.selectList("SmmsWebCaseIp.selectRidByIp", selectMap);
      FMPLog.debug("ip查询数量" + ipList.size());
      if ((ipList != null) && (ipList.size() > 0))
      {
        Map ipMap = (Map)ipList.get(0);
        
        Map selectUrlByRidMap = (Map)this.sqlSession.selectOne("WebCase.getDomainNameByRid", ipMap);
        if ((selectUrlByRidMap != null) && (selectUrlByRidMap.size() > 0))
        {
          enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
          mapRid.put("ENFORCE_COUNT", enforceCount);
          message = mainDel.ForceClose(mapRid);
          setMsg(message);
        }
      }
      else
      {
        try
        {
          enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
          mapRid.put("ENFORCE_COUNT", enforceCount);
          message = mainDel.bbcForceClose(mapRid, rpcBusdel);
          setMsg(message);
        }
        catch (Exception e)
        {
          e.printStackTrace();
          
          mapRid.put("RECTIFY_STATE", "000");
          mapRid.put("ENFORCE_COUNT", (Long)eventMap.get("ENFORCE_COUNT"));
          mainDel.updateQzgtState(mapRid);
          e.printStackTrace();
          setMsg("无法连接bbc服务器");
        }
      }
    }
    return "empty";
  }
}
