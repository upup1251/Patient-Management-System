package com.example.db_2024;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet("/add")
public class add extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String table_name = (String) req.getParameter("table_select");
        Connection con = (Connection) req.getSession().getAttribute("con");
        String sql = null;
        if (table_name.equals("Patient")) {
            sql = "INSERT INTO Patient (p_id, p_record, p_name, p_sex, p_age, p_phone, p_date_in, p_date_out, p_dept_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        } else if (table_name.equals("Doctor")) {
            sql = "INSERT INTO Doctor (doc_id, doc_name, doc_phone, doc_specialty, doc_dept_id) VALUES (?, ?, ?, ?, ?)";
        } else if (table_name.equals("Department")) {
            sql = "INSERT INTO Department (dept_id, dept_name, dept_head, dept_phone) VALUES (?, ?, ?, ?)";
        } else if (table_name.equals("Treatment_Record")) {
            sql = "INSERT INTO Treatment_Record (tr_id, tr_p_id, tr_doc_id, tr_date, tr_diagnosis, tr_plan) VALUES (?, ?, ?, ?, ?, ?)";
        }
        // 如果表名不匹配，抛出异常
        if (sql == null) {
            throw new IllegalArgumentException("Invalid table name: " + table_name);
        }
        PreparedStatement pstmt = null;
        if (con != null) {
            try {
                pstmt = con.prepareStatement(sql);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            System.out.println("add.java : con is null!");
            return;
        }
        // 根据表名绑定参数
        try {
            if (table_name.equals("Patient")) {
                pstmt.setString(1, req.getParameter("p_id").trim());
                pstmt.setString(2, req.getParameter("p_record").trim());
                pstmt.setString(3, req.getParameter("p_name").trim());
                pstmt.setString(4, req.getParameter("p_sex").trim());
                pstmt.setString(5, req.getParameter("p_age").trim());
                pstmt.setString(6, req.getParameter("p_phone").trim());
                pstmt.setString(7, req.getParameter("p_date_in").trim());
                pstmt.setString(8, req.getParameter("p_date_out").trim());
                pstmt.setString(9, req.getParameter("p_dept_id").trim());
            } else if (table_name.equals("Doctor")) {
                pstmt.setString(1, req.getParameter("doc_id").trim());
                pstmt.setString(2, req.getParameter("doc_name").trim());
                pstmt.setString(3, req.getParameter("doc_phone").trim());
                pstmt.setString(4, req.getParameter("doc_specialty").trim());
                pstmt.setString(5, req.getParameter("doc_dept_id").trim());
            } else if (table_name.equals("Department")) {
                pstmt.setString(1, req.getParameter("dept_id").trim());
                pstmt.setString(2, req.getParameter("dept_name").trim());
                pstmt.setString(3, req.getParameter("dept_head").trim());
                pstmt.setString(4, req.getParameter("dept_phone").trim());
            } else if (table_name.equals("Treatment_Record")) {
                pstmt.setString(1, req.getParameter("tr_id").trim());
                pstmt.setString(2, req.getParameter("tr_p_id").trim());
                pstmt.setString(3, req.getParameter("tr_doc_id").trim());
                pstmt.setString(4, req.getParameter("tr_date").trim());
                pstmt.setString(5, req.getParameter("tr_diagnosis").trim());
                pstmt.setString(6, req.getParameter("tr_plan").trim());
            }
            pstmt.executeUpdate();
            req.getSession().setAttribute("status","ok");
            resp.sendRedirect("/DB_2024/status_display.jsp");
        } catch (Exception e) {
            req.getSession().setAttribute("status","failed");
            req.getSession().setAttribute("errormessage",e);
            resp.sendRedirect("/DB_2024/status_display.jsp");
            e.printStackTrace();
        }

    }
}