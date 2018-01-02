package com.zstar.SMMS.BaseData.SmmsRoomInfo.action;

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

public class InputRoomInfoAction extends FrameAction {

	private String idcId;

	private File roomFile;

	private String roomFileContentType;

	private String roomFileFileName;

	public String getIdcId() {
		return this.idcId;
	}

	public void setIdcId(String idcId) {
		this.idcId = idcId;
	}

	public File getRoomFile() {
		return this.roomFile;
	}

	public void setRoomFile(File roomFile) {
		this.roomFile = roomFile;
	}

	public String getRoomFileContentType() {
		return this.roomFileContentType;
	}

	public void setRoomFileContentType(String roomFileContentType) {
		this.roomFileContentType = roomFileContentType;
	}

	public String getRoomFileFileName() {
		return this.roomFileFileName;
	}

	public void setRoomFileFileName(String roomFileFileName) {
		this.roomFileFileName = roomFileFileName;
	}

	public String bizExecute() throws Exception {
		List<Map> listMap = this.sqlSession.selectList("IdcInfo.viewIdcExist", this.idcId);
		String uploadPath = FMPContex.getSystemProperty("BASE_UPLOAD_PATH") + this.idcId + "/";
		if (this.roomFileFileName.endsWith(".xls")) {
			StringBuffer sb = new StringBuffer(this.roomFileFileName);
			sb.append('x');
			this.roomFileFileName = sb.toString();
		}
		File saveFile = new File(uploadPath, this.roomFileFileName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
		FileUtils.copyFile(this.roomFile, saveFile);
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
						if ("ROOM_NAME".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("ROOM_NAME", Integer.valueOf(j));
						}
						if ("ROOM_IDX".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("ROOM_IDX", Integer.valueOf(j));
						}
						if ("ROOM_ADDRESS".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("ROOM_ADDRESS", Integer.valueOf(j));
						}
						if ("ROOM_PROPERTY".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("ROOM_PROPERTY", Integer.valueOf(j));
						}
						if ("ROOM_MANAGER".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("ROOM_MANAGER", Integer.valueOf(j));
						}
						if ("MANAGER_TEL".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_TEL", Integer.valueOf(j));
						}
						if ("MANAGER_MOBILE".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("MANAGER_MOBILE", Integer.valueOf(j));
						}
						if ("IPS".equals(((ArrayList) result.get(i)).get(j).toString())) {
							map.put("IPS", Integer.valueOf(j));
						}
					}
				}
				for (int k = 2; k < result.size(); k++) {
					try {
						String jfmc = ((ArrayList) result.get(k)).get(((Integer) map.get("ROOM_NAME")).intValue()).toString();
						String jfxh = ((ArrayList) result.get(k)).get(((Integer) map.get("ROOM_IDX")).intValue()).toString();
						String jfszd = ((ArrayList) result.get(k)).get(((Integer) map.get("ROOM_ADDRESS")).intValue()).toString();
						String jfxz = ((ArrayList) result.get(k)).get(((Integer) map.get("ROOM_PROPERTY")).intValue()).toString();

						String fzr = ((ArrayList) result.get(k)).get(((Integer) map.get("ROOM_MANAGER")).intValue()).toString();
						String fzrdh = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_TEL")).intValue()).toString();
						String fzrsj = ((ArrayList) result.get(k)).get(((Integer) map.get("MANAGER_MOBILE")).intValue()).toString();
						String ip = ((ArrayList) result.get(k)).get(((Integer) map.get("IPS")).intValue()).toString();

						dataMap.put("ROOM_NAME", jfmc);
						dataMap.put("ROOM_IDX", jfxh);
						dataMap.put("ROOM_ADDRESS", jfszd);
						dataMap.put("ROOM_PROPERTY", jfxz);
						dataMap.put("ROOM_MANAGER", fzr);
						dataMap.put("MANAGER_TEL", fzrdh);
						dataMap.put("MANAGER_MOBILE", fzrsj);
						dataMap.put("IPS", ip);

						String ridStr = FMPContex.getNewUUID();
						dataMap.put("RID", ridStr);
						dataMap.put("ACCESS_ID", this.idcId);
						dataMap.put("CREATTIME", FMPContex.getCurrentTime());
						dataMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
						dataMap.put("RECORDSTATE", "0");
						this.sqlSession.update("SmmsRoomInfo.updateRecordstate", dataMap);
						successNum = this.sqlSession.insert("SmmsRoomInfo.insertSave", dataMap);
						if ((ip != null) && (!"".equals(ip))) {
							String str = ExcelUtil.ToDBC(ip);
							int comma = str.indexOf(',');
							if (comma == -1) {
								String[] ips = str.split("-");
								Map ipMap = new HashMap();
								ipMap.put("RID", FMPContex.getNewUUID());
								ipMap.put("ACCESS_ID", this.idcId);
								ipMap.put("ROOM_IDX", jfxh);
								ipMap.put("BEGIN_IP", ips[0]);
								ipMap.put("END_IP", ips[1]);
								ipMap.put("CREATTIME", FMPContex.getCurrentTime());
								ipMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
								ipMap.put("RECORDSTATE", "0");
								this.sqlSession.update("SmmsRoomIprange.updateRecordstate", ipMap);
								this.sqlSession.insert("SmmsRoomIprange.insertSave", ipMap);
							} else {
								String[] kv = str.split(",");
								for (int j = 0; j < kv.length; j++) {
									String[] ips = kv[j].split("-");
									Map ipMap = new HashMap();
									ipMap.put("RID", FMPContex.getNewUUID());
									ipMap.put("ACCESS_ID", this.idcId);
									ipMap.put("ROOM_IDX", jfxh);
									ipMap.put("BEGIN_IP", ips[0]);
									ipMap.put("END_IP", ips[1]);
									ipMap.put("CREATTIME", FMPContex.getCurrentTime());
									ipMap.put("MODIFIEDTIME", FMPContex.getCurrentTime());
									ipMap.put("RECORDSTATE", "0");
									this.sqlSession.update("SmmsRoomIprange.updateRecordstate", ipMap);
									this.sqlSession.insert("SmmsRoomIprange.insertSave", ipMap);
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
					if (falseCol.endsWith(",")) {
						falseCol = falseCol.substring(0, falseCol.length() - 1);
					}
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
