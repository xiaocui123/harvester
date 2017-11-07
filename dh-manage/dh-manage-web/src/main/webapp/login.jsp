<!--[if IE 8]> <html lang="en" class="ie8 no-js"> <![endif]-->
<!--[if IE 9]> <html lang="en" class="ie9 no-js"> <![endif]-->
<!--[if !IE]><!-->
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE HTML>
<html>
<head>
    <script type="text/javascript">
        if (window.top != window.self) {
            window.top.location.href = "login.jsp";
        }
        var path = "<%=request.getContextPath()%>";
    </script>
    <title>多源海洋数据同一化处理系统</title>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta content="width=device-width, initial-scale=1.0" name="viewport"/>
    <meta http-equiv="Content-type" content="text/html; charset=utf-8">
    <meta content="" name="description"/>
    <meta content="" name="author"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"/>
    <meta name="format-detection" content="telephone=no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="author" content="duanliang, duanliang920.com"/>
    <!--[if lt IE9]>
    <script>
        (function () {
            if (!
                            /*@cc_on!@*/
                            0) return;
            var e = "abbr, article, aside, audio, canvas, datalist, details, dialog, eventsource, figure, footer, header, hgroup, mark, menu, meter, nav, output, progress, section, time, video".split(', ');
            var i = e.length;
            while (i--) {
                document.createElement(e[i])
            }

        })()


    </script>
    <![endif]-->
    ;
    <!-- BEGIN THEME STYLES -->
    <link href="<%=request.getContextPath()%>/js/plugins/font-awesome/css/font-awesome.min.css"
          rel="stylesheet"
          type="text/css">
    <link href="<%=request.getContextPath()%>/js/plugins/simple-line-icons/simple-line-icons.min.css"
          rel="stylesheet"
          type="text/css">
    <link href="<%=request.getContextPath()%>/js/plugins/bootstrap/css/bootstrap.min.css"
          rel="stylesheet"
          type="text/css">
    <link href="<%=request.getContextPath()%>/js/plugins/uniform/css/uniform.default.css"
          rel="stylesheet"
          type="text/css">
    <link href="<%=request.getContextPath()%>/js/scripts/onepage2/css/layout.css" rel="stylesheet"
          type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/components.css" id="style_components"
          rel="stylesheet"
          type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/plugins.css" rel="stylesheet"
          type="text/css"/>
    <link href="<%=request.getContextPath()%>/js/scripts/layout4/css/layout.css" rel="stylesheet"
          type="text/css"/>
    <link id="style_color" href="<%=request.getContextPath()%>/js/scripts/layout4/css/themes/light.css"
          rel="stylesheet"
          type="text/css"/>
    <link href="<%=request.getContextPath()%>/js/scripts/layout4/css/custom.css" rel="stylesheet"
          type="text/css"/>
    <link href="<%=request.getContextPath()%>/css/login-soft.css" rel="stylesheet"
          type="text/css"/>
    <!-- END THEME STYLES -->

</head>
<!-- BEGIN BODY -->
<!-- DOC: Apply "page-on-scroll" class to the body element to set fixed header layout -->
<body class="login">
<!-- BEGIN MAIN LAYOUT -->
<!-- BEGIN LOGO -->
<div class="logo">
    <a href="login.jsp">
        <img src="<%=request.getContextPath()%>/images/th.png" alt="logo"
             class="logo-default"/>
    </a>
</div>
<!-- END LOGO -->
<!-- BEGIN SIDEBAR TOGGLER BUTTON -->
<div class="menu-toggler sidebar-toggler">
</div>
<!-- END SIDEBAR TOGGLER BUTTON -->

<!-- BEGIN CONTAINER -->
<div class="page-container" style="min-width: 1000px;">
    <!-- BEGIN INTRO SECTION -->
    <section id="intro">
        <div class="page-container">
            <div class="content">
                <!-- BEGIN LOGIN FORM -->
                <form class="login-form" method="post">
                    <h3 class="form-title">登录您的账号</h3>
                    <div class="alert alert-danger display-hide">
                        <button class="close" data-close="alert"></button>
                        <span> 请输入账号或密码！</span>
                    </div>
                    <div class="form-group">
                        <!--ie8, ie9 does not support html5 placeholder, so we just show field title for that-->
                        <label class="control-label visible-ie8 visible-ie9">账号</label>
                        <div class="input-icon">
                            <i class="fa fa-user"></i>
                            <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
                                   placeholder="账号" name="staff_code" id="staff_code"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label visible-ie8 visible-ie9">密码</label>
                        <div class="input-icon">
                            <i class="fa fa-lock"></i>
                            <input class="form-control placeholder-no-fix" type="password" autocomplete="off"
                                   placeholder="密码" name="staff_pwd" id="staff_pwd"/>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button onclick="login()" type="button" class="btn blue pull-right">
                            登&nbsp;录
                            <i class="m-icon-swapright m-icon-white"></i>
                        </button>
                    </div>
                    <div class="forget-password">
                        <h4>忘记密码 ?</h4>
                        <p>
                            <a href="javascript:;" id="forget-password"> 点击这里 </a>
                            找回密码.
                        </p>
                    </div>
                    <!--  <div class="create-account">
                    <p>
                        还没有账号 ?&nbsp; <a href="javascript:;" id="register-btn">
                            机构注册 </a>
                    </p>
                </div>-->
                </form>
                <!-- END LOGIN FORM -->
                <!-- BEGIN FORGOT PASSWORD FORM -->
                <form class="forget-form" action="" method="post">
                    <h3>忘记密码 ?</h3>
                    <p>输入邮箱找回密码.</p>
                    <div class="form-group">
                        <div class="input-icon">
                            <i class="fa fa-envelope"></i>
                            <input class="form-control placeholder-no-fix" type="text" autocomplete="off"
                                   placeholder="邮箱" name="email"/>
                        </div>
                    </div>
                    <div class="form-actions">
                        <button type="button" id="back-btn" class="btn">
                            <i class="m-icon-swapleft"></i>
                            返&nbsp;回
                        </button>
                        <button type="submit" class="btn blue pull-right">
                            提&nbsp;交
                            <i class="m-icon-swapright m-icon-white"></i>
                        </button>
                    </div>
                </form>
                <!-- END FORGOT PASSWORD FORM -->
            </div>
            <!-- BEGIN COPYRIGHT -->
            <div class="copyright">
                2017 &copy; marathon.
            </div>
            <!-- END COPYRIGHT -->

        </div>
    </section>
</div>
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/jQuery-core/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jQuery-core/jquery-migrate.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/plugins/bootstrap/js/bootstrap.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jQuery-core/jquery.blockui.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/plugins/uniform/jquery.uniform.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/jQuery-core/jquery.cokie.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/plugins/jquery-validation/js/jquery.validate.min.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/plugins/backstretch/jquery.backstretch.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/scripts/login-soft.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/scripts/onepage2/scripts/layout.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/scripts/metronic.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/scripts/layout4/scripts/layout.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/scripts/layout4/scripts/demo.js"
        type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/js/scripts/onepage2/scripts/html5shiv.js"
        type="text/javascript"></script>
<script type="text/javascript"
        src="<%=request.getContextPath()%>/js/plugins/bootbox/bootbox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jQuery-core/md5-min.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/index/login.js"></script>
<!-- END PAGE LEVEL SCRIPTS -->
</body>
</html>
