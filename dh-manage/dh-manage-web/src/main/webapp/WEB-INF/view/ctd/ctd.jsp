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
            <li class="active"><a href="#">CTD</a></li>
        </ul>
    </div>
    <div style="margin-top: 10px">
        <div class="input-group col-xs-12 clearfix attachment-list-div">
            <div class="col-xs-12">
                <input id="file-0a" name="file" type="file" multiple class="file-loading">
            </div>
        </div>
    </div>

    <div style="margin-top: 10px">
        <button type="button" class="btn btn-danger"
                id="buoy-delete-btn">
            <span class="glyphicon glyphicon-remove" aria-hidden="true"></span>删除
        </button>

    </div>

    <div class="row-fluid">
        <table id="grid"></table>
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
<script type="text/javascript" src="${ctx}/resource/js/view/ctd/ctd.js"></script>