package com.mobile_app_server.service.impl;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class PrintHelloTask implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        System.out.println(">>> Hello from Activiti Service Task!");
    }
}