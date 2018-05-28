/***********************************
 * @文件描述：
 *************************************/
function doOnload(){
	if (typeof(doOnload_Super) != "undefined") { doOnload_Super();}  //如果存在页面加载超级方法，则先执行
////////对于列表操作，页面加载时要执行的代码在此完成//////////
}

function doChange(obj){
	if (typeof(doChange_Super) != "undefined") { doChange_Super(obj);}  //如果存在onChange的超级方法，则先执行
////////对于列表操作，页面元素发生改变是要执行的代码在此完成//////////
}
//显示日志举证
function viewContentPath(contentPath){
	//ac日志举证
	var obj = getDataMapObj('SmmsPendingEvent_Wba.selectContentPathByRid&RID='+contentPath);
	if(obj.SNAPSHOP != 'null' && obj.SNAPSHOP !='' && obj.SNAPSHOP != null && obj.EVENT_TYPE1!= 'null' && obj.EVENT_TYPE1 !='' && obj.EVENT_TYPE1 != null){
		var url = rootPath+"/SMMS/SmmsPendingEvent_WbaBiz/PendingEventWba?RID="+contentPath;
		openWindow(url);
	}else{
		//af日志举证
		var url = rootPath+"/SMMS/SmmsPending_rzjzBiz/showAfSnapshop?RID="+contentPath;
		openWindow(comUrl(url));
	}
}
