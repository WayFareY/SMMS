package com.zstar.SMMS.STAT.SmmsCitySumYb.action.del;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.Map;

public class SmmsCitySumDel
  extends BaseDelegate
{
  public SmmsCitySumDel(ActionContext contex)
  {
    super(contex);
  }
  
  public Map countIdcEventSum()
  {
    Map getIdcCountMap = (Map)this.sqlSession.selectOne("IdcInfo.getIdcCount");
    
    Map getIdcRoomIdxCountMap = (Map)this.sqlSession.selectOne("SmmsRoomInfo.getIdcRoomIdxCount");
    
    Map getIdcWebCountMap = (Map)this.sqlSession.selectOne("WebCase.getIdcWebCount");
    
    Map resultMap = new HashMap();
    
    resultMap.put("IDCJFS", getIdcRoomIdxCountMap.get("ROOM_COUNT"));
    
    resultMap.put("YYSS", getIdcCountMap.get("IDC_COUNT"));
    
    resultMap.put("WZBAS", getIdcWebCountMap.get("BUSSY_COUNT"));
    
    Map WbasjsMap = (Map)this.sqlSession.selectOne("SmmsIdcEvent.countWbasjs");
    resultMap.putAll(WbasjsMap);
    
    Map XxaqsjsMap = (Map)this.sqlSession.selectOne("SmmsIdcEvent.countXxaqsjs");
    resultMap.putAll(XxaqsjsMap);
    
    Map countWlaqsjsMap = (Map)this.sqlSession.selectOne("SmmsIdcEvent.countWlaqsjs");
    resultMap.putAll(countWlaqsjsMap);
    FMPLog.debug("json:" + JsonUtil.dataMapToJson(resultMap));
    return resultMap;
  }
  
  public int insertSmmsCitySum()
  {
    Map map = new HashMap();
    
    deleteSmmsCityEvent();
    Map countCitySumMap = countIdcEventSum();
    map.putAll(countCitySumMap);
    map.put("RID", FMPContex.getNewUUID());
    
    map.put("RECORDSTATE", "0");
    
    map.put("CREATTIME", FMPContex.getCurrentTime());
    
    map.put("MODIFIEDTIME", FMPContex.getCurrentTime());
    int i = this.sqlSession.insert("SmmsCitySumYb.insertSave", map);
    FMPLog.debug("(地市汇总表基础)是否插入成功：" + i);
    return i;
  }
  
  public int deleteSmmsCityEvent()
  {
    int i = this.sqlSession.delete("SmmsCitySumYb.delete");
    FMPLog.debug("(地市汇总表)是否删除成功：" + i);
    return i;
  }
  
  public Map getSptCitySumList()
  {
    Map map = (Map)this.sqlSession.selectOne("SmmsCitySumYb.selectList");
    return map;
  }
}
