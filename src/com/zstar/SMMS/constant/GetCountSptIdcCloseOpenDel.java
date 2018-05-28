package com.zstar.SMMS.constant;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.util.List;
import java.util.Map;

public class GetCountSptIdcCloseOpenDel
  extends BaseDelegate
{
  public GetCountSptIdcCloseOpenDel(ActionContext contex)
  {
    super(contex);
  }
  
  public List<Map> getCountGtz()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountGtz");
    return list;
  }
  
  public List<Map> getCountSqhf()
  {
    List<Map> list = this.sqlSession.selectList("SmmsEventMain.getCountSqhf");
    return list;
  }
  
  public List<Map> getCountYsl()
  {
    List<Map> list = this.sqlSession.selectList("SmmsWebOpenApp.getCountYsl");
    return list;
  }
  
  public List<Map> getCountShz()
  {
    List<Map> list1 = this.sqlSession.selectList("SmmsWebOpenApp.getCountShz");
    FMPLog.debug("list1:" + JsonUtil.dataListToJson(list1));
    List<Map> list2 = this.sqlSession.selectList("SmmsWebCloseApp.getCountShz");
    FMPLog.debug("list2:" + JsonUtil.dataListToJson(list2));
    list1 = ReGroupUtil.reGroupList(list1, list2);
    FMPLog.debug("listall:" + JsonUtil.dataListToJson(list1));
    return list1;
  }
  
  public List<Map> getCountYhf()
  {
    List<Map> list = this.sqlSession.selectList("SmmsWebOpenApp.getCountYhf");
    return list;
  }
  
  public void insertSptIdcCloseOpenDel()
  {
    List<Map> oldList = getCountGtz();
    
    List<Map> getCountHfzList = getCountSqhf();
    oldList = ReGroupUtil.reGroupList(oldList, getCountHfzList);
    
    List<Map> getCountYslList = getCountYsl();
    oldList = ReGroupUtil.reGroupList(oldList, getCountYslList);
    
    List<Map> getCountShzList = getCountShz();
    oldList = ReGroupUtil.reGroupList(oldList, getCountShzList);
    
    List<Map> getCountYhfList = getCountYhf();
    oldList = ReGroupUtil.reGroupList(oldList, getCountYhfList);
    if ((!oldList.isEmpty()) && (oldList.size() > 0))
    {
      int deleteResult = this.sqlSession.delete("SptIdcCloseOpen.deleteTableData");
      
      int sum = 0;
      for (Map eventStatMap : oldList)
      {
        eventStatMap.put("RID", FMPContex.getNewUUID());
        
        eventStatMap.put("CREATTIME", FMPContex.getCurrentTime());
        
        eventStatMap.put("RECORDSTATE", "0");
        
        int j = this.sqlSession.insert("SptIdcCloseOpen.insertSave", eventStatMap);
        sum += j;
      }
      FMPLog.debug("插入IDC业务关停与恢复共" + sum + "条数据");
    }
  }
}
