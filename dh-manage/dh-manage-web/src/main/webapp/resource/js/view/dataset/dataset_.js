(function dataset(){
    window.DATASET=window.DATASET || {};
    var newTimeAttribute=
        '<div class="time-attribute">' +
            '<div class="col-xs-5">' +
                '<div class="input-group form-group">' +
                    '<div class="input-group-addon">key</div>' +
                    '<input id="time-attribute-key-input" type="text" class="form-control" placeholder="key">' +
                '</div>'+
            '</div>'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">value</div>'+
                    '<input id="time-attribute-value-input" type="text" class="form-control" placeholder="value">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-2">'+
                '<button type="button" class="btn btn-danger">删除</button>'+
            '</div>'+
        '</div>';

    var newDepthVariable=
        '<div class="depth-variable">'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">深度字段</div>'+
                    '<input id="depth-column-input" type="text" class="form-control" placeholder="深度字段">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">深度变量名称</div>'+
                    '<input id="depth-varName-input" type="text" class="form-control" placeholder="深度变量名称">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-2">'+
                '<div class="input-group form-group">'+
                    '<button type="button" class="btn btn-success">添加属性</button>'+
                '</div>'+
            '</div>'+
        '</div>';


    var newDepthAttribute=
        '<div class="depth-attribute">'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">key</div>'+
                    '<input id="depth-attribute-key-input" type="text" class="form-control" placeholder="key">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">value</div>'+
                    '<input id="depth-attribute-value-input" type="text" class="form-control" placeholder="value">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-2">'+
                '<div class="input-group form-group">'+
                    '<button type="button" class="btn btn-danger">删除</button>'+
                '</div>'+
            '</div>'+
        '</div>';

    var newVariable=
        '<div class="variable-div-wrapper">'+
            '<div id="variable-div" class="col-xs-10">'+
                '<div id="variable" class="row">'+
                    '<div class="col-xs-5">'+
                        '<div class="input-group form-group">'+
                            '<div class="input-group-addon">变量字段</div>'+
                            '<input id="variable-column-input" type="text" class="form-control"  placeholder="变量字段">'+
                        '</div>'+
                    '</div>'+
                    '<div class="col-xs-5">'+
                        '<div class="input-group form-group">'+
                            '<div class="input-group-addon">变量名称</div>'+
                            '<input id="variable-varName-input" type="text" class="form-control" placeholder="变量名称">'+
                        '</div>'+
                    '</div>'+
                    '<div class="col-xs-2">'+
                        '<button id="addVariableAtribute-btn" type="button" class="btn btn-success">添加属性</button>'+
                    '</div>'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-2">'+
                '<button id="delete-variable-btn" type="button" class="btn btn-success">删除变量</button>'+
            '</div>'+
        '</div>';

    var newVariableAttribute=
        '<div id="attribute" class="row">'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">key</div>'+
                    '<input id="variable-attribute-key-input" type="text" class="form-control" placeholder="key">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">value</div>'+
                    '<input id="variable-attribute-value-input" type="text" class="form-control" placeholder="value">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-2">'+
                '<button type="button" class="btn btn-danger">删除</button>'+
            '</div>'+
        '</div>';

    var newGlobalAttribute=
        '<div class="global-attribute-wrapper">'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">key</div>'+
                    '<input id="global-attribute-key-input" type="text" class="form-control" placeholder="key">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-5">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">value</div>'+
                    '<input id="global-attribute-value-input" type="text" class="form-control"  placeholder="value">'+
                '</div>'+
            '</div>'+
            '<div class="col-xs-2">'+
                '<div class="input-group form-group">'+
                    '<button type="button" class="btn btn-danger">删除</button>'+
                '</div>'+
            '</div>'+
        '</div>';

    var jdbcDiv=
        '<div id="jdcb-source-div">'+
            '<div class="row">'+
                '<div class="col-xs-4">'+
                    '<div class="input-group form-group">'+
                        '<div class="input-group-addon">数据库类型</div>'+
                        '<select class="form-control" id="jdbc-input"></select>'+
                    '</div>'+
                '</div>'+
                '<div class="col-xs-8">'+
                    '<div class="input-group form-group">'+
                        '<div class="input-group-addon">URL</div>'+
                        '<input id="jdbc-url-input" type="text" class="form-control" placeholder="URL">'+
                    '</div>'+
                '</div>'+
            '</div>'+
            '<div class="row">'+
                '<div class="col-xs-4">'+
                    '<div class="input-group form-group">'+
                        '<div class="input-group-addon">用户名</div>'+
                        '<input id="jdb-username-input" type="text" class="form-control" placeholder="用户名">'+
                    '</div>'+
                '</div>'+
                '<div class="col-xs-4">'+
                    '<div class="input-group form-group">'+
                        '<div class="input-group-addon">密码</div>'+
                        '<input id="jdbc-password-input" type="text" class="form-control" placeholder="密码">'+
                    '</div>'+
                '</div>'+
                '<div class="col-xs-4">'+
                    '<div class="input-group form-group">'+
                        '<div class="input-group-addon">数据表</div>'+
                        '<input id="jdbc-table-input" type="text" class="form-control" placeholder="数据表">'+
                    '</div>'+
                '</div>'+
            '</div>'+
        '</div>';

    var excelDiv=
        '<div id="excel-source-div" class="row">'+
            '<div class="col-xs-12">'+
                '<div class="input-group form-group">'+
                    '<div class="input-group-addon">Excel文件</div>'+
                    '<input id="excel-input" type="text" class="form-control" placeholder="文件路径">'+
                '</div>'+
            '</div>'+
        '</div>';

    DATASET = {
        //添加时间属性
        addTimeAttribute:function(){
            var row = $(newTimeAttribute).appendTo( $('#time-div'));
            row.find("button").click(function(){
                row.remove();
            });
        },

        addDepthVariable:function(){
            var row=$(newDepthVariable).appendTo($('#depth-div'));
            row.find("button").click(function(){
                var row = $(newDepthAttribute).appendTo($('#depth-div'));
                row.find("button").click(function(){
                    row.remove();
                });
            });
        },

        addVariable:function(){
            var row=$(newVariable).appendTo($('#metured-variable'));
            row.find("#delete-variable-btn").click(function(){
               row.remove();
            });
            row.find('#addVariableAtribute-btn').click(function(){
                var attribute=$(newVariableAttribute).appendTo(row.find('#variable-div'));
                console.log(attribute);
                attribute.find("button").click(function(){
                    attribute.remove();
                })
            });
        },
        addGlobalAttribute:function(){
            var row=$(newGlobalAttribute).appendTo($('#global-attribute'));
            row.find("button").click(function(){
                row.remove();
            });
        },
        selectJdbc:function(){
            $('#source-div').empty();
            $(jdbcDiv).appendTo($('#source-div'));
        },
        selectExcel:function(){
            $('#source-div').empty();
            $(excelDiv).appendTo($('#source-div'));
        }
    }
})();