<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link href="${ctx}/js/plugins/bootstraptable/bootstrap-table.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/resource/assets/bootstrap-fileinput/css/fileinput.css" rel="stylesheet" type="text/css"/>
<script>
    var path = "${ctx}";
</script>
<div class="container" style="height: 100%; overflow-y: auto; overflow-x: no">
    <div class="row">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#">定点浮标</a></li>
        </ul>
    </div>
    <div style="margin-top: 10px">
        <div class="input-group col-xs-12 clearfix attachment-list-div">
            <div class="col-xs-12">
                <input id="file-0a" name="file" type="file" multiple class="file-loading">
            </div>
        </div>
    </div>
    <div class="row-fluid">
        <table id="grid"></table>
    </div>
</div>
<script type="text/javascript" src="${ctx}/resource/assets/bootbox/bootbox.js"></script>
<script src="${ctx}/js/plugins/bootstraptable/bootstrap-table.js"></script>
<script src="${ctx}/js/plugins/bootstraptable/locale/bootstrap-table-zh-CN.js"></script>
<script src="${ctx}/resource/assets/bootstrap-fileinput/js/fileinput.js"></script>
<script src="${ctx}/resource/assets/bootstrap-fileinput/js/fileinput_locale_zh.js"/>
<script type="text/javascript" src="${ctx}/resource/js/view/buoy/buoy.js"></script>