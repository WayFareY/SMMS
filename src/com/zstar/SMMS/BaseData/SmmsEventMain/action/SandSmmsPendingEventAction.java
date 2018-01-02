package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import com.zstar.fmp.log.FMPLog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SandSmmsPendingEventAction extends FrameAction {

	public String bizExecute() throws Exception {
		String rid = (String) getWebData("RID");
		String[] kv = rid.split(",");
		String ridList = "";
		for (int j = 0; j < kv.length; j++) {
			String s = "'" + kv[j] + "'" + ",";
			ridList = ridList + s;
		}
		ridList = ridList.substring(0, ridList.length() - 1);

		FMPLog.printErr("test" + ridList);
		Map mapRid = new HashMap();
		mapRid.put("RIDCONDITION", "and ssp.RID IN (" + ridList + ") order by ssp.access_id asc");

		mapRid.put("DBTYPE", FMPContex.DBType);

		List<Map<String, String>> dataList = this.sqlSession.selectList("SmmsEventMain.viewToRectify", mapRid);
		EventMainDel mainDel = new EventMainDel(this.contex);
		String returnMessage = mainDel.returnSandMessage(dataList);
		setMsg(returnMessage);
		return "empty";
	}
}
