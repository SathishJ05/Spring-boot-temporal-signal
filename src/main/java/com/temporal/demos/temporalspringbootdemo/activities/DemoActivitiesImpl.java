package com.temporal.demos.temporalspringbootdemo.activities;

import io.cloudevents.CloudEvent;
import io.temporal.spring.boot.ActivityImpl;
import org.springframework.stereotype.Component;

@Component
@ActivityImpl(taskQueues = "OrderWorkflowTaskQueue")
public class DemoActivitiesImpl implements DemoActivities {

    @Override
    public void addOrder(String orderId) {
        System.out.println("Inside DemoActivitiesImpl.addOrder(): "+orderId);
        // todo
    }

    @Override
    public void approveOrder(String orderId) {
        System.out.println("Inside DemoActivitiesImpl.approveOrder(): "+orderId);
        // todo
      }

    @Override
    public void completeOrder(String orderId) {
        System.out.println("Inside DemoActivitiesImpl.completeOrder(): "+orderId);
        // todo
    }


}
