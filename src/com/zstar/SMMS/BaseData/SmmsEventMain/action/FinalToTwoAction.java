package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalToTwoAction
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
      String s = "'" + kv[j] + "'" + ",";
      ridList = ridList + s;
    }
    ridList = ridList.substring(0, ridList.length() - 1);
    
    FMPLog.printErr("移至确定关停:" + ridList);
    
    String automatic = FMPContex.getSystemParameter("BIZ1");
    System.out.println("是否自动强制关停：" + automatic);
    if ("1".equals(automatic))
    {
      mapRid.put("RIDCONDITION", "and ssp.RID IN (" + ridList + ") order by ssp.access_id asc");
      
      mapRid.put("DBTYPE", FMPContex.DBType);
      List<Map<String, Object>> forceCloselist = this.sqlSession.selectList("SmmsEventMain.viewToJson", mapRid);
      EventMainDel mainDel = new EventMainDel(this.contex);
      String message = mainDel.returnForceCloseMessage(forceCloselist);
      setMsg(message);
    }
    else
    {
      mapRid.put("ridCondition", "RID IN (" + ridList + ")");
      
      mapRid.put("FINAL_RECTIFY_SUGGEST", "2");
      
      mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      int i = this.sqlSession.update("SmmsEventMain.updateFinalRectifySuggest", mapRid);
      sum += i;
      setMsg("成功处理" + sum + "条数据");
    }
    return "empty";
  }
}
