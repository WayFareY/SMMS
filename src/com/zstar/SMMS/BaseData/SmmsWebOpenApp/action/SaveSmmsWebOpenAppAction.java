package com.zstar.SMMS.BaseData.SmmsWebOpenApp.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.CommonSaveAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.Map;

public class SaveSmmsWebOpenAppAction
  extends CommonSaveAction
{
  public void afterBiz()
    throws Exception
  {
    super.afterBiz();
    
    Map dataMap = new HashMap();
    
    EventMainDel mainDel = new EventMainDel(this.contex);
    
    FMPLog.debug("日志事件主键rid:" + getWebData("EVENT_RID"));
    dataMap.put("RID", getWebData("EVENT_RID"));
    dataMap.put("EVENT_RID", getWebData("EVENT_RID"));
    
    String appResult = (String)getWebData("APP_RESULT");
    FMPLog.debug("审批结果：" + appResult);
    Map detailMap = new HashMap();
    try
    {
      String msg = mainDel.bbcBlocking(dataMap);
      
      detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
      detailMap.put("RID", detailMap.get("DETAIL_RID"));
      detailMap.put("RECTIFY_STATE", "030");
      detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
      
      int j = this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
      if (msg.indexOf("000") != -1)
      {
        msg = msg.substring(4, 8);
        setMsg(msg);
      }
      else
      {
        setMsg(msg);
      }
      if ("8".equals(appResult))
      {
        dataMap.put("RECTIFY_STATE", "030");
        
        dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        
        int result = this.sqlSession.update("SmmsEventMain.updateReState", dataMap);
        
        detailMap = (Map)this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
        detailMap.put("RID", detailMap.get("DETAIL_RID"));
        detailMap.put("RECTIFY_STATE", "030");
        detailMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
        
        this.sqlSession.update("SmmsPendingEvent.updateState", detailMap);
        
        FMPLog.debug("未经过2105解封更新状态是否成功：" + result);
        setMsg("处理成功");
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      setMsg("无法连接BBC服务器");
      
      throw ((Exception)e.fillInStackTrace());
    }
  }
  
  public void beforeBiz()
    throws Exception
  {
    super.beforeBiz();
  }
}
