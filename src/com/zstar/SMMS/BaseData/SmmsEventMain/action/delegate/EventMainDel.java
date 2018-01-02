package com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.IdcInfo.delegate.IdcInfoDel;
import com.zstar.SMMS.constant.SMMSConstant;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.utils.JsonUtil;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;

public class EventMainDel extends BaseDelegate {

	public EventMainDel(ActionContext contex) {
		super(contex);
	}

	public void pendingEvent101(Map map) {
		Map<String, String> updateMap = (Map) this.sqlSession.selectOne("SmmsEventMain.selectAll", map);
		map.put("MODIFIEDTIME", FMPContex.getCurrentTime());
		if (map != null) {
			this.sqlSession.update("SmmsEventMain.updateSave", updateMap);
		}
	}

	public String rectification(Map mapRid) {
		Map jsonMap = (Map) this.sqlSession.selectOne("SmmsEventMain.viewToRectify", mapRid);

		Map listMap = new HashMap();
		if ((jsonMap != null) && (jsonMap.size() > 0)) {
			listMap.put("event_id", jsonMap.get("EVENT_ID"));
			listMap.put("url", jsonMap.get("URL"));
			listMap.put("security_type", jsonMap.get("SECURITY_TYPE"));
			listMap.put("damage_class", jsonMap.get("DAMAGE_CLASS"));
			listMap.put("rectify_suggestion", jsonMap.get("RECTIY_SUGGESTION"));
			listMap.put("rectify_term", jsonMap.get("RECTIFY_TERM"));
			listMap.put("case_id", jsonMap.get("CASE_ID"));
			listMap.put("snapshop", jsonMap.get("SNAPSHOP"));
		}
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));
		List<Map> list = new ArrayList();
		list.add(listMap);
		mapTotal.put("rows", list);

		Map map = new HashMap();
		map.put("data", mapTotal);
		String json = JSONObject.fromObject(map).toString();

		IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);
		String idcid = (String) jsonMap.get("ACCESS_ID");
		String tokenid = idcInfoDel.getIdcToken(idcid);
		String idcName = idcInfoDel.getIdcName(idcid);
		String serviecUrl = idcInfoDel.getIdcServiceUrl(idcid);

		RpcBusDel rpc = new RpcBusDel(this.contex);
		try {
			System.out.println("2101接口:" + json);

			String msg = rpc.rpc2101(tokenid, serviecUrl, json);

			Map messageJson = JsonUtil.jsonToDataMap(msg);

			mapRid.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_END);
			mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
			mapRid.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
			if ("000".equals(messageJson.get("return_code"))) {
				this.sqlSession.update("SmmsEventMain.issueStateUpdate", mapRid);
			}
			String message = (String) messageJson.get("return_msg");
			return messageJson.get("return_code") + ":" + message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "无法连接" + idcName + "IDC服务器";
	}

	public int sendAllForceClose(List<Map<String, String>> sendList, String idcId) {
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));

		mapTotal.put("rows", sendList);
		Map map = new HashMap();
		map.put("data", mapTotal);

		String json = JsonUtil.dataMapToJson(map);
		int sum = 0;
		IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);
		String serviceURL = idcInfoDel.getIdcServiceUrl(idcId);
		String tokenId = idcInfoDel.getIdcToken(idcId);
		String idcName = idcInfoDel.getIdcName(idcId);
		RpcBusDel del = new RpcBusDel(this.contex);
		String str = "";
		if ((!"".equals(serviceURL)) && (serviceURL != null)) {
			System.out.println("2102接口" + json);

			str = del.rpc2102(tokenId, serviceURL, json);
			Map messageJson = JsonUtil.jsonToDataMap(str);
			if ("000".equals((String) messageJson.get("return_code"))) {
				for (Map ridMap : sendList) {
					ridMap.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_END);
					ridMap.put("SEND_TIME", FMPContex.getCurrentTime());
					ridMap.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
					int i = this.sqlSession.update("SmmsEventMain.updateRectifyState", ridMap);
					sum += i;
				}
			}
		} else {
			String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");
			List<Map> list = this.sqlSession.selectList("SmmsEventMain.viewToAllJsonIp");
			Object domainMap = new HashMap();
			String domain = "";
			for (Map urlMap : list) {
				domain = domain + urlMap.get("DOMAIN") + ";";
			}
			if (domain.endsWith(";")) {
				domain = domain.substring(0, domain.length() - 1);
			}
			((Map) domainMap).put("domain", domain);

			System.out.println("-------------------" + domain + "url:" + domainServiceURL);
			String domainJson = JsonUtil.dataMapToJson((Map) domainMap);

			System.out.println("调用2105的json:" + domainJson);
			str = del.rpc2105(tokenId, domainServiceURL, domainJson);
			Map messageJson = JsonUtil.jsonToDataMap(str);
			if ("000".equals((String) messageJson.get("return_code"))) {
				for (Map ridMap : sendList) {
					ridMap.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_END);
					ridMap.put("SEND_TIME", FMPContex.getCurrentTime());
					ridMap.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
					int i = this.sqlSession.update("SmmsEventMain.updateQzgtState", ridMap);
					sum += i;
				}
			}
		}
		return sum;
	}

	public String returnForceCloseMessage(List<Map<String, String>> forceCloselist) {
		IdcInfoDel idcInfoDel;
		String str;
		String tmp_accessid;
		String idcName;
		String idcNameErr;
		String errmsg;
		int sum;
		int sumThen;
		List sendList;
		Iterator iterator;
		idcInfoDel = new IdcInfoDel(contex);
		str = "";
		tmp_accessid = "";
		idcName = "";
		idcNameErr = "";
		errmsg = "";
		sum = 0;
		sumThen = 0;
		int sumAfter = 0;
		sendList = new ArrayList();
		iterator = forceCloselist.iterator();
		while (iterator.hasNext()) {
			Map mapToJson;
			Map changeJsonMap;
			mapToJson = (Map) iterator.next();
			changeJsonMap = new HashMap();
			if (mapToJson.get("CLOSE_TERM") == null) {
				mapToJson.put("CLOSE_TERM", SMMSConstant.CLOSE_TERM);
			}
			if (mapToJson.get("RECTIFY_TERM") == null) {
				mapToJson.put("RECTIFY_TERM", SMMSConstant.RECTIFY_TERM);
			}
			if (mapToJson.get("DAMAGE_CLASS") == null) {
				mapToJson.put("DAMAGE_CLASS", "3");
			}
			if (mapToJson.get("SECURITY_TYPE") == null) {
				mapToJson.put("SECURITY_TYPE", SMMSConstant.SECURITY_TYPE);
			}
			if (mapToJson.get("ACCESS_ID") == null || "".equals(mapToJson.get("ACCESS_ID"))) {
				continue; /* Loop/switch isn't completed */
			}
			if ("".equals(tmp_accessid)) {
				String key;
				for (Iterator iterator1 = mapToJson.keySet().iterator(); iterator1.hasNext(); changeJsonMap.put(key.toLowerCase(),
						(String) mapToJson.get(key))) {
					key = (String) iterator1.next();
				}

				sendList.add(changeJsonMap);
				tmp_accessid = (String) mapToJson.get("ACCESS_ID");
				continue; /* Loop/switch isn't completed */
			}
			if (tmp_accessid.equals(mapToJson.get("ACCESS_ID"))) {
				String key;
				for (Iterator iterator2 = mapToJson.keySet().iterator(); iterator2.hasNext(); changeJsonMap.put(key.toLowerCase(),
						(String) mapToJson.get(key))) {
					key = (String) iterator2.next();
				}

				sendList.add(changeJsonMap);
				continue; /* Loop/switch isn't completed */
			}
			idcName = idcInfoDel.getIdcName(tmp_accessid);
			idcNameErr = (new StringBuilder(String.valueOf(idcNameErr))).append(idcInfoDel.getIdcName(tmp_accessid)).append(",").toString();
			try {
				sum = sendAllForceClose(sendList, tmp_accessid);
				str = (new StringBuilder(String.valueOf(str))).append(idcName).append("处理了:").append(sum).append("条数据").append("\n").toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (!"".equals(errmsg)) {
					errmsg = (new StringBuilder("无法连接")).append(idcNameErr).append("IDC服务器").toString();
				}
				sumThen += sum;
				sendList.clear();
				String key;
				for (Iterator iterator3 = mapToJson.keySet().iterator(); iterator3.hasNext(); changeJsonMap.put(key.toLowerCase(),
						(String) mapToJson.get(key))) {
					key = (String) iterator3.next();
				}

				sendList.add(changeJsonMap);
				tmp_accessid = (String) mapToJson.get("ACCESS_ID");
			}
		}
		idcName = idcInfoDel.getIdcName(tmp_accessid);
		idcNameErr = (new StringBuilder(String.valueOf(idcNameErr))).append(idcInfoDel.getIdcName(tmp_accessid)).toString();
		try {
			sumAfter = sendAllForceClose(sendList, tmp_accessid);
			str = (new StringBuilder(String.valueOf(str))).append(idcName).append("处理了:").append(sumAfter).append("条数据").append("\n").toString();
			sumAfter += sumThen;
			str = (new StringBuilder(String.valueOf(str))).append("总共处理:").append(sumAfter).append("条数据").append("\n").toString();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		errmsg = (new StringBuilder("无法连接")).append(idcNameErr).append("IDC服务器").toString();
		return errmsg;
	}

	public String insertSmmsEventMain(Map insertMap) {
		String rid = FMPContex.getNewUUID();
		insertMap.put("RID", rid);

		insertMap.put("REPORT_CITY", SMMSConstant.REPORT_CITY);

		insertMap.put("REPORT_TIME", FMPContex.getCurrentTime());
		if (insertMap.get("OCCUR_TIME") == null) {
			insertMap.put("OCCUR_TIME", FMPContex.getCurrentTime());
		}
		insertMap.put("IS_WHITE_LIST", SMMSConstant.IS_WHITE_LIST);

		insertMap.put("CREATTIME", FMPContex.getCurrentTime());

		insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());

		insertMap.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE);
		this.sqlSession.insert("SmmsEventMain.insertSave", insertMap);
		return rid;
	}

	public Map selectInsertPendingInfoByIpOrUrl(Map selectMap) {
		selectMap.put("DBTYPE", FMPContex.DBType);
		Map insertMap = new HashMap();

		List list = this.sqlSession.selectList("WebCase.viewDoMainNameAndAccessId", selectMap);
		if ((list != null) && (list.size() > 0)) {
			Map webCaseMap = (Map) list.get(0);
			insertMap.put("DOMAIN_NAME", webCaseMap.get("DOMAIN_NAME"));
			insertMap.put("SNAPSHOP", webCaseMap.get("SNAPSHOP"));
			insertMap.put("ACCESS_ID", webCaseMap.get("ACCESS_ID"));
			insertMap.put("WEB_CASE_RID", webCaseMap.get("RID"));

			insertMap.put("MAPPING_MODE", SMMSConstant.MAPPING_MODE_ONE);
		} else {
			List roomIdcList = this.sqlSession.selectList("SmmsRoomIprange.selectAccesIdByIp", selectMap);
			Map roomIdcMap = new HashMap();
			String accessId = "";
			if ((roomIdcList != null) && (roomIdcList.size() > 0)) {
				roomIdcMap = (Map) roomIdcList.get(0);
				accessId = (String) roomIdcMap.get("ACCESS_ID");
				insertMap.put("ACCESS_ID", accessId);

				List roomInfoList = this.sqlSession.selectList("SmmsRoomInfo.selectRoomNameAndRoomIdx", roomIdcMap);
				if ((roomInfoList != null) && (roomInfoList.size() > 0)) {
					Map roomInfo = (Map) roomInfoList.get(0);
					insertMap.put("ROOM_IDX", roomInfo.get("ROOM_IDX"));
					insertMap.put("ROOM_NAME", roomInfo.get("ROOM_NAME"));
				}
			}
			List listWebCaseInfo = this.sqlSession.selectList("SmmsWebCaseIp.selectRidByIp", selectMap);
			if ((listWebCaseInfo != null) && (listWebCaseInfo.size() > 0)) {
				Map webCaseInfoMap = (Map) listWebCaseInfo.get(0);
				insertMap.put("WEB_CASE_RID", webCaseInfoMap.get("RID"));
				insertMap.put("MAPPING_MODE", SMMSConstant.MAPPING_MODE_TWO);

				Map webCaseMap = (Map) this.sqlSession.selectOne("WebCase.getDomainNameAndWebstiteUrlByRid", webCaseInfoMap);
				if ((webCaseMap != null) && (webCaseMap.size() > 0)) {
					insertMap.put("SNAPSHOP", webCaseMap.get("SNAPSHOP"));
					insertMap.put("DOMAIN_NAME", webCaseMap.get("DOMAIN_NAME"));
					insertMap.put("URL", webCaseMap.get("WEBSITE_URL"));
					if ((accessId == null) || (accessId.equals(""))) {
						insertMap.put("ACCESS_ID", webCaseMap.get("ACCESS_ID"));
					}
				}
			}
		}
		return insertMap;
	}

	public String ForceClose(Map mapRid) {
		mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
		mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
		String idcId = "";
		String idcName = "";

		Map<String, String> jsonMap = (Map) this.sqlSession.selectOne("SmmsEventMain.viewToJson", mapRid);
		Map<String, String> changeJsonMap = new HashMap();
		if ((jsonMap != null) && (jsonMap.size() > 0)) {
			if (jsonMap.get("CLOSE_TERM") == null) {
				jsonMap.put("CLOSE_TERM", SMMSConstant.CLOSE_TERM);
			}
			if (jsonMap.get("RECTIFY_TERM") == null) {
				jsonMap.put("RECTIFY_TERM", SMMSConstant.RECTIFY_TERM);
			}
			if (jsonMap.get("DAMAGE_CLASS") == null) {
				jsonMap.put("DAMAGE_CLASS", "3");
			}
			if (jsonMap.get("SECURITY_TYPE") == null) {
				jsonMap.put("SECURITY_TYPE", SMMSConstant.SECURITY_TYPE);
			}
			if (jsonMap.get("ACCESS_ID") != null) {
				idcId = String.valueOf(jsonMap.get("ACCESS_ID"));
			}
			for (String key : jsonMap.keySet()) {
				changeJsonMap.put(key.toLowerCase(), (String) jsonMap.get(key));
			}
		}
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));
		Object list = new ArrayList();
		((List) list).add(changeJsonMap);
		mapTotal.put("rows", list);
		Map map = new HashMap();
		map.put("data", mapTotal);

		String json = JsonUtil.dataMapToJson(map);

		IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);
		String serviceURL = idcInfoDel.getIdcServiceUrl(idcId);
		String tokenId = idcInfoDel.getIdcToken(idcId);
		idcName = idcInfoDel.getIdcName(idcId);
		RpcBusDel rpcBusdel = new RpcBusDel(this.contex);
		String str = "";
		Map domainMap = new HashMap();
		String domain = "";
		try {
			if ((!"".equals(serviceURL)) && (serviceURL != null)) {
				System.out.println("json:" + json);

				str = rpcBusdel.rpc2102(tokenId, serviceURL, json);
				System.out.println("调用2102返回的json:" + str);

				Map messageJson = JsonUtil.jsonToDataMap(str);
				if ("000".equals(messageJson.get("return_code"))) {
					mapRid.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_END);
					this.sqlSession.update("SmmsEventMain.updateRectifyState", mapRid);
				}
				String message = (String) messageJson.get("return_msg");
				return messageJson.get("return_code") + ":" + message;
			}
			String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");

			String tokenIdBbc = FMPContex.getSystemProperty("TOKENID");
			List<Map> domainList = this.sqlSession.selectList("SmmsEventMain.viewToJsonIp", mapRid);
			for (Map urlMap : domainList) {
				domain = domain + urlMap.get("DOMAIN") + ";";
			}
			if (domain.endsWith(";")) {
				domain = domain.substring(0, domain.length() - 1);
			}
			domainMap.put("domain", domain);

			System.out.println("-------------------" + domain + "url:" + domainServiceURL);
			String domainJson = JsonUtil.dataMapToJson(domainMap);
			System.out.println("数据库存在拼接" + domainJson);
			str = "{\"return_code\":\"000\",\"return_msg\":\"处理成功\"}";
			str = rpcBusdel.rpc2105(tokenIdBbc, domainServiceURL, domainJson);

			Map messageJson = JsonUtil.jsonToDataMap(str);
			if ("000".equals(messageJson.get("return_code"))) {
				mapRid.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_ENGTHIT);
				this.sqlSession.update("SmmsEventMain.updateQzgtState", mapRid);
			}
			String message = (String) messageJson.get("return_msg");
			return messageJson.get("return_code") + ":" + message;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "无法连接" + idcName + "IDC服务器";
	}

	public int sandAll(List<Map> sendList, String idcId) {
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));

		mapTotal.put("rows", sendList);
		Map map = new HashMap();
		map.put("data", mapTotal);

		String json = JsonUtil.dataMapToJson(map);
		System.out.println("json:" + json);
		IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);
		String serviceURL = idcInfoDel.getIdcServiceUrl(idcId);
		String tokenId = idcInfoDel.getIdcToken(idcId);
		String idcName = idcInfoDel.getIdcName(idcId);
		RpcBusDel del = new RpcBusDel(this.contex);
		String str = del.rpc2101(tokenId, serviceURL, json);

		int i = 0;
		int sum = 0;
		Map messageJson = JsonUtil.jsonToDataMap(str);
		if ("000".equals(messageJson.get("return_code"))) {
			for (Map updateMap : sendList) {
				updateMap.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE_END);
				updateMap.put("SEND_TIME", FMPContex.getCurrentTime());
				updateMap.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
				i = this.sqlSession.update("SmmsEventMain.issueStateUpdate", updateMap);
				sum += i;
			}
		}
		return sum;
	}

	public String returnSandMessage(List<Map<String, String>> dataList) {
		IdcInfoDel idcInfoDel;
		String str;
		String tmp_accessid;
		String idcName;
		String idcNameErr;
		String errmsg;
		int sum;
		int sumThen;
		List sendList;
		Iterator iterator;
		idcInfoDel = new IdcInfoDel(contex);
		str = "";
		tmp_accessid = "";
		idcName = "";
		idcNameErr = "";
		errmsg = "";
		sum = 0;
		sumThen = 0;
		int sumAfter = 0;
		sendList = new ArrayList();
		iterator = dataList.iterator();
		while (iterator.hasNext()) {
			Map mapToJson;
			Map changeJsonMap;
			mapToJson = (Map) iterator.next();
			changeJsonMap = new HashMap();
			if (mapToJson.get("ACCESS_ID") == null || "".equals(mapToJson.get("ACCESS_ID"))) {
				continue; /* Loop/switch isn't completed */
			}
			if ("".equals(tmp_accessid)) {
				String key;
				for (Iterator iterator1 = mapToJson.keySet().iterator(); iterator1.hasNext(); changeJsonMap.put(key.toLowerCase(),
						(String) mapToJson.get(key))) {
					key = (String) iterator1.next();
				}

				sendList.add(changeJsonMap);
				tmp_accessid = (String) mapToJson.get("ACCESS_ID");
				continue; /* Loop/switch isn't completed */
			}
			if (tmp_accessid.equals(mapToJson.get("ACCESS_ID"))) {
				String key;
				for (Iterator iterator2 = mapToJson.keySet().iterator(); iterator2.hasNext(); changeJsonMap.put(key.toLowerCase(),
						(String) mapToJson.get(key))) {
					key = (String) iterator2.next();
				}

				sendList.add(changeJsonMap);
				continue; /* Loop/switch isn't completed */
			}
			idcName = idcInfoDel.getIdcName(tmp_accessid);
			idcNameErr = (new StringBuilder(String.valueOf(idcNameErr))).append(idcInfoDel.getIdcName(tmp_accessid)).append(",").toString();
			try {
				sum = sandAll(sendList, tmp_accessid);
				str = (new StringBuilder(String.valueOf(str))).append(idcName).append("处理了:").append(sum).append("条数据").append("\n").toString();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (!"".equals(errmsg)) {
					errmsg = (new StringBuilder("无法连接")).append(idcNameErr).append("IDC服务器").toString();
				}
				sumThen += sum;
				sendList.clear();
				String key;
				for (Iterator iterator3 = mapToJson.keySet().iterator(); iterator3.hasNext(); changeJsonMap.put(key.toLowerCase(),
						(String) mapToJson.get(key))) {
					key = (String) iterator3.next();
				}

				sendList.add(changeJsonMap);
				tmp_accessid = (String) mapToJson.get("ACCESS_ID");
			}

		}
		idcName = idcInfoDel.getIdcName(tmp_accessid);
		idcNameErr = (new StringBuilder(String.valueOf(idcNameErr))).append(idcInfoDel.getIdcName(tmp_accessid)).toString();
		try {
			sumAfter = sandAll(sendList, tmp_accessid);
			str = (new StringBuilder(String.valueOf(str))).append(idcName).append("处理了:").append(sumAfter).append("条数据").append("\n").toString();
			sumAfter += sumThen;
			str = (new StringBuilder(String.valueOf(str))).append("总共处理:").append(sumAfter).append("条数据").append("\n").toString();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (new StringBuilder("无法连接")).append(idcNameErr).append("IDC服务器").toString();
	}

	public String findDomain(String url) {
		int j = url.indexOf("/");
		String str2 = url.substring(j + 2, url.length());
		int k = str2.indexOf("/");
		String domain = str2.substring(0, k);
		return domain;
	}
}
