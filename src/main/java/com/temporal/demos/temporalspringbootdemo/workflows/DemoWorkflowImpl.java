package com.temporal.demos.temporalspringbootdemo.workflows;

import com.temporal.demos.temporalspringbootdemo.activities.DemoActivities;
import io.cloudevents.CloudEvent;
import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@WorkflowImpl(taskQueues = "OrderWorkflowTaskQueue")
public class DemoWorkflowImpl implements DemoWorkflow {

    //private List<CloudEvent> eventList = new ArrayList<>();
    private String OrderStatus="Started";

    private DemoActivities demoActivities =
            Workflow.newActivityStub(DemoActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(2))
                            .build());

    @Override
    public String execOrder(String orderId) {

        //Creating the Order.
        demoActivities.addOrder(orderId);

        //Waiting for Signal.
        Workflow.await(() -> OrderStatus != "Started");

        //Approving the Order.
        demoActivities.approveOrder(orderId);

        Workflow.await(() -> OrderStatus != "Approved");

        //Completing the Order.
        demoActivities.completeOrder(orderId);
        return OrderStatus;
    }

    @Override // SignalMethod
    public void orderStatus(String orderId) {
        System.out.println("Approving the Order: "+orderId+" by the Signal.");
        OrderStatus = "Approved";
    }

    @Override
    public void fulfillOrder(String orderId) {
        System.out.println("Fulfilling the Order: "+orderId+" by the Signal.");
        OrderStatus = "Fulfilled";
    }

    @Override
    public String getStatus(String orderId) {
        System.out.println("Querying the Order: "+orderId+" by the query.");
        return OrderStatus;
    }

}
