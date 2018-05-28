package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetEventTotalAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String condition = "";
    Map map = new HashMap();
    String json = "";
    
    String mark = "";
    mark = (String)getWebData("MARK");
    
    String limit = "";
    limit = (String)getWebData("LIMIT");
    String offset = "";
    offset = (String)getWebData("OFFSET");
    if ((!"".equals(mark)) && (mark != null) && ("WBA".equals(mark))) {
      condition = "  and (sse.WEB_CASE_RID is not null or sse.WEB_CASE_RID!='' )";
    } else if ((!"".equals(mark)) && (mark != null) && ("SS1".equals(mark))) {
      condition = " and (sse.threat_type4 like '%010%' or sse.threat_type4 like '%020%' or sse.threat_type4 like '%030%') ";
    } else if ((!"".equals(mark)) && (mark != null) && ("SS2".equals(mark))) {
      condition = " and (sse.threat_type4 like '%030%' or sse.threat_type4 like '%040%' or sse.threat_type4 like '%050%')";
    } else if ((!"".equals(mark)) && (mark != null) && ("VPNO".equals(mark))) {
      condition = " and EVENT_TYPE1='vpno'";
    } else if ((!"".equals(mark)) && (mark != null) && ("ZSRQ".equals(mark))) {
      condition = " and THREAT_TYPE1='01' ";
    } else if ((!"".equals(mark)) && (mark != null) && ("HKKZ".equals(mark))) {
      condition = " and THREAT_TYPE1='02' ";
    } else if ((!"".equals(mark)) && (mark != null) && ("HCML".equals(mark))) {
      condition = " and THREAT_TYPE1='03' ";
    } else if ((!"".equals(mark)) && (mark != null) && ("NBGJ".equals(mark))) {
      condition = " and THREAT_TYPE1='04' ";
    } else if ((!"".equals(mark)) && (mark != null) && ("XXAQ".equals(mark))) {
      condition = " and sse.threat_type4 is not null and sse.threat_type4!='' ";
    } else if ((!"".equals(mark)) && (mark != null) && ("WLAQ".equals(mark))) {
      condition = " and (sse.THREAT_TYPE1 is not null and  sse.THREAT_TYPE1!='' and sse.THREAT_TYPE1!='99' or sse.EVENT_TYPE1='vpno' ) ";
    }
    if ((!"".equals(condition)) && (condition != null)) {
      map.put("CONDITION", condition);
    }
    if ((!"".equals(limit)) && (limit != null)) {
      map.put("LIMIT", limit);
    }
    if ((!"".equals(offset)) && (offset != null)) {
      map.put("OFFSET", offset);
    }
    List list = this.sqlSession.selectList("SmmsEventMain.getEventTotal", map);
    FMPLog.debug("事件总数：" + list.size());
    json = JsonUtil.dataListToJson(list);
    setMsg(json);
    return "empty";
  }
}
