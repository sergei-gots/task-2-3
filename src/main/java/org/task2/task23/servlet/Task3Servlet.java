package org.task2.task23.servlet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.task2.task23.service.Initializer;
import org.task2.task23.service.Service;
import org.task2.task23.service.ServiceFactory;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Task3Servlet extends HttpServlet {

    private final Initializer initializer = ServiceFactory.getInitializerInstance();
    private final Service service = ServiceFactory.getServiceInstance();

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
             throws IOException {
        // Установите тип контента в "text/html", чтобы браузер знал, что это HTML-содержимое
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Получите объект PrintWriter из HttpServletResponse для записи HTML-кода
        try (PrintWriter out = response.getWriter()) {
            // Указываете путь к вашему HTML-файлу внутри проекта
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Устанавливаем кодировку для чтения данных из тела запроса
        request.setCharacterEncoding("UTF-8");

        // Получаем BufferedReader из тела запроса
        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(request.getInputStream(), "UTF-8"))) {
            // Читаем данные из BufferedReader
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
                    response.setContentType("text/plain");
                    response.getWriter().write("JSON received successfully and contains the offset=" + offsetValue);
                    return;
                }
            }
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "There is no offset value in JSON found or it is not a number");
        } catch (IOException e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error reading JSON or offset value from it from request");
        }
    }
    public void doPost1(HttpServletRequest request,
                      HttpServletResponse response)
             throws IOException {
        response.setContentType("text/html");

        // Получите объект PrintWriter из HttpServletResponse для записи HTML-кода
        try (PrintWriter out = response.getWriter()) {
            // Здесь вы можете написать HTML-код, который вы хотите вернуть клиенту
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>This is a doPost:)</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Hello, this is my DoPost method!</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}