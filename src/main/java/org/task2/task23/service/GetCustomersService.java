package org.task2.task23.service;

import org.task2.task23.dto.CustomerInfo;

import java.sql.SQLException;
import java.util.List;

public interface GetCustomersService extends DbConstants {
    List<CustomerInfo> get5000Customers(int offset) throws SQLException;
}
