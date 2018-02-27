package com.zstar.SMMS.BaseData.SmmsEventMain_aqsj.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.HashMap;
import java.util.Map;

public class MoveToCloseAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    String[] kv = rid.split(",");
    int sum = 0;
    Map mapRid = new HashMap();
    for (int j = 0; j < kv.length; j++)
    {
      mapRid.put("RID", kv[j]);
      
      mapRid.put("IS_FORCE_CLOSE", "1");
      
      mapRid.put("SYS_RECTIFY_SUGGEST", "2");
      
      mapRid.put("FINAL_RECTIFY_SUGGEST", "2");
      
      mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      mapRid.put("RECTIFY_STATE", "000");
      int i = this.sqlSession.update("SmmsEventMain_aqsj.updateState", mapRid);
      sum += i;
    }
    setMsg("成功将" + sum + "条数据移至已确定关停网站中");
    return "empty";
  }
}
