<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link href="${ctx}/js/plugins/bootstraptable/bootstrap-table.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="${ctx}/old_resource/extenstion/jQuery_zTree/css/metro.css" type="text/css">
<script>
    var path = "${ctx}";
</script>
<div class="container" style="height: 100%; overflow-y: auto; overflow-x: no">
    <div class="row">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#">角色列表</a></li>
        </ul>
    </div>
    <div style="margin-top: 10px">
        <button type="button" class="btn btn-default" id="sysrole-add-btn">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新建角色</button>
        <button type="button" class="btn btn-default" id="sysrole-update-btn">
            <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改角色</button>
        <button type="button" class="btn btn-danger" id="sysrole-delete-btn">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除角色</button>

    </div>
    <div class="row-fluid">
        <table id="grid"></table>
    </div>

    <div class="modal fade bs-example-modal-sm" id="addSysRole-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" style="width: 50%;background: #fff; margin:30px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="modal-title">新建角色</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="sysRoleName-input" class="col-sm-2 control-label">角色名称</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="sysRoleName-input" placeholder="请输入角色名字">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="sysRoleDesc-input" class="col-sm-2 control-label">描述</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="sysRoleDesc-input" placeholder="请输入角色描述">
                            </div>
                        </div>
                    </form>
                    <div class="row-fluid" style="margin-top: 10px">
                        <div class="scroller"
                             data-always-visible="1" data-rail-visible1="0"
                             data-handle-color="#D7DCE2">
                            <ul id="treePermission" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" id="edit-footer">
                    <button type="button" class="btn btn-primary text-center" id="saveSysroleBtn">保存</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="analySub" >关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/resource/assets/bootbox/bootbox.js"></script>
<script src="${ctx}/js/plugins/bootstraptable/bootstrap-table.js"></script>
<script src="${ctx}/js/plugins/bootstraptable/locale/bootstrap-table-zh-CN.js"></script>
<script type="text/javascript" src="${ctx}/old_resource/extenstion/jQuery_zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctx}/old_resource/extenstion/jQuery_zTree/js/jquery.ztree.excheck-3.5.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/view/sys/sysrolelist.js"></script>