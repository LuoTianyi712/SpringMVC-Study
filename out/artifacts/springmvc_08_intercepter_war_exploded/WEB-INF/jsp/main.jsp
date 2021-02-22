<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/22
  Time: 16:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>
main
<p>
    <span>
        ${sessionScope.userLoginInfo}
    </span>
</p>

<p>
    <a href="${pageContext.request.contextPath}/user/logout">logout</a>
</p>

</body>
</html>
