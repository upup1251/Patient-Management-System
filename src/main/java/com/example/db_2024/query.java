package com.example.db_2024;

import jdk.jshell.JShell;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/query")
public class query extends HttpServlet {
    // pstmt的位置参数
    private int pos = 1;
    private PreparedStatement pstmt = null;

    // Department 表字段
    private String dept_id = null;       // 科室ID
    private String dept_name = null;    // 科室名称
    private String dept_head = null;    // 科室负责人
    private String dept_phone = null;   // 科室电话

    // Patient 表字段
    private String p_id = null;         // 病患ID
    private String p_record = null;     // 病历号
    private String p_name = null;       // 病患姓名
    private String p_sex = null;        // 性别
    private String p_age = null;       // 年龄
    private String p_phone = null;      // 联系电话
    private String p_date_in = null;    // 入院日期
    private String p_date_out = null;   // 出院日期
    private String p_dept_id = null;   // 所属科室ID（外键）

    // Doctor 表字段
    private String doc_id = null;       // 医生ID
    private String doc_name = null;     // 医生姓名
    private String doc_phone = null;    // 医生电话
    private String doc_specialty = null; // 专科信息
    private String doc_dept_id = null;   // 所属科室ID（外键）

    // Treatment_Record 表字段
    private String tr_id = null;        // 记录ID
    private String tr_p_id = null;      // 病患ID（外键）
    private String tr_doc_id = null;    // 医生ID（外键）
    private String tr_date = null;      // 诊疗时间
    private String tr_diagnosis = null; // 诊断结果
    private String tr_plan = null;      // 治疗方案
    private String table_select = null;

    private String log_id = null;
    private String log_action = null;
    private String log_table_name = null;
    private String log_old_data = null;
    private String log_new_data = null;
    private String log_changed_time = null;

    private String getNullableParameter(HttpServletRequest req, String paramName) {
        String value = req.getParameter(paramName);
        if (value == null) {
            return null;
        } else if (value.isEmpty()) {
            return null;
        } else {
            return value.trim();
        }
    }

    private void getPatientParams(HttpServletRequest req) {
        // 接收 Patient 表字段
        p_id = getNullableParameter(req, "p_id");
        p_record = getNullableParameter(req, "p_record");
        p_name = getNullableParameter(req, "p_name");
        p_sex = getNullableParameter(req, "p_sex");
        p_age = getNullableParameter(req, "p_age");
        p_phone = getNullableParameter(req, "p_phone");
        p_date_in = getNullableParameter(req, "p_date_in");
        p_date_out = getNullableParameter(req, "p_date_out");
        p_dept_id = getNullableParameter(req, "p_dept_id"); // 外键接收
    }

    private void getDoctorParams(HttpServletRequest req) {
        // 接收 Doctor 表字段
        doc_id = getNullableParameter(req, "doc_id");
        doc_name = getNullableParameter(req, "doc_name");
        doc_phone = getNullableParameter(req, "doc_phone");
        doc_specialty = getNullableParameter(req, "doc_specialty");
        doc_dept_id = getNullableParameter(req, "doc_dept_id");
    }

    private void getDepartmentParams(HttpServletRequest req) {
        // 接收 Department 表字段
        dept_id = getNullableParameter(req, "dept_id");
        dept_name = getNullableParameter(req, "dept_name");
        dept_head = getNullableParameter(req, "dept_head");
        dept_phone = getNullableParameter(req, "dept_phone");
    }

    private void getTreatment_RecordParams(HttpServletRequest req) {
        // 接收 Treatment_Record 表字段
        tr_id = getNullableParameter(req, "tr_id");
        tr_p_id = getNullableParameter(req, "tr_p_id");
        tr_doc_id = getNullableParameter(req, "tr_doc_id");
        tr_date = getNullableParameter(req, "tr_date");
        tr_diagnosis = getNullableParameter(req, "tr_diagnosis");
        tr_plan = getNullableParameter(req, "tr_plan");
    }

    private void getAudit_LogParams(HttpServletRequest req) {
        // 接收 Patient 表字段
        log_id = getNullableParameter(req, "log_id");
        log_table_name = getNullableParameter(req, "log_table_name");
        log_action = getNullableParameter(req, "log_action");
        log_old_data = getNullableParameter(req, "log_old_data");
        log_new_data = getNullableParameter(req, "log_new_data");
        log_changed_time = getNullableParameter(req, "log_changed_time");
    }

    private String getPatientWhere() {
        String where = "";
        if (p_id != null) {
            where = where + " AND p_id LIKE ? ";
        }
        if (p_record != null) {
            where = where + " AND p_record LIKE ? ";
        }
        if (p_name != null) {
            where = where + " AND p_name LIKE ? ";
        }
        if (p_sex != null) {
            where = where + " AND p_sex = ? ";
        }
        // todo: int
        if (p_age != null) {
            where = where + " AND p_age = ? ";
        }
        if (p_phone != null) {
            where = where + " AND p_phone LIKE ? ";
        }
        // todo: data
        if (p_date_in != null) {
            where = where + " AND p_date_in = ? ";
        }
        // todo: data
        if (p_date_out != null) {
            where = where + " AND p_date_out = ? ";
        }
        if (p_dept_id != null) {
            where = where + " AND p_dept_id LIKE ? ";
        }
        return where;
    }

    private String getDoctorWhere() {
        String where = "";
        if (doc_id != null) {
            where = where + " AND doc_id LIKE ? ";
        }
        if (doc_name != null) {
            where = where + " AND doc_name LIKE ? ";
        }
        if (doc_phone != null) {
            where = where + " AND p_phone LIKE ? ";
        }
        if (doc_specialty != null) {
            where = where + " AND doc_specialty LIKE ? ";
        }
        if (doc_dept_id != null) {
            where = where + " AND doc_dept_id LIkE ? ";
        }
        return where;
    }

    private String getDepartmentWhere() {
        String where = "";
        if (dept_id != null) {
            where = where + " AND dept_id LIKE ? ";
        }
        if (dept_name != null) {
            where = where + " AND dept_name LIKE ? ";
        }
        if (dept_head != null) {
            where = where + " AND dept_head LIKE ? ";
        }
        if (dept_phone != null) {
            where = where + " AND dept_phone LIKE ? ";
        }
        return where;
    }

    private String getTreatment_RecordWhere() {
        String where = "";
        if (tr_id != null) {
            where = where + " AND tr_id LIKE ? ";
        }
        if (tr_p_id != null) {
            where = where + " AND tr_p_id LIKE ? ";
        }
        if (tr_doc_id != null) {
            where = where + " AND tr_doc_id LIKE ? ";
        }
        // todo: data
        if (tr_date != null) {
            where = where + " AND tr_date = ? ";
        }
        if (tr_diagnosis != null) {
            where = where + " AND tr_diagnosis LIkE ? ";
        }
        if (tr_plan != null) {
            where = where + " AND tr_plan LIkE ? ";
        }
        return where;
    }

    private String getAudit_LogWhere() {
        String where = "";
        if (log_id != null) {
            where = where + " AND log_id LIKE ? ";
        }
        if (log_table_name != null) {
            where = where + " AND log_table_name LIKE ? ";
        }
        if (log_action != null) {
            where = where + " AND log_action LIKE ? ";
        }
        if (log_old_data != null) {
            where = where + " AND log_old_data LIKE ? ";
        }
        if (log_new_data != null) {
            where = where + " AND log_new_data LIkE ? ";
        }
        if (log_changed_time != null) {
            where = where + " AND log_change_time LIkE ? ";
        }
        return where;
    }

    private void setPatientPstmtParams() throws SQLException {
        if (p_id != null) {
            pstmt.setString(pos, p_id);
            pos++;
        }
        if (p_record != null) {
            pstmt.setString(pos, p_record);
            pos++;
        }
        if (p_name != null) {

            pstmt.setString(pos, p_name);
            pos++;
        }
        if (p_sex != null) {
            pstmt.setString(pos, p_sex);
            pos++;
        }
        // todo: int
        if (p_age != null) {
            pstmt.setString(pos, p_age);
            pos++;
        }
        if (p_phone != null) {
            pstmt.setString(pos, p_phone);
            pos++;
        }
        // todo: data
        if (p_date_in != null) {
            pstmt.setString(pos, p_date_in);
            pos++;
        }
        // todo: data
        if (p_date_out != null) {
            pstmt.setString(pos, p_date_out);
            pos++;
        }
        if (p_dept_id != null) {
            pstmt.setString(pos, p_dept_id);
            pos++;
        }
    }

    private void setDoctorPstmtParams() throws SQLException {
        if (doc_id != null) {
            pstmt.setString(pos, doc_id);
            pos++;
        }
        if (doc_name != null) {
            pstmt.setString(pos, doc_name);
            pos++;
        }
        if (doc_phone != null) {
            pstmt.setString(pos, doc_phone);
            pos++;
        }
        if (doc_specialty != null) {
            pstmt.setString(pos, doc_specialty);
            pos++;
        }
        if (doc_dept_id != null) {
            pstmt.setString(pos, doc_dept_id);
            pos++;
        }
    }

    private void setDepartmentPstmtParams() throws SQLException {
        if (dept_id != null) {
            pstmt.setString(pos, dept_id);
            pos++;

        }
        if (dept_name != null) {
            pstmt.setString(pos, dept_name);
            pos++;
        }
        if (dept_head != null) {
            pstmt.setString(pos, dept_head);
            pos++;
        }
        if (dept_phone != null) {
            pstmt.setString(pos, dept_phone);
            pos++;
        }
    }

    private void setTreatment_RecordPstmtParams() throws SQLException {
        if (tr_id != null) {
            pstmt.setString(pos, tr_id);
            pos++;
        }
        if (tr_p_id != null) {
            pstmt.setString(pos, tr_p_id);
            pos++;
        }
        if (tr_doc_id != null) {
            pstmt.setString(pos, tr_doc_id);
            pos++;
        }
        // todo: data
        if (tr_date != null) {
            pstmt.setString(pos, tr_date);
            pos++;
        }
        if (tr_diagnosis != null) {
            pstmt.setString(pos, tr_diagnosis);
            pos++;
        }
        if (tr_plan != null) {
            pstmt.setString(pos, tr_plan);
            pos++;
        }
    }

    private void setAudit_LogPstmtParams() throws SQLException {
        if (log_id != null) {
            pstmt.setString(pos, log_id);
            pos++;
        }
        if (log_table_name != null) {
            pstmt.setString(pos, log_table_name);
            pos++;
        }
        if (log_action != null) {
            pstmt.setString(pos, log_action);
            pos++;
        }
        if (log_old_data != null) {
            pstmt.setString(pos, log_old_data);
            pos++;
        }
        if (log_new_data != null) {
            pstmt.setString(pos, log_new_data);
            pos++;
        }
        if (log_changed_time != null) {
            pstmt.setString(pos, log_changed_time);
            pos++;
        }
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        // 获得与数据库的连接
        Connection con = (Connection) req.getSession().getAttribute("con");
        if (con == null) {
            System.out.println("con to database is null");
            return;
        }


        String select;
        // 获得目标列数组
        String[] targets = req.getParameterValues("target_columns");
        req.getSession().setAttribute("target_columns", targets);
        // 获得目标表
        String table_select = req.getParameter("table_select");

        if (table_select.equals("00001")) {

            getAudit_LogParams(req);

            select = "select ";
            for (String target : targets) {
                select = select + target + ",";
            }
            select = select+ "log_id ";
            // 生成from语句
            String from = " FROM Audit_Log ";
            // 生成where字段
            String where = " WHERE 1=1 " + getAudit_LogWhere() + " ; ";

            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;

                setAudit_LogPstmtParams();

                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                req.getSession().setAttribute("status","failed");
                req.getSession().setAttribute("errormessage",e);
                resp.sendRedirect("/DB_2024/status_display.jsp");
            }

        }

        // 生成select语句
        if (targets != null) {
            int[] id_append = {0, 0, 0, 0};
            select = "select ";
            for (String target : targets) {
                select = select + target + ",";
                if (target.split("_")[0].equals("p") && id_append[0] == 0) {
                    select = select + "p_id,";
                    id_append[0] = 1;
                } else if (target.split("_")[0].equals("doc") && id_append[1] == 0) {
                    select = select + "doc_id,";
                    id_append[1] = 1;
                } else if (target.split("_")[0].equals("dept") && id_append[2] == 0) {
                    select = select + "dept_id,";
                    id_append[2] = 1;
                } else if (target.split("_")[0].equals("tr") && id_append[3] == 0) {
                    select = select + "tr_id,";
                    id_append[3] = 1;
                }
            }
            select = select.substring(0, select.length() - 1);
        } else {
            System.out.println("No values received for 'target_columns'.");
            select = "select * ";
            req.getSession().setAttribute("status", "failed");
            req.getSession().setAttribute("errormessage", "No values input");
            resp.sendRedirect("/DB_2024/status_display.jsp");
        }

        // 生成from语句
        String from = " FROM ";
        // 获得目标列所在的表
        table_select = req.getParameter("table_select");
        // 生成where字段
        String where = " WHERE 1=1 ";

        // 目标列在病人表中
        if (table_select.equals("10000")) {
            from = from + " Patient ";

            getPatientParams(req);

            where = where + getPatientWhere() + ";";

            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;

                setPatientPstmtParams();

                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 目标列在医生表中
        else if (table_select.equals("01000")) {
            from = from + " Doctor ";

            getDoctorParams(req);

            where = where + getDoctorWhere() + ";";

            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;

                setDoctorPstmtParams();

                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 目标列在科室表中
        else if (table_select.equals("00100")) {
            from = from + " Department ";

            getDepartmentParams(req);

            where = where + getDepartmentWhere() + ";";

            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;

                setDepartmentPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 目标列在诊断记录表中
        else if (table_select.equals("00010")) {
            from = from + " Treatment_Record ";

            getTreatment_RecordParams(req);

            where = where + getTreatment_RecordWhere() + ";";

            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;

                setTreatment_RecordPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // patient and dept
        else if (table_select.equals("10100")) {
            from = from + " Patient "
                    + " INNER JOIN Department"
                    + " ON Patient.p_dept_id = Department.dept_id ";

            getPatientParams(req);
            getDepartmentParams(req);

            where = where + getPatientWhere() + getDepartmentWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setPatientPstmtParams();
                setDepartmentPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // patient and record
        else if (table_select.equals("10010")) {
            from = from + " Patient "
                    + " INNER JOIN Treatment_Record"
                    + " ON Patient.p_id = Treatment_Record.tr_p_id ";

            getPatientParams(req);
            getTreatment_RecordParams(req);

            where = where + getPatientWhere() + getTreatment_RecordWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setPatientPstmtParams();
                setTreatment_RecordPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // doctor and dept
        else if (table_select.equals("01100")) {
            from = from + " Doctor "
                    + " INNER JOIN Department"
                    + " ON Doctor.doc_dept_id = Department.dept_id ";

            getDoctorParams(req);
            getDepartmentParams(req);

            where = where + getDoctorWhere() + getDepartmentWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setDoctorPstmtParams();
                setDepartmentPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // doctor and record
        else if (table_select.equals("01010")) {
            from = from + " Doctor "
                    + " INNER JOIN Treatment_Record"
                    + " ON Doctor.doc_id = Treatment_Record.tr_doc_id ";

            getDoctorParams(req);
            getTreatment_RecordParams(req);

            where = where + getDoctorWhere() + getTreatment_RecordWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setDoctorPstmtParams();
                setTreatment_RecordPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // patient and doctor
        else if (table_select.equals("11000") || table_select.equals("11100")) {
            from = from + " Patient "
                    + " INNER JOIN Department"
                    + " ON Patient.p_dept_id = Department.dept_id"
                    + " INNER JOIN Doctor"
                    + " ON Department.dept_id = Doctor.doc_dept_id ";

            getPatientParams(req);
            getDoctorParams(req);
            getDepartmentParams(req);

            where = where + getPatientWhere() + getDoctorWhere() + getDepartmentWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setPatientPstmtParams();
                setDoctorPstmtParams();
                setDepartmentPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (table_select.equals("11010")) {
            from = from + " Patient "
                    + " INNER JOIN Treatment_Record"
                    + " ON Patient.p_id = Treatment_Record.tr_p_id"
                    + " INNER JOIN Doctor"
                    + " ON Treatment_Record.tr_doc_id = Doctor.doc_id ";

            getPatientParams(req);
            getDoctorParams(req);
            getTreatment_RecordParams(req);

            where = where + getPatientWhere() + getDoctorWhere() + getTreatment_RecordWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setPatientPstmtParams();
                setDoctorPstmtParams();
                setTreatment_RecordPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (table_select.equals("10110")) {
            from = from + " Treatment_Record "
                    + " INNER JOIN Patient"
                    + " ON Treatment_Record.tr_p_id = Patient.p_id"
                    + " INNER JOIN Department"
                    + " ON Department.dept_id = Patient.p_dept_id ";

            getPatientParams(req);
            getDepartmentParams(req);
            getTreatment_RecordParams(req);

            where = where + getPatientWhere() + getDepartmentWhere() + getTreatment_RecordWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setPatientPstmtParams();
                setDepartmentPstmtParams();
                setTreatment_RecordPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (table_select.equals("11110")) {
            from = from + " Patient "
                    + " INNER JOIN Department"
                    + " ON Patient.p_dept_id = Department.dept_id"
                    + " INNER JOIN Doctor"
                    + " ON Department.dept_id = Doctor.doc_dept_id"
                    + " INNER JOIN Treatment_Record"
                    + " ON Treatment_Record.tr_p_id = Patient.p_id and Treatment_Record.tr_doc_id = Doctor.doc_id ";

            getPatientParams(req);
            getDoctorParams(req);
            getDepartmentParams(req);
            getTreatment_RecordParams(req);

            where = where + getPatientWhere() + getDoctorWhere() + getDepartmentWhere() + getTreatment_RecordWhere() + ";";


            String sql = select + from + where;
            System.out.println("sql:" + sql);
            try {
                pstmt = con.prepareStatement(sql);
                pos = 1;


                setPatientPstmtParams();
                setDoctorPstmtParams();
                setDepartmentPstmtParams();
                setTreatment_RecordPstmtParams();


                ResultSet rs = pstmt.executeQuery();
                req.getSession().setAttribute("rs", rs);
                resp.sendRedirect("/DB_2024/outcome.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
