package org.task2.task23.service;

public class ServiceFactory {
    private final static Service service = new ServiceImpl();
    private final static Initializer initializer = new InitializerImpl();
    public static Initializer getInitializerInstance() {
        return initializer;
    }
    public static Service getServiceInstance() {
        return service;
    }
}
