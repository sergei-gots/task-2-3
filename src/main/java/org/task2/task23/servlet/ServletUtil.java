package org.task2.task23.servlet;

public interface ServletUtil {
    default String href (String text, String endPoint) {
        return "<a href=http://localhost:8080/task-2-3" + endPoint + ">" + text + "</a>";
    }

    default String head (String title) {
        return "<!DOCTYPE html><html><head><title>" + title + "</title></head>";
    }

}
