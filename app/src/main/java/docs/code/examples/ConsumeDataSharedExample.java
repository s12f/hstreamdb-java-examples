package docs.code.examples;

import static java.util.concurrent.TimeUnit.SECONDS;

import io.hstream.Consumer;
import io.hstream.HRecordReceiver;
import io.hstream.HStreamClient;
import io.hstream.Subscription;
import java.util.concurrent.TimeoutException;

public class ConsumeDataSharedExample {
  public static void main(String[] args) throws Exception {
    String serviceUrl = "127.0.0.1:6570";
    if (System.getenv("serviceUrl") != null) {
      serviceUrl = System.getenv("serviceUrl");
    }

    String stream = "stream_h_records";
    String subscription = "your-subscription-id";
    String consumer1 = "your-consumer1-name";
    String consumer2 = "your-consumer2-name";
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    // create a subscription
    makeSubscriptionExample(client, stream, subscription);

    // create two consumers to consume records including two ordering keys.
    Thread t1 =
        new Thread(() -> consumeDataFromSubscriptionSharedExample(client, subscription, consumer1));
    Thread t2 =
        new Thread(() -> consumeDataFromSubscriptionSharedExample(client, subscription, consumer2));
    t1.start();
    t2.start();
    t1.join();
    t2.join();

    // delete subscription
    client.deleteSubscription(subscription);
    // close client
    client.close();
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
      // sleep 5s for consuming records
      consumer.startAsync().awaitRunning();
      consumer.awaitTerminated(5, SECONDS);
    } catch (TimeoutException e) {
      // stop consumer
      consumer.stopAsync().awaitTerminated();
    }
  }
}
