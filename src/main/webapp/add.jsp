<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/theme.css">
    <title>增加数据</title>
</head>
<body>
<div>
    <form action="${pageContext.request.contextPath}/add" method="post">
        <h1>
            <a href="home.jsp">
                添加数据
            </a>
        </h1>
        <table id="table">
            <tr>
                <td colspan="2" style="text-align: right">
                    <span>
                        当前选择的表：
                    </span>
                </td>
                <td colspan="2">
                    <select id="table_select" name="table_select">
                        <option selected value="Patient" id="Patient" name="Patient">
                            Patient
                        </option>
                        <option value="Doctor" id="Doctor" name="Doctor">
                            Doctor
                        </option>
                        <option value="Department" id="Department" name="Department">
                            Department
                        </option>
                        <option value="Treatment_Record" id="Treatment_Record" name="Treatment_Record">
                            Treatment Record
                        </option>
                    </select>
                </td>
            </tr>
            <tr id="button_row">
                <td></td>
                <td></td>
                <td colspan="2" style="text-align: center;">
                    <% if (((Integer)session.getAttribute("allPermission")).equals(1)) { %>
                        <button type="submit">Insert</button>
                    <%} else {%>
                        <button id="back_button" style="background-color: darkgrey" type="button" >No permisssion,go back</button>
                    <%}%>
                </td>
            </tr>
        </table>
    </form>
</div>

<script>
    let Patient = ["p_id", "p_record", "p_name", "p_sex", "p_age", "p_phone", "p_date_in", "p_date_out", "p_dept_id"];
    let Department = ["dept_id", "dept_name", "dept_head", "dept_phone"];
    let Doctor = ["doc_id", "doc_name", "doc_phone", "doc_specialty", "doc_dept_id"];
    let Treatment_Record = ["tr_id", "tr_p_id", "tr_doc_id", "tr_date", "tr_diagnosis", "tr_plan"];
    let table = document.getElementById("table");
    const button_row = document.getElementById("button_row")


    table.addEventListener("change", (event) => {
        if (event.target.tagName === "SELECT" && event.target.id === "table_select") {
            const select_table = event.target.value;
            button_row.remove();
            if (select_table === "Patient") {
                add_content(Patient)
            }
            if (select_table === "Doctor") {
                add_content(Doctor)
            }
            if (select_table === "Department") {
                add_content(Department)
            }
            if (select_table === "Treatment_Record") {
                add_content(Treatment_Record)
            }
        }
    })

    // 初始化
    add_content(Patient)

    function add_content(table_name) {
        while (table.rows.length > 1) {
            table.deleteRow(1);
        }
        for (let i = 0; i < table_name.length; i++) {
            // 在table表中生成新行
            let newRow = document.createElement("tr");
            // 新的span
            let newLableCell = document.createElement("td");
            newLableCell.style.width = "25%";
            let newLable = document.createElement("label");
            newLable.for = table_name[i];
            newLable.textContent = table_name[i];
            newLableCell.appendChild(newLable);

            // 新的输入框
            let newInputCell = document.createElement("td");
            newInputCell.style.width = "25%";
            let newInput = document.createElement("input");
            newInput.id = table_name[i];
            newInput.name = table_name[i];
            newInput.placeholder = "请输入值";
            if (newInput.id !== "p_date_out" || newInput.id !== "p_dept_id" || newInput.id !== "doc_dept_id" || newInput !== "tr_plan" || newInput.id !== "tr_diagnosis"){
                newInput.required = true
            }
            newInputCell.appendChild(newInput);
            newRow.appendChild(newLableCell)
            newRow.appendChild(newInputCell)

            i++
            if (i < table_name.length) {
                // 新的span
                newLableCell = document.createElement("td");
                newLableCell.style.width = "25%";
                newLable = document.createElement("label");
                newLable.for = table_name[i];
                newLable.textContent = table_name[i];
                newLableCell.appendChild(newLable);

                // 新的输入框
                newInputCell = document.createElement("td");
                newInputCell.style.width = "25%";
                newInput = document.createElement("input");
                newInput.id = table_name[i];
                newInput.name = table_name[i];
                newInput.placeholder = "请输入值";
                newInputCell.appendChild(newInput);

                newRow.appendChild(newLableCell)
                newRow.appendChild(newInputCell)
            }
            table.appendChild(newRow)
        }
        table.appendChild(button_row)
    }

    let allPermission = <%=session.getAttribute("allPermission")%>
    if (allPermission === 0){
        let back_button = document.getElementById("back_button")
        back_button.addEventListener("click",() => {
            window.location.replace("/DB_2024/home.jsp")
        })

    }

    form.addEventListener("submit",(event) => {

    })
</script>
</body>
</html>
