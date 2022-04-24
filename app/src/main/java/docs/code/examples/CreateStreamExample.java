package docs.code.examples;

import io.hstream.HStreamClient;

public class CreateStreamExample {
  public static void main(String[] args) throws Exception {
    // TODO(developer): Replace these variables before running the sample.
    // String serviceUrl = "your-service-url-address";
    String serviceUrl = "192.168.1.170:1234";
    String streamName = "your-stream-name";

    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    createStreamExample(client, streamName);
  }

  public static void createStreamExample(HStreamClient client, String streamName) {
    client.createStream(streamName);
  }
}
