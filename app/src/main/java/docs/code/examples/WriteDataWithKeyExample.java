package docs.code.examples;

import io.hstream.Record;
import io.hstream.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class WriteDataWithKeyExample {
  public static void main(String[] args) {
    // TODO (developers): Replace these variables for your own use cases.
    String serviceUrl = "192.168.1.170:1234";
    String streamName = "demo1";

    // We do not recommend write both raw data and HRecord data into the same stream.
    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    writeHRecordDataWithKey(client, streamName);
  }

  public static void writeHRecordDataWithKey(HStreamClient client, String streamName) {
    // For demonstrations, we would use the following as our ordering keys for the records.
    // As the documentations mentioned, if we do not give any ordering key, it will get a default
    // key and
    // mapped to some default shard.
    String key1 = "South";
    String key2 = "North";
    // Create a buffered producer with default triggers.
    BufferedProducer producer = client.newBufferedProducer().stream(streamName).build();

    List<CompletableFuture<RecordId>> recordIds = new ArrayList<>();
    Random random = new Random();

    for (int i = 0; i < 100; i++) {
      double temp = random.nextInt(100) / 10.0 + 15 ;
      HRecord hRecord = HRecord.newBuilder().put("temperature", temp).put("withKey", 1).build();
      Record record;
      if ((i % 2) == 0) {
        record = Record.newBuilder().hRecord(hRecord).orderingKey(key1).build();
      } else {
        record = Record.newBuilder().hRecord(hRecord).orderingKey(key2).build();
      }

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
