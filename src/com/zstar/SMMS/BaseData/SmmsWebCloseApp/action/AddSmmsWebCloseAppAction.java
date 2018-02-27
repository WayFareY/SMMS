package com.zstar.SMMS.BaseData.SmmsWebCloseApp.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.frame.action.CommonAddAction;
import java.util.HashMap;
import java.util.Map;

public class AddSmmsWebCloseAppAction
  extends CommonAddAction
{
  public void afterBiz()
    throws Exception
  {
    super.afterBiz();
    Map dataMap = new HashMap();
    Map getMap = new HashMap();
    dataMap.put("RID", getWebData("RID"));
    getMap = (Map)this.sqlSession.selectOne("SmmsEventMain.forCloseApply", dataMap);
    
    dataMap.put("ACCESS_ID", getMap.get("ACCESS_ID"));
    dataMap.put("EVENT_RID", getWebData("RID"));
    
    this.detailData.putAll(dataMap);
  }
  
  public void beforeBiz()
    throws Exception
  {
    super.beforeBiz();
  }
}
