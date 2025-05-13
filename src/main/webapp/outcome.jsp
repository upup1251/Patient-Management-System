<%@ page import="java.sql.ResultSet" %>
<%@ page import="java.sql.SQLException" %><%--
  Created by IntelliJ IDEA.
  User: upupup
  Date: 2024/12/6
  Time: 09:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/theme.css">
    <title>Title</title>
    <STYLE>

        form {
            padding: 0;
            max-width: 95%;
            max-height: 90%;
            overflow: auto;
        }

        form::-webkit-scrollbar {
            width: 0; /* 隐藏水平滚动条 */
            height: 0; /* 隐藏垂直滚动条 */
        }

        table {
            width: 100%;
            max-height: 100%;
            border-collapse: collapse;
            background-color: white;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            /*overflow: auto;*/
        }


        thead {
            background-color: #07c160;
        }

        thead th {
            color: white;
            padding: 15px;
            text-align: left;
            font-size: 16px;
            text-transform: uppercase;
        }

        tbody tr {
            transition: background-color 0.3s ease;
        }

        tbody tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        tbody tr:hover {
            background-color: #e6ffe8;
        }

        tbody td {
            padding: 15px;
            text-align: left;
            font-size: 14px;
            border-bottom: 1px solid #ddd;
        }

        tbody td:last-child {
        }

        tbody td input, tbody td select {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            box-sizing: border-box;
        }

        tbody td input:focus, tbody td select:focus {
            border-color: #07c160;
            outline: none;
            box-shadow: 0 0 5px rgba(7, 193, 96, 0.5);
        }

        tfoot {
            background-color: #f4f4f9;
            text-align: center;
        }

        tfoot td {
            padding: 10px;
            font-size: 14px;
            color: #555;
        }
    </STYLE>
</head>
<body>

<%
    ResultSet rs = (ResultSet) session.getAttribute("rs");
    String[] targets = (String[]) session.getAttribute("target_columns");
//    if (targets != null) {
//        for (String target : targets){
//            System.out.println(target);
//        }
//    } else {
//        System.out.println("targets is null");
//    }
%>

<form id="form" action="${pageContext.request.contextPath}/update" method="post">

    <table id="table">
        <thead>
        <tr>
            <%-- 遍历 targets 数组生成表头 --%>
            <% for (String target : targets) { %>
            <th><%= target %>
            </th>
            <% } %>
        </tr>
        </thead>
        <tbody>
        <%
            // 遍历 ResultSet 数据生成表格内容
            while (rs.next()) {
        %>
        <tr>
            <%
                String p_id = null;
                String doc_id = null;
                String dept_id = null;
                String tr_id = null;
                String log_id = null;
                try {
                    doc_id = rs.getString("doc_id");
                } catch (SQLException e) {
                }
                try {
                    dept_id = rs.getString("dept_id");
                } catch (SQLException e) {
                }
                try {
                    tr_id = rs.getString("tr_id");
                } catch (SQLException e) {
                }
                try {
                    p_id = rs.getString("p_id");
                } catch (SQLException e) {
                }
                try {
                    log_id = rs.getString("log_id");
                } catch (SQLException e) {
                }
                String binding_id = " ";
                for (String target : targets) {
                    if (target.split("_")[0].equals("p")) {
                        binding_id = p_id;
                    } else if (target.split("_")[0].equals("doc")) {
                        binding_id = doc_id;
                    } else if (target.split("_")[0].equals("dept")) {
                        binding_id = dept_id;
                    } else if (target.split("_")[0].equals("tr")) {
                        binding_id = tr_id;
                    } else if (target.split("_")[0].equals("log")) {
                        binding_id = log_id;
                    }
            %>
            <td data-bindingid="<%=binding_id%>">
                <%= rs.getString(target) %>
            </td>
            <% } %>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

</form>
<script>
    let table = document.getElementById("table");
    let form = document.getElementById("form")
    let submit_on = false;

    let allPermission = <%=session.getAttribute("allPermission")%>;
    if (allPermission === 1) {


        table.addEventListener("contextmenu", (event) => {
            event.preventDefault();
            const cell = event.target;

            if (cell.tagName === "TD") {
                // 获得双击处在表格中的列坐标
                let columnIndex = cell.cellIndex;
                // 获取目标表头的内容
                let targetHeaderTextcontent = table.rows[0].cells[columnIndex].textContent
                // 获取双击处属性在其表格中的id值，在前面生成表格时已进行绑定
                let binding_id = cell.getAttribute("data-bindingid");

                const userConfirmed = generate_del_confirem(targetHeaderTextcontent.split("_")[0].trim(), targetHeaderTextcontent.trim(), binding_id.trim(), cell.innerHTML.trim())
                if (userConfirmed) {
                    let hiddeninput = document.createElement("input")
                    hiddeninput.type = "hidden"
                    hiddeninput.name = "delete"
                    // 列名_新值_更改列的id(表名可从列名的前缀获取)
                    hiddeninput.value = targetHeaderTextcontent + "&" + binding_id;
                    form.appendChild(hiddeninput)
                    if (!submit_on) {
                        add_submint();
                    }
                    cell.closest('tr').remove()
                }
            }
        })

        function add_submint() {
            let tr = document.createElement("tr")
            let td = document.createElement("td")
            td.colSpan = 100;
            tr.appendChild(td)
            td.textContent = "Commit Change";
            td.style.cursor = "pointer";
            td.style.textAlign = "center";
            td.style.background = "#07c160";
            td.style.color = "white";
            td.style.padding = "15px";
            td.style.fontSize = "16px";
            td.style.textTransform = "uppercase";
            td.style.fontFamily = "Arial, sans-serif";
            td.style.letterSpacing = "1px";
            td.style.borderTop = "1px solid #ddd";

            td.addEventListener("click", () => {
                form.submit();
            })
            tr.appendChild(td)
            table.appendChild(tr)
            submit_on = true;
        }


        // 双击更改sql，返回string语句给hiddeninput
        table.addEventListener("dblclick", (event) => {
            const cell = event.target;
            if (cell.tagName === "TD") {
                // 获得双击处在表格中的列坐标
                let columnIndex = cell.cellIndex;
                // 获取目标表头的内容
                let targetHeaderTextcontent = table.rows[0].cells[columnIndex].textContent
                // 获取双击处属性在其表格中的id值，在前面生成表格时已进行绑定
                let binding_id = cell.getAttribute("data-bindingid");

                // 获取新的值
                let input = document.createElement("input")
                input.placeholder = "输入新的值"
                let oldValue = cell.innerHTML
                cell.innerHTML = ""
                cell.appendChild(input)
                input.focus();
                input.addEventListener("blur", () => {
                        cell.innerHTML = oldValue
                    }
                )
                input.addEventListener("keydown", (event) => {
                    if (event.key === "Enter") {
                        // 防止冒泡到form表单的提交
                        event.stopPropagation();
                        newValue = input.value;
                        // 确认更改
                        let table_name_prefix = targetHeaderTextcontent.split("_")[0]
                        const UserConfirmed = generate_change_confirem(table_name_prefix, binding_id, targetHeaderTextcontent, oldValue, newValue);
                        if (UserConfirmed) {
                            let hiddeninput = document.createElement("input")
                            hiddeninput.type = "hidden"
                            hiddeninput.name = "update"
                            // 列名_新值_更改列的id(表名可从列名的前缀获取)
                            hiddeninput.value = targetHeaderTextcontent + "&" + newValue + "&" + binding_id;
                            form.appendChild(hiddeninput)
                            if (!submit_on) {
                                add_submint();
                            }
                            cell.innerHTML = ""
                            cell.innerHTML = newValue;
                        } else {
                            cell.innerHTML = ""
                            cell.innerHTML = oldValue;
                        }
                    }
                })
            }
        })

        function generate_change_confirem(table_name_prefix, binding_id, targetHeaderTextcontent, oldValue, newValue) {
            let UserConfirmed;
            var table_name;
            if (table_name_prefix === "tr") {
                table_name = "Treatment_Record"
                UserConfirmed = confirm("是否将表" + table_name.trim() + "中\ntr_id为" + binding_id.trim() + "记录的\n属性" + targetHeaderTextcontent.trim() + "从" + oldValue.trim() + "修改为" + newValue)
            }
            if (table_name_prefix === "dept") {
                table_name = "Department"
                UserConfirmed = confirm("是否将表" + table_name.trim() + "中\ndept_id为" + binding_id.trim() + "记录的\n属性" + targetHeaderTextcontent.trim() + "从" + oldValue.trim() + "修改为" + newValue)
            }
            if (table_name_prefix === "doc") {
                table_name = "Doctor"
                UserConfirmed = confirm("是否将表" + table_name.trim() + "中\ndoc_id为" + binding_id.trim() + "记录的\n属性" + targetHeaderTextcontent.trim() + "从" + oldValue.trim() + "修改为" + newValue)
            }
            if (table_name_prefix === "p") {
                table_name = "Patient"
                UserConfirmed = confirm("是否将表" + table_name.trim() + "中\np_id为" + binding_id.trim() + "记录的\n属性" + targetHeaderTextcontent.trim() + "从" + oldValue.trim() + "修改为" + newValue)
            }
            if (table_name_prefix === "log") {
                table_name = "Audit_Log"
                UserConfirmed = confirm("是否将表" + table_name.trim() + "中\nlog_id为" + binding_id.trim() + "记录的\n属性" + targetHeaderTextcontent.trim() + "从" + oldValue.trim() + "修改为" + newValue)
            }
            return UserConfirmed
        }

        function generate_del_confirem(table_name_prefix, targetHeaderTextcontent, binding_id, value) {
            let UserConfirmed;
            if (table_name_prefix === "tr") {
                UserConfirmed = confirm('你确认删除Treatment_Record表中\ntr_id 为' + binding_id + ';\n' + targetHeaderTextcontent + '为' + value + '\n的记录吗？')
            }
            if (table_name_prefix === "dept") {
                UserConfirmed = confirm('你确认删除Department表中\ndept_id 为' + binding_id + ';\n' + targetHeaderTextcontent + '为' + value + '\n的记录吗？')
            }
            if (table_name_prefix === "doc") {
                UserConfirmed = confirm('你确认删除Doctor表中\ndoc_id 为' + binding_id + ';\n' + targetHeaderTextcontent + '为' + value + '\n的记录吗？')
            }
            if (table_name_prefix === "p") {
                UserConfirmed = confirm('你确认删除Patient表中\np_id 为' + binding_id + ';\n' + targetHeaderTextcontent + '为' + value + '\n的记录吗？')
            }
            if (table_name_prefix === "log") {
                UserConfirmed = confirm('你确认删除Audit_Log表中\nlog_id 为' + binding_id + ';\n' + targetHeaderTextcontent + '为' + value + '\n的记录吗？')
            }
            return UserConfirmed
        }
    }
</script>
</body>
</html>