/***********************************
 * @文件描述：SMMS系统公共的js，与SMMS相关的js函数都写在这
 *************************************/

//IDC安全事件管理的解封申请
function unblockApply(){
	var result=getDataListTrueValue(selectedRid,"RECTIFY_STATE");
	if(result =='030'){
		showMessage("该数据已在申请解封中，请勿执行操作");
		//刷新列表
		doRefreshList();
	}else{
		var url=rootPath+"/SMMS/SmmsWebOpenAppBiz/IDCAdd?tableModelId=SmmsWebOpenApp&RID="+selectedRid;
		openWindow(comUrl(url));
	}

}
//通过点击事件获取运行商编号的值（IDC_ID）并弹出一个查看页面
function viewIdcByAccessId(idcId){

	//在pop页面阻止打开查看页面
	if(typeof canOpenDivWindow  != "undefined" && !canOpenDivWindow){
		return;
	}

	var obj = getDataMapObj('IdcInfo.getIdcInfoByIdcId&IDC_ID='+idcId);
	if(obj != null && obj.RID != null){
		var rid = obj.RID;
		var url = rootPath+"/SMMS/IdcInfoBiz/DoView?RID="+rid+"&tableModelId=IdcInfo";

		openWindow(comUrl(url));
	}

}

//待处理日志事件副表
function viewEventDetialByRid(rid){
	var event_main_rid=" and ssp.MAIN_RID = '"+rid+"'";
	var url = rootPath+"/SMMS/SmmsPendingEventBiz/DoList?freezeCondition="+event_main_rid+"&tableModelId=SmmsPendingEvent";
	openWindow(url);
}

function viewAqsjByRid(rid){


	var event_main_rid=" and spea.MAIN_RID = '"+rid+"'";
	var url = rootPath+"/SMMS/PendingEvent_aqsjBiz/DoList?freezeCondition="+event_main_rid+"&tableModelId=PendingEvent_aqsj";
	openWindow(url);
}
function viewVpnByRid(rid){


	var event_main_rid=" and ssp.MAIN_RID = '"+rid+"'";
	var url = rootPath+"/SMMS/PendingEvent_vpnBiz/DoList?freezeCondition="+event_main_rid+"&tableModelId=PendingEvent_vpn";
	openWindow(url);
}
function viewWbaByRid(rid){


	var event_main_rid=" and ssp.MAIN_RID = '"+rid+"'";
	var url = rootPath+"/SMMS/SmmsPendingEvent_WbaBiz/DoList?freezeCondition="+event_main_rid+"&tableModelId=SmmsPendingEvent_Wba";
	openWindow(url);
}
function viewKeyByRid(rid){


	var event_main_rid=" and kyy.MAIN_RID = '"+rid+"'";
	var url = rootPath+"/SMMS/PendingEvent_keyBiz/DoList?freezeCondition="+event_main_rid+"&tableModelId=PendingEvent_key";
	openWindow(url);
}

//IDC安全事件管理的标记已整改
function signCorrect(){
	var selectedDataList = new Array();
	selectedDataList = setSelectedDataList();
	var rids = "";
	if(selectedDataList != null){
		for (var i = 0; i<selectedDataList.length ;i++){
			var rid=selectedDataList[i].get("RID");  
			rids =rids+rid+",";
		}
		var url = rootPath+"/SMMS/SmmsEventMainBiz/signCorrect?RID="+rids;
		//发送一个ajax请求
		XMLHttp.urlSubmit(url,backCallSendMsg);
		function backCallSendMsg(msg){
			showMessage(msg);
			//刷新列表
			doRefreshList();
		}	
	}
}

function viewWebCaseByRid(rid){
	if(rid!=''&& rid !=null && rid!='无'){
		var url = rootPath+"/SMMS/IdcInfoBiz/DoView?RID="+rid+"&tableModelId=WebCase";
		openWindow(comUrl(url));
	}else{
		showMessage("该网站无备案信息");
	}

}
//显示日志事件详情页
function viewPendingByRid(rid){

	//ac日志举证
	var obj = getDataMapObj('SmmsPendingEvent_Wba.selectContentPathByRid&RID='+rid);
	if(obj.SNAPSHOP != 'null' && obj.SNAPSHOP !='' && obj.SNAPSHOP != null && obj.EVENT_TYPE1!= 'null' && obj.EVENT_TYPE1 !='' && obj.EVENT_TYPE1 != null){
		var url = rootPath+"/SMMS/SmmsPendingEvent_WbaBiz/PendingEventWba?RID="+contentPath;
		openWindow(url);
	}else{
		//af日志举证
		var url = rootPath+"/SMMS/SmmsPending_rzjzBiz/showAfSnapshop?RID="+rid;
		openWindow(comUrl(url));
	}
		
}


function viewEventMainByRid(rid){
	var obj = getDataMapObj('SmmsEventMain_rzjz.findSnapByRid&RID='+rid);
	if(obj.SNAPSHOP != null && obj.SNAPSHOP !='' && obj.SNAPSHOP != 'null'){
		var url = rootPath+"/SMMS/SmmsEventMain_rzjzBiz/DoView?RID="+rid+"&tableModelId=SmmsEventMain_rzjz";
		openWindow(comUrl(url));}
}

function viewAFlogByRid(rid){
	var obj = getDataMapObj('SmmsAfLog.findEventEvidence&RID='+rid);
	if(obj.EVENT_EVIDENCE!= null && obj.EVENT_EVIDENCE !='' && obj.EVENT_EVIDENCE!= 'null'){
		var url = rootPath+"/SMMS/SmmsAfLogTwoBiz/DoView?RID="+rid+"&tableModelId=SmmsAfLogTwo";
		openWindow(comUrl(url));}
}
function viewUrl(URL){
	//在pop页面阻止打开查看页面
	if(typeof canOpenDivWindow  != "undefined" && !canOpenDivWindow){
		return;
	}
	var urlStr = 'http://' + URL.replace(/^http:\/\//i, '');
	openWindow(comUrl(urlStr));

}
//下发关键字的方法
function sandKeys(){
	var url = rootPath+"/SMMS/SmmsKeywordBiz/sandKey";
	XMLHttp.urlSubmit(url,backCallSendMsg);
	function backCallSendMsg(msg){
		var message = msg.substr(0, 3);
		if (message == '000') {
			var msg = msg.substr(4, 8);
			showMessage(msg);
			doRefreshList();
		} else {
			showMessage(msg);
			doRefreshList();
		}

	}
}
//删除列表的方法
function deleteList(){
	var selectedDataList = new Array();
	selectedDataList = setSelectedDataList();
	var rids = "";
	if (selectedDataList != null) {
		if (confirm("是否确认要删除？")) {
			for (var i = 0; i<selectedDataList.length ;i++){
				var rid=selectedDataList[i].get("RID");  
				rids +=rid+",";

			}
			var url = rootPath+"/SMMS/SmmsEventMainBiz/DeleteList?RID="+rids;
			//发送一个ajax请求
			XMLHttp.urlSubmit(url,backCallSendMsg);
			function backCallSendMsg(msg){
				showMessage(msg);
				//刷新列表
				doRefreshList();
			}
		}
	} 
}

//移至待关停操作，目的是：修改字段‘是否强制关停’的值为1 ，代表是
function moveToClose(){
	var selectedDataList = new Array();
	//获取多选框的值
	selectedDataList = setSelectedDataList();
	//取出相应数值
	if(selectedDataList != null){
		for(var i = 0;i<selectedDataList.length;i++){
			var rid = selectedDataList[i].get("RID");
			var url = rootPath+"/SMMS/PendingEvent_aqsjBiz/MoveToClose?RID="+rid;
			XMLHttp.urlSubmit(url,backCallSendMsg);
			function backCallSendMsg(msg){
				showMessage(msg);
				//刷新列表
				doRefreshList();
			}
		}

	}else{
		showMessage("数据异常，请稍后再试。");
	}

}

//移至待整改操作，目的是：修改字段‘是否强制关停’的值为2 ，代表否
function moveToRectify(){
	var selectedDataList = new Array();
	//获取多选框的值
	selectedDataList = setSelectedDataList();
	var rids ="";
	//取出相应数值
	if(selectedDataList != null){
		for (var i = 0; i<selectedDataList.length ;i++){
			var rid=selectedDataList[i].get("RID");  
			rids +=rid+",";

		}
		var url = rootPath+"/SMMS/PendingEvent_aqsjBiz/MoveToRectify?RID="+rids;
		XMLHttp.urlSubmit(url,backCallSendMsg);
		function backCallSendMsg(msg){
			showMessage(msg);
			//刷新列表
			doRefreshList();
		}
	}else{
		showMessage("数据异常，请稍后再试。");
	}
}


//移至待关停 只有待整改的数据并且处理状态不030（恢复中）才能移至待关停（确定）
function moveToCloseEm(){
	var selectedDataList = new Array();
	//获取多选框的值
	selectedDataList = setSelectedDataList();
	//取出相应数值
	var rids="";
	if(selectedDataList != null){
		for(var i = 0;i<selectedDataList.length;i++){
			var rid = selectedDataList[i].get("RID");
			//获取最终整改建议
			var finalRectifySuggest=getDataListTrueValue(rid,"FINAL_RECTIFY_SUGGEST");
			var rectifyState=getDataListTrueValue(rid,"RECTIFY_STATE");
			if(finalRectifySuggest=='1'  && rectifyState !='030'){
				rids +=rid+",";}
		}
		if(rids!=""){
			var url = rootPath+"/SMMS/SmmsEventMain_aqsjBiz/MoveToCloseEm?RID="+rids;
			XMLHttp.urlSubmit(url,backCallSendMsg);
			function backCallSendMsg(msg){
				showMessage(msg);
				//刷新列表
				doRefreshList();
			}
		}else{
			showMessage("只能将整改并且处理状态不是恢复中的数据移至待关停");
		}

	}

}
//移至待整改 只有关停的数据并且处理状态不030（恢复中）是才能移至待整改
function moveToRectifyEm(){
	var selectedDataList = new Array();
	//获取多选框的值
	selectedDataList = setSelectedDataList();
	//取出相应数值
	var rids="";
	if(selectedDataList != null){
		for(var i = 0;i<selectedDataList.length;i++){
			var rid = selectedDataList[i].get("RID");
			//获取最终整改建议
			var finalRectifySuggest=getDataListTrueValue(rid,"FINAL_RECTIFY_SUGGEST");
			var rectifyState=getDataListTrueValue(rid,"RECTIFY_STATE");
			if(finalRectifySuggest=='2' && rectifyState !='030'){
				rids +=rid+",";
			}
		}
		if(rids!=""){
			var url = rootPath+"/SMMS/SmmsEventMain_aqsjBiz/MoveToRectifyEm?RID="+rids;
			XMLHttp.urlSubmit(url,backCallSendMsg);
			function backCallSendMsg(msg){
				showMessage(msg);
				//刷新列表
				doRefreshList();
			}
		}else{

			showMessage("只能将关停并且处理状态不是恢复中的数据移至待整改");


		}
	}
}



