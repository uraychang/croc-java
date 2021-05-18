package idv.ray.croc.room;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import idv.ray.croc.client.Options;
import idv.ray.croc.connection.ClientConnectionHandler;
import idv.ray.croc.connection.ConnectionHandler;
import idv.ray.croc.exception.RoomException.NoSenderException;
import idv.ray.croc.exception.RoomException.OverwriteException;

public class Room {

	private ClientConnectionHandler senderConnection;
	private ClientConnectionHandler receiverConnection;
	private AtomicBoolean fileTransferFinished;

	// used to transfer file
	byte[] fileBuffer;

	public Room() {
		this.fileBuffer = new byte[Options.FILE_BUFFER_SIZE];
		this.fileTransferFinished = new AtomicBoolean(false);
	}

	// clear this room when the transfer has finished
	public synchronized void clear() {
		clearSender();
		clearReceiver();
	}

	public synchronized void setSender(ClientConnectionHandler sender) throws OverwriteException {
		if (this.senderConnection == null) {
			this.senderConnection = sender;
		} else {
			throw new OverwriteException("sender already exists");
		}
	}

	public synchronized void setReceiver(ClientConnectionHandler receiver)
			throws OverwriteException, NoSenderException {

		if (this.senderConnection != null) {
			if (this.receiverConnection == null) {
				this.receiverConnection = receiver;
			} else {
				throw new OverwriteException("receiver already exists");
			}

		} else {
			throw new NoSenderException("no sender exists when setting a receiver in the room");
		}

	}

	public synchronized void clearSender() {
		this.senderConnection = null;
	}

	public synchronized void clearReceiver() {
		this.receiverConnection = null;
	}

	public synchronized boolean receiverIsReady() {
		return (receiverConnection != null);
	}

	public synchronized AtomicBoolean fileTransferFinished() {
		return fileTransferFinished;
	}

	public ClientConnectionHandler getSenderConnection() {
		return senderConnection;
	}

	public ClientConnectionHandler getReceiverConnection() {
		return receiverConnection;
	}

	public synchronized boolean getFileTransferFinished() {
		return fileTransferFinished.get();
	}

	public synchronized void setFileTransferFinished(boolean fileTransferFinished) {
		this.fileTransferFinished.set(fileTransferFinished);
	}

}