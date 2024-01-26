package com.temporal.demos.temporalspringbootdemo.workflows;

import io.cloudevents.CloudEvent;
import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.SignalMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface DemoWorkflow {

    @WorkflowMethod
    String execOrder(String orderId);

    @SignalMethod
    void orderStatus(String orderId);

    @SignalMethod
    void fulfillOrder(String orderId);

    @QueryMethod
    String getStatus(String orderId);

}
