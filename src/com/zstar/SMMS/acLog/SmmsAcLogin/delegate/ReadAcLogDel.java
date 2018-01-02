package com.zstar.SMMS.acLog.SmmsAcLogin.delegate;

import com.opensymphony.xwork2.ActionContext;
import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.constant.SMMSConstant;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.base.delegate.BaseDelegate;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadAcLogDel extends BaseDelegate {

	public ReadAcLogDel(ActionContext contex) {
		super(contex);
	}

	public List<Map> selectIdcIdAndRoomList() {
		return this.sqlSession.selectList("IdcInfo.idcAndRoomList");
	}

	public int insertAcLogin(String filePath, String fileName, String tableRow, String sqlId, boolean flag) {
		String idcIdAndRoomIdx = filePath.substring(filePath.length() - 15, filePath.length() - 1);
		Map roomIdcMap = selectRoomInfo(filePath);
		List roomIdcList = this.sqlSession.selectList("SmmsRoomInfo.selectRoomNameAndRoomIdx", roomIdcMap);
		Map roomMap = (Map) roomIdcList.get(0);
		String[] rowKey = tableRow.split(",");

		int sum = 0;
		PendingEventDel del = new PendingEventDel(this.contex);
		EventMainDel mainDel = new EventMainDel(this.contex);
		Map keysMap = new HashMap();
		keysMap.put("DBTYPE", FMPContex.DBType);
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
			BufferedReader in = new BufferedReader(isr);
			String str = null;
			while ((str = in.readLine()) != null) {
				String[] kv = str.split("\\|\\*\\|", 1000);

				Map map = new HashMap();
				if (kv.length == rowKey.length) {
					for (int j = 0; j < kv.length; j++) {
						map.put(rowKey[j], kv[j]);
					}
					String rid = FMPContex.getNewUUID();
					map.put("RID", rid);
					map.put("CREATTIME", FMPContex.getCurrentTime());
					map.put("RECORDSTATE", "0");

					map.put("GROUPNAME", idcIdAndRoomIdx);
					Boolean state = Boolean.valueOf(map.containsKey("CASE_STATE"));

					Boolean servcrc = Boolean.valueOf(map.containsKey("SERV_CRC"));

					Boolean appcrc = Boolean.valueOf(map.containsKey("APP_CRC"));
					if (!state.booleanValue()) {
						map.put("CASE_STATE", SMMSConstant.CASE_STATE);
					}
					if (sqlId.startsWith("SmmsDomainFulx.")) {
						Map resultMap = new HashMap();
						Map timeMap = new HashMap();
						resultMap = selectRoomInfo(filePath);
						map.put("IDC_ID", resultMap.get("ACCESS_ID"));
						map.put("ROOM_IDX", resultMap.get("ROOM_IDX"));
						timeMap = getRecordMap();
						map.putAll(timeMap);
					}
					int i = this.sqlSession.insert(sqlId, map);
					sum += i;
					if (flag) {
						Boolean urlContains = Boolean.valueOf(map.containsKey("URL"));
						Boolean desIpKey = Boolean.valueOf(map.containsKey("DES_IP"));
						Boolean ipKey = Boolean.valueOf(map.containsKey("IP"));
						Boolean keyWords = Boolean.valueOf(map.containsKey("KEYWORDS"));
						String url = "";
						String ip = "";
						if (urlContains.booleanValue()) {
							url = String.valueOf(map.get("URL"));
							map.put("URL", url.toUpperCase());
						}
						if (desIpKey.booleanValue()) {
							ip = (String) map.get("DES_IP");
							map.put("IP", ip);
						}
						Map insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(map);

						insertMap.put("RECTIFY_STATE", SMMSConstant.RECTIFY_STATE);
						if (!insertMap.containsKey("MAPPING_MODE")) {
							insertMap.put("MAPPING_MODE", SMMSConstant.MAPPING_MODE_ZERO);
						}
						if (urlContains.booleanValue()) {
							insertMap.put("URL", url.toLowerCase());
						}
						if (ipKey.booleanValue()) {
							ip = (String) map.get("IP");
							insertMap.put("IP", ip);
						}
						if (servcrc.booleanValue()) {
							insertMap.put("EVENT_TYPE2", map.get("SERV_CRC"));
						}
						if (appcrc.booleanValue()) {
							insertMap.put("EVENT_TYPE3", map.get("APP_CRC"));
						}
						if (sqlId.startsWith("SmmsAcAppVpn.")) {
							insertMap.put("LOG_TABLENAME", "SMMS_AC_APP_VPN");
							if (String.valueOf(map.get("VPN_PROXY").toString()) == "0") {
								insertMap.put("EVENT_TYPE1", SMMSConstant.VPN);
							} else {
								insertMap.put("EVENT_TYPE1", SMMSConstant.VPNO);
							}
						}
						if (sqlId.startsWith("SmmsAcBbsKey.")) {
							insertMap.put("LOG_TABLENAME", SMMSConstant.SMMS_AC_BBS_KEY);
							insertMap.put("EVENT_TYPE1", SMMSConstant.BBS);
						}
						if (sqlId.startsWith("SmmsAcMailKey.")) {
							insertMap.put("LOG_TABLENAME", SMMSConstant.SMMS_AC_MAIL_KEY);
							insertMap.put("EVENT_TYPE1", SMMSConstant.MAIL);
						}
						if (sqlId.startsWith("SmmsAcUrlKey.")) {
							insertMap.put("EVENT_TYPE1", SMMSConstant.URL);
							insertMap.put("LOG_TABLENAME", SMMSConstant.SMMS_AC_URL_KEY);
						}
						if (sqlId.startsWith("SmmsAcProxy.")) {
							insertMap.put("EVENT_TYPE1", SMMSConstant.PROXY);
							insertMap.put("LOG_TABLENAME", SMMSConstant.SMMS_AC_PROXY);
						}
						insertMap.put("LOG_RID", map.get("RID"));

						insertMap.put("CLIENTNAME", SMMSConstant.CLIENTNAME);

						insertMap.put("EVENT_SOURCE", SMMSConstant.EVENT_SOURCE_AC);
						if (keyWords.booleanValue()) {
							String keys = String.valueOf(map.get("KEYWORDS"));
							keysMap.put("KEYS", keys);
							String keyWordType = "";

							Map mapLevel = (Map) this.sqlSession.selectOne("SmmsKeyword.selectKeywordLevel", keysMap);
							String keyWordLevel = (String) mapLevel.get("KEYWORD_LEVEL");

							List<Map> typeList = this.sqlSession.selectList("SmmsKeyword.selectKeywordType", keysMap);
							for (Map keyMap : typeList) {
								keyWordType = keyWordType + (String) keyMap.get("KEYWORD_TYPE") + ",";
							}
							if (keyWordType.endsWith(",")) {
								keyWordType = keyWordType.substring(0, keyWordType.length() - 1);
							}
							insertMap.put("THREAT_LEVEL", keyWordLevel);
							insertMap.put("THREAT_TYPE4", keyWordType);
							if ((keyWordType.indexOf("010") != -1) || (keyWordType.indexOf("020") != -1) || (keyWordType.indexOf("030") != -1)) {
								insertMap.put("IS_FORCE_CLOSE", SMMSConstant.IS_FORCE_CLOSE);
							} else {
								insertMap.put("IS_FORCE_CLOSE", SMMSConstant.IS_FORCE_CLOSE_TWO);
							}
							insertMap.put("KEYWORDS", map.get("KEYWORDS"));
						} else {
							insertMap.put("IS_FORCE_CLOSE", "2");
						}
						insertMap.put("ACCESS_ID", roomIdcMap.get("ACCESS_ID"));
						insertMap.put("ROOM_NAME", roomMap.get("ROOM_NAME"));
						insertMap.put("ROOM_IDX", roomIdcMap.get("ROOM_IDX"));

						String mainRid = "";
						if ((sqlId.startsWith("SmmsAcAppVpn.")) || (sqlId.startsWith("SmmsAcBbsKey.")) || (sqlId.startsWith("SmmsAcMailKey."))
								|| (sqlId.startsWith("SmmsAcUrlKey."))) {
							insertMap.put("SNAPSHOP", map.get("EVENT_EVIDENCE"));
						}
						if (i != 0) {
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
						}
					}
				}
			}
			isr.close();
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sum;
	}

	public static Map selectRoomInfo(String filePath) {
		String idcId = filePath.substring(filePath.length() - 15, filePath.length() - 5);

		String roomIdx = filePath.substring(filePath.length() - 4, filePath.length() - 1);

		String idcIdAndRoomIdx = filePath.substring(filePath.length() - 15, filePath.length() - 1);

		Map roomIdcMap = new HashMap();
		roomIdcMap.put("ACCESS_ID", idcId);
		roomIdcMap.put("ROOM_IDX", roomIdx);

		return roomIdcMap;
	}

	public static Map getRecordMap() {
		Map recordMap = new HashMap();

		Calendar cal = Calendar.getInstance();

		int year = cal.get(1);

		int month = cal.get(2) + 1;

		String date = FMPContex.getCurrentDate();

		recordMap.put("STAT_YEAR", Integer.valueOf(year));
		recordMap.put("STAT_MONTH", Integer.valueOf(month));
		recordMap.put("STAT_DATE", date);
		return recordMap;
	}

	public void insertRecordMap(String filePath, String logFlag, int sum) {
		Map roomIdcMap = selectRoomInfo(filePath);

		Map recorderMap = getRecordMap();
		recorderMap.put("LOG_FLAG", logFlag);
		recorderMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
		recorderMap.putAll(roomIdcMap);

		List<Map> flagList = new ArrayList();
		flagList = this.sqlSession.selectList("SmmsRptRecord.selectExist", recorderMap);
		if ((flagList != null) && (flagList.size() > 0)) {
			Map flagMap = (Map) flagList.get(0);
			Integer logCount = Integer.valueOf(sum + Integer.valueOf(String.valueOf(flagMap.get("LOG_COUNT"))).intValue());
			recorderMap.put("LOG_COUNT", logCount);
			this.sqlSession.update("SmmsRptRecord.updatLogCount", recorderMap);
		} else {
			String rid = FMPContex.getNewUUID();
			recorderMap.put("RID", rid);
			recorderMap.put("LOG_COUNT", Integer.valueOf(sum));
			recorderMap.put("CREATTIME", FMPContex.getCurrentTime());
			this.sqlSession.insert("SmmsRptRecord.insertSave", recorderMap);
		}
	}
}
