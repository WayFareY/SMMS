package com.zstar.SMMS.constant;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCountMessageDel
  extends BaseDelegate
{
  public GetCountMessageDel(ActionContext contex)
  {
    super(contex);
  }
  
  public Map getIdcCount()
  {
    Map map = (Map)this.sqlSession.selectOne("IdcInfo.getIdcCount");
    return map;
  }
  
  public Map getIdcRoomIdxCount()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsRoomInfo.getIdcRoomIdxCount");
    return map;
  }
  
  public Map getIdcWebCount()
  {
    Map map = (Map)this.sqlSession.selectOne("WebCase.getIdcWebCount");
    return map;
  }
  
  public Map getCountWebCaseRid()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountWebCaseRid");
    return map;
  }
  
  public Map getCountModifiedtime()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountModifiedtime");
    return map;
  }
  
  public Map getCountRectifyStateEight()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountRectifyStateEight");
    return map;
  }
  
  public Map getCountRectifyStateNoEight()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountRectifyStateNoEight");
    return map;
  }
  
  public Map getCountIsForceCloseOne()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIsForceCloseOne");
    return map;
  }
  
  public Map getCountIsForceCloseNoOne()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIsForceCloseNoOne");
    return map;
  }
  
  public Map getCountIsForceCloseTwo()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIsForceCloseTwo");
    return map;
  }
  
  public Map getCountIsForceCloseNoTwo()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIsForceCloseNoTwo");
    return map;
  }
  
  public Map getCountThreatTypeOneAll()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeOneAll");
    return map;
  }
  
  public Map getCountThreatTypeOne()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeOne");
    return map;
  }
  
  public Map getCountThreatTypeTwo()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeTwo");
    return map;
  }
  
  public Map getCountThreatTypeThree()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeThree");
    return map;
  }
  
  public Map getCountThreatTypeTwoAll()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeTwoAll");
    return map;
  }
  
  public Map getCountThreatTypeFour()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeFour");
    return map;
  }
  
  public Map getCountThreatTypeFive()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeFive");
    return map;
  }
  
  public Map getCountThreatTypeSix()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountThreatTypeSix");
    return map;
  }
  
  public List getCountThreatTypeAll()
  {
    List threatTypeList = this.sqlSession.selectList("SmmsEventMain.getCountThreatTypeAll");
    return threatTypeList;
  }
  
  public List getCountEventTypeAll()
  {
    List eventTypeList = this.sqlSession.selectList("SmmsEventMain.getCountEventTypeAll");
    return eventTypeList;
  }
  
  public List getCountThreatTypeBase()
  {
    List threatTypeList = this.sqlSession.selectList("SmmsEventMain.getCountThreatByState");
    return threatTypeList;
  }
  
  public List getCountEventTypeBase()
  {
    List eventTypeList = this.sqlSession.selectList("SmmsEventMain.getCountEventByState");
    return eventTypeList;
  }
  
  public List getCountThreatTypeToTen()
  {
    List eventTypeList = this.sqlSession.selectList("SmmsEventMain.getCountThreatTypeToTen");
    return eventTypeList;
  }
  
  public List getCountThreatTypeToTenNoDel()
  {
    List eventTypeList = this.sqlSession.selectList("SmmsEventMain.getCountThreatTypeToTenNoDel");
    return eventTypeList;
  }
  
  public void setTimeMap()
  {
    String afFilePath = FMPContex.getSystemProperty("AF_LOG_FILE");
    File file = new File(afFilePath);
    if (file.exists()) {
      SMMSConstant.AF_TimeMap.put("AF_LOG_NORMAL", FMPContex.getCurrentTime());
    }
    ReadAcLogDel acDel = new ReadAcLogDel(getContex());
    List<Map> list = acDel.selectIdcIdAndRoomList();
    String idcFilePath = "";
    for (Map map : list)
    {
      String roomIdx = String.valueOf(map.get("ROOM_IDX"));
      
      String idcId = String.valueOf(map.get("IDC_ID"));
      
      idcFilePath = idcId + "/" + roomIdx;
      
      String acFilePath = FMPContex.getSystemProperty("AC_LOG_PATH") + idcFilePath + "/";
      
      String acKeyFilePath = FMPContex.getSystemProperty("AC_LOG_KEY_PATH") + idcFilePath + "/";
      FileUtil.makeDirs(acFilePath);
      FileUtil.makeDirs(acKeyFilePath + "/htm");
      
      String acLog = idcId + "_" + roomIdx;
      
      List<String> acFileNameList = FileUtil.outFileName(acFilePath);
      List<String> acKeyFileNameList = FileUtil.outFileName(acKeyFilePath);
      if ((acFileNameList.size() > 0) || (acKeyFileNameList.size() > 1)) {
        SMMSConstant.AC_TimeMap.put(acLog, FMPContex.getCurrentTime());
      } else if (!SMMSConstant.AC_TimeMap.containsKey(acLog)) {
        SMMSConstant.AC_TimeMap.put(acLog, FMPContex.getCurrentTime());
      }
    }
  }
  
  public Map getAcCount()
  {
    Map countMap = new HashMap();
    
    double acLogInterVal = Double.valueOf(FMPContex.getSystemProperty("AC_LOG_INTERVAL")).doubleValue();
    int acOnlineSum = 0;
    int acOfflineSum = 0;
    String currentTime = FMPContex.getCurrentTime();
    if (SMMSConstant.AC_TimeMap.size() > 0) {
      for (String key : SMMSConstant.AC_TimeMap.keySet())
      {
        FMPLog.debug(key + "---" + (String)SMMSConstant.AC_TimeMap.get(key));
        String testTime = (String)SMMSConstant.AC_TimeMap.get(key);
        FMPLog.debug("ac时间：" + testTime);
        double time = TimeUtil.compareDifference(currentTime, testTime);
        FMPLog.debug("比较后的时间：" + time);
        if (time < acLogInterVal) {
          acOnlineSum++;
        } else {
          acOfflineSum++;
        }
      }
    }
    FMPLog.debug("ac在线台数：" + acOnlineSum);
    FMPLog.debug("ac离线台数：" + acOfflineSum);
    countMap.put("AC_ONLINE", Integer.valueOf(acOnlineSum));
    countMap.put("AC_OFFLINE", Integer.valueOf(acOfflineSum));
    return countMap;
  }
  
  public int intsertSmmsBaseStat()
  {
    Map baseMap = new HashMap();
    
    Map idcCountMap = getIdcCount();
    baseMap.putAll(idcCountMap);
    
    Map roomCountMap = getIdcRoomIdxCount();
    baseMap.putAll(roomCountMap);
    
    Map bussyCountMap = getIdcWebCount();
    baseMap.putAll(bussyCountMap);
    
    Map noCaseMap = getCountWebCaseRid();
    baseMap.putAll(noCaseMap);
    
    Map monthEventMap = getCountModifiedtime();
    baseMap.putAll(monthEventMap);
    
    Map monthDealtMap = getCountRectifyStateEight();
    baseMap.putAll(monthDealtMap);
    
    Map monthNotDealtMap = getCountRectifyStateNoEight();
    baseMap.putAll(monthNotDealtMap);
    
    Map closedCountMap = getCountIsForceCloseOne();
    baseMap.putAll(closedCountMap);
    
    Map needClosedCountMap = getCountIsForceCloseNoOne();
    baseMap.putAll(needClosedCountMap);
    
    Map rectifyCountMap = getCountIsForceCloseTwo();
    baseMap.putAll(rectifyCountMap);
    
    Map needRectifyCountMap = getCountIsForceCloseNoTwo();
    baseMap.putAll(needRectifyCountMap);
    
    Map keyOneMap = getCountThreatTypeOne();
    baseMap.putAll(keyOneMap);
    
    Map keyTwoMap = getCountThreatTypeTwo();
    baseMap.putAll(keyTwoMap);
    
    Map keyThreeMap = getCountThreatTypeThree();
    baseMap.putAll(keyThreeMap);
    
    Map keyFourMap = getCountThreatTypeFour();
    baseMap.putAll(keyFourMap);
    
    Map keyFiveMap = getCountThreatTypeFive();
    baseMap.putAll(keyFiveMap);
    
    Map keySixMap = getCountThreatTypeSix();
    baseMap.putAll(keySixMap);
    
    String currentTime = FMPContex.getCurrentTime();
    
    baseMap.put("RID", FMPContex.getNewUUID());
    
    baseMap.put("CREATTIME", currentTime);
    
    baseMap.put("RECORDSTATE", "0");
    
    baseMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    
    double afLogInterVal = Double.valueOf(FMPContex.getSystemProperty("AF_LOG_INTERVAL")).doubleValue();
    if (SMMSConstant.AF_TimeMap.size() > 0)
    {
      String normalTime = (String)SMMSConstant.AF_TimeMap.get("AF_LOG_NORMAL");
      FMPLog.debug("AF接受时间：" + normalTime);
      
      double afTime = TimeUtil.compareDifference(currentTime, normalTime);
      if (afTime < afLogInterVal)
      {
        baseMap.put("AF_ONLINE", "1");
        
        baseMap.put("AF_OFFLINE", "0");
      }
      else
      {
        baseMap.put("AF_ONLINE", "0");
        baseMap.put("AF_OFFLINE", "0");
      }
    }
    else
    {
      baseMap.put("AF_ONLINE", "0");
      baseMap.put("AF_OFFLINE", "0");
    }
    Map<String, Object> acCountMap = getAcCount();
    baseMap.putAll(acCountMap);
    
    int deleteResult = this.sqlSession.delete("SmmsBaseStat.deleteTableData");
    FMPLog.debug("删除首页汇总-基本信息统计：" + deleteResult);
    int i = this.sqlSession.insert("SmmsBaseStat.insertSave", baseMap);
    FMPLog.debug("添加首页汇总-基本信息统计：" + i);
    return i;
  }
  
  public Map getSmmsBaseStat(Map map)
  {
    Map baseMap = new HashMap();
    if (!map.containsKey("ACCESS_ID"))
    {
      Map idcCountMap = getIdcCount();
      baseMap.putAll(idcCountMap);
      
      Map roomCountMap = getIdcRoomIdxCount();
      baseMap.putAll(roomCountMap);
      
      Map bussyCountMap = getIdcWebCount();
      baseMap.putAll(bussyCountMap);
      
      String currentTime = FMPContex.getCurrentTime();
      
      double afLogInterVal = Double.valueOf(FMPContex.getSystemProperty("AF_LOG_INTERVAL")).doubleValue();
      if (SMMSConstant.AF_TimeMap.size() > 0)
      {
        String normalTime = (String)SMMSConstant.AF_TimeMap.get("AF_LOG_NORMAL");
        FMPLog.debug("AF接受时间：" + normalTime);
        
        double afTime = TimeUtil.compareDifference(currentTime, normalTime);
        if (afTime < afLogInterVal)
        {
          baseMap.put("AF_ONLINE", "1");
          
          baseMap.put("AF_OFFLINE", "0");
        }
        else
        {
          baseMap.put("AF_ONLINE", "0");
          baseMap.put("AF_OFFLINE", "0");
        }
      }
      else
      {
        baseMap.put("AF_ONLINE", "0");
        baseMap.put("AF_OFFLINE", "0");
      }
      Map<String, Object> acCountMap = getAcCount();
      for (String key : acCountMap.keySet()) {
        FMPLog.debug("AC台数key:" + key + "===" + "ACvalue:" + acCountMap.get(key));
      }
      baseMap.putAll(acCountMap);
    }
    Map noCaseMap = getCountWebCaseRid();
    baseMap.putAll(noCaseMap);
    
    Map monthEventMap = getCountModifiedtime();
    baseMap.putAll(monthEventMap);
    
    Map monthDealtMap = getCountRectifyStateEight();
    baseMap.putAll(monthDealtMap);
    
    Map monthNotDealtMap = getCountRectifyStateNoEight();
    baseMap.putAll(monthNotDealtMap);
    
    return baseMap;
  }
  
  public int insertSmmsEventTop()
  {
    int sum = 0;
    
    int deleteResult = this.sqlSession.delete("SmmsEventTop10.deleteTableData");
    FMPLog.debug("删除首页汇总-威胁事件top10：" + deleteResult);
    List<Map> topTenList = getCountThreatTypeToTen();
    List<Map> topTenListNoList = getCountThreatTypeToTenNoDel();
    
    List oldList = this.sqlSession.selectList("IdcInfo.IdcIdaAll");
    for (Map topMap : topTenList)
    {
      topMap.put("RID", FMPContex.getNewUUID());
      
      topMap.put("CREATTIME", FMPContex.getCurrentTime());
      
      topMap.put("RECORDSTATE", "0");
      
      topMap.put("DATAFLAG", "1");
      
      topMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      int j = this.sqlSession.insert("SmmsEventTop10.insertSave", topMap);
      sum += j;
    }
    for (Map topNodelMap : topTenListNoList)
    {
      topNodelMap.put("RID", FMPContex.getNewUUID());
      
      topNodelMap.put("CREATTIME", FMPContex.getCurrentTime());
      
      topNodelMap.put("RECORDSTATE", "0");
      
      topNodelMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      topNodelMap.put("DATAFLAG", "2");
      int j = this.sqlSession.insert("SmmsEventTop10.insertSave", topNodelMap);
      sum += j;
    }
    return sum;
  }
  
  public int insertSmmsEventStat()
  {
    int deleteResult = this.sqlSession.delete("SmmsEventStat.deleteTableData");
    FMPLog.debug("插入首页汇总-网络安全事件统计：" + deleteResult);
    int sum = 0;
    List<Map> eventStatListOne = getCount("1");
    FMPLog.debug("daxiao1:" + eventStatListOne.size());
    int j;
    for (Map eventStatMap : eventStatListOne)
    {
      eventStatMap.put("RID", FMPContex.getNewUUID());
      
      eventStatMap.put("CREATTIME", FMPContex.getCurrentTime());
      
      eventStatMap.put("RECORDSTATE", "0");
      
      eventStatMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      eventStatMap.put("DATAFLAG", "1");
      j = this.sqlSession.insert("SmmsEventStat.insertSave", eventStatMap);
      sum += j;
    }
    List<Map> eventStatListTwo = getCount("2");
    FMPLog.debug("daxiao2:" + eventStatListTwo.size());
    for (Map eventStatMap : eventStatListTwo)
    {
      eventStatMap.put("RID", FMPContex.getNewUUID());
      
      eventStatMap.put("CREATTIME", FMPContex.getCurrentTime());
      
      eventStatMap.put("RECORDSTATE", "0");
      
      eventStatMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      eventStatMap.put("DATAFLAG", "2");
      int j = this.sqlSession.insert("SmmsEventStat.insertSave", eventStatMap);
      sum += j;
    }
    return sum;
  }
  
  public String eventJson(Map map)
  {
    Map jsonMap = new HashMap();
    
    Map zgclMap = new HashMap();
    String json = "";
    
    Map ss1DzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Dzg", map);
    Map ss1ZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Zgz", map);
    Map ss1YzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Yzg", map);
    
    ss1DzgMap.putAll(ss1ZgzMap);
    ss1DzgMap.putAll(ss1YzgMap);
    zgclMap.put("SZWK", ss1DzgMap);
    
    Map ss2DzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Dzg", map);
    Map ss2ZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Zgz", map);
    Map ss2YzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Yzg", map);
    
    ss2DzgMap.putAll(ss2ZgzMap);
    ss2DzgMap.putAll(ss2YzgMap);
    zgclMap.put("SWDU", ss2DzgMap);
    
    Map AqsjDzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjDzg", map);
    Map AqsjZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjZgz", map);
    Map AqsjYzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjYzg", map);
    
    AqsjDzgMap.putAll(AqsjZgzMap);
    AqsjDzgMap.putAll(AqsjYzgMap);
    zgclMap.put("AQSJ", AqsjDzgMap);
    
    Map wbaDzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcWbaDzg", map);
    Map wbaZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcWbaZgz", map);
    Map wbaYzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcWbaYzg", map);
    
    wbaDzgMap.putAll(wbaZgzMap);
    wbaDzgMap.putAll(wbaYzgMap);
    zgclMap.put("WBA", wbaDzgMap);
    
    Map baseStatMap = getSmmsBaseStat(map);
    
    List<Map> eventTopListOne = this.sqlSession.selectList("SmmsEventTop10.selectData", map);
    
    List<Map> eventTopListTwo = this.sqlSession.selectList("SmmsEventTop10.selectDataByDataFlagTwo", map);
    
    List<Map> eventStatListOne = this.sqlSession.selectList("SmmsEventStat.selectData", map);
    
    List<Map> eventStatListTwo = this.sqlSession.selectList("SmmsEventStat.selectDataByDataFlagTwo", map);
    jsonMap.put("ZGYW", zgclMap);
    
    json = JsonUtil.dataMapToJson(jsonMap);
    FMPLog.debug("组织 事件的json:" + json);
    return json;
  }
  
  public String getStatJson(Map map)
  {
    Map jsonMap = new HashMap();
    Map baseStatMap = getSmmsBaseStat(map);
    
    List<Map> eventTopListOne = this.sqlSession.selectList("SmmsEventTop10.selectData", map);
    
    List<Map> eventTopListTwo = this.sqlSession.selectList("SmmsEventTop10.selectDataByDataFlagTwo", map);
    
    List<Map> eventStatListOne = this.sqlSession.selectList("SmmsEventStat.selectData", map);
    
    List<Map> eventStatListTwo = this.sqlSession.selectList("SmmsEventStat.selectDataByDataFlagTwo", map);
    jsonMap.put("SMMS_BASE_STAT", baseStatMap);
    jsonMap.put("SMMS_EVENT_TOP10_COUNT", eventTopListOne);
    jsonMap.put("SMMS_EVENT_TOP10_CURRENT", eventTopListTwo);
    jsonMap.put("SMMS_EVENT_STAT_COUNT", eventStatListOne);
    jsonMap.put("SMMS_EVENT_STAT_CURRENT", eventStatListTwo);
    
    String json = JsonUtil.dataMapToJson(jsonMap);
    FMPLog.debug("基础数据的json:" + jsonMap);
    return json;
  }
  
  public String getZgclEvent(Map map)
  {
    Map zgclMap = new HashMap();
    
    Map ss1DzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Dzg", map);
    Map ss1ZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Zgz", map);
    Map ss1YzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Yzg", map);
    
    ss1DzgMap.putAll(ss1ZgzMap);
    ss1DzgMap.putAll(ss1YzgMap);
    zgclMap.put("SZWK", ss1DzgMap);
    
    Map ss2DzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Dzg", map);
    Map ss2ZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Zgz", map);
    Map ss2YzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Yzg", map);
    
    ss2DzgMap.putAll(ss2ZgzMap);
    ss2DzgMap.putAll(ss2YzgMap);
    zgclMap.put("SHDU", ss2DzgMap);
    
    Map AqsjDzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjDzg", map);
    Map AqsjZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjZgz", map);
    Map AqsjYzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjYzg", map);
    
    AqsjDzgMap.putAll(AqsjZgzMap);
    AqsjDzgMap.putAll(AqsjYzgMap);
    zgclMap.put("AQSJ", AqsjDzgMap);
    
    Map wbaDzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcWbaDzg", map);
    Map wbaZgzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcWbaZgz", map);
    Map wbaYzgMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcWbaYzg", map);
    
    wbaDzgMap.putAll(wbaZgzMap);
    wbaDzgMap.putAll(wbaYzgMap);
    zgclMap.put("WBA", wbaDzgMap);
    
    String json = JsonUtil.dataMapToJson(zgclMap);
    FMPLog.debug("页面整改业务的json:" + zgclMap);
    return json;
  }
  
  public String getGtywEvent(Map map)
  {
    Map gtywMap = new HashMap();
    
    Map ss1YgTMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Ygt", map);
    
    Map ss1SqhfMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1Sqhf", map);
    
    Map ss1CloseShzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1CloseShz", map);
    Map ss1OpenShzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs1OpenShz", map);
    Map ss1ShzMap = new HashMap();
    ss1ShzMap.put("SS1SHZ", Long.valueOf(((Long)ss1CloseShzMap.get("SS1CLOSESHZ")).longValue() + ((Long)ss1OpenShzMap.get("SS1OPENSHZ")).longValue()));
    
    ss1YgTMap.putAll(ss1SqhfMap);
    ss1YgTMap.putAll(ss1ShzMap);
    gtywMap.put("SZWK", ss1YgTMap);
    
    Map ss2YgtMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Ygt", map);
    
    Map ss2SqhfMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2Sqhf", map);
    
    Map ss2CloseShzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2CloseShz", map);
    Map ss2OpenShzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcSs2OpenShz", map);
    Map ss2ShzMap = new HashMap();
    ss2ShzMap.put("SS2SHZ", Long.valueOf(((Long)ss2CloseShzMap.get("SS2CLOSESHZ")).longValue() + ((Long)ss2OpenShzMap.get("SS2OPENSHZ")).longValue()));
    
    ss2YgtMap.putAll(ss2SqhfMap);
    ss2YgtMap.putAll(ss2ShzMap);
    gtywMap.put("SHDU", ss2YgtMap);
    
    Map aqsjYgtMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjYgt", map);
    Map aqsjSqhfMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjSqhf", map);
    Map aqsjCloseShzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjCloseShz", map);
    Map aqsjOpenShzMap = (Map)this.sqlSession.selectOne("SmmsEventMain.getCountIdcAqsjOpenShz", map);
    Map aqsjShzMap = new HashMap();
    aqsjShzMap.put("AQSJSHZ", Long.valueOf(((Long)aqsjCloseShzMap.get("AQSJCLOSESHZ")).longValue() + ((Long)aqsjOpenShzMap.get("AQSJOPENSHZ")).longValue()));
    
    aqsjYgtMap.putAll(aqsjSqhfMap);
    aqsjYgtMap.putAll(aqsjShzMap);
    gtywMap.put("AQSJ", aqsjYgtMap);
    String json = JsonUtil.dataMapToJson(gtywMap);
    FMPLog.debug("页面关停业务的json:" + gtywMap);
    return json;
  }
  
  public List getCount(String contant)
  {
    List<Map> roomIdxList = new ArrayList();
    roomIdxList = this.sqlSession.selectList("SmmsRoomInfo.selectRoomIdxList");
    List<Map> list1 = new ArrayList();
    List<Map> list2 = new ArrayList();
    if ("1".equals(contant))
    {
      list1 = getCountThreatTypeAll();
      
      list2 = getCountEventTypeAll();
    }
    else if ("2".equals(contant))
    {
      System.out.println("22222v" + contant);
      list1 = getCountThreatTypeBase();
      
      list2 = getCountEventTypeBase();
    }
    else
    {
      list1 = null;
      list2 = null;
    }
    if ((list1 == null) && (list1.isEmpty()) && (list2 == null) && (list2.isEmpty())) {
      return roomIdxList;
    }
    list1 = ReGroupUtil.addMap(list1);
    
    list2 = ReGroupUtil.addMap(list2);
    
    roomIdxList = ReGroupUtil.reGroupList(roomIdxList, list1);
    roomIdxList = ReGroupUtil.reGroupList(roomIdxList, list2);
    return roomIdxList;
  }
  
  public String getWxxfJson()
  {
    List<Map> roomIdxList = new ArrayList();
    roomIdxList = this.sqlSession.selectList("SmmsRoomInfo.selectRoomIdxList");
    for (Map map : roomIdxList)
    {
      map.put("ALL_COUNT", "0");
      map.put("ROOM_COUNT", "0");
      
      map.put("SMBSM", "0");
      map.put("LDSM", "0");
      map.put("webshellSCGJ", "0");
      map.put("TYXTX", "0");
      map.put("WGFWMG", "0");
      map.put("WYRJSalityJZ", "0");
      map.put("WYRJGbotJZ", "0");
      map.put("EYRJSdbotJZ", "0");
      map.put("EYRJZeusZJ", "0");
      map.put("JCEJSWL", "0");
      map.put("IRCJSWL", "0");
      map.put("LSRJWannaCry", "0");
      map.put("FKRC", "0");
      map.put("RamnitJZ", "0");
      map.put("LPKJZ", "0");
      map.put("DDOSJZ", "0");
      map.put("XCodeGhost", "0");
      map.put("AYMMIII", "0");
      map.put("DGASJYMLSTMSFJQXX", "0");
      map.put("LSRJPetya", "0");
      map.put("SobigRC", "0");
      map.put("GamarueRC", "0");
      map.put("NitolJSWL", "0");
      map.put("VirutJSWL", "0");
      map.put("MY", "0");
      map.put("webshellHM", "0");
      map.put("XshellHM", "0");
      map.put("YCUA", "0");
      map.put("FXFW", "0");
      map.put("HL", "0");
      
      map.put("XNHBWK", "0");
      map.put("DDOSGJ", "0");
      map.put("BLPJ", "0");
      map.put("webshellSM", "0");
      map.put("webLDLY", "0");
      map.put("systemLDLY", "0");
      map.put("webshellSCGJ", "0");
      map.put("SQLZR", "0");
      map.put("SMBXYSM", "0");
      map.put("webZZXTLDGJ", "0");
      map.put("XSSGJ", "0");
      map.put("MLBLGJ", "0");
      map.put("WZSM", "0");
      map.put("LDSM", "0");
      map.put("EYLJ", "0");
      map.put("BLPJ", "0");
      map.put("LSBD", "0");
      map.put("DKSM", "0");
      map.put("IPSM", "0");
      map.put("SQLZR", "0");
      map.put("webLDLY", "0");
      map.put("systemLDLY", "0");
      map.put("webshellSC", "0");
      map.put("SMBXYSM", "0");
      map.put("SMBXYGJ", "0");
    }
    List getCountThreatTypeRoomIdxList = this.sqlSession.selectList("SmmsEventMain.getCountThreatTypeRoomIdx");
    System.out.println("daxiao:" + getCountThreatTypeRoomIdxList.size());
    
    getCountThreatTypeRoomIdxList = ReGroupUtil.addMap(getCountThreatTypeRoomIdxList);
    
    roomIdxList = ReGroupUtil.reGroupList(roomIdxList, getCountThreatTypeRoomIdxList);
    
    List getCountThreatTypeIdcIdList = this.sqlSession.selectList("SmmsEventMain.getCountThreatTypeIdcId");
    
    roomIdxList = ReGroupUtil.reGroupList(roomIdxList, getCountThreatTypeIdcIdList);
    
    List getCountThreatTypeRoomIdxAllList = this.sqlSession.selectList("SmmsEventMain.getCountThreatTypeRoomIdxAll");
    roomIdxList = ReGroupUtil.reGroupList(roomIdxList, getCountThreatTypeRoomIdxAllList);
    
    String json = JsonUtil.dataListToJson(roomIdxList);
    FMPLog.debug("json:jsonAll" + json);
    return json;
  }
}
