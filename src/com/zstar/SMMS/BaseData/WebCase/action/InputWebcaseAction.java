package com.zstar.SMMS.BaseData.WebCase.action;

import com.zstar.SMMS.constant.ExcelUtil;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.exceptions.PersistenceException;

public class InputWebcaseAction extends FrameAction {

	private String idcId;

	private File webcaseFile;

	private String webcaseFileContentType;

	private String webcaseFileFileName;

	public String getWebcaseFileContentType() {
		return this.webcaseFileContentType;
	}

	public void setWebcaseFileContentType(String webcaseFileContentType) {
		this.webcaseFileContentType = webcaseFileContentType;
	}

	public String getWebcaseFileFileName() {
		return this.webcaseFileFileName;
	}

	public void setWebcaseFileFileName(String webcaseFileFileName) {
		this.webcaseFileFileName = webcaseFileFileName;
	}

	public File getWebcaseFile() {
		return this.webcaseFile;
	}

	public void setWebcaseFile(File webcaseFile) {
		this.webcaseFile = webcaseFile;
	}

	public String getIdcId() {
		return this.idcId;
	}

	public void setIdcId(String idcId) {
		this.idcId = idcId;
	}

	public String bizExecute() throws Exception {
		List<Map> listMap = this.sqlSession.selectList("IdcInfo.viewIdcExist", this.idcId);
		String uploadPath = FMPContex.getSystemProperty("BASE_UPLOAD_PATH") + this.idcId + "/";
		if (this.webcaseFileFileName.endsWith(".xls")) {
			StringBuffer sb = new StringBuffer(this.webcaseFileFileName);
			sb.append('x');
			this.webcaseFileFileName = sb.toString();
		}
		File saveFile = new File(uploadPath, this.webcaseFileFileName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
		FileUtils.copyFile(this.webcaseFile, saveFile);
		if ((listMap != null) && (!listMap.isEmpty())) {
			ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(saveFile);
			if ((!"".equals(result)) && (result != null)) {
				String webCaseErrorMessage = "";
				int sum = 0;
				int successNum = 0;
				String falseCol = "";
				int falseNum = 0;
				Map dataMap = new HashMap();
				Map<String, Integer> map = new HashMap();
				FMPLog.printLog("上传记录数目========" + (result.size() - 2));
				for (int i = 1; i < result.size(); i++) {
					for (int j = 0; j < ((ArrayList) result.get(i)).size(); j++) {
						if ("SPONSER".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER", Integer.valueOf(j));
						}
						if ("SPONSER_CERT_TYPE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_CERT_TYPE", Integer.valueOf(j));
						}
						if ("SPONSER_CERT_NUM".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_CERT_NUM", Integer.valueOf(j));
						}
						if ("SPONSER_PROVINCE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_PROVINCE", Integer.valueOf(j));
						}
						if ("SPONSER_ADDRESS".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_ADDRESS", Integer.valueOf(j));
						}
						if ("SPONSER_CASE_NUM".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_CASE_NUM", Integer.valueOf(j));
						}
						if ("SPONSER_CASE_STATE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_CASE_STATE", Integer.valueOf(j));
						}
						if ("WEBSITE_NAME".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_NAME", Integer.valueOf(j));
						}
						if ("WEBSITE_URL".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_URL", Integer.valueOf(j));
						}
						if ("WEBSITE_CASE_NAME".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_CASE_NAME", Integer.valueOf(j));
						}
						if ("WEBSITE_CASE_STATE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_CASE_STATE", Integer.valueOf(j));
						}
						if ("DOMAIN_NAME".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("DOMAIN_NAME", Integer.valueOf(j));
						}
						if ("SERVER_PLACE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SERVER_PLACE", Integer.valueOf(j));
						}
						if ("SERVER_PLACE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SERVER_PLACE", Integer.valueOf(j));
						}
						if ("SPONSER_CERT_ADDRESS".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("SPONSER_CERT_ADDRESS", Integer.valueOf(j));
						}
						if ("MANAGER".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER", Integer.valueOf(j));
						}
						if ("MANAGER_CERT_TYPE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_CERT_TYPE", Integer.valueOf(j));
						}
						if ("MANAGER_CERT_NUM".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_CERT_NUM", Integer.valueOf(j));
						}
						if ("MANAGER_TEL_NUM".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_TEL_NUM", Integer.valueOf(j));
						}
						if ("MANAGER_MOBILE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_MOBILE", Integer.valueOf(j));
						}
						if ("MANAGER_EMAIL".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_EMAIL", Integer.valueOf(j));
						}
						if ("WEBSITE_MANAGER".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_MANAGER", Integer.valueOf(j));
						}
						if ("WEBSITE_MANAGER_TEL_NUM".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_MANAGER_TEL_NUM", Integer.valueOf(j));
						}
						if ("WEBSITE_MANAGER_MOBILE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_MANAGER_MOBILE", Integer.valueOf(j));
						}
						if ("WEBSITE_MANAGER_EMAIL".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_MANAGER_EMAIL", Integer.valueOf(j));
						}
						if ("WEBSITE_MANAGER_CERT_NUM".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("WEBSITE_MANAGER_CERT_NUM", Integer.valueOf(j));
						}
						if ("IPS".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("IPS", Integer.valueOf(j));
						}
					}
				}
				for (int k = 2; k < result.size(); k++) {
					try {
						String zbdw = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER")).intValue()).toString();
						String zbdwzjlx = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_CERT_TYPE")).intValue()).toString();
						String zbdwzjhm = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_CERT_NUM")).intValue()).toString();
						String ztszs = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_PROVINCE")).intValue()).toString();

						String dz = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_ADDRESS")).intValue()).toString();
						String ztbah = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_CASE_NUM")).intValue()).toString();
						String ztbazt = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_CASE_STATE")).intValue()).toString();
						String wzmc = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_NAME")).intValue()).toString();
						String wzsy = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_URL")).intValue()).toString();

						String wzbah = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_CASE_NAME")).intValue()).toString();

						String wzbazt = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_CASE_STATE")).intValue()).toString();
						String ym = ((ArrayList) result.get(k)).get(((Integer) map.get("DOMAIN_NAME")).intValue()).toString();
						String fwqcfdz = ((ArrayList) result.get(k)).get(((Integer) map.get("SERVER_PLACE")).intValue()).toString();

						String zjzs = ((ArrayList) result.get(k)).get(((Integer) map.get("SPONSER_CERT_ADDRESS")).intValue()).toString();
						String ztfzr = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER")).intValue()).toString();
						String ztfzrzjlx = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_CERT_TYPE")).intValue()).toString();
						String ztfzrzjhm = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_CERT_NUM")).intValue()).toString();
						String ztfzrdhhm = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_TEL_NUM")).intValue()).toString();
						String ztfzrsjhm = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_MOBILE")).intValue()).toString();
						String ztfzrdzyj = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_EMAIL")).intValue()).toString();
						String wzfzr = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_MANAGER")).intValue()).toString();
						String wzfzrdhhm = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_MANAGER_TEL_NUM")).intValue()).toString();
						String wzfzrsjhm = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_MANAGER_MOBILE")).intValue()).toString();
						String wzfzrdzyj = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_MANAGER_EMAIL")).intValue()).toString();
						String wzfzrzjhm = ((ArrayList) result.get(k)).get(((Integer) map.get("WEBSITE_MANAGER_CERT_NUM")).intValue())
								.toString();
						String ip = ((ArrayList) result.get(k)).get(((Integer) map.get("IPS")).intValue()).toString();

						dataMap.put("SPONSER", zbdw);
						dataMap.put("SPONSER_CERT_TYPE", zbdwzjlx);
						dataMap.put("SPONSER_CERT_NUM", zbdwzjhm);
						dataMap.put("SPONSER_PROVINCE", ztszs);
						dataMap.put("SPONSER_ADDRESS", dz);
						dataMap.put("SPONSER_CASE_STATE", ztbazt);
						dataMap.put("SPONSER_CASE_NUM", ztbah);
						dataMap.put("WEBSITE_NAME", wzmc);
						dataMap.put("WEBSITE_URL", wzsy);
						if (wzbah.indexOf("/") != -1) {
							String[] name = wzbah.split("/");
							dataMap.put("WEBSITE_CASE_NAME", name[0]);
						} else {
							dataMap.put("WEBSITE_CASE_NAME", wzbah);
						}
						dataMap.put("WEBSITE_CASE_STATE", wzbazt);
						dataMap.put("DOMAIN_NAME", ym);
						dataMap.put("SERVER_PLACE", fwqcfdz);
						dataMap.put("SPONSER_CERT_ADDRESS", zjzs);

						dataMap.put("MANAGER", ztfzr);
						dataMap.put("MANAGER_CERT_TYPE", ztfzrzjlx);
						dataMap.put("MANAGER_CERT_NUM", ztfzrzjhm);
						dataMap.put("MANAGER_TEL_NUM", ztfzrdhhm);
						dataMap.put("MANAGER_MOBILE", ztfzrsjhm);
						dataMap.put("MANAGER_EMAIL", ztfzrdzyj);
						dataMap.put("WEBSITE_MANAGER", wzfzr);
						dataMap.put("WEBSITE_MANAGER_TEL_NUM", wzfzrdhhm);
						dataMap.put("WEBSITE_MANAGER_MOBILE", wzfzrsjhm);
						dataMap.put("WEBSITE_MANAGER_EMAIL", wzfzrdzyj);
						dataMap.put("WEBSITE_MANAGER_CERT_NUM", wzfzrzjhm);

						String ridStr = FMPContex.getNewUUID();
						dataMap.put("RID", ridStr);
						dataMap.put("ACCESS_ID", this.idcId);
						dataMap.put("CREATTIME", FMPContex.getCurrentTime());
						dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
						dataMap.put("RECORDSTATE", "0");

						this.sqlSession.update("WebCase.judgeUrlUpdate", dataMap);
						successNum = this.sqlSession.insert("WebCase.insertSave", dataMap);
						if ((ip != null) && (!"".equals(ip))) {
							String str = ExcelUtil.ToDBC(ip);
							int comma = str.indexOf(',');
							if (comma == -1) {
								Map ipMap = new HashMap();
								ipMap.put("RID", FMPContex.getNewUUID());
								ipMap.put("CASE_RID", ridStr);
								ipMap.put("ACCESS_IP", ip);
								ipMap.put("CREATTIME", FMPContex.getCurrentTime());
								ipMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
								ipMap.put("RECORDSTATE", "0");

								this.sqlSession.insert("SmmsWebCaseIp.insertSave", ipMap);
							} else {
								String[] kv = str.split(",");
								for (int j = 0; j < kv.length; j++) {
									Map ipMap = new HashMap();
									ipMap.put("RID", FMPContex.getNewUUID());
									ipMap.put("ACCESS_IP", kv[j]);
									ipMap.put("CASE_RID", ridStr);
									ipMap.put("CREATTIME", FMPContex.getCurrentTime());
									ipMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
									ipMap.put("RECORDSTATE", "0");

									this.sqlSession.insert("SmmsWebCaseIp.insertSave", ipMap);
								}
							}
						}
						sum += successNum;

						File dFile = new File(uploadPath);
						ExcelUtil.deleteDir(dFile);
					} catch (PersistenceException e) {
						FMPLog.printLog("第" + (k + 1) + "行数据有误");
						if ("".equals(falseCol)) {
							falseCol = falseCol + (k + 1);
						} else {
							falseCol = falseCol + "," + (k + 1);
						}
						falseNum++;
						e.printStackTrace();
					} catch (Exception e) {
						FMPLog.printLog("第" + (k + 1) + "行数据有误");
						if ("".equals(falseCol)) {
							falseCol = falseCol + (k + 1);
						} else {
							falseCol = falseCol + "," + (k + 1);
						}
						falseNum++;
						e.printStackTrace();
					}
				}
				if (falseNum > 0) {
					setMsg(("上传成功共:" + sum + "条;" + "上传失败共:" + falseNum + "条,其中第" + falseCol + "条失败").trim());
					return "empty";
				}
				setMsg(("上传成功共:" + sum + "条;" + "上传失败共:" + falseNum + "条").trim());
				return "empty";
			}
			setMsg("excel版本错误".trim());
			return "empty";
		}
		setMsg("IDC编号不存在".trim());
		return "empty";
	}
}
