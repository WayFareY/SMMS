<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String rootPath = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<link href="<%=rootPath %>/styles/fmp/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="<%=rootPath %>/jedate-6.0.2/skin/jedate.css" rel="stylesheet" type="text/css" />
<script src="<%=rootPath %>/scripts/echarts.min.js"></script>
<script src="<%=rootPath %>/scripts/fmp/jquery-1.11.3.js"></script>
<script src="<%=rootPath %>/scripts/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=rootPath %>/jedate-6.0.2/jquery.jedate.js"></script> 

<style type="text/css">
* {
	margin: 0;
	padding: 0;
}

#homepage_border {
	width: 100%;
	min-width: 1366px;
}

.right_container_title {
	right: 2px;
	top: 61px;
	width: 100%;
	height: 28px;
	border: #4e627b solid 1px;
	background-color: #bfdcfc;
}

.right_title_bg {
	background: url(../../images/zscrd/kuangjia/title_bg.png) repeat-x;
	height: 28px;
	width: 100%;
}

.right_title_txt {
	background: url(../../images/zscrd/kuangjia/home_gb.png) repeat-x;
	width: 80px;
	height: 22px;
	position: absolute;
	top: 7px;
	left: 10px;
	border-left: #354860 solid 1px;
	border-top: #354860 solid 1px;
	border-right: #354860 solid 1px;
	line-height: 25px;
	text-align: center;
	font-size: 14px;
}

#right_container {
	background-color: #FFF;
	margin: 5px;
	height: auto;
	width: 99.5%;
	margin-top: 0px;
}

#right_container_row {
	background: url(../../images/zscrd/kuangjia/bgd.png) repeat-x;
	height: 80px;
}

#right_container_count_idc {
	background: url(../../images/zscrd/kuangjia/idc.png) no-repeat;
	height: 58px;
	margin-top: 12px;
}

#right_container_count_idc span {
	margin-left: 80px;
	display: block;
	padding-top: 12px;
	color: #5ed0ef;
	font-size: 14px;
	min-width: 80px;
}

#right_container_count_idchome {
	background: url(../../images/zscrd/kuangjia/jf.png) no-repeat;
	height: 58px;
	margin-top: 12px;
}

#right_container_count_idchome span {
	margin-left: 80px;
	display: block;
	padding-top: 12px;
	color: #5ed0ef;
	font-size: 14px;
	min-width: 80px;
}

#right_container_count_ywts {
	background: url(../../images/zscrd/kuangjia/ywts.png) no-repeat;
	height: 58px;
	margin-top: 12px;
	float: left;
}

#right_container_count_ywts span {
	margin-left: 105px;
	display: block;
	padding-top: 12px;
	color: #5ed0ef;
	font-size: 14px;
	min-width: 80px;
}

#right_container_count_ac {
	background: url(../../images/zscrd/kuangjia/ac.png) no-repeat;
	height: 58px;
	margin-top: 12px;
	margin-left: 15px;
}

#right_container_count_ac span {
	margin-left: 95px;
	display: block;
	padding-top: 3px;
	color: #5ed0ef;
	font-size: 14px;
	min-width: 80px;
}

#right_container_count_ac span ul {
	font-size: 8px;
}

#right_container_count_af {
	background: url(../../images/zscrd/kuangjia/ngaf.png) no-repeat;
	height: 58px;
	margin-top: 12px;
}

#right_container_count_af span {
	margin-left: 95px;
	display: block;
	padding-top: 3px;
	color: #5ed0ef;
	font-size: 14px;
	min-width: 80px;
}

#right_container_count_af ul {
	font-size: 8px;
	margin-left: 14px;
}

#right_container_count_sta {
	background: url(../../images/zscrd/kuangjia/sta.png) no-repeat;
	height: 58px;
	margin-top: 12px;
}

#right_container_count_sta span {
	margin-left: 95px;
	display: block;
	padding-top: 3px;
	color: #5ed0ef;
	font-size: 14px;
	min-width: 80px;
}

#right_container_count_sta ul {
	font-size: 8px;
}

#right_container_bottom_title {
	height: 30px;
	margin-top: 10px;
	margin-left: -15px;
	margin-bottom: 8px;
}

#right_container_bottom_count {
	border: 1px solid #a5c6ff;
	border-radius: 5px;
	padding-top: 8px;
	padding-bottom: 8px;
	background: #f7faff;
}

#allcount_monthk {
	font-size: 16px;
	color: #fff;
	background: #46b7ff;
}

#allcount_monthj {
	font-size: 16px;
	color: #fff;
	background: #ffa200;
	margin-right: 2px;
}

#allcount_monthi {
	font-size: 16px;
	color: #fff;
	background: #ff3eeb;
	margin-right: 2px;
}

#allcount_month2 {
	font-size: 16px;
	color: #007eff;
}

#allcount_month3 {
	font-size: 16px;
	color: #00b711;
}

#allcount_month4 {
	font-size: 16px;
	color: #ff9a22;
}

#echarts_five {
	height: 260px;
	margin-top: 4px;
	margin-bottom: 4px;
}

#echarts {
 	border: 1px solid rgba(0, 0, 0, 0.2); 
	-webkit-box-shadow: 0px 0px 0px rgba(0, 0, 0, 0.2);
	height: 260px;
}

.echarts_title {
	height: 45px;
	width: 100%;
	line-height: 45px;
}

.echarts_title ul {
	list-style: none;
}

.echarts_title ul li {
	float: left;
}

.echarts_title ul li input {
	height: 25px;
}

.echarts_title ul li:nth-child(1) {
	font-size: 18px;
	font-weight: bold;
}

.echarts_title ul li:nth-child(2) {
	float: right;
}

.echarts_barandpie {
	height: 180px;
	width: 100%;
}

.echarts_bottom {
	height: 30px;
	line-height: 30px;
}

.echarts_bottom ul {
	height: 100%;
	margin-left: 55px;
	list-style: none;
	
}

.echarts_bottom ul li {
	float: left;
	width: 33%;
}

.dot1,.dot2,.dot3 {
	display: inline-block;
	width: 12px;
	height: 12px;
	margin-right: 5px;
	vertical-align: middle;
	overflow: hidden;
	border-radius:200px;
}
.dot1{
	background-color: rgb(0,179,54);
}
.dot2{
	background-color: rgb(255,180,0);
}
.dot3{
	background-color: rgb(240,0,28);
}
.echarts_pie {
	width: 39%;
	height: 180px;
}

.echarts_pie,.echarts_bar {
	float: left;
}

.echarts_bar {
	width: 60%;
	height: 180px;
	float: right;
}

.echarts_bar_fenbu {
	border: 0px solid rgba(0, 0, 0, 0.2);
	-webkit-box-shadow: 0px 0px 0px rgba(0, 0, 0, 0.2);
	background-color: rgb(238, 238, 238);
	width: 100%;
	height: 25px;
	line-height: 25px;
	font-size: 12px;
}

.echarts_bar_main {
	-webkit-box-shadow: 0px 0px 0px rgba(0, 0, 0, 0.2);
	width: 100%;
	height: 155px;
}

#echarts_bar_wlaqsj,#echarts_bar_wxsjTop {
	height: auto;
	margin: 0;
	padding: 0;
	border-top: 1px solid #768aa3;
}

#echarts_bar_wlaqsj_top {
	height: 27px;
	width: 100%;
	border-bottom: 2px solid #639ddb;
	border: 1px solid #639ddb;
	background: url(../../images/zscrd/kuangjia/sjbg.png) repeat-x;
}

#echarts_bar_wlaqsj_left {
	height: 25px;
	width: 120px;
	background: url(../../images/zscrd/kuangjia/sjtbg.png) repeat-x;
	float: left;
	margin-left: 12px;
	text-align: center;
	line-height: 25px;
	color: #fff;
	font-size: 14px;
}

#echarts_bar_wlaqsj_right {
	float: right;
	margin-top: 2px;
	width: 125px;
}

</style>
</head>
<body>
	<div id="homepage_border">
		<!-- 主题内容 -->
		<div id="right_container">
			<div class="container-fluid">
				<div class="row" id="right_container_row">
					<div class="col-xs-2" id="right_container1">
						<div class="right_container_count" id="right_container_count_idc">
							<span id="idc">0个</span>
						</div>
					</div>
					<div class="col-xs-2" id="right_container1">
						<div class="right_container_count"
							id="right_container_count_idchome">
							<span id="roomidc">0个</span>
						</div>
					</div>
					<div class="col-xs-2" id="right_container1">
						<div class="right_container_count" id="right_container_count_ywts">
							<span id="ywts">0条</span>
						</div>
					</div>
					<div class="col-xs-2" id="right_container1">
						<div class="right_container_count" id="right_container_count_ac">
							<span>
								<ul>
									<li style="color: #42f603;"><a
										style="color: #5de0ef; font-size: 10px; text-decoration: none;"
										id="aconline">在线:0台</a></li>
									<li style="color: #ff1212;"><a
										style="color: #5de0ef; font-size: 10px; text-decoration: none;"
										id="acoffline">离线:0台</a></li>
								</ul>
							</span>
						</div>
					</div>
					<div class="col-xs-2" id="right_container1">
						<div class="right_container_count" id="right_container_count_af">
							<span>
								<ul>
									<li style="color: #42f603;"><a
										style="color: #5de0ef; font-size: 10px; text-decoration: none;"
										id="afonline">在线:0台</a></li>
									<li style="color: #ff1212;"><a
										style="color: #5de0ef; font-size: 10px; text-decoration: none;"
										id="afoffline">离线:0台</a></li>
								</ul>
							</span>
						</div>
					</div>
					<div class="col-xs-2" id="right_container1">
						<div class="right_container_count" id="right_container_count_sta">
							<span>
								<ul>
									<li style="color: #42f603;"><a
										style="color: #5de0ef; font-size: 10px; text-decoration: none;"
										id="staonline">在线:1台</a></li>
									<li style="color: #ff1212;"><a
										style="color: #5de0ef; font-size: 10px; text-decoration: none;"
										id="staoffline">离线:0台</a></li>
								</ul>
							</span>
						</div>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-12">
						<table cellpadding="0" cellspacing="0"
							id="right_container_bottom_title">
							<tr>
								<td><img alt="" src="../../images/zscrd/kuangjia/dot.png"></td>
								<td
									style="font-size: 14px; font-weight: bold; padding-left: 15px;">全市全网IDC安全管理</td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="row" id="right_container_bottom_count">
					<div class="col-xs-3">
						<table>
							<tr>
								<td><img src="../../images/zscrd/kuangjia/URL.png" alt="" /></td>
								<td>&nbsp;&nbsp;未备案URL/IP总数 :&nbsp;</td>
								<td id="allcount_monthi"></td>
								<td id="allcount_monthj"></td>
								<td id="allcount_monthk"></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-3">
						<table>
							<tr>
								<td><img src="../../images/zscrd/kuangjia/find.png" alt="" /></td>
								<td>&nbsp;&nbsp;当月累积发现安全事件 :&nbsp;</td>
								<td id="allcount_month2"></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-3">
						<table>
							<tr>
								<td><img src="../../images/zscrd/kuangjia/fin.png" alt="" /></td>
								<td>&nbsp;&nbsp;当月累积已处理安全事件 :&nbsp;</td>
								<td id="allcount_month3"></td>
							</tr>
						</table>
					</div>
					<div class="col-xs-3">
						<table>
							<tr>
								<td><img src="../../images/zscrd/kuangjia/not.png" alt="" /></td>
								<td>&nbsp;&nbsp;当月累积未处理安全事件 :&nbsp;</td>
								<td id="allcount_month4"></td>
							</tr>
						</table>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="row" id="echarts_five">
					<div class="col-xs-6" id="echarts">
						<div class="echarts_title">
							<ul>
								<li>整改业务分布</li>
								<li>时间范围:
								<input class="datainp" style="width:140px;" id="date_start" type="text">
								至<input class="datainp" style="width:140px;" id="date_end" type="text">
								</li>
							</ul>
						</div>
						<div class="echarts_barandpie">
							<div class="echarts_pie" id="echarts_pie"></div>
							<div class="echarts_bar">
								<div class="echarts_bar_fenbu">整改原因分布</div>
								<div class="echarts_bar_main" id="echarts_bar_data"></div>
							</div>
						</div>
						<div class="echarts_bottom">
							<ul>
								<li><span class="dot1"></span>已整改</li>
								<li><span class="dot2"></span>整改中</li>
								<li><span class="dot3"></span>待整改</li>
							</ul>
						</div>
					</div>
					<div class="col-xs-6" id="echarts">
						<div class="echarts_title">
							<ul>
								<li>关停业务分布</li>
								<li>时间范围:
								<input class="datainp" style="width:140px;" id="date_start_gt" type="text" readonly>
								至<input class="datainp" style="width:140px;" id="date_end_gt" type="text" readonly>
								</li>
							</ul>
						</div>
						<div class="echarts_barandpie">
							<div class="echarts_pie" id="echarts_pie_gt"></div>
							<div class="echarts_bar">
								<div class="echarts_bar_fenbu">关停原因分布</div>
								<div class="echarts_bar_main" id="echarts_bar_gt_data"></div>
							</div>
						</div>
						<div class="echarts_bottom">
							<ul>
								<li><span class="dot1"></span>已关停</li>
								<li><span class="dot2"></span>申请恢复</li>
								<li><span class="dot3"></span>审核中</li>
							</ul>
						</div>
					</div>
				</div>
			</div>
			<div class="container-fluid">
				<div class="row">
					<div class="col-xs-6" id="echarts_bar_wlaqsj">
						<div id="echarts_bar_wlaqsj_top">
							<div id="echarts_bar_wlaqsj_left">网络安全事件</div>
							<div id="echarts_bar_wlaqsj_right">
								<table>
									<tr>
										<td>查看:</td>
										<td><select id="ztlj">
												<option value="1">总体累积</option>
												<option value="2">当前状态</option>
										</select></td>
									</tr>
								</table>
							</div>
						</div>
						<div id="echarts_bar_1"></div>
						<div id="echarts_bar_3"></div>
					</div>
					<div class="col-xs-6" id="echarts_bar_wxsjTop">
						<div id="echarts_bar_wlaqsj_top">
							<div id="echarts_bar_wlaqsj_left">威胁事件Top10</div>
							<div id="echarts_bar_wlaqsj_right">
								<table>
									<tr>
										<td>查看:</td>
										<td><select id="wxsj">
												<option value="1">总体累积</option>
												<option value="2">当前状态</option>
										</select></td>
									</tr>
								</table>
							</div>
						</div>
						<div id="echarts_bar_2"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="serviceIp" name="serviceIp" value="<%=request.getServerName()+":"+request.getServerPort()%>">
</body>
<script type="text/javascript">

	var SMMS_EVENT_STAT, SMMS_EVENT_TOP10, SMMS_BASE_STAT,SMMS_EVENT_STAT_CURRENT, SMMS_EVENT_TOP10_CURRENT;
	var IDC_NAME = [], INVADE = [], HACKER = [], BLACK_PROFIT = [], INSIDER_ATTACK = [], OVER_WALL = [], PROXY = [], VPN = [];
	var eventTop10 = [], eventTop10_current = [];
	var idc_count,sta_offline=0, sta_online=0,month_not_deal, af_online, af_offline,room_count, 
	month_dealt, month_event,all_SQHF,all_SHZ,bussy_count,ac_online, ac_offline, no_case;
	var IDC_NAME_CURRENT = [], INVADE_CURRENT = [], HACKER_CURRENT = [], BLACK_PROFIT_CURRENT = [], INSIDER_ATTACK_CURRENT = [], OVER_WALL_CURRENT = [], PROXY_CURRENT = [], VPN_CURRENT = [];
	var dzg=[];
	var yzg=[];
	var zgz=[];
	var ygt=[];
	var sqgt=[];
	var spz=[];
	var ec1,ec2;
	var time1,time2,time3,time4;
	var ip_addr;
	
	var color = [ '#CCCFFF', '#6699CC', '#3399CC', '#8faadc', '#0099CC ',
	  			'#006699', '#00b0f0', '#99CCFF', '#99CCCC', '#336699' ];
	var color1 = [  'rgb(240,0,28)', 'rgb(255,180,0)','rgb(0,179,54)' ];
	
	$(function() {
		ip_addr = $("#serviceIp").val();
		showTime();
		ajaxData1(ip_addr);
		baseload();
		aqsjload();
		wxsjload(eventTop10);
		time1=$("#date_start").val();
		time2=$("#date_end").val();
		ajaxData2(time1,time2,ip_addr);
		YWZGFB();//业务整改分布
		
		time3=$("#date_start_gt").val();
		time4=$("#date_end_gt").val();
		ajaxData3(time3,time4,ip_addr);
		YWGTFB();//业务关停分布
	});

	
	//基础数据加载
	function baseload(){
		$(idc).text(idc_count + "个");
		$(roomidc).text(room_count + "个");
		$(ywts).text(bussy_count + "条");
		$(aconline).text("在线:"+ac_online+"台");
		$(acoffline).text("离线:"+ac_offline+"台");
		$(afonline).text("在线:"+af_online+"台");
		$(afoffline).text("离线:"+af_offline+"台");
// 		$(staonline).text("在线:"+sta_online+"台");
// 		$(staoffline).text("离线:"+sta_offline+"台");

		tianRuCount(allcount_monthi, allcount_monthj, allcount_monthk, no_case);
		$(allcount_month2).text(month_event);
		$(allcount_month3).text(month_dealt);
		$(allcount_month4).text(month_not_deal);
	}
	
	//业务整改分布
	function YWZGFB(){
		var cin1 = [ {
			name : '待整改',
			value : all_DZG
		}, 
		{
			name : '整改中',
			value : all_ZGZ
		}, 
		{
			name : '已整改',
			value : all_YZG
		} ];		
		var count1 = all_ZGZ + all_YZG + all_DZG;
		pie_xiugai('合计' + count1 + '个', cin1, 'echarts_pie', color1);//环图
		var event_name=['未备案整改','安全事件整改','涉黄赌毒','涉政稳恐'];
		Internet_bar(event_name, dzg, zgz, yzg,'echarts_bar_data');//柱状图
	}
	
	//业务关停分布
	function YWGTFB(){
		var cin2 = [ {
			name : '审核中',
			value : all_SHZ
		}, {
			name : '申请恢复',
			value : all_SQHF
		}, {
			name : '已关停',
			value : all_YGT
		} ];
		var count2 = all_YGT+all_SQHF+all_SHZ;
		pie_xiugai('合计' + count2 + '个', cin2, 'echarts_pie_gt', color1);
		var event_name2=['安全事件','涉黄赌毒','涉政稳恐'];
		Internet_bar2(event_name2, ygt, sqgt, spz,'echarts_bar_gt_data');
	}
	
	
	function aqsjload(){//默认加载总体累积
		var height1 = 80;
		for (var i = 1; i <= IDC_NAME.length; i++) {
			height1 += 20;
		}
		$("#echarts_bar_1").height(height1);
		ec1 = echarts.init(document.getElementById("echarts_bar_1"));
		ec2 = echarts.init(document.getElementById("echarts_bar_3"));
		Internet_safe(IDC_NAME, INVADE, HACKER, BLACK_PROFIT, INSIDER_ATTACK,OVER_WALL, PROXY, VPN, ec1);
	}
	
	$("#ztlj").change(//网络安全事件切换事件
			function() {
				var val = $(this).val();
				if (val == 1) {//累积状态
					$("#echarts_bar_3").css("display","none");
					$("#echarts_bar_1").css("display","block");
					aqsjload();
				}
				if (val == 2) {//当前状态
					$("#echarts_bar_1").css("display","none");
					$("#echarts_bar_3").css("display","block");
					var height2 = 80;
					for (var i = 1; i <= IDC_NAME_CURRENT.length; i++) {
						height2 += 20;
					}
					$("#echarts_bar_3").height(height2);
					ec2 = echarts.init(document.getElementById("echarts_bar_3"));
					Internet_safe(IDC_NAME_CURRENT, INVADE_CURRENT,HACKER_CURRENT, BLACK_PROFIT_CURRENT,INSIDER_ATTACK_CURRENT, OVER_WALL_CURRENT,PROXY_CURRENT, VPN_CURRENT, ec2);
				}
			});
	
	//威胁事件Top10
	function wxsjload(message){
		var height1 = 80;
		for (var i = 1; i <= message.length; i++) {
			height1 += 20;
		}
		$("#echarts_bar_2").height(height1);
		ThreatEvent(message, 'echarts_bar_2', color);
	}
	
	$("#wxsj").change(function() {//威胁事件Top10切换
		var val = $(this).val();
		if (val == 1) {//总体累积
			wxsjload(eventTop10);
		}
		if (val == 2) {//当前状态
			wxsjload(eventTop10_current);
		}
	});
		//通用环形图-修改后
		/*
		 * name:标题
		 * count:图形中间的文字描述
		 * CinArray:对应的数值和名称
		 * divId:位置ID
		 * colorL:颜色
		 */
		 function pie_xiugai(count, CinArray, divId, colorL) {
				// 基于准备好的dom，初始化echarts实例
				var NameArray = CinArray.map(function(cinarray) {
					return cinarray.name;
				}), DataArray = CinArray.map(function(cinarray) {
					return cinarray.value;
				});
				var tag=1;
				for(var i=0;i<DataArray.length;i++){if(DataArray[i]!=0){tag=0;break;}}
				if(tag==1){
					colorL=['rgb(220,220,220)','rgb(220,220,220)','rgb(220,220,220)','rgb(220,220,220)','rgb(220,220,220)','rgb(220,220,220)']
				}
				var myChart = echarts.init(document.getElementById(divId));

				// 指定图表的配置项和数据
				option = {
					title : {
						text : '',
						x : 'center',
						subtext : count,
						itemGap : 85,
						textStyle : {
							fontSize : 14
						}
					},
					tooltip : {
						show : 'true',
						trigger : 'item',
						formatter : "{a} <br/>{b}: {c} ({d}%)"
					},
					series : [ {
						name : '',
						type : 'pie',
						radius : [ '45%', '63%' ],
						center : [ '50%', '55%' ],
						avoidLabelOverlap : false,
						hoverAnimation : false,
						label : {
							normal : {
								show : false,
								position : 'center'
							},
							emphasis : {
								show : false,
								textStyle : {
									fontSize : '20',
									fontWeight : 'bold'
								}
							}
						},
						labelLine : {
							normal : {
								show : false
							}
						},
						data : CinArray,
						itemStyle : {
							normal : {
								//柱状图颜色  
								color : function(params) {
									// 颜色列表  
									var colorList = colorL;
									//返回颜色  
									return colorList[params.dataIndex];
								},

							},
							emphasis : {

							}
						}
					} ]
				};

				// 使用刚指定的配置项和数据显示图表。
				myChart.setOption(option);
				window.addEventListener("resize",function(){
				    myChart.resize();
				});
	}
	
	//柱状图-倒转修改后
	// 基于准备好的dom，初始化echarts实例
	function Internet_bar(name, a1, a2, a3,divId) {
		var colorL = ['rgb(240,0,28)', 'rgb(255,180,0)','rgb(0,179,54)'];
		var IDC = name;
		var myChart = echarts.init(document.getElementById(divId));

		// 指定图表的配置项和数据
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				top : '20',
				containLabel : true
			},
			xAxis : {
				type : 'value',
				show : false
				//inverse:true
			},
			yAxis : {
				type : 'category',
				axisLabel : {
					
					textStyle : {
						color : 'rgb(119 ,136 ,153)'
					}
				},
				axisLine:{
					//onZero:false
				},
				data : IDC
			},
			series : [ {
				name : '待整改',
				type : 'bar',
				barWidth : 12,
				barCategoryGap : '15%',
				boundaryGap : true,
				stack : '总量',
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[0]
					},
				},

				data : a1
			}, {
				name : '整改中',
				type : 'bar',
				stack : '总量',
				barWidth : 12,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[1]
					},
				},
				data : a2
			}, {
				name : '已整改',
				type : 'bar',
				stack : '总量',
				barWidth : 12,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[2]
					},
				},
				data : a3
			}]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
		window.addEventListener("resize",function(){
		    myChart.resize();
		});
	}
	
	// 基于准备好的dom，初始化echarts实例
	// 基于准备好的dom，初始化echarts实例
	function Internet_bar2(name, a1, a2, a3,divId) {
		var colorL = [ 'rgb(0,179,54)', 'rgb(255,180,0)','rgb(240,0,28)'];
		var IDC = name;
		var myChart = echarts.init(document.getElementById(divId));

		// 指定图表的配置项和数据
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				top : '20',
				containLabel : true
			},
			xAxis : {
				type : 'value',
				show : false
				//inverse:true
			},
			yAxis : {
				type : 'category',
				axisLabel : {
					
					textStyle : {
						color : 'rgb(119 ,136 ,153)'
					}
				},
				axisLine:{
					//onZero:false
				},
				data : IDC
			},
			series : [ {
				name : '已关停',
				type : 'bar',
				barWidth : 12,
				barCategoryGap : '15%',
				boundaryGap : true,
				stack : '总量',
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[0]
					},
				},

				data : a1
			}, {
				name : '申请恢复',
				type : 'bar',
				stack : '总量',
				barWidth : 12,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[1]
					},
				},
				data : a2
			}, {
				name : '审核中',
				type : 'bar',
				stack : '总量',
				barWidth : 12,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[2]
					},
				},
				data : a3
			}]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
		window.addEventListener("resize",function(){
		    myChart.resize();
		});
	}
	
	//网络安全事件
	// 基于准备好的dom，初始化echarts实例
	function Internet_safe(idc, a1, a2, a3, a4, a5, a6, a7, ec) {
		var colorL = [ 'rgb(122 ,139 ,139)', 'rgb(0, 104, 139)',
				'rgb(0, 0, 139)', 'rgb(85, 26, 139)', 'rgb(24 ,116 ,205)',
				'rgb(0 ,0 ,255)', 'rgb(70 ,130 ,180)', 'rgb(0 ,139 ,139)',
				'rgb(0, 255, 255)', 'rgb(78 ,238 ,148)', 'rgb(0 ,191 ,255)',
				'rgb(46 ,139 ,87)' ];

		var element = [ '遭受入侵', '黑客控制', '黑产牟利', '内部攻击', '翻墙软件', '代理工具', 'VPN' ];
		var IDC = idc;
		var Element = element;
		var myChart = ec;

		// 指定图表的配置项和数据
		option = {
			tooltip : {
				trigger : 'axis',
				axisPointer : { // 坐标轴指示器，坐标轴触发有效
					type : 'shadow' // 默认为直线，可选为：'line' | 'shadow'
				}
			},
			legend : {
				data : Element,

				// icon: 'circle',
				align : 'auto',
				itemGap : 6,
				itemWidth : 15,
				itemHeight : 8,
				top:'10',
				align : 'left',
				selectedMode : true,
				formatter : function(v) {
					return v;
				},
				textStyle : {
					fontSize : 14,
					color : '#666'
				}
			},
			grid : {
				left : '3%',
				right : '4%',
				bottom : '3%',
				top : '60',
				containLabel : true
			},
			xAxis : {
				type : 'value',
				show : false
				//inverse:true
			},
			yAxis : {
				type : 'category',
				//position:'right',
				//silent:true,
				// axisTick:{
				// 	show:false
				// },
				axisLabel : {
					
					textStyle : {
						color : 'rgb(119 ,136 ,153)'
					}
				},
				axisLine:{
					//onZero:false
				},
				data : IDC
			},
			series : [ {
				name : '遭受入侵',
				type : 'bar',
				barWidth : 10,
				barCategoryGap : '15%',
				boundaryGap : true,
				stack : '总量',
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[0]
					},
				},

				data : a1
			}, {
				name : '黑客控制',
				type : 'bar',
				stack : '总量',
				barWidth : 10,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[1]
					},
				},
				data : a2
			}, {
				name : '黑产牟利',
				type : 'bar',
				stack : '总量',
				barWidth : 10,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[2]
					},
				},
				data : a3
			}, {
				name : '内部攻击',
				type : 'bar',
				stack : '总量',
				barWidth : 10,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[3]
					},
				},
				data : a4
			}, {
				name : '翻墙软件',
				type : 'bar',
				stack : '总量',
				barWidth : 10,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[4]
					},
				},
				data : a5
			}, {
				name : '代理工具',
				type : 'bar',
				stack : '总量',
				barWidth : 10,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[5]
					},
				},
				data : a6
			}, {
				name : 'VPN',
				type : 'bar',
				stack : '总量',
				barWidth : 10,
				label : {
					normal : {
						show : false,
						position : 'insideRight'
					}
				},
				itemStyle : {
					normal : {
						color : colorL[6]
					},
				},
				data : a7,
			} ]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
	    myChart.resize();
	}
	$(window).resize(function () {
        ec1.resize();
        ec2.resize();
        
    });
    
	//威胁事件top10
	function ThreatEvent(dataArray, divname, colorL) {
		var name = dataArray.map(function(data) {
			return data.name;
		}), data = dataArray.map(function(data) {
			return data.value;
		});
		var myChart = echarts.init(document.getElementById(divname));
		option = {

			tooltip : {
				formatter : '{b} ({c})'
			},
			legend : {
				data : ''
			},
			grid : {
				left : '2%',
				right : '4%',
				bottom : '3%',
				width : '95%',
				height : '90%',

				containLabel : true
			},
			xAxis : {
				type : 'value',
				boundaryGap : [ 0, 0.01 ],
				position :'top'
			},
			yAxis : {
				type : 'category',
				data : name
			},
			series : [ {
				name : name,
				type : 'bar',
				barWidth : 10,
				data : data,
				label : {
					normal : {
						show : true,
						position : "right",
						textStyle : {
							color : "#9EA7C4"
						}
					}
				},
				itemStyle : {
					normal : {
						//柱状图颜色  
						color : function(params) {
							// 颜色列表  
							var colorList = colorL;
							//返回颜色  
							return colorList[params.dataIndex];
						},

					}
				}
			} ]
		};

		// 使用刚指定的配置项和数据显示图表。
		myChart.setOption(option);
		window.addEventListener("resize",function(){
		    myChart.resize();
		});
	};

	function tianRuCount(divId1, divId2, divId3, num) {
		var num = (num || 0).toString(), result = '';
		for (var i = 0; i < 3; i++) {
			if (i == 0) {
				if (num.length > 4) {
					result = ' ' + num.slice(-4) + result;
					$(divId3).text(result);
					num = num.slice(0, num.length - 4);
				}else{
					$(divId3).text(num);
					num="";
				}
			}
			if (i == 1 && num!="") {
				if (num.length > 4) {
					result = ' ' + num.slice(-4);
					$(divId2).text(result);
					num = num.slice(0, num.length - 4);
				}else{
					$(divId2).text(num);
					num="";
				}
			}
			if (i == 2 && num!="") {
				$(divId1).text(num);
			}
		}
	};

	var compare = function (obj1, obj2) {//排序
	    var val1 = obj1.value;
	    var val2 = obj2.value;
	    if (val1 < val2) {
	        return -1;
	    } else if (val1 > val2) {
	        return 1;
	    } else {
	        return 0;
	    }            
	};
	function add(name,value){this.name=name;this.value=value;}
	function ajaxData1(ipaddr){
		var url = "http://"+ipaddr+"/SMMS/SMMS/SmmsEventMainBiz/getStatJson";
		$.ajax({
            type:"POST",
            async:false,
            //url:"http://localhost:8080/test/data2.json",
 			url:url,
            dataType:"json",
            success:function(data){
            	 SMMS_EVENT_STAT=eval(data.SMMS_EVENT_STAT_COUNT);
                 SMMS_EVENT_TOP10=eval(data.SMMS_EVENT_TOP10_COUNT);
                 SMMS_BASE_STAT=eval(data.SMMS_BASE_STAT);
                 
                 SMMS_EVENT_STAT_CURRENT=eval(data.SMMS_EVENT_STAT_CURRENT);
                 SMMS_EVENT_TOP10_CURRENT=eval(data.SMMS_EVENT_TOP10_CURRENT);
                 $.each(SMMS_EVENT_STAT,function(i,item){//安全事件—总体累积
                	
                	var name = item.IDC_NAME;
                	var room = item.ROOM_IDX;
                	var long_name =name+room+"机房";
                  	IDC_NAME.push(long_name);
                  	INVADE.push(item.INVADE);
                  	HACKER.push(item.HACKER);
                  	BLACK_PROFIT.push(item.BLACK_PROFIT);
                  	INSIDER_ATTACK.push(item.INSIDER_ATTACK);
                  	OVER_WALL.push(item.OVER_WALL);
                  	PROXY.push(item.PROXY);
                  	VPN.push(item.VPN);
                  	
                  });
                 $.each(SMMS_EVENT_STAT_CURRENT,function(i,item){//安全事件—当前累积
                	 
                	 var name = item.IDC_NAME;
                 	 var room = item.ROOM_IDX;
                 	 var long_name =name+room+"机房";
                     IDC_NAME_CURRENT.push(long_name);
                     INVADE_CURRENT.push(item.INVADE);
                     HACKER_CURRENT.push(item.HACKER);
                     BLACK_PROFIT_CURRENT.push(item.BLACK_PROFIT);
                     INSIDER_ATTACK_CURRENT.push(item.INSIDER_ATTACK);
                     OVER_WALL_CURRENT.push(item.OVER_WALL);
                     PROXY_CURRENT.push(item.PROXY);
                     VPN_CURRENT.push(item.VPN);
                     
                 });
                 
                 $.each(SMMS_EVENT_TOP10,function(i,item){
                 	eventTop10[eventTop10.length] = new add(item.EVENT_NAME,item.EVENT_COUNT); 
                 }); 
                 $.each(SMMS_EVENT_TOP10_CURRENT,function(i,item){
                  	eventTop10_current[eventTop10_current.length] = new add(item.EVENT_NAME,item.EVENT_COUNT); 
                  });
                 
                 eventTop10=eventTop10.sort(compare);
                 eventTop10_current=eventTop10_current.sort(compare);
                 //基础数据处理
                 dealData();
            },
            error:function(data){
                alert("Get Data Failed");
            }
        });
	}
	function ajaxData2(time1,time2,ipaddr){
		var url = "http://"+ipaddr+"/SMMS/SMMS/SmmsEventMainBiz/getZgywJson?beginTime="+time1+"&endTime="+time2;
// 		alert(url);
		$.ajax({
            type:"POST",
            async:false,
			url:url,
			//url:"http://localhost:8080/test/data3.json",
            dataType:"json",
            success:function(data){
            	var AQSJYZG = data.AQSJ.AQSJYZG;
            	var AQSJZGZ = data.AQSJ.AQSJZGZ;
            	var AQSJDZG = data.AQSJ.AQSJDZG;
            	
            	var WBAYZG = data.WBA.WBAYZG;
            	var WBAZGZ = data.WBA.WBAJZGZ;
            	var WBADZG = data.WBA.WBAJDZG;
            	
            	var SHDUYZG = data.SHDU.SS2YZG;
            	var SHDUZGZ = data.SHDU.SS2ZGZ;
            	var SHDUDZG = data.SHDU.SS2DZG;
            	
            	var SZWKYZG = data.SZWK.SS1YZG;
            	var SZWKZGZ = data.SZWK.SS1ZGZ;
            	var SZWKDZG = data.SZWK.SS1DZG;
            	all_DZG=[];
            	all_ZGZ=[];
            	all_YZG=[];
            	dzg=[];
            	yzg=[];
            	zgz=[];
            	all_DZG=AQSJDZG;
            	all_ZGZ=AQSJZGZ;
            	all_YZG=AQSJYZG;
            	dzg.push(WBADZG);
            	dzg.push(AQSJDZG);
            	dzg.push(SHDUDZG);
            	dzg.push(SZWKDZG);
            	 
            	zgz.push(WBAZGZ);
            	zgz.push(AQSJZGZ);
            	zgz.push(SHDUZGZ);
            	zgz.push(SZWKZGZ);
            	 
            	yzg.push(WBAYZG);
            	yzg.push(AQSJYZG);
            	yzg.push(SHDUYZG);
            	yzg.push(SZWKYZG);
            },
            error:function(data){
                alert("Get Data Failed");
            }
        });
	}
	function ajaxData3(time3,time4,ipaddr){
		var url = "http://"+ipaddr+"/SMMS/SMMS/SmmsEventMainBiz/getGtywJson?beginTime="+time3+"&endTime="+time4;
// 		alert(url);
		$.ajax({
            type:"POST",
            async:false,
			url:url,
			//url:"http://localhost:8080/test/data4.json",
            dataType:"json",
            success:function(data){
            	var AQSJYGT = data.AQSJ.SS2YGT;
            	var AQSJSQHF = data.AQSJ.AQSJSQHF;
            	var AQSJSHZ = data.AQSJ.AQSJSHZ;
            	
            	var SHDUYGT = data.SHDU.SS2YGT;
            	var SHDUSQHF = data.SHDU.SS2SQHF;
            	var SHDUSHZ = data.SHDU.SS2SHZ;
            	
            	var SZWKYGT = data.SZWK.SS1YGT;
            	var SZWKSQHF = data.SZWK.SS1SQHF;
            	var SZWKSHZ = data.SZWK.SS1SHZ;
            	 
            	all_YGT=AQSJYGT;
            	all_SQHF=AQSJSQHF;
            	all_SHZ=AQSJSHZ;
            	ygt=[];
            	sqgt=[];
            	spz=[];
        		ygt.push(AQSJYGT);
        		ygt.push(SHDUYGT);
        		ygt.push(SZWKYGT);
            	 
        		sqgt.push(AQSJSQHF);
        		sqgt.push(SHDUSQHF);
        		sqgt.push(SZWKSQHF);
            	 
        		spz.push(AQSJSHZ);
        		spz.push(SHDUSHZ);
        		spz.push(SZWKSHZ);
            },
            error:function(data){
                alert("Get Data Failed");
            }
        });
	}

	function dealData() {
		idc_count = (SMMS_BASE_STAT.IDC_COUNT==''||SMMS_BASE_STAT.IDC_COUNT==undefined)?0:SMMS_BASE_STAT.IDC_COUNT;
		room_count = (SMMS_BASE_STAT.ROOM_COUNT==''||SMMS_BASE_STAT.ROOM_COUNT==undefined)?0:SMMS_BASE_STAT.ROOM_COUNT;
		bussy_count = (SMMS_BASE_STAT.BUSSY_COUNT==''||SMMS_BASE_STAT.BUSSY_COUNT==undefined)?0:SMMS_BASE_STAT.BUSSY_COUNT;
		ac_online = (SMMS_BASE_STAT.AC_ONLINE==''||SMMS_BASE_STAT.AC_ONLINE==undefined)?0:SMMS_BASE_STAT.AC_ONLINE;
		ac_offline = (SMMS_BASE_STAT.AC_OFFLINE==''||SMMS_BASE_STAT.AC_OFFLINE==undefined)?0:SMMS_BASE_STAT.AC_OFFLINE;
		af_online = (SMMS_BASE_STAT.AF_ONLINE==''||SMMS_BASE_STAT.AF_ONLINE==undefined)?0:SMMS_BASE_STAT.AF_ONLINE;
		af_offline = (SMMS_BASE_STAT.AF_OFFLINE==''||SMMS_BASE_STAT.AF_OFFLINE==undefined)?0:SMMS_BASE_STAT.AF_OFFLINE;
		sta_offline = (SMMS_BASE_STAT.STA_OFFLINE==''||SMMS_BASE_STAT.STA_OFFLINE==undefined)?0:SMMS_BASE_STAT.STA_OFFLINE;
		sta_online = (SMMS_BASE_STAT.STA_ONLINE==''||SMMS_BASE_STAT.STA_ONLINE==undefined)?0:SMMS_BASE_STAT.STA_ONLINE;

		no_case = (SMMS_BASE_STAT.NO_CASE==''||SMMS_BASE_STAT.NO_CASE==undefined)?0:SMMS_BASE_STAT.NO_CASE;
		month_event = (SMMS_BASE_STAT.MONTH_EVENT==''||SMMS_BASE_STAT.MONTH_EVENT==undefined)?0:SMMS_BASE_STAT.MONTH_EVENT;
		month_not_deal = (SMMS_BASE_STAT.MONTH_NOT_DEAL==''||SMMS_BASE_STAT.MONTH_NOT_DEAL==undefined)?0:SMMS_BASE_STAT.MONTH_NOT_DEAL;
		month_dealt = (SMMS_BASE_STAT.MONTH_DEALT==''||SMMS_BASE_STAT.MONTH_DEALT==undefined)?0:SMMS_BASE_STAT.MONTH_DEALT;
		
	}
	function showTime(){
		$.jeDate("#date_start",{
			format: 'YYYY-MM-DD hh:mm:ss',
            isinitVal:true,
            initDate:[{DD:"-7"},true],   //初始化日期加提前一周
            isTime:true,
            minDate:"2014-09-19 00:00:00",
            maxDate:$.nowDate(0),
            clearfun:function(elem, val) {
            	time1=val;
            	time2=$("#date_end").val();
            	changeTime1(time1,time2);
            },
            okfun:function(obj){
            	time1=obj.val;
            	time2=$("#date_end").val();
            	changeTime1(time1,time2);
           	}
        });
		$.jeDate("#date_end",{
			format: 'YYYY-MM-DD hh:mm:ss',
            isinitVal:true,
            isTime:true,
            minDate:"2014-09-19 00:00:00",
            maxDate:$.nowDate(0),
            okfun:function(obj){
            	time1=$("#date_start").val();
            	time2=obj.val;
            	changeTime1(time1,time2);
           	},
           	clearfun:function(elem, val) {
           		time1=$("#date_start").val();
            	time2=val;
            	changeTime1(time1,time2);
            }
        });
		$.jeDate("#date_start_gt",{
			format: 'YYYY-MM-DD hh:mm:ss',
            isinitVal:true,
            initDate:[{DD:"-7"},true],   //初始化日期加提前一周
            isTime:true,
            minDate:"2014-09-19 00:00:00",
//             minDate: $.nowDate({MM:"-1"}),
            maxDate:$.nowDate(0),
            okfun:function(obj){
            	time3=obj.val;
            	time4=$("#date_end_gt").val();
            	changeTime2(time3,time4);
            },
           	clearfun:function(elem, val) {
           		time3=val;
            	time4=$("#date_end_gt").val();
            	changeTime2(time3,time4);
            }
        });
		$.jeDate("#date_end_gt",{
			format: 'YYYY-MM-DD hh:mm:ss',
            isinitVal:true,
            isTime:true,
            minDate:"2014-09-19 00:00:00",
            maxDate:$.nowDate(0),
            okfun:function(obj){
            	time3=$("#date_start_gt").val();
            	time4=obj.val;
            	changeTime2(time3,time4);
            },
            clearfun:function(elem, val) {
            	time3=$("#date_start_gt").val();
            	time4=val;
            	changeTime2(time3,time4);
            }
        });
		
	}
	function changeTime1(time1,time2){
		var date = new Date();
		var nowTime = FormatDate(date);
		var startTime = FormatDate(time1);
		var endTime = FormatDate(time2);
		var now = new Date(nowTime);
		var start = new Date(startTime);
		var end = new Date(endTime);
		var nowTimestamp = now.getTime();
		var startTimestamp = start.getTime();
		var endTimestamp = end.getTime();
		if(startTimestamp>endTimestamp){
			alert("开始时间不能大于结束时间！请重新选择");
		}else if(nowTimestamp<endTimestamp){
			alert("结束时间不能大于当前时间！请重新选择");
		}else if(time1==undefined){
			alert("时间不能为空!");
		}else if(time2==undefined){
			alert("时间不能为空!");
		}else{
			ajaxData2(time1,time2,ip_addr);
			YWZGFB();//业务整改分布
		}
	}
	function changeTime2(time3,time4){
		var date = new Date();
		var nowTime = FormatDate(date);
		var startTime = FormatDate(time3);
		var endTime = FormatDate(time4);
		var now = new Date(nowTime);
		var start = new Date(startTime);
		var end = new Date(endTime);
		var nowTimestamp = now.getTime();
		var startTimestamp = start.getTime();
		var endTimestamp = end.getTime();
		if(startTimestamp>endTimestamp){
			alert("开始时间不能大于结束时间！请重新选择");
		}else if(nowTimestamp<endTimestamp){
			alert("结束时间不能大于当前时间！请重新选择");
		}else if(time3==undefined){
			alert("时间不能为空!");
		}else if(time4==undefined){
			alert("时间不能为空!");
		}else{
			ajaxData3(time3,time4,ip_addr);
			YWGTFB();//业务关停分布
		}
	}
	function FormatDate (strTime) {
	    var date = new Date(strTime);
	    return date.getFullYear()+"/"+(date.getMonth()+1)+"/"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
	}
</script>
</html>