package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.Map;

public class UpdateDetialCountAction
  extends FrameAction
{
  public void beforeBiz()
    throws Exception
  {
    String mainRid = (String)getWebData("RID");
    Map map = getWebDataMap();
    
    this.sqlSession.update("SmmsEventMain.UpdateDetialCountAddOne", map);
    Map allMap = (Map)this.sqlSession.selectOne("SmmsEventMain.findEventMainMessage", mainRid);
    allMap.put("RID", FMPContex.getNewUUID());
    allMap.put("REPORT_TIME", FMPContex.getCurrentTime());
    allMap.put("OCCUR_TIME", FMPContex.getCurrentTime());
    allMap.put("MAIN_RID", mainRid);
    int i = this.sqlSession.insert("SmmsPendingEvent.insertSave", allMap);
  }
}
