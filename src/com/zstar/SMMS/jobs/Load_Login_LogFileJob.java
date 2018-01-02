package com.zstar.SMMS.jobs;

import com.opensymphony.xwork2.ActionContext;
import com.strutsframe.db.DBSqlSession;
import com.zstar.SMMS.acLog.SmmsAcLogin.delegate.ReadAcLogDel;
import com.zstar.SMMS.constant.FileOperateUtil;
import com.zstar.SMMS.constant.FileUtil;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.job.BaseJob;
import java.io.File;
import java.util.*;
import org.quartz.JobExecutionException;

public class Load_Login_LogFileJob extends BaseJob {

	public Load_Login_LogFileJob() {
	}

	public void jobExcute(String arg0) throws JobExecutionException {
		Iterator iterator;
		ReadAcLogDel acDel = new ReadAcLogDel(getContex());
		List list = acDel.selectIdcIdAndRoomList();
		iterator = list.iterator();
		while (iterator.hasNext()) {
			String filePath;
			String backUpPath;
			Iterator iterator1;
			Map map = (Map) iterator.next();
			String idcFilePath = (new StringBuilder(String.valueOf(String.valueOf(map.get("IDC_ID"))))).append("/")
					.append(String.valueOf(map.get("ROOM_IDX"))).toString();
			filePath = (new StringBuilder(String.valueOf(FMPContex.getSystemProperty("AC_LOG_PATH")))).append(idcFilePath).append("/")
					.toString();
			String backPath = FMPContex.getSystemProperty("AC_LOG_BACKUP_PATH");
			String date = FMPContex.getCurrentDate();
			backUpPath = (new StringBuilder(String.valueOf(backPath))).append("/action/").append(date).append("/").append(idcFilePath)
					.toString();
			FileUtil.makeDirs(filePath);
			FileUtil.makeDirs(backUpPath);
			List fileNameList = FileUtil.outFileName(filePath);
			iterator1 = fileNameList.iterator();
			while (iterator1.hasNext()) {
				String fileName;
				String mdfPath;
				DBSqlSession dbsqlSession;
				fileName = (String) iterator1.next();
				if (!fileName.startsWith("login") || !fileName.endsWith(".mdf.ok")) {
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
					String tableRow = "USERNAME,GROUPNAME,IP,SERV_CRC,APP_CRC,LOG_TIME,MAC";
					String sqlId = "SmmsAcLogin.insertSave";
					ReadAcLogDel del = new ReadAcLogDel(contex);
					dbsqlSession.delete("SmmsAcLogin.deleteAll");
					int sum = del.insertAcLogin(filePath, mdfPath, tableRow, sqlId, false);
					String logFlag = "020";
					del.insertRecordMap(filePath, logFlag, sum);
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
