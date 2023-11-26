package org.task2.task23.service;

import org.apache.commons.dbutils.DbUtils;
import org.task2.util.DbProperties;
import org.task2.util.SqlTemplates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class InitializerImpl implements Initializer, SqlTemplates {

    public static final int TABLE_I_COUNT_BY_DEFAULT = 30;
    private int tableICount;



    static private final DbProperties dbProperties
            = DbProperties.loadProperties("db.properties");

    @Override
    public void validateData() {

        Connection connection = null;

        try {
            connection = dbProperties.getConnection();
            validateTableCustomer(connection);
            validateTablesI(connection);
            validateTableMany(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            DbUtils.closeQuietly(connection);
        }
    }

    private boolean doesTableExist(Connection connection, String tableName) throws SQLException {
        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;
        try {
            sqlStatement = connection.prepareStatement(SQL_STATEMENT_IS_TABLE_EXIST);
            sqlStatement.setString(1, tableName);
            resultSet = sqlStatement.executeQuery();
            resultSet.next();
            return resultSet.getBoolean(1);

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            DbUtils.closeQuietly(resultSet);
            throw e;
        }
    }

    private void validateTableCustomer(Connection connection) throws  SQLException{
        if (!doesTableExist(connection, CUSTOMER)) {
            createTableCustomer(connection);
        }
    }

    private void validateTableMany(Connection connection) throws  SQLException{
        if (!doesTableExist(connection, TABLE_MANY)) {
            createTableMany(connection);
        }
    }

    private void validateTablesI(Connection connection) throws SQLException{
        tableICount = 0;
        while (doesTableExist(connection,TABLE_I + tableICount)) {
            tableICount++;
        }
        while(tableICount < TABLE_I_COUNT_BY_DEFAULT) {
            createTableI(connection, tableICount++);
        }
    }

    private void createTableCustomer(Connection connection) throws SQLException{

        PreparedStatement sqlStatement = null;
        try {
            sqlStatement = connection.prepareStatement(
                            """
                CREATE TABLE customer (
                    customer_id VARCHAR(200) NOT NULL PRIMARY KEY,
                    col_1 INTEGER,
                    col_2 INTEGER,
                    col_3 INTEGER,
                    col_4 INTEGER,
                    col_5 INTEGER
                )""");
            sqlStatement.execute();

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }
    }

    private void createTableI(Connection connection, int i) throws SQLException{

        PreparedStatement sqlStatement = null;
        try {
            sqlStatement = connection.prepareStatement(
                    "CREATE TABLE table_" +  i +
                            """
                                    (   customer_id VARCHAR(200) NOT NULL PRIMARY KEY REFERENCES customer (customer_id),
                                        col_1 INTEGER,
                                        col_2 INTEGER,
                                        col_3 INTEGER,
                                        col_4 INTEGER,
                                        col_5 INTEGER
                                    )
                                    """);
            sqlStatement.execute();

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }
    }

    private void createTableMany(Connection connection) throws SQLException{

        PreparedStatement sqlStatement = null;
        try {
            sqlStatement = connection.prepareStatement("""
                CREATE TABLE table_many (
                    customer_id VARCHAR(200) NOT NULL REFERENCES customer (customer_id),
                    groupId INT NOT NULL
                )""");
            sqlStatement.execute();

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }
    }
}
