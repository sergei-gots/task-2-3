package org.task2.task23.service;

import com.github.javafaker.Faker;
import org.apache.commons.dbutils.DbUtils;
import org.task2.util.DbProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class InitializerImpl implements Initializer {

    public final static int COL_I_AMOUNT = 5;
    public final static int TOTAL_COL_AMOUNT = COL_I_AMOUNT + 1;
    public final static int TABLE_I_COUNT_BY_DEFAULT = 30;
    public final static int CUSTOMER_COUNT_BY_DEFAULT = 10_000;

    public final static String[] INSERT_INTO_TABLE_I_STATEMENTS = new String[TABLE_I_COUNT_BY_DEFAULT];

    static {
        Arrays.setAll(INSERT_INTO_TABLE_I_STATEMENTS,
                i -> "INSERT INTO Table_" + i  + """ 
                        (customer_id, col_1, col_2, col_3, col_4, col_5)
                        VALUES (?, ?, ?, ?, ?, ?)
                        """);
    }

    private int tableICount;
    private int customerCount;

    private Connection connection = null;
    private final Random random = new Random();
    private final Faker faker = new Faker();
    private static final Logger logger = LoggerFactory.getLogger(InitializerImpl.class);
    private static final DbProperties dbProperties = DbProperties.getDbProperties();

    @Override
    public void validateData() throws SQLException {

        logger.info("validateData()");

        try {
            connection = dbProperties.getConnection();
            if (!doesTableExist("customer")) {
                createTableCustomer();
            }
            validateTablesI();
            if (!doesTableExist("table_many")) {
                createTableMany();
            }

            validateTableCustomerData();

        } finally {
            DbUtils.closeQuietly(connection);
        }
    }

    private boolean doesTableExist(String tableName) throws SQLException {

        logger.info("doesTableExist(tableName={})", tableName);

        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;
        try {
            sqlStatement = connection.prepareStatement(
                    "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = ?)"
            );
            sqlStatement.setString(1, tableName);
            resultSet = sqlStatement.executeQuery();
            resultSet.next();
            boolean result = resultSet.getBoolean(1);
            logger.info("doesTableExist return {})", result);
            return result;

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            DbUtils.closeQuietly(resultSet);
            throw e;
        }
    }

    private void validateTablesI() throws SQLException {

        logger.info("validateTableI()");

        tableICount = 0;
        while (doesTableExist("table_" + tableICount)) {
            tableICount++;
        }
        logger.info("validateTableI having tableICount = {}", tableICount );

        while (tableICount < TABLE_I_COUNT_BY_DEFAULT) {
            createTableI(tableICount++);
        }
    }

    private void createTableCustomer() throws SQLException {

        logger.info("createTableCustomer()");

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

    private void createTableI(int i) throws SQLException {

        logger.info("createTableI(i={})", i);
        PreparedStatement sqlStatement = null;
        try {
            sqlStatement = connection.prepareStatement(
                    "CREATE TABLE table_" + i +
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

    private void createTableMany() throws SQLException {

        logger.info("createTableMany()");

        PreparedStatement sqlStatement = null;
        try {
            sqlStatement = connection.prepareStatement("""
                    CREATE TABLE table_many (
                        customer_id VARCHAR(200) NOT NULL REFERENCES customer (customer_id),
                        groupId INT NOT NULL
                    )""");
            sqlStatement.execute();

            sqlStatement = connection.prepareStatement("""
                        ALTER TABLE table_many
                                            ADD CONSTRAINT unique_customer_group
                                            UNIQUE (customer_id, groupId)
                    """);
            sqlStatement.execute();
        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }
    }

    private void validateTableCustomerData() throws SQLException {
        logger.info("validateTableCustomerData()");

        alterIndexIdxCustomerIdOnCustomer(false);
        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;
        try {
            sqlStatement = connection.prepareStatement(
                    "SELECT COUNT(*) as row_count FROM Customer"
            );
            resultSet = sqlStatement.executeQuery();
            resultSet.next();
            customerCount = resultSet.getInt(1);

            while (customerCount++ < CUSTOMER_COUNT_BY_DEFAULT) {
                generateInsertsIntoTables_I(generateCustomer());
                if (customerCount%1000 == 0) {
                    logger.info(
                            "validateTableCustomerData() : current customerCount={}",
                            customerCount);
                }

            }
            alterIndexIdxCustomerIdOnCustomer(true);

        } catch (SQLException e) {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }

    }

    private void generateInsertsIntoTables_I(String customerId) throws SQLException {
        long x = random.nextLong();
        long mask = 1;
        for(int i = 0; i < tableICount; i++, mask = mask << 1) {
            if ((mask & x) != 0) {
                generateInsertIntoTable_I(i, customerId);
            }
        }
    }

    private void generateInsertIntoTable_I (int tableIndex, String customerId) throws SQLException {
        PreparedStatement sqlStatement = null;

        try {
            sqlStatement = connection.prepareStatement(INSERT_INTO_TABLE_I_STATEMENTS[tableIndex]);

            sqlStatement.setString(1, customerId);
            for (int i = 2; i <= TOTAL_COL_AMOUNT; i++) {
                sqlStatement.setInt(i, random.nextInt());
            }
            sqlStatement.execute();

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }


    }

    /**
     * @return value of the generated customer_id.
     */
    private String generateCustomer() throws SQLException {
        PreparedStatement sqlStatement = null;

        try {
            sqlStatement = connection.prepareStatement("""
                        INSERT INTO Customer (customer_id, col_1, col_2, col_3, col_4, col_5)
                                    VALUES (?, ?, ?, ?, ?, ?)
                    """);
            String customerId = generateCustomerId();
            sqlStatement.setString(1, customerId);
            for (int i = 2; i <= TOTAL_COL_AMOUNT; i++) {
                sqlStatement.setInt(i, random.nextInt());
            }
            sqlStatement.execute();
            return customerId;

        } catch (SQLException e) {
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }

    }

    private String generateCustomerId() throws SQLException {

        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;
        String name = null;
        try {
            sqlStatement = connection.prepareStatement(
                    "SELECT COUNT(*) as row_count FROM Customer WHERE customer_id=?"
            );
            do {
                name = faker.harryPotter().character() + random.nextInt();
                sqlStatement.setString(1, name);
                resultSet = sqlStatement.executeQuery();
                resultSet.next();
            }
            while (resultSet.getInt(1) > 0);
            return name;

        } catch (SQLException e) {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(sqlStatement);
            throw e;
        }

    }

    private void alterIndexIdxCustomerIdOnCustomer(boolean enable)  {
        logger.info("alterIndexIdxCustomerIdOnCustomer(enable={})", enable );

        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;

        try {
            sqlStatement = connection.prepareStatement(
                            "SELECT indexname FROM pg_indexes WHERE indexname='idx_customer_id'"
            );
            resultSet = sqlStatement.executeQuery();
            if(!resultSet.next()) {
                return;
            }

            sqlStatement = connection.prepareStatement(
                    enable ?
                            "CREATE INDEX idx_customer_id ON customer (customer_id)" :
                            "DROP INDEX idx_customer_id"
            );
            sqlStatement.execute();

        } catch (SQLException e) {
            logger.info("alterIndexIdxCustomerIdOnCustomer. SQLException caught", e );
            DbUtils.closeQuietly(sqlStatement);
        }
    }

}
