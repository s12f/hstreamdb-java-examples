package docs.code.examples;

import io.hstream.HStreamClient;

public class DeleteStreamExample {
  public static void main(String[] args) throws Exception {
    // TODO(developer): Replace these variables before running the sample.
    // String serviceUrl = "your-service-url-address";
    String serviceUrl = "192.168.1.170:1234";
    String streamName = "your-stream-name";

    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    deleteStreamExample(client, streamName);
    deleteStreamForceExample(client, streamName);
  }

  public static void deleteStreamExample(HStreamClient client, String streamName) {
    client.deleteStream(streamName);
  }

  public static void deleteStreamForceExample(HStreamClient client, String streamName) {
    client.deleteStream(streamName, true);
  }
}
