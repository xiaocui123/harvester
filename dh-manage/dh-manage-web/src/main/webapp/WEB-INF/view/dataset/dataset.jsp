<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<link href="${ctx}/js/plugins/bootstraptable/bootstrap-table.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/resource/assets/bootstrap-fileinput/css/fileinput.css" rel="stylesheet" type="text/css"/>
<link href="${ctx}/resource/assets/bootstrap-tagsinput/css/bootstrap-tagsinput.css" rel="stylesheet" type="text/css"/>
<script>
    var path = "${ctx}";
</script>

<style>
    .bootstrap-tagsinput {
        display: block;
    }
</style>

<div class="container" style="height: 100%; overflow-y: auto; overflow-x: no">
    <div class="row">
        <ul class="nav nav-tabs">
            <li class="active"><a href="#">同一数据</a></li>
        </ul>
    </div>

    <div style="margin-top: 10px">
        <button type="button" class="btn btn-default"
                id="dataset-create-btn">
            <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>创建
        </button>
        <button type="button" class="btn btn-danger"
                id="buoy-delete-btn">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>

    </div>

    <div class="row-fluid">
        <table id="grid"></table>
    </div>

    <!-- 创建数据模态框 -->
    <div class="modal" id="dataset-create-modal" style="overflow-y: visible;">
        <div class="modal-dialog"
             style="width: 70%; background: #fff; margin: 30px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                    </button>
                    <h4 class="modal-title" id="title">创建数据集</h4>
                </div>
                <div class="modal-body">
                    <div class="col-xs-12">
                        <div class="input-group form-group">
                            <div class="input-group-addon">数据名称</div>
                            <input id="name-input" type="text" class="form-control"
                                   placeholder="数据名称">
                        </div>
                    </div>
                    <!--第二行 -->
                    <div class="col-xs-12">
                            <div class="input-group form-group">
                                <div class="input-group-addon">数据源类型</div>
                                <select class="form-control" id="source-input"></select>
                            </div>
                    </div>

                    <!-- 数据源 -->
                    <div id="source-div" class="col-xs-12">
                        <div id="jdcb-source-div">
                            <div class="row">
                                <div class="col-xs-4">
                                    <div class="input-group form-group">
                                        <div class="input-group-addon">数据库类型</div>
                                        <select class="form-control" id="jdbc-input"></select>
                                    </div>
                                </div>
                                <div class="col-xs-8">
                                    <div class="input-group form-group">
                                        <div class="input-group-addon">URL</div>
                                        <input id="jdbc-url-input" type="text" class="form-control"
                                               placeholder="URL">
                                    </div>
                                </div>
                            </div>
                            <div class="row">
                                <div class="col-xs-4">
                                    <div class="input-group form-group">
                                        <div class="input-group-addon">用户名</div>
                                        <input id="jdb-username-input" type="text" class="form-control"
                                               placeholder="用户名">
                                    </div>
                                </div>
                                <div class="col-xs-4">
                                    <div class="input-group form-group">
                                        <div class="input-group-addon">密码</div>
                                        <input id="jdbc-password-input" type="text" class="form-control"
                                               placeholder="密码">
                                    </div>
                                </div>
                                <div class="col-xs-4">
                                    <div class="input-group form-group">
                                        <div class="input-group-addon">数据表</div>
                                        <input id="jdbc-table-input" type="text" class="form-control"
                                               placeholder="数据表">
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div id="excel-source-div" class="row">
                            <div class="col-xs-12">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">Excel文件</div>
                                    <input id="excel-input" type="text" class="form-control"
                                           placeholder="文件路径">
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="station-div">
                        <div class="col-xs-3">
                            <div class="input-group form-group">
                                <div class="input-group-addon">站点字段</div>
                                <input id="station-column-input" type="text" class="form-control"
                                       placeholder="站点字段">
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group form-group">
                                <div class="input-group-addon">站点名字</div>
                                <input id="station-name-input" type="text" class="form-control"
                                       placeholder="站点名字">
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group form-group">
                                <div class="input-group-addon">经度值</div>
                                <input id="longitude-input" type="text" class="form-control"
                                       placeholder="经度值">
                            </div>
                        </div>
                        <div class="col-xs-3">
                            <div class="input-group form-group">
                                <div class="input-group-addon">纬度值</div>
                                <input id="latitude-input" type="text" class="form-control"
                                       placeholder="纬度值">
                            </div>
                        </div>
                    </div>

                    <div id="time-div">
                        <div class="time-variable">
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">时间字段</div>
                                    <input id="time-column-input" type="text" class="form-control"
                                           placeholder="时间字段">
                                </div>
                            </div>
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">时间变量名称</div>
                                    <input id="time-varName-input" type="text" class="form-control"
                                           placeholder="时间变量名字">
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <button type="button" id="addTimeAttribute-btn" class="btn btn-success">添加属性</button>
                            </div>
                        </div>
                        <div class="time-attribute">
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">key</div>
                                    <input id="time-attribute-key-input" type="text" class="form-control"
                                           placeholder="key">
                                </div>
                            </div>
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">value</div>
                                    <input id="time-attribute-value-input" type="text" class="form-control"
                                           placeholder="value">
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <button type="button" class="btn btn-danger">删除</button>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <label>
                            <input type="checkbox" id="depth-checkbox">深度
                        </label>
                    </div>

                    <div id="depth-div">
                        <div class="depth-variable">
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">深度字段</div>
                                    <input id="depth-column-input" type="text" class="form-control"
                                           placeholder="深度字段">
                                </div>
                            </div>
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">深度变量名称</div>
                                    <input id="depth-varName-input" type="text" class="form-control"
                                           placeholder="深度变量名称">
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <div class="input-group form-group">
                                    <button type="button" class="btn btn-success">添加属性</button>
                                </div>
                            </div>
                        </div>
                        <div class="depth-attribute">
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">key</div>
                                    <input id="depth-attribute-key-input" type="text" class="form-control"
                                           placeholder="key">
                                </div>
                            </div>
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">value</div>
                                    <input id="depth-attribute-value-input" type="text" class="form-control"
                                           placeholder="value">
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <div class="input-group form-group">
                                    <button type="button" class="btn btn-danger">删除</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="input-group form-group">
                            <button id="addVariable-btn" type="button" class="btn btn-success">添加变量</button>
                        </div>
                    </div>

                    <div id="metured-variable">
                        <div class="variable-div-wrapper">
                            <div id="variable-div" class="col-xs-10">
                                <div id="variable" class="row">
                                    <div class="col-xs-5">
                                        <div class="input-group form-group">
                                            <div class="input-group-addon">变量字段</div>
                                            <input id="variable-column-input" type="text" class="form-control"
                                                   placeholder="变量字段">
                                        </div>
                                    </div>
                                    <div class="col-xs-5">
                                        <div class="input-group form-group">
                                            <div class="input-group-addon">变量名称</div>
                                            <input id="variable-varName-input" type="text" class="form-control"
                                                   placeholder="变量名称">
                                        </div>
                                    </div>
                                    <div class="col-xs-2">
                                        <button type="button" class="btn btn-success">添加属性</button>
                                    </div>
                                </div>

                                <div id="attribute" class="row">
                                    <div class="col-xs-5">
                                        <div class="input-group form-group">
                                            <div class="input-group-addon">key</div>
                                            <input id="variable-attribute-key-input" type="text" class="form-control"
                                                   placeholder="key">
                                        </div>
                                    </div>
                                    <div class="col-xs-5">
                                        <div class="input-group form-group">
                                            <div class="input-group-addon">value</div>
                                            <input id="variable-attribute-value-input" type="text" class="form-control"
                                                   placeholder="value">
                                        </div>
                                    </div>
                                    <div class="col-xs-2">
                                        <button type="button" class="btn btn-danger">删除</button>
                                    </div>
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <button type="button" class="btn btn-success">删除变量</button>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="input-group form-group">
                            <button type="button" id="addGlobalAttribute-btn" class="btn btn-success">添加全局属性</button>
                        </div>
                    </div>

                    <div id="global-attribute">
                        <div class="global-attribute-wrapper">
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">key</div>
                                    <input id="global-attribute-key-input" type="text" class="form-control"
                                           placeholder="key">
                                </div>
                            </div>
                            <div class="col-xs-5">
                                <div class="input-group form-group">
                                    <div class="input-group-addon">value</div>
                                    <input id="global-attribute-value-input" type="text" class="form-control"
                                           placeholder="value">
                                </div>
                            </div>
                            <div class="col-xs-2">
                                <div class="input-group form-group">
                                    <button type="button" class="btn btn-danger">删除</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-12">
                        <div class="input-group form-group col-xs-12 text-center" >
                            <button type="button" class="btn btn-success" id="dataset-save-btn">保存</button>
                            &nbsp;&nbsp;
                            <button type="button" class="btn btn-warning" id="close" data-dismiss="modal">关闭</button>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" id="dataset-create-footer">
                    <!--
                    <button type="button" class="btn btn-primary text-center"
                            id="dataset-save-btn">保存</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            id="close">关闭</button>
                    -->
                </div>
            </div>
        </div>
    </div>

    <!-- 发布模态框 -->
    <div class="modal" id="dataset-publish-modal" style="overflow-y: auto;">
        <div class="modal-dialog"
             style="width: 70%; background: #fff; margin: 30px auto;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
                    </button>
                    <h4 class="modal-title" id="modal-title" data_id="">发布数据</h4>
                </div>
                <div class="modal-body">
                    <!-- 第一行 -->
                    <div class="input-group col-xs-12" clearfix>
                        <div class="col-xs-12">
                            <div class="input-group form-group">
                                <div class="input-group-addon">数据名称</div>
                                <input id="dataset-name-input" type="text" class="form-control"
                                       placeholder="数据名称">
                            </div>
                        </div>
                    </div>
                    <!-- 第 二行 -->
                    <div class="input-group col-xs-12 clearfix ">
                        <div class="col-xs-12">
                            <div class="input-group form-group">
                                <div class="input-group-addon">数据描述</div>
                                <textarea class="form-control textareaHeight" id="dataset_desc"></textarea>
                            </div>
                        </div>
                    </div>
                    <!-- 第三行 -->
                    <div class="input-group col-xs-12 clearfix ">
                        <div class="col-xs-12">
                            <div class="input-group form-group">
                                <div class="input-group-addon">标签</div>
                                <input id="tag" type="text" class="form-control" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer" id="edit-footer">
                    <button type="button" class="btn btn-primary text-center"
                            id="publishBtn">发布</button>
                    <button type="button" class="btn btn-default" data-dismiss="modal"
                            id="analySub">关闭</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript" src="${ctx}/resource/assets/bootbox/bootbox.js"></script>
<script src="${ctx}/js/plugins/bootstraptable/bootstrap-table.js"></script>
<script src="${ctx}/js/plugins/bootstraptable/locale/bootstrap-table-zh-CN.js"></script>
<script src="${ctx}/resource/assets/bootstrap-fileinput/js/fileinput.js"></script>
<script src="${ctx}/resource/assets/bootstrap-fileinput/js/fileinput_locale_zh.js"/>
<script src="${ctx}/resource/assets/bootstrap-tagsinput/js/bootstrap-tagsinput.js"/>
<script src="${ctx}/resource/assets/typeahead/typeahead.bundle.js"/>
<script type="text/javascript" src="${ctx}/resource/js/view/dataset/dataset_.js"></script>
<script type="text/javascript" src="${ctx}/resource/js/view/dataset/dataset.js"></script>