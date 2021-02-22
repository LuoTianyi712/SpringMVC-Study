<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/20
  Time: 17:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>

    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
    <script>
        function a1(){
            $.post({
                url: "${pageContext.request.contextPath}/ajax/a3",
                data:{"name":$("#name").val()},
                success:function (data) {

                    console.log(data);
                    if (data.toString()==='ok'){
                        $("#userInfo").css("color","green");
                    } else {
                        $("#userInfo").css("color","red");
                    }
                    $("#userInfo").html(data);
                },
            });
        }

        function a2(){
            $.post({
                url: "${pageContext.request.contextPath}/ajax/a3",
                data:{"pwd":$("#pwd").val()},
                success:function (data) {
                    console.log(data);

                    if (data.toString()==='ok'){
                        $("#pwdInfo").css("color","green");
                    } else {
                        $("#pwdInfo").css("color","red");
                    }
                    $("#pwdInfo").html(data);
                },
            });
        }
    </script>

</head>
<body>

<p>
    username: <input id="name" type="text" onblur="a1()">
    <span id="userInfo"></span>
</p>

<p>
    password: <input id="pwd" type="text" onblur="a2()">
    <span id="pwdInfo"></span>
</p>

</body>
</html>
