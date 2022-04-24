package docs.code.examples;

import io.hstream.HStreamClient;

public class CreateStreamExample {
  public static void main(String[] args) throws Exception {
    // TODO(developer): Replace these variables before running the sample.
    String serviceUrl = "127.0.0.1:6570";
    if (System.getenv("serviceUrl") != null) {
      serviceUrl = System.getenv("serviceUrl");
    }

    String streamName1 = "stream_h_records";
    String streamName2 = "stream_raw_records";

    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    createStreamExample(client, streamName1);
    createStreamExample(client, streamName2);
    client.close();
  }

  public static void createStreamExample(HStreamClient client, String streamName) {
    client.createStream(streamName);
  }
}
