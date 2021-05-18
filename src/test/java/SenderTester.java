import java.io.IOException;

import idv.ray.croc.client.Options;
import idv.ray.croc.client.Sender;
import idv.ray.croc.exception.ClientException;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.RelayException;

public class SenderTester {
	public static void main(String[] args) throws IOException, RelayException, ClientException, CommunicationException {
		Options options = new Options();
		options.setRelayAddress("asd");
		Sender sender = new Sender("C:\\Users\\±i¦³ºÍ\\Desktop\\123.txt", options);
		sender.start();
	}
}
