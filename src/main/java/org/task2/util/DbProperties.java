package org.task2.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DbProperties {

    private boolean loaded;
    private final String driver;
    private final String url;
    private final String username;
    private final String password;

    private static DbProperties dbProperties;

    static {
        try {
            dbProperties = new DbProperties("db.properties");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DbProperties getDbProperties () {
           return dbProperties;
     }

    private DbProperties(String filename) throws Exception {
        Properties properties = new Properties();

        try (InputStream is = getClass().getClassLoader().getResourceAsStream(filename)) {
            properties.load(is);
            driver = properties.getProperty("driver", "");
            url = properties.getProperty("url", "");
            username = properties.getProperty("username", "");
            password = properties.getProperty("password", "");

        } catch (IOException e) {
            System.out.println("Could not load " + filename + ". cause =" + e.getCause());
            throw e;
        }
        try {
            loadDriver();
        }
        catch (ClassNotFoundException e) {
            System.out.println("Could not load driver " + driver + ". cause: " + e.getCause());
            throw e;
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
