package idv.ray.croc.connection;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientConnectionHandler extends ConnectionHandler {

	private AtomicBoolean isSender = new AtomicBoolean(false);

	/* the socket in ClientConnectionHandler stands for client side */
	public ClientConnectionHandler(Socket socket) throws IOException {
		super(socket);
	}

	public boolean isSender() {
		return isSender.get();
	}

	public void setSender(boolean isSender) {
		this.isSender.set(isSender);
	}

}
