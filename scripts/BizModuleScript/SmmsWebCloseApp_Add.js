/***********************************
 * @文件描述：
 *************************************/
function doOnload(){
	if (typeof(doOnload_Super) != "undefined") { doOnload_Super();}  //如果存在页面加载超级方法，则先执行
////////对于新增操作，页面加载时要执行的代码在此完成//////////
	var objId1 = "DoAddSave";
	var yn = false;
	setObjectDisplay(objId1,yn);
	var objId2 = "DoAddSaveClose";
	setObjectDisplay(objId2,yn);
	var objId = "addResetButton";
	setObjectDisplay(objId,yn);
	var btnText="提交";
	var clickFunc = "submitExam(this)";
	addButton(objId,btnText,clickFunc,yn);

}

function doChange(obj){
	if (typeof(doChange_Super) != "undefined") { doChange_Super(obj);}  //如果存在onChange的超级方法，则先执行
////////对于新增操作，页面元素发生改变是要执行的代码在此完成//////////
}

function submitExam(obj){
	var rid = getFieldTrueValue('RID');
	var level = getFieldTrueValue('APP_LEVEL');
	var reason = getFieldTrueValue('CLOSE_REASON');
	//reason = EncodeUtf8(reason,"UTF-8");
	//var url = rootPath + "/SMMS/SmmsWebCloseAppBiz/submitExam?RID="+rid+"&APP_LEVEL="+level+"&CLOSE_REASON="+reason;
	var url = rootPath + "/SMMS/SmmsWebCloseAppBiz/submitExam";
	//url = encodeURI(url);
	var form = document.forms[0];
	var result =checkAll(form);
	var obj=getDataMapObj("SmmsWebCloseApp.getCountTotal","RID="+rid);
	if(obj.TOTAL==0){
		if(result){
			XMLHttp.formSubmit(form, url, backCallSendMsg);
			//XMLHttp.urlSubmit(url, backCallSendMsg);
			function backCallSendMsg(msg){
				if(msg!='999'){
					selectedRid = msg;
					commStartWorkFlow('000034LCMB','SmmsWebCloseApp','400');
				}
				//刷新列表
				doRefreshList();
			}
		}else{
			showMessage("MSG1019");//请检查信息是否填写完整！
			return;
		}	
	}else{
		showMessage("数据已提交，请勿重复提交");//请检查信息是否填写完整！
		return;
	}
}
//查看日志详情
function doClick(obj){
	var rid=getFieldDisplayValue('EVENT_RID');
	var url = rootPath+"/SMMS/SmmsEventMainBiz/DoView?RID="+rid+"&tableModelId=SmmsEventMain";
	openWindow(comUrl(url));

}
//关闭当前页面
//function submitClose(obj){
//closeWindow();
//}
//function doAfterSubmit(msg){
//var rid=msg.substr(0,msg.length-5);
//rid="a880c9181df84f6186f2db47b12562e1";
//showMessage(rid);
//selectedRid = rid;
//commStartWorkFlow('000033LCMB','SmmsWebCloseApp','300');
////showMessage(msg);
//doRefreshList();
//closeWindow();
//}
