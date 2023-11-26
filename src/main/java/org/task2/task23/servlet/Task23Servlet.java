package org.task2.task23.servlet;

import java.io.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Task23Servlet extends HttpServlet implements ServletUtil{

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");

        System.out.println(getClass().getSimpleName() + ".doGet() is invoked");

        try (PrintWriter out = response.getWriter()) {

            out.println(head("Test Task 2.3 Start page"));
            out.println("""
                            <body>
                            <h1>Hello, this is a Test Task 2.3</h1>
                            <br><br>
                        """);
            out.println(href("Validate Data", "/validateData"));
            out.println("<br><br>");
            out.println(href("Get Customers", "/getCustomers"));
            out.println("</body>");
            out.println("</html>");
        }
    }
}