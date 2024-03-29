package docs.code.examples;

// main App for running all Examples
public class App {
  public static void main(String[] args) throws Exception {
    //    try { DeleteStreamExample.main(null); } catch (Exception ignored) {}
    CreateStreamExample.main(null);
    System.out.println("CreateStreamExample Done");
    ListStreamsExample.main(null);
    WriteDataSimpleExample.main(null);
    System.out.println("WriteDataSimpleExample Done");
    WriteDataBufferedExample.main(null);
    System.out.println("WriteDataBufferedExample Done");
    WriteDataWithKeyExample.main(null);
    System.out.println("WriteDataWithKeyExample Done");
    ConsumeDataSimpleExample.main(null);
    System.out.println("ConsumeDataSimpleExample Done");
    ConsumeDataSharedExample.main(null);
    System.out.println("ConsumeDataSharedExample Done");
    ConsumeDataWithErrorListenerExample.main(null);
    System.out.println("ConsumeDataWithErrorListenerExample Done");
    DeleteStreamExample.main(null);
    System.out.println("DeleteStreamExample Done");
  }
}
