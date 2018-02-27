/***********************************
* @文件描述：
*************************************/
function doOnload(){
  if (typeof(doOnload_Super) != "undefined") { doOnload_Super();}  //如果存在页面加载超级方法，则先执行
////////对于修改操作，页面加载时要执行的代码在此完成//////////
  var key = keyLimit.value;
  if(key=="1"){
	  setFieldDisplay("BIZ2",false);
	  setFieldDisplay("BIZ3",false);
	  setFieldDisplay("BIZ4",false);
	  setFieldDisplay("BIZ5",false);
  }else if(key=="2"){
	  setFieldDisplay("BIZ1",false);
	  setFieldDisplay("BIZ3",false);
	  setFieldDisplay("BIZ4",false);
	  setFieldDisplay("BIZ5",false);
  }else if(key=="3"){
	  setFieldDisplay("BIZ2",false);
	  setFieldDisplay("BIZ1",false);
	  setFieldDisplay("BIZ4",false);
	  setFieldDisplay("BIZ5",false);
  }else if(key=="4"){
	  setFieldDisplay("BIZ2",false);
	  setFieldDisplay("BIZ3",false);
	  setFieldDisplay("BIZ1",false);
	  setFieldDisplay("BIZ5",false);
  }else if(key=="5"){
	  setFieldDisplay("BIZ2",false);
	  setFieldDisplay("BIZ3",false);
	  setFieldDisplay("BIZ1",false);
	  setFieldDisplay("BIZ4",false);
  }
}
function doChange(obj){
  if (typeof(doChange_Super) != "undefined") { doChange_Super(obj);}  //如果存在onChange的超级方法，则先执行
////////对于修改操作，页面元素发生改变是要执行的代码在此完成//////////
//修改判断是否人工关停设置 和 自动关停设置 
	//若是判断人工关停 则将自动关停设置改成否（人工）
	//若自动关停设置改成自动 则判断是否人工关停则改成否

	//判断是否人工关停
/*
	var biz5= getFieldTrueValue('BIZ5');
	var biz1= getFieldTrueValue('BIZ1');
	var flag=true;
	if (obj.name == "BIZ5"  && biz5=="1"){
		flag=confirm("自动关停设置将设置为否");
		if(flag==true){
			setFieldValue("BIZ1","2");
		}else{
			setFieldValue("BIZ5","2");		
			$("input[name='rdo_BIZ5'][value=1]").attr("checked",false); 
			$("input[name='rdo_BIZ5'][value=2]").attr("checked"); 
			
		}
		
	}
	if (obj.name == "BIZ1" && biz1=="1"){
		flag=confirm("判断是否人工关停设置将设置为否");
		if(flag==true){
			setFieldValue("BIZ5","2");
			
		}else{
			setFieldValue("BIZ1","2");		
			$("input[name='rdo_BIZ1'][value=1]").attr("checked",false); 
			$("input[name='rdo_BIZ1'][value=2]").attr("checked"); 
		}
	}*/	
}
 
