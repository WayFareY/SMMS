package com.zstar.SMMS.afLog.SmmsAfLog.delegate;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.SMMS.constant.FileOperateUtil;
import com.zstar.SMMS.webservice.delegate.RpcBusDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import com.zstar.fmp.log.FMPLog;
import com.zstar.fmp.utils.JsonUtil;

import net.sf.json.JSONNull;

public class ReadAFLogDel extends BaseDelegate {

	public ReadAFLogDel(ActionContext contex) {
		super(contex);
	}

	public long insertAFLog(long filePos, String fileName) {
		String filePath = FMPContex.getSystemProperty("AF_LOG_FILE");
		String fileFormat = FMPContex.getSystemProperty("AF_LOG_FORMAT");
		long currPos = filePos;
		PendingEventDel del = new PendingEventDel(contex);
		EventMainDel mainDel = new EventMainDel(contex);
		RpcBusDel rpcBusdel = new RpcBusDel(contex);
		String message = "";
		Map eventMap = new HashMap();
		Map map = new HashMap();
		Map resultMap = new HashMap();
		Map closeMap = new HashMap();
		closeMap.put("DBTYPE", FMPContex.DBType);
		int result = 0;
		ReadAcLogDel acDel = new ReadAcLogDel(getContex());
		InputStreamReader isr = null;
		BufferedReader in = null;
		try {
			isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			in = new BufferedReader(isr);
			String str = null;
			in.skip(filePos);
			while ((str = in.readLine()) != null) {
				try {
					currPos += str.length();
					if (str.startsWith("[inavigator:][ [")) {
						String s = str.substring(16, str.length() - 2);
						Map jsonMap = new HashMap();
						jsonMap = JsonUtil.jsonToDataMap(s);
						for (Iterator it = jsonMap.keySet().iterator(); it.hasNext();) {
							String key = (String) it.next();
							Object obj = jsonMap.get(key);
							if (obj == null || (obj instanceof JSONNull)) {
								jsonMap.put(key, "");
							} else {
								jsonMap.put(key, String.valueOf(obj));
							}
						}

						FMPLog.printLog((new StringBuilder("jsonMap============")).append(jsonMap).toString());
						String type = (String) jsonMap.get("TYPE");
						if ("遭受入侵".equals(type)) {
							jsonMap.put("TYPE", "01");
						} else if ("黑客控制".equals(type)) {
							jsonMap.put("TYPE", "02");
						} else if ("黑产牟利".equals(type)) {
							jsonMap.put("TYPE", "03");
						} else if ("内部攻击".equals(type)) {
							jsonMap.put("TYPE", "04");
						} else {
							jsonMap.put("TYPE", "99");
						}
						String level = (String) jsonMap.get("LEVEL");
						if ("蓝色预警".equals(level)) {
							jsonMap.put("LEVEL", "1");
						} else if ("橙色预警".equals(level)) {
							jsonMap.put("LEVEL", "2");
						} else if ("红色预警".equals(level)) {
							jsonMap.put("LEVEL", "3");
						} else {
							jsonMap.put("LEVEL", "0");
						}
						String rid = FMPContex.getNewUUID();
						jsonMap.put("RID", rid);
						jsonMap.put("CREATTIME", FMPContex.getCurrentTime());
						jsonMap.put("RECORDSTATE", "0");
						String src_ip = (String) jsonMap.get("SRC_IP");
						jsonMap.put("SRC_IP", src_ip);
						String dst_ip = (String) jsonMap.get("DST_IP");
						jsonMap.put("DST_IP", dst_ip);
						int i = sqlSession.insert("SmmsAfLog.insertSave", jsonMap);
						String isClose = mainDel.getIsClose(jsonMap, null);
						FMPLog.debug((new StringBuilder("是否强制关停：")).append(isClose).toString());
						String ip = "";
						Map insertMap = new HashMap();
						Boolean dstIpKey = Boolean.valueOf(jsonMap.containsKey("DST_IP"));
						if (dstIpKey.booleanValue() && !"".equals(jsonMap.get("DST_IP")) && !"0.0.0.0".equals(jsonMap.get("DST_IP"))) {
							ip = (String) jsonMap.get("DST_IP");
							jsonMap.put("IP", ip);
							insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(jsonMap);
							FMPLog.debug((new StringBuilder("使用目的地ip匹配：")).append(insertMap.size()).toString());
						}
						if (insertMap != null && insertMap.size() == 0) {
							ip = (String) jsonMap.get("SRC_IP");
							jsonMap.put("IP", ip);
							insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(jsonMap);
							FMPLog.debug((new StringBuilder("使用源IP地址匹配：")).append(insertMap.size()).toString());
						}
						if (!insertMap.containsKey("MAPPING_MODE")) {
							insertMap.put("MAPPING_MODE", "0");
						}
						if ("1".equals(isClose)) {
							insertMap.put("FORCE_CLOSE_DESC", "威胁等级高，主机已失陷");
						} else {
							insertMap.put("FORCE_CLOSE_DESC", "");
						}
						insertMap.put("IP", jsonMap.get("DST_IP"));
						insertMap.put("ATTACK_IP", jsonMap.get("SRC_IP"));
						insertMap.put("SNAPSHOP", jsonMap.get("EVENT_EVIDENCE"));
						insertMap.put("OCCUR_TIME", jsonMap.get("START_TIME"));
						type = (String) jsonMap.get("TYPE");
						insertMap.put("THREAT_TYPE1", type);
						String subType = (String) jsonMap.get("SUB_TYPE");
						insertMap.put("THREAT_TYPE2", subType);
						insertMap.put("THREAT_NAME", jsonMap.get("NAME"));
						insertMap.put("THREAT_LEVEL", jsonMap.get("RELIABILITY"));
						insertMap.put("PRIORITY", jsonMap.get("PRIORITY"));
						insertMap.put("CLIENTNAME", "admin");
						insertMap.put("EVENT_SOURCE", "1");
						insertMap.put("CLIENTNAME", "admin");
						insertMap.put("IS_FORCE_CLOSE", isClose);
						Map getSuggestMap = mainDel.getSuggestMap(isClose);
						insertMap.putAll(getSuggestMap);
						insertMap.put("RECTIFY_STATE", "000");
						String accessId = "";
						accessId = String.valueOf(jsonMap.get("BRANCH"));
						if (accessId.indexOf("/") != -1) {
							String kv[] = accessId.split("/");
							insertMap.put("ACCESS_ID", kv[0]);
							insertMap.put("ROOM_IDX", kv[1]);
							List list = sqlSession.selectList("SmmsRoomInfo.selectRoomNameAndRoomIdx", insertMap);
							if (list.size() > 0 && list != null) {
								insertMap.put("ROOM_NAME", ((Map) list.get(0)).get("ROOM_NAME"));
							}
						}
						insertMap.put("LOG_TABLENAME", "SMMS_AF_LOG");
						insertMap.put("LOG_RID", rid);
						Long total = null;
						if (i == 1) {
							String mainRid = "";
							eventMap = mainDel.checkEventMain(insertMap, total, resultMap, map);
							mainRid = (String) eventMap.get("MAIN_RID");
							String rectifyState = (String) eventMap.get("RECTIFY_STATE");
							if (!"".equals(mainRid)) {
								insertMap.put("MAIN_RID", mainRid);
								insertMap.put("RID", eventMap.get("DETAIL_RID"));
								result = del.insertSmmsPendingEvent(insertMap);
								acDel.insertRecordMap(accessId, "060", result);
							}
							String automatic = FMPContex.getSystemParameter("BIZ1");
							String issued = FMPContex.getSystemParameter("BIZ2");
							closeMap.put("EVENT_RID", mainRid);
							closeMap.put("DETAIL_RID", eventMap.get("DETAIL_RID"));
							mainRid = (new StringBuilder("'")).append(mainRid).append("'").toString();
							closeMap.put("RIDCONDITION", (new StringBuilder("and ssp.RID IN (")).append(mainRid).append(")").toString());
							Long enforceCount = (Long) eventMap.get("ENFORCE_COUNT");
							enforceCount = Long.valueOf(enforceCount.longValue() + 1L);
							closeMap.put("ENFORCE_COUNT", enforceCount);
							Map checkMap = mainDel.checkForece(eventMap, insertMap);
							FMPLog.debug((new StringBuilder("比较数据库后数据大小：")).append(checkMap.size()).toString());
							insertMap.put("SEND_TIME", "");
							insertMap.put("SEND_TIMESTAMP", "");
							insertMap.put("FEEDBACK_TIME", "");
							insertMap.put("FEEDBACK_TIMESTAMP", "");
							insertMap.put("FORCE_CLOSE_DESC", "");
							if (checkMap.containsKey("IS_FORCE_CLOSE")) {
								String checkIsClose = (String) checkMap.get("IS_FORCE_CLOSE");
								if ("1".equals(checkIsClose)) {
									insertMap.put("RID", eventMap.get("MAIN_RID"));
									insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
									if ("899".equals(rectifyState) || "020".equals(rectifyState) || "888".equals(rectifyState)
											|| "999".equals(rectifyState) || "900".equals(rectifyState)) {
										insertMap.put("RECTIFY_STATE", "000");
									} else if (!"1".equals(eventMap.get("IS_FORCE_CLOSE")) && "010".equals(rectifyState)) {
										insertMap.put("RECTIFY_STATE", "000");
									} else {
										insertMap.put("RECTIFY_STATE", rectifyState);
									}
									rectifyState = (String) insertMap.get("RECTIFY_STATE");
									if ((!"2".equals(eventMap.get("FINAL_RECTIFY_SUGGEST")) || !"000".equals(eventMap.get("RECTIFY_STATE")))
											&& (!"1".equals(eventMap.get("IS_FORCE_CLOSE")) || !"010".equals(rectifyState))) {
										int j = sqlSession.update("SmmsEventMain.updateSave", insertMap);
										FMPLog.debug((new StringBuilder("af关停 待处理日志是否更新成功：")).append(j).toString());
									}
								} else {
									insertMap.put("RID", eventMap.get("MAIN_RID"));
									if ("888".equals(rectifyState) || "999".equals(rectifyState) || "900".equals(rectifyState)) {
										insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
										insertMap.put("RECTIFY_STATE", "000");
									} else {
										insertMap.put("RECTIFY_STATE", rectifyState);
									}
									rectifyState = (String) insertMap.get("RECTIFY_STATE");
									if ((!"2".equals(eventMap.get("IS_FORCE_CLOSE")) || !"010".equals(rectifyState))
											&& !"020".equals(eventMap.get("RECTIFY_STATE"))
											&& (!"0".equals(eventMap.get("FINAL_RECTIFY_SUGGEST")) || !"000".equals(rectifyState))) {
										int j = sqlSession.update("SmmsEventMain.updateSave", insertMap);
										FMPLog.debug((new StringBuilder("af整改 待处理日志是否更新成功：")).append(j).toString());
									}
								}
							} else {
								if ("888".equals(rectifyState) || "999".equals(rectifyState) || "900".equals(rectifyState)) {
									insertMap.put("RECTIFY_STATE", "000");
									insertMap.put("RID", eventMap.get("MAIN_RID"));
									insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
									int j = sqlSession.update("SmmsEventMain.updateSave", insertMap);
									FMPLog.debug((new StringBuilder("af关停 待处理日志是否更新成功：")).append(j).toString());
								} else {
									insertMap.put("RECTIFY_STATE", rectifyState);
								}
								rectifyState = (String) insertMap.get("RECTIFY_STATE");
							}
							boolean flag = mainDel.checkTime(eventMap);
							FMPLog.debug((new StringBuilder("是否是当天:")).append(flag).toString());
							if ("000".equals(rectifyState)) {
								closeMap.put("ENFORCE_USER", "自动执法");
								closeMap.put("ENFORCE_TIME", FMPContex.getCurrentTime());
								if ("1".equals(automatic) && "2".equals(eventMap.get("FINAL_RECTIFY_SUGGEST"))) {
									Long closeCount = (Long) eventMap.get("CLOSE_COUNT");
									closeCount = Long.valueOf(closeCount.longValue() + 1L);
									closeMap.put("CLOSE_COUNT", closeCount);
									message = mainDel.ForceClose(closeMap);
									FMPLog.debug((new StringBuilder("处理状态为000AF关停返回结果:")).append(message).toString());
								}
								if ("1".equals(issued) && "1".equals(eventMap.get("FINAL_RECTIFY_SUGGEST")))
									if (insertMap.containsKey("WEB_CASE_RID")) {
										message = mainDel.rectification(closeMap);
										FMPLog.debug((new StringBuilder("处理状态为000AF下发返回结果:")).append(message).toString());
									} else {
										FMPLog.debug("处理状态为000AF没对接系统下发返回结果");
										mainDel.updateRectifyState(closeMap);
										mainDel.insertEventHis(closeMap);
									}
							}
						}
					}
				} catch (Exception e) {
					FMPLog.printErr("处理日志异常。。。");
					String err = "错误数据:\n";
					err = (new StringBuilder(String.valueOf(err))).append(str).toString();
					FileOperateUtil.createFiles(filePath, err, fileFormat);
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception exception1) {
			}
			try {
				isr.close();
			} catch (Exception exception2) {
			}
		}
		return currPos;
	}
}
