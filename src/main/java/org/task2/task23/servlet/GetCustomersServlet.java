package org.task2.task23.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.task2.task23.dto.CustomerInfo;
import org.task2.task23.service.GetCustomersService;
import org.task2.task23.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;


public class GetCustomersServlet extends AbstractServlet {

    private final GetCustomersService service = ServiceFactory.getServiceInstance();

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws IOException {

        logMethodStart("doGet");

        writeFileToResponse(response, "get-customers-form.html");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        logMethodStart("doPost");
        long t0 = System.currentTimeMillis();
        
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
            List<CustomerInfo> customerList = service.get5000Customers(offset);

            logger.info("new Gson().toJson will be invoked next");
            String jsonResult = new Gson().toJson(customerList);
            String executionTimeReport = "/GetCustomers took  on the Server in total "
                    + (System.currentTimeMillis() - t0) + " ms.";
            out.println(executionTimeReport);
            out.println("<br><br>");
            out.write(jsonResult);
            logger.info(executionTimeReport);


            out.println("</body></html>");
            logger.info("doPost completed.");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error due performing SQL request");
        }
    }

    private int parseJsonAsInt(String jsonString, String key) {

        logMethodStart("parseJsonAsInt");
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