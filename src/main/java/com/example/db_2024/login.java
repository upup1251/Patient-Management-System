package com.example.db_2024;

//import jdk.javadoc.internal.doclets.toolkit.taglets.UserTaglet;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet("/login")
public class login extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("username:" + request.getParameter("username"));
        System.out.println("password:" + request.getParameter("password"));
        response.setContentType("text/html");
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        // 初始化数据
        HttpSession session = request.getSession();
        // 每个表中的属性
        String[] Patient = {"p_id", "p_record", "p_name", "p_sex", "p_age", "p_phone", "p_date_in", "p_date_out", "dept_id"};
        String[] Department = {"dept_id", "dept_name", "dept_head", "dept_phone"};
        String[] Doctor = {"doc_id", "doc_name", "doc_phone", "doc_specialty", "dept_id"};
        String[]  Treatment_Record = {"tr_id", "p_id", "doc_id", "tr_date", "tr_diagnosis", "tr_plan"};
        session.setAttribute("Patient",Patient);
        session.setAttribute("Department",Department);
        session.setAttribute("Doctor",Doctor);
        session.setAttribute("Treatment_Record",Treatment_Record);

        response.setContentType("text/html");
        // Hello
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1> connecting ... </h1>");
        out.println("</body></html>");

        final String username = request.getParameter("username");
        final String password = request.getParameter("password");
        final String DataBase_Name = request.getParameter("DataBase_Name");
        final String ip_port = request.getParameter("ip_port");
        System.out.println("username:" + username);
        System.out.println("password:" + password);
        System.out.println("DataBase-Name:" + DataBase_Name);
        System.out.println("ip-port:" + ip_port);
        // 判断是否开启全部权限，否则只有查看能力
        if (username.equals("root")){
            request.getSession().setAttribute("allPermission",1);
        }
        else {
            request.getSession().setAttribute("allPermission",0);
        }

        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL = "jdbc:mysql://" + ip_port + "/" + DataBase_Name + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
//        static final String DB_URL = "jdbc:mysql://localhost:3306/RUNOOB";
        try {
            System.out.println("连接数据库...");
            Class.forName(JDBC_DRIVER);
            Connection con = DriverManager.getConnection(DB_URL, username, password);
            // 将连接保存在session对象里以便于共享，浏览器关闭后失效
            request.getSession().setAttribute("con", con);
            request.getSession().setAttribute("database_name", DataBase_Name);
            request.getSession().setAttribute("ip_port", ip_port);
            request.getSession().setAttribute("username", username);

            System.out.println("数据库连接成功");
        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/html");
            out.println("<html><body>");
            out.println("<h1> Error happened </h1>");
            out.println(e.getMessage());
            out.println("</body></html>");
            return;
        }
        response.sendRedirect("/DB_2024/home.jsp");
    }

    public void destroy() {
    }
}