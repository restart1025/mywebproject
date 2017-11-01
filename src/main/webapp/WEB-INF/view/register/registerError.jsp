<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>注册失败</title>

    <meta name="keywords" content="restart1025">
    <meta name="description" content="个人主页">

    <link rel="shortcut icon" href="favicon.ico">
    <link href="${pageContext.request.contextPath}/bootstrapHplus/css/bootstrap.min.css-v=3.3.5.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/bootstrapHplus/css/font-awesome.min.css-v=4.4.0.css" rel="stylesheet">

    <link href="${pageContext.request.contextPath}/bootstrapHplus/css/animate.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/bootstrapHplus/css/style.min.css-v=4.0.0.css" rel="stylesheet"><base target="_blank">
</head>
<body class="gray-bg">
    <div class="wrapper wrapper-content">
        <div class="row">
            <div class="col-sm-12">
                <div class="middle-box text-center animated fadeInRightBig">
                    <h3 class="font-bold">系统提示</h3>
                    <div class="error-desc">
                        抱歉, 由于系统问题, 您的注册失败, 请重新前往登录页面注册<br/>
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary m-t">返回主页</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/bootstrapHplus/js/jquery.min.js-v=2.1.4.js"></script>
    <script src="${pageContext.request.contextPath}/bootstrapHplus/js/bootstrap.min.js-v=3.3.5.js"></script>
    <script src="${pageContext.request.contextPath}/bootstrapHplus/js/content.min.js-v=1.0.0.js"></script>
</body>
</html>