package com.zstar.SMMS.acLog.SmmsAcLogin.delegate;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAcLogDel
  extends BaseDelegate
{
  public ReadAcLogDel(ActionContext contex)
  {
    super(contex);
  }
  
  public List<Map> selectIdcIdAndRoomList()
  {
    return this.sqlSession.selectList("IdcInfo.idcAndRoomList");
  }
  
  public int insertAcLogin(String filePath, String fileName, String tableRow, String sqlId, boolean flag)
  {
    String idcIdAndRoomIdx = filePath.substring(filePath.length() - 15, filePath.length() - 1);
    Map roomIdcMap = selectRoomInfo(filePath);
    List roomIdcList = this.sqlSession.selectList("SmmsRoomInfo.selectRoomNameAndRoomIdx", roomIdcMap);
    Map roomMap = (Map)roomIdcList.get(0);
    String[] rowKey = tableRow.split(",");
    
    int sum = 0;
    PendingEventDel del = new PendingEventDel(this.contex);
    EventMainDel mainDel = new EventMainDel(this.contex);
    
    Map keysMap = new HashMap();
    keysMap.put("DBTYPE", FMPContex.DBType);
    
    Map mainKeyMap = new HashMap();
    
    Map countMap = new HashMap();
    
    Map eventMap = new HashMap();
    
    Map closeMap = new HashMap();
    
    String message = "";
    
    closeMap.put("DBTYPE", FMPContex.DBType);
    try
    {
      InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
      BufferedReader in = new BufferedReader(isr);
      String str = null;
      while ((str = in.readLine()) != null)
      {
        String[] kv = str.split("\\|\\*\\|", 1000);
        
        Map map = new HashMap();
        if (kv.length == rowKey.length)
        {
          for (int j = 0; j < kv.length; j++) {
            map.put(rowKey[j], kv[j]);
          }
          String rid = FMPContex.getNewUUID();
          map.put("RID", rid);
          map.put("CREATTIME", FMPContex.getCurrentTime());
          map.put("RECORDSTATE", "0");
          
          map.put("GROUPNAME", idcIdAndRoomIdx);
          Boolean state = Boolean.valueOf(map.containsKey("CASE_STATE"));
          
          Boolean servcrc = Boolean.valueOf(map.containsKey("SERV_CRC"));
          
          Boolean appcrc = Boolean.valueOf(map.containsKey("APP_CRC"));
          if (!state.booleanValue()) {
            map.put("CASE_STATE", "0");
          }
          if (sqlId.startsWith("SmmsDomainFulx."))
          {
            Map resultMap = new HashMap();
            Map timeMap = new HashMap();
            resultMap = selectRoomInfo(filePath);
            map.put("IDC_ID", resultMap.get("ACCESS_ID"));
            map.put("ROOM_IDX", resultMap.get("ROOM_IDX"));
            timeMap = getRecordMap();
            map.putAll(timeMap);
          }
          int i = this.sqlSession.insert(sqlId, map);
          sum += i;
          if (flag)
          {
            Boolean urlContains = Boolean.valueOf(map.containsKey("URL"));
            Boolean desIpKey = Boolean.valueOf(map.containsKey("DES_IP"));
            Boolean ipKey = Boolean.valueOf(map.containsKey("IP"));
            Boolean keyWords = Boolean.valueOf(map.containsKey("KEYWORDS"));
            String url = "";
            String ip = "";
            if (urlContains.booleanValue())
            {
              url = String.valueOf(map.get("URL"));
              map.put("URL", url.toUpperCase());
            }
            if (desIpKey.booleanValue())
            {
              ip = (String)map.get("DES_IP");
              map.put("IP", ip);
            }
            Map insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(map);
            
            insertMap.put("RECTIFY_STATE", "000");
            if (!insertMap.containsKey("MAPPING_MODE")) {
              insertMap.put("MAPPING_MODE", "0");
            }
            if (urlContains.booleanValue()) {
              insertMap.put("URL", url.toLowerCase());
            }
            if (ipKey.booleanValue())
            {
              ip = (String)map.get("IP");
              insertMap.put("IP", ip);
            }
            if (servcrc.booleanValue()) {
              insertMap.put("EVENT_TYPE2", map.get("SERV_CRC"));
            }
            if (appcrc.booleanValue()) {
              insertMap.put("EVENT_TYPE3", map.get("APP_CRC"));
            }
            if (sqlId.startsWith("SmmsAcAppVpn."))
            {
              insertMap.put("LOG_TABLENAME", "SMMS_AC_APP_VPN");
              if (String.valueOf(map.get("VPN_PROXY").toString()) == "0") {
                insertMap.put("EVENT_TYPE1", "vpn");
              } else {
                insertMap.put("EVENT_TYPE1", "vpno");
              }
            }
            if (sqlId.startsWith("SmmsAcBbsKey."))
            {
              insertMap.put("LOG_TABLENAME", "SMMS_AC_BBS_KEY");
              insertMap.put("EVENT_TYPE1", "bbs");
            }
            if (sqlId.startsWith("SmmsAcMailKey."))
            {
              insertMap.put("LOG_TABLENAME", "SMMS_AC_MAIL_KEY");
              insertMap.put("EVENT_TYPE1", "mail");
            }
            if (sqlId.startsWith("SmmsAcUrlKey."))
            {
              insertMap.put("EVENT_TYPE1", "url");
              insertMap.put("LOG_TABLENAME", "SMMS_AC_URL_KEY");
            }
            if (sqlId.startsWith("SmmsAcProxy."))
            {
              insertMap.put("EVENT_TYPE1", "proxy");
              insertMap.put("LOG_TABLENAME", "SMMS_AC_PROXY");
            }
            insertMap.put("LOG_RID", map.get("RID"));
            
            insertMap.put("CLIENTNAME", "admin");
            
            insertMap.put("EVENT_SOURCE", "1");
            
            insertMap.put("OCCUR_TIME", map.get("LOG_TIME"));
            
            String keyWordType = "";
            
            String keys = "";
            
            String isForClose = "";
            if (keyWords.booleanValue())
            {
              keys = String.valueOf(map.get("KEYWORDS"));
              keysMap.put("KEYS", keys);
              
              Map mapLevel = (Map)this.sqlSession.selectOne("SmmsKeyword.selectKeywordLevel", keysMap);
              String keyWordLevel = (String)mapLevel.get("KEYWORD_LEVEL");
              
              List<Map> typeList = this.sqlSession.selectList("SmmsKeyword.selectKeywordType", keysMap);
              for (Map keyMap : typeList) {
                keyWordType = keyWordType + (String)keyMap.get("KEYWORD_TYPE") + ",";
              }
              if (keyWordType.endsWith(",")) {
                keyWordType = keyWordType.substring(0, keyWordType.length() - 1);
              }
              if ("".equals(keyWordLevel)) {
                insertMap.put("THREAT_LEVEL", "3");
              }
              insertMap.put("THREAT_LEVEL", keyWordLevel);
              insertMap.put("THREAT_TYPE4", keyWordType);
              if (!"".equals(keyWordType)) {
                isForClose = mainDel.getIsClose(null, keyWordType);
              }
              if ("1".equals(isForClose)) {
                insertMap.put("FORCE_CLOSE_DESC", "涉及一级关键字");
              }
              insertMap.put("KEYWORDS", map.get("KEYWORDS"));
            }
            else
            {
              isForClose = "2";
            }
            insertMap.put("ACCESS_ID", roomIdcMap.get("ACCESS_ID"));
            insertMap.put("ROOM_NAME", roomMap.get("ROOM_NAME"));
            insertMap.put("ROOM_IDX", roomIdcMap.get("ROOM_IDX"));
            insertMap.put("IS_FORCE_CLOSE", isForClose);
            
            Map getSuggestMap = mainDel.getSuggestMap(isForClose);
            insertMap.putAll(getSuggestMap);
            if (!insertMap.containsKey("THREAT_LEVEL")) {
              insertMap.put("THREAT_LEVEL", "3");
            }
            if ((sqlId.startsWith("SmmsAcAppVpn.")) || (sqlId.startsWith("SmmsAcBbsKey.")) || (sqlId.startsWith("SmmsAcMailKey.")) || (sqlId.startsWith("SmmsAcUrlKey."))) {
              insertMap.put("SNAPSHOP", map.get("EVENT_EVIDENCE"));
            }
            Long total = null;
            if (i != 0)
            {
              String mainRid = "";
              Map mainEventMap = mainDel.checkEventMain(insertMap, total, eventMap, countMap);
              
              String rectifyState = (String)mainEventMap.get("RECTIFY_STATE");
              mainRid = (String)mainEventMap.get("MAIN_RID");
              if (!"".equals(mainRid))
              {
                insertMap.put("MAIN_RID", mainRid);
                
                insertMap.put("LOG_FILENAME", fileName);
                
                insertMap.put("RID", mainEventMap.get("DETAIL_RID"));
                del.insertSmmsPendingEvent(insertMap);
              }
              String selectKeyWordType = "";
              selectKeyWordType = (String)mainEventMap.get("THREAT_TYPE4");
              
              String selectKeys = "";
              selectKeys = (String)mainEventMap.get("KEYWORDS");
              if (((!"".equals(keyWordType)) && (!"".equals(selectKeyWordType))) || ((!"".equals(keys)) && (!"".equals(selectKeys))))
              {
                String keyTypeAll = keyWordType;
                if (selectKeyWordType.indexOf(",") != -1)
                {
                  String[] keyWord = selectKeyWordType.split(",");
                  for (int j = 0; j < keyWord.length; j++) {
                    if (keyWordType.indexOf(keyWord[j]) == -1) {
                      keyTypeAll = keyTypeAll + "," + keyWord[j];
                    }
                  }
                }
                else if (selectKeyWordType.equals(keyWordType))
                {
                  keyTypeAll = keyTypeAll + "," + selectKeyWordType;
                }
                if (!selectKeyWordType.equals(keyWordType)) {
                  insertMap.put("THREAT_TYPE4", keyTypeAll);
                }
                String keyWordAll = keys;
                if (selectKeys.indexOf(",") != -1)
                {
                  String[] splitKeys = selectKeys.split(",");
                  for (int z = 0; z < splitKeys.length; z++) {
                    if (!keys.equals(splitKeys[z])) {
                      keyWordAll = keyWordAll + "," + splitKeys[z];
                    }
                  }
                }
                else if (!selectKeys.equals(keys))
                {
                  keyWordAll = keyWordAll + "," + selectKeys;
                }
                if (!keyWordAll.equals(keys)) {
                  insertMap.put("KEYWORDS", keyWordAll);
                }
                FMPLog.debug("更新关键字类型----" + selectKeyWordType);
                FMPLog.debug("更新命中关键字----" + selectKeys);
              }
              Map checkMap = mainDel.checkForece(mainEventMap, insertMap);
              FMPLog.debug("比较数据库后数据大小：" + checkMap.size());
              
              insertMap.put("SEND_TIME", "");
              
              insertMap.put("SEND_TIMESTAMP", "");
              
              insertMap.put("FEEDBACK_TIME", "");
              
              insertMap.put("FEEDBACK_TIMESTAMP", "");
              
              insertMap.put("FORCE_CLOSE_DESC", "");
              if (checkMap.containsKey("IS_FORCE_CLOSE"))
              {
                String checkIsClose = (String)checkMap.get("IS_FORCE_CLOSE");
                FMPLog.debug("比较后的是否强制关停：" + checkIsClose);
                if ("1".equals(checkIsClose))
                {
                  insertMap.put("RID", mainEventMap.get("MAIN_RID"));
                  
                  insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
                  if (("020".equals(rectifyState)) || ("888".equals(rectifyState)) || ("999".equals(rectifyState)) || ("900".equals(rectifyState))) {
                    insertMap.put("RECTIFY_STATE", "000");
                  } else if ((!"1".equals(mainEventMap.get("IS_FORCE_CLOSE"))) && ("010".equals(rectifyState))) {
                    insertMap.put("RECTIFY_STATE", "000");
                  } else {
                    insertMap.put("RECTIFY_STATE", rectifyState);
                  }
                  rectifyState = (String)insertMap.get("RECTIFY_STATE");
                  if ((!"2".equals(mainEventMap.get("FINAL_RECTIFY_SUGGEST"))) || (!"000".equals(mainEventMap.get("RECTIFY_STATE")))) {
                    if ((!"1".equals(mainEventMap.get("IS_FORCE_CLOSE"))) || (!"010".equals(rectifyState)))
                    {
                      int j = this.sqlSession.update("SmmsEventMain.updateSave", insertMap);
                      FMPLog.debug("AC关停 待处理日志是否更新成功：" + j);
                    }
                  }
                }
                else
                {
                  insertMap.put("RID", mainEventMap.get("MAIN_RID"));
                  
                  insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
                  if (("888".equals(rectifyState)) || ("999".equals(rectifyState)) || ("900".equals(rectifyState))) {
                    insertMap.put("RECTIFY_STATE", "000");
                  } else {
                    insertMap.put("RECTIFY_STATE", rectifyState);
                  }
                  rectifyState = (String)insertMap.get("RECTIFY_STATE");
                  if ((!"2".equals(mainEventMap.get("IS_FORCE_CLOSE"))) || (!"010".equals(rectifyState))) {
                    if (!"020".equals(mainEventMap.get("RECTIFY_STATE")))
                    {
                      int j = this.sqlSession.update("SmmsEventMain.updateSave", insertMap);
                      FMPLog.debug("AC整改 待处理日志是否更新成功：" + j);
                    }
                  }
                }
              }
              else
              {
                if (("888".equals(rectifyState)) || ("999".equals(rectifyState)) || ("900".equals(rectifyState)))
                {
                  insertMap.put("RID", mainEventMap.get("MAIN_RID"));
                  
                  insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
                  
                  insertMap.put("RECTIFY_STATE", "000");
                  
                  int j = this.sqlSession.update("SmmsEventMain.updateSave", insertMap);
                  FMPLog.debug("af关停 待处理日志是否更新成功：" + j);
                }
                else
                {
                  insertMap.put("RECTIFY_STATE", rectifyState);
                }
                rectifyState = (String)insertMap.get("RECTIFY_STATE");
              }
              closeMap.put("EVENT_RID", mainRid);
              mainRid = "'" + mainRid + "'";
              closeMap.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");
              
              closeMap.put("DETAIL_RID", mainEventMap.get("DETAIL_RID"));
              
              Long enforceCount = (Long)mainEventMap.get("ENFORCE_COUNT");
              enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
              closeMap.put("ENFORCE_COUNT", enforceCount);
              
              String automatic = FMPContex.getSystemParameter("BIZ1");
              
              String issued = FMPContex.getSystemParameter("BIZ2");
              
              boolean resultFlag = mainDel.checkTime(mainEventMap);
              FMPLog.debug("是否为当天：" + resultFlag);
              if ("000".equals(rectifyState))
              {
                if (("1".equals(automatic)) && ("2".equals(insertMap.get("FINAL_RECTIFY_SUGGEST"))))
                {
                  message = mainDel.ForceClose(closeMap);
                  FMPLog.debug("处理状态为000AC关停返回结果:" + message);
                }
                if (("1".equals(issued)) && ("1".equals(insertMap.get("FINAL_RECTIFY_SUGGEST")))) {
                  if (insertMap.containsKey("WEB_CASE_RID"))
                  {
                    message = mainDel.rectification(closeMap);
                    FMPLog.debug("处理状态为000AF下发返回结果:" + message);
                  }
                  else
                  {
                    FMPLog.debug("处理状态为000AC没对接系统下发返回结果");
                    
                    mainDel.updateRectifyState(closeMap);
                    
                    mainDel.updateQzgtStateForPending(closeMap);
                    
                    mainDel.insertEventHis(closeMap);
                  }
                }
              }
            }
          }
        }
      }
      isr.close();
      in.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return sum;
  }
  
  public static Map selectRoomInfo(String filePath)
  {
    String idcId = filePath.substring(filePath.length() - 15, filePath.length() - 5);
    
    String roomIdx = filePath.substring(filePath.length() - 4, filePath.length() - 1);
    
    String idcIdAndRoomIdx = filePath.substring(filePath.length() - 15, filePath.length() - 1);
    
    Map roomIdcMap = new HashMap();
    roomIdcMap.put("ACCESS_ID", idcId);
    roomIdcMap.put("ROOM_IDX", roomIdx);
    
    return roomIdcMap;
  }
  
  public static Map getRecordMap()
  {
    Map recordMap = new HashMap();
    
    Calendar cal = Calendar.getInstance();
    
    int year = cal.get(1);
    
    int month = cal.get(2) + 1;
    
    String date = FMPContex.getCurrentDate();
    
    recordMap.put("STAT_YEAR", Integer.valueOf(year));
    recordMap.put("STAT_MONTH", Integer.valueOf(month));
    recordMap.put("STAT_DATE", date);
    return recordMap;
  }
  
  public void insertRecordMap(String filePath, String logFlag, int sum)
  {
    Map roomIdcMap = selectRoomInfo(filePath);
    
    Map recorderMap = getRecordMap();
    recorderMap.put("LOG_FLAG", logFlag);
    recorderMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    recorderMap.putAll(roomIdcMap);
    
    List<Map> flagList = new ArrayList();
    flagList = this.sqlSession.selectList("SmmsRptRecord.selectExist", recorderMap);
    if ((flagList != null) && (flagList.size() > 0))
    {
      Map flagMap = (Map)flagList.get(0);
      Integer logCount = Integer.valueOf(sum + Integer.valueOf(String.valueOf(flagMap.get("LOG_COUNT"))).intValue());
      recorderMap.put("LOG_COUNT", logCount);
      this.sqlSession.update("SmmsRptRecord.updatLogCount", recorderMap);
    }
    else
    {
      String rid = FMPContex.getNewUUID();
      recorderMap.put("RID", rid);
      recorderMap.put("LOG_COUNT", Integer.valueOf(sum));
      recorderMap.put("CREATTIME", FMPContex.getCurrentTime());
      this.sqlSession.insert("SmmsRptRecord.insertSave", recorderMap);
    }
  }
}
