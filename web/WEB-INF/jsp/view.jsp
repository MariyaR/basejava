<%@ page import="com.urise.webapp.model.ListOfStrings" %>
<%@ page import="com.urise.webapp.model.Organization" %>
<%@ page import="com.urise.webapp.model.Organizations" %>
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
    <h2>${resume.fullName}&nbsp;<a href="resume?uuid=${resume.uuid}&action=edit"><img src="img/pencil.png"></a></h2>
    <p>
        <c:forEach var="contactEntry" items="${resume.contacts}">
            <jsp:useBean id="contactEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.ContactName, java.lang.String>"/>
                <%=contactEntry.getKey().toHtml(contactEntry.getValue())%><br/>
        </c:forEach>

        <c:forEach var="sectionEntry" items="${resume.sections}">
            <jsp:useBean id="sectionEntry"
                         type="java.util.Map.Entry<com.urise.webapp.model.SectionName, com.urise.webapp.model.SectionBasic>"/>
            <c:set var="name" value="${sectionEntry.key}"/>
            <c:set var="section" value="${sectionEntry.value}"/>
            <jsp:useBean id="section" type="com.urise.webapp.model.SectionBasic"/>

        <c:choose>
        <c:when test="${name=='Personal' || name=='CurrentPosition'}">
                <%=sectionEntry.getKey().getContent() + sectionEntry.getValue()%><br/><br/>
        </c:when>


        <c:when test="${name=='Achievements' || name=='Skills'}">
                <%=sectionEntry.getKey().getContent()%><br/>


        <c:forEach var="item" items="<%=((ListOfStrings) section).getList()%>">
            ${item},
        </c:forEach>

        <br/><br/>

        </c:when>

        <c:when test="${name=='Experience' || name=='Education'}">
                <%=sectionEntry.getKey().getContent()%><br/>
    <ul>
        <c:forEach var="organization" items="<%=((Organizations) section).getOrganizations()%>">
            <jsp:useBean id="organization"
                         type="com.urise.webapp.model.Organization"/>
            <li><%=organization.getTitle() + "    " %>
                <a href="${organization.homePage.url}">${organization.homePage.name}</a>
                <br/>

                <ul>
                    <c:forEach var="position" items="<%=organization.getPeriods()%>">
                        <jsp:useBean id="position"
                                     type="com.urise.webapp.model.Organization.DateAndText"/>
                        <li>
                            <%=position.getStartDate() + " - " + position.getEndDate() + "               " + position.getPosition()%>
                            <br/>
                            <%=position.getResponsibilities()%>
                        </li>


                    </c:forEach>
                </ul>

            </li>

        </c:forEach>
    </ul>

    </c:when>

    </c:choose>


    </c:forEach>
    <p>
</section>
<jsp:include page="fragments/footer.jsp"/>
</body>
</html>
