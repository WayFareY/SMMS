package com.zstar.SMMS.constant;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.utils.JsonUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetCountCitySumDel
  extends BaseDelegate
{
  public GetCountCitySumDel(ActionContext contex)
  {
    super(contex);
  }
  
  public String getCountEventGroupXxaqJson(String condition)
  {
    Map map = new HashMap();
    if ((!"".equals(condition)) && (condition != null)) {
      map.put("CONDITION", condition);
    }
    List<Map<String, Object>> result = new ArrayList();
    result = this.sqlSession.selectList("SmmsEventMain.getCountEventGroupXxaq", map);
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
  
  public String getWlaqJson(String condition)
  {
    Map map = new HashMap();
    if ((!"".equals(condition)) && (condition != null)) {
      map.put("CONDITION", condition);
    }
    List<Map<String, Object>> result = new ArrayList();
    
    result = this.sqlSession.selectList("SmmsEventMain.getCountEventGroupWlaq", map);
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
  
  public String getWbaJson(String condition)
  {
    Map map = new HashMap();
    if ((!"".equals(condition)) && (condition != null)) {
      map.put("CONDITION", condition);
    }
    List<Map<String, Object>> result = new ArrayList();
    result = this.sqlSession.selectList("SmmsEventMain.getCountEventWba", map);
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
  
  public String getSgtAndHfJson()
  {
    List<Map<String, Object>> result = new ArrayList();
    result = this.sqlSession.selectList("SptIdcCloseOpen.selectListToJson");
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
  
  public String getFxFwqJson()
  {
    List<Map<String, Object>> result = new ArrayList();
    result = this.sqlSession.selectList("SmmsEventMain.getCountFxFwq");
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
  
  public String getYcllJson()
  {
    List<Map<String, Object>> result = new ArrayList();
    result = this.sqlSession.selectList("SmmsEventMain.getCountYcll");
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
}
