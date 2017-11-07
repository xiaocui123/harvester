<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link rel="stylesheet" href="${ctx}/old_resource/extenstion/jQuery_zTree/css/zTreeStyle/zTreeStyle.css" type="text/css">
<script type="text/javascript" src="${ctx}/old_resource/extenstion/jQuery_zTree/js/jquery.ztree.core-3.5.js"></script>
<script type="text/javascript" src="${ctx}/old_resource/extenstion/jQuery_zTree/js/jquery.ztree.excheck-3.5.js"></script>

<script>
    var path = "${ctx}";
</script>
<div class="container" style="height: 100%; overflow-y: auto; overflow-x: no">
    <div class="row">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#">部门人员</a></li>
        </ul>
    </div>
    <div class="row-fluid" style="margin-top: 10px">
            <button type="button" class="btn btn-default" id="addDeptBtn">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新建部门
            </button>
            <button type="button" class="btn btn-default" id="updateDeptBtn">
                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改部门
            </button>
            <button type="button" class="btn btn-default" id="deleteDeptBtn">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除部门
            </button>

            <button type="button" class="btn btn-default" id="addStaffBtn">
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>新建人员
            </button>
            <button type="button" class="btn btn-default" id="updateStaffBtn">
                <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>修改人员
            </button>
            <button type="button" class="btn btn-default" id="deleteStaffBtn">
                <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除人员
            </button>
            <button type="button" class="btn btn-primary" id="allocateRoleBtn">
                <span class="glyphicon glyphicon-ok-circle" aria-hidden="true"></span>分配角色
            </button>
    </div>
    <div class="row-fluid" style="margin-top: 10px">
        <div class="scroller"
             data-always-visible="1" data-rail-visible1="0"
             data-handle-color="#D7DCE2">
            <ul id="treeDept" class="ztree"></ul>
        </div>
    </div>

    <div class="modal fade bs-example-modal-sm" id="addDept-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" style="width: 50%;background: #fff; margin:30px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="modal-title">新建部门</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="deptName-input" class="col-sm-2 control-label">部门名称</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="deptName-input" placeholder="请输入部门名字">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer" id="edit-footer">
                    <button type="button" class="btn btn-primary text-center" id="saveDeptBtn">保存</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="analySub" >关闭</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade bs-example-modal-sm" id="addStaff-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" style="width: 50%;background: #fff; margin:30px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="modal-staff-title">新建人员</h4>
                </div>
                <div class="modal-body">
                    <form class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="staffCode-input" class="col-sm-2 control-label">登录名</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="staffCode-input" placeholder="请输入登录名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="staffName-input" class="col-sm-2 control-label">真实姓名</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="staffName-input" placeholder="请输入真实姓名">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="staffTel-input" class="col-sm-2 control-label">固话</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="staffTel-input" placeholder="请输入固定电话">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="staffMobile-input" class="col-sm-2 control-label">手机号</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="staffMobile-input" placeholder="请输入手机号">
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="staffEmail-input" class="col-sm-2 control-label">邮件</label>
                            <div class="col-sm-10">
                                <input type="text" class="form-control" id="staffEmail-input" placeholder="请输入邮箱地址">
                            </div>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary text-center" id="saveStaffBtn">保存</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="closeModal" >关闭</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade bs-example-modal-sm" id="allocateRole-modal" tabindex="-1" role="dialog">
        <div class="modal-dialog" style="width: 50%;background: #fff; margin:30px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="modal-allocateRole-title">分配角色</h4>
                </div>
                <div class="modal-body">
                    <div class="row-fluid" style="margin-top: 10px">
                        <div class="scroller"
                             data-always-visible="1" data-rail-visible1="0"
                             data-handle-color="#D7DCE2">
                            <ul id="treeRole" class="ztree"></ul>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary text-center" id="saveStaffRoleBtn">保存</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal" id="close" >关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/resource/assets/bootbox/bootbox.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/view/sys/department.js"></script>