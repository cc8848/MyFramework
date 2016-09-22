<%@ page import="com.sys.util.SuccessMsg" %>
<%@ page import="com.sys.constant.Globals" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<c:set var="actionUrl" >
    ${pageContext.request.contextPath}/login.do
</c:set>
<!DOCTYPE html>
<html>
<head>
    <title>管理系统</title>
    <%@include file="/include/head.jsp"%>
    <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <meta http-equiv="description" content="this is my page">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/plug-in/main/styles/login.css">
    <script>
        if(parent.window!=window){
            parent.location.reload();
        }
        window.onload = function () {
            getMsg();
            submitForm();

        }

        function getMsg(){
            <%
            SuccessMsg msg = (SuccessMsg) request.getAttribute(Globals.SYSTEM_MSG);
            if(msg!=null&&!msg.isSuccess()){
                %>
            alertMsg("<%=msg.getMsg()%>");
            <%
            }
        %>
        }
        function keyDown(){
            if (event.keyCode==13)
                document.getElementsByName("loginForm")[0].submit();
        }

    </script>
</head>
<body onkeydown="keyDown()">
<header>
</header>
<section>
    <div class="wrap">
        <div id="loginblock">
            <form name="loginForm" action="${actionUrl}?login" method="post">
                <div class="logininfo">

                    <dl>
                        <dd class="user">
                            <i class="icon"></i>
                            <input class="text" type="text" name="userName" value="" id="username" placeholder="请输入用户名">
                        </dd>
                    </dl>
                    <dl>
                        <dd class="password">
                            <i class="icon"></i>
                            <input class="text" type="password" name="passwd"  value="" id="password" placeholder="请输入密码">
                        </dd>
                    </dl>
                    <dl>
                        <dd>
                            <input class="submit"  type="submit" value="立即登录" onclick="loginForm.submit()"/>
                        </dd>
                    </dl>
                    <dl>
                        <dd class="msg"><b>注：</b>建议使用 1280*720以上分辨率，IE8.0以上版本浏览器</dd>
                    </dl>

                </div>
            </form>
        </div>
    </div>
</section>
<footer>
    <div class="wrap">
        <p id="copyright">版权所有：有限公司</p>
    </div>
</footer>
</body>
</html>
