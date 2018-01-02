package com.zstar.SMMS.BaseData.SmmsPendingEvent_Wba.action;

import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

public class RzjzPendingEvent_WbaAction extends FrameAction {

	public RzjzPendingEvent_WbaAction() {
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
		Map map = (Map) sqlSession.selectOne("SmmsPendingEvent_Wba.selectContentPathByRid", ridMap);
		String fileName = (String) map.get("SNAPSHOP");
		FMPLog.printLog((new StringBuilder("test:")).append(fileName).toString());
		if (fileName == null || fileName.length() <= 0) {
			FMPLog.printErr("fileName == null || fileName.length() <= 0");
			return "empty";
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
