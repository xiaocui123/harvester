<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="common/common.jsp" %>
<title>欢迎使用多源海洋数据同一化处理系统</title>
<link href="${ctx}/resource/assets/bootstrap/css/bootstrap.min.css" rel="stylesheet">
<link rel="stylesheet" href="${ctx}/resource/css/layout.css">
<script src="${ctx}/resource/js/index.js"></script>
<script type="text/javascript">
	var modualIds_ = '${AuthFunc}';
</script>
</head>
<body>
    <!-- 加载中 -->
    <div class="" id="loading"></div>
    <div id="header">
        <div class="logo-box">
           <span class="logo-img"><img src="${ctx }/resource/images/th.png"></span>
           <span class="logo-text">多源海洋数据同一化处理系统</span>
        </div>
		<!-- BEGIN TOP NAVIGATION MENU -->
        <div class="app-top-menu">
        	<!-- 用户菜单 -->
            <ul class="nav navbar-nav pull-right" style="margin:0;">
                <li class="dropdown">
                    <a class="dropdown-toggle menu-btn" type="button" data-toggle="dropdown">
                    	<span class="glyphicon glyphicon-user"></span> 
                    	<span>${sessionScope.SYSTEM_USER.userName}</span>
				        <span class="caret"></span>
				    </a>
                    <ul class="dropdown-menu pull-right hzp16110_dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
                        <li role="presentation"><a role="menuitem" tabindex="-1"  data-toggle="modal" data-target="#myModal" href="#">修改密码</a></li>
                        <%--<li role="presentation"><a  href="javascript:void(0);" onclick="showAbout();">关于</a></li>--%>
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="${ctx}/sys/logout">注销</a></li>
                       <!--  <li role="presentation" class="divider"></li>
                        <li role="presentation"><a role="menuitem" tabindex="-1" href="javascript:;">关于</a></li> -->
                    </ul>
                </li>
            </ul>
            <!-- 用户菜单 -->
        </div>
        <!-- END TOP NAVIGATION MENU -->
        <div class="tool-box hide">
            <div class="search-box-parent hide">
                <div class="search-box">
                    <div class="left-text">
                        <input type="text" class="input-text">
                    </div>
                    <div class="right-btn">
                        <span class="glyphicon glyphicon-search"></span>
                    </div>
                </div>
            </div>
            <a class="tool-btn" href="javascript:;">
               <span class="glyphicon glyphicon-user"></span> 
               <span>${sesnUsrACOUNT}</span>
            </a>
            <a class="tool-btn hide" href="javascript:;">
                <span class="glyphicon glyphicon-comment"></span> 消息
            </a>
        </div>
    </div>
    <div class="app-sidebar-wrapper app-sidebar-close">
        <div class="app-sidebar-toggle">
            <div class="app-sidebar-menu">
                <span>>></span>
            </div>
        </div>
        <div class="app-side-bar">
            <ul class="sidebar-wrapper"></ul>
        </div>
    </div>
    <div id="left-bar">
        <!-- 切换按钮 -->
        <div class="menu-btn menu-btn-toggle">
            <div class="menu-out-box">
                <div class="menu-btn-box">
                    <span class="glyphicon glyphicon-forward"></span>
                </div>
            </div>
        </div>
        <div class="menu-list">
            <div class="menu-btn">
                <div class="menu-out-box">
                    <div class="menu-btn-box" data-toggle="tooltip" data-placement="right" title="sdfsdfright">
                        <span class="glyphicon glyphicon-th"></span>
                    </div>
                </div>
            </div>
            <div class="menu-btn last-menu">
                <div class="menu-out-box">
                    <div class="menu-btn-box">
                        <span class="glyphicon glyphicon-th"></span>
                    </div>
                </div>
            </div>
        </div>
        <!-- 导航按钮，独立于其他按钮 -->
        <div class="menu-btn menu-btn-nav">
            <div class="menu-out-box" id="nav-list-btn">
                <div class="menu-btn-box" data-toggle="tooltip" data-placement="right" title="导航菜单">
                    <span class="glyphicon glyphicon-th"></span>
                </div>
                <span class="btn-title">导航菜单</span>
            </div>

            <div class="scrollbar" id="nav-list">
                <div class="scrollbar">
                    <div class="track"><div class="thumb"> <div class="end"></div></div></div>
                </div>
                <div class="viewport">
                    <div class="overview">
                        <div class="list-box">
                            <dl>
                                <dt class="list-title">未命名组</dt>
                                <dd class="nav-list-box">
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                    <div class="link-box">
                                        <div class="link-img"><span class="glyphicon glyphicon-user"></span></div>
                                        <span class="link-text">用户管理</span>
                                    </div>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="scrollbar" id="proscenium">
        <div class="scrollbar hide">
            <div class="track"><div class="thumb"> <div class="end"></div></div></div>
        </div>
        <div class="viewport">
            <div class="overview" id="proscenium-box">
                <!-- begin 这里是各个业务页面 -->
				
                <!-- begin 这里是各个业务页面 -->
            </div>
        </div>
    </div>
    <!-- 消息提示信息 -->
    <div id="hb-messages-box"></div>
    <!-- 确认，提示 -->
    <div class="modal fade bs-example-modal-sm" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myLargeModalLabel">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                    <h4 class="modal-title">修改密码</h4>
                </div>
                <div class="modal-body">
					<div class="container-fluid">
						<div class="row-fluid">
							<div class="col-xs-12">
								<form id="editPassForm" class="form-horizontal" role="form">
									<div class="form-group">
										<label for="passOld" class="col-sm-2 control-label">原密码</label>
										<div class="col-sm-10" style="margin-bottom: 10px;">
											<input type="password" class="form-control" name="passOld" id="passOld">
											<span class="help-block ishas" style="position: absolute;">原密码错误</span>
										</div>
									</div>
									<div class="form-group">
										<label for="passNew" class="col-sm-2 control-label">新密码</label>
										<div class="col-sm-10" style="margin-bottom: 10px;">
											<input type="password" class="form-control" name="passNew" id="passNew">
											<span class="help-block ishas" style="position: absolute;">该字段为必填字段</span>
										</div>
									</div>
									<div class="form-group">
										<label for="passNewTwo" class="col-sm-2 control-label">密码确认</label>
										<div class="col-sm-10" style="margin-bottom: 10px;">
											<input type="password" class="form-control" name="passNewTwo" id="passNewTwo">
											<span class="help-block ishas" style="position: absolute;">两次输入新密码不一致，请重新输入！</span>
										</div>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button type="button" class="btn btn-primary" id="editPass-save">保存</button>
                </div>
            </div>
        </div>
    </div>
<div class="hzp161011_aboutMask" id="hzp161011_about">   
	<div class="hzp161010_about" >
		<div class="hzp161010_about_close">×</div>
		<br />
		<img src="${ctx }/resource/images/th.png"><h3>多源海洋数据同一化处理系统</h3>
		<br /><br />
        <p>开发服务：XXXXX有限公司</p>
        <p>客服电话：<span>010-55716100</span></p>
        <p>公司地址：XXXXXXXXXXXXXX</p>
        <p>公司官网：<a href="http://www.marathon.com/" target="_blank">http://www.marathon.com/</a></p>
        <p>©2017  XXXXX有限公司  All Rights Reserved</p>
	</div>
</div>
</body>
</html>