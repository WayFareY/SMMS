package com.zstar.SMMS.BaseData.SptCityRectify.action.del;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.constant.ReGroupUtil;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CityRecityDel
  extends BaseDelegate
{
  public CityRecityDel(ActionContext contex)
  {
    super(contex);
  }
  
  public void delete()
  {
    int i = this.sqlSession.delete("SptCityRectify.delete");
    FMPLog.debug("删除整改处理表：" + i);
  }
  
  public void insert()
  {
    List<Map> oldList = new ArrayList();
    oldList = this.sqlSession.selectList("SmmsEventMain.getCountDzg");
    List getCountZgz = this.sqlSession.selectList("SmmsEventMain.getCountZgz");
    oldList = ReGroupUtil.reGroupList(oldList, getCountZgz);
    List getCountYzg = this.sqlSession.selectList("SmmsEventMain.getCountYzg");
    
    oldList = ReGroupUtil.reGroupList(oldList, getCountYzg);
    int sum = 0;
    if ((oldList.size() > 0) && (!oldList.isEmpty()))
    {
      delete();
      for (Map map : oldList)
      {
        map.put("RID", FMPContex.getNewUUID());
        
        map.put("CREATTIME", FMPContex.getCurrentTime());
        
        map.put("RECORDSTATE", "0");
        
        int j = this.sqlSession.insert("SptCityRectify.insertSave", map);
        sum += j;
      }
      FMPLog.debug("插入整改处理共" + sum + "条数据");
    }
  }
  
  public Map selectListRequest()
  {
    Map map = (Map)this.sqlSession.selectOne("SptCityRectify.selectList");
    return map;
  }
  
  public String selectListJson()
  {
    List<Map<String, Object>> result = new ArrayList();
    result = this.sqlSession.selectList("SptCityRectify.selectListToJson");
    String json = JsonUtil.dataListToJson(result);
    return json;
  }
}
