package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.STAT.SmmsCityEvent.action.del.SmmsCityEventDel;
import com.zstar.SMMS.STAT.SmmsIdcEvent.action.del.SmmsIdcEventDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestJsonIdcEventAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    SmmsIdcEventDel idcEventdel = new SmmsIdcEventDel(this.contex);
    SmmsCityEventDel smmsCityEventDel = new SmmsCityEventDel(this.contex);
    Map map = new HashMap();
    
    List idcEventList = idcEventdel.getIdcEventList();
    map.put("IDCEVENTALL", idcEventList);
    
    List eventTop10List = idcEventdel.getTop10List();
    map.put("SMMSIDCEVENTTOP10", eventTop10List);
    
    List selectMaxCityEventInfoList = smmsCityEventDel.selectMaxCityEventInfo();
    map.put("SMMSEVENT", selectMaxCityEventInfoList);
    
    List smmsCityEventGroupByXsList = smmsCityEventDel.selectSmmsCityEventGroupByXs();
    map.put("SMMSEVENTZX", smmsCityEventGroupByXsList);
    String json = JsonUtil.dataMapToJson(map);
    
    FMPLog.debug("IDC安全态势数据Json:" + json);
    setMsg(json);
    return "empty";
  }
}
