package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.utils.JsonUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckUrlAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String url = (String)getWebData("URL");
    Map urlMap = new HashMap();
    urlMap.put("URL", url.toUpperCase());
    
    urlMap.put("DBTYPE", FMPContex.DBType);
    
    List urlList = this.sqlSession.selectList("WebCase.checkUrl", urlMap);
    if ((urlList != null) && (urlList.size() > 0))
    {
      Map webCaseMap = (Map)urlList.get(0);
      
      Map webCaseRidMap = new HashMap();
      webCaseRidMap.put("CASE_RID", webCaseMap.get("SWCRID"));
      List webCaseIpList = this.sqlSession.selectList("SmmsWebCaseIp.findAccessIpByCaseRid", webCaseRidMap);
      if ((webCaseIpList != null) && (webCaseIpList.size() > 0))
      {
        Map webCaseIpMap = (Map)webCaseIpList.get(0);
        webCaseIpMap.put("IP", webCaseIpMap.get("ACCESS_IP"));
        webCaseIpMap.put("ACCESS_ID", webCaseMap.get("ACCESS_ID"));
        webCaseIpMap.put("DBTYPE", FMPContex.DBType);
        
        List listRoomIdc = this.sqlSession.selectList("SmmsRoomIprange.selectAccesIdByIp", webCaseIpMap);
        if ((listRoomIdc != null) && (listRoomIdc.size() > 0))
        {
          Map roomIdcMap = (Map)listRoomIdc.get(0);
          
          List listRoomAndIdc = this.sqlSession.selectList("SmmsEventMain.checkAccessIdAndRoomIdx", roomIdcMap);
          if ((listRoomAndIdc != null) && (listRoomAndIdc.size() > 0))
          {
            Map roomInfoMap = (Map)listRoomAndIdc.get(0);
            
            webCaseMap.put("ROOM_NAME", roomInfoMap.get("ROOM_NAME"));
            webCaseMap.put("ROOM_IDX", roomInfoMap.get("ROOM_IDX"));
            webCaseMap.put("ROOM_ADDRESS", roomInfoMap.get("ROOM_ADDRESS"));
            setMsg(JsonUtil.dataMapToJson(webCaseMap));
          }
        }
      }
    }
    else
    {
      Map ipMap = new HashMap();
      ipMap.put("IP", getWebData("IP"));
      ipMap.put("DBTYPE", FMPContex.DBType);
      List listRoomIdc = this.sqlSession.selectList("SmmsRoomIprange.selectAccesIdByIp", ipMap);
      if ((listRoomIdc != null) && (listRoomIdc.size() > 0))
      {
        Map roomIdcMap = (Map)listRoomIdc.get(0);
        List listRoomAndIdc = this.sqlSession.selectList("SmmsEventMain.checkAccessIdAndRoomIdx", roomIdcMap);
        if ((listRoomAndIdc != null) && (listRoomAndIdc.size() > 0))
        {
          Map roomAndIdcMap = (Map)listRoomAndIdc.get(0);
          List listSwcIp = this.sqlSession.selectList("SmmsWebCaseIp.selectRidByIp", ipMap);
          if ((listSwcIp != null) && (listSwcIp.size() > 0))
          {
            Map swcIpMap = (Map)listSwcIp.get(0);
            List listSwc = this.sqlSession.selectList("SmmsWebCaseIp.webCaseRid", swcIpMap);
            if ((listSwc != null) && (listSwc.size() > 0))
            {
              Map swcMap = (Map)listSwc.get(0);
              roomAndIdcMap.put("SWCRID", swcMap.get("SWCRID"));
              roomAndIdcMap.put("WEBSITE_NAME", swcMap.get("WEBSITE_NAME"));
              roomAndIdcMap.put("SPONSER_CASE_NUM", swcMap.get("SPONSER_CASE_NUM"));
              roomAndIdcMap.put("WEBSITE_URL", swcMap.get("WEBSITE_URL"));
              setMsg(JsonUtil.dataMapToJson(roomAndIdcMap));
            }
          }
        }
      }
    }
    return "empty";
  }
}
