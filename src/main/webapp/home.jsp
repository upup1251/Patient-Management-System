<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>数据查询</title>
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/theme.css">
</head>
<body>
<div>
    <form id="form" action="${pageContext.request.contextPath}/query" method="post">
        <h1>
            <a href="add.jsp">数据查询</a>
        </h1>
        <table id="table">
            <tr>
                <td>
                    <select id="target_column" name="target_column"></select>
                </td>
                <td id="targetColumnDisplay">

                </td>
            </tr>
            <tr>
                <td style="width: 50%;">
                    <select id="condition_select" name="condition_select"></select>
                </td>
                <td style="width: 50%;">
                    <input type="hidden" id="condition_input" name="condition_input" placeholder="请输入条件">
                </td>
            </tr>
            <tr id="query_row">
                <td colspan="2" style="text-align: center;">
                    <button type="submit">query</button>
                </td>
            </tr>
        </table>
    </form>
</div>

<script>
    // 数据加载
    const form = document.getElementById("form")
    const table = document.getElementById("table");
    let buttonRow = document.getElementById("query_row");

    let Patient = ["p_id", "p_record", "p_name", "p_sex", "p_age", "p_phone", "p_date_in", "p_date_out", "p_dept_id"]
    let Doctor = ["doc_id", "doc_name", "doc_phone", "doc_specialty", "doc_dept_id"]
    let Department = ["dept_id", "dept_name", "dept_head", "dept_phone"]
    let Treatment_Record = ["tr_id", "tr_p_id", "tr_doc_id", "tr_date", "tr_diagnosis", "tr_plan"]
    let Audit_log = ["log_id", "log_table_name", "log_action", "log_old_data", "log_new_data", "log_changed_time"]

    let table_select = [0, 0, 0, 0, 0]
    // 添加目标列隐含参数，在submit时，一起发给jsp处理
    let hidden_input_table_select = document.createElement("input")
    hidden_input_table_select.type = 'hidden'
    hidden_input_table_select.name = 'table_select'
    let string_table_select = table_select.join('') // return 00000
    hidden_input_table_select.value = string_table_select
    form.appendChild(hidden_input_table_select)

    let able_condition = ["condition"]
    let able_targets = [" target ",
        "p_id", "p_record", "p_name", "p_sex", "p_age", "p_phone", "p_date_in", "p_date_out", "p_dept_id",
        "dept_id", "dept_name", "dept_head", "dept_phone",
        "doc_id", "doc_name", "doc_phone", "doc_specialty", "doc_dept_id",
        "tr_id", "tr_p_id", "tr_doc_id", "tr_date", "tr_diagnosis", "tr_plan",
        "log_id", "log_table_name", "log_action", "log_old_data", "log_new_data", "log_changed_time"
    ]
    let current_targets = [];

    // 初始化下拉框
    let condition_select = document.getElementById("condition_select");
    let target_column = document.getElementById("target_column")
    condition_change(condition_select, null);
    target_change(target_column, null)

    // 此时条件和目标列改变时互不影响
    // 条件改变时，为新的条件下拉框select设置子元素option
    function condition_change(new_condition_select, value) {
        if (value !== null) {
            // condition删除指定元素，每个元素只能在条件处出现一次
            able_condition = able_condition.filter(item => item !== value);
        }
        new_condition_select.innerHTML = "";
        able_condition.forEach(item => {
            let optionElement = document.createElement("option");
            optionElement.value = item;
            optionElement.textContent = item;
            new_condition_select.appendChild(optionElement);
        });
    }

    // 目标列改变时，为新的目标列下拉框seelct设置子元素option
    function target_change(new_target_select, value) {
        if (value !== null) {
            // 目标列数组添加元素
            current_targets.push(value)

            // 目标列发生变化时，动态调整condtion可选项，只有目标列所在table中的属性才可作为条件
            let prefix = value.substring(0, value.indexOf('_'))
            if (prefix === "p") {
                if (table_select[0] === 0) {
                    // 使用扩展运算符展开 Patient 数组加入，而不是作为一个整体
                    able_condition.push(...Patient)
                    table_select[0] = 1
                    string_table_select = table_select.join('') // return 0000
                    hidden_input_table_select.value = string_table_select
                    Patient.forEach(item => {
                        let optionElement = document.createElement("option");
                        optionElement.value = item;
                        optionElement.textContent = item;
                        condition_select.appendChild(optionElement);
                    });
                }
            } else if (prefix === "doc") {
                if (table_select[1] === 0) {
                    able_condition.push(...Doctor)
                    table_select[1] = 1

                    string_table_select = table_select.join('') // return 0000
                    hidden_input_table_select.value = string_table_select
                    Doctor.forEach(item => {
                        let optionElement = document.createElement("option");
                        optionElement.value = item;
                        optionElement.textContent = item;
                        condition_select.appendChild(optionElement);
                    });

                }
            } else if (prefix === "dept") {
                if (table_select[2] === 0) {
                    able_condition.push(...Department)
                    table_select[2] = 1
                    string_table_select = table_select.join('') // return 0000
                    hidden_input_table_select.value = string_table_select
                    Department.forEach(item => {
                        let optionElement = document.createElement("option");
                        optionElement.value = item;
                        optionElement.textContent = item;
                        condition_select.appendChild(optionElement);
                    });
                }
            } else if (prefix === "tr") {
                if (table_select[3] === 0) {
                    able_condition.push(...Treatment_Record)
                    table_select[3] = 1
                    string_table_select = table_select.join('') // return 0000
                    hidden_input_table_select.value = string_table_select
                    Treatment_Record.forEach(item => {
                        let optionElement = document.createElement("option");
                        optionElement.value = item;
                        optionElement.textContent = item;
                        condition_select.appendChild(optionElement);
                    });
                }

            }
            // 该表不能和别的表连接搜索
            else if (prefix === "log") {
                if (table_select[4] === 0) {
                    able_condition = ["condition", ...Audit_log]
                    able_targets = ["target",...Audit_log]
                    current_targets = [value]
                    table_select[0] = 0
                    table_select[1] = 0
                    table_select[2] = 0
                    table_select[3] = 0
                    table_select[4] = 1
                    string_table_select = table_select.join('') // return 0000
                    hidden_input_table_select.value = string_table_select

                    // 后端删除原有的目标列值
                    const hiddenInputForTarget = document.querySelectorAll(`input[type="hidden"][name="target_columns"]`)
                    hiddenInputForTarget.forEach(input => input.remove())

                    // 前端清空原来的目标列和选项
                    const historyTarget = document.getElementById("targetColumnDisplay")
                    historyTarget.innerHTML = ""
                    // 添加当前选项
                    const span = document.createElement("span");
                    span.style.paddingLeft = "15px";
                    span.style.paddingRight = "15px";
                    span.style.fontWeight = "bold";
                    span.textContent = value;
                    document.querySelector('table tr:first-child td:nth-child(2)').appendChild(span)

                    // 前端清空condition: 删除表格从第三行还是的所有列（包括第三行）
                    const rows = Array.from(table.rows)
                    for (let i = 1; i < rows.length; i++) {
                        table.deleteRow(1);
                    }
                    // 前端添加新的condition select
                    // 在table表中生成新行
                    const newRow = document.createElement("tr");
                    // 新的下拉框
                    const newSelectCell = document.createElement("td");
                    newSelectCell.style.width = "50%";
                    const newSelect = document.createElement("select");
                    // id和name取代原来的下拉框
                    newSelect.id = "condition_select";
                    newSelect.name = "condition_select";
                    // 为该下拉框添加子元素
                    newSelectCell.appendChild(newSelect);

                    // 新的输入框
                    const newInputCell = document.createElement("td");
                    newInputCell.style.width = "50%";
                    const newInput = document.createElement("input");
                    newInput.id = "condition_input";
                    newInput.name = "condition_input";
                    newInput.type = "hidden"
                    newInput.placeholder = "请输入条件";
                    newInputCell.appendChild(newInput);

                    newRow.appendChild(newSelectCell);
                    newRow.appendChild(newInputCell);

                    table.appendChild(newRow);
                    table.appendChild(buttonRow);

                    condition_select = newSelect
                    // 改变条件框
                    condition_select.innerHTML = ""
                    able_condition.forEach(item => {
                        let optionElement = document.createElement("option");
                        optionElement.value = item;
                        optionElement.textContent = item;
                        condition_select.appendChild(optionElement);
                    });
                }

            }

            // 添加目标列隐含参数，在submit时，一起发给jsp处理
            let hiddenInput = document.createElement("input")
            hiddenInput.type = 'hidden'
            hiddenInput.name = 'target_columns'
            hiddenInput.value = value
            form.appendChild(hiddenInput)

            // 可选的目标元素减少，每个目标列只能在目标列处出现一次
            able_targets = able_targets.filter(item => item !== value)
        }
        new_target_select.innerHTML = "";
        able_targets.forEach(item => {
            let optionElement = document.createElement("option");
            optionElement.value = item;
            optionElement.textContent = item;
            new_target_select.appendChild(optionElement);
        });
    }


    // 事件监听，处理下拉框变化
    table.addEventListener("change", (event) => {
            if (event.target.tagName === "SELECT" && event.target.id === "condition_select") {
                // 获取触发事件的条件下拉框
                const target = event.target;
                // 替换该下拉框
                const span = document.createElement("span");
                span.style.paddingLeft = "15px";
                span.style.fontWeight = "bold";
                span.textContent = target.value;

                target.replaceWith(span);

                // 改变该下拉框所对应input的id值
                const inputField = document.getElementById("condition_input");
                inputField.id = target.value;
                inputField.name = target.value;
                inputField.type = "text"

                if (able_condition.length !== 2) {
                    // 如果还有可选条件，则生成新的下拉框和输入框
                    // 否则，不生成

                    // 在table表中生成新行
                    const newRow = document.createElement("tr");
                    // 新的下拉框
                    const newSelectCell = document.createElement("td");
                    newSelectCell.style.width = "50%";
                    const newSelect = document.createElement("select");
                    // id和name取代原来的下拉框
                    newSelect.id = "condition_select";
                    newSelect.name = "condition_select";
                    // 为该下拉框添加子元素
                    condition_change(newSelect, target.value);
                    newSelectCell.appendChild(newSelect);

                    // 新的输入框
                    const newInputCell = document.createElement("td");
                    newInputCell.style.width = "50%";
                    const newInput = document.createElement("input");
                    newInput.id = "condition_input";
                    newInput.name = "condition_input";
                    newInput.type = "hidden"
                    newInput.placeholder = "请输入条件";
                    newInputCell.appendChild(newInput);

                    newRow.appendChild(newSelectCell);
                    newRow.appendChild(newInputCell);

                    buttonRow.remove();
                    table.appendChild(newRow);
                    table.appendChild(buttonRow);
                } else {
                    able_condition = able_condition.filter(item => item !== target.value);
                }
            }
            if (event.target.tagName === "SELECT" && event.target.id === "target_column") {
                // 添加新的span到select框右侧的td
                const target = event.target;
                const span = document.createElement("span");
                span.style.paddingLeft = "15px";
                span.style.paddingRight = "15px";
                span.style.fontWeight = "bold";
                span.textContent = target.value;
                document.querySelector('table tr:first-child td:nth-child(2)').appendChild(span)


                // 生成新的select框
                const newSelect = document.createElement("select");
                newSelect.id = "target_column";
                newSelect.name = "target_column";
                target_change(newSelect, target.value);
                target.replaceWith(newSelect);

                if (current_targets.length % 2 === 0) {
                    document.querySelector('table tr:first-child td:nth-child(2)').appendChild(document.createElement("br"))
                }
            }
        }
    );

</script>
</body>
</html>