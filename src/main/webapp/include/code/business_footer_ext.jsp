<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach var="_module" items="${_modulesList}">
    <pro:authFilter buttonCode="${_module.moduleCode}" actionUrl="${_modulesLink}" show="${_module.defaultShow eq 1}">
        <c:forEach items="${_module.moduleExtDetails}" var="_detail">
            <c:if test="${_detail.moduleType=='view'}">
                <c:import url="/modules/${_detail.moduleLink}"/>
            </c:if>
        </c:forEach>
    </pro:authFilter>
</c:forEach>
