package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecoverWebAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    String rid = (String)getWebData("RID");
    String[] kv = rid.split(",");
    
    String ridList = "";
    int sum = kv.length;
    for (int j = 0; j < kv.length; j++)
    {
      String s = "'" + kv[j] + "'" + ",";
      ridList = ridList + s;
    }
    System.out.println("一键恢复接口rid:" + ridList);
    ridList = ridList.substring(0, ridList.length() - 1);
    EventMainDel del = new EventMainDel(this.contex);
    
    Map mapRid = new HashMap();
    mapRid.put("RIDCONDITION", "and ssp.RID IN (" + ridList + ")");
    
    String domain = "";
    
    Map domainMap = new HashMap();
    
    String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");
    
    String tokenIdBbc = FMPContex.getSystemProperty("TOKENID");
    
    String returnCode = "";
    List<Map> domainList = this.sqlSession.selectList("SmmsEventMain.viewToJsonIp", mapRid);
    for (Map urlMap : domainList) {
      domain = domain + urlMap.get("DOMAIN") + ";";
    }
    if (domain.endsWith(";")) {
      domain = domain.substring(0, domain.length() - 1);
    }
    domainMap.put("domain", domain);
    domainMap.put("opMode", Integer.valueOf("2"));
    FMPLog.debug("-------------------" + domain + "url:" + domainServiceURL);
    String domainJson = JsonUtil.dataMapToJson(domainMap);
    FMPLog.debug("恢复数据库存在拼接" + domainJson);
    RpcBusDel rpcBusdel = new RpcBusDel(this.contex);
    try
    {
      String str = rpcBusdel.rpc2105(tokenIdBbc, domainServiceURL, domainJson);
      FMPLog.debug("2105恢复响应回来的json:" + str);
      
      Map messageJson = JsonUtil.jsonToDataMap(str);
      returnCode = (String)messageJson.get("return_code");
      if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
        for (Map enforceCountMap : domainList) {
          if ("000".equals(returnCode))
          {
            enforceCountMap.put("RECTIFY_STATE", "900");
            enforceCountMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
            
            int i = this.sqlSession.update("SmmsEventMain.updateQzgtState", enforceCountMap);
            
            int j = this.sqlSession.update("SmmsPendingEvent.updateQzgtState", enforceCountMap);
            FMPLog.debug("主表是否更新成功:" + i + "从表是否更新成功" + j);
          }
        }
      }
      setMsg("成功恢复" + sum + "条数据");
    }
    catch (Exception e)
    {
      setMsg("bbc服务器异常");
    }
    return "empty";
  }
}
