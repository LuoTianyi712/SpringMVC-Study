<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/20
  Time: 13:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Index</title>
    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
    <script>
      function a1(){
        $.post({
          url:"${pageContext.request.contextPath}/ajax/a1",
          data:{'name':$("#username").val()},
          success:function (data,status) {
            alert(data);
            console.log("data: "+data);
            console.log("status: "+status)
          },
          error:function (data,status) {
            console.log("data: "+data);
            console.log("status: "+status)
          }
        });
      }
    </script>

  </head>
  <body>

  <label>
    <input type="text" id="username" onblur="a1()">
  </label>

  </body>
</html>
