<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    <ul>
        <li>
            <a href="<c:url value="/admin-home?page=categories"/>">Quản lý thể loại</a>
        </li>

        <li>
            <a href="<c:url value="/admin-home?page=blogs"/>">Quản lý truyện</a>
        </li>

        <li>
            <a href="<c:url value="/admin-home?page=users"/>">Quản lý người dùng</a>
        </li>

        <li>
            <a href="<c:url value="/admin-home?page=statistic"/>">Thống kê</a>
        </li>
    </ul>
</body>
</html>
