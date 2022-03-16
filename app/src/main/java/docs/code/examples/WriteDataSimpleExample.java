package docs.code.examples;

import io.hstream.Record;
import io.hstream.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WriteDataSimpleExample {
  public static void main(String[] args) {
    // TODO (developers): Replace these variables for your own use cases.
    String serviceUrl = "192.168.1.170:1234";
    String streamName1 = "demo1";
    String streamName2 = "demo2";

    // We do not recommend write both raw data and HRecord data into the same stream.
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    writeHRecordData(client, streamName1);
    writeRawData(client, streamName2);
  }

  public static void writeHRecordData(HStreamClient client, String streamName) {
    // Create a basic producer for low latency scenarios
    // For high throughput scenarios, please see the next section "Using `BufferedProducer`s"
    Producer producer = client.newProducer().stream(streamName).build();

    HRecord hRecord1 = HRecord.newBuilder().put("temperature", 22).build();
    HRecord hRecord2 = HRecord.newBuilder().put("temperature", 18).build();

    List<HRecord> hRecords = Arrays.asList(hRecord1, hRecord2);

    for (final HRecord hRecord : hRecords) {
      Record record = Record.newBuilder().hRecord(hRecord).build();
      // If the data is written successfully, returns a server-assigned record id
      CompletableFuture<RecordId> recordId = producer.write(record);
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
      CompletableFuture<RecordId> recordId = producer.write(record);
      try {
        System.out.println("Published message ID: " + recordId.get());
      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
      }
    }
  }
}
