import java.io.IOException;

import idv.ray.croc.client.Options;
import idv.ray.croc.client.Receiver;
import idv.ray.croc.exception.ClientException;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.RelayException;

public class ReceiverTester {
	public static void main(String[] args) throws IOException, ClientException, RelayException, CommunicationException {
		Options options = new Options();
		options.setRelayAddress("asd");
		Receiver receiver = new Receiver(options);
		receiver.start();
	}
}
