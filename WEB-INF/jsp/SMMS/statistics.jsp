<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="com.zstar.fmp.core.base.FMPContex"%>
<%@ page import="com.zstar.fmp.web.component.*"%>
<%
	String rootPath = request.getContextPath();
	WebComponent wff = FMPContex.webFieldFactory;
%>
<jsp:include
	page="/WEB-INF/jsp/fmp/frame/common/partialJsp/headinclude.jsp" />
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>安全事件统计</title>
<style type="text/css">
#count {
	width: 100%;
	height: auto;
	overflow-x: auto;
}
#count table {
	white-space: nowrap;
	text-align: center;
	border:solid #C7E6FF; 
	border-width:1px 0px 0px 1px;
}

td{
	border:solid #C7E6FF; 
	border-width:0px 1px 1px 0px; 
	padding:5px;
}
</style>
</head>
<body>
<div id="count">
<table cellpadding="0" cellspacing="0" border="1px" id="count_table">
	<tr>
		<td>运营商名称</td>
		<td>IDC总计</td>
		<td>机房名称</td>
		<td>机房序号</td>
		<td>总计</td>
		<td>SMB扫描</td>
		<td>漏洞扫描</td>
		<td>webshell上传攻击</td>
		<td>C&C通信</td>
		<td>违规访问，蜜罐</td>
		<td>恶意软件，Sality家族</td>
		<td>恶意软件，Gbot家族</td>
		<td>恶意软件，Sdbot家族</td>
		<td>恶意软件，Zeus家族</td>
		<td>JCE僵尸网络</td>
		<td>IRC僵尸网络</td>
		<td>勒索软件，WannaCry</td>
		<td>飞客蠕虫</td>
		<td>Ramnit家族</td>
		<td>LPK家族</td>
		<td>DDOS家族</td>
		<td>XCodeGhost</td>
		<td>暗云木马III</td>
		<td>DGA随机域名，LSTM算法，机器学习</td>
		<td>勒索软件，Petya</td>
		<td>Sobig蠕虫</td>
		<td>Gamarue蠕虫</td>
		<td>Nitol僵尸网络</td>
		<td>Virut僵尸网络</td>
		<td>魔鼬</td>
		<td>webshell后门</td>
		<td>异常UA</td>
		<td>风险访问</td>
		<td>黑链</td>
		<td>虚拟货币挖矿</td>
		<td>DDOS攻击</td>
		<td>暴力破解</td>
		<td>webshell扫描</td>
		<td>web漏洞利用</td>
		<td>system漏洞利用</td>
		<td>SQL注入</td>
		<td>SMB协议扫描</td>
		<td>web整站系统漏洞攻击</td>
		<td>XSS攻击</td>
		<td>目录遍历攻击</td>
		<td>网站扫描</td>
		<td>恶意链接</td>
		<td>端口扫描</td>
		<td>IP扫描</td>
		<td>勒索病毒</td>
		<td>SMB协议攻击</td>
	</tr>
</table>
</div>
</body>

<jsp:include
	page="/WEB-INF/jsp/fmp/frame/common/partialJsp/afterinclude.jsp" />
<script src="<%=rootPath%>/scripts/smms/jquery-3.2.1.js"></script>
<script type="text/javascript">
	$(function(){
 		var url = rootPath + "/SMMS/SmmsEventMainBiz/getWxxfJosnAction";
	  $.ajax({
		  type:"POST",
          async:false,
          url:url,
          dataType:"json",
          success:function(data){
        	  load_table(data,"count_table");
          },
          error:function(data){
              alert("Get Data Failed");
          }
	  });
	});
	function load_table(data,divId){
		$("#count_table tr:gt(0)").remove();
		var tab = $("#"+divId);
		var IDC1 = [];
		var IDC2 = [];
		var IDC3 = [];
		var IDC4 = [];
		for (var i = 0; i < data.length; i++) {
			if(data[i].IDC_NAME=="时代互联"){
				IDC1[IDC1.length]=data[i];
			}
			if(data[i].IDC_NAME=="讯天在线"){
				IDC2[IDC2.length]=data[i];
			}
			if(data[i].IDC_NAME=="耐思尼克"){
				IDC3[IDC3.length]=data[i];
			}
			if(data[i].IDC_NAME=="盛网软件"){
				IDC4[IDC4.length]=data[i];
			}
		}
		for (var i = 0; i < IDC1.length; i++) {
			var lg = IDC1.length;
			//创建节点
			if(i==0){
				var tdhtml = $("<tr style='border:1px solid black;'><td rowspan='"+lg+"'>"+IDC1[i].IDC_NAME+"</td><td rowspan='"+lg+"'>"+IDC1[i].ALL_COUNT+"</td><td>"+IDC1[i].ROOM_NAME+
						"<td>"+IDC1[i].ROOM_IDX+"</td><td>"+IDC1[i].ROOM_COUNT+"</td><td>"+IDC1[i].SMBSM+
						"<td>"+IDC1[i].LDSM+"</td><td>"+IDC1[i].webshellSCGJ+"</td><td>"+IDC1[i].TYXTX+
						"<td>"+IDC1[i].WGFWMG+"</td><td>"+IDC1[i].WYRJSalityJZ+"</td><td>"+IDC1[i].WYRJGbotJZ+
						"<td>"+IDC1[i].EYRJSdbotJZ+"</td><td>"+IDC1[i].EYRJZeusZJ+"</td><td>"+IDC1[i].JCEJSWL+
						"<td>"+IDC1[i].IRCJSWL+"</td><td>"+IDC1[i].LSRJWannaCry+"</td><td>"+IDC1[i].FKRC+
						"<td>"+IDC1[i].RamnitJZ+"</td><td>"+IDC1[i].LPKJZ+"</td><td>"+IDC1[i].DDOSJZ+
						"<td>"+IDC1[i].XCodeGhost+"</td><td>"+IDC1[i].AYMMIII+"</td><td>"+IDC1[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC1[i].LSRJPetya+"</td><td>"+IDC1[i].SobigRC+"</td><td>"+IDC1[i].GamarueRC+
						"<td>"+IDC1[i].NitolJSWL+"</td><td>"+IDC1[i].VirutJSWL+"</td><td>"+IDC1[i].MY+
						"<td>"+IDC1[i].webshellHM+"</td><td>"+IDC1[i].YCUA+"</td><td>"+IDC1[i].FXFW+
						"<td>"+IDC1[i].HL+"</td><td>"+IDC1[i].XNHBWK+"</td><td>"+IDC1[i].DDOSGJ+
						"<td>"+IDC1[i].BLPJ+"</td><td>"+IDC1[i].webshellSM+"<td>"+IDC1[i].webLDLY+
						"</td><td>"+IDC1[i].systemLDLY+"</td><td>"+IDC1[i].SQLZR+
						"<td>"+IDC1[i].SMBXYSM+"</td><td>"+IDC1[i].webZZXTLDGJ+"</td><td>"+IDC1[i].XSSGJ+
						"<td>"+IDC1[i].MLBLGJ+"</td><td>"+IDC1[i].WZSM+"</td><td>"+IDC1[i].EYLJ+
						"<td>"+IDC1[i].DKSM+"</td><td>"+IDC1[i].IPSM+"</td><td>"+IDC1[i].LSBD+"</td><td>"+IDC1[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}else{
				var tdhtml = $("<tr style='border:1px solid black;'></td><td>"+IDC1[i].ROOM_NAME+
						"<td>"+IDC1[i].ROOM_IDX+"</td><td>"+IDC1[i].ROOM_COUNT+"</td><td>"+IDC1[i].SMBSM+
						"<td>"+IDC1[i].LDSM+"</td><td>"+IDC1[i].webshellSCGJ+"</td><td>"+IDC1[i].TYXTX+
						"<td>"+IDC1[i].WGFWMG+"</td><td>"+IDC1[i].WYRJSalityJZ+"</td><td>"+IDC1[i].WYRJGbotJZ+
						"<td>"+IDC1[i].EYRJSdbotJZ+"</td><td>"+IDC1[i].EYRJZeusZJ+"</td><td>"+IDC1[i].JCEJSWL+
						"<td>"+IDC1[i].IRCJSWL+"</td><td>"+IDC1[i].LSRJWannaCry+"</td><td>"+IDC1[i].FKRC+
						"<td>"+IDC1[i].RamnitJZ+"</td><td>"+IDC1[i].LPKJZ+"</td><td>"+IDC1[i].DDOSJZ+
						"<td>"+IDC1[i].XCodeGhost+"</td><td>"+IDC1[i].AYMMIII+"</td><td>"+IDC1[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC1[i].LSRJPetya+"</td><td>"+IDC1[i].SobigRC+"</td><td>"+IDC1[i].GamarueRC+
						"<td>"+IDC1[i].NitolJSWL+"</td><td>"+IDC1[i].VirutJSWL+"</td><td>"+IDC1[i].MY+
						"<td>"+IDC1[i].webshellHM+"</td><td>"+IDC1[i].YCUA+"</td><td>"+IDC1[i].FXFW+
						"<td>"+IDC1[i].HL+"</td><td>"+IDC1[i].XNHBWK+"</td><td>"+IDC1[i].DDOSGJ+
						"<td>"+IDC1[i].BLPJ+"</td><td>"+IDC1[i].webshellSM+"<td>"+IDC1[i].webLDLY+
						"</td><td>"+IDC1[i].systemLDLY+"</td><td>"+IDC1[i].SQLZR+
						"<td>"+IDC1[i].SMBXYSM+"</td><td>"+IDC1[i].webZZXTLDGJ+"</td><td>"+IDC1[i].XSSGJ+
						"<td>"+IDC1[i].MLBLGJ+"</td><td>"+IDC1[i].WZSM+"</td><td>"+IDC1[i].EYLJ+
						"<td>"+IDC1[i].DKSM+"</td><td>"+IDC1[i].IPSM+"</td><td>"+IDC1[i].LSBD+"</td><td>"+IDC1[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}
		}
		for (var i = 0; i < IDC2.length; i++) {
			var len = IDC2.length;
			//创建节点
			if(i==0){
				var tdhtml = $("<tr style='border:1px solid black;'><td rowspan='"+len+"'>"+IDC2[i].IDC_NAME+"</td><td rowspan='"+len+"'>"+IDC2[i].ALL_COUNT+"</td><td>"+IDC2[i].ROOM_NAME+
						"<td>"+IDC2[i].ROOM_IDX+"</td><td>"+IDC2[i].ROOM_COUNT+"</td><td>"+IDC2[i].SMBSM+
						"<td>"+IDC2[i].LDSM+"</td><td>"+IDC2[i].webshellSCGJ+"</td><td>"+IDC2[i].TYXTX+
						"<td>"+IDC2[i].WGFWMG+"</td><td>"+IDC2[i].WYRJSalityJZ+"</td><td>"+IDC2[i].WYRJGbotJZ+
						"<td>"+IDC2[i].EYRJSdbotJZ+"</td><td>"+IDC2[i].EYRJZeusZJ+"</td><td>"+IDC2[i].JCEJSWL+
						"<td>"+IDC2[i].IRCJSWL+"</td><td>"+IDC2[i].LSRJWannaCry+"</td><td>"+IDC2[i].FKRC+
						"<td>"+IDC2[i].RamnitJZ+"</td><td>"+IDC2[i].LPKJZ+"</td><td>"+IDC2[i].DDOSJZ+
						"<td>"+IDC2[i].XCodeGhost+"</td><td>"+IDC2[i].AYMMIII+"</td><td>"+IDC2[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC2[i].LSRJPetya+"</td><td>"+IDC2[i].SobigRC+"</td><td>"+IDC2[i].GamarueRC+
						"<td>"+IDC2[i].NitolJSWL+"</td><td>"+IDC2[i].VirutJSWL+"</td><td>"+IDC2[i].MY+
						"<td>"+IDC2[i].webshellHM+"</td><td>"+IDC2[i].YCUA+"</td><td>"+IDC2[i].FXFW+
						"<td>"+IDC2[i].HL+"</td><td>"+IDC2[i].XNHBWK+"</td><td>"+IDC2[i].DDOSGJ+
						"<td>"+IDC2[i].BLPJ+"</td><td>"+IDC2[i].webshellSM+"<td>"+IDC2[i].webLDLY+
						"</td><td>"+IDC2[i].systemLDLY+"</td><td>"+IDC2[i].SQLZR+
						"<td>"+IDC2[i].SMBXYSM+"</td><td>"+IDC2[i].webZZXTLDGJ+"</td><td>"+IDC2[i].XSSGJ+
						"<td>"+IDC2[i].MLBLGJ+"</td><td>"+IDC2[i].WZSM+"</td><td>"+IDC2[i].EYLJ+
						"<td>"+IDC2[i].DKSM+"</td><td>"+IDC2[i].IPSM+"</td><td>"+IDC2[i].LSBD+"</td><td>"+IDC2[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}else{
				var tdhtml = $("<tr style='border:1px solid black;'></td><td>"+IDC2[i].ROOM_NAME+
						"<td>"+IDC2[i].ROOM_IDX+"</td><td>"+IDC2[i].ROOM_COUNT+"</td><td>"+IDC2[i].SMBSM+
						"<td>"+IDC2[i].LDSM+"</td><td>"+IDC2[i].webshellSCGJ+"</td><td>"+IDC2[i].TYXTX+
						"<td>"+IDC2[i].WGFWMG+"</td><td>"+IDC2[i].WYRJSalityJZ+"</td><td>"+IDC2[i].WYRJGbotJZ+
						"<td>"+IDC2[i].EYRJSdbotJZ+"</td><td>"+IDC2[i].EYRJZeusZJ+"</td><td>"+IDC2[i].JCEJSWL+
						"<td>"+IDC2[i].IRCJSWL+"</td><td>"+IDC2[i].LSRJWannaCry+"</td><td>"+IDC2[i].FKRC+
						"<td>"+IDC2[i].RamnitJZ+"</td><td>"+IDC2[i].LPKJZ+"</td><td>"+IDC2[i].DDOSJZ+
						"<td>"+IDC2[i].XCodeGhost+"</td><td>"+IDC2[i].AYMMIII+"</td><td>"+IDC2[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC2[i].LSRJPetya+"</td><td>"+IDC2[i].SobigRC+"</td><td>"+IDC2[i].GamarueRC+
						"<td>"+IDC2[i].NitolJSWL+"</td><td>"+IDC2[i].VirutJSWL+"</td><td>"+IDC2[i].MY+
						"<td>"+IDC2[i].webshellHM+"</td><td>"+IDC2[i].YCUA+"</td><td>"+IDC2[i].FXFW+
						"<td>"+IDC2[i].HL+"</td><td>"+IDC2[i].XNHBWK+"</td><td>"+IDC2[i].DDOSGJ+
						"<td>"+IDC2[i].BLPJ+"</td><td>"+IDC2[i].webshellSM+"<td>"+IDC2[i].webLDLY+
						"</td><td>"+IDC2[i].systemLDLY+"</td><td>"+IDC2[i].SQLZR+
						"<td>"+IDC2[i].SMBXYSM+"</td><td>"+IDC2[i].webZZXTLDGJ+"</td><td>"+IDC2[i].XSSGJ+
						"<td>"+IDC2[i].MLBLGJ+"</td><td>"+IDC2[i].WZSM+"</td><td>"+IDC2[i].EYLJ+
						"<td>"+IDC2[i].DKSM+"</td><td>"+IDC2[i].IPSM+"</td><td>"+IDC2[i].LSBD+"</td><td>"+IDC2[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}
		}
		for (var i = 0; i < IDC3.length; i++) {
			var len = IDC3.length;
			//创建节点
			if(i==0){
				var tdhtml = $("<tr style='border:1px solid black;'><td rowspan='"+len+"'>"+IDC3[i].IDC_NAME+"</td><td rowspan='"+len+"'>"+IDC3[i].ALL_COUNT+"</td><td>"+IDC3[i].ROOM_NAME+
						"<td>"+IDC3[i].ROOM_IDX+"</td><td>"+IDC3[i].ROOM_COUNT+"</td><td>"+IDC3[i].SMBSM+
						"<td>"+IDC3[i].LDSM+"</td><td>"+IDC3[i].webshellSCGJ+"</td><td>"+IDC3[i].TYXTX+
						"<td>"+IDC3[i].WGFWMG+"</td><td>"+IDC3[i].WYRJSalityJZ+"</td><td>"+IDC3[i].WYRJGbotJZ+
						"<td>"+IDC3[i].EYRJSdbotJZ+"</td><td>"+IDC3[i].EYRJZeusZJ+"</td><td>"+IDC3[i].JCEJSWL+
						"<td>"+IDC3[i].IRCJSWL+"</td><td>"+IDC3[i].LSRJWannaCry+"</td><td>"+IDC3[i].FKRC+
						"<td>"+IDC3[i].RamnitJZ+"</td><td>"+IDC3[i].LPKJZ+"</td><td>"+IDC3[i].DDOSJZ+
						"<td>"+IDC3[i].XCodeGhost+"</td><td>"+IDC3[i].AYMMIII+"</td><td>"+IDC3[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC3[i].LSRJPetya+"</td><td>"+IDC3[i].SobigRC+"</td><td>"+IDC3[i].GamarueRC+
						"<td>"+IDC3[i].NitolJSWL+"</td><td>"+IDC3[i].VirutJSWL+"</td><td>"+IDC3[i].MY+
						"<td>"+IDC3[i].webshellHM+"</td><td>"+IDC3[i].YCUA+"</td><td>"+IDC3[i].FXFW+
						"<td>"+IDC3[i].HL+"</td><td>"+IDC3[i].XNHBWK+"</td><td>"+IDC3[i].DDOSGJ+
						"<td>"+IDC3[i].BLPJ+"</td><td>"+IDC3[i].webshellSM+"<td>"+IDC3[i].webLDLY+
						"</td><td>"+IDC3[i].systemLDLY+"</td><td>"+IDC3[i].SQLZR+
						"<td>"+IDC3[i].SMBXYSM+"</td><td>"+IDC3[i].webZZXTLDGJ+"</td><td>"+IDC3[i].XSSGJ+
						"<td>"+IDC3[i].MLBLGJ+"</td><td>"+IDC3[i].WZSM+"</td><td>"+IDC3[i].EYLJ+
						"<td>"+IDC3[i].DKSM+"</td><td>"+IDC3[i].IPSM+"</td><td>"+IDC3[i].LSBD+"</td><td>"+IDC3[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}else{
				var tdhtml = $("<tr style='border:1px solid black;'></td><td>"+IDC3[i].ROOM_NAME+
						"<td>"+IDC3[i].ROOM_IDX+"</td><td>"+IDC3[i].ROOM_COUNT+"</td><td>"+IDC3[i].SMBSM+
						"<td>"+IDC3[i].LDSM+"</td><td>"+IDC3[i].webshellSCGJ+"</td><td>"+IDC3[i].TYXTX+
						"<td>"+IDC3[i].WGFWMG+"</td><td>"+IDC3[i].WYRJSalityJZ+"</td><td>"+IDC3[i].WYRJGbotJZ+
						"<td>"+IDC3[i].EYRJSdbotJZ+"</td><td>"+IDC3[i].EYRJZeusZJ+"</td><td>"+IDC3[i].JCEJSWL+
						"<td>"+IDC3[i].IRCJSWL+"</td><td>"+IDC3[i].LSRJWannaCry+"</td><td>"+IDC3[i].FKRC+
						"<td>"+IDC3[i].RamnitJZ+"</td><td>"+IDC3[i].LPKJZ+"</td><td>"+IDC3[i].DDOSJZ+
						"<td>"+IDC3[i].XCodeGhost+"</td><td>"+IDC3[i].AYMMIII+"</td><td>"+IDC3[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC3[i].LSRJPetya+"</td><td>"+IDC3[i].SobigRC+"</td><td>"+IDC3[i].GamarueRC+
						"<td>"+IDC3[i].NitolJSWL+"</td><td>"+IDC3[i].VirutJSWL+"</td><td>"+IDC3[i].MY+
						"<td>"+IDC3[i].webshellHM+"</td><td>"+IDC3[i].YCUA+"</td><td>"+IDC3[i].FXFW+
						"<td>"+IDC3[i].HL+"</td><td>"+IDC3[i].XNHBWK+"</td><td>"+IDC3[i].DDOSGJ+
						"<td>"+IDC3[i].BLPJ+"</td><td>"+IDC3[i].webshellSM+"<td>"+IDC3[i].webLDLY+
						"</td><td>"+IDC3[i].systemLDLY+"</td><td>"+IDC3[i].SQLZR+
						"<td>"+IDC3[i].SMBXYSM+"</td><td>"+IDC3[i].webZZXTLDGJ+"</td><td>"+IDC3[i].XSSGJ+
						"<td>"+IDC3[i].MLBLGJ+"</td><td>"+IDC3[i].WZSM+"</td><td>"+IDC3[i].EYLJ+
						"<td>"+IDC3[i].DKSM+"</td><td>"+IDC3[i].IPSM+"</td><td>"+IDC3[i].LSBD+"</td><td>"+IDC3[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}
		}
		for (var i = 0; i < IDC4.length; i++) {
			var len = IDC4.length;
			//创建节点
			if(i==0){
				var tdhtml = $("<tr style='border:1px solid black;'><td rowspan='"+len+"'>"+IDC4[i].IDC_NAME+"</td><td rowspan='"+len+"'>"+IDC4[i].ALL_COUNT+"</td><td>"+IDC4[i].ROOM_NAME+
						"<td>"+IDC4[i].ROOM_IDX+"</td><td>"+IDC4[i].ROOM_COUNT+"</td><td>"+IDC4[i].SMBSM+
						"<td>"+IDC4[i].LDSM+"</td><td>"+IDC4[i].webshellSCGJ+"</td><td>"+IDC4[i].TYXTX+
						"<td>"+IDC4[i].WGFWMG+"</td><td>"+IDC4[i].WYRJSalityJZ+"</td><td>"+IDC4[i].WYRJGbotJZ+
						"<td>"+IDC4[i].EYRJSdbotJZ+"</td><td>"+IDC4[i].EYRJZeusZJ+"</td><td>"+IDC4[i].JCEJSWL+
						"<td>"+IDC4[i].IRCJSWL+"</td><td>"+IDC4[i].LSRJWannaCry+"</td><td>"+IDC4[i].FKRC+
						"<td>"+IDC4[i].RamnitJZ+"</td><td>"+IDC4[i].LPKJZ+"</td><td>"+IDC4[i].DDOSJZ+
						"<td>"+IDC4[i].XCodeGhost+"</td><td>"+IDC4[i].AYMMIII+"</td><td>"+IDC4[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC4[i].LSRJPetya+"</td><td>"+IDC4[i].SobigRC+"</td><td>"+IDC4[i].GamarueRC+
						"<td>"+IDC4[i].NitolJSWL+"</td><td>"+IDC4[i].VirutJSWL+"</td><td>"+IDC4[i].MY+
						"<td>"+IDC4[i].webshellHM+"</td><td>"+IDC4[i].YCUA+"</td><td>"+IDC4[i].FXFW+
						"<td>"+IDC4[i].HL+"</td><td>"+IDC4[i].XNHBWK+"</td><td>"+IDC4[i].DDOSGJ+
						"<td>"+IDC4[i].BLPJ+"</td><td>"+IDC4[i].webshellSM+"<td>"+IDC4[i].webLDLY+
						"</td><td>"+IDC4[i].systemLDLY+"</td><td>"+IDC4[i].SQLZR+
						"<td>"+IDC4[i].SMBXYSM+"</td><td>"+IDC4[i].webZZXTLDGJ+"</td><td>"+IDC4[i].XSSGJ+
						"<td>"+IDC4[i].MLBLGJ+"</td><td>"+IDC4[i].WZSM+"</td><td>"+IDC4[i].EYLJ+
						"<td>"+IDC4[i].DKSM+"</td><td>"+IDC4[i].IPSM+"</td><td>"+IDC4[i].LSBD+"</td><td>"+IDC4[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}else{
				var tdhtml = $("<tr style='border:1px solid black;'></td><td>"+IDC4[i].ROOM_NAME+
						"<td>"+IDC4[i].ROOM_IDX+"</td><td>"+IDC4[i].ROOM_COUNT+"</td><td>"+IDC4[i].SMBSM+
						"<td>"+IDC4[i].LDSM+"</td><td>"+IDC4[i].webshellSCGJ+"</td><td>"+IDC4[i].TYXTX+
						"<td>"+IDC4[i].WGFWMG+"</td><td>"+IDC4[i].WYRJSalityJZ+"</td><td>"+IDC4[i].WYRJGbotJZ+
						"<td>"+IDC4[i].EYRJSdbotJZ+"</td><td>"+IDC4[i].EYRJZeusZJ+"</td><td>"+IDC4[i].JCEJSWL+
						"<td>"+IDC4[i].IRCJSWL+"</td><td>"+IDC4[i].LSRJWannaCry+"</td><td>"+IDC4[i].FKRC+
						"<td>"+IDC4[i].RamnitJZ+"</td><td>"+IDC4[i].LPKJZ+"</td><td>"+IDC4[i].DDOSJZ+
						"<td>"+IDC4[i].XCodeGhost+"</td><td>"+IDC4[i].AYMMIII+"</td><td>"+IDC4[i].DGASJYMLSTMSFJQXX+
						"<td>"+IDC4[i].LSRJPetya+"</td><td>"+IDC4[i].SobigRC+"</td><td>"+IDC4[i].GamarueRC+
						"<td>"+IDC4[i].NitolJSWL+"</td><td>"+IDC4[i].VirutJSWL+"</td><td>"+IDC4[i].MY+
						"<td>"+IDC4[i].webshellHM+"</td><td>"+IDC4[i].YCUA+"</td><td>"+IDC4[i].FXFW+
						"<td>"+IDC4[i].HL+"</td><td>"+IDC4[i].XNHBWK+"</td><td>"+IDC4[i].DDOSGJ+
						"<td>"+IDC4[i].BLPJ+"</td><td>"+IDC4[i].webshellSM+"<td>"+IDC4[i].webLDLY+
						"</td><td>"+IDC4[i].systemLDLY+"</td><td>"+IDC4[i].SQLZR+
						"<td>"+IDC4[i].SMBXYSM+"</td><td>"+IDC4[i].webZZXTLDGJ+"</td><td>"+IDC4[i].XSSGJ+
						"<td>"+IDC4[i].MLBLGJ+"</td><td>"+IDC4[i].WZSM+"</td><td>"+IDC4[i].EYLJ+
						"<td>"+IDC4[i].DKSM+"</td><td>"+IDC4[i].IPSM+"</td><td>"+IDC4[i].LSBD+"</td><td>"+IDC4[i].SMBXYGJ+"</tr>");
				$(tab).append($(tdhtml));
			}
		}
		//样式控制
		changeColor();
	};
	function changeColor(){
		$('table').find('tr:even').css("background-color","#F7F6F6");
		$('table').find('tr:eq(0)').css("background-color","#C4DCF9");
	}
</script>

</html>