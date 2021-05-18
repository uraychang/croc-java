package idv.ray.croc.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ServerConnectionHandler extends ConnectionHandler {

	/* the socket in ClientConnectionHandler stands for server side */
	public ServerConnectionHandler(Socket socket) throws IOException {
		super(socket);
	}

	public void transferFileToServer(InputStream in) throws IOException {
		try {
			super.transferFile(in, socket.getOutputStream());
		} catch (IOException e) {
			throw new IOException("fail to transfer file to server", e);
		}
	}

	public void receiveFileFromServer(OutputStream out) throws IOException {
		try {
			super.transferFile(socket.getInputStream(), out);
		} catch (IOException e) {
			throw new IOException("fail to receive file from server", e);
		}
	}
}
