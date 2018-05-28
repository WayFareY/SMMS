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
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>事件举证详情</title>
<script src="<%=rootPath%>/scripts/smms/echarts.js"></script>
<script src="<%=rootPath%>/scripts/smms/jquery-3.2.1.js"></script>
<style type="text/css">
* {
	margin: 10;
	padding: 0;
}

#page_border {
	float: left;
	/* display: inline-block; */
	/* width: 100%; */
	/* border: 1px solid red; */
}

.trend {
	font-weight: bold;
}

.data {
	height: 300px;
}
</style>
</head>
<body>
	<div id="page_border">
		<div id="trend" class="trend">访问趋势（次）</div>
		<div id="data" class="data"></div>
		<div id="headline"></div>
		<div id="title"></div>
	</div>
</body>
<script type="text/javascript">
	var headline, title, data;
    var all=${SNAPSHOP};
    var msg=all.SNAPSHOP;
    var json=JSON.parse(msg);
    var jsonTrend=json.trend;
	
	var trendData = jsonTrend[0].data;;

	headline = json.headline;
	title = json.title;

	var timestamp = [];
	var dateTime = [];
	var times = [];
	var trendData_new = [];

	//获取X轴数据
	for (var i = 0; i < trendData.length; i++) {
		var d = trendData[i];
		timestamp.push(d[0]);
	}

	//获取y轴数据
	for (var i = 0; i < trendData.length; i++) {
		var d = trendData[i];
		times.push(d[1]);
	}

	//时间戳转换
	for (var i = 0; i < timestamp.length; i++) {
		var date = new Date(Number(timestamp[i]));//时间戳为10位需*1000，时间戳为13位的话不需乘1000
		Y = date.getFullYear() + '-';
		M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date
				.getMonth() + 1)
				+ '-';
		D = date.getDate();
		dateTime.push(Y + M + D);
	}

	//重组趋势数据
	for (var i = 0; i < dateTime.length; i++) {
		var array = [];
		array.push(dateTime[i]);
		array.push(times[i]);
		trendData_new.push(array);
	}

	//title换行
	for (var i = 0; i < title.length; i++) {
		//title[i] = "&nbsp;&nbsp;&nbsp;&nbsp;" + title[i];
		title[i] = title[i].replace(/&bull;/g, "<br/>&bull;");
		if (title[i + 1] != null) {
			title[i] = title[i] + "<br/>";
		}
	}

	$(function() {
		headline = $("#headline").html(headline);
		title = $("#title").html(title);
		data = $("#data").html(scatter("data"));
	});

	//散点图
	function scatter(divId) {
		var myChart = echarts.init(document.getElementById(divId));
		var option = {
			left : 'left',

			//鼠标移上去的提示
			tooltip : {

			},

			xAxis : {
				type : 'time',
				//坐标轴分割刻度最大为一天
				maxInterval : 3600 * 24 * 1000
			},
			yAxis : {},
			series : [ {
				symbolSize : 15,
				data : trendData_new,
				type : 'scatter'
			} ]
		}
		myChart.setOption(option);
	};

</script>
</html>