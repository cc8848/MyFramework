<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<script type="text/javascript">
    function checkAll(){
        var checkbox=$(".idInput");
        var titleCheckbox=$("#idCheck");
        if(titleCheckbox.attr("checked")){
            checkbox.attr("checked",true);
        }else{
            checkbox.attr("checked",false);
        }
    }
</script>
<style>
    .tborder{
        border-collapse:collapse;

    }
    .tborder tr td {
        border:1px solid #9a9a9a;
        padding: 15px;
    }
</style>
<table align="center" style="padding: 5px;width: 100%" class="tborder">
    <thead>
    <tr align="center">
        <c:if test="${showId}">
            <td><input type="checkbox" id="idCheck" onclick="checkAll()"></td>
        </c:if>
        <c:forEach var="title" items="${titleMap}" >
            <td>${title.value}</td>
        </c:forEach>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${dataList}" var="data">
        <tr align="center">
            <c:if test="${showId}">
                <td><input type="checkbox" name="idc" class="idInput" value="${data['id']}">
                    <input type="hidden" name="id"  value="${data['id']}">
                </td>
            </c:if>
            <c:forEach var="title" items="${titleMap}" >
                <td>${data[title.key]}</td>
            </c:forEach>
        </tr>
    </c:forEach>
    </tbody>
</table>