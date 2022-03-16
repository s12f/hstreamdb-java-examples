package docs.code.examples;

import io.hstream.Record;
import io.hstream.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WriteDataBufferedExample {
  public static void main(String[] args) {
    // TODO (developers): Replace these variables for your own use cases.
    String serviceUrl = "192.168.1.170:1234";
    String streamName1 = "demo3";

    // We do not recommend write both raw data and HRecord data into the same stream.
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    writeHRecordDataWithBufferedProducers(client, streamName1);
  }

  public static void writeHRecordDataWithBufferedProducers(
      HStreamClient client, String streamName) {
    // When creating a `bufferedProducers`, we can set three trigger mode for bufferedProducer.
    //
    int maxNumberOfRecordBeforeFlush = 100; // default: 100, the value must be greater than 0
    long maxTimeIntervalBeforeFlush = 100; // default: 100(ms), disabled if the value <= 0
    int maxSizeBeforeFlush = 4096; // default: 4096(Bytes), disabled if the value <= 0
    BufferedProducer producer =
        client.newBufferedProducer().stream(streamName)
            .recordCountLimit(maxNumberOfRecordBeforeFlush) // optional
            .flushIntervalMs(maxTimeIntervalBeforeFlush) // optional
            .maxBytesSize(maxSizeBeforeFlush) // optional
            .build();
    List<CompletableFuture<RecordId>> recordIds = new ArrayList<>();
    Random random = new Random();

    for (int i = 0; i < 100; i++) {
      double temp = random.nextInt(100) / 10.0 + 15 ;
      HRecord hRecord = HRecord.newBuilder().put("temperature", temp).build();
      Record record = Record.newBuilder().hRecord(hRecord).build();
      CompletableFuture<RecordId> recordId = producer.write(record);
      recordIds.add(recordId);
    }
    producer.flush();
    producer.close();
    System.out.println(
        "Wrote message IDs: "
            + recordIds.stream()
                .map(
                    (x) -> {
                      try {
                        return x.get();
                      } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                      }
                      return "";
                    })
                .toList());
  }
}
