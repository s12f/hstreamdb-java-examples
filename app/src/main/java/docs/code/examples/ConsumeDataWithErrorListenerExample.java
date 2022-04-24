package docs.code.examples;

import static java.util.concurrent.TimeUnit.SECONDS;

import com.google.common.util.concurrent.Service;
import io.hstream.HRecordReceiver;
import io.hstream.HStreamClient;
import io.hstream.Subscription;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeoutException;

public class ConsumeDataWithErrorListenerExample {
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

  public static void consumeDataFromSubscriptionExample(HStreamClient client, String subscription) {
    HRecordReceiver receiver =
        ((hRecord, responder) -> {
          System.out.println("Received a record :" + hRecord.getHRecord());
          responder.ack();
        });
    var consumer =
        client
            .newConsumer()
            .subscription(subscription)
            .name("consumer_1")
            .hRecordReceiver(receiver)
            .build();
    consumer.addListener(
        new Service.Listener() {
          public void failed(Service.State from, Throwable failure) {
            System.out.println("consumer failed, with error: " + failure.getMessage());
          }
        },
        new ScheduledThreadPoolExecutor(1));
    try {
      consumer.startAsync().awaitRunning();
      consumer.awaitTerminated(30, SECONDS);
    } catch (TimeoutException e) {
      consumer.stopAsync();
    }
  }
}
