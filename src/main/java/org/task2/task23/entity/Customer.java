package org.task2.task23.entity;

public class Customer {
    private String customerId;
    private int col1;
    private int col2;
    private int col3;
    private int col4;
    private int col5;

    public Customer(String customerId,
                    int col1,
                    int col2,
                    int col3,
                    int col4,
                    int col5
    ) {
        this.customerId = customerId;
        this.col1 = col1;
        this.col2 = col2;
        this.col3 = col3;
        this.col4 = col4;
        this.col5 = col5;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
}
