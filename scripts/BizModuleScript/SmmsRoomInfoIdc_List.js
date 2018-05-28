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
//下载IDC机房信息模板文件
function downloadRoomInfo(){
	var url = rootPath+"/SMMS/SmmsRoomInfoBiz/downloadRoomInfo";
	location.href=url;
}
//上传idc机房信息
function uploadExcelToRoomInf(){
	var url=rootPath+'/SMMS/SmmsRoomInfoIdcBiz/uploadExcelToRoomInf';
	openWindow(comUrl(url),'上传附件',null,null,'555px','200px');
}