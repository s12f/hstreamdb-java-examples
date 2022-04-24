package docs.code.examples;

import io.hstream.*;
import io.hstream.Record;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WriteDataBufferedExample {
  public static void main(String[] args) {
    // TODO (developers): Replace these variables for your own use cases.
    String serviceUrl = "192.168.1.170:1234";
    String streamName1 = "demo2";

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
            //            .recordCountLimit(maxNumberOfRecordBeforeFlush) // optional
            //            .flushIntervalMs(maxTimeIntervalBeforeFlush) // optional
            //            .maxBytesSize(maxSizeBeforeFlush) // optional
            .build();
    List<CompletableFuture<String>> recordIds = new ArrayList<>();

    for (int i = 0; i < 3000; i++) {
      HRecord hRecord =
          HRecord.newBuilder()
              .put("int", 10)
              .put("boolean", true)
              .put("array", HArray.newBuilder().add(1).add(2).add(3).build())
              .put("string", "h".repeat(1))
              .build();
      Record record = Record.newBuilder().hRecord(hRecord).build();
      CompletableFuture<String> recordId = producer.write(record);
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
