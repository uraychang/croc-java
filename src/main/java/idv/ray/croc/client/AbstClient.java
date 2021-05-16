package idv.ray.croc.client;

import java.io.IOException;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import idv.ray.croc.connection.ConnectionHandler;
import idv.ray.croc.connection.ServerConnectionHandler;
import idv.ray.croc.exception.ClientException;
import idv.ray.croc.exception.ClientException.NoRelayConnectedException;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.CommunicationException.MessagePatternException;
import idv.ray.croc.exception.ExceptionHandler;
import idv.ray.croc.exception.RelayException;

public abstract class AbstClient {

	public enum ClientType {
		Sender, Receiver
	}

	protected Logger logger = LogManager.getLogger();
	// [0] is local connection, [1] is remote connection
	private ServerConnectionHandler[] connectionHandlers;
	protected ServerConnectionHandler relayConnection;
	protected final ClientType clientType;
	protected Options options;

	public abstract void start() throws IOException, ClientException, RelayException, CommunicationException;

	public AbstClient(ClientType clientType, Options options) {
		Thread.currentThread().setUncaughtExceptionHandler(new ExceptionHandler());
		this.clientType = clientType;
		this.options = options;
	}

	/*
	 * failing to connecting with local relay is okay. Client gets "ack" from the
	 * relay then the connectionHanlder admitted.
	 */
	protected ServerConnectionHandler connectToServer(String addr) throws IOException, MessagePatternException {
		ServerConnectionHandler relay = null;
		try {
			Socket socket = new Socket(addr, options.getRelayPort());
			relay = new ServerConnectionHandler(socket);

			/* tell relay this is a Sender or a Receiver */
			relay.send(this.clientType.toString());
			String response = relay.read();
			ConnectionHandler.checkMessagePattern(response, ConnectionHandler.Message.Ack.toString());
			return relay;

		} catch (IOException e) {
			/* failing to connect with local relay is okay */
			logger.warn("fail to connect to " + addr + " relay", e.getMessage());
		}

		return null;
	}

	/* connection to both local and remote relay, and return one of them */
	protected ServerConnectionHandler getRelayConnection(String localRelayAddress, String remoteRelayAddress)
			throws NoRelayConnectedException, IOException, MessagePatternException {
		connectionHandlers[0] = connectToServer(localRelayAddress);
		if (!options.isTransferOnlyOnLocal()) {
			connectionHandlers[1] = connectToServer(remoteRelayAddress);
		}

		/* decide that use local or remote relay */
		ServerConnectionHandler relayConnection;
		if (connectionHandlers[0] != null) {
			relayConnection = connectionHandlers[0];
			logger.debug("sender connects to local relay");
		} else if (connectionHandlers[1] != null) {
			relayConnection = connectionHandlers[1];
			logger.debug("sender connects to remote relay");
		} else {
			throw new NoRelayConnectedException("both local and remote relay not connected");
		}
		return relayConnection;
	}

}