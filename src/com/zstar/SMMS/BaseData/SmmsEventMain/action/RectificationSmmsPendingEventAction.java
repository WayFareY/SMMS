package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.Map;

public class RectificationSmmsPendingEventAction
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
    ReadAcLogDel acDel = new ReadAcLogDel(this.contex);
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
      String domainName = mainDel.findDomain(url.toLowerCase());
      insertMap.put("DOMAIN_NAME", domainName);
      insertMap.put("IP", ip);
      insertMap.put("MAPPING_MODE", "0");
    }
    insertMap.put("DBTYPE", FMPContex.DBType);
    if (!insertMap.containsKey("MAPPING_MODE")) {
      insertMap.put("MAPPING_MODE", "0");
    }
    threat_name = (String)getWebData("THREAT_NAME");
    
    insertMap.put("CLIENTNAME", getWebData("CURR_USERID"));
    
    insertMap.put("IS_FORCE_CLOSE", "2");
    
    insertMap.put("THREAT_LEVEL", "3");
    
    insertMap.put("EVENT_SOURCE", "3");
    if ("".equals(threat_name)) {
      insertMap.put("THREAT_NAME", "未知");
    } else {
      insertMap.put("THREAT_NAME", threat_name);
    }
    Long total = null;
    
    Map resultMap = new HashMap();
    
    Map map = new HashMap();
    
    String mainRid = "";
    Map eventMap = mainDel.checkEventMain(insertMap, total, resultMap, map);
    Long enforceCount = null;
    mainRid = (String)eventMap.get("MAIN_RID");
    if (!"".equals(mainRid))
    {
      insertMap.put("MAIN_RID", mainRid);
      
      insertMap.put("RID", eventMap.get("DETAIL_RID"));
      int i = del.insertSmmsPendingEvent(insertMap);
      if ((insertMap.containsKey("ACCESS_ID")) && (insertMap.containsKey("ROOM_IDX")))
      {
        String path = (String)insertMap.get("ACCESS_ID") + "," + (String)insertMap.get("ROOM_IDX");
        acDel.insertRecordMap(path, "999", i);
      }
      insertMap.put("RID", mainRid);
      
      insertMap.put("SYS_RECTIFY_SUGGEST", "1");
      insertMap.put("FINAL_RECTIFY_SUGGEST", "1");
      
      insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      insertMap.put("SEND_TIME", "");
      
      insertMap.put("SEND_TIMESTAMP", "");
      
      insertMap.put("FEEDBACK_TIME", "");
      
      insertMap.put("FEEDBACK_TIMESTAMP", "");
      int j = this.sqlSession.update("SmmsEventMain.updateSave", insertMap);
      FMPLog.debug("页面下发是否更新：" + j);
    }
    Map mapRid = new HashMap();
    mapRid.put("EVENT_RID", mainRid);
    mainRid = "'" + mainRid + "'";
    mapRid.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");
    
    mapRid.put("DETAIL_RID", eventMap.get("DETAIL_RID"));
    enforceCount = (Long)eventMap.get("ENFORCE_COUNT");
    enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
    mapRid.put("ENFORCE_COUNT", enforceCount);
    
    mapRid.put("ENFORCE_USER", getWebData("CURR_USERID"));
    
    mapRid.put("ENFORCE_TIME", FMPContex.getCurrentTime());
    
    String message = "";
    if ("1".equals(insertMap.get("MAPPING_MODE")))
    {
      message = mainDel.rectification(mapRid);
      setMsg(message);
    }
    else if ("2".equals(insertMap.get("MAPPING_MODE")))
    {
      message = mainDel.rectification(mapRid);
      setMsg(message);
    }
    else
    {
      mainDel.updateRectifyState(mapRid);
      message = "000:处理成功";
      setMsg(message);
    }
    return "empty";
  }
}
