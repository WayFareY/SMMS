package com.zstar.SMMS.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.SMMS.constant.FileOperateUtil;
import com.zstar.SMMS.constant.FileUtil;
import com.zstar.SMMS.constant.SMMSConstant;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.job.BaseJob;

public class Load_Domain_Count_LogFileJob extends BaseJob {

	public void jobExcute(String arg0) throws org.quartz.JobExecutionException {
		ReadAcLogDel acDel = new ReadAcLogDel(getContex());
		List list = acDel.selectIdcIdAndRoomList();
		Iterator iterator = list.iterator();

		while (iterator.hasNext()) {
			String filePath;
			String backUpPath;
			Iterator iterator1;
			Map map = (Map) iterator.next();
			String roomIdx = String.valueOf(map.get("ROOM_IDX"));
			String idcId = String.valueOf(map.get("IDC_ID"));
			String idcFilePath = (new StringBuilder(String.valueOf(idcId))).append("/").append(roomIdx).toString();
			filePath = (new StringBuilder(String.valueOf(FMPContex.getSystemProperty("AC_LOG_KEY_PATH")))).append(idcFilePath).append("/")
					.toString();
			String backPath = FMPContex.getSystemProperty("AC_LOG_BACKUP_PATH");
			String date = FMPContex.getCurrentDate();
			backUpPath = (new StringBuilder(String.valueOf(backPath))).append("/key/").append(date).append("/").append(idcFilePath)
					.toString();
			FileUtil.makeDirs(filePath);
			FileUtil.makeDirs(backUpPath);
			List fileNameList = FileUtil.outFileName(filePath);
			String acFilePath = (new StringBuilder(String.valueOf(FMPContex.getSystemProperty("AC_LOG_PATH")))).append(idcFilePath)
					.append("/").toString();
			FileUtil.makeDirs(acFilePath);
			List acKeyFileNameList = FileUtil.outFileName(acFilePath);
			String acLog = (new StringBuilder(String.valueOf(idcId))).append("_").append(roomIdx).toString();
			if (fileNameList.size() > 0 || acKeyFileNameList.size() > 1) {
				SMMSConstant.AC_TimeMap.put(acLog, FMPContex.getCurrentTime());
			} else if (!SMMSConstant.AC_TimeMap.containsKey(acLog)) {
				SMMSConstant.AC_TimeMap.put(acLog, FMPContex.getCurrentTime());
			}
			iterator1 = fileNameList.iterator();
			while (iterator1.hasNext()) {
				String fileName = (String) iterator1.next();
				if (!fileName.startsWith("domain_count") || !fileName.endsWith(".mdf.ok")) {
					continue; /* Loop/switch isn't completed */
				}
				String mdfFileName = fileName.replace(".mdf.ok", ".mdf");
				String mdfPath = (new StringBuilder(String.valueOf(filePath))).append(mdfFileName).toString();
				File file = new File(mdfPath);
				if (!file.exists()) {
					continue; /* Loop/switch isn't completed */
				}
				DBSqlSession dbsqlSession = null;
				try {
					dbsqlSession = new DBSqlSession(FMPContex.DBConnection.openSqlSession(null));
					Map roomIdcMap = ReadAcLogDel.selectRoomInfo(filePath);
					Map selectMap = new HashMap();
					selectMap.put("DBTYPE", FMPContex.DBType);
					try {
						InputStreamReader isr = new InputStreamReader(new FileInputStream(mdfPath), "UTF-8");
						BufferedReader in = new BufferedReader(isr);
						for (String str = null; (str = in.readLine()) != null;) {
							Map insertMap = new HashMap();
							String rid = FMPContex.getNewUUID();
							insertMap.put("RID", rid);
							insertMap.put("DOMAIN_NAME", str);
							insertMap.put("IDC_ID", roomIdcMap.get("ACCESS_ID"));
							insertMap.put("ROOM_IDX", roomIdcMap.get("ROOM_IDX"));
							insertMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
							selectMap.put("URL", str.toUpperCase());
							List webList = dbsqlSession.selectList("WebCase.viewDoMainNameAndAccessId", selectMap);
							if (webList != null && webList.size() > 0) {
								Map webCaseMap = (Map) webList.get(0);
								insertMap.put("WEB_CASE_RID", webCaseMap.get("RID"));
								insertMap.put("WEB_CASE_STATE", "1");
							} else {
								insertMap.put("WEB_CASE_STATE", "2");
							}
							int i = dbsqlSession.update("SmmsSubjectInfo.updateModifiedtime", insertMap);
							insertMap.put("CREATTIME", FMPContex.getCurrentTime());
							insertMap.put("RECORDSTATE", "0");
							if (i == 0)
								dbsqlSession.insert("SmmsSubjectInfo.insertSave", insertMap);
						}

						isr.close();
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
					FMPContex.DBConnection.commitSession(dbsqlSession.getSqlSession());
					String okPath = (new StringBuilder(String.valueOf(filePath))).append(fileName).toString();
					FileOperateUtil.cutGeneralFile(okPath, backUpPath);
					FileOperateUtil.cutGeneralFile(mdfPath, backUpPath);
				} catch (Exception e) {
					FMPContex.DBConnection.rollBackSession(dbsqlSession.getSqlSession());
					e.printStackTrace();
				} finally {
					FMPContex.DBConnection.closeSession(dbsqlSession.getSqlSession());
				}
			}
		}
	}
}
