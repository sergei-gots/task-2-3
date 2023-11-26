package org.task2.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbProperties {

    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    public static DbProperties loadProperties(String resourceFilename) {
        return new DbProperties(resourceFilename);

    }

    private DbProperties(String filename) {
        Properties properties = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            properties.load(is);
            driver = properties.getProperty("driver", "");
            url = properties.getProperty("url", "");
            username = properties.getProperty("username", "");
            password = properties.getProperty("password", "");

        } catch (IOException e) {
            throw new RuntimeException("Could not load " + filename, e);
        }
    }

    /**
     * load driver communication of db server.
     *
     * @throws ClassNotFoundException
     */
    public void loadDriver() throws ClassNotFoundException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Could not load class " + driver + ", cause: " + e.getCause());
            throw e;
        }
    }

    /**
     * Opens the connection
     *
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {

        return  DriverManager.getConnection(url, username, password);
    }
}
