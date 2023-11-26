package org.task2.task23.servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.task2.task23.service.Service;
import org.task2.task23.service.ServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class GetCustomersServlet extends HttpServlet
implements ServletUtil{

    private final Service service = ServiceFactory.getServiceInstance();

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {

        System.out.println(getClass().getSimpleName() + ".doGet() is invoked");

        writeFileToResponse(response, "/WEB-INF/get-customers-form.html");
    }
    private void writeFileToResponse (HttpServletResponse response, String relativeFilePath)
    throws IOException {

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try (PrintWriter out = response.getWriter()) {
            String filePath = getServletContext().getRealPath(relativeFilePath);

            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    out.println(line);
                }
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        System.out.println(getClass().getSimpleName() + ".doPost() is invoked");

        request.setCharacterEncoding("UTF-8");

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String jsonString = sb.toString();
            System.out.println("Received JSON: " + jsonString);

            JsonElement jsonElement = JsonParser.parseString(jsonString);
            if (jsonElement.isJsonObject()) {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                JsonElement offsetElement = jsonObject.get("offset");
                if (offsetElement != null && offsetElement.isJsonPrimitive()) {
                    int offsetValue = offsetElement.getAsInt();
                    PrintWriter out =  response.getWriter();
                    out.println(head("GetCustomer"));
                    out.print("<body>JSON received successfully and contains the offset=");
                    out.println(offsetValue);
                    out.println("<br><br>");
                    out.println("back to /getCustomers page:");
                    out.println(href("Get Customers", "/getCustomers"));
                    out.println("</body></html>");
                    return;
                }
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no offset value in JSON found or it is not a number");
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading JSON or offset value from it from request");
        }
    }
}