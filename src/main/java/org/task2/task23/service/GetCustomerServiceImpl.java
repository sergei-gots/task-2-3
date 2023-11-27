package org.task2.task23.service;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task2.task23.entity.Customer;
import org.task2.util.DbProperties;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetCustomerServiceImpl implements GetCustomersService {
    private static final Logger logger = LoggerFactory.getLogger(InitializerImpl.class);
    private static final DbProperties dbProperties = DbProperties.getDbProperties();

    private Connection connection = null;

    @Override
    public List<Customer> get5000Customers(int offset) throws SQLException {
        logger.info("get5000Customers(offset={})", offset);

        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;
        try {
            List<Customer> customerList = new ArrayList<>(5000);

            connection = dbProperties.getConnection();

            sqlStatement
                    = connection.prepareStatement(""" 
                    SELECT * FROM customer
                    ORDER BY customer_id
                    OFFSET ? ROWS
                    FETCH NEXT 5000 ROWS ONLY
                """);
            sqlStatement.setInt(1, offset);

            logger.info("sqlStatement.executeQuery()");
            resultSet = sqlStatement.executeQuery();
            logger.info("Query executed: add customers to the list");
            for (int i=0; resultSet.next(); i++) {
                customerList.add(i,
                        new Customer(
                                resultSet.getString(1),
                                resultSet.getInt(2),
                                resultSet.getInt(3),
                                resultSet.getInt(4),
                                resultSet.getInt(5),
                                resultSet.getInt(6)
                                )
                );
            }
            logger.info("List completed");
            return customerList;

        } catch (SQLException e) {
            logger.error("get5000Customers() :", e);
            throw e;
        } finally {
            DbUtils.closeQuietly(resultSet);
            DbUtils.closeQuietly(sqlStatement);
            DbUtils.closeQuietly(connection);
        }
    }
}
