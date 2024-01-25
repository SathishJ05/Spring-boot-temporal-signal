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
    private String OrderStatus=null;

    private DemoActivities demoActivities =
            Workflow.newActivityStub(DemoActivities.class,
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofSeconds(2))
                            .build());

    @Override
    public String execOrder(String orderId) {
        demoActivities.addOrder(orderId);
        // wait for bulk requester complete signal
        Workflow.await(() -> OrderStatus != null);
        demoActivities.approveOrder(orderId);
        demoActivities.completeOrder(orderId);
        return OrderStatus;
    }

    @Override // SignalMethod
    public void orderStatus(String orderId) {
        System.out.println("Signal Successful...");
        OrderStatus = "Approved";
    }

}
