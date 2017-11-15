$(function () {
    $('#grid').bootstrapTable({
        url: path + '/nutrient/query',         //请求后台的URL（*）
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
        uniqueId: "nutrientId",             //每一行的唯一标识，一般为主键列
        showToggle: false,                    //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                   //是否显示父子表
        columns: [
            {
                checkbox: true
            },
            {
                field: "nutrientId", title: "序号", width: 40, align: 'center',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {field: "publishDatasetName", title: "数据集名称", width: 100, valign: 'middle'},
            {field: "nutrientName", title: "文件名称", width: 100, valign: 'middle'},
            {field: "nutrientGenerator", title: "创建者", width: 80, valign: 'middle'},
            {field: "nutrientGenerateTimeStr", title: "创建时间", width: 100, valign: 'middle'},
            {
                field: 'buoyNcId', title: '操作', width: 120, align: 'center',
                formatter: function (value, row, index) {
                    console.log(row);
                    var gridBtnDownload = '<input type="button" value="下载" onclick="gridBtnDownload(\'' + row.nutrientId + '\')"  class="btn btn-primary btn-xs">';
                    if(row.published){
                        var gridBtnPreview = '<input type="button" value="预览" onclick="gridBtnPreview(\'' + row.publishUrl + '\')"  class="btn btn-primary btn-xs">';
                        return gridBtnDownload +'&nbsp;'+gridBtnPreview;

                    }else{
                        var gridBtnPublish = '<input type="button" value="发布" onclick="gridBtnPublish(\'' + row.nutrientId + '\')"  class="btn btn-danger btn-xs">';
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
    }


    $("#file-0a").fileinput({
        language: 'zh', //设置语言
        uploadUrl: path + "/nutrient/generate", //上传的地址
        uploadAsync: true, //默认异步上传
        showUpload: true, //是否显示上传按钮
        showRemove: true, //显示移除按钮
        showPreview: false, //是否显示预览
        showCaption: true,//是否显示标题
        browseClass: "btn btn-primary", //按钮样式
        dropZoneEnabled: false,//是否显示拖拽区域
        maxFileCount: 1, //表示允许同时上传的最大文件个数
        enctype: 'multipart/form-data',
        validateInitialCount: true
    });

    $('#file-0a').on('fileuploaderror', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
        console.log(data);
        console.log('File upload error');
    });

    $('#file-0a').on('fileerror', function (event, data) {
        console.log(data.id);
        console.log(data.index);
        console.log(data.file);
        console.log(data.reader);
        console.log(data.files);
    });

    $('#file-0a').on('fileuploaded', function (event, data, previewId, index) {
        var form = data.form, files = data.files, extra = data.extra,
            response = data.response, reader = data.reader;
    });

    $('#file-0a').on('filebatchuploadcomplete', function (event, files, extra) {
        $("#file-0a").fileinput('reset');
        bootbox.alert("营养盐数据生成成功！");
        $("#grid").bootstrapTable('refresh');
    });

    $('#publishBtn').click(function(){
        var tags=$("#tag").tagsinput('items');
        var publish={
            publishResourceId:$('#dataset-publish-modal').data('resourceid'),
            publishDatasetName: $.trim($('#dataset-name-input').val()),
            publishDatasetDescription: $.trim($('#dataset_desc').val()),
            lstTagName: tags,
            publishResourceType:"ROUTES_NUTRIENT"
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

function gridBtnDownload(row) {
    window.open(path + '/nutrient/downloadFile?nutrientId=' + row);
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

function gridBtnDel() {
    var selectContent = $("#grid").bootstrapTable('getSelections');
    var ids = [];
    if (selectContent.length > 0) {
        $.each(selectContent,function(index,nutrient){
            ids.push(nutrient.nutrientId);
        });
        bootbox.setLocale("zh_CN");
        bootbox.confirm("确定要删除吗？", function (result) {
            if (result) {
                $.ajax({
                    url: path + '/nutrient/delete',
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
    }else {
        bootbox.alert("请选择要删除的条目！")
    }
}

