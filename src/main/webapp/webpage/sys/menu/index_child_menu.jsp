<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/include/head.jsp"%>
    <link href="${pageContext.request.contextPath}/plug-in/main/styles/style.css" rel="stylesheet">
    <style>
        .div_back_color{
            background:#64b4d5; color:#FFF
        }
    </style>
    <script>
        //打开菜单
        function openMenu(menuId,name,url){
            if($.trim(url).length==0){
                openChildMenu(menuId);

            }else{
               window.parent.openMenu(menuId,name,url);
            }
        }
        //打开子菜单
        function openChildMenu(menuId){
            window.location.href="${pageContext.request.contextPath}/index.do?childmenu&&menuId="+menuId;
        }
        //鼠标移入+背景色
        function addDivBackColor(obj){
            $(obj).addClass("div_back_color");
        }
        //鼠标移出去掉背景色
        function removeBackColor(obj){
            $(obj).removeClass("div_back_color");
        }
        //文本框菜单进行查询
        function inputSearch(title,value,url){
            window.parent.inputSearch(title,value,url);
        }
    </script>
</head>
<body style="background: url(plug-in/main/images/body_bg.jpg) no-repeat top right;">
<div style="overflow:hidden;padding:15px 0 0 0;margin:0;">
<c:forEach items="${menuList}" var="menu">
       <c:if test="${menu.lx!='input'}">
        <div  style="cursor: pointer"  class="con_mod_detail_sec" onclick="openMenu(${menu.id},'${menu.text}','${menu.menuLink}')">
            <ul class="con_mod_img_sec" >
                <li><img src="${pageContext.request.contextPath}/plug-in/main/images/${menu.pic}" height="60"/>&nbsp;&nbsp;${menu.text}</li>
            </ul>

        </div>
       </c:if>
       <c:if test="${menu.lx=='input'}">
           <div  style="cursor: pointer"  class="con_mod_detail_sec" >
               <ul class="con_mod_img_sec">
                   <li>${menu.text}&nbsp;<input type="text" data-options="
                                    prompt: '请输入关键字',
                                    icons:[{
                                            iconCls:'icon-search',
                                            handler: function(e){
                                                var v = $(e.data.target).textbox('getValue');
                                                inputSearch('${menu.text}',v,'${menu.menuLink}')
                                                }
                                           }]
                                " /></li>
               </ul>
           </div>
       </c:if>
</c:forEach>
</div>
</body>
</html>
