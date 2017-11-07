$(function () {
    $('#grid').bootstrapTable({
        url: path + '/sysrole/queryAll',         //请求后台的URL（*）
        method: 'post',                      //请求方式（*）
        toolbar: '#toolbar',                //工具按钮用哪个容器
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: false,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        queryParams: queryParams,            //传递参数（*）
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                       //初始化加载第一页，默认第一页
        pageSize: 10,                       //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: false,                       //是否显示表格搜索，此搜索是客户端搜索，不会进服务端，所以，个人感觉意义不大
        strictSearch: true,
        showColumns: false,                  //是否显示所有的列
        showRefresh: false,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        height: 500,                        //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "roleId",             //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        columns: [
            {
                checkbox: true
            },
            {
                field: "roleId", title: "序号", width: 40, align: 'center',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {field: "roleName", title: "角色名称", width: 100, valign: 'middle'},
            {field: "roleDesc", title: "角色描述", valign: 'middle'}
        ]
    });

    function queryParams(params) {
        var parameter = {
            limit: params.limit,
            offset: params.offset
        };
        return parameter;
    }

    $('#sysrole-add-btn').click(function () {
        gridBtnAdd();
    });

    $('#sysrole-update-btn').click(function () {
        gridBtnUpdate();
    });

    $('#sysrole-delete-btn').click(function () {
        gridBtnDel();
    });

    $('#saveSysroleBtn').click(function () {
        saveSysrole();
    });

    //权限树
    var setting = {
        check: {
            enable: true
        },
        data: {
            simpleData: {
                enable: true
            }
        }
    };
    $.get(path + "/menu/permissionTree", function (data) {
        $.fn.zTree.init($("#treePermission"), setting, data);
    });

});

function gridBtnAdd() {
    $('#modal-title').html("新建角色");
    $('#addSysRole-modal').removeData("data");
    $("#sysRoleName-input").val('');
    $("#sysRoleDesc-input").val('');
    var treeObj = $.fn.zTree.getZTreeObj("treePermission");
    treeObj.checkAllNodes(false);
    $('#addSysRole-modal').modal('show');
}

function gridBtnUpdate() {
    var selectContent = $("#grid").bootstrapTable('getSelections');
    if (selectContent.length > 1) {
        bootbox.alert("只能选择一条修改！");
    } else if (selectContent.length == 0) {
        bootbox.alert("请选择一条进行修改！")
    } else {
        $('#modal-title').html("修改角色");
        $('#addSysRole-modal').data("data", selectContent[0]);

        //删除重新加载
        var zTreeObj = $.fn.zTree.getZTreeObj("treePermission");
        zTreeObj.destroy();
        var setting = {
            check: {
                enable: true
            },
            data: {
                simpleData: {
                    enable: true
                }
            }
        };
        $.get(path + "/menu/permissionTree?sysRoleId=" + selectContent[0].roleId, function (data) {
            $.fn.zTree.init($("#treePermission"), setting, data);
        });

        $("#sysRoleName-input").val(selectContent[0].roleName);
        $("#sysRoleDesc-input").val(selectContent[0].roleDesc);
        $('#addSysRole-modal').modal('show');
    }
}

function gridBtnDel() {
    var selectContent = $("#grid").bootstrapTable('getSelections');
    if (selectContent.length == 1) {
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确定要删除吗？", function (result) {
            if (result) {
                $.get(path + "/sysrole/delete?sysRoleId=" + selectContent[0].roleId, function (response) {
                    if (!response.success) {
                        bootbox.alert(response.message);
                    } else {
                        bootbox.alert("删除机构成功！", function () {
                            $("#grid").bootstrapTable('refresh');
                        });
                    }
                })
            }
        });
    } else {
        bootbox.alert("请选择一个角色！");
    }
}

function saveSysrole() {
    var data = $('#addSysRole-modal').data("data");
    if (!data) {
        //新建
        var addSysroleVO = {};
        addSysroleVO.roleName = $.trim($('#sysRoleName-input').val());
        addSysroleVO.roleDesc = $.trim($('#sysRoleDesc-input').val());
        addSysroleVO.lstPermissionId = [];

        var treeObj = $.fn.zTree.getZTreeObj("treePermission");
        var nodes = treeObj.getCheckedNodes(true);
        $.each(nodes, function (index, node) {
            addSysroleVO.lstPermissionId.push(node.id);
        });

        $.ajax({
            url: path + '/sysrole/add',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(addSysroleVO),
            success: function (response) {
                if (!response.success) {
                    bootbox.alert(response.message);
                } else {
                    bootbox.alert("添加角色成功！", function () {
                        $('#addSysRole-modal').modal('hide');
                        $("#grid").bootstrapTable('refresh');
                    });
                }
            },
            error: function (response) {
                bootbox.alert("error");
            }
        });
    }
    else {
        var addSysroleVO = {};
        addSysroleVO.roleId = data.roleId;
        addSysroleVO.roleName = $.trim($('#sysRoleName-input').val());
        addSysroleVO.roleDesc = $.trim($('#sysRoleDesc-input').val());
        addSysroleVO.lstPermissionId = [];

        var treeObj = $.fn.zTree.getZTreeObj("treePermission");
        var nodes = treeObj.getCheckedNodes(true);
        $.each(nodes, function (index, node) {
            addSysroleVO.lstPermissionId.push(node.id);
        });
        $.ajax({
            url: path + '/sysrole/update',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(addSysroleVO),
            success: function (response) {
                if (!response.success) {
                    bootbox.alert(response.message);
                } else {
                    bootbox.alert("修改角色成功！", function () {
                        $('#addSysRole-modal').modal('hide');
                        $("#grid").bootstrapTable('refresh');
                    });
                }
            },
            error: function (response) {
                bootbox.alert("error");
            }
        });
    }
}