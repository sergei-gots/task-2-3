package org.task2.task23.service;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.task2.task23.dto.CustomerInfo;
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
    public List<CustomerInfo> get5000Customers(int offset) throws SQLException {
        logger.info("get5000Customers(offset={})", offset);
        long t0 = System.currentTimeMillis();

        PreparedStatement sqlStatement = null;
        ResultSet resultSet = null;
        try {
            List<CustomerInfo> customerList = new ArrayList<>(5000);

            connection = dbProperties.getConnection();

            sqlStatement
                    = connection.prepareStatement(""" 
                    SELECT * FROM customer c
                    LEFT JOIN table_1 t0 ON c.customer_id = t0.customer_id
                    LEFT JOIN table_1 t1 ON c.customer_id = t1.customer_id
                    LEFT JOIN table_2 t2 ON c.customer_id = t2.customer_id
                    LEFT JOIN table_3 t3 ON c.customer_id = t3.customer_id
                    LEFT JOIN table_4 t4 ON c.customer_id = t4.customer_id
                    LEFT JOIN table_5 t5 ON c.customer_id = t5.customer_id
                    LEFT JOIN table_6 t6 ON c.customer_id = t6.customer_id
                    LEFT JOIN table_7 t7 ON c.customer_id = t7.customer_id
                    LEFT JOIN table_8 t8 ON c.customer_id = t8.customer_id
                    LEFT JOIN table_9 t9 ON c.customer_id = t9.customer_id
                    LEFT JOIN table_10 t10 ON c.customer_id = t10.customer_id
                    LEFT JOIN table_11 t11 ON c.customer_id = t11.customer_id
                    LEFT JOIN table_12 t12 ON c.customer_id = t12.customer_id
                    LEFT JOIN table_13 t13 ON c.customer_id = t13.customer_id
                    LEFT JOIN table_14 t14 ON c.customer_id = t14.customer_id
                    LEFT JOIN table_15 t15 ON c.customer_id = t15.customer_id
                    LEFT JOIN table_16 t16 ON c.customer_id = t16.customer_id
                    LEFT JOIN table_17 t17 ON c.customer_id = t17.customer_id
                    LEFT JOIN table_18 t18 ON c.customer_id = t18.customer_id
                    LEFT JOIN table_19 t19 ON c.customer_id = t19.customer_id
                    LEFT JOIN table_20 t20 ON c.customer_id = t20.customer_id
                    LEFT JOIN table_21 t21 ON c.customer_id = t21.customer_id
                    LEFT JOIN table_22 t22 ON c.customer_id = t22.customer_id
                    LEFT JOIN table_23 t23 ON c.customer_id = t23.customer_id
                    LEFT JOIN table_24 t24 ON c.customer_id = t24.customer_id
                    LEFT JOIN table_25 t25 ON c.customer_id = t25.customer_id
                    LEFT JOIN table_26 t26 ON c.customer_id = t26.customer_id
                    LEFT JOIN table_27 t27 ON c.customer_id = t27.customer_id
                    LEFT JOIN table_28 t28 ON c.customer_id = t28.customer_id
                    LEFT JOIN table_29 t29 ON c.customer_id = t29.customer_id
                    ORDER BY c.customer_id
                    OFFSET ? ROWS
                    LIMIT 5000
                    
                """);
            sqlStatement.setInt(1, offset);

            logger.info("sqlStatement.executeQuery()");
            resultSet = sqlStatement.executeQuery();
            logger.info("Query executed: add customers to the list");

            for (int i=0; resultSet.next(); i++) {
                CustomerInfo customerInfo = new CustomerInfo(resultSet.getString(1));
                customerList.add(i, customerInfo);
                int[] cols = customerInfo.getCols();

                cols[0] = resultSet.getInt(2);
                for (int j = 3, k = 1; j <= GRAND_TOTAL_COL_COUNT; j++, k++) {
                    if (k % 5 == 0) {
                        j++;
                    }
                    cols[k] = resultSet.getInt(j);
                }
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
            logger.info("GetCustomers5000 took " + (System.currentTimeMillis() - t0) + " ms");
        }
    }
}
