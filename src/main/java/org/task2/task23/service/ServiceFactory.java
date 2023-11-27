package org.task2.task23.service;

public class ServiceFactory {
    private final static GetCustomersService service = new GetCustomerServiceImpl();
    private final static Initializer initializer = new InitializerImpl();
    public static Initializer getInitializerInstance() {
        return initializer;
    }
    public static GetCustomersService getServiceInstance() {
        return service;
    }
}
