package docs.code.examples;

import io.hstream.HStreamClient;
import io.hstream.Stream;
import java.util.List;

public class ListStreamsExample {
  public static void main(String[] args) throws Exception {
    // TODO(developer): Replace these variables before running the sample.
    // String serviceUrl = "your-service-url-address";
    String serviceUrl = "192.168.1.170:1234";

    HStreamClient client = HStreamClient.builder().serviceUrl(serviceUrl).build();
    listStreamExample(client);
  }

  public static void listStreamExample(HStreamClient client) {
    List<Stream> streams = client.listStreams();
    for (Stream stream : streams) {
      System.out.println(stream.getStreamName());
    }
  }
}
