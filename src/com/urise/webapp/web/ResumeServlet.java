package com.urise.webapp.web;

import com.urise.webapp.config.Config;
import com.urise.webapp.model.Resume;
import com.urise.webapp.storage.SQLStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class ResumeServlet extends HttpServlet {
    static final SQLStorage storage = new SQLStorage(Config.get().getProps().getProperty("db.url"), Config.get().getProps().getProperty("db.user"), Config.get().getProps().getProperty("db.password"));

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            response.getWriter().write(e.getCause().toString());
        }
        Writer writer = response.getWriter();
        if (uuid == null) {
            StringBuilder sb = new StringBuilder();
            sb.append("<table border = \"1\"> ");
            sb.append("<tr> <td> Full Name </td> <td> Contacts </td> <td> Sections </td>");
            List<Resume> list = storage.getAllSorted();
            for (Resume r : list) {
                sb.append("<tr> <td>" + r.getFullName() + "</td>  <td> " + r.getContacts().toString() + "</td>  <td> " +
                        r.getSections().toString() + "</td>");
            }
            sb.append("</tr> </table>");
            writer.write(sb.toString());
        } else {
            StringBuilder sb = new StringBuilder();
            Resume r = storage.get(uuid);
            sb.append("<table border = \"1\"> ");
            sb.append("<tr> <td> Full Name </td> <td> Contacts </td> <td> Sections </td>");
            sb.append("<tr> <td>" + r.getFullName() + "</td>  <td> " + r.getContacts().toString() + "</td>  <td> " +
                    r.getSections().toString() + "</td>");
            sb.append("</tr> </table>");
            writer.write(sb.toString());

        }
    }
}

