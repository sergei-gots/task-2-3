package org.task2.task23.servlet;

import org.task2.task23.service.Initializer;
import org.task2.task23.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

public class ValidateDataServlet extends AbstractServlet {

    private final Initializer initializer = ServiceFactory.getInitializerInstance();

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response)
            throws IOException {
        writeFileToResponse (response, "validate-data-form.html");
    }


    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
             throws IOException {

        logMethodStart("doGet");
        response.setContentType("text/html; charset=UTF-8");

        try {
            initializer.validateData();
        }
        catch (SQLException e) {
            e.printStackTrace();
            //response.getWriter().write("Could not validate data. Cause:" + e.getCause());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error validating data. Cause: " + e.getCause());
            return;
        }

        try (PrintWriter out = response.getWriter()) {
            out.println(head("Data validation"));
            out.println("<body>Data is ready to use with /getCustomers-request:");
            out.println("<br><br>");
            out.println(href("Get Customers", "/getCustomers"));
            out.println("</body></html>");
        }

//        response.sendRedirect(request.getContextPath() + "/task-2-3/getCustomers");
    }


}