package com.temporal.demos.temporalspringbootdemo.activities;

import io.cloudevents.CloudEvent;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface DemoActivities {
    void before(CloudEvent cloudEvent);
    void after(CloudEvent cloudEvent);

    void addOrder(String orderId);

    void approveOrder(String orderId);

    void completeOrder(String orderId);
}
