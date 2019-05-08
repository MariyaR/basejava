<%@ page import="com.urise.webapp.model.ContactName" %>
<%@ page import="com.urise.webapp.model.SectionName" %>
<%@ page import="com.urise.webapp.model.ListOfStrings" %>
<%@ page import="com.urise.webapp.model.Organizations" %>
<%@ page import="com.urise.webapp.model.SectionBasic" %>
<%@ page import="com.urise.webapp.util.DateUtil" %>
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
        <hr>
        <c:forEach var="type" items="<%=SectionName.values()%>">
            <c:set var="section" value="${resume.getSectionByName(type)}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.SectionBasic"/>

            <h2><a>${type.content}</a></h2>
                <c:choose>
                    <c:when test="${type=='CurrentPosition'}">
                        <input type="text" name="${type.name()}" size=75 value="${section}"></br>
                    </c:when>
                    <c:when test="${type=='Personal'}">
                        <textarea name="${type.name()}" cols =75 rows=5> <%=section%></textarea>
                    </c:when>

                    <c:when test="${type=='Achievements' || type=='Skills'}">
                        <textarea name='${type}' cols=75 rows=5><%=String.join("\n", ((ListOfStrings) section)
                                        .getList())%></textarea></br>
                    </c:when>

                    <c:when test="${type=='Experience' || type=='Education'}">
                        <c:forEach var="org" items="<%=((Organizations) section).getOrganizations()%>"
                                   varStatus="counter">

                            <dl>
                                <dt>Название учереждения:</dt>
                                <dd><input type="text" name='${type}' size=100 value="${org.homePage.name}"></dd>
                            </dl>
                            <dl>
                                <dt>Сайт учереждения:</dt>
                                <dd><input type="text" name='${type}url' size=100 value="${org.homePage.url}"></dd>
                                </dd>
                            </dl>
                            <br>
                            <div style="margin-left: 30px">
                                <c:forEach var="pos" items="${org.periods}">
                                    <jsp:useBean id="pos" type="com.urise.webapp.model.Organization.DateAndText"/>
                                <dl>
                                    <dt>Начальная дата:</dt>
                                    <dd>
                                        <input type="text" name="${type}${counter.index}startDate" size=10
                                               value="<%=DateUtil.format(pos.getStartDate())%>" placeholder="MM/yyyy">
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>Конечная дата:</dt>
                                    <dd>
                                        <input type="text" name="${type}${counter.index}endDate" size=10
                                               value="<%=DateUtil.format(pos.getEndDate())%>" placeholder="MM/yyyy">
                                </dl>
                                <dl>
                                    <dt>Должность:</dt>
                                    <dd><input type="text" name='${type}${counter.index}title' size=75
                                               value="${pos.position}">
                                </dl>
                                <dl>
                                    <dt>Описание:</dt>
                                    <dd><textarea name="${type}${counter.index}description" rows=5
                                                  cols=75>${pos.responsibilities}</textarea></dd>
                                </dl>

                                </c:forEach>
                            </div>
                        </c:forEach>
                    </c:when>
                </c:choose>
        </c:forEach>
        <hr>
        <button type="submit">Сохранить</button>
        <button onclick="window.history.back()">Отменить</button>
    </form>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>