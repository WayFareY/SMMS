package com.zstar.SMMS.BaseData.SmmsEventMain.action;

import com.zstar.SMMS.BaseData.SmmsEventMain.action.delegate.EventMainDel;
import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.constant.SMMSConstant;
import com.zstar.fmp.core.base.FMPContex;
import com.zstar.fmp.core.frame.action.FrameAction;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectificationSmmsPendingEventAction extends FrameAction {

	public String bizExecute() throws Exception {
		String url = (String) getWebData("URL");
		Map selectMap = new HashMap();
		selectMap.put("URL", url.toUpperCase());

		selectMap.put("IP", getWebData("IP"));

		String rid = "";
		String mainRid = "";
		String threat_name = "";

		PendingEventDel del = new PendingEventDel(this.contex);

		EventMainDel mainDel = new EventMainDel(this.contex);

		Map insertMap = mainDel.selectInsertPendingInfoByIpOrUrl(selectMap);
		if ((insertMap != null) && (insertMap.size() > 0)) {
			insertMap.put("DBTYPE", FMPContex.DBType);
			insertMap.put("IP", selectMap.get("IP"));
			insertMap.put("URL", url.toLowerCase());

			threat_name = (String) getWebData("THREAT_NAME");
			insertMap.put("THREAT_NAME", threat_name);

			insertMap.put("FORCE_CLOSE_DESCE", getWebData("FORCE_CLOSE_DESCE"));

			insertMap.put("CLIENTNAME", getWebData("CURR_USERID"));

			insertMap.put("IS_FORCE_CLOSE", SMMSConstant.IS_FORCE_CLOSE_TWO);

			insertMap.put("THREAT_LEVEL", SMMSConstant.THREAT_LEVEL);

			insertMap.put("EVENT_SOURCE", SMMSConstant.EVENT_SOURCE);
			if ("".equals(threat_name)) {
				insertMap.put("THREAT_NAME", SMMSConstant.THREAT_NAME);
			}
			if (insertMap.containsKey("WEB_CASE_RID")) {
				Map webRidMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countWebCaseRidTotal", insertMap);

				Long webCaseTotal = (Long) webRidMap.get("WEBCASETOTAL");
				if (webCaseTotal.longValue() == 0L) {
					mainRid = mainDel.insertSmmsEventMain(insertMap);
				} else {
					mainRid = (String) webRidMap.get("MAIN_RID");
				}
			} else if (insertMap.containsKey("DOMAIN_NAME")) {
				Map domainMap = (Map) this.sqlSession.selectOne("SmmsEventMain.countDomainTotal", insertMap);

				Long domainTotal = (Long) domainMap.get("DOMAINTOTAL");
				if (domainTotal.longValue() == 0L) {
					mainRid = mainDel.insertSmmsEventMain(insertMap);
				} else {
					mainRid = (String) domainMap.get("MAIN_RID");
				}
			}
			if (!"".equals(mainRid)) {
				insertMap.put("MAIN_RID", mainRid);
				del.insertSmmsPendingEvent(insertMap);
			}
		}
		Map mapRid = new HashMap();

		List mapUrlList = this.sqlSession.selectList("WebCase.viewDoMainNameAndAccessId", selectMap);
		if ((mapUrlList != null) && (mapUrlList.size() > 0)) {
			mapRid.put("EVENT_ID", mainRid);
			String ridList = "'" + mainRid + "'";
			mapRid.put("RIDCONDITION", "and ssp.RID IN (" + ridList + ")");
			String message = mainDel.rectification(mapRid);
			setMsg(message);
		} else {
			List eventList = this.sqlSession.selectList("SmmsWebCaseIp.selectRidByIp", insertMap);
			if ((eventList != null) && (eventList.size() > 0)) {
				Map eventMap = (Map) eventList.get(0);

				Map selectUrlByRidMap = (Map) this.sqlSession.selectOne("WebCase.getDomainNameByRid", eventMap);
				if ((selectUrlByRidMap != null) && (selectUrlByRidMap.size() > 0)) {
					mapRid.put("EVENT_ID", mainRid);
					String ridList = "'" + mainRid + "'";
					mapRid.put("RIDCONDITION", "and ssp.RID IN (" + ridList + ")");
					String message = mainDel.rectification(eventMap);
					setMsg(message);
				}
			} else {
				setMsg("查询不到数据");
			}
		}
		return "empty";
	}
}
