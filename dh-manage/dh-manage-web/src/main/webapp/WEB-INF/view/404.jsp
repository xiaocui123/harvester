<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://"
            + request.getServerName() + ":" + request.getServerPort()
            + path + "/";

%>

<!DOCTYPE HTML>
<html>
<head>
    <base href="<%=basePath%>">

    <title>404</title>
    <link href="<%=request.getContextPath()%>/js/plugins/bootstrap/css/bootstrap.min.css"
          rel="stylesheet" type="text/css"/>
    <script type="text/javascript">
        var path = "<%=request.getContextPath()%>";
    </script>
</head>
<style>
    .page-404 {
        text-align: center;
    }

    .page-404 .number {
        position: relative;
        top: 35px;
        display: inline-block;
        letter-spacing: -10px;
        margin-top: 0px;
        margin-bottom: 10px;
        line-height: 128px;
        font-size: 128px;
        font-weight: 300;
        color: #7bbbd6;
        text-align: right;
    }

    .page-404 .details {
        margin-left: 40px;
        display: inline-block;
        padding-top: 0px;
        text-align: left;
    }
</style>
<body>
<!-- BEGIN PAGE CONTENT-->
<div class="row">
    <div class="col-md-12 page-404">
        <div class="number">
            404
        </div>
        <div class="details">
            <h3>找不到页面，请联系管理员！</h3>
            <p>
                We can not find the page you're looking for.<br/>
            </p>
        </div>
    </div>
</div>
<!-- END PAGE CONTENT-->

<script src="<%=request.getContextPath()%>/js/jQuery-core/jquery-1.9.1.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/plugins/bootstrap/js/bootstrap.min.js"
        type="text/javascript"></script>
</body>
</html>
