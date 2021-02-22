<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/22
  Time: 16:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
</head>
<body>

<h1>Login</h1>
<form action="${pageContext.request.contextPath}/user/login" method="post">
<label>
    Username<input type="text" name="username">
    Password<input type="text" name="password">
</label>
    <input type="submit" value="submit">
</form>

</body>
</html>
