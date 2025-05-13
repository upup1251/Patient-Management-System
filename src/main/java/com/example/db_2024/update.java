package com.example.db_2024;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet("/update")
public class update extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Connection con = (Connection) req.getSession().getAttribute("con");

        String[] updates = req.getParameterValues("update");
        if (updates != null){
            for (String update : updates) {
                String sql = "";
                if (update.split("&")[0].split("_")[0].equals("p")) {
                    sql = " UPDATE Patient"
                            + " set " + update.split("&")[0] + " = '" + update.split("&")[1] + "'"
                            + " where p_id = '"  + update.split("&")[2] + "' ";
                }

                if (update.split("&")[0].split("_")[0].equals("doc")) {
                    sql = " UPDATE Doctor"
                            + " set " + update.split("&")[0] + " = '" + update.split("&")[1] + "'"
                            + " where doc_id = '"  + update.split("&")[2] + "'";
                }
                if (update.split("&")[0].split("_")[0].equals("dept")) {
                    sql = " UPDATE Department"
                            + " set " + update.split("&")[0] + " = '" + update.split("&")[1] + "'"
                            + " where dept_id = '"  + update.split("&")[2] + "'";
                }
                if (update.split("&")[0].split("_")[0].equals("tr")) {
                    sql = " UPDATE Treatment_Record"
                            + " set " + update.split("&")[0] + " = '" + update.split("&")[1] + "'"
                            + " where tr_id = '"  + update.split("&")[2] + "'";
                }
                if (update.split("&")[0].split("_")[0].equals("log")) {
                    sql = " UPDATE Audit_Log"
                            + " set " + update.split("&")[0] + " = '" + update.split("&")[1] + "'"
                            + " where log_id = '"  + update.split("&")[2] + "'";
                }
                System.out.println("Insert:" + sql);
                Statement stmt;
                try {
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                    req.getSession().setAttribute("status","ok");
                } catch (SQLException e) {
                    req.getSession().setAttribute("errormessage",e);
                    req.getSession().setAttribute("status","failed");
                    throw new RuntimeException(e);
                }
            }

        }

        String[] deletes = req.getParameterValues("delete");
        if (deletes != null){
            for (String delete : deletes){
                System.out.println(delete);

                String sql = "";
                if (delete.split("&")[0].split("_")[0].equals("p")) {
                    sql = " DELETE FROM Patient"
                            + " where p_id = '"  + delete.split("&")[1] + "' ";
                }
                if (delete.split("&")[0].split("_")[0].equals("doc")) {
                    sql = " DELETE FROM Doctor"
                            + " where doc_id = '"  + delete.split("&")[1] + "'";
                }
                if (delete.split("&")[0].split("_")[0].equals("dept")) {
                    sql = " DELETE FROM Department"
                            + " where dept_id = '"  + delete.split("&")[1] + "'";
                }
                if (delete.split("&")[0].split("_")[0].equals("tr")) {
                    sql = " DELETE FROM Treatment_Record"
                            + " where tr_id = '"  + delete.split("&")[1] + "'";
                }
                if (delete.split("&")[0].split("_")[0].equals("log")) {
                    sql = " DELETE FROM Audit_Log"
                            + " where log_id = '"  + delete.split("&")[1] + "'";
                }
                System.out.println("delete:" + sql);
                Statement stmt;
                try {
                    stmt = con.createStatement();
                    stmt.executeUpdate(sql);
                    req.getSession().setAttribute("status","ok");
                } catch (SQLException e) {
                    req.getSession().setAttribute("errormessage",e);
                    req.getSession().setAttribute("status","failed");
                    throw new RuntimeException(e);
                }
            }
        }
        resp.sendRedirect("/DB_2024/status_display.jsp");
    }
}
