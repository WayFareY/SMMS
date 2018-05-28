package com.zstar.SMMS.STAT.SmmsCityEvent.action.del;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmmsCityEventDel
  extends BaseDelegate
{
  public SmmsCityEventDel(ActionContext contex)
  {
    super(contex);
  }
  
  public Map getDate()
  {
    Map map = new HashMap();
    Calendar cal = Calendar.getInstance();
    
    int year = cal.get(1);
    
    int month = cal.get(2) + 1;
    
    int day = cal.get(5);
    
    int xs = cal.get(11);
    map.put("NF", Integer.valueOf(year));
    map.put("YF", Integer.valueOf(month));
    map.put("RQ", Integer.valueOf(day));
    map.put("XS", Integer.valueOf(xs));
    return map;
  }
  
  public void insertSmmsCityEvent()
  {
    Map insertMap = (Map)this.sqlSession.selectOne("SmmsIdcEvent.selectList");
    insertMap.putAll(getDate());
    insertMap.put("RID", FMPContex.getNewUUID());
    
    insertMap.put("RECORDSTATE", "0");
    
    insertMap.put("CREATTIME", FMPContex.getCurrentTime());
    int i = this.sqlSession.insert("SmmsCityEvent.insertSave", insertMap);
    FMPLog.debug("插入地市总表是否成功：" + i);
  }
  
  public List selectSmmsCityEventGroupByYf()
  {
    List list = this.sqlSession.selectList("SmmsCityEvent.selectSmmsCityEventGroupByYf");
    return list;
  }
  
  public List selectSmmsCityEventGroupByXs()
  {
    List list = this.sqlSession.selectList("SmmsCityEvent.selectSmmsCityEventGroupByXs");
    return list;
  }
  
  public List selectMaxCityEventInfo()
  {
    List list = this.sqlSession.selectList("SmmsCityEvent.selectMaxCityEventInfo");
    return list;
  }
}
