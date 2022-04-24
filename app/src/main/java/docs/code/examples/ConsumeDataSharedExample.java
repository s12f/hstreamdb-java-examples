package docs.code.examples;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.hstream.Consumer;
import io.hstream.HRecordReceiver;
import io.hstream.HStreamClient;
import io.hstream.Subscription;
import java.util.concurrent.TimeoutException;

public class ConsumeDataSharedExample {
  public static void main(String[] args) {
    String serviceUrl = "192.168.1.170:1234";
    //    String serviceUrl = "your-service-url";
    String stream = "your-stream-name";
    String subscription = "your-subscription-id";
    String consumer1 = "your-consumer1-name";
    String consumer2 = "your-consumer2-name";
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    makeSubscriptionExample(client, stream, subscription);
    consumeDataFromSubscriptionSharedExample(client, subscription, consumer1);
    consumeDataFromSubscriptionSharedExample(client, subscription, consumer2);
  }

  public static void makeSubscriptionExample(
      HStreamClient client, String streamName, String subId) {
    Subscription sub1 =
        Subscription.newBuilder().subscription(subId).stream(streamName)
            .ackTimeoutSeconds(600)
            .build();
    client.createSubscription(sub1);
  }

  public static void consumeDataFromSubscriptionSharedExample(
      HStreamClient client, String subscription, String consumerName) {
    HRecordReceiver receiver =
        ((hRecord, responder) -> {
          System.out.println("Received a record :" + hRecord.getHRecord());
          responder.ack();
        });
    Consumer consumer =
        client
            .newConsumer()
            .subscription(subscription)
            .name(consumerName)
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
