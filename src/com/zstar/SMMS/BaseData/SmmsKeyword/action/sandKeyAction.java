package com.zstar.SMMS.BaseData.SmmsKeyword.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.utils.JsonUtil;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class sandKeyAction
  extends FrameAction
{
  public String bizExecute()
    throws Exception
  {
    List<Map> list = this.sqlSession.selectList("SmmsKeyword.selectList");
    Map keyWordMap = new HashMap();
    String keyWord = "";
    for (Map map : list) {
      keyWord = keyWord + map.get("KEYWORD") + ";";
    }
    if (keyWord.endsWith(";")) {
      keyWord = keyWord.substring(0, keyWord.length() - 1);
    }
    keyWordMap.put("keyword", keyWord);
    keyWordMap.put("opMode", Integer.valueOf(1));
    System.out.println("-------------------" + keyWord);
    String json = JsonUtil.dataMapToJson(keyWordMap);
    System.out.println("json:" + json);
    RpcBusDel rpcBusdel = new RpcBusDel(this.contex);
    String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");
    String tokenId = FMPContex.getSystemProperty("TOKENID");
    String str = "";
    String resultMessage = "";
    try
    {
      str = rpcBusdel.rpc2104(tokenId, domainServiceURL, json);
      System.out.println("json:" + str);
      Map messageJson = JsonUtil.jsonToDataMap(str);
      String message = (String)messageJson.get("return_msg");
      resultMessage = messageJson.get("return_code") + ":" + message;
    }
    catch (Exception e)
    {
      e.printStackTrace();
      resultMessage = "无法连接+bbc+IDC服务器";
    }
    setMsg(resultMessage);
    return "empty";
  }
}
