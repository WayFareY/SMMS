package com.zstar.SMMS.BaseData.SmmsRoomInfo.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;

public class DownloadRoomInfoAction extends FrameAction {

	/* Error */
	public String bizExecute() throws java.lang.Exception {
		HttpServletResponse response = (HttpServletResponse) contex.get("com.opensymphony.xwork2.dispatcher.HttpServletResponse");
		String path = getClass().getResource("/").getPath();
		String jiequ = path.substring(0, path.length() - 16);
		String fileName = (new StringBuilder(String.valueOf(jiequ))).append("fileDownLoad/room_module.xls").toString();
		String downFileName = "room_module.xls";
		System.out.println(fileName);
		System.out.println((new StringBuilder("666:")).append(jiequ).toString());
		if ("".equals(fileName) || fileName.length() <= 0) {

		}
		File downfile = new File(fileName);
		if (!downfile.exists()) {
			FMPLog.printErr((new StringBuilder("未找到文件：")).append(fileName).toString());
			setMsg((new StringBuilder("未找到文件：")).append(fileName).toString());
			return "msg";
		}
		String suffixName = "";
		if (fileName.indexOf(".") > 0) {
			suffixName = fileName.substring(fileName.lastIndexOf("."), fileName.length());
			FMPLog.printLog((new StringBuilder("后缀名：")).append(suffixName).toString());
		}
		response.setContentLength((int) downfile.length());
		response.setContentType("application/octet-stream");
		FMPLog.printLog("ContentType====application/octet-stream");
		response.setHeader("Content-Disposition",
				(new StringBuilder("attachment;filename=")).append(URLEncoder.encode(downFileName, "UTF-8")).toString());
		OutputStream out = null;
		FileInputStream fis = null;
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
		return "none";
	}
}
