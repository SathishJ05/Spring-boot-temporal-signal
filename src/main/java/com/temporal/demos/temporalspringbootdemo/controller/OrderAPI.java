package com.temporal.demos.temporalspringbootdemo.controller;

import com.temporal.demos.temporalspringbootdemo.workflows.DemoWorkflow;
import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import io.temporal.client.WorkflowStub;
import io.temporal.workflow.Async;
import io.temporal.workflow.ExternalWorkflowStub;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderAPI {

    @Autowired
    private WorkflowClient workflowClient;

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @GetMapping("/addOrder")
    public String addOrder(@RequestParam("orderId") String orderId) {
        DemoWorkflow demoWorkflow = workflowClient.newWorkflowStub(DemoWorkflow.class,
                WorkflowOptions.newBuilder().setWorkflowId("Order-Workflow-"+orderId).setTaskQueue("OrderWorkflowTaskQueue").build());

        logger.info("Calling addOrder workflow");

        WorkflowClient.start(demoWorkflow::execOrder, orderId);
        //String orderStatus = demoWorkflow.execOrder(orderId);
        logger.info("Workflow addOrder success");

        return "Order-Workflow-"+orderId+" : Added Successfully";
    }

    @GetMapping("/orderStatus")
    public String orderStatus(@RequestParam("orderId") String orderId) {

        WorkflowStub workflow =  workflowClient.newUntypedWorkflowStub("Order-Workflow-"+orderId);

        logger.info("Calling orderStatus workflow");
        workflow.signal("orderStatus" , orderId);
        logger.info("Workflow orderStatus success");

        return orderId+" : Approved Successfully...";
    }

    @GetMapping("/fulfillOrder")
    public String fulfillOrder(@RequestParam("orderId") String orderId) {

        WorkflowStub workflow =  workflowClient.newUntypedWorkflowStub("Order-Workflow-"+orderId);

        logger.info("Calling fulfillOrder workflow");
        workflow.signal("fulfillOrder" , orderId);
        logger.info("Workflow fulfillOrder success");

        return orderId+" : Approved Successfully...";
    }

    @GetMapping("/getOrderStatus")
    public String getOrderStatus(@RequestParam("orderId") String orderId) {

        WorkflowStub workflow =  workflowClient.newUntypedWorkflowStub("Order-Workflow-"+orderId);

        logger.info("Calling getOrderStatus workflow");
        String status = workflow.query("getStatus", String.class, orderId);
        logger.info("Workflow getOrderStatus success");

        return status;
    }
}
