package idv.ray.croc.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import idv.ray.croc.connection.ConnectionHandler;
import idv.ray.croc.exception.ClientException;
import idv.ray.croc.exception.ClientException.FileTransferException;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.CommunicationException.MessagePatternException;
import idv.ray.croc.exception.RelayException;
import idv.ray.croc.exception.RelayException.ServerInitException;
import idv.ray.croc.relay.LocalRelay;

public class Sender extends AbstClient {

	/* the file which is going to be transfered */
	private File file;

	public Sender(String filePath, Options options) {
		super(ClientType.Sender, options);
		this.file = iniFile(filePath);
	}

	/* load the file via the filePath */
	private File iniFile(String filePath) {
		File file = new File(filePath);
		return file;
	}

	public void start() throws IOException, RelayException, ClientException, CommunicationException {
		try {
			initLocalRelay();
			this.relayConnection = getRelayConnection("localhost", options.getRelayAddress());
			waitReceiver();
			sendFile();
		} catch (IOException | RelayException | ClientException | CommunicationException e) {
			throw e;
		} finally {
			/* no matter what, the sender should be closed eventually */
			close();
		}
	}

	/* initialize the local relay to start listening for clients */
	private void initLocalRelay() throws ServerInitException {
		LocalRelay localRelay = new LocalRelay();
		localRelay.listen(options.getRelayPort());
	}

	/* waiting for receiver to connect with relay server */
	private void waitReceiver() throws IOException, CommunicationException {

		relayConnection.setTimeout(Options.WAIT_RECEIVER_TIMEOUT);
		relayConnection.send(ConnectionHandler.Message.Ready.toString());
		String response = relayConnection.read();
		ConnectionHandler.checkMessagePattern(response, ConnectionHandler.Message.ReceiverIsReady.toString());
		relayConnection.setTimeout(Options.SOCKET_TIMEOUT);

	}

	// start transfering file
	private void sendFile() throws FileTransferException, IOException, MessagePatternException {
		/* send file name */
		relayConnection.send(ConnectionHandler.Message.FileName.toString() + "@" + file.getName());
		
		/* send file */
		relayConnection.transferFileToServer(new FileInputStream(file));
		String message = relayConnection.read();
		ConnectionHandler.checkMessagePattern(message, ConnectionHandler.Message.Finished.toString());
	}

	private void close() throws IOException {
		logger.debug("close relay connection");
		if (relayConnection != null) {
			relayConnection.close();
		}
	}

}