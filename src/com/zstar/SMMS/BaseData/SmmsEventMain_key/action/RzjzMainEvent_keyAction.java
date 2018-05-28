package com.zstar.SMMS.BaseData.SmmsEventMain_key.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;

public class RzjzMainEvent_keyAction extends FrameAction {

	public String bizExecute() throws java.lang.Exception {
		HttpServletResponse response = (HttpServletResponse) contex.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
		String rid = (String) getWebData("RID");
		Map ridMap = new HashMap();
		ridMap.put("RID", rid);
		Map map = (Map) sqlSession.selectOne("SmmsEventMain_key.selectContentPathByRid", ridMap);
		String filePath = FMPContex.getSystemProperty("AC_LOG_KEY_PATH");
		String idcFilePath = (new StringBuilder(String.valueOf(String.valueOf(map.get("ACCESS_ID"))))).append("/")
				.append(String.valueOf(map.get("ROOM_IDX"))).toString();
		String fileName = (new StringBuilder(String.valueOf(filePath))).append(idcFilePath).append("/").append("htm").append("/")
				.append((String) map.get("SNAPSHOP")).toString();
		FMPLog.printLog((new StringBuilder("test:")).append(fileName).toString());
		if (fileName == null || fileName.length() <= 0) {

		}
		File outFile = new File(fileName);
		if (!outFile.exists()) {
			FMPLog.printErr((new StringBuilder("未找到文件：")).append(fileName).toString());
			setMsg((new StringBuilder("未找到文件：")).append(fileName).toString());
			return "empty";
		}
		response.setContentLength((int) outFile.length());
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		OutputStream out = null;
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(outFile);
			out = response.getOutputStream();
			byte b[] = new byte[1024];
			for (int i = 0; (i = fis.read(b)) > 0;) {
				out.write(b, 0, i);
			}
			fis.close();
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fis != null) {
					fis.close();
					fis = null;
				}
				if (out != null) {
					out.close();
					out = null;
				}
			} catch (Exception exception1) {
			}
		}
		return null;
	}
}
