package com.zstar.SMMS.jobs;

import com.zstar.SMMS.constant.GetCountMessageDel;
import com.zstar.fmp.job.BaseJob;
import com.zstar.fmp.log.FMPLog;
import org.quartz.JobExecutionException;

public class Load_Count_MessageJob
  extends BaseJob
{
  public void jobExcute(String arg0)
    throws JobExecutionException
  {
    GetCountMessageDel del = new GetCountMessageDel(getContex());
    
    del.setTimeMap();
    
    int i = del.intsertSmmsBaseStat();
    FMPLog.debug("插入首页汇总-基本信息统计：" + i);
    
    int eventTopSum = del.insertSmmsEventTop();
    FMPLog.debug("总共插入首页汇总-威胁事件top10：" + eventTopSum + "条数据");
    
    int evenStatSum = del.insertSmmsEventStat();
    FMPLog.debug("总共插入首页汇总-网络安全事件统计：" + evenStatSum + "条数据");
  }
}
