package docs.code.examples;

import io.hstream.Consumer;
import io.hstream.HRecordReceiver;
import io.hstream.HStreamClient;
import io.hstream.Subscription;

public class ConsumeDataSimpleExample {
  public static void main(String[] args) {
    String serviceUrl = "192.168.1.170:1234";
    String streamName = "demo1";
    String subscriptionId = "ccc";
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    makeSubscriptionExample(client, streamName, subscriptionId);
    consumeDataFromSubscriptionExample(client, subscriptionId);
  }

  public static void makeSubscriptionExample(
      HStreamClient client, String streamName, String subId) {
    Subscription sub1 =
        Subscription.newBuilder().subscription(subId).stream(streamName)
            .ackTimeoutSeconds(600)
            .build();
    client.createSubscription(sub1);
  }

  public static void consumeDataFromSubscriptionExample(HStreamClient client, String subId) {
//    HRecordReceiver receiver =
//        ((hRecord, responder) -> {
//          System.out.println("Received a record :" + hRecord.getHRecord());
//          responder.ack();
//        });
    Consumer consumer = client.newConsumer().subscription(subId).hRecordReceiver(((hRecord, responder) -> {
      System.out.println("Received a record :" + hRecord.getHRecord());
      responder.ack();
    })).build();
    consumer.startAsync().awaitRunning();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
