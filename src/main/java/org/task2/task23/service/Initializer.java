package org.task2.task23.service;

import java.sql.SQLException;

public interface Initializer extends DbConstants {
    void validateData() throws SQLException;
}
