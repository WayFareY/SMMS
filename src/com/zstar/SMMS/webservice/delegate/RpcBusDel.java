package com.zstar.SMMS.webservice.delegate;

import com.opensymphony.xwork2.ActionContext;
import com.smms.WsRpciProxy;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.BaseData.SmmsRpcLog.delegate.RpcLogDel;
import com.zstar.SMMS.BaseData.SptCityRectify.action.del.CityRecityDel;
import com.zstar.SMMS.STAT.SmmsCitySumYb.action.del.SmmsCitySumDel;
import com.zstar.SMMS.constant.SMMSConstant;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RpcBusDel extends BaseDelegate {

	// 远程接口日志代理类
	private RpcLogDel del = new RpcLogDel(this.contex);

	private String tokenId;

	public String getTokenId() {
		return this.tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public RpcBusDel(ActionContext contex) {
		super(contex);
	}

	/**
	 * 根据传入的json串，然后解释，并保存备案新到IDC机房信息和IDC机房网段信息
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rpc1001(String json) {
		Map<String, Object> map = JsonUtil.jsonToDataMap(json);

		Map<String, Object> dataMap = (Map) map.get("data");

		String idc_id = (String) dataMap.get("access_id");
		Map ssrMap = new HashMap();
		ssrMap.put("ACCESS_ID", String.valueOf(dataMap.get("access_id")));
		ssrMap.put("ROOM_IDX", String.valueOf(dataMap.get("machine_room_idx")));
		ssrMap.put("ROOM_NAME", String.valueOf(dataMap.get("machine_room_name")));
		ssrMap.put("ROOM_ADDRESS", String.valueOf(dataMap.get("machine_room_place")));
		ssrMap.put("ROOM_PROPERTY", String.valueOf(dataMap.get("machine_room_property")));
		ssrMap.put("ROOM_MANAGER", String.valueOf(dataMap.get("room_manager")));
		ssrMap.put("MANAGER_TEL", String.valueOf(dataMap.get("room_manager_tel")));
		ssrMap.put("MANAGER_MOBILE", String.valueOf(dataMap.get("room_manager_mobile")));
		ssrMap.put("CREATTIME", FMPContex.getCurrentTime());
		ssrMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
		ssrMap.put("RECORDSTATE", "0");
		ssrMap.put("RID", FMPContex.getNewUUID());

		String smmsRoomInfoErrorMessage = checkValidMap(ssrMap,
				"ACCESS_ID,ROOM_NAME,ROOM_IDX,ROOM_PROPERTY,ROOM_MANAGER,MANAGER_TEL,MANAGER_MOBILE",
				"ACCESS_ID:10,ROOM_NAME:20,ROOM_IDX:3,ROOM_ADDRESS:100,ROOM_PROPERTY:1,ROOM_MANAGER:20,MANAGER_TEL:20,MANAGER_MOBILE:100");
		String success = "{\"return_code\":\"000\",\"return_msg\":\"处理成功\"}";
		String smmsRoomIprangeInfoErrorMessage = "";
		int i = 0;
		if ("".equals(smmsRoomInfoErrorMessage)) {
			this.sqlSession.update("SmmsRoomInfo.updateRecordstate", ssrMap);
			i = this.sqlSession.insert("SmmsRoomInfo.insertSave", ssrMap);
			if (i == 1) {
				List<Map> list = (List) dataMap.get("rows");
				for (Map siiMap : list) {
					siiMap.put("RID", FMPContex.getNewUUID());
					siiMap.put("ACCESS_ID", String.valueOf(dataMap.get("access_id")));
					siiMap.put("ROOM_IDX", String.valueOf(dataMap.get("machine_room_idx")));
					siiMap.put("CREATTIME", FMPContex.getCurrentTime());
					siiMap.put("BEGIN_IP", String.valueOf(siiMap.get("begin_ip")));
					siiMap.put("END_IP", String.valueOf(siiMap.get("end_ip")));
					siiMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
					siiMap.put("RECORDSTATE", "0");
					smmsRoomIprangeInfoErrorMessage = checkValidMap(siiMap, "ACCESS_ID,ROOM_IDX,BEGIN_IP,END_IP",
							"ACCESS_ID:10,ROOM_IDX:3,BEGIN_IP:15,END_IP:15");
					if ("".equals(smmsRoomIprangeInfoErrorMessage)) {
						this.sqlSession.update("SmmsRoomIprange.updateRecordstate", siiMap);
						this.sqlSession.insert("SmmsRoomIprange.insertSave", siiMap);
					} else {
						smmsRoomInfoErrorMessage = smmsRoomInfoErrorMessage + smmsRoomIprangeInfoErrorMessage;
					}
				}
			}
		}
		// 定义错误信息
		String errorMessage = "{\"return_code\":\"999\",\"return_msg\":\"处理失败," + smmsRoomInfoErrorMessage + "\"}";
		if ("".equals(smmsRoomInfoErrorMessage)) {
			if (json.length() > 500) {
				json = json.substring(0, 500);
				// 保存数据到远程日志表中
				this.del.addRpcLog("1001", idc_id, json, success, success);
			} else {
				// 保存数据到远程日志表中
				this.del.addRpcLog("1001", idc_id, json, success, success);
			}
			return success;
		}
		// 保存数据到远程日志表中
		this.del.addRpcLog("1001", idc_id, json, errorMessage, errorMessage);
		return errorMessage;
	}

	/**
	 * 根据传入的json串，然后解释，并保存备案新到网站备案信息数据库中
	 * @param json
	 * @return 返回处理结果的JSON格式字符串
	 */
	@SuppressWarnings("unchecked")
	public String rpc1002(String json) {
		Map map = JsonUtil.jsonToDataMap(json);
		Map maps = (Map) map.get("data");
		List<Map> list = (List) maps.get("rows");
		int i = 0;
		int sum = 0;
		String webCaseErrorMessage = "";
		String noNull = "RID,ACCESS_ID,ACCESS_TENANT,SERVER_PLACE,SPONSER_CERT_TYPE,SPONSER_CERT_NUM,SPONSER_PROVINCE,SPONSER_ADDRESS,SPONSER_CERT_ADDRESS,MANAGER,MANAGER_CERT_TYPE,MANAGER_CERT_NUM,MANAGER_TEL_NUM,MANAGER_MOBILE,MANAGER_EMAIL,SPONSER_CASE_NUM,SPONSER_CASE_STATE,WEBSITE_NAME,WEBSITE_URL,WEBSITE_MANAGER,WEBSITE_MANAGER_MOBILE,WEBSITE_MANAGER_EMAIL,WEBSITE_CASE_NAME,WEBSITE_CASE_STATE,DOMAIN_NAME,WEBSITE_MANAGER_CERT_NUM,CREATTIME,MODIFIEDTIME,RECORDSTATE";
		String FieldLength = "RID:40,ACCESS_ID:10,ACCESS_TENANT:50,SERVER_PLACE:255,SPONSER:50,SPONSER_CERT_TYPE:2,SPONSER_CERT_NUM:30,SPONSER_PROVINCE:30,SPONSER_ADDRESS:255,SPONSER_CERT_ADDRESS:100,MANAGER:50,MANAGER_CERT_TYPE:2,MANAGER_CERT_NUM:30,MANAGER_TEL_NUM:100,MANAGER_MOBILE:30,MANAGER_EMAIL:200,SPONSER_CASE_NUM:30,SPONSER_CASE_STATE:1,WEBSITE_NAME:50,WEBSITE_URL:255,WEBSITE_MANAGER:50,WEBSITE_MANAGER_TEL_NUM:100,WEBSITE_MANAGER_MOBILE:100,WEBSITE_MANAGER_EMAIL:200,WEBSITE_CASE_NAME:30,WEBSITE_CASE_STATE:1,DOMAIN_NAME:100,WEBSITE_MANAGER_CERT_NUM:30,CREATTIME:20,MODIFIEDTIME:20,RECORDSTATE:1";
		Map dataMap = new HashMap();
		for (Map jsonMap : list) {
			String ridStr = FMPContex.getNewUUID();
			dataMap.put("RID", ridStr);
			dataMap.put("SPONSER", jsonMap.get("sponsor"));
			dataMap.put("ACCESS_ID", jsonMap.get("access_id"));
			dataMap.put("ACCESS_TENANT", jsonMap.get("access_tenant"));
			dataMap.put("SERVER_PLACE", jsonMap.get("server_place"));
			dataMap.put("SPONSER_CERT_TYPE", jsonMap.get("sponsor_cert_type"));
			dataMap.put("SPONSER_CERT_NUM", jsonMap.get("sponsor_cert_num"));
			dataMap.put("SPONSER_PROVINCE", jsonMap.get("sponsor_province"));
			dataMap.put("SPONSER_ADDRESS", jsonMap.get("sponsor_address"));
			dataMap.put("SPONSER_CERT_ADDRESS", jsonMap.get("sponsor_cert_address"));
			dataMap.put("MANAGER", jsonMap.get("manager"));
			dataMap.put("MANAGER_CERT_TYPE", jsonMap.get("manager_cert_type"));
			dataMap.put("MANAGER_CERT_NUM", jsonMap.get("manager_cert_num"));
			dataMap.put("MANAGER_TEL_NUM", jsonMap.get("manager_tel_num"));
			dataMap.put("MANAGER_MOBILE", jsonMap.get("manager_mobile"));
			dataMap.put("MANAGER_EMAIL", jsonMap.get("manager_email"));
			dataMap.put("SPONSER_CASE_NUM", jsonMap.get("sponsor_case_num"));
			dataMap.put("SPONSER_CASE_STATE", jsonMap.get("sponsor_case_state"));
			dataMap.put("WEBSITE_NAME", jsonMap.get("website_name"));
			dataMap.put("WEBSITE_URL", jsonMap.get("website_url"));
			dataMap.put("WEBSITE_MANAGER", jsonMap.get("website_manager"));
			dataMap.put("WEBSITE_MANAGER_TEL_NUM", jsonMap.get("website_manager_tel_num"));
			dataMap.put("WEBSITE_MANAGER_MOBILE", jsonMap.get("website_manager_mobile"));
			dataMap.put("WEBSITE_MANAGER_EMAIL", jsonMap.get("website_manager_email"));
			dataMap.put("WEBSITE_CASE_NAME", jsonMap.get("website_case_num"));
			dataMap.put("WEBSITE_CASE_STATE", jsonMap.get("website_case_state"));
			dataMap.put("DOMAIN_NAME", jsonMap.get("domain_name"));
			dataMap.put("WEBSITE_MANAGER_CERT_NUM", jsonMap.get("website_manager_cert_num"));
			dataMap.put("CREATTIME", FMPContex.getCurrentTime());
			dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
			dataMap.put("RECORDSTATE", "0");

			String s = (String) jsonMap.get("access_ip");
			if ((s != null) && (!"".equals(s))) {
				String[] kv = s.split(",");
				for (int j = 0; j < kv.length; j++) {
					Map ipMap = new HashMap();
					ipMap.put("RID", FMPContex.getNewUUID());
					ipMap.put("ACCESS_IP", kv[j]);
					ipMap.put("CASE_RID", ridStr);
					ipMap.put("CREATTIME", FMPContex.getCurrentTime());
					ipMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
					ipMap.put("RECORDSTATE", "0");

					this.sqlSession.insert("SmmsWebCaseIp.insertSave", ipMap);
				}
			}
			// 字段校验
			String fieldCheck = checkValidMap(dataMap, noNull, FieldLength);
			webCaseErrorMessage = webCaseErrorMessage + fieldCheck;
			// 返回空表示数据校验成功
			if (fieldCheck.equals("")) {
				// 返回turn表示数据库有AccessId
				if (checkAccessId(dataMap).booleanValue()) {
					this.sqlSession.update("WebCase.judgeUrlUpdate", dataMap);
					i = this.sqlSession.insert("WebCase.insertSave", dataMap);
					sum += i;
					dataMap.clear();
				} else {
					webCaseErrorMessage = webCaseErrorMessage + "接入商识别码不匹配";
				}
			}
		}
		String success = "{\"return_code\":\"000\",\"return_msg\":\"处理成功,insert:" + sum + "条数据\"}";
		String fail = "{ \"return_code\":\"999\",\"return_msg\":\"处理失败," + webCaseErrorMessage + "\"}";
		String idc_id = "";
		if ("".equals(webCaseErrorMessage)) {
			idc_id = this.del.getIdcId(json);
			FMPLog.debug(idc_id + "--------------------");
			// 保存数据到远程日志表中
			if (json.length() > 500) {
				json = json.substring(0, 500);
				this.del.addRpcLog("1002", idc_id, json, success, success);
			} else {
				this.del.addRpcLog("1002", idc_id, json, success, success);
			}
			return success;
		}
		idc_id = this.del.getIdcId(json);
		// 保存数据到远程日志表中
		this.del.addRpcLog("1002", idc_id, json, fail, fail);
		return fail;
	}

	/**
	 * 3.1. 整改信息推送接口（接口编号：2101）
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rpc2101(String tokenid, String serviecUrl, String json) {
		// 远程日志调用结果
		String rpc_result = "";
		WsRpciProxy wrp = new WsRpciProxy();
		wrp.setEndpoint(serviecUrl);
		String rtStr = "";
		try {
			rtStr = wrp.getWsRpci().wscall(tokenid, "2101", json, null);
			rpc_result = rtStr;
		} catch (RemoteException e) {
			e.printStackTrace();
			rpc_result = "无法连接对应的服务器";
		} finally {
			// 获取idcid
			String idc_id = del.getIdcId(json);
			// 保存数据到远程日志表中
			del.addRpcLog(SMMSConstant.ZGTS_ITF_NUM, idc_id, json, rtStr, rpc_result);
		}

		return rtStr;
	}

	/**
	 * 一键关停接口
	 * @param json 传入参数json
	 * @return 返回处理结果
	 */
	public String rpc2102(String tokenid, String serviecUrl, String json) {
		// 远程日志调用结果
		String rpc_result = "";
		WsRpciProxy wrp = new WsRpciProxy();
		wrp.setEndpoint(serviecUrl);
		String rtStr = "";
		try {
			rtStr = wrp.getWsRpci().wscall(tokenid, "2102", json, null);
			rpc_result = rtStr;
		} catch (RemoteException e) {
			e.printStackTrace();
			rpc_result = "无法连接对应的服务器";
		} finally {
			String idc_id = del.getIdcId(json);
			del.addRpcLog(SMMSConstant.QZGT_ITF_NUM, idc_id, json, rtStr, rpc_result);
		}
		return rtStr;
	}

	/**
	 * 1101
	 * 已整改/关停结果反馈接口
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rpc1101(String json) {
		Map map = JsonUtil.jsonToDataMap(json);
		Map maps = (Map) map.get("data");
		List<Map> list = (List) maps.get("rows");
		String FieldLength = "RID:40,RECTIFY_MEASURE:1,RECTIFY_STATE:3,FEEDBACK_TIME:20,FEEDBACK_TIMESTAMP:20";
		String noNull = "RID,RECTIFY_MEASURE,RECTIFY_STATE";
		Map dataMap = new HashMap();
		int i = 0;
		int sum = 0;
		String pendingEventErrorMessage = "";
		for (Map jsonMap : list) {
			if ("1".equals(String.valueOf(jsonMap.get("rectify_result")))) {
				dataMap.put("RECTIFY_STATE", "888");
			} else if ("9".equals(String.valueOf(jsonMap.get("rectify_result")))) {
				dataMap.put("RECTIFY_STATE", "999");
			}
			dataMap.put("RID", String.valueOf(jsonMap.get("event_id")));
			dataMap.put("RECTIFY_MEASURE", String.valueOf(jsonMap.get("rectify_measure")));
			dataMap.put("FEEDBACK_TIME", FMPContex.getCurrentTime());
			dataMap.put("FEEDBACK_TIMESTAMP", Long.valueOf(System.currentTimeMillis()));
			dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());

			// 字段校验
			String fieldCheck = checkValidMap(dataMap, noNull, FieldLength);
			pendingEventErrorMessage = pendingEventErrorMessage + fieldCheck;
			// 返回空表示数据校验成功
			if (fieldCheck.equals("")) {
				Map detailMap = new HashMap();
				detailMap = (Map) this.sqlSession.selectOne("SmmsEventMain.selectDetailRid", dataMap);
				if (("1".equals(detailMap.get("IS_FORCE_CLOSE"))) && (detailMap.get("IS_FORCE_CLOSE") != null)
						&& (!"".equals(detailMap.get("IS_FORCE_CLOSE")))) {
					this.sqlSession.update("SmmsEventMain.stateUpdateForClose", dataMap);
					dataMap.put("IS_FORCE_CLOSE", "1");
				} else {
					dataMap.put("IS_FORCE_CLOSE", "2");
					this.sqlSession.update("SmmsEventMain.stateUpdate", dataMap);
				}
				i = this.sqlSession.update("SmmsEnforceHis.stateUpdate", dataMap);

				dataMap.putAll(detailMap);
				this.sqlSession.update("SmmsPendingEvent.stateUpdate", dataMap);
				sum += i;
				dataMap.clear();
			}
		}
		String success = "{\"return_code\":\"000\",\"return_msg\": \"处理成功,update：" + sum + "条数据\"}";
		String fail = "{\"return_code\":\"999\",\"return_msg\":\"处理失败" + pendingEventErrorMessage + "\"}";
		String idc_id = "";
		if ("".equals(pendingEventErrorMessage)) {
			// 获取idcid
			idc_id = this.del.getIdcId(json);
			// 保存数据到远程日志表中
			this.del.addRpcLog("1101", idc_id, json, success, success);
			return success;
		}
		idc_id = this.del.getIdcId(json);
		// 保存数据到远程日志表中
		this.del.addRpcLog("1101", idc_id, json, fail, fail);
		return fail;
	}

	/***
	 * 已关停网站申请恢复接口（接口编号：1102）
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rpc1102(String json) {
		Map map = JsonUtil.jsonToDataMap(json);
		Map maps = (Map) map.get("data");
		List<Map> list = (List) maps.get("rows");
		int i = 0;
		int sum = 0;
		String pendingEventErrorMessage = "";
		String FieldLength = "EVENT_RID:40,ACCESS_ID:10,RECTIFY_MEASURE:1,RECTIFY_RESULT:1,RESTORE_REASON:500";
		String noNull = "EVENT_RID,ACCESS_ID,RECTIFY_MEASURE,RECTIFY_RESULT,RESTORE_REASON";
		Map dataMap = new HashMap();
		for (Map jsonMap : list) {
			dataMap.put("RID", FMPContex.getNewUUID());
			dataMap.put("EVENT_RID", jsonMap.get("event_id"));
			dataMap.put("ACCESS_ID", jsonMap.get("access_id"));
			dataMap.put("RECTIFY_MEASURE", jsonMap.get("rectify_measure"));
			dataMap.put("RECTIFY_RESULT", jsonMap.get("rectify_result"));
			dataMap.put("RESTORE_REASON", jsonMap.get("restore_reason"));
			dataMap.put("APP_USERID", null);
			dataMap.put("APP_TIME", FMPContex.getCurrentTime());
			dataMap.put("APP_OPINION", null);
			dataMap.put("APP_RESULT", "0");
			dataMap.put("ISSUE_STATE", "0");
			dataMap.put("CREATTIME", FMPContex.getCurrentTime());
			dataMap.put("RECORDSTATE", "0");
			// 字段校验
			String fieldCheck = checkValidMap(dataMap, noNull, FieldLength);

			pendingEventErrorMessage = pendingEventErrorMessage + fieldCheck;
			// 返回空表示数据校验成功
			if (fieldCheck.equals("")) {
				// 用重复是修改记录状态
				this.sqlSession.update("SmmsWebOpenApp.eventUpdate", dataMap);
				i = this.sqlSession.insert("SmmsWebOpenApp.insertSave", dataMap);
				sum += i;
				dataMap.clear();
			}
		}
		String idc_id = "";
		String success = "{\"return_code\":\"000\",\"return_msg\":\"处理成功,insert:" + sum + "条数据\"}";
		String fail = "{\"return_code\":\"999\",\"return_msg\":\"处理失败" + pendingEventErrorMessage + "\"}";
		if ("".equals(pendingEventErrorMessage)) {
			// 获取idcid
			idc_id = this.del.getIdcId(json);
			// 保存数据到远程日志表中
			this.del.addRpcLog("1102", idc_id, json, success, success);
			return success;
		}
		// 获取idcid
		idc_id = this.del.getIdcId(json);
		// 保存数据到远程日志表中
		this.del.addRpcLog("1102", idc_id, json, fail, fail);
		return fail;
	}

	/**
	 * 4.2.网站恢复申请结果通知接口（接口编号：2103）
	 * 可以对网站进行恢复处理。
	 * @param json
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String rpc2103(String tokenid, String serviecUrl, String json) {
		// 远程日志调用结果
		String rpc_result = "";
		WsRpciProxy wrp = new WsRpciProxy();
		wrp.setEndpoint(serviecUrl);
		String rtStr = "";
		try {
			rtStr = wrp.getWsRpci().wscall(tokenid, "2103", json, null);
			rpc_result = rtStr;
		} catch (RemoteException e) {
			e.printStackTrace();
			rpc_result = "无法连接对应的服务器";
		} finally {
			// 获取idcId
			String idc_id = del.getIdcId(json);
			// 保存数据到远程日志表中
			del.addRpcLog(SMMSConstant.WZHF_ITF_NUM, idc_id, json, rtStr, rpc_result);
		}
		return rtStr;
	}

	public String rpc2104(String tokenid, String serviecUrl, String json) {
		// 远程日志调用结果
		String rpc_result = "";
		WsRpciProxy wrp = new WsRpciProxy();
		wrp.setEndpoint(serviecUrl);
		String rtStr = "";
		String webkey = "bbcKey";
		try {
			rtStr = wrp.getWsRpci().wscall(tokenid, "2104", json, null);
			rpc_result = rtStr;
		} catch (RemoteException e) {
			e.printStackTrace();
		} finally {
			del.addRpcLog("2104", webkey, json, rtStr, rpc_result);
		}
		return rtStr;
	}

	public String rpc2105(String tokenid, String serviecUrl, String json) {
		// 远程日志调用结果
		String rpc_result = "";
		WsRpciProxy wrp = new WsRpciProxy();
		wrp.setEndpoint(serviecUrl);
		String rtStr = "";
		String bbc = "bbc";
		try {
			rtStr = wrp.getWsRpci().wscall(tokenid, "2105", json, null);
			rpc_result = rtStr;
		} catch (RemoteException e) {
			e.printStackTrace();
		} finally {
			del.addRpcLog("2105", bbc, json, rtStr, rpc_result);
		}
		return rtStr;
	}

	/**
	 * 检查Map的合法性
	 * @param map
	 * @param notNullKey 不能为空的参数名，逗号分隔例如： "a,b,c,d,e,f"
	 * @param maxLengthKey 长度控制的参数名和对应长度，逗号和冒号分隔例如："a:4,b:5,c:6,d:12"
	 * @return
	 */
	public static String checkValidMap(Map<String, Object> map, String notNullKey, String maxLengthKey) {
		String retStr = "";
		// 循环遍历，做非空判断
		for (String key : notNullKey.split(",")) {
			if ((key != null) && (!"".equals(key)) && ((map.get(key) == null) || (map.get(key).equals("")))) {
				retStr = retStr + "参数[" + key + "]不能为空！,";
			}
		}

		// 循环遍历，做长度限制判断
		for (String keyValue : maxLengthKey.split(",")) {
			String[] kv = keyValue.split(":");

			String k = kv[0];
			String v = kv[1];
			if ((k != null) && (!"".equals(k)) && (map.get(k) != null)
					&& (String.valueOf(map.get(k)).length() > Integer.valueOf(v).intValue())) {
				retStr = retStr + "参数[" + k + "]长度不能超过" + v + "字节！" + ",";
			}
		}
		return retStr;
	}

	// 校验接入商识别码
	public Boolean checkAccessId(Map accessIdMap) {
		Map map = new HashMap();
		map.put("IDC_ID", accessIdMap.get("ACCESS_ID"));
		Map mapcoum = (Map) this.sqlSession.selectOne("IdcInfo.checkIdcId", map);
		if (mapcoum != null) {
			return Boolean.valueOf(true);
		}
		return Boolean.valueOf(false);
	}

	/**
	 * 6.1数据统计查询接口（接口编号：1201）
	 * 对 1：统计网站备案数据总数
	 * 2：统计IDC机房数据总数
	 * 进行统计查询
	 * @param json
	 * @return String
	 */
	public String rpc1201(String json) {
		Map jsonMap = JsonUtil.jsonToDataMap(json);
		Map maps = (Map) jsonMap.get("data");
		Map queryMap = new HashMap();// 查询map
		Map backMap = new HashMap();// 返回map
		int queryType = ((Integer) maps.get("query_type")).intValue();
		String idcId = "";// idc运营商编号
		idcId = (String) maps.get("access_id");
		queryMap.put("ACCESS_ID", idcId);
		String queryResult = "";
		// 判断query_type是统计哪张表的数据
		if (queryType == 1) {
			queryResult = (String) this.sqlSession.selectOne("WebCase.CountByAccessId", queryMap);
			if ((!"".equals(queryResult)) && (!"0".equals(queryResult))) {
				backMap.put("query_result", queryResult);
				backMap.put("return_code", "000");
				backMap.put("return_msg", "处理成功");
			} else {
				backMap.put("return_code", "999");
				backMap.put("return_msg", "处理失败");
			}
		} else if (queryType == 2) {
			queryResult = (String) this.sqlSession.selectOne("SmmsRoomInfo.CountByAccessId", queryMap);
			if ((!"".equals(queryResult)) && (!"0".equals(queryResult))) {
				backMap.put("query_result", queryResult);
				backMap.put("return_code", "000");
				backMap.put("return_msg", "处理成功");
			} else {
				backMap.put("return_code", "999");
				backMap.put("return_msg", "处理失败");
			}
		} else {
			backMap.put("return_code", "999");
			backMap.put("return_msg", "处理失败");
		}
		String result = JsonUtil.dataMapToJson(backMap);
		this.del.addRpcLog("1201", idcId, json, result, result);
		return result;
	}

	/**
	 * IDC网站备案信息查询接口
	 * @param json
	 * @return String
	 */
	public String rpc1202(String json) {
		// 将json字符串转换为map集合
		Map jsonMap = JsonUtil.jsonToDataMap(json);
		Map dataMap = (Map) jsonMap.get("data");
		Map webCaseMap = new HashMap();// 查询map
		Map resultMap = new HashMap();// 返回map
		String idcId = "";// idc运营商标号

		idcId = (String) dataMap.get("access_id");
		webCaseMap.put("ACCESS_ID", idcId);
		webCaseMap.put("WEBSITE_CASE_NAME", dataMap.get("website_case_num"));
		String access_ip = "";
		// 根据idc运营商编号和网站备案号查出网站备案信息的数据
		List<Map> webCaseList = this.sqlSession.selectList("WebCase.selectWebCaseInfo", webCaseMap);
		int total = 0;
		if ((webCaseList != null) && (webCaseList.size() > 0)) {
			total = webCaseList.size();
			for (Map map : webCaseList) {
				// 根据备案信息的rid查询备案ip信息表的数据
				List<Map> webCaseIpList = this.sqlSession.selectList("SmmsWebCaseIp.findAccessIpByCaseRid", map);
				for (Map webCaseIpMap : webCaseIpList) {
					if ((webCaseIpList != null) && (webCaseIpList.size() > 0)) {
						String ip = (String) webCaseIpMap.get("access_ip") + ",";
						access_ip = access_ip + ip;
						access_ip = access_ip.substring(0, access_ip.length() - 1);
					}
				}
				map.put("access_ip", access_ip);
				map.remove("CASE_RID");
			}
			Map maps = new HashMap();
			maps.put("rows", webCaseList);
			maps.put("total", Integer.valueOf(total));

			resultMap.put("data", maps);
			resultMap.put("return_code", "000");
			resultMap.put("return_msg", "处理成功");
		} else {
			resultMap.put("return_code", "999");
			resultMap.put("return_msg", "处理失败");
		}
		String result = JsonUtil.dataMapToJson(resultMap);

		this.del.addRpcLog("1202", idcId, json, result, result);
		return result;
	}

	/**
	 * IDC机房信息信息查询接口
	 * @param json
	 * @return String
	 */
	public String rpc1203(String json) {
		Map jsonMap = JsonUtil.jsonToDataMap(json);
		Map dataMap = (Map) jsonMap.get("data");
		Map selectMap = new HashMap();// 查询map
		Map resultMap = new HashMap();// 返回map
		String idcId = "";// idc运营商标号
		idcId = (String) dataMap.get("access_id");
		selectMap.put("ACCESS_ID", idcId);
		selectMap.put("ROOM_IDX", dataMap.get("machine_room_idx"));
		// 根据idc运营商编号和机房序号查出机房信息的数据
		List<Map> roomList = this.sqlSession.selectList("SmmsRoomInfo.findRoomInfo", selectMap);
		int total = 0;// 记录总数
		if ((roomList != null) && (roomList.size() > 0)) {
			Map roomMap = (Map) roomList.get(0);
			// 根据idc运营商编号和机房序号查出机房网段信息的数据
			List<Map> roomIprangeList = this.sqlSession.selectList("SmmsRoomIprange.findRoomIprangeInfo", selectMap);
			if ((roomIprangeList != null) && (roomIprangeList.size() > 0)) {
				total = roomIprangeList.size();
			}
			Map maps = new HashMap();
			maps.put("rows", roomIprangeList);
			maps.put("total", Integer.valueOf(total));
			maps.putAll(roomMap);
			resultMap.put("data", maps);
			resultMap.put("return_code", "000");
			resultMap.put("return_msg", "处理成功");
		} else {
			resultMap.put("return_code", "999");
			resultMap.put("return_msg", "处理失败");
		}
		String result = JsonUtil.dataMapToJson(resultMap);
		this.del.addRpcLog("1203", idcId, json, result, result);
		return result;
	}

	public String rpc1211(String json) {
		Map selectMap = new HashMap();
		Map resultMap = new HashMap();
		String returnMessage = "";
		String tokenId = getTokenId();

		Map selectIdcId = new HashMap();
		selectIdcId.put("RID", tokenId);
		Map map = new HashMap();
		map = (Map) this.sqlSession.selectOne("IdcInfo.selectIdcId", selectIdcId);
		if ((map != null) && (map.size() > 0)) {
			Map jsonMap = JsonUtil.jsonToDataMap(json);
			Map dataMap = (Map) jsonMap.get("data");

			String noNull = "time_rang,event_type1,limit,offset";
			String FieldLength = "time_rang:29";
			String fieldCheck = checkValidMap(dataMap, noNull, FieldLength);
			returnMessage = returnMessage + fieldCheck;
			if ("".equals(returnMessage)) {
				String timeRang = "";
				timeRang = (String) dataMap.get("time_rang");
				String eventType1 = "";
				eventType1 = (String) dataMap.get("event_type1");

				List list = new ArrayList();
				int afTotal = 0;
				if ((dataMap.containsKey("ip")) && (dataMap.get("ip") != null) && (!"".equals(dataMap.get("ip")))) {
					selectMap.put("IP", dataMap.get("ip"));
				}
				selectMap.put("ACCESS_ID", map.get("IDC_ID"));
				selectMap.put("LIMIT", dataMap.get("limit"));
				selectMap.put("OFFSET", dataMap.get("offset"));
				if ((timeRang.length() == 29) && ("1".equals(eventType1))) {
					String startTime = timeRang.substring(0, 4) + "-" + timeRang.substring(4, 6) + "-" + timeRang.substring(6, 8) + " "
							+ timeRang.substring(8, 10) + ":" + timeRang.substring(10, 12) + ":" + timeRang.substring(12, 14);
					String endTime = timeRang.substring(15, 19) + "-" + timeRang.substring(19, 21) + "-" + timeRang.substring(21, 23) + " "
							+ timeRang.substring(23, 25) + ":" + timeRang.substring(25, 27) + ":" + timeRang.substring(27, timeRang.length());
					FMPLog.debug("开始时间：" + startTime);
					FMPLog.debug("结束时间：" + endTime);
					selectMap.put("STARTTIME", startTime);
					selectMap.put("ENDTIME", endTime);
					list = this.sqlSession.selectList("SmmsEventMain.selectEventMailAfInWeb", selectMap);
					afTotal = ((Integer) this.sqlSession.selectOne("SmmsEventMain.selectEventMailAfInWebTotal", selectMap)).intValue();
					FMPLog.debug("查询af备案数据库后的大小：" + list.size());
					FMPLog.debug("查询af备案数据库后的总记录数：" + afTotal);
				} else if ((timeRang.length() == 29) && ("2".equals(eventType1))) {
					String startTime = timeRang.substring(0, 4) + "-" + timeRang.substring(4, 6) + "-" + timeRang.substring(6, 8) + " "
							+ timeRang.substring(8, 10) + ":" + timeRang.substring(10, 12) + ":" + timeRang.substring(12, 14);
					String endTime = timeRang.substring(15, 19) + "-" + timeRang.substring(19, 21) + "-" + timeRang.substring(21, 23) + " "
							+ timeRang.substring(23, 25) + ":" + timeRang.substring(25, 27) + ":" + timeRang.substring(27, timeRang.length());
					FMPLog.debug("开始时间：" + startTime);
					FMPLog.debug("结束时间：" + endTime);
					selectMap.put("STARTTIME", startTime);
					selectMap.put("ENDTIME", endTime);
					list = this.sqlSession.selectList("SmmsEventMain.selectEventMailAfNotInWeb", selectMap);
					afTotal = ((Integer) this.sqlSession.selectOne("SmmsEventMain.selectEventMailAfNotInWebTotal", selectMap)).intValue();
					FMPLog.debug("查询af未备案数据库后的大小：" + list.size());
					FMPLog.debug("查询af未备案数据库后的总记录数：" + afTotal);
				}
				Map maps = new HashMap();
				maps.put("rows", list);
				maps.put("total", Integer.valueOf(afTotal));
				resultMap.put("data", maps);
				resultMap.put("return_code", "000");
				resultMap.put("return_msg", "处理成功");
			} else {
				resultMap.put("return_code", "999");
				returnMessage = returnMessage.substring(0, returnMessage.length() - 1);
				resultMap.put("return_msg", returnMessage);
			}
		} else {
			returnMessage = "授权码不正确";
			resultMap.put("return_code", "999");
			resultMap.put("return_msg", returnMessage);
		}
		String result = JsonUtil.dataMapToJson(resultMap);
		return result;
	}

	public String rpc1212(String json) {
		Map selectMap = new HashMap();
		Map resultMap = new HashMap();
		String returnMessage = "";
		String tokenId = getTokenId();

		Map selectIdcId = new HashMap();
		selectIdcId.put("RID", tokenId);
		Map map = new HashMap();
		map = (Map) this.sqlSession.selectOne("IdcInfo.selectIdcId", selectIdcId);
		if ((map != null) && (map.size() > 0)) {
			Map jsonMap = JsonUtil.jsonToDataMap(json);
			Map dataMap = (Map) jsonMap.get("data");

			String noNull = "time_rang,event_type1,limit,offset";
			String FieldLength = "time_rang:29";
			String fieldCheck = checkValidMap(dataMap, noNull, FieldLength);
			returnMessage = returnMessage + fieldCheck;
			if ("".equals(returnMessage)) {
				String timeRang = "";
				timeRang = (String) dataMap.get("time_rang");
				String eventType1 = "";
				eventType1 = (String) dataMap.get("event_type1");

				List list = new ArrayList();
				int acTotal = 0;
				if ((dataMap.get("ip") != null) && (!"".equals(dataMap.get("ip")))) {
					selectMap.put("IP", dataMap.get("ip"));
				}
				selectMap.put("ACCESS_ID", map.get("IDC_ID"));
				selectMap.put("LIMIT", dataMap.get("limit"));
				selectMap.put("OFFSET", dataMap.get("offset"));
				if ((timeRang.length() == 29) && ("1".equals(eventType1))) {
					String startTime = timeRang.substring(0, 4) + "-" + timeRang.substring(4, 6) + "-" + timeRang.substring(6, 8) + " "
							+ timeRang.substring(8, 10) + ":" + timeRang.substring(10, 12) + ":" + timeRang.substring(12, 14);
					String endTime = timeRang.substring(15, 19) + "-" + timeRang.substring(19, 21) + "-" + timeRang.substring(21, 23) + " "
							+ timeRang.substring(23, 25) + ":" + timeRang.substring(25, 27) + ":" + timeRang.substring(27, timeRang.length());
					FMPLog.debug("开始时间：" + startTime);
					FMPLog.debug("结束时间：" + endTime);
					selectMap.put("STARTTIME", startTime);
					selectMap.put("ENDTIME", endTime);
					list = this.sqlSession.selectList("SmmsEventMain.selectEventMailAcInWeb", selectMap);
					acTotal = ((Integer) this.sqlSession.selectOne("SmmsEventMain.selectEventMailAcInWebTotal", selectMap)).intValue();
					FMPLog.debug("查询ac备案数据库后的大小：" + list.size());
					FMPLog.debug("查询ac备案数据库后的总记录数：" + acTotal);
				} else if ((timeRang.length() == 29) && ("2".equals(eventType1))) {
					String startTime = timeRang.substring(0, 4) + "-" + timeRang.substring(4, 6) + "-" + timeRang.substring(6, 8) + " "
							+ timeRang.substring(8, 10) + ":" + timeRang.substring(10, 12) + ":" + timeRang.substring(12, 14);
					String endTime = timeRang.substring(15, 19) + "-" + timeRang.substring(19, 21) + "-" + timeRang.substring(21, 23) + " "
							+ timeRang.substring(23, 25) + ":" + timeRang.substring(25, 27) + ":" + timeRang.substring(27, timeRang.length());
					FMPLog.debug("开始时间：" + startTime);
					FMPLog.debug("结束时间：" + endTime);
					selectMap.put("STARTTIME", startTime);
					selectMap.put("ENDTIME", endTime);
					list = this.sqlSession.selectList("SmmsEventMain.selectEventMailAcNotInWeb", selectMap);
					acTotal = ((Integer) this.sqlSession.selectOne("SmmsEventMain.selectEventMailAcNotInWebTotal", selectMap)).intValue();
					FMPLog.debug("查询ac未备案数据库后的大小：" + list.size());
					FMPLog.debug("查询ac未被备案数据库后的总记录数：" + acTotal);
				} else {
					returnMessage = "处理失败，参数数据有误";
				}
				Map maps = new HashMap();
				maps.put("rows", list);
				maps.put("total", Integer.valueOf(acTotal));
				resultMap.put("data", maps);
				resultMap.put("return_code", "000");
				resultMap.put("return_msg", "处理成功");
			} else {
				resultMap.put("return_code", "999");
				returnMessage = returnMessage.substring(0, returnMessage.length() - 1);
				resultMap.put("return_msg", returnMessage);
			}
		} else {
			returnMessage = "授权码不正确";
			resultMap.put("return_code", "999");
			resultMap.put("return_msg", returnMessage);
		}
		String result = JsonUtil.dataMapToJson(resultMap);
		return result;
	}

	public String rpc1301(String json) {
		Map jsonMap = JsonUtil.jsonToDataMap(json);
		Map resultMap = new HashMap();
		List list = new ArrayList();

		Map idcEventMap = new HashMap();
		idcEventMap = (Map) this.sqlSession.selectOne("SmmsIdcEvent.selectList");

		Map idcCloseOpenMap = new HashMap();
		idcCloseOpenMap = (Map) this.sqlSession.selectOne("SptIdcCloseOpen.selectList");

		resultMap.put("CITY_EVENT", idcEventMap);
		resultMap.put("CITY_CLOSE_OPEN", idcCloseOpenMap);

		SmmsCitySumDel smmsCitySumYbDel = new SmmsCitySumDel(this.contex);
		Map citySumMap = smmsCitySumYbDel.getSptCitySumList();
		resultMap.put("CITY_SUM", citySumMap);

		CityRecityDel cityRecityDel = new CityRecityDel(this.contex);
		Map cityRecityMap = cityRecityDel.selectListRequest();
		resultMap.put("CITY_RECTIFY", cityRecityMap);
		resultMap.put("return_code", "000");
		resultMap.put("return_msg", "处理成功");
		String result = JsonUtil.dataMapToJson(resultMap);
		System.out.println("result:" + result);
		return result;
	}

	public static void main(String[] s) {
		/*
		 * Map map = new HashMap();
		 * map.put("a", "123123");
		 * map.put("b", "234");
		 * map.put("c", "123423423");
		 * map.put("d", "123223");
		 * map.put("e", "");
		 * 
		 * String ss = RpcBusDel.checkValidMap(map, "a,b,c,d,e,f", "a:4,b:5,c:6,d:12");
		 * 
		 * System.err.println(ss);
		 */
	}
}
