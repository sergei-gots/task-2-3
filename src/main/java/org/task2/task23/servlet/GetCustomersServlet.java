package org.task2.task23.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task2.task23.entity.Customer;
import org.task2.task23.service.GetCustomersService;
import org.task2.task23.service.ServiceFactory;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;


public class GetCustomersServlet extends HttpServlet
implements ServletUtil{

    private final GetCustomersService service = ServiceFactory.getServiceInstance();

    private static final Logger logger = LoggerFactory.getLogger(GetCustomersServlet.class);
    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {

        logger.info("doGet(...)");

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
        
        logger.info("doPost(...)");
        
        int offset = 0;

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(request.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }

            String jsonString = sb.toString();
            logger.info("Received JSON={}", jsonString);

            try {
                offset = parseJsonAsInt(jsonString, "offset");
                logger.info("offset={}", offset);
            }
            catch(IllegalArgumentException e) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST,
                            "There is no offset value in JSON found or it is not a number");
                }
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error reading JSON or offset value from it from request");
        }

        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out =  response.getWriter();

            out.println(head("GetCustomer"));
            out.print("<body>JSON received successfully and contains the offset=");
            out.println(offset);
            out.println("<br><br>");
            out.println("back to /getCustomers page:");
            out.println(href("Get Customers", "/getCustomers"));
            out.println("<br><br> The result:<br><br>");

            logger.info("service.get5000Customers will be invoked next.");
            List<Customer> customerList = service.get5000Customers(offset);
            logger.info("new Gson().toJson wiill be invoked next");
            out.write(new Gson().toJson(customerList));
            out.println("</body></html>");
            logger.info("doPost completed.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error due performing SQL request");
        }
    }

    private int parseJsonAsInt(String jsonString, String key) {

        JsonElement jsonElement = JsonParser.parseString(jsonString);

        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("jsonString=" + jsonString + " is not a Json Object");
        }

        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonElement targetElement = jsonObject.get(key);
        if (targetElement == null || !targetElement.isJsonPrimitive()) {
            throw new IllegalArgumentException("There is no key=" + key + " is jsonString=" + jsonString);
        }

        return targetElement.getAsInt();
    }
}