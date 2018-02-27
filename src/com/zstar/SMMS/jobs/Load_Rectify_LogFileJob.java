package com.zstar.SMMS.jobs;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsWebOpenApp.action.delegate.WebOpenDel;
import com.zstar.SMMS.constant.TimeUtil;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.job.BaseJob;
import com.zstar.fmp.log.FMPLog;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.quartz.JobExecutionException;

public class Load_Rectify_LogFileJob
  extends BaseJob
{
  public void jobExcute(String s)
    throws JobExecutionException
  {
    WebOpenDel del = new WebOpenDel(getContex());
    
    RpcBusDel rpcBusdel = new RpcBusDel(getContex());
    EventMainDel mainDel = new EventMainDel(getContex());
    
    String currentTime = FMPContex.getCurrentTime();
    
    List<Map> listWeb = this.sqlSession.selectList("SmmsEventMain.selectModifiedTime");
    FMPLog.printLog("装载扫描待验证的整改网站:" + listWeb.size() + "条");
    if ((listWeb != null) && (!listWeb.isEmpty()))
    {
      FMPLog.printLog("开始");
      for (Map map : listWeb)
      {
        FMPLog.printLog("进行中");
        Map mapRid = new HashMap();
        String mainRid = (String)map.get("EVENT_RID");
        
        mapRid.put("EVENT_RID", mainRid);
        mapRid.put("MODIFIEDTIME", map.get("MODIFIEDTIME"));
        
        List<Map> list = this.sqlSession.selectList("SmmsEventMain.dealRectify", mapRid);
        try
        {
          double time = TimeUtil.compareDifference(currentTime, (String)map.get("MODIFIEDTIME"));
          
          String systemConTime = FMPContex.getSystemProperty("EVENT_LOG_INTERVAL");
          if (("".equals(systemConTime)) || (systemConTime == null)) {
            throw new NullPointerException("系统配置文件没有配置，idc安全事件管理的异常日志事件的时间间隔");
          }
          double ConditionTime = Double.valueOf(systemConTime).doubleValue();
          
          FMPLog.printLog("shijiandaxiao+" + list.size());
          if (list.size() > 0)
          {
            mapRid.put("RECTIFY_STATE", "000");
            int i = this.sqlSession.update("SmmsEventMain.updateRectifyState", mapRid);
            FMPLog.printLog("是否更新成功" + i + "---" + map.get("ACCESS_ID") + "网站整改失败");
          }
          else if ((list.size() == 0) && (ConditionTime < time))
          {
            mapRid.put("RECTIFY_STATE", "888");
            int i = this.sqlSession.update("SmmsEventMain.updateRectifyState", mapRid);
            FMPLog.printLog("是否更新成功" + i + "---" + map.get("ACCESS_ID") + "网站整改成功");
          }
        }
        catch (Exception e)
        {
          FMPLog.printLog(e);
          e.printStackTrace();
        }
      }
    }
    else
    {
      FMPLog.printLog("没有待验证的整改网站");
    }
  }
  
  public static void main(String[] args)
  {
    String a1 = "2";
    String a2 = "2";
    int result = a1.compareTo(a2);
    System.out.println(result);
  }
}
