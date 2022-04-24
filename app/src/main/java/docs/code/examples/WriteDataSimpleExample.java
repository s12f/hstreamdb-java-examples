package docs.code.examples;

import io.hstream.*;
import io.hstream.Record;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WriteDataSimpleExample {
  public static void main(String[] args) {
    // TODO (developers): Replace these variables for your own use cases.
    String serviceUrl = "192.168.1.170:1234";
    String streamName1 = "demo3";
    String streamName2 = "demo2";

    // We do not recommend write both raw data and HRecord data into the same stream.
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();

    writeHRecordData(client, streamName1);
  }

  public static void writeHRecordData(HStreamClient client, String streamName) {
    // Create a basic producer for low latency scenarios
    // For high throughput scenarios, please see the next section "Using `BufferedProducer`s"
    Producer producer = client.newProducer().stream(streamName).build();

    HRecord hRecord =
        HRecord.newBuilder()
            .put("int", 10)
            .put("boolean", true)
            .put("array", HArray.newBuilder().add(1).add(2).add(3).build())
            .put("string", "h".repeat(100))
            .build();

    for (int i = 0; i <= 3000; i++) {
      Record record = Record.newBuilder().hRecord(hRecord).build();
      // If the data is written successfully, returns a server-assigned record id
      CompletableFuture<String> recordId = producer.write(record);
      try {
        System.out.println("Wrote message ID: " + recordId.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }

  private static void writeRawData(HStreamClient client, String streamName) {
    Producer producer = client.newProducer().stream(streamName).build();
    List<String> messages = Arrays.asList("first", "second");
    for (final String message : messages) {
      Record record =
          Record.newBuilder().rawRecord(message.getBytes(StandardCharsets.UTF_8)).build();
      CompletableFuture<String> recordId = producer.write(record);
      try {
        System.out.println("Published message ID: " + recordId.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}
