<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- 插入打印控件 -->
<OBJECT style="display: none"  ID="jatoolsPrinter" CLASSID="CLSID:B43D3361-D075-4BE2-87FE-057188254255"
         codebase="http://${pageContext.request.serverName }:${pageContext.request.serverPort }${pageContext.request.contextPath}/include/print/jatoolsPrinter.cab#version=8,6,0,0"></OBJECT>
<script type="text/javascript">
    //打印
    function doPrint(orientation,preView,doc) {
       if(!orientation)
           orientation=1;
       if(!preView)
            preView=true;
        if(!doc)
            doc=document;
        myDoc = {
            documents: doc,
            settings:{paperName:'a4',orientation:orientation/*,topMargin:100,
                leftMargin:100,
                bottomMargin:100,
                rightMargin:100*/},

            /*
             要打印的div 对象在本文档中，控件将从本文档中的 id 为 'page1' 的div对象，
             作为首页打印id 为'page2'的作为第二页打印            */
            copyrights: '杰创软件拥有版权  www.jatools.com' // 版权声明,必须
        };
        jatoolsPrinter.print(myDoc, preView); // 直接打印，不弹出打印机设置对话框
    }
    //打印预览
    function doPrintView(){
        myDoc = {
            documents: document,
            settings:{paperName:'a4',topMargin:100,
                leftMargin:100,
                bottomMargin:100,
                rightMargin:100},

            /*
             要打印的div 对象在本文档中，控件将从本文档中的 id 为 'page1' 的div对象，
             作为首页打印id 为'page2'的作为第二页打印            */
            copyrights: '杰创软件拥有版权  www.jatools.com' // 版权声明,必须
        };
        jatoolsPrinter.printPreview(myDoc);
    }
</script>