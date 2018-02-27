package com.zstar.SMMS.BaseData.SmmsWebOpenApp.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class SubmitExamAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    String accept = (String)getWebData("IS_ACCEPT");
    String level = (String)getWebData("APP_LEVEL");
    String reason = (String)getWebData("REJECT_REASON");
    reason = URLDecoder.decode(reason, "UTF-8");
    Map paramMap = new HashMap();
    paramMap.put("RID", rid);
    if ("2".equals(accept)) {
      paramMap.put("REJECT_REASON", reason);
    }
    paramMap.put("IS_ACCEPT", accept);
    paramMap.put("APP_LEVEL", level);
    
    int result = this.sqlSession.update("SmmsWebOpenApp.updateSave", paramMap);
    if ("2".equals(accept))
    {
      Map webOpenEventRid = (Map)this.sqlSession.selectOne("SmmsWebOpenApp.webOpenInfo", paramMap);
      Map dataMap = new HashMap();
      dataMap.put("RID", webOpenEventRid.get("RID"));
      
      dataMap.put("RECTIFY_STATE", "888");
      dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      int after = this.sqlSession.update("SmmsEventMain.updateReStateAfter", dataMap);
      FMPLog.debug("审批通过后处理状态：" + after);
    }
    if (result == 1)
    {
      setMsg("000");
      return "empty";
    }
    setMsg("999");
    return "empty";
  }
}
