<%--
  Created by IntelliJ IDEA.
  User: upupup
  Date: 2024/12/8
  Time: 09:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>update status display</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/theme.css">
    <style>
        #error_h1 {
            color: crimson;
        }

        #message_td {
            text-align: center;
        }

    </style>
</head>
<body>

<div>
    <form>
        <% if (session.getAttribute("status").equals("ok")) {%>
        <table>
            <tr>
                <td colspan="2">
                    <h1>执行成功</h1>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="toAdd">添加数据</button>
                </td>
                <td>
                    <button id="toQuery">查询数据</button>
                </td>
            </tr>
        </table>
        <%}%>

        <% if (session.getAttribute("status").equals("failed")) {%>
        <table>
            <tr>
                <td colspan="2">
                    <h1 id="error_h1">
                        执行失败
                    </h1>
                </td>
            </tr>
            <tr>
                <td id="message_td" colspan="2">
                    错误原因：<%=session.getAttribute("errormessage")%><br><br><br>
                </td>
            </tr>
            <tr>
                <td>
                    <button id="errorToAdd">添加数据</button>
                </td>
                <td>
                    <button id="errorToQuery">查询数据</button>
                </td>
            </tr>
        </table>
        <%}%>
    </form>
</div>
<script>
    let toAddButton = document.getElementById("toAdd")
    let errortoAddButton = document.getElementById("errorToAdd")
    let toQueryButton = document.getElementById("toQuery")
    let errortoQuery = document.getElementById("errorToQuery")
    if (toAddButton !== null){
        toAddButton.addEventListener("click",()=>{
            event.preventDefault()
            window.location.replace("/DB_2024/add.jsp")
        })
    }
    if (errortoAddButton !== null){
        errortoAddButton.addEventListener("click",()=>{
            event.preventDefault()
            window.location.replace("/DB_2024/add.jsp")
        })
    }
    if (toQueryButton !== null){
        toQueryButton.addEventListener("click",()=>{
            event.preventDefault()
            window.location.replace("/DB_2024/home.jsp")
        })
    }
    if (errortoQuery !== null){
        errortoQuery.addEventListener("click",()=>{
            event.preventDefault()
            window.location.replace("/DB_2024/home.jsp")
        })
    }
</script>

</body>
</html>
