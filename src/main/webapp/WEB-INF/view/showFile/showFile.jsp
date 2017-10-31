<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>文件展示</title>

    <!-- jquery样式 -->
    <!-- 		<script type="text/javascript" language="javascript" src="//code.jquery.com/jquery-1.12.4.js"></script> -->
    <script src="https://cdn.bootcss.com/jquery/3.2.1/jquery.min.js"></script>

    <!-- bootstrap样式 -->
    <link href="https://cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/bootstrap-table.min.js"></script>

    <!-- Latest compiled and minified Locales -->
    <script src="//cdnjs.cloudflare.com/ajax/libs/bootstrap-table/1.11.1/locale/bootstrap-table-zh-CN.min.js"></script>

    <!-- sweetalert弹出框的js样式 -->
    <script src="https://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.js"></script>
    <!-- sweetalert弹出框的css样式 -->
    <link href="https://cdn.bootcss.com/sweetalert/1.1.3/sweetalert.min.css" rel="stylesheet">

    <%--<link href="https://cdn.bootcss.com/jqueryui/1.12.1/jquery-ui.min.css" rel="stylesheet">--%>

    <script type="text/javascript">
        $(function(){
            $('#table').bootstrapTable({
                url:"${pageContext.request.contextPath}/uploadData/showData.action",
                method:'post',
//                contentType:'application/json',
                idField:'id',
                uniqueId:'fileSn',
//                queryParams:function(params) {
//                    params['personId']='restart1025';
//                    return params;
//                },
                search:true,//是否启用搜索框
                searchOnEnterKey:true,//按回车触发搜索方法
                striped:true,		//隔行变色
                singleSelect:true,	//禁止多选
                clickToSelect:true,	//点击行时，自动选择
                showToggle : true,	//切换试图的图标
                undefinedText:"无",	//数据为undefined时显示的字符
                showRefresh:true,	//刷新按钮
                pagination:true,	//显示分页条
                //onlyInfoPagination:true,//仅显示总数据数
                sidePagination:'server',//在哪里进行分页
                showPaginationSwitch:true,//数据条数选择框
                pageNumber:1,			//首页页码
                pageSize:10,			//页面数据条数
                pageList:[10, 25, 50, 100],
                dataField : 'rows',
                totalField : 'total',
                columns: [{
                    field: 'fileSn',
                    title: '文件编号',
                    visible : false
                }, {
                    field: 'fileName',
                    title: '文件名称',
                    align: 'center',
                    formatter: function(value, row) {
                        return "<a onclick=selectSection('" + row.filePath + "','" + row.fileName + "')>" + row.fileName + "</a>";
                    }
                }, {
                    field: 'fileType',
                    title: '文件类型'
                }, {
                    field: 'fileSize',
                    title: '文件大小'
                }, {
                    field: 'uploader',
                    title: '上传人'
                }, {
                    field: 'uploadTime',
                    title: '上传时间'
                }, {
                    field: 'deleted',
                    title: '操作',
                    align: 'center',
                    formatter: function(value, row) {
                        return "<a onclick=deleteFileFun('" + row.fileSn + "','" + row.uploader + "')>删除</a>";
                    }
                }]
            });
        });

        /**
         * 下载文件
         * @param filePath
         * @param fileName
         */
        function selectSection(filePath, fileName){
//            alert("功能开发中, 请耐心等待");
            var url ='${pageContext.request.contextPath}/uploadData/download.action';
            var input1=$("<input>");
            input1.attr("type", "hidden");
            input1.attr("name", "filePath");
            input1.attr("value", filePath);


            var input2=$("<input>");
            input2.attr("type", "hidden");
            input2.attr("name", "fileName");
            input2.attr("value", fileName);
            $('<form method="post" action="' + url + '"></form>').append(input1).append(input2).appendTo('body').submit().remove();
        };

        /**
         * 逻辑删除文件
         * @param event
         */
        function deleteFileFun(fileSn, uploader) {

            swal({
                title: "系统提示",
                text: "你确定要删除该文件吗？删除后不可恢复",
                type: "warning",
                showCancelButton: true,
                closeOnConfirm: false,
                showLoaderOnConfirm: true,
            },
            function(){
                $.ajax({
                    url:'${pageContext.request.contextPath}/uploadData/deleteFile.action',
                    type:'post',
                    contentType:'application/json',
                    data:JSON.stringify({fileSn:fileSn, uploader:uploader}),
                    dataType:'json',
                    success: function(data){
//                        console.log(data);
                        if(data.result)
                        {
                            swal("系统提示", "删除文件成功!", "success")
                        } else {
                            swal("系统提示", "删除文件失败!" + data.msg, "error")
                        }
                        $('#table').bootstrapTable('refresh');
                    }
                },"json");
            });
        };
    </script>
</head>
<body>
    <div style="margin:10px 10%;">
        <table id="table"></table>
    </div>
</body>
</html>