package idv.ray.croc.relay;

import java.io.IOException;
import java.net.Socket;

import idv.ray.croc.client.AbstClient.ClientType;
import idv.ray.croc.connection.ClientConnectionHandler;
import idv.ray.croc.connection.ConnectionHandler;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.CommunicationException.MessagePatternException;
import idv.ray.croc.exception.RoomException.NoSenderException;
import idv.ray.croc.exception.RoomException.OverwriteException;
import idv.ray.croc.room.Room;

/*A client socket will be identified a sender or a receiver by the 
clientHanlder, and start a new receiverHandler or senderHandler thread
respectively*/
public class ClientHandler implements Runnable {

	private ClientConnectionHandler clientConnection;
	/*
	 * clientHandler needs to read the room information to determine whether the
	 * client is able to join the room. the buffer for file transfer is also in the
	 * room.
	 */
	private Room room;

	public ClientHandler(Socket socket, Room room) throws IOException {
		this.clientConnection = new ClientConnectionHandler(socket);
		this.room = room;
	}

	public void run() {

		String request;
		try {
			request = clientConnection.read();
			ConnectionHandler.checkMessagePattern(request,
					ClientType.Sender.toString() + "|" + request.equals(ClientType.Receiver.toString()));
		} catch (MessagePatternException e) {
			throw new RuntimeException("fail to accept client", e);
		} catch (IOException e) {
			throw new RuntimeException("server fail to read message from the client", e);
		}

		/*
		 * if the request msg is Sender: call handleSender(), otherwise call
		 * handleReceiver()
		 */
		if (request.equals(ClientType.Sender.toString())) {
			try {
				handleSender();
				clientConnection.setSender(true);
			} catch (MessagePatternException | OverwriteException | IOException e) {
				throw new RuntimeException("server fail to handle the sender", e);
			}
		} else {
			try {
				handleReceiver();
			} catch (OverwriteException | NoSenderException | IOException e) {
				throw new RuntimeException("server fail to handle the receiver", e);
			}
		}

	}

	// handling a socket client from sender side
	private void handleSender() throws MessagePatternException, OverwriteException, IOException {
		// if room sets sender successfully
		room.setSender(clientConnection);
		clientConnection.send(ConnectionHandler.Message.Ack.toString());

		String response = clientConnection.read();
		if (!response.equals(ConnectionHandler.Message.Ready.toString())) {
			throw new CommunicationException.MessagePatternException("the response message must be \"Ready\"");
		}

		/* wait for receiver set in room */
		while (!room.receiverIsReady())
			;

		clientConnection.send(ConnectionHandler.Message.ReceiverIsReady.toString());
		/* get the fileName from sender and pass to receiver */
		String msg = clientConnection.read();
		ConnectionHandler.checkMessagePattern(msg.split("@")[0], ConnectionHandler.Message.FileName.toString());
		room.getReceiverConnection().send(msg);

		/* start transferring file */
		room.transferFile();
		clientConnection.send(ConnectionHandler.Message.Finished.toString());
		clientConnection.close();
	}

	// handling a socket client from sender side
	private void handleReceiver() throws OverwriteException, NoSenderException, IOException {

		room.setReceiver(clientConnection);
		clientConnection.send(ConnectionHandler.Message.Ack.toString());
		/* wait for file transfer finished */
		while (!room.fileTransferFinished().get())
			;
	}
}
