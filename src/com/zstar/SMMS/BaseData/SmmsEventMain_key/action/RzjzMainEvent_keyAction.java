package com.zstar.SMMS.BaseData.SmmsEventMain_key.action;

import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class RzjzMainEvent_keyAction extends FrameAction {

	public RzjzMainEvent_keyAction() {
	}

	public String bizExecute() throws Exception {
		HttpServletResponse response;
		File outFile;
		OutputStream out;
		FileInputStream fis;
		response = (HttpServletResponse) contex.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
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
			FMPLog.printErr("fileName == null || fileName.length() <= 0");
		}
		outFile = new File(fileName);
		if (!outFile.exists()) {
			FMPLog.printErr((new StringBuilder("未找到文件：")).append(fileName).toString());
			setMsg((new StringBuilder("未找到文件：")).append(fileName).toString());
			return "empty";
		}
		response.setContentLength((int) outFile.length());
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Content-type", "text/html;charset=UTF-8");
		out = null;
		fis = null;
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
			if (fis != null) {
				fis.close();
				fis = null;
			}
			if (out != null) {
				out.close();
				out = null;
			}
		}
		return null;
	}
}