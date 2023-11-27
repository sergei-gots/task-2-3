package org.task2.task23.servlet;

import java.io.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Task23Servlet extends AbstractServlet {

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {

        logMethodStart("doGet");
        response.setContentType("text/html");
        writeFileToResponse (response, "task-2-3-start.html");
    }
}