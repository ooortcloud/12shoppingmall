<%@ page contentType="text/html; charset=EUC-KR" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<title>��� ��ǰ ����</title>
</head>

<body>
	�ֱ� �� ��ǰ ���
<br>
<br>

<c:if test="${ !empty requestScope.histories }">
	<c:set var="historyNo" value="${ historyNo}" />
	<c:set var="i" value="0" />
	<c:forEach var="history" items="${histories }">
			<a href="/product/getProduct/search?prodNo=${historyNo[i] }"	target="rightFrame">${history }</a>
			<br/>
			<c:set var="i" value="${i+1 }" />
	</c:forEach>
</c:if>

</body>
</html>