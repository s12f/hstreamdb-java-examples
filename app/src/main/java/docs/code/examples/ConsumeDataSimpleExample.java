package docs.code.examples;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.hstream.Consumer;
import io.hstream.HRecordReceiver;
import io.hstream.HStreamClient;
import io.hstream.Subscription;
import java.util.concurrent.TimeoutException;

public class ConsumeDataSimpleExample {
  public static void main(String[] args) {
    String serviceUrl = "192.168.1.170:1234";
    //    String serviceUrl = "your-service-url";
    String streamName = "your-stream-name";
    String subscriptionId = "your-subscription-id";
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

  public static void consumeDataFromSubscriptionExample(
      HStreamClient client, String subscriptionId) {
    HRecordReceiver receiver =
        ((hRecord, responder) -> {
          System.out.println("Received a record :" + hRecord.getHRecord());
          responder.ack();
        });
    Consumer consumer =
        client
            .newConsumer()
            .subscription(subscriptionId)
            .name("consumer_1")
            .hRecordReceiver(receiver)
            .build();
    try {
      consumer.startAsync().awaitRunning();
      consumer.awaitTerminated(30, SECONDS);
    } catch (TimeoutException e) {
      consumer.stopAsync();
    }
  }
}
