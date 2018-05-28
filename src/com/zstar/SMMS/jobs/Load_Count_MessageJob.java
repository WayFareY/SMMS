package com.zstar.SMMS.jobs;

import com.zstar.SMMS.BaseData.SptCityRectify.action.del.CityRecityDel;
import com.zstar.SMMS.STAT.SmmsCityEvent.action.del.SmmsCityEventDel;
import com.zstar.SMMS.STAT.SmmsCitySumYb.action.del.SmmsCitySumDel;
import com.zstar.SMMS.constant.GetCountIdcEventTDel;
import com.zstar.SMMS.constant.GetCountMessageDel;
import com.zstar.SMMS.constant.GetCountSptIdcCloseOpenDel;
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
    GetCountIdcEventTDel idcEventDel = new GetCountIdcEventTDel(getContex());
    GetCountSptIdcCloseOpenDel idcCloseOpenDel = new GetCountSptIdcCloseOpenDel(getContex());
    SmmsCitySumDel smmsCitySumYbDel = new SmmsCitySumDel(getContex());
    CityRecityDel cityRecityDel = new CityRecityDel(getContex());
    SmmsCityEventDel smmsCityEventDel = new SmmsCityEventDel(getContex());
    
    del.setTimeMap();
    
    int eventTopSum = del.insertSmmsEventTop();
    FMPLog.debug("总共插入首页汇总-威胁事件top10：" + eventTopSum + "条数据");
    
    int evenStatSum = del.insertSmmsEventStat();
    FMPLog.debug("总共插入首页汇总-网络安全事件统计：" + evenStatSum + "条数据");
    
    idcEventDel.insertSmmsIdcEvent();
    
    idcCloseOpenDel.insertSptIdcCloseOpenDel();
    
    cityRecityDel.insert();
    
    smmsCitySumYbDel.insertSmmsCitySum();
    
    smmsCityEventDel.insertSmmsCityEvent();
  }
}
