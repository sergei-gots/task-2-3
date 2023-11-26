package org.task2.task23.service;

import org.task2.task23.entity.Customer;

import java.util.List;

public interface Service {
    List<Customer> get5000Customers(int offset);
}
