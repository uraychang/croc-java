package idv.ray.croc.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

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
	public void start() throws IOException, ClientException, CommunicationException {
		try {

			String localRelayIp = getLocalRelayIp();
			/* determine using local or remote */
			this.relayConnection = super.getRelayConnection("localhost", options.getRelayAddress());
			receiveFile();
		} catch (IOException | ClientException | CommunicationException e) {
			throw e;
		} finally {
			close();
		}
	}

	private void receiveFile() throws IOException, MessagePatternException {
		String msg = relayConnection.read();
		ConnectionHandler.checkMessagePattern(msg.split("@")[0], ConnectionHandler.Message.FileHeader.toString());
		String fileName = msg.split("@")[1];
		String longString = msg.split("@")[2];
		long fileLength = Long.parseLong(longString);
		logger.debug("download file name:" + fileName);
		File file = new File(options.getDownloadPath() + "/" + fileName);
		if (!file.exists()) {
			file.createNewFile();
		} else {
			if (!options.isOverwriteFile()) {
				file = new File(options.getDownloadPath() + "/" + fileName + "(new)");
			}
		}
		FileOutputStream fileOut = new FileOutputStream(file);
		ConnectionHandler.transferFile(relayConnection.getSocket().getInputStream(), fileOut);
	}

	private String getLocalRelayIp() {

		return listenToBroadcast();
	}

	private String listenToBroadcast() {

		/* TODO */
		return null;
	}

}
