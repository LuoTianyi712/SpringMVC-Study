<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/8
  Time: 10:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/hello" method="post">
    <label>
        <input type="text" name="method">
    </label>
    <input type="submit">
</form>

</body>
</html>
