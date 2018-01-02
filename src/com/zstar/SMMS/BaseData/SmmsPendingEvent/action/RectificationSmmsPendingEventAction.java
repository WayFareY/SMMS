package com.zstar.SMMS.BaseData.SmmsPendingEvent.action;

import com.zstar.SMMS.BaseData.SmmsPendingEvent.delegate.PendingEventDel;
import com.zstar.SMMS.constant.SMMSConstant;
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
		String threat_name = "";

		PendingEventDel del = new PendingEventDel(this.contex);

		Map insertMap = del.selectInsertPendingInfoByIpOrUrl(selectMap);
		if ((insertMap != null) && (insertMap.size() > 0)) {
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
			rid = del.insertSmmsPendingEvent(insertMap);
		}
		Map mapRid = new HashMap();

		List mapUrlList = this.sqlSession.selectList("WebCase.viewDoMainNameAndAccessId", insertMap);
		if ((mapUrlList != null) && (mapUrlList.size() > 0)) {
			mapRid.put("event_id", rid);
			String ridList = "'" + rid + "'";
			mapRid.put("ridCondition", "and ssp.RID IN (" + ridList + ")");
			String message = del.rectification(mapRid);
			setMsg(message);
		} else {
			List eventList = this.sqlSession.selectList("SmmsWebCaseIp.selectRidByIp", insertMap);
			if ((eventList != null) && (eventList.size() > 0)) {
				Map eventMap = (Map) eventList.get(0);

				Map selectUrlByRidMap = (Map) this.sqlSession.selectOne("WebCase.getDomainNameByRid", eventMap);
				if ((selectUrlByRidMap != null) && (selectUrlByRidMap.size() > 0)) {
					eventMap.put("event_id", rid);
					String ridList = "'" + rid + "'";
					mapRid.put("ridCondition", "and ssp.RID IN (" + ridList + ")");
					String message = del.rectification(eventMap);
					setMsg(message);
				}
			} else {
				setMsg("查询不到数据");
			}
		}
		return "empty";
	}
}
