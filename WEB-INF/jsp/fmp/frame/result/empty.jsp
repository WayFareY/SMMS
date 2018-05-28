<%@ page contentType="text/html;charset=utf-8" language="java" %>
<%@ taglib prefix="fmp" uri="/WEB-INF/tlds/fmp-tags.tld"%>
<%@ page import="com.zstar.fmp.core.base.FMPContex"%>
<%response.setHeader("Access-Control-Allow-Origin", "*");%>
<%=FMPContex.fmpDecodeUrl((String)request.getAttribute("msg")) %>

