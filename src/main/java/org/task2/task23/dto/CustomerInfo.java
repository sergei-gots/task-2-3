package org.task2.task23.dto;

import org.task2.task23.service.DbConstants;

public class CustomerInfo implements DbConstants {
    private final String customerId;
    private final int[] cols = new int[GRAND_TOTAL_COL_I_COUNT];

    private String groups;

    public CustomerInfo(String customerId) {
        this.customerId = customerId;
    }

    public int[] getCols() {
        return cols;
    }

    public void setGroups(String groups) {

        this.groups = groups;
    }

}
