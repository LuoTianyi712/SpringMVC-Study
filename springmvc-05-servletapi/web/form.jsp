<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/10
  Time: 8:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Form</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/encoding/test1" method="post">
    <label>
        <input type="text" name="name">
        <input type="submit">
    </label>
</form>
</body>
</html>
