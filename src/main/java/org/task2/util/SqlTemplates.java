package org.task2.util;

public interface SqlTemplates {

    String CUSTOMER = "customer";
    String TABLE_I = "table_";
    String TABLE_MANY = "table_many";
    String SQL_STATEMENT_IS_TABLE_EXIST = "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = ?)";

}
