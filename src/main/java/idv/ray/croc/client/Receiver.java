package idv.ray.croc.client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import idv.ray.croc.connection.ConnectionHandler;
import idv.ray.croc.exception.ClientException;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.CommunicationException.MessagePatternException;
import idv.ray.croc.exception.RelayException;

public class Receiver extends AbstClient {

	public Receiver(Options options) {
		super(ClientType.Receiver, options);
	}

	@Override
	public void start() throws IOException, ClientException, RelayException, CommunicationException {
		String localRelayIp = getLocalRelayIp();
		/* determine using local or remote */
		this.relayConnection = super.getRelayConnection("", options.getRelayAddress());

		receiveFile();

	}

	private void receiveFile() throws IOException, MessagePatternException {
		String msg = relayConnection.read();
		ConnectionHandler.checkMessagePattern(msg.split("@")[0], ConnectionHandler.Message.FileName.toString());
		String fileName = msg.split("@")[1];
		File file = new File(options.getDownloadPath() + "/" + fileName);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			if (!options.isOverwriteFile()) {
				file = new File(options.getDownloadPath() + "/" + fileName + "(new)");
			}
		}
		ConnectionHandler.transferFile(relayConnection.getSocket().getInputStream(), new FileOutputStream(file));
		relayConnection.send(ConnectionHandler.Message.Finished.toString());
	}

	private String getLocalRelayIp() {

		return listenToBroadcast();
	}

	private String listenToBroadcast() {

		/* TODO */
		return null;
	}

}
