package org.task2.task23.service;

import org.task2.task23.entity.Customer;

import java.sql.SQLException;
import java.util.List;

public interface GetCustomersService {
    List<Customer> get5000Customers(int offset) throws SQLException;
}
