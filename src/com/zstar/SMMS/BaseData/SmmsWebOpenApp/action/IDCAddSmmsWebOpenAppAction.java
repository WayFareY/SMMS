package com.zstar.SMMS.BaseData.SmmsWebOpenApp.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.CommonAddAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.Map;

public class IDCAddSmmsWebOpenAppAction
  extends CommonAddAction
{
  public void afterBiz()
    throws Exception
  {
    super.afterBiz();
    Map dataMap = new HashMap();
    Map getMap = new HashMap();
    dataMap.put("RID", getWebData("RID"));
    getMap = (Map)this.sqlSession.selectOne("SmmsEventMain.unblockApply", dataMap);
    FMPLog.debug("日志事件主键：" + getWebData("RID"));
    dataMap.put("ACCESS_ID", getMap.get("ACCESS_ID"));
    dataMap.put("EVENT_RID", getWebData("RID"));
    
    dataMap.put("OPEN_APP_TIME", FMPContex.getCurrentTime());
    
    dataMap.put("CHECK_RESULT", "0");
    dataMap.put("RID", FMPContex.getNewUUID());
    
    String isExamine = FMPContex.getSystemParameter("BIZ4");
    FMPLog.debug("是否需要审批:" + isExamine);
    if ("1".equals(isExamine)) {
      dataMap.put("APP_RESULT", "0");
    } else {
      dataMap.put("APP_RESULT", "8");
    }
    dataMap.put("ISSUE_STATE", "0");
    dataMap.put("LIABMAN", "admin");
    dataMap.put("LIABDEPT", "admin");
    dataMap.put("LIABORG", "admin");
    this.detailData.putAll(dataMap);
  }
  
  public void beforeBiz()
    throws Exception
  {
    super.beforeBiz();
  }
}
