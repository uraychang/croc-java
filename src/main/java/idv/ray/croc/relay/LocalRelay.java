package idv.ray.croc.relay;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import idv.ray.croc.client.Options;
import idv.ray.croc.exception.ExceptionHandler;
import idv.ray.croc.exception.RelayException;
import idv.ray.croc.exception.RelayException.ServerInitException;
import idv.ray.croc.room.Room;

public class LocalRelay {

	private Logger logger = LogManager.getLogger(LocalRelay.class);

	// room keeps sender thread and receiver thread
	private Room room;
	private ServerSocket serverSocket;

	public LocalRelay() {
		room = new Room();
	}

	/*
	 * listen for client socket and start a new clientHandler thread. in
	 * clientHandler, the client will be idenfied to sender or receiver and added to
	 * room object
	 */
	public void listen(int port) throws ServerInitException {

		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			RelayException.ServerInitException ex = new RelayException.ServerInitException(
					"fail to initialize server socket", e);
			throw ex;
		}

		// create a anonymous thread to listen requests
		Thread listenThread = new Thread(new Runnable() {
			public void run() {

				while (true) {
					Socket socket;
					try {
						/* server socket start to accept client */
						socket = serverSocket.accept();
						/* create new thread for each client */
						Thread clientHanlderThread = new Thread(new ClientHandler(socket, room));
						clientHanlderThread.setUncaughtExceptionHandler(new ExceptionHandler());
						clientHanlderThread.start();
					} catch (IOException e) {
						/* if some error happened, try to close server socket */
						closeServer();
						throw new RuntimeException(e);
					}
				}
			}
		});
		listenThread.setUncaughtExceptionHandler(new ExceptionHandler());
		listenThread.start();

	}

	private void closeServer() {
		try {
			serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("fail to close server socket", e);
		}
	}

	/* broadcasts to notify other local computers "here is a relay" */
//	public void broadcast(){
//		//create a new thread to send broadcast message
//		new Thread(()->{
//			List broadcastAddrs = get all broadcast addresses of each networkInterface;
//			for(addr : broadcastAddrs){
//				send broadcast message via udp("local relay here"+localRelay ip);
//			}		
//		}).start();
//	}
}