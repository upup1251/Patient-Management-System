<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>登录 - 数据库连接</title>

    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/theme.css">
</head>
<body>
    <div>
        <form action="${pageContext.request.contextPath}/login" method="post">
            <h1>连接数据库</h1>
            <table>
                <tr>
                    <td><label for="ip_port">Ip-Port:</label></td>
                    <td>
                        <input type="text" id="ip_port" name="ip_port" value="localhost:3306" placeholder="e.g., localhost:3306" required>
                    </td>
                </tr>
                <tr>
                    <td><label for="DataBase_Name">DataBase-Name:</label></td>
                    <td>
                        <input type="text" id="DataBase_Name" name="DataBase_Name" value="hospital" placeholder="Enter your target Database Name"
                               required>
                    </td>
                </tr>
                <tr>
                    <td><label for="username">Username:</label></td>
                    <td>
                        <input type="text" id="username" name="username" value="root" placeholder="Enter your username" required>
                    </td>
                </tr>
                <tr>
                    <td><label for="password">Password:</label></td>
                    <td>
                        <input type="password" id="password" name="password" value="tianqi985" placeholder="Enter your password" required>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <button type="submit">Connect</button>
                    </td>
                </tr>
            </table>
        </form>
    </div>
</body>
</html>