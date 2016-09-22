<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/include/include.jsp" %>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <title>管理系统</title>
    <%@include file="/include/head.jsp" %>
    <link href="plug-in/main/styles/style2.css" rel="stylesheet">
    <style>
        table {
            border-collapse: collapse;
        }

        table tr td {
            border: 1px solid #d8d8d8;
            height: 30px;
        }
    </style>
    <script>
        var menu_window_state = false;
        function resizeMenuWindow(w, h) {
            if (menu_window_state) {
                $("#menu_window").window("resize", {width: w, height: h});
            }
        }

        var bussinessMenuPercent = {heightP: 0.9, widthP: 0.9};
        var bussinessChidMenuPercent = {heightP: 0.85, widthP: 0.85};
        var childMenuPercent = {heightP: 0.6, widthP: 0.5};
        window.onresize = function () {
            $("#menu_window").window("resize", {
                width: fixWidth(bussinessMenuPercent.widthP),
                height: fixHeight(bussinessMenuPercent.heightP)
            });
            $("#menu_child_window").window("resize", {
                width: fixWidth(childMenuPercent.widthP),
                height: fixHeight(childMenuPercent.heightP)
            });
            $("#bussiness_child_window").window("resize", {
                width: fixWidth(bussinessChidMenuPercent.widthP),
                height: fixHeight(bussinessChidMenuPercent.heightP)
            });
        }
        //打开菜单
        function openMenu(menuId, name, url) {
            if ($.trim(url).length == 0) {
                openChildMenu(menuId, name);
            } else {
                var tabsNum = $('#main_content_tabs').tabs('tabs');
                if (tabsNum.length < 12) {
                    $("#menu_child_window").dialog("close");
                    addTab_in_mainContent({
                        title: name,
                        closable: true,
                        content: '<iframe src="'
                        + url
                        + '" frameborder="0" style="border:0;width:100%;height:99%;"></iframe>'
                    });
                } else {
                    alertMsg("选项卡个数已达到最大!")
                    return false;
                }
            }
        }
        /**
         * 操作菜单树,单击菜单树上的叶子节点时,在主窗口创建tab页
         */
        function addTab_in_mainContent(opts) {
            // 获取tab
            var _tab = $('#main_content_tabs');

            // 如果已经创建tab,就选择tab
            if (_tab.tabs('exists', opts.title)) {
                _tab.tabs('close', opts.title);
                _tab.tabs('add', opts);
                //_tab.tabs('select', opts.title);
            } else {
                // tab不存在,创建tab
                _tab.tabs('add', opts);
            }
        }
        //转入请求
        function openBussiness(title, url) {
            $("#menu_window").window("setTitle", title);
            $("#menu_window").window("resize", {
                width: fixWidth(bussinessMenuPercent.widthP),
                height: fixHeight(bussinessMenuPercent.heightP)
            });
            $("#menu_link").attr("src", "");
            $("#menu_link").attr("src", url);
            openEasyuiWindow("menu_window");
        }
        //打开业务页面中的对话框
        function openBussinessChildMenu(title, url) {
            $("#bussiness_child_window").window("setTitle", title);
            $("#bussiness_child_window").window("resize", {
                width: fixWidth(bussinessChidMenuPercent.widthP),
                height: fixHeight(bussinessChidMenuPercent.heightP)
            });
            $("#bussiness_child_link").attr("src", "");
            $("#bussiness_child_link").attr("src", url);
            openEasyuiWindow("bussiness_child_window");
        }
        //打开子菜单
        function openChildMenu(menuId, name) {

            var url = "${pageContext.request.contextPath}/index.do?childmenu&&menuId=" + menuId;
            window.parent.openMenu(menuId, name, url);
        }
        //搜索
        function doSearch(value) {
            if ($.trim(value).length > 0) {
                $("#menu_child_window").window("setTitle", "搜索");
                $("#menu_child_link").attr("src", encodeURI("${pageContext.request.contextPath}/index.do?searchmenu&&menuName=" + value));
                openEasyuiWindow("menu_child_window");
            }
        }
        //打开设置
        function openSet() {
            $("#set_dia").dialog("open");
        }
        function bottomLeft() {
            $.messager.show({
                title: '系统设置',
                msg: '<ul><li>用户管理</li><li>角色管理</li></ul>',
                height: 300,
                width: 200,
                showType: 'show',
                style: {
                    left: 0,
                    right: '',
                    top: '',
                    bottom: -document.body.scrollTop - document.documentElement.scrollTop
                }
            });
        }
        //鼠标移入+背景色
        function addDivBackColor(obj) {
            $(obj).addClass("div_back_color");
        }
        //鼠标移出去掉背景色
        function removeBackColor(obj) {
            $(obj).removeClass("div_back_color");
        }
        //文本框菜单进行查询
        function inputSearch(title, value, url) {
            if ($.trim(value).length == 0) {
                slideMsgInfo("请输入关键字");
                return false;
            }
            url += "&keyWord=" + encodeURI(value);
            window.parent.openMenu(null, title, url);
        }
        function refreshBussinessData() {
            window.frames["menu_link"].getDataList()
        }
        function showFileList(filePath, showType, type) {
            var url = context + "/file.do?toupload&&actionUrl=${actionUrl}&&filePath=" + encodeURI(filePath);
            if (showType)
                url += "&&showType=" + showType;
            if (type)
                url += "&&type=" + type;
            document.getElementById("fileListFrame").src = url;
            openEasyuiDialog("fileList");

        }
        /**
         *查看文件
         * @param filePath
         */
        function viewFile(filePath) {
            var file = "${pageContext.request.contextPath}/file.do?view&&filePath=" + encodeURI(filePath);
            showFrameDialog("fileDia", "fileFrame", file);
        }
        //获取代办事项数量
        function getTaskCount() {
            ajaxJson("${pageContext.request.contextPath}/activiti/task.do?getTaskCount", {}, function (data) {
                if (data > 0) {
                    $("#dbsx").text("(" + data + ")");
                } else {
                    $("#dbsx").text("");
                }
            });
        }
        //获取任务列表
        function getTaskList() {
            openBussiness("待办事项", "activiti/task.do?showSimpleTaskListView");
        }
        //lt@ 2015.5.15
        function getTaskList2(name, url) {
            var tabsNum = $('#main_content_tabs').tabs('tabs');

            if (tabsNum.length < 12) {
                $("#menu_child_window").dialog("close");
                addTab_in_mainContent({
                    title: name,
                    closable: true,
                    content: '<iframe src="'
                    + url
                    + '" frameborder="0" style="border:0;width:100%;height:99%;"></iframe>'
                });
            } else {
                alertMsg("选项卡个数已达到最大!")
                return false;
            }
        }

        // lt@ 2015:5.15
        function getApply2(name, url) {
            var tabsNum = $('#main_content_tabs').tabs('tabs');
            if (tabsNum.length < 12) {
                $("#menu_child_window").dialog("close");
                addTab_in_mainContent({
                    title: name,
                    closable: true,
                    content: '<iframe src="'
                    + url
                    + '" frameborder="0" style="border:0;width:100%;height:99%;"></iframe>'
                });
            } else {
                alertMsg("选项卡个数已达到最大!")
                return false;
            }

        }
        window.onload = function () {

        }
    </script>
</head>


<body class="easyui-layout" style="background:#fff">

<div data-options="region:'north'" style="width:100%;height:79px;background: #00a3e6;overflow: hidden">
    <div class="header">
        <div class="right">
            <ul>
                <li class="admin">${login_session_user.realName}，欢迎您！</li>
                <li class="exit"><a href="${pageContext.request.contextPath}/login.do?loginout"
                                    data-options="plain:true">安全退出</a></li>
            </ul>
        </div>
        <div class="clear"></div>
    </div>
</div>
<div data-options="region:'west'" style="width: 200px;background:#258dc2;overflow-y:scroll; ">
    <h4 style="height:50px;line-height:50px;border-bottom:1px solid #f1f1f1;padding-left:20px;font-size:15px;color:#fff;background: #1271A1;">
        ──　导航菜单　──</h4>
    <ul class="accordion">
        <c:forEach var="menu" items="${menuList}">
            <c:if test="${menu.menuLink=='' && menu.lx!='input'}">
                <li id="${menu.id}" class="files"><a href="#one">${menu.text}</a></li>
            </c:if>
            <c:if test="${menu.menuLink!=''&& menu.lx!='input'}">
                <li id="${menu.id}"><a href="#one"
                                       onclick="openMenu(${menu.id},'${menu.text}','${menu.menuLink}')">${menu.text}</a>
                </li>
            </c:if>
        </c:forEach>
    </ul>
</div>

<div data-options="region:'center',onResize:function(w,h){resizeMenuWindow(w,h);}" align="center">
    <div id="main_content_tabs" style="overflow: hidden;">
        <div title="我的桌面" style=" background: url(plug-in/main/images/body_bg.jpg) no-repeat bottom right;">
            <div class="wrap" style="width: 1170px;height:auto;text-align:center;margin-top:50px;">
            </div>
        </div>
    </div>
    <div class="easyui-window" id="menu_window" style="width:900px;height:550px;"
         data-options="collapsible:false,closed:true,shadow:true,minimizable:false,maximizable:false,inline:true,onOpen:function(){menu_window_state=true;},draggable:false,maximized:true,onDrag:onDrag,onClose:function(){menu_window_state=false;$('#menu_link').attr('src','');}">
        <iframe style="height: 99%;width: 100%" frameborder="0" class="iFra" id="menu_link" name="menu_link"></iframe>
    </div>
    <div class="easyui-window" id="menu_child_window" style="width:600px;height:400px;"
         data-options="collapsible:false,closed:true,minimizable:false,maximizable:false,inline:true,onClose:function(){$('#menu_child_link').attr('src','');}">
        <iframe style="height: 99%;width: 100%;" frameborder="0" class="iFra" id="menu_child_link"
                name="menu_child_link"></iframe>
    </div>
    <div class="easyui-window" id="bussiness_child_window" style="width:600px;height:400px;"
         data-options="collapsible:false,closed:true,minimizable:false,maximizable:false,onClose:function(){$('#bussiness_child_link').attr('src','');refreshBussinessData();}">
        <iframe style="height: 99%;width: 100%" frameborder="0" class="iFra" id="bussiness_child_link"></iframe>
    </div>
    <div id="fileList" title="文件列表" class="easyui-dialog" data-options="closed:true,width:500,height:300">
        <iframe scrolling="auto" id="fileListFrame" frameborder="0" src="" style="width:99%;height:99%;"></iframe>
    </div>
    <div id="fileDia" title="浏览文件" class="easyui-dialog" data-options="closed:true,width:800,height:500">
        <iframe scroling="auto" id="fileFrame" frameborder="0" src="" style="width:99%;height:99%;"></iframe>
    </div>

</div>

<div id="main_content_tabs_menu" style="width:120px;display:none;">
    <div type="close">关闭</div>
    <div type="refresh">刷新</div>
    <div type="closeOther">关闭其它</div>
    <div type="closeAll">关闭所有</div>
</div>

</body>
</html>

<script type="text/javascript">
    $(document).ready(function () {
        var $main_content_tabs = $('#main_content_tabs').tabs({
            fit: true,
            border: false,
            onContextMenu: function (e, title) {
                e.preventDefault();
                if (title == "我的桌面") {
                    return false;
                }
                // 展示菜单选项卡
                $('#main_content_tabs_menu').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data('tabTitle', title);
            }
        });
        // 定义标签页上的右键菜单
        $('#main_content_tabs_menu').menu(
                {
                    onClick: function (item) {

                        // 传入data{'tabTitle':title}
                        var currentTabTitle = $(this).data('tabTitle');

                        // <div type="refresh">刷新</div>
                        var type = $(item.target).attr('type');

                        // 刷新
                        if (type === 'refresh') {
                            var currentTab = $main_content_tabs.tabs('getTab',
                                    currentTabTitle);
                            currentTab.panel('refresh');
                            return;
                        }
                        // 关闭当前Tab页
                        if (type === 'close') {
                            var currentTab = $main_content_tabs.tabs('getTab',
                                    currentTabTitle);
                            if (currentTab.panel('options').closable) {
                                // 关闭当前Tab页
                                $main_content_tabs.tabs('close', currentTabTitle);


                            }
                            return;
                        }

                        // 关闭其它Tab页
                        var allTabs = $main_content_tabs.tabs('tabs');
                        var closeTabsTitle = [];

                        // 遍历所有的Tab页
                        $.each(allTabs, function () {
                            var opt = $(this).panel('options');
                            if (opt.closable && opt.title != currentTabTitle
                                    && type === 'closeOther') {
                                closeTabsTitle.push(opt.title);
                            } else if (opt.closable && type === 'closeAll') {
                                closeTabsTitle.push(opt.title);
                            }
                        });

                        for (var i = 0; i < closeTabsTitle.length; i++) {
                            // 依次关闭Tab页
                            $main_content_tabs.tabs('close', closeTabsTitle[i]);
                        }
                    }
                });

        // Store variables
        var accordion_head = $('.accordion > li > a');
        // Click function
        accordion_head.on('click', function (event) {
            // Disable header links
            event.preventDefault();
            var mmm = $(this);
            // Show and hide the tabs on click
            if (mmm.attr('class') != 'active') {
                var menuId = mmm.parents('li').attr('id');
                var s = "";
                ajaxJson(actionUrl + "?selectChildByPId", {pId: menuId}, function (data) {

                    if (assertAjaxSuccess(data.menuEntityList)) {
                        if (data.flag == 0) {
                            var s1 = '<ul class="sub-menu">';
                            var s3 = "";
                            for (var i = 0; i < data.menuEntityList.length; i++) {
                                s3 = s3 + '<li><a href="#" onclick="openMenu(' + data.menuEntityList[i].id + ',\'' + data.menuEntityList[i].text + '\',\'' + data.menuEntityList[i].menuLink + '\')">' + data.menuEntityList[i].name + '</a></li>';
                            }
                            var s2 = '</ul>';
                            s = s1 + s3 + s2;
                            mmm.parents('li').append(s);
                            var accordion_body = $('.accordion  li > .sub-menu');
                            accordion_body.slideUp('normal');
                            mmm.next().stop(true, true).slideToggle('normal');
                            accordion_head.removeClass('active');
                            mmm.addClass('active');
                        }
                    }
                });
            } else {
                var accordion_body = $('.accordion  li > .sub-menu');
                accordion_body.slideUp('normal');
                accordion_head.removeClass('active');
            }
        });

    });
</script>

