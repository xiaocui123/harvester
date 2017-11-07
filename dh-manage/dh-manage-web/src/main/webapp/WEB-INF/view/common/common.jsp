<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.Date"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->

    <link rel="icon" href="${ctx}/resource/images/ico/favicons.ico" type="image/x-icon" />
    <link rel="shortcut icon" href="${ctx}/resource/images/ico/favicons.ico" type="image/x-icon" />
    <!-- 上面两个是图标 -->


    <!-- Bootstrap -->
    <link href="${ctx}/resource/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
    <link href="${ctx}/resource/assets/bootstrap-datetimepicker/css/bootstrap-datetimepicker.min.css" rel="stylesheet">
    <link href="${ctx}/resource/assets/bootstrapvalidator/css/bootstrapValidator.min.css" rel="stylesheet">
	<link href="${ctx}/resource/assets/bootstrap-select/css/bootstrap-select.css" rel="stylesheet">
    <!-- 布局结构css layout.css -->

    <link rel="stylesheet" href="${ctx}/resource/css/layout.css">

    <!-- ie8兼容 -->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="${ctx}/resource/assets/html5shiv.min.js"></script>
      <script src="${ctx}/resource/assets/respond.min.js"></script>
    <![endif]-->
   
    <!-- old easyUI -->
	<script type="text/javascript" src="${ctx}/old_resource/comm/jquery-1.10.2.min.js"></script>
	<!-- jQuery 动画加强 -->
	<script src="${ctx}/resource/assets/jquery.easing.1.3.js"></script>
	<script type="text/javascript" src="${ctx}/old_resource/extenstion/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="${ctx}/old_resource/comm/common.js"></script>
	<script type="text/javascript" src="${ctx}/old_resource/extenstion/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="${ctx}/old_resource/comm/validate.js"></script>
	<script>
	//解决IE不支持indexOf的属性方法
	if (!Array.prototype.indexOf){  
        Array.prototype.indexOf = function(elt ){  
        var len = this.length >>> 0;  
        var from = Number(arguments[1]) || 0;  
        from = (from < 0)  
             ? Math.ceil(from)  
             : Math.floor(from);  
        if (from < 0)  
          from += len;  
        for (; from < len; from++)  
        {  
          if (from in this &&  
              this[from] === elt)  
            return from;  
        }  
        return -1;  
      };  
    }  
	</script>
	<script type="text/javascript">
		var dqcookie="${isAlarm}";//获取用户角色判断
	</script>
	<!-- jQuery -->
	<%-- <script src="${ctx}/resource/assets/jquery.min.js"></script> --%>
	<!-- bootstrap 源文件 -->
	<script src="${ctx}/resource/assets/bootstrap/js/bootstrap.js"></script>
	
	<script src="${ctx}/resource/assets/bootstrapvalidator/js/bootstrapValidator.min.js"></script>
	<script src="${ctx}/resource/assets/bootstrap-datetimepicker/js/bootstrap-datetimepicker.js"></script>
	<script src="${ctx}/resource/assets/bootstrap-select/js/bootstrap-select.js"></script>
	<!-- jQuery 滚动条美化 -->
	<script src="${ctx}/resource/assets/bootstrap/js/jquery.tinyscrollbar.js"></script>
	<!-- 二次封装bs -->
	<script src="${ctx}/resource/js/comm/hb.js"></script>

	<script src="${ctx}/resource/js/comm/common.js"></script>

	<!-- 首页布局js -->



	<script type="text/javascript">
	    var ctxPath = "${ctx}";
	    var sessionid = '<%=session.getId()%>';
	    var sysdate = '<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>';
	    
	    $(function(){
			$.fn.datetimepicker.dates['en'] = {
					days: ["星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"],
					daysShort: ["周日", "周一", "周二", "周三", "周四", "周五", "周六", "周日"],
					daysMin:  ["日", "一", "二", "三", "四", "五", "六", "日"],
					months: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
					monthsShort: ["一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"],
					today: "今天",
					suffix: [],
					meridiem: ["上午", "下午"]
			};
	    });
	</script>
	<script type="text/javascript">
		var _authFunc='${AuthFunc}';
		var _sysName='${sysName}';
	
		var isIE=!!window.ActiveXObject;
		var isIE6=isIE&&!window.XMLHttpRequest;
		var isIE8=isIE&&!!document.documentMode;
		var isIE7=isIE&&!isIE6&&!isIE8;
	</script>
	
	<!--[if lt IE 9]> 
		<script src="${ctx}/resource/js/comm/html5.js"></script>
	<![endif]-->