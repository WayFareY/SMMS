package com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.IdcInfo.delegate.IdcInfoDel;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
			if ((jsonMap.get("SECURITY_TYPE") == null) || ("".equals(jsonMap.get("SECURITY_TYPE")))) {
				listMap.put("security_type", "未知");
			} else {
				listMap.put("security_type", jsonMap.get("SECURITY_TYPE"));
			}
			if ((jsonMap.get("RECTIFY_TERM") == null) || ("".equals(jsonMap.get("RECTIFY_TERM")))) {
				listMap.put("rectify_term", "0000000000");
			} else {
				listMap.put("rectify_term", jsonMap.get("RECTIFY_TERM"));
			}
			if ((jsonMap.get("DAMAGE_CLASS") == null) || ("".equals(jsonMap.get("DAMAGE_CLASS")))) {
				listMap.put("damage_class", "3");
			} else {
				listMap.put("damage_class", jsonMap.get("THREAT_LEVEL"));
			}
			listMap.put("rectify_suggestion", jsonMap.get("RECTIY_SUGGESTION"));
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
		Long enforceCount = (Long) jsonMap.get("ENFORCE_COUNT");

		String getConnectSys = idcInfoDel.getConnectSys(idcid);

		String returnCode = "";
		try {
			if ((!"".equals(getConnectSys)) && ("1".equals(getConnectSys))) {
				FMPLog.debug("调用2101下发接口:" + json);

				String msg = rpc.rpc2101(tokenid, serviecUrl, json);

				Map messageJson = JsonUtil.jsonToDataMap(msg);
				returnCode = (String) messageJson.get("return_code");
				if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
					if ("999".equals(returnCode)) {
						mapRid.put("RECTIFY_STATE", "999");
					}
					updateRectifyState(mapRid);

					updateQzgtStateForPending(mapRid);

					insertEventHis(mapRid);
				}
				String message = (String) messageJson.get("return_msg");
				return messageJson.get("return_code") + ":" + message;
			}
			updateRectifyState(mapRid);

			updateQzgtStateForPending(mapRid);

			insertEventHis(mapRid);
			return "000:处理成功";
		} catch (Exception e) {
			e.printStackTrace();

			mapRid.put("RECTIFY_STATE", "000");
			mapRid.put("ENFORCE_COUNT", enforceCount);
			updateRectifyState(mapRid);
		}
		return "无法连接" + idcName + "IDC服务器";
	}

	public int updateRectifyState(Map mapRid) {
		if (!mapRid.containsKey("RECTIFY_STATE")) {
			mapRid.put("RECTIFY_STATE", "010");
		}
		mapRid.put("SEND_TIME", FMPContex.getCurrentTime());
		mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));

		mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
		int i = this.sqlSession.update("SmmsEventMain.updateRectifyState", mapRid);
		FMPLog.debug("更新数据" + i);
		return i;
	}

	public int sendAllForceClose(List<Map<String, Object>> sendList, String idcId)
			throws Exception {
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));

		mapTotal.put("rows", sendList);
		Map map = new HashMap();
		map.put("data", mapTotal);

		String json = JsonUtil.dataMapToJson(map);
		int sum = 0;
		IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);

		String serviceURL = idcInfoDel.getIdcServiceUrl(idcId);
		String getConnectSys = idcInfoDel.getConnectSys(idcId);
		String tokenId = idcInfoDel.getIdcToken(idcId);
		String idcName = idcInfoDel.getIdcName(idcId);
		RpcBusDel del = new RpcBusDel(this.contex);
		String str = "";

		String returnCode = "";
		if ((!"".equals(getConnectSys)) && ("1".equals(getConnectSys))) {
			FMPLog.debug("2102接口：" + json);

			str = del.rpc2102(tokenId, serviceURL, json);
			FMPLog.debug("2012返回json:" + str);
			Map messageJson = JsonUtil.jsonToDataMap(str);

			returnCode = (String) messageJson.get("return_code");
			if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
				for (Map ridMap : sendList) {
					if ("999".equals(returnCode)) {
						ridMap.put("RECTIFY_STATE", "999");
					}
					ridMap.put("EVENT_RID", ridMap.get("event_id"));

					Long enforceCount = (Long) ridMap.get("enforce_count");
					enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
					ridMap.put("ENFORCE_COUNT", enforceCount);

					ridMap.put("ENFORCE_USER", getWebData("CURR_USERID"));

					ridMap.put("ENFORCE_TIME", FMPContex.getCurrentTime());
					int i = updateQzgtState(ridMap);

					ridMap.put("DETAIL_RID", ridMap.get("detail_rid"));

					System.out.println("执法人：" + getWebData("CURR_USERID"));
					updateQzgtStateForPending(ridMap);

					insertEventHis(ridMap);
					FMPLog.debug("数据" + i);
					sum += i;
				}
			}
		} else {
			String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");

			String tokenIdBbc = FMPContex.getSystemProperty("TOKENID");

			Object domainMap = new HashMap();
			String domain = "";
			for (Map urlMap : sendList) {
				domain = domain + urlMap.get("domain") + ";";
			}
			if (domain.endsWith(";")) {
				domain = domain.substring(0, domain.length() - 1);
			}
			((Map) domainMap).put("domain", domain);

			((Map) domainMap).put("opMode", Integer.valueOf(Integer.parseInt("1")));

			FMPLog.debug("-------------------" + domain + "url:" + domainServiceURL);
			String domainJson = JsonUtil.dataMapToJson((Map) domainMap);

			FMPLog.debug("调用2105的json:" + domainJson);
			str = del.rpc2105(tokenIdBbc, domainServiceURL, domainJson);
			Map messageJson = JsonUtil.jsonToDataMap(str);

			returnCode = (String) messageJson.get("return_code");
			if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
				for (Map ridMap : sendList) {
					if ("999".equals(returnCode)) {
						ridMap.put("RECTIFY_STATE", "999");
					} else {
						ridMap.put("RECTIFY_STATE", "888");
					}
					Long enforceCount = (Long) ridMap.get("enforce_count");
					enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
					ridMap.put("EVENT_RID", ridMap.get("event_id"));
					ridMap.put("ENFORCE_COUNT", enforceCount);

					Long closeCount = (Long) ridMap.get("close_count");
					closeCount = Long.valueOf(closeCount.longValue() + 1L);
					ridMap.put("CLOSE_COUNT", closeCount);

					ridMap.put("ENFORCE_USER", getWebData("CURR_USERID"));

					ridMap.put("ENFORCE_TIME", FMPContex.getCurrentTime());
					int i = updateQzgtState(ridMap);

					ridMap.put("DETAIL_RID", ridMap.get("detail_rid"));
					updateQzgtStateForPending(ridMap);

					insertEventHis(ridMap);
					sum += i;
				}
			}
		}
		return sum;
	}

	public String returnForceCloseMessage(List<Map<String, Object>> forceCloselist) {
		IdcInfoDel idcInfoDel = new IdcInfoDel(contex);
		String str = "";
		String tmp_accessid = "";
		String idcName = "";
		String idcNameErr = "";
		String errmsg = "";
		int sum = 0;
		int sumThen = 0;
		int sumAfter = 0;
		List sendList = new ArrayList();
		Iterator iterator = forceCloselist.iterator();
		while (iterator.hasNext()) {
			Map mapToJson;
			Map changeJsonMap;
			mapToJson = (Map) iterator.next();
			changeJsonMap = new HashMap();
			if (mapToJson.get("CLOSE_TERM") == null || "".equals(mapToJson.get("CLOSE_TERM"))) {
				mapToJson.put("CLOSE_TERM", "0000000000");
			}
			if (mapToJson.get("RECTIFY_TERM") == null || "".equals(mapToJson.get("RECTIFY_TERM"))) {
				mapToJson.put("RECTIFY_TERM", "0000000000");
			}
			if (mapToJson.get("DAMAGE_CLASS") == null || "".equals(mapToJson.get("DAMAGE_CLASS"))) {
				mapToJson.put("DAMAGE_CLASS", "3");
			}
			if (mapToJson.get("SECURITY_TYPE") == null || "".equals(mapToJson.get("SECURITY_TYPE"))) {
				mapToJson.put("SECURITY_TYPE", "未知");
			}
			if (mapToJson.get("ACCESS_ID") == null || "".equals(mapToJson.get("ACCESS_ID"))) {
				continue; /* Loop/switch isn't completed */
			}
			if ("".equals(tmp_accessid)) {
				String key;
				for (Iterator iterator1 = mapToJson.keySet().iterator(); iterator1.hasNext(); changeJsonMap.put(key.toLowerCase(),
						mapToJson.get(key))) {
					key = (String) iterator1.next();
				}
				sendList.add(changeJsonMap);
				tmp_accessid = (String) mapToJson.get("ACCESS_ID");
				continue; /* Loop/switch isn't completed */
			}
			if (tmp_accessid.equals(mapToJson.get("ACCESS_ID"))) {
				String key;
				for (Iterator iterator2 = mapToJson.keySet().iterator(); iterator2.hasNext(); changeJsonMap.put(key.toLowerCase(),
						mapToJson.get(key))) {
					key = (String) iterator2.next();
				}
				sendList.add(changeJsonMap);
				continue; /* Loop/switch isn't completed */
			}
			idcName = idcInfoDel.getIdcName(tmp_accessid);
			try {
				sum = sendAllForceClose(sendList, tmp_accessid);
				str = (new StringBuilder(String.valueOf(str))).append(idcName).append("处理了:").append(sum).append("条数据").append("\n").toString();
			} catch (Exception e) {
				e.printStackTrace();
				idcNameErr = (new StringBuilder(String.valueOf(idcNameErr))).append(idcInfoDel.getIdcName(tmp_accessid)).append(",").toString();
				sum = 0;
				errmsg = (new StringBuilder("无法连接")).append(idcNameErr).append("IDC服务器").append("\n").toString();
			} finally {
				sumThen += sum;
				sendList.clear();
				String key;
				for (Iterator iterator3 = mapToJson.keySet().iterator(); iterator3.hasNext(); changeJsonMap.put(key.toLowerCase(),
						mapToJson.get(key))) {
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
			str = (new StringBuilder(String.valueOf(str))).append(errmsg).toString();
			sumAfter += sumThen;
			str = (new StringBuilder(String.valueOf(str))).append("总共处理:").append(sumAfter).append("条数据").append("\n").toString();
			FMPLog.debug((new StringBuilder("执行最后一个关停：")).append(str).toString());
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (idcNameErr.endsWith(",")) {
			idcNameErr = idcNameErr.substring(1, idcNameErr.length() - 1);
		}
		sumAfter += sumThen;
		str = (new StringBuilder(String.valueOf(str))).append("总共处理:").append(sumAfter).append("条数据").append("\n").toString();
		errmsg = (new StringBuilder(String.valueOf(str))).append("\n").append("无法连接").append(idcNameErr).append("IDC服务器").toString();
		return errmsg;
	}

	public String insertSmmsEventMain(Map insertMap) {
		String rid = FMPContex.getNewUUID();
		insertMap.put("RID", rid);

		insertMap.put("REPORT_CITY", "珠海");

		insertMap.put("REPORT_TIME", FMPContex.getCurrentTime());
		if ((insertMap.get("OCCUR_TIME") == null) || ("".equals(insertMap.get("OCCUR_TIME")))) {
			insertMap.put("OCCUR_TIME", FMPContex.getCurrentTime());
		}
		insertMap.put("IS_WHITE_LIST", "2");
		if ((insertMap.get("SYS_RECTIFY_SUGGEST") == null) || ("".equals(insertMap.get("SYS_RECTIFY_SUGGEST")))) {
			insertMap.put("SYS_RECTIFY_SUGGEST", "1");
		}
		if ((insertMap.get("SYS_RECTIFY_SUGGEST") == null) || ("".equals(insertMap.get("SYS_RECTIFY_SUGGEST")))) {
			insertMap.put("FINAL_RECTIFY_SUGGEST", "1");
		}
		insertMap.put("CREATTIME", FMPContex.getCurrentTime());

		insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());

		insertMap.put("RECTIFY_STATE", "000");
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

			Map webCaseRidMap = new HashMap();
			webCaseRidMap.put("CASE_RID", webCaseMap.get("RID"));
			List webCaseIpList = this.sqlSession.selectList("SmmsWebCaseIp.findAccessIpByCaseRid", webCaseRidMap);
			if ((webCaseIpList != null) && (webCaseIpList.size() > 0)) {
				Map webCaseIpMap = (Map) webCaseIpList.get(0);
				webCaseIpMap.put("IP", webCaseIpMap.get("ACCESS_IP"));
				webCaseIpMap.put("ACCESS_ID", webCaseMap.get("ACCESS_ID"));
				webCaseIpMap.put("DBTYPE", FMPContex.DBType);
				insertMap.put("IP", webCaseIpMap.get("ACCESS_IP"));

				List listRoomIdc = this.sqlSession.selectList("SmmsRoomIprange.selectAccesIdByIp", webCaseIpMap);
				if ((listRoomIdc != null) && (listRoomIdc.size() > 0)) {
					Map roomIdcMap = (Map) listRoomIdc.get(0);

					List listRoomAndIdc = this.sqlSession.selectList("SmmsEventMain.checkAccessIdAndRoomIdx", roomIdcMap);
					if ((listRoomAndIdc != null) && (listRoomAndIdc.size() > 0)) {
						Map roomInfoMap = (Map) listRoomAndIdc.get(0);

						insertMap.put("ROOM_NAME", roomInfoMap.get("ROOM_NAME"));

						insertMap.put("ROOM_IDX", roomInfoMap.get("ROOM_IDX"));
					}
				}
			}
			insertMap.put("MAPPING_MODE", "1");
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
				insertMap.put("MAPPING_MODE", "2");

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
		String idcId = "";

		String idcName = "";

		Long enforceCount = null;

		String returnCode = "";

		Map<String, Object> jsonMap = (Map) this.sqlSession.selectOne("SmmsEventMain.viewToJson", mapRid);
		Map<String, Object> changeJsonMap = new HashMap();
		if ((jsonMap != null) && (jsonMap.size() > 0)) {
			if ((jsonMap.get("CLOSE_TERM") == null) || ("".equals(jsonMap.get("CLOSE_TERM")))) {
				jsonMap.put("CLOSE_TERM", "0000000000");
			}
			if ((jsonMap.get("RECTIFY_TERM") == null) || ("".equals(jsonMap.get("RECTIFY_TERM")))) {
				jsonMap.put("RECTIFY_TERM", "0000000000");
			}
			if ((jsonMap.get("DAMAGE_CLASS") == null) || ("".equals(jsonMap.get("DAMAGE_CLASS")))) {
				jsonMap.put("DAMAGE_CLASS", "3");
			}
			if ((jsonMap.get("SECURITY_TYPE") == null) || ("".equals(jsonMap.get("SECURITY_TYPE")))) {
				jsonMap.put("SECURITY_TYPE", "未知");
			}
			if (jsonMap.get("ACCESS_ID") != null) {
				idcId = String.valueOf(jsonMap.get("ACCESS_ID"));
			}
			for (String key : jsonMap.keySet()) {
				changeJsonMap.put(key.toLowerCase(), jsonMap.get(key));
			}
		}
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));
		Object list = new ArrayList();
		((List) list).add(changeJsonMap);
		mapTotal.put("rows", list);
		Map map = new HashMap();
		map.put("data", mapTotal);
		try {
			String json = JsonUtil.dataMapToJson(map);
			IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);

			String serviceURL = idcInfoDel.getIdcServiceUrl(idcId);

			String getConnectSys = idcInfoDel.getConnectSys(idcId);

			String tokenId = idcInfoDel.getIdcToken(idcId);

			idcName = idcInfoDel.getIdcName(idcId);
			RpcBusDel rpcBusdel = new RpcBusDel(this.contex);

			String str = "";

			String message = "";

			enforceCount = (Long) jsonMap.get("ENFORCE_COUNT");
			if ((!"".equals(getConnectSys)) && ("1".equals(getConnectSys))) {
				FMPLog.debug("调用2102的前json:" + json);

				str = rpcBusdel.rpc2102(tokenId, serviceURL, json);
				FMPLog.debug("调用2102返回的json:" + str);

				Map messageJson = JsonUtil.jsonToDataMap(str);
				returnCode = (String) messageJson.get("return_code");
				if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
					if ("999".equals(returnCode)) {
						mapRid.put("RECTIFY_STATE", "999");
					}
					if (mapRid.containsKey("CLOSE_COUNT")) {
						mapRid.remove("CLOSE_COUNT");
					}
					updateQzgtState(mapRid);

					updateQzgtStateForPending(mapRid);

					insertEventHis(mapRid);
				}
				message = (String) messageJson.get("return_msg");
				return messageJson.get("return_code") + ":" + message;
			}
			idcName = "bbc";
			return bbcForceClose(mapRid, rpcBusdel);
		} catch (Exception e) {
			e.printStackTrace();

			mapRid.put("RECTIFY_STATE", "000");
			mapRid.put("ENFORCE_COUNT", enforceCount);
			updateQzgtState(mapRid);
		}
		return "无法连接" + idcName + "IDC服务器";
	}

	public String bbcForceClose(Map mapRid, RpcBusDel rpcBusdel)
			throws Exception {
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
		domainMap.put("opMode", Integer.valueOf(Integer.parseInt("1")));
		FMPLog.debug("-------------------" + domain + "url:" + domainServiceURL);
		String domainJson = JsonUtil.dataMapToJson(domainMap);
		FMPLog.debug("数据库存在拼接" + domainJson);

		String str = rpcBusdel.rpc2105(tokenIdBbc, domainServiceURL, domainJson);
		FMPLog.debug("2105响应回来的json:" + str);

		Map messageJson = JsonUtil.jsonToDataMap(str);
		returnCode = (String) messageJson.get("return_code");
		if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
			for (Map enforceCountMap : domainList) {
				if ("000".equals(returnCode)) {
					enforceCountMap.put("RECTIFY_STATE", "888");
				} else {
					enforceCountMap.put("RECTIFY_STATE", "999");
				}
				if (((mapRid.containsKey("RECTIFY_STATE")) && (!"030".equals(mapRid.get("RECTIFY_STATE"))))
						|| (!mapRid.containsKey("RECTIFY_STATE"))) {
					enforceCountMap.put("ENFORCE_USER", mapRid.get("ENFORCE_USER"));

					enforceCountMap.put("ENFORCE_TIME", mapRid.get("ENFORCE_TIME"));

					enforceCountMap.put("ENFORCE_COUNT", mapRid.get("ENFORCE_COUNT"));
					enforceCountMap.put("EVENT_RID", mapRid.get("EVENT_RID"));
					enforceCountMap.put("DETAIL_RID", mapRid.get("DETAIL_RID"));
					enforceCountMap.put("CLOSE_COUNT", mapRid.get("CLOSE_COUNT"));
					updateQzgtState(enforceCountMap);

					updateQzgtStateForPending(enforceCountMap);

					insertEventHis(enforceCountMap);
				}
			}
		}
		String message = (String) messageJson.get("return_msg");
		return messageJson.get("return_code") + ":" + message;
	}

	public String bbcBlocking(Map dataMap)
			throws Exception {
		Map getMap = (Map) this.sqlSession.selectOne("SmmsEventMain.unblockApply", dataMap);

		RpcBusDel del = new RpcBusDel(this.contex);

		String domainServiceURL = FMPContex.getSystemProperty("DOMAIN_SERVICE");

		String tokenIdBbc = FMPContex.getSystemProperty("TOKENID");
		Map domainMap = new HashMap();
		domainMap.put("domain", getMap.get("DOMAIN_NAME"));

		domainMap.put("opMode", Integer.valueOf("2"));
		EventMainDel mainDel = new EventMainDel(this.contex);
		String json = JsonUtil.dataMapToJson(domainMap);
		FMPLog.debug("解封申请2105：" + json);
		FMPLog.debug("domainServiceUrl" + domainServiceURL);
		FMPLog.debug("tokenId:" + tokenIdBbc);

		String str = del.rpc2105(tokenIdBbc, domainServiceURL, json);
		Map messageJson = JsonUtil.jsonToDataMap(str);
		if ("000".equals(messageJson.get("return_code"))) {
			dataMap.put("RECTIFY_STATE", "030");

			dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
			int result = this.sqlSession.update("SmmsEventMain.updateQzgtState", dataMap);
			if (result > 0) {
				FMPLog.printLog(getWebData("RID") + ",该条rid记录修改RECTIFY_STATE的状态为(000:未处理)，处理成功");
			} else {
				FMPLog.printLog(getWebData("RID") + ",该条rid记录修改RECTIFY_STATE的状态为(000:未处理)，处理失败");
			}
		}
		FMPLog.debug("调用2105返回json" + str);
		return (String) messageJson.get("return_code") + ":" + (String) messageJson.get("return_msg");
	}

	public int updateQzgtState(Map mapRid) {
		if (!mapRid.containsKey("RECTIFY_STATE")) {
			mapRid.put("RECTIFY_STATE", "010");
		}
		mapRid.put("SEND_TIME", FMPContex.getCurrentTime());

		mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));

		mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
		int i = this.sqlSession.update("SmmsEventMain.updateQzgtState", mapRid);
		FMPLog.debug("是否更新成功:" + i);
		return i;
	}

	public int updateQzgtStateForPending(Map mapRid) {
		if (!mapRid.containsKey("RECTIFY_STATE")) {
			mapRid.put("RECTIFY_STATE", "010");
		}
		mapRid.put("SEND_TIME", FMPContex.getCurrentTime());

		mapRid.put("SEND_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));

		mapRid.put("MODIFIEDTIME", FMPContex.getCurrentTime());
		int i = this.sqlSession.update("SmmsPendingEvent.updateQzgtState", mapRid);
		FMPLog.debug("从表是否更新成功:" + i);
		return i;
	}

	public int insertEventHis(Map mapRid) {
		Map hisMap = (Map) this.sqlSession.selectOne("SmmsEventMain.findEventMainMessage", mapRid);
		hisMap.put("RID", FMPContex.getNewUUID());

		int i = this.sqlSession.insert("SmmsEnforceHis.insertSave", hisMap);
		FMPLog.debug("插入历史记录表:" + i);
		return i;
	}

	public int sandAll(List<Map> sendList, String idcId) {
		Map mapTotal = new HashMap();
		mapTotal.put("total", Integer.valueOf(1));

		mapTotal.put("rows", sendList);
		Map map = new HashMap();
		map.put("data", mapTotal);

		String json = JsonUtil.dataMapToJson(map);
		FMPLog.debug("json:" + json);
		IdcInfoDel idcInfoDel = new IdcInfoDel(this.contex);
		String serviceURL = idcInfoDel.getIdcServiceUrl(idcId);
		String tokenId = idcInfoDel.getIdcToken(idcId);
		String idcName = idcInfoDel.getIdcName(idcId);
		RpcBusDel del = new RpcBusDel(this.contex);

		String getConnectSys = idcInfoDel.getConnectSys(idcId);
		FMPLog.debug("是否对接系统：" + getConnectSys);

		String returnCode = "";

		int sum = 0;

		Long enforceCount = null;
		if ((!"".equals(getConnectSys)) && ("1".equals(getConnectSys))) {
			FMPLog.debug("调用2101下发接口:" + json);
			String str = del.rpc2101(tokenId, serviceURL, json);

			int i = 0;

			Map messageJson = JsonUtil.jsonToDataMap(str);

			returnCode = (String) messageJson.get("return_code");
			if (("000".equals(returnCode)) || ("999".equals(returnCode))) {
				for (Iterator localIterator = sendList.iterator(); localIterator.hasNext();) {
					Map updateMap = (Map) localIterator.next();
					if ("999".equals(returnCode)) {
						updateMap.put("RECTIFY_STATE", "999");
					}
					updateMap.put("EVENT_RID", updateMap.get("event_id"));
					enforceCount = (Long) updateMap.get("enforce_count");
					enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
					updateMap.put("ENFORCE_COUNT", enforceCount);

					updateMap.put("ENFORCE_USER", getWebData("CURR_USERID"));

					updateMap.put("ENFORCE_TIME", FMPContex.getCurrentTime());
					i = updateRectifyState(updateMap);

					updateMap.put("DETAIL_RID", updateMap.get("detail_rid"));
					updateQzgtStateForPending(updateMap);

					insertEventHis(updateMap);
					sum += i;
				}
			}
		} else {
			for (Map updateMap : sendList) {
				updateMap.put("EVENT_RID", updateMap.get("event_id"));
				enforceCount = (Long) updateMap.get("enforce_count");
				enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
				updateMap.put("ENFORCE_COUNT", enforceCount);

				updateMap.put("ENFORCE_USER", getWebData("CURR_USERID"));

				updateMap.put("ENFORCE_TIME", FMPContex.getCurrentTime());

				int j = updateRectifyState(updateMap);

				updateMap.put("DETAIL_RID", updateMap.get("detail_rid"));
				updateQzgtStateForPending(updateMap);
				sum += j;

				insertEventHis(updateMap);
			}
		}
		return sum;
	}

	public String returnSandMessage(List<Map<String, Object>> dataList) {
		IdcInfoDel idcInfoDel = new IdcInfoDel(contex);
		String str = "";
		String tmp_accessid = "";
		String idcName = "";
		String idcNameErr = "";
		String errmsg = "";
		int sum = 0;
		int sumThen = 0;
		int sumAfter = 0;
		List sendList = new ArrayList();
		Iterator iterator = dataList.iterator();
		while (iterator.hasNext()) {
			Map mapToJson;
			Map changeJsonMap;
			mapToJson = (Map) iterator.next();
			if (mapToJson.get("RECTIFY_TERM") == null || "".equals(mapToJson.get("RECTIFY_TERM"))) {
				mapToJson.put("RECTIFY_TERM", "0000000000");
			}
			if (mapToJson.get("DAMAGE_CLASS") == null || "".equals(mapToJson.get("DAMAGE_CLASS"))) {
				mapToJson.put("DAMAGE_CLASS", "3");
			}
			if (mapToJson.get("SECURITY_TYPE") == null || "".equals(mapToJson.get("SECURITY_TYPE"))) {
				mapToJson.put("SECURITY_TYPE", "未知");
			}
			changeJsonMap = new HashMap();
			if (mapToJson.get("ACCESS_ID") == null || "".equals(mapToJson.get("ACCESS_ID"))) {
				continue; /* Loop/switch isn't completed */
			}
			if ("".equals(tmp_accessid)) {
				String key;
				for (Iterator iterator1 = mapToJson.keySet().iterator(); iterator1.hasNext(); changeJsonMap.put(key.toLowerCase(),
						mapToJson.get(key))) {
					key = (String) iterator1.next();
				}
				sendList.add(changeJsonMap);
				tmp_accessid = (String) mapToJson.get("ACCESS_ID");
				continue; /* Loop/switch isn't completed */
			}
			if (tmp_accessid.equals(mapToJson.get("ACCESS_ID"))) {
				String key;
				for (Iterator iterator2 = mapToJson.keySet().iterator(); iterator2.hasNext(); changeJsonMap.put(key.toLowerCase(),
						mapToJson.get(key))) {
					key = (String) iterator2.next();
				}
				sendList.add(changeJsonMap);
				continue; /* Loop/switch isn't completed */
			}
			idcName = idcInfoDel.getIdcName(tmp_accessid);
			try {
				sum = sandAll(sendList, tmp_accessid);
				str = (new StringBuilder(String.valueOf(str))).append(idcName).append("处理了:").append(sum).append("条数据").append("\n").toString();
			} catch (Exception e) {
				e.printStackTrace();
				sum = 0;
				idcNameErr = (new StringBuilder(String.valueOf(idcNameErr))).append(idcInfoDel.getIdcName(tmp_accessid)).append(",").toString();
				FMPLog.debug((new StringBuilder("执行错误idc名称：")).append(idcNameErr).toString());
				errmsg = (new StringBuilder("无法连接")).append(idcNameErr).append("IDC服务器").append("\n").toString();
			} finally {
				sumThen += sum;
				sendList.clear();
				String key;
				for (Iterator iterator3 = mapToJson.keySet().iterator(); iterator3.hasNext(); changeJsonMap.put(key.toLowerCase(),
						mapToJson.get(key))) {
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
			str = (new StringBuilder(String.valueOf(str))).append(errmsg).toString();
			sumAfter += sumThen;
			str = (new StringBuilder(String.valueOf(str))).append("总共处理:").append(sumAfter).append("条数据").append("\n").toString();
			return str;
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (idcNameErr.endsWith(","))
			idcNameErr = idcNameErr.substring(0, idcNameErr.length() - 1);
		sumAfter += sumThen;
		str = (new StringBuilder(String.valueOf(str))).append("总共处理:").append(sumAfter).append("条数据").append("\n").toString();
		FMPLog.debug((new StringBuilder("数据")).append(str).toString());
		errmsg = (new StringBuilder(String.valueOf(str))).append("\n").append("无法连接").append(idcNameErr).append("IDC服务器").toString();
		FMPLog.debug((new StringBuilder("数据")).append(errmsg).toString());
		return errmsg;
	}

	public String findDomain(String url) {
		String domainName = "";
		try {
			URL domain = new URL(url);
			return domain.getHost();
		} catch (MalformedURLException e) {
			e.printStackTrace();
			domainName = url;
		}
		return domainName;
	}

	public String getIsClose(Map afMap, String keyWordType) {
		String isForceClose = FMPContex.getSystemParameter("BIZ3");
		if ("1".equals(isForceClose)) {
			if ((!"".equals(keyWordType)) && (keyWordType != null) && (!"null".equals(keyWordType))) {
				if ((keyWordType.indexOf("010") != -1) || (keyWordType.indexOf("020") != -1) || (keyWordType.indexOf("030") != -1)) {
					return "1";
				}
				return "2";
			}
			boolean priority = afMap.containsKey("PRIORITY");

			boolean reliability = afMap.containsKey("RELIABILITY");

			String sxqd = (String) afMap.get("PRIORITY");
			String wxdj = (String) afMap.get("RELIABILITY");
			if (("3".equals(sxqd)) && ("3".equals(wxdj))) {
				return "1";
			}
			return "2";
		}
		return "2";
	}

	public Map getSuggestMap(String isClose) {
		Map insertMap = new HashMap();

		String isApprove = FMPContex.getSystemParameter("BIZ5");
		if ("1".equals(isClose)) {
			if ("1".equals(isApprove)) {
				insertMap.put("SYS_RECTIFY_SUGGEST", "2");

				insertMap.put("FINAL_RECTIFY_SUGGEST", "0");
			} else {
				insertMap.put("SYS_RECTIFY_SUGGEST", "2");
				insertMap.put("FINAL_RECTIFY_SUGGEST", "2");
			}
		} else {
			insertMap.put("SYS_RECTIFY_SUGGEST", "1");
			insertMap.put("FINAL_RECTIFY_SUGGEST", "1");
		}
		return insertMap;
	}

	public Map checkEventMain(Map insertMap, Long total, Map resultMap, Map map) {
		Map eventMap = new HashMap();
		Map ridMap = new HashMap();

		String mainRid = "";
		Long detialCount = null;
		String detailRid = FMPContex.getNewUUID();

		insertMap.put("DETAIL_RID", detailRid);
		if (insertMap.containsKey("WEB_CASE_RID")) {
			resultMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countWebCaseRidTotal", insertMap);

			total = (Long) resultMap.get("WEBCASETOTAL");
			if (total.longValue() == 0L) {
				insertMap.put("DETIAL_COUNT", Integer.valueOf(1));

				insertMap.put("ENFORCE_COUNT", Integer.valueOf(0));

				insertMap.put("RECTIFY_COUNT", Integer.valueOf(0));

				insertMap.put("CLOSE_COUNT", Integer.valueOf(0));
				mainRid = insertSmmsEventMain(insertMap);
				ridMap.put("RID", mainRid);
				resultMap = (Map) this.sqlSession.selectOne("SmmsEventMain.findEventMainInfo", ridMap);
			} else {
				mainRid = (String) resultMap.get("MAIN_RID");
				detialCount = (Long) resultMap.get("DETIAL_COUNT");
				detialCount = Long.valueOf(detialCount.longValue() + 1L);
				map.put("DETIAL_COUNT", detialCount);

				map.put("MODIFIEDTIME", FMPContex.getCurrentTime());
				map.put("RID", mainRid);
				this.sqlSession.update("SmmsEventMain.updateDetialCount", map);
			}
		} else if ((insertMap.containsKey("DOMAIN_NAME")) && (!"".equals(insertMap.get("DOMAIN_NAME")))
				&& (insertMap.get("DOMAIN_NAME") != null)) {
			resultMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countDomainTotal", insertMap);

			total = (Long) resultMap.get("DOMAINTOTAL");
			if (total.longValue() == 0L) {
				insertMap.put("DETIAL_COUNT", Integer.valueOf(1));

				insertMap.put("ENFORCE_COUNT", Integer.valueOf(0));

				insertMap.put("RECTIFY_COUNT", Integer.valueOf(0));

				insertMap.put("CLOSE_COUNT", Integer.valueOf(0));
				mainRid = insertSmmsEventMain(insertMap);
				ridMap.put("RID", mainRid);
				resultMap = (Map) this.sqlSession.selectOne("SmmsEventMain.findEventMainInfo", ridMap);
			} else {
				mainRid = (String) resultMap.get("MAIN_RID");
				detialCount = (Long) resultMap.get("DETIAL_COUNT");
				detialCount = Long.valueOf(detialCount.longValue() + 1L);
				map.put("DETIAL_COUNT", detialCount);
				map.put("MODIFIEDTIME", FMPContex.getCurrentTime());
				map.put("RID", mainRid);
				this.sqlSession.update("SmmsEventMain.updateDetialCount", map);
			}
		} else if (insertMap.containsKey("IP")) {
			resultMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countIpTotal", insertMap);

			total = (Long) resultMap.get("IPTOTAL");
			if (total.longValue() == 0L) {
				insertMap.put("DETIAL_COUNT", Integer.valueOf(1));

				insertMap.put("ENFORCE_COUNT", Integer.valueOf(0));

				insertMap.put("RECTIFY_COUNT", Integer.valueOf(0));

				insertMap.put("CLOSE_COUNT", Integer.valueOf(0));
				mainRid = insertSmmsEventMain(insertMap);
				ridMap.put("RID", mainRid);
				resultMap = (Map) this.sqlSession.selectOne("SmmsEventMain.findEventMainInfo", ridMap);
			} else {
				mainRid = (String) resultMap.get("MAIN_RID");
				detialCount = (Long) resultMap.get("DETIAL_COUNT");
				detialCount = Long.valueOf(detialCount.longValue() + 1L);
				map.put("DETIAL_COUNT", detialCount);
				map.put("MODIFIEDTIME", FMPContex.getCurrentTime());
				map.put("RID", mainRid);
				this.sqlSession.update("SmmsEventMain.updateDetialCount", map);
			}
		}
		eventMap.put("DETAIL_RID", detailRid);
		eventMap.put("MAIN_RID", mainRid);
		eventMap.put("RECTIFY_STATE", resultMap.get("RECTIFY_STATE"));
		eventMap.put("CREATTIME", resultMap.get("CREATTIME"));
		eventMap.put("ENFORCE_COUNT", resultMap.get("ENFORCE_COUNT"));
		eventMap.put("CLOSE_COUNT", resultMap.get("CLOSE_COUNT"));
		eventMap.put("THREAT_TYPE4", resultMap.get("THREAT_TYPE4"));
		eventMap.put("THREAT_TYPE1", resultMap.get("THREAT_TYPE1"));
		eventMap.put("THREAT_TYPE2", resultMap.get("THREAT_TYPE2"));
		eventMap.put("KEYWORDS", resultMap.get("KEYWORDS"));
		eventMap.put("IS_FORCE_CLOSE", resultMap.get("IS_FORCE_CLOSE"));
		eventMap.put("PRIORITY", resultMap.get("PRIORITY"));
		eventMap.put("THREAT_LEVEL", resultMap.get("THREAT_LEVEL"));
		eventMap.put("FINAL_RECTIFY_SUGGEST", resultMap.get("FINAL_RECTIFY_SUGGEST"));
		return eventMap;
	}

	public Map checkForece(Map eventMap, Map insertMap) {
		Map checkMap = new HashMap();
		String selectClose = (String) eventMap.get("IS_FORCE_CLOSE");
		String isClose = (String) insertMap.get("IS_FORCE_CLOSE");
		if (selectClose.compareTo(isClose) >= 0) {
			checkMap.put("IS_FORCE_CLOSE", isClose);
		}
		return checkMap;
	}

	public boolean checkTime(Map eventMap) {
		Date date = new Date();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String createDate = sdf.format(date);
		String createTime = (String) eventMap.get("CREATTIME");

		createTime = createTime.substring(0, 10);
		if (createDate.equals(createTime)) {
			return true;
		}
		return false;
	}
}
