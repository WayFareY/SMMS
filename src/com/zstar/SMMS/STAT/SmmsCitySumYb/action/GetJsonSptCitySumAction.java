package com.zstar.SMMS.STAT.SmmsCitySumYb.action;

import com.zstar.SMMS.STAT.SmmsCityEvent.action.del.SmmsCityEventDel;
import com.zstar.SMMS.STAT.SmmsCitySumYb.action.del.SmmsCitySumDel;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetJsonSptCitySumAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    SmmsCitySumDel del = new SmmsCitySumDel(this.contex);
    SmmsCityEventDel smmsCityEventDel = new SmmsCityEventDel(this.contex);
    
    List selectSmmsCityEventGroupByYfList = smmsCityEventDel.selectSmmsCityEventGroupByYf();
    Map resultMap = new HashMap();
    Map map = del.getSptCitySumList();
    resultMap.put("CITY_BASE", map);
    resultMap.put("CITY_BASE_ZX", selectSmmsCityEventGroupByYfList);
    String json = JsonUtil.dataMapToJson(resultMap);
    FMPLog.debug("idc安全信息分类汇总基础数据Json:" + json);
    setMsg(json);
    return "empty";
  }
}
