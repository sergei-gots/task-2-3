package org.task2.task23.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public abstract class AbstractServlet extends HttpServlet {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    protected void logMethodStart(String methodName){
        logger.info(methodName + "()-method is invoked.");
    }
    protected String href (String text, String endPoint) {
        return "<a href=http://localhost:8080/task-2-3" + endPoint + ">" + text + "</a>";
    }

    protected String head (String title) {
        return "<!DOCTYPE html><html><head><title>" + title + "</title></head>";
    }

    protected void writeFileToResponse (HttpServletResponse response, String filename)
            throws IOException {

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String filePath = getServletContext().getRealPath("/WEB-INF/" + filename);

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                }
            }
        }
    }


}
