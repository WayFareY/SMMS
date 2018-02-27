package com.zstar.SMMS.BaseData.SmmsEventMain_aqsj.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.HashMap;
import java.util.Map;

public class MoveToRectifyAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    String[] kv = rid.split(",");
    
    String ridList = "";
    int sum = 0;
    Map mapRid = new HashMap();
    for (int j = 0; j < kv.length; j++)
    {
      mapRid.put("RID", kv[j]);
      
      mapRid.put("IS_FORCE_CLOSE", "2");
      
      mapRid.put("SYS_RECTIFY_SUGGEST", "1");
      
      mapRid.put("FINAL_RECTIFY_SUGGEST", "1");
      
      mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      mapRid.put("RECTIFY_STATE", "000");
      int i = this.sqlSession.update("SmmsEventMain_aqsj.updateState", mapRid);
      sum += i;
    }
    setMsg("成功将" + sum + "条数据移至待整改网站");
    return "empty";
  }
}
