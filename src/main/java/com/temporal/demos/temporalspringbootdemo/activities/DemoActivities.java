package com.temporal.demos.temporalspringbootdemo.activities;

import io.cloudevents.CloudEvent;
import io.temporal.activity.ActivityInterface;

@ActivityInterface
public interface DemoActivities {

    void addOrder(String orderId);

    void approveOrder(String orderId);

    void completeOrder(String orderId);
}
