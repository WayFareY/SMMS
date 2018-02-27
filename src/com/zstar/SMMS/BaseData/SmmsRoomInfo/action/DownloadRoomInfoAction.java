package com.zstar.SMMS.BaseData.SmmsRoomInfo.action;

import com.opensymphony.xwork2.ActionContext;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;

public class DownloadRoomInfoAction extends FrameAction {

	public DownloadRoomInfoAction() {
	}

	public String bizExecute() throws Exception {
		HttpServletResponse response;
		File downfile;
		OutputStream out;
		FileInputStream fis;
		response = (HttpServletResponse) contex.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
		String path = getClass().getResource("/").getPath();
		String jiequ = path.substring(0, path.length() - 16);
		String fileName = (new StringBuilder(String.valueOf(jiequ))).append("fileDownLoad/IDC机房信息模板.xls").toString();
		String downFileName = "IDC机房信息模板.xls";
		System.out.println(fileName);
		System.out.println((new StringBuilder("666:")).append(jiequ).toString());
		if ("".equals(fileName) || fileName.length() <= 0) {
			FMPLog.printErr("\"\".equals(fileName) || fileName.length() <= 0");
		}
		downfile = new File(fileName);
		if (!downfile.exists()) {
			FMPLog.printErr((new StringBuilder("未找到文件：")).append(fileName).toString());
			setMsg((new StringBuilder("未找到文件：")).append(fileName).toString());
			return "msg";
		}
		String suffixName = "";
		if (fileName.indexOf(".") > 0) {
			suffixName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			FMPLog.printLog((new StringBuilder("后缀名")).append(suffixName).toString());
		}
		response.setContentLength((int) downfile.length());
		response.setContentType("application/octet-stream");
		FMPLog.printLog("ContentType====application/octet-stream");
		response.setHeader("Content-Disposition",
				(new StringBuilder("attachment;filename=")).append(URLEncoder.encode(downFileName, "UTF-8")).toString());
		out = null;
		fis = null;
		try {
			fis = new FileInputStream(downfile);
			out = response.getOutputStream();
			byte b[] = new byte[0x19000];
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
		return "none";
	}
}
