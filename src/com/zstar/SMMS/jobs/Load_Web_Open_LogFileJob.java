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

public class Load_Web_Open_LogFileJob
  extends BaseJob
{
  public void jobExcute(String s)
    throws JobExecutionException
  {
    WebOpenDel del = new WebOpenDel(getContex());
    
    RpcBusDel rpcBusdel = new RpcBusDel(getContex());
    EventMainDel mainDel = new EventMainDel(getContex());
    
    String currentTime = FMPContex.getCurrentTime();
    
    List<Map> listWeb = del.selectWebOpen();
    FMPLog.printLog("装载扫描待验证的网站恢复申请:" + listWeb.size() + "条");
    if ((listWeb != null) && (!listWeb.isEmpty())) {
      for (Map map : listWeb)
      {
        Map mapRid = new HashMap();
        String mainRid = (String)map.get("EVENT_RID");
        
        Map param = del.selectByRid(mainRid);
        
        String rectifyState = "";
        if ((param != null) && (!param.isEmpty()))
        {
          mapRid.put("ENFORCE_COUNT", param.get("ENFORCE_COUNT"));
          rectifyState = (String)param.get("RECTIFY_STATE");
        }
        FMPLog.printLog("日志主表处理状态：" + rectifyState);
        mapRid.put("EVENT_RID", mainRid);
        mainRid = "'" + mainRid + "'";
        mapRid.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");
        
        List<Map> list = del.dealWebOpen(map);
        try
        {
          double time = TimeUtil.compareDifference(currentTime, (String)map.get("OPEN_APP_TIME"));
          
          String systemConTime = FMPContex.getSystemProperty("EVENT_LOG_INTERVAL");
          if (("".equals(systemConTime)) || (systemConTime == null)) {
            throw new NullPointerException("系统配置文件没有配置，idc安全事件管理的异常日志事件的时间间隔");
          }
          double ConditionTime = Double.valueOf(systemConTime).doubleValue();
          if (list.size() > 0)
          {
            map.put("CHECK_RESULT", "2");
            
            map.put("APP_RESULT", "9");
            int result = del.updateState(map);
            if (result == 1)
            {
              mapRid.put("ENFORCE_COUNT", Long.valueOf(((Long)param.get("ENFORCE_COUNT")).longValue() + 1L));
              mapRid.put("RECTIFY_STATE", "030");
              String message = mainDel.bbcForceClose(mapRid, rpcBusdel);
              FMPLog.printLog(map.get("ACCESS_ID") + "网站恢复失败");
            }
          }
          else if ((list.size() == 0) && (ConditionTime < time))
          {
            if (("8".equals(map.get("APP_RESULT"))) && ("030".equals(rectifyState)))
            {
              mapRid.put("RECTIFY_STATE", "900");
              mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
              int i = this.sqlSession.update("SmmsEventMain.updateQzgtState", mapRid);
              FMPLog.printLog("不需要审批流量验证通过了的更新待处理日志事件主表：" + i);
              mapRid.put("RID", map.get("EVENT_RID"));
              Map detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", mapRid);
              detailMap.put("RID", detailMap.get("DETAIL_RID"));
              detailMap.put("RECTIFY_STATE", "900");
              detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
              
              int j = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
              FMPLog.printLog("不需要审批的更新待处理日志事件从表：" + j);
            }
            else if (!"900".equals(rectifyState))
            {
              mapRid.put("RECTIFY_STATE", "030");
              message = mainDel.bbcForceClose(mapRid, rpcBusdel);
              FMPLog.printLog(map.get("ACCESS_ID") + "网站恢复成功");
            }
            map.put("CHECK_RESULT", "1");
            String message = del.updateState(map);
          }
        }
        catch (Exception e)
        {
          FMPLog.printLog(e);
          e.printStackTrace();
        }
      }
    } else {
      FMPLog.printLog("没有待验证的网站恢复申请");
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
