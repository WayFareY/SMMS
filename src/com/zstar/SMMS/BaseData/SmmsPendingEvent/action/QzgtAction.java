package com.zstar.SMMS.BaseData.SmmsPendingEvent.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.constant.SMMSConstant;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.utils.JsonUtil;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QzgtAction extends FrameAction {

	public String bizExecute() throws Exception {
		String url = (String) getWebData("URL");
		String ip = (String) getWebData("IP");
		Map selectMap = new HashMap();
		selectMap.put("URL", url.toUpperCase());

		selectMap.put("IP", ip);

		String rid = "";

		String threat_name = "";
		PendingEventDel del = new PendingEventDel(this.contex);
		Map insertMap = new HashMap();
		insertMap = del.selectInsertPendingInfoByIpOrUrl(selectMap);

		insertMap.put("IP", ip);
		if (!insertMap.containsKey("URL")) {
			insertMap.put("URL", url.toLowerCase());
		}
		if (!insertMap.containsKey("MAPPING_MODE")) {
			insertMap.put("MAPPING_MODE", SMMSConstant.MAPPING_MODE_ZERO);
		}
		threat_name = (String) getWebData("THREAT_NAME");
		insertMap.put("THREAT_NAME", threat_name);

		insertMap.put("FORCE_CLOSE_DESCE", getWebData("FORCE_CLOSE_DESCE"));

		insertMap.put("CLIENTNAME", getWebData("CURR_USERID"));

		insertMap.put("IS_FORCE_CLOSE", SMMSConstant.IS_FORCE_CLOSE);

		insertMap.put("THREAT_LEVEL", SMMSConstant.THREAT_LEVEL);

		insertMap.put("EVENT_SOURCE", SMMSConstant.EVENT_SOURCE);
		if ("".equals(threat_name)) {
			insertMap.put("THREAT_NAME", SMMSConstant.THREAT_NAME);
		}
		rid = del.insertSmmsPendingEvent(insertMap);

		Map mapRid = new HashMap();

		List mapUrlList = this.sqlSession.selectList("WebCase.viewDoMainNameAndAccessId", insertMap);
		if ((url != null) && (!"".equals(url))) {
			if ((mapUrlList != null) && (mapUrlList.size() > 0)) {
				mapRid.put("event_id", rid);
				rid = "'" + rid + "'";
				mapRid.put("ridCondition", "and ssp.RID IN (" + rid + ")");
				String message = del.ForceClose(mapRid);
				setMsg(message);
			} else {
				try {
					String tokenIdBbc = FMPContex.getSystemProperty("TOKENID");
					String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");
					Map domainMap = new HashMap();
					domainMap.put("domain", url);
					String domainJson = JsonUtil.dataMapToJson(domainMap);
					System.out.println("拼接" + domainJson);
					System.out.println("url:" + domainServiceURL);

					RpcBusDel rpcBusdel = new RpcBusDel(this.contex);

					String str = rpcBusdel.rpc2105(tokenIdBbc, domainServiceURL, domainJson);
					System.out.println("返回的json" + str);
					Map messageJson = JsonUtil.jsonToDataMap(str);
					mapRid.put("event_id", rid);
					mapRid.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_ENGTHIT);
					mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
					mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
					String message = (String) messageJson.get("return_msg");
					if ("000".equals(messageJson.get("return_code"))) {
						this.sqlSession.update("SmmsPendingEvent.updateQzgtState", mapRid);
					}
					System.out.println("返回信息" + messageJson.get("return_code") + ":" + message);
					setMsg(messageJson.get("return_code") + ":" + message);
				} catch (Exception e) {
					e.printStackTrace();
					setMsg("无法连接bbcIDC服务器");
				}
			}
		} else {
			List eventList = this.sqlSession.selectList("SmmsWebCaseIp.selectRidByIp", insertMap);
			if ((eventList != null) && (eventList.size() > 0)) {
				Map eventMap = (Map) eventList.get(0);

				Map selectUrlByRidMap = (Map) this.sqlSession.selectOne("WebCase.getDomainNameByRid", eventMap);
				if ((selectUrlByRidMap != null) && (selectUrlByRidMap.size() > 0)) {
					mapRid.put("event_id", rid);
					rid = "'" + rid + "'";
					mapRid.put("ridCondition", "and ssp.RID IN (" + rid + ")");

					String message = del.ForceClose(mapRid);
					setMsg(message);
				}
			} else {
				try {
					String tokenIdBbc = FMPContex.getSystemProperty("TOKENID");
					String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");
					Map domainMap = new HashMap();
					domainMap.put("domain", ip);
					String domainJson = JsonUtil.dataMapToJson(domainMap);
					System.out.println("拼接" + domainJson);
					System.out.println("webserviceurl:" + domainServiceURL);

					RpcBusDel rpcBusdel = new RpcBusDel(this.contex);
					String str = "{\"return_code\":\"000\",\"return_msg\":\"处理成功\"}";

					System.out.println("返回的json" + str);
					Map messageJson = JsonUtil.jsonToDataMap(str);
					mapRid.put("event_id", rid);
					mapRid.put("RECTIFY_STATE", "888");
					mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
					mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
					if ("000".equals(messageJson.get("return_code"))) {
						this.sqlSession.update("SmmsPendingEvent.updateQzgtState", mapRid);
					}
					String message = (String) messageJson.get("return_msg");
					System.out.println("返回信息" + messageJson.get("return_code") + ":" + message);
					setMsg(messageJson.get("return_code") + ":" + message);
				} catch (Exception e) {
					e.printStackTrace();
					setMsg("无法连接bbc服务器");
				}
			}
		}
		return "empty";
	}
}
