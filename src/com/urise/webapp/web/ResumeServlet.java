package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.SQLStorage;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import static com.urise.webapp.model.SectionName.*;

public class ResumeServlet extends HttpServlet {
    static SQLStorage storage;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new SQLStorage(Config.get().getProps().getProperty("db.url"), Config.get().getProps().getProperty("db.user"), Config.get().getProps().getProperty("db.password"));
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String uuid = request.getParameter("uuid");
        String fullName = request.getParameter("fullName");
        Resume r = storage.get(uuid);
        r.setFullName(fullName);
        for (ContactName contactName : ContactName.values()) {
            String value = request.getParameter(contactName.name());
            if (value != null && value.trim().length() != 0) {
                r.addContact(contactName, value);
            } else {
                r.getContacts().remove(contactName);
            }
        }
        for (SectionName sectionName : SectionName.values()) {
            String value = request.getParameter(sectionName.name());
            if (value != null && value.trim().length() != 0) {

                switch (sectionName) {
                    case Personal:
                    case CurrentPosition: {
                        PlainText section = new PlainText(value);
                        r.addSection(sectionName, section);
                    }
                    break;
                    case Skills:
                    case Achievements: {
                        ListOfStrings section = new ListOfStrings();
                        Arrays.stream(value.split(", ")).forEach(section::addRecord);
                        r.addSection(sectionName, section);
                    }
                    break;
                }
            } else {
                r.getSections().remove(sectionName);
            }
        }
        storage.update(r);
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
//        request.setCharacterEncoding("UTF-8");
//        response.setCharacterEncoding("UTF-8");
//        response.setContentType("text/html; charset=UTF-8");
//        String uuid = request.getParameter("uuid");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            response.getWriter().write(e.getCause().toString());
        }

        String uuid = request.getParameter("uuid");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("resumes", storage.getAllSorted());
            request.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(request, response);
            return;
        }
        Resume r;
        switch (action) {
            case "delete":
                storage.delete(uuid);
                response.sendRedirect("resume");
                return;
            case "view":
            case "edit":
                r = storage.get(uuid);
                break;
            case "add":
                r = new Resume();
                break;
            default:
                throw new IllegalArgumentException("Action " + action + " is illegal");
        }
        request.setAttribute("resume", r);
        request.getRequestDispatcher(
                ("view".equals(action) ? "/WEB-INF/jsp/view.jsp" : "/WEB-INF/jsp/edit.jsp")
        ).forward(request, response);
    }
}

