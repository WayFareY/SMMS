package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
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
		EventMainDel mainDel = new EventMainDel(this.contex);
		Map insertMap = new HashMap();
		insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(selectMap);

		insertMap.put("DBTYPE", FMPContex.DBType);
		insertMap.put("IP", ip);
		if ((insertMap != null) && (insertMap.size() > 0)) {
			if ("1".equals(insertMap.get("MAPPING_MODE"))) {
				insertMap.put("URL", url.toLowerCase());
			} else if ("2".equals(insertMap.get("MAPPING_MODE"))) {
				insertMap.put("URL", "");
			}
		} else {
			insertMap.put("URL", url.toLowerCase());

			insertMap.put("DOMAIN", url);
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
		String mainRid = "";
		if (insertMap.containsKey("WEB_CASE_RID")) {
			Map webRidMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countWebCaseRidTotal", insertMap);

			Long webCaseTotal = (Long) webRidMap.get("WEBCASETOTAL");
			if (webCaseTotal.longValue() == 0L) {
				mainRid = mainDel.insertSmmsEventMain(insertMap);
			} else {
				mainRid = (String) webRidMap.get("MAIN_RID");
			}
		} else if (insertMap.containsKey("DOMAIN_NAME")) {
			Map domainMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countDomainTotal", insertMap);

			Long domainTotal = (Long) domainMap.get("DOMAINTOTAL");
			if (domainTotal.longValue() == 0L) {
				mainRid = mainDel.insertSmmsEventMain(insertMap);
			} else {
				mainRid = (String) domainMap.get("MAIN_RID");
			}
		} else if (insertMap.containsKey("IP")) {
			Map ipMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countIpTotal", insertMap);

			Long ipTotal = (Long) ipMap.get("IPTOTAL");
			if (ipTotal.longValue() == 0L) {
				mainRid = mainDel.insertSmmsEventMain(insertMap);
			} else {
				mainRid = (String) ipMap.get("MAIN_RID");
			}
		}
		if (!"".equals(mainRid)) {
			insertMap.put("MAIN_RID", mainRid);
			del.insertSmmsPendingEvent(insertMap);
		}
		Map mapRid = new HashMap();

		mapRid.put("DBTYPE", FMPContex.DBType);

		List mapUrlList = this.sqlSession.selectList("WebCase.viewDoMainNameAndAccessId", selectMap);
		System.out.println("大小" + mapUrlList.size());
		if ((url != null) && (!"".equals(url))) {
			if ((mapUrlList != null) && (mapUrlList.size() > 0)) {
				mapRid.put("EVENT_RID", mainRid);
				mainRid = "'" + mainRid + "'";
				mapRid.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");
				String message = mainDel.ForceClose(mapRid);
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
					mapRid.put("EVENT_RID", mainRid);
					mapRid.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_ENGTHIT);
					mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
					mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
					String message = (String) messageJson.get("return_msg");
					if ("000".equals(messageJson.get("return_code"))) {
						this.sqlSession.update("SmmsEventMain.updateQzgtState", mapRid);
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
					mapRid.put("EVENT_RID", mainRid);
					mainRid = "'" + mainRid + "'";
					mapRid.put("RIDCONDITION", "and ssp.RID IN (" + mainRid + ")");

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

					String str = rpcBusdel.rpc2105(tokenIdBbc, domainServiceURL, domainJson);
					System.out.println("返回的json" + str);
					Map messageJson = JsonUtil.jsonToDataMap(str);
					mapRid.put("EVENT_RID", rid);
					mapRid.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_ENGTHIT);
					mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
					mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
					if ("000".equals(messageJson.get("return_code"))) {
						this.sqlSession.update("SmmsEventMain.updateQzgtState", mapRid);
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
