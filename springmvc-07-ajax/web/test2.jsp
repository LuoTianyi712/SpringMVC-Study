<%--
  Created by IntelliJ IDEA.
  User: LiuFeiyu
  Date: 2021/2/20
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test 2</title>

    <script src="https://cdn.bootcdn.net/ajax/libs/jquery/3.5.1/jquery.js"></script>
    <script>
        $(function (){
            $("#btn").click(function (){
                $.post("${pageContext.request.contextPath}/ajax/a2",function (data){
                    console.log(data);
                    let html = "";
                    for (let i = 0; i < data.length; i++) {
                        html += "<tr>" +
                            "<td>" + data[i].name + "</td>" +
                            "<td>" + data[i].age + "</td>" +
                            "<td>" + data[i].sex + "</td>" +
                            "</tr>"
                    }
                    $("#content").html(html);
                })
            })
        });
    </script>
</head>
<body>

<input id="btn" type="button" value="load data">

<table>
    <thead>
        <tr>
            <td>姓名</td>
            <td>年龄</td>
            <td>性别</td>
        </tr>
    </thead>
    <tbody id="content">
    <tr>
        <td></td>
    </tr>
    </tbody>
</table>

</body>
</html>
