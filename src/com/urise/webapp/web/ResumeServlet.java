package com.urise.webapp.web;

import com.urise.webapp.Config;
import com.urise.webapp.model.*;
import com.urise.webapp.storage.SQLStorage;
import com.urise.webapp.util.DateUtil;
import com.urise.webapp.util.HtmlUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
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

        final boolean isCreate = (uuid == null || uuid.length() == 0);
        Resume r;
        if (isCreate) {
            r = new Resume(fullName);
        } else {
            r = storage.get(uuid);
            r.setFullName(fullName);
        }

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
            String[] values = request.getParameterValues(sectionName.name());
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
                        //ListOfStrings section = new ListOfStrings();
                        //Arrays.stream(value.split("\n")).forEach(section::addRecord);
                        r.addSection(sectionName, new ListOfStrings(value.split("\\n")));
                    }
                    break;
                    case Education:
                    case Experience:   List<Organization> orgs = new ArrayList<>();
                        String[] urls = request.getParameterValues(sectionName.name() + "url");
                        for (int i = 0; i < values.length; i++) {
                            String name = values[i];
                            if (!HtmlUtil.isEmpty(name)) {
                                List<Organization.DateAndText> positions = new ArrayList<>();
                                String pfx = sectionName.name() + i;
                                String[] startDates = request.getParameterValues(pfx + "startDate");
                                String[] endDates = request.getParameterValues(pfx + "endDate");
                                String[] titles = request.getParameterValues(pfx + "title");
                                String[] descriptions = request.getParameterValues(pfx + "description");
                                for (int j = 0; j < titles.length; j++) {
                                    if (!HtmlUtil.isEmpty(titles[j])) {
                                        positions.add(new Organization.DateAndText(titles[j], DateUtil.parse(startDates[j]), DateUtil.parse(endDates[j]), descriptions[j]));
                                    }
                                }
                                orgs.add(new Organization(new Link(name, urls[i]), positions));
                            }
                        }
                        r.addSection(sectionName, new Organizations(orgs));
                        break;
                }
            } else {
                r.getSections().remove(sectionName);
            }
        }
        if (isCreate) {
            storage.save(r);
        } else {
            storage.update(r);
        }
        response.sendRedirect("resume");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws javax.servlet.ServletException, IOException {
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
                r = storage.get(uuid);
                break;
            case "edit":
                r = storage.get(uuid);
                for (SectionName name : SectionName.values()) {
                    SectionBasic section = r.getSectionByName(name);
                    switch (name) {
                        case Personal:
                        case CurrentPosition:
                            if (section == null) {
                                section = PlainText.EMPTY;
                            }
                            break;
                        case Skills:
                        case Achievements:
                            if (section == null) {
                                section = ListOfStrings.EMPTY;
                            }
                            break;
                        case Experience:
                        case Education:
                            Organizations orgSection = (Organizations) section;
                            List<Organization> emptyFirstOrganizations = new ArrayList<>();
                            emptyFirstOrganizations.add(Organization.EMPTY);
                            if (section != null) {
                                for (Organization org : orgSection.getOrganizations()) {
                                    List<Organization.DateAndText> emptyFirstPositions = new ArrayList<>();
                                    emptyFirstPositions.add(Organization.DateAndText.EMPTY);
                                    emptyFirstPositions.addAll(org.getPeriods());
                                    emptyFirstOrganizations.add(new Organization("", emptyFirstPositions, org.getHomePage()));
                                }
                            }
                            section = new Organizations(emptyFirstOrganizations);
                            break;
                    }
                    r.addSection(name, section);
                }
            break;
            case "add":
                r = Resume.EMPTY;
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

