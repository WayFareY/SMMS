package com.zstar.SMMS.jobs;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.SMMS.constant.*;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.job.BaseJob;
import java.io.File;
import java.util.*;
import org.quartz.JobExecutionException;

public class Load_Domain_Fulx_LogFileJob extends BaseJob {

	public Load_Domain_Fulx_LogFileJob() {
	}

	public void jobExcute(String s) throws JobExecutionException {
		Iterator iterator;
		ReadAcLogDel acDel = new ReadAcLogDel(getContex());
		List list = acDel.selectIdcIdAndRoomList();
		iterator = list.iterator();
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
			backUpPath = (new StringBuilder(String.valueOf(backPath))).append("/key/").append(date).append("/").append(idcFilePath).toString();
			FileUtil.makeDirs(filePath);
			FileUtil.makeDirs(backUpPath);
			List fileNameList = FileUtil.outFileName(filePath);
			String acFilePath = (new StringBuilder(String.valueOf(FMPContex.getSystemProperty("AC_LOG_PATH")))).append(idcFilePath).append("/")
					.toString();
			FileUtil.makeDirs(acFilePath);
			List acFileNameList = FileUtil.outFileName(acFilePath);
			String acLog = (new StringBuilder(String.valueOf(idcId))).append("_").append(roomIdx).toString();
			if (fileNameList.size() > 1 || acFileNameList.size() > 0) {
				SMMSConstant.AC_TimeMap.put(acLog, FMPContex.getCurrentTime());
			} else if (!SMMSConstant.AC_TimeMap.containsKey(acLog)) {
				SMMSConstant.AC_TimeMap.put(acLog, FMPContex.getCurrentTime());
			}
			iterator1 = fileNameList.iterator();
			while (iterator1.hasNext()) {
				String fileName;
				String mdfPath;
				DBSqlSession dbsqlSession;
				fileName = (String) iterator1.next();
				if (!fileName.startsWith("domain_flux_") || !fileName.endsWith(".mdf.ok")) {
					continue; /* Loop/switch isn't completed */
				}
				String mdfFileName = fileName.replace(".mdf.ok", ".mdf");
				mdfPath = (new StringBuilder(String.valueOf(filePath))).append(mdfFileName).toString();
				File file = new File(mdfPath);
				if (!file.exists()) {
					continue; /* Loop/switch isn't completed */
				}
				dbsqlSession = null;
				try {
					dbsqlSession = new DBSqlSession(FMPContex.DBConnection.openSqlSession(null));
					ActionContext contex = new ActionContext(new HashMap());
					contex.put("sqlSession", dbsqlSession.getSqlSession());
					String tableRow = "DOMAIN,VISI_COUNT,TOTAL_FLUX,UP_FLUX,DOWN_FLUX,PERCENT";
					String sqlId = "SmmsDomainFulx.insertSave";
					ReadAcLogDel del = new ReadAcLogDel(contex);
					int sum = del.insertAcLogin(filePath, mdfPath, tableRow, sqlId, false);
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
