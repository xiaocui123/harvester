$(function () {
    $('#grid').bootstrapTable({
        url: path + '/dataset/query',         //请求后台的URL（*）
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
        uniqueId: "datasetId",             //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        columns: [
            {
                checkbox: true
            },
            {
                field: "datasetId", title: "序号", width: 40, align: 'center',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {field: "datasetName", title: "数据集名称", width: 100, valign: 'middle'},
            {field: "datasetGenerator", title: "创建者", width: 80, valign: 'middle'},
            {field: "datasetGenerateTimeStr", title: "创建时间", width: 100, valign: 'middle'},
            {
                field: 'datasetId', title: '操作', width: 120, align: 'center',
                formatter: function (value, row, index) {
                    console.log(row);
                    var gridBtnDownload = '<input type="button" value="下载" onclick="gridBtnDownload(\'' + row.datasetId + '\')"  class="btn btn-primary btn-xs">';
                    if(row.published){
                        var gridBtnPreview = '<input type="button" value="预览" onclick="gridBtnPreview(\'' + row.publishUrl + '\')"  class="btn btn-primary btn-xs">';
                        return gridBtnDownload +'&nbsp;'+gridBtnPreview;

                    }else{
                        var gridBtnPublish = '<input type="button" value="发布" onclick="gridBtnPublish(\'' + row.datasetId + '\')"  class="btn btn-danger btn-xs">';
                        return gridBtnDownload + '&nbsp;' + gridBtnPublish
                    }
                }
            }
        ]
    });

    function queryParams(params) {
        var parameter = {
            limit: params.limit,
            offset: params.offset
        };
        return parameter;
    };

    // 初始化码值
    (function () {
        var lstParentId = ["100"];
        $.ajax({
            url: path + '/dict/getDict',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(lstParentId),
            success: function (response) {
                initDict(response);
            },
            error: function (response) {
                bootbox.alert("error");
            }
        });
        function initDict(data) {
            var sourceType = [];
            $.each(data, function (index, dict) {
                if (dict.parent == 100) {
                    sourceType.push(dict);
                }
            });
            // 给下拉框赋值
            createOption(sourceType, '#source-input', false);
        }
    }());

    $('#publishBtn').click(function(){
        var tags=$("#tag").tagsinput('items');
        var publish={
            publishResourceId:$('#dataset-publish-modal').data('resourceid'),
            publishDatasetName: $.trim($('#dataset-name-input').val()),
            publishDatasetDescription: $.trim($('#dataset_desc').val()),
            lstTagName: tags,
            publishResourceType:"GENERAL"
        };

        $.ajax({
            url: path + '/publish/publish',
            method: 'post',
            dataType: "json",
            contentType: 'application/json;charset=UTF-8',
            data: JSON.stringify(publish),
            success: function (response) {
                bootbox.alert("数据发布成功！",function(){
                    $('#dataset-publish-modal').modal('hide')
                    $("#grid").bootstrapTable('refresh');
                });
            },
            error: function (response) {
                bootbox.alert("response.message");
            }
        });
    });

    $('#buoy-delete-btn').click(function(){
       gridBtnDel();
    });

    $('#dataset-create-btn').click(function(){
        $('#source-input').val(-1);
        $('#source-div').empty();
        $('.time-attribute').remove();
        $('#depth-div').empty();
        $('#metured-variable').empty();
        $('#global-attribute').empty();
        $('#dataset-create-modal').modal('show');
    });

    //添加时间属性
    $('#addTimeAttribute-btn').click(function(){
        DATASET.addTimeAttribute();
    });

    $("#depth-checkbox").on('click',function(){
        if ($(this).prop("checked")){
            DATASET.addDepthVariable();
        }else{
            $('#depth-div').empty();
        }
    });

    //添加测量值变量
    $('#addVariable-btn').click(function(){
        DATASET.addVariable();

    });

    //添加全局属性
    $('#addGlobalAttribute-btn').click(function(){
        DATASET.addGlobalAttribute();
    });

    //数据源combo change事件
    $('#source-input').change(function(){
        var value=$(this).val();
        if(1001 == value){
            DATASET.selectJdbc();
            initJdbcTypeSelect();
        }else if(1002 == value){
            DATASET.selectExcel();
        }
    });

    //保存数据集
    $('#dataset-save-btn').click(function(){
        saveDataSet()
    });

    //TODO
    var citynames = new Bloodhound({
        datumTokenizer: Bloodhound.tokenizers.obj.whitespace('name'),
        queryTokenizer: Bloodhound.tokenizers.whitespace,
        prefetch: {
            url: path+'/resource/assets/typeahead/tags.json',
            filter: function(list) {
                return $.map(list, function(cityname) {
                    return { name: cityname }; });
            }
        }
    });
    citynames.initialize();

    $('#tag').tagsinput({
        typeaheadjs: {
            name: 'tags',
            displayKey: 'name',
            valueKey: 'name',
            source: citynames.ttAdapter()
        }
    });
});

function initJdbcTypeSelect(){
    var lstParentId = ["200"];
    $.ajax({
        url: path + '/dict/getDict',
        method: 'post',
        dataType: "json",
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(lstParentId),
        success: function (response) {
            var jdbcType = [];
            $.each(response, function (index, dict) {
                if (dict.parent == 200) {
                    jdbcType.push(dict);
                }
            });
            // 给下拉框赋值
            createOption(jdbcType, '#jdbc-input', false);
        },
        error: function (response) {
            bootbox.alert("error");
        }
    });
}


function createOption(data, elem, hasNull) {
    var html = hasNull ? '<option value="-1">请选择</option>' : '';
    for (var i = 0; i < data.length; i++) {
        html += '<option value="' + data[i].id + '">' + data[i].val + '</option>'
    }
    $(elem).html(html);
    $(elem).val("");
}

function gridBtnDownload(row) {
    window.open(path + '/dataset/downloadFile?datasetId=' + row);
}

function gridBtnPublish(row){
    $('#dataset-name-input').val("");
    $('#dataset_desc').val("");
    $('#tag').tagsinput('removeAll');
    $('#dataset-publish-modal').data("resourceid",row);
    $('#dataset-publish-modal').modal('show');
}

function gridBtnPreview(resourceUrl){
    console.log(resourceUrl);
    window.open(resourceUrl);
}

//保存数据集
function saveDataSet(){
    var dataset={};
    var datasetName= $.trim($('#name-input').val());
    if(datasetName.length == 0){
        bootbox.alert("数据名称不能为空！");
        return false;
    }
    dataset.datasetName=datasetName;
    var sourceType=$('#source-input').val();
    if(sourceType == -1){
        bootbox.alert("请选择数据源！");
        return false;
    }else if(sourceType == 1001){
        dataset.source={};
        dataset.source.jdbcType={};
        dataset.source.jdbcType.driver=$('#jdbc-input').val();
        dataset.source.jdbcType.url= $.trim($('#jdbc-url-input').val());
        dataset.source.jdbcType.username= $.trim($('#jdb-username-input').val());
        dataset.source.jdbcType.password= $.trim($('#jdbc-password-input').val());
        dataset.source.jdbcType.table= $.trim($('#jdbc-table-input').val());
    }else if(sourceType == 1002){
        dataset.source={};
        dataset.source.exel={};
        dataset.source.exel.path=$.trim($('#excel-input').val());
    }
    dataset.station={};
    dataset.station.stationVariable={};
    dataset.station.stationVariable.column= $.trim($('#station-column-input').val());
    dataset.station.stationVariable.name= $.trim($('#station-name-input').val());
    dataset.station.longitude= $.trim($('#longitude-input').val());
    dataset.station.latitude= $.trim($('#latitude-input').val());

    dataset.time={};
    dataset.time.column= $.trim($('#time-column-input').val());
    dataset.time.name=$.trim($('#time-varName-input').val());
    dataset.time.attribute=[];
    $.each($('#time-div .time-attribute'), function( key, value ) {
        var attribute={};
        attribute.key = $.trim($(value).find('#time-attribute-key-input').val());
        attribute.value= $.trim($(value).find($('#time-attribute-value-input').val()));
        dataset.time.attribute.push(attribute);
    });

    if($('#depth-checkbox').prop("checked")){
        dataset.depth={};
        dataset.depth.column= $.trim($('#depth-column-input').val());
        dataset.depth.name= $.trim($('#depth-varName-input').val());
        dataset.depth.attribute=[];
        $.each($('#depth-div .depth-attribute'), function( key, value ) {
            var attribute={};
            attribute.key = $.trim($(value).find('#depth-attribute-key-input').val());
            attribute.value= $.trim($(value).find($('#depth-attribute-value-input').val()));
            dataset.depth.attribute.push(attribute);
        });
    }

    //测量值变量
    dataset.meaturedVariables={};
    dataset.meaturedVariables.meaturedVariable=[];
    $.each($('#metured-variable .variable-div-wrapper'),function(key,variableDom){
        var variable={};
        variable.column= $.trim($(variableDom).find('#variable-column-input').val());
        variable.name= $.trim($(variableDom).find('#variable-varName-input').val());
        variable.attribute=[];
        $.each($(variableDom).find('#attribute'),function(key,value){
            var attribute={};
            attribute.key = $.trim($(value).find('#variable-attribute-key-input').val());
            attribute.value= $.trim($(value).find('#variable-attribute-value-input').val());
            variable.attribute.push(attribute);
        });
        dataset.meaturedVariables.meaturedVariable.push(variable);
    });

    //全局变量
    dataset.globalAttributes={};
    dataset.globalAttributes.globalAttribute=[];
    $.each($('#global-attribute .global-attribute-wrapper'),function(key,globalAttribute){
        var attribute={};
        attribute.key= $.trim($(globalAttribute).find('#global-attribute-key-input').val());
        attribute.value= $.trim($(globalAttribute).find('#global-attribute-value-input').val());
        dataset.globalAttributes.globalAttribute.push(attribute);
    });

    $.ajax({
        url: path + '/dataset/create',
        method: 'post',
        dataType: "json",
        contentType: 'application/json;charset=UTF-8',
        data: JSON.stringify(dataset),
        success: function (response) {
            if (!response.success) {
                bootbox.alert(response.message);
            } else {
                bootbox.alert("生成数据成功！", function () {
                    $("#grid").bootstrapTable('refresh');
                    $('#dataset-create-modal').modal('hide');
                });
            }
        },
        error: function (response) {
            bootbox.alert("error");
        }
    });

}

function gridBtnDel() {
    var selectContent = $("#grid").bootstrapTable('getSelections');
    var ids = [];
    if (selectContent.length > 0) {
        $.each(selectContent,function(index,dataset){
            ids.push(dataset.datasetId);
        });
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确定要删除吗？", function (result) {
            if (result) {
                $.ajax({
                    url: path + '/dataset/delete',
                    method: 'post',
                    dataType: "json",
                    contentType: 'application/json;charset=UTF-8',
                    data: JSON.stringify(ids),
                    success: function (response) {
                        if (!response.success) {
                            bootbox.alert(response.message);
                        } else {
                            bootbox.alert("删除数据成功！", function () {
                                $("#grid").bootstrapTable('refresh');
                            });
                        }
                    },
                    error: function (response) {
                        bootbox.alert("error");
                    }
                });
            }
        });
    } else {
        bootbox.alert("请选择要删除的条目！")
    }
}
