package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.storage.SQLStorage;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResumeServlet extends HttpServlet {
    static final SQLStorage storage = new SQLStorage(Config.get().getProps().getProperty("db.url"), Config.get().getProps().getProperty("db.user"),Config.get().getProps().getProperty("db.password"));

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
//        response.setHeader("Content-Type", "text/html; charset=UTF-8");
        response.setContentType("text/html; charset=UTF-8");
        String uuid = request.getParameter("uuid");
        if (uuid==null) {
            response.getWriter().write(storage.getAllSorted().toString());
        }
        else
        response.getWriter().write(storage.get(uuid).toString());
    }
}

