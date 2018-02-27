package com.zstar.SMMS.BaseData.SmmsWebOpenApp.action.delegate;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebOpenDel
  extends BaseDelegate
{
  public WebOpenDel(ActionContext contex)
  {
    super(contex);
  }
  
  public List<Map> selectWebOpen()
  {
    List<Map> listWeb = this.sqlSession.selectList("SmmsWebOpenApp.selectOpenTime");
    return listWeb;
  }
  
  public List<Map> dealWebOpen(Map webMap)
  {
    List<Map> listWeb = this.sqlSession.selectList("SmmsWebOpenApp.dealWebOpen", webMap);
    return listWeb;
  }
  
  public int updateState(Map webMap)
  {
    int result = this.sqlSession.update("SmmsWebOpenApp.updateResult", webMap);
    return result;
  }
  
  public int updateCount(Map webMap)
  {
    int result = this.sqlSession.update("SmmsWebOpenApp.updateCount", webMap);
    return result;
  }
  
  public Map selectByRid(String eventId)
  {
    Map param = new HashMap();
    param.put("EVENT_MAINRID", eventId);
    Map result = (Map)this.sqlSession.selectOne("SmmsEventMain.selectByRid", param);
    if ((result != null) && (!result.isEmpty()))
    {
      long count = ((Long)result.get("ENFORCE_COUNT")).longValue();
      
      count += 1L;
      result.put("ENFORCE_COUNT", Long.valueOf(count));
      return result;
    }
    return null;
  }
}
