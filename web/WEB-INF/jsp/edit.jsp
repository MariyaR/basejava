<%@ page import="com.urise.webapp.model.ContactName" %>
<%@ page import="com.urise.webapp.model.SectionName" %>
<%@ page import="com.urise.webapp.model.ListOfStrings" %>
<%@ page import="com.urise.webapp.model.Organizations" %>
<%@ page import="com.urise.webapp.model.SectionBasic" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="css/style.css">
    <jsp:useBean id="resume" type="com.urise.webapp.model.Resume" scope="request"/>
    <title>Резюме ${resume.fullName}</title>
</head>
<body>
<jsp:include page="fragments/header.jsp"/>
<section>
    <form method="post" action="resume" enctype="application/x-www-form-urlencoded">
        <input type="hidden" name="uuid" value="${resume.uuid}">
        <dl>
            <dt>Имя:</dt>
            <dd><input type="text" name="fullName" size=50 value="${resume.fullName}"></dd>
        </dl>
        <h3>Контакты:</h3>
        <c:forEach var="type" items="<%=ContactName.values()%>">
            <dl>
                <dt>${type.content}</dt>
                <dd><input type="text" name="${type.name()}" size=30 value="${resume.getContact(type)}"></dd>
            </dl>
        </c:forEach>
        <h3>Секции:</h3>
        <c:forEach var="type" items="<%=SectionName.values()%>">
            <c:set var="section" value="${resume.getSectionByName(type)}"/>

            <dl>

                <dt>${type.content}</dt>

                <dd><c:choose>
                    <c:when test="${type=='Personal' || type=='CurrentPosition'}">
                        <input type="text" name="${type.name()}" size=30 value="${section}"></br>
                    </c:when>

                    <c:when test="${type=='Achievements' || type=='Skills'}">
                        <jsp:useBean id="section" type="com.urise.webapp.model.SectionBasic"/>
                        <%--<jsp:useBean id="section" type="com.urise.webapp.model.SectionBasic"/>
                        <c:choose>
                            <c:when test="${section!=null}"> --%>
                                <textarea name='${type}' cols=50
                                          rows=8><%=String.join("\n", ((ListOfStrings) section)
                                        .getList())%></textarea></br>
                           <%-- </c:when>
                            <c:when test="${section==null}">
                                <textarea name='${type}' cols=50 rows=8></textarea></br>
                            </c:when>
                        </c:choose> --%>
                    </c:when>


                    <c:when test="${type=='Experience' || type=='Education'}">
                    <%--<c:choose>
                        <c:when test="${section!=null}"> --%>
                            <c:forEach var="organization" items="<%=((Organizations) section).getOrganizations()%>">
                                <jsp:useBean id="organization"
                                             type="com.urise.webapp.model.Organization"/>
                                <input type="text" name="${type.name()}" size=30 value="<%=organization.getTitle()%>"></br>

                                <a href="${organization.homePage.url}">${organization.homePage.name}</a>
                                <br/>


                                <c:forEach var="position" items="<%=organization.getPeriods()%>">
                                    <jsp:useBean id="position"
                                                 type="com.urise.webapp.model.Organization.DateAndText"/>

                                    <%=position.getStartDate() + " - " + position.getEndDate() + "               " + position.getPosition()%>
                                    <br/>
                                    <%=position.getResponsibilities()%>

                                </c:forEach>

                            </c:forEach>

                        <%--</c:when>
                    </c:choose> --%>
                    </c:when>
                </c:choose></dd>

            </dl>


            <%-- <dl>
                 <dt>${type.content}</dt>
                 <dd><input type="text" name="${type.name()}" size=30 value="${resume.getSection(type)}"></dd>
             </dl> --%>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>