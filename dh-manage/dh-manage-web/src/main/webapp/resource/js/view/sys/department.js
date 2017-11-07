$(function () {
    var setting = {
        data: {
            keep: {
                parent: true,
                leaf: true
            },
            simpleData: {
                enable: true
            }
        }
    };

    $.get(path + '/department/queryTree',function(data){
        $.fn.zTree.init($("#treeDept"), setting, data);
    });

    $('#addDeptBtn').click(function () {
        addDept();
    });

    $('#updateDeptBtn').click(function () {
        updateDept();
    });

    $('#deleteDeptBtn').click(function () {
        deleteDept();
    });

    $('#saveDeptBtn').click(function () {
        saveDept();
    });


    $('#addStaffBtn').click(function () {
        addStaff();
    });

    $('#updateStaffBtn').click(function () {
        updateStaff();
    });

    $('#deleteStaffBtn').click(function () {
        deleteStaff();
    });

    $('#saveStaffBtn').click(function () {
        saveStaff();
    });

    $('#allocateRoleBtn').click(function () {
        allocateRole();
    });

    $('#saveStaffRoleBtn').click(function () {
        savaStaffRole();
    });
});

function addDept() {
    $('#modal-title').html("新建部门");
    $('#addDept-modal').removeData("data");
    $('#deptName-input').val('');
    $('#addDept-modal').modal('show');
}

function updateDept() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
    var nodes = treeObj.getSelectedNodes();
    if (nodes.length != 1) {
        bootbox.alert("请选择一个部门!");
        return;
    } else {
        var selectedNode = nodes[0];
        if (selectedNode.isParent) {
            $('#modal-title').html("修改部门");
            $('#addDept-modal').data("data", selectedNode);
            $('#deptName-input').val(selectedNode.name);
            $('#addDept-modal').modal('show');
        }
    }
}

function deleteDept() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
    var nodes = treeObj.getSelectedNodes();
    if (nodes.length == 1) {
        var selectedNode = nodes[0];
        $.get(path + '/department/delete?deptId=' + selectedNode.id, function (response) {
            if (!response.success) {
                bootbox.alert(response.message);
            } else {
                bootbox.alert("删除部门成功！", function () {
                    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                    var nodes = treeObj.getSelectedNodes();
                    for (var i = 0, l = nodes.length; i < l; i++) {
                        if (nodes[i].isParent) {
                            treeObj.removeNode(nodes[i]);
                        }
                    }
                });
            }

        });
    }
}

function saveDept() {
    var data = $('#addDept-modal').data("data");
    var department = {};
    if (data) {
        //修改
        department.departmentId = data.id;
        department.departmentName = $.trim($('#deptName-input').val());
        $.ajax({
            url: path + '/department/update',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(department),
            success: function (response) {
                if (!response.success) {
                    bootbox.alert(response.message);
                } else {
                    bootbox.alert("修改群组成功！", function () {
                        $('#addDept-modal').modal('hide');
                        var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                        var nodes = treeObj.getSelectedNodes();
                        if (nodes.length > 0) {
                            nodes[0].name = $.trim($('#deptName-input').val());
                            treeObj.updateNode(nodes[0]);
                        }
                    });
                }
            },
            error: function (response) {
                bootbox.alert("error");
            }
        });

    } else {
        //新建
        var treeObj = $.fn.zTree.getZTreeObj("treeDept");
        var nodes = treeObj.getSelectedNodes();

        var parentNode = null;
        if (nodes.length == 1) {
            var selectedNode = nodes[0];
            if (selectedNode.isParent) {
                department.parentDepId = selectedNode.id;
                parentNode = selectedNode;
            }
        }
        department.departmentName = $.trim($('#deptName-input').val());

        $.ajax({
            url: path + '/department/add',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(department),
            success: function (response) {
                if (!response) {
                    bootbox.alert("添加部门失败！");
                } else {
                    bootbox.alert("添加部门成功！", function () {
                        $('#addDept-modal').modal('hide');
                        var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                        var newNode = {};
                        newNode.id = response.departmentId;
                        newNode.name = response.departmentName;
                        newNode.pId = response.parentDepId;
                        newNode.isParent = true;
                        treeObj.addNodes(parentNode, newNode);
                    });
                }
            },
            error: function (response) {
                bootbox.alert("error");
            }
        });
    }
}

function addStaff() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
    var nodes = treeObj.getSelectedNodes();
    if (nodes.length == 1) {
        var selectedNode = nodes[0];
        if (selectedNode.isParent) {
            $('#modal-staff-title').html("新建人员");
            $('#addStaff-modal').removeData("data");
            $('#staffCode-input').val('').attr("disabled", false);
            $('#staffName-input').val('');
            $('#staffTel-input').val('');
            $('#staffMobile-input').val('');
            $('#staffEmail-input').val('');
            $('#addStaff-modal').modal('show');
        }
    }
}

function updateStaff() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
    var nodes = treeObj.getSelectedNodes();
    if (nodes.length != 1) {
        bootbox.alert("请选择一个人员!");
        return;
    } else {
        var selectedNode = nodes[0];
        if (!selectedNode.isParent) {
            $('#modal-title').html("修改人员");
            $.get(path + "/user/queryById?userId=" + selectedNode.id, function (sysStaffInfo) {
                $('#addStaff-modal').data("data", sysStaffInfo);
                $('#staffCode-input').val(sysStaffInfo.userAccount).attr("disabled", true);
                $('#staffName-input').val(sysStaffInfo.userName);
                $('#staffTel-input').val(sysStaffInfo.mobilePhone);
                $('#staffMobile-input').val(sysStaffInfo.mobilePhone);
                $('#staffEmail-input').val(sysStaffInfo.email);
                $('#addStaff-modal').modal('show');
            });
        }
    }
}

function deleteStaff() {
    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
    var nodes = treeObj.getSelectedNodes();
    if (nodes.length == 1) {
        var selectedNode = nodes[0];
        if (!selectedNode.isParent) {
            $.get(path + "/user/delete?userId=" + selectedNode.id, function (data) {
                if (data.success) {
                    bootbox.alert("删除人员成功！", function () {
                        var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                        var nodes = treeObj.getSelectedNodes();
                        for (var i = 0, l = nodes.length; i < l; i++) {
                            if (!nodes[i].isParent) {
                                treeObj.removeNode(nodes[i]);
                            }
                        }
                    });
                }
            });
        }
    }
}

function saveStaff() {
    var data = $('#addStaff-modal').data("data");
    if (data) {
        //修改
        data.userName = $.trim($('#staffName-input').val());
        data.mobilePhone = $.trim($('#staffMobile-input').val());
        data.email = $.trim($('#staffEmail-input').val());

        $.ajax({
            url: path + '/user/update',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(data),
            success: function (response) {
                if (!response.success) {
                    bootbox.alert(response.message);
                } else {
                    bootbox.alert("修改人员成功！", function () {
                        $('#addStaff-modal').modal('hide');
                        var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                        var nodes = treeObj.getSelectedNodes();
                        if (nodes.length > 0) {
                            nodes[0].name = $.trim($('#staffName-input').val());
                            treeObj.updateNode(nodes[0]);
                        }
                    });
                }
            },
            error: function (response) {
                bootbox.alert("error");
            }
        });

    } else {
        //新建
        var sysStaffInfo = {};
        var treeObj = $.fn.zTree.getZTreeObj("treeDept");
        var nodes = treeObj.getSelectedNodes();

        if (nodes.length == 1) {
            var selectedNode = nodes[0];
            if (selectedNode.isParent) {
                sysStaffInfo.deptId = selectedNode.id;

                sysStaffInfo.userAccount = $.trim($('#staffCode-input').val());
                sysStaffInfo.userName = $.trim($('#staffName-input').val());
                sysStaffInfo.mobilePhone = $.trim($('#staffMobile-input').val());
                sysStaffInfo.email = $.trim($('#staffEmail-input').val());

                $.ajax({
                    url: path + '/user/add',
                    method: 'post',
                    dataType: "json",
                    contentType: 'application/json;charset=UTF-8',
                    data: JSON.stringify(sysStaffInfo),
                    success: function (response) {
                        if (!response) {
                            bootbox.alert("添加人员失败！");
                        } else {
                            bootbox.alert("添加人员成功！", function () {
                                $('#addStaff-modal').modal('hide');
                                var treeObj = $.fn.zTree.getZTreeObj("treeDept");
                                var newNode = {};
                                newNode.id = response.userId;
                                newNode.name = response.userName;
                                newNode.pId = response.deptId;
                                newNode.isParent = false;
                                treeObj.addNodes(selectedNode, newNode);
                            });
                        }
                    },
                    error: function (response) {
                        bootbox.alert("error");
                    }
                });
            }
        }
    }
}

function allocateRole() {
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
    var treeObj = $.fn.zTree.getZTreeObj("treeDept");
    var nodes = treeObj.getSelectedNodes();
    if (nodes.length == 1) {
        var selectedNode = nodes[0];
        if (!selectedNode.isParent) {
            $.get(path + "/user/role?sysUserId=" + selectedNode.id, function (data) {
                $.fn.zTree.init($("#treeRole"), setting, data);
                $('#allocateRole-modal').modal("show").data("data", selectedNode);
            });
        }
    }
}

function savaStaffRole() {
    var treeObj = $.fn.zTree.getZTreeObj("treeRole");
    var nodes = treeObj.getCheckedNodes(true);

    var staffId = $('#allocateRole-modal').data('data').id;
    var addStaffRoleVO = {};
    addStaffRoleVO.staffId = staffId;
    addStaffRoleVO.lstRoleId=[];
    if (nodes.length > 0) {
        $.each(nodes, function (index, node) {
            addStaffRoleVO.lstRoleId.push(node.id);
        });
    }
    $.ajax({
        url: path + '/user/saveStaffRole',
        method: 'post',
        dataType: "json",
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(addStaffRoleVO),
        success: function (response) {
            if (!response) {
                bootbox.alert("保存角色失败！");
            } else {
                bootbox.alert("保存角色成功！", function () {
                    $('#allocateRole-modal').modal('hide');
                });
            }
        },
        error: function (response) {
            bootbox.alert("error");
        }

    });
}