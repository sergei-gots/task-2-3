package org.task2.task23.servlet;

import org.task2.task23.service.Initializer;
import org.task2.task23.service.ServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;

public class ValidateDataServlet extends HttpServlet {

    private final Initializer initializer = ServiceFactory.getInitializerInstance();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
             throws IOException {


        try {
            initializer.validateData();
        }
        catch (SQLException e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("Could not validate data. Cause:" + e.getCause());
            return;
        }

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (!response.isCommitted()) {
            response.sendRedirect("/getCustomers");
        }

        try (PrintWriter out = response.getWriter()) {
            String filePath = getServletContext().getRealPath("/WEB-INF/get-customers-form.html");

            // Читаем содержимое HTML-файла
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                }
            }
        }

    }

}