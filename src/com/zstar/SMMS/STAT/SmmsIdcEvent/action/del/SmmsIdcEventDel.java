package com.zstar.SMMS.STAT.SmmsIdcEvent.action.del;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmmsIdcEventDel
  extends BaseDelegate
{
  public SmmsIdcEventDel(ActionContext contex)
  {
    super(contex);
  }
  
  public Map getCountIdcEvent()
  {
    Map map = new HashMap();
    map = (Map)this.sqlSession.selectOne("SmmsIdcEvent.selectList");
    return map;
  }
  
  public List getTop10List()
  {
    List list = this.sqlSession.selectList("SmmsIdcEvent.selectTop10");
    return list;
  }
  
  public List getIdcEventList()
  {
    List list = this.sqlSession.selectList("SmmsIdcEvent.selectEventList");
    return list;
  }
}
