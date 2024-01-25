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

   /* @Override // WorkflowMethod
    public CloudEvent exec(CloudEvent cloudEvent) {
        eventList.add(cloudEvent);

        demoActivities.before(cloudEvent);

        // wait for second event

        Workflow.await(() -> eventList.size() == 2);

        demoActivities.after(cloudEvent);

        // return demo result CE
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.createObjectNode();
        ((ObjectNode)node).putArray("events");
        for(CloudEvent c : eventList) {
            ((ArrayNode)node.get("events")).add(c.getId());
        }
        ((ObjectNode)node).put("outcome", "done");
        return CloudEventBuilder.v1()
                .withId(String.valueOf(Workflow.newRandom().nextInt(1000 - 1 + 1) + 1))
                .withType("example.demo.result")
                .withSource(URI.create("http://temporal.io"))
                .withData(
                        "application/json",
                        (node.toPrettyString())
                                .getBytes(Charset.defaultCharset()))
                .build();

    }*/

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

    /*@Override
    public void orderStatus(CloudEvent cloudEvent) {

    }*/

    /*@Override // QueryMethod
    public CloudEvent getLastEvent() {
        return eventList.get(eventList.size() - 1);
    }*/
}
