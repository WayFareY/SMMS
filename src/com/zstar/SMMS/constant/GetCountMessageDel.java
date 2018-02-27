package com.zstar.SMMS.constant;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
        double time = TimeUtil.compareDifference(currentTime, testTime);
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
  
  public int insertSmmsEventTop()
  {
    int sum = 0;
    
    int deleteResult = this.sqlSession.delete("SmmsEventTop10.deleteTableData");
    FMPLog.debug("删除首页汇总-威胁事件top10：" + deleteResult);
    List<Map> topTenList = getCountThreatTypeToTen();
    List<Map> topTenListNoDel = getCountThreatTypeToTenNoDel();
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
    for (Map topNodelMap : topTenListNoDel)
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
    List<Map> eventStatListTwo = getCount("2");
    for (Map eventStatMap : eventStatListOne)
    {
      eventStatMap.put("RID", FMPContex.getNewUUID());
      
      eventStatMap.put("CREATTIME", FMPContex.getCurrentTime());
      
      eventStatMap.put("RECORDSTATE", "0");
      
      eventStatMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      eventStatMap.put("DATAFLAG", "1");
      int j = this.sqlSession.insert("SmmsEventStat.insertSave", eventStatMap);
      sum += j;
    }
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
  
  public String eventJson()
  {
    Map jsonMap = new HashMap();
    String json = "";
    
    Map baseStatMap = (Map)this.sqlSession.selectOne("SmmsBaseStat.selectData");
    
    List<Map> eventTopListOne = this.sqlSession.selectList("SmmsEventTop10.selectData");
    
    List<Map> eventTopListTwo = this.sqlSession.selectList("SmmsEventTop10.selectDataByDataFlagTwo");
    
    List<Map> eventStatListOne = this.sqlSession.selectList("SmmsEventStat.selectData");
    
    List<Map> eventStatListTwo = this.sqlSession.selectList("SmmsEventStat.selectDataByDataFlagTwo");
    jsonMap.put("SMMS_BASE_STAT", baseStatMap);
    jsonMap.put("SMMS_EVENT_TOP10_COUNT", eventTopListOne);
    jsonMap.put("SMMS_EVENT_TOP10_CURRENT", eventTopListTwo);
    jsonMap.put("SMMS_EVENT_STAT_COUNT", eventStatListOne);
    jsonMap.put("SMMS_EVENT_STAT_CURRENT", eventStatListTwo);
    json = JsonUtil.dataMapToJson(jsonMap);
    FMPLog.debug("组织 事件的json:" + json);
    return json;
  }
  
  public List getCount(String contant)
  {
    List<Map> list1 = new ArrayList();
    List<Map> list2 = new ArrayList();
    if ("1".equals(contant))
    {
      list1 = getCountThreatTypeAll();
      
      list2 = getCountEventTypeAll();
    }
    else if ("2".equals(contant))
    {
      list1 = getCountThreatTypeBase();
      
      list2 = getCountEventTypeBase();
    }
    else
    {
      list1 = null;
      list2 = null;
    }
    List<Map> list3 = new ArrayList();
    List<Map> list4 = new ArrayList();
    if ((list1 != null) && (!list1.isEmpty()) && (list2 != null) && (!list2.isEmpty()))
    {
      for (int k = 0; k < list1.size() - 1; k++)
      {
        String id = (String)((Map)list1.get(k)).get("IDC_ID");
        String id1 = (String)((Map)list1.get(k + 1)).get("IDC_ID");
        Map map = new HashMap();
        if (!id.equals(id1))
        {
          map.put("IDC_ID", id);
          map.put("IDC_NAME", ((Map)list1.get(k)).get("IDC_NAME"));
          list3.add(map);
        }
        if (k == list1.size() - 2)
        {
          Map map1 = new HashMap();
          map1.put("IDC_ID", id1);
          map1.put("IDC_NAME", ((Map)list1.get(k + 1)).get("IDC_NAME"));
          list3.add(map1);
        }
      }
      for (int i = 0; i < list3.size(); i++)
      {
        String id = (String)((Map)list3.get(i)).get("IDC_ID");
        ((Map)list3.get(i)).put("INVADE", Integer.valueOf(0));
        ((Map)list3.get(i)).put("HACKER", Integer.valueOf(0));
        ((Map)list3.get(i)).put("BLACK_PROFIT", Integer.valueOf(0));
        ((Map)list3.get(i)).put("INSIDER_ATTACK", Integer.valueOf(0));
        ((Map)list3.get(i)).put("OVER_WALL", Integer.valueOf(0));
        ((Map)list3.get(i)).put("PROXY", Integer.valueOf(0));
        ((Map)list3.get(i)).put("VPN", Integer.valueOf(0));
        for (int j = 0; j < list1.size(); j++)
        {
          Map param = new HashMap();
          String id1 = (String)((Map)list1.get(j)).get("IDC_ID");
          if (id.equals(id1))
          {
            String type = (String)((Map)list1.get(j)).get("THREAT_TYPE1");
            if ("01".equals(type)) {
              ((Map)list3.get(i)).put("INVADE", ((Map)list1.get(j)).get("COUNT_THREAT_TYPE1"));
            } else if ("02".equals(type)) {
              ((Map)list3.get(i)).put("HACKER", ((Map)list1.get(j)).get("COUNT_THREAT_TYPE1"));
            } else if ("03".equals(type)) {
              ((Map)list3.get(i)).put("BLACK_PROFIT", ((Map)list1.get(j)).get("COUNT_THREAT_TYPE1"));
            } else if ("04".equals(type)) {
              ((Map)list3.get(i)).put("INSIDER_ATTACK", ((Map)list1.get(j)).get("COUNT_THREAT_TYPE1"));
            }
          }
        }
      }
      for (int k = 0; k < list2.size() - 1; k++)
      {
        String id = (String)((Map)list2.get(k)).get("IDC_ID");
        String id1 = (String)((Map)list2.get(k + 1)).get("IDC_ID");
        Map map = new HashMap();
        if (!id.equals(id1))
        {
          map.put("IDC_ID", id);
          map.put("IDC_NAME", ((Map)list2.get(k)).get("IDC_NAME"));
          list4.add(map);
        }
        if (k == list2.size() - 2)
        {
          Map map1 = new HashMap();
          map1.put("IDC_ID", id1);
          map1.put("IDC_NAME", ((Map)list2.get(k + 1)).get("IDC_NAME"));
          list4.add(map1);
        }
      }
      for (int i = 0; i < list4.size(); i++)
      {
        String id = (String)((Map)list4.get(i)).get("IDC_ID");
        ((Map)list4.get(i)).put("OVER_WALL", Integer.valueOf(0));
        ((Map)list4.get(i)).put("PROXY", Integer.valueOf(0));
        ((Map)list4.get(i)).put("VPN", Integer.valueOf(0));
        for (int j = 0; j < list2.size(); j++)
        {
          Map param = new HashMap();
          String id1 = (String)((Map)list2.get(j)).get("IDC_ID");
          if (id.equals(id1))
          {
            String type = (String)((Map)list2.get(j)).get("EVENT_TYPE1");
            if ("vpno".equals(type)) {
              ((Map)list4.get(i)).put("OVER_WALL", ((Map)list2.get(j)).get("COUNT_EVENT_TYPE1"));
            } else if ("proxy".equals(type)) {
              ((Map)list4.get(i)).put("PROXY", ((Map)list2.get(j)).get("COUNT_EVENT_TYPE1"));
            } else if ("vpn".equals(type)) {
              ((Map)list4.get(i)).put("VPN", ((Map)list2.get(j)).get("COUNT_EVENT_TYPE1"));
            }
          }
        }
      }
      Iterator<Map> it = list3.iterator();
      Iterator<Map> it1;
      for (; it.hasNext(); it1.hasNext())
      {
        Map x = (Map)it.next();
        it1 = list4.iterator();
        continue;
        Map eventMap = (Map)it1.next();
        if (x.get("IDC_ID").equals(eventMap.get("IDC_ID")))
        {
          x.putAll(eventMap);
          it1.remove();
        }
      }
      if (list4.size() > 0) {
        for (int i = 0; i < list4.size(); i++)
        {
          ((Map)list4.get(i)).put("INVADE", Integer.valueOf(0));
          ((Map)list4.get(i)).put("HACKER", Integer.valueOf(0));
          ((Map)list4.get(i)).put("BLACK_PROFIT", Integer.valueOf(0));
          ((Map)list4.get(i)).put("INSIDER_ATTACK", Integer.valueOf(0));
        }
      }
      list3.addAll(list4);
    }
    return list3;
  }
}
