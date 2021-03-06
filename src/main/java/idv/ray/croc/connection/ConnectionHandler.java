package idv.ray.croc.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import idv.ray.croc.client.Options;
import idv.ray.croc.exception.CommunicationException;
import idv.ray.croc.exception.CommunicationException.MessagePatternException;

public abstract class ConnectionHandler {

	/* FileHeader pattern: FileHeader@filName@fileLength */
	public enum Message {
		Ack, Ready, ReceiverIsReady, FileHeader
	}

	private static Logger staticLogger = LogManager.getLogger();
	protected Logger logger;
	protected Socket socket;
	protected BufferedReader reader;
	protected BufferedWriter writer;

	/* open input reader and output writer in the constructor */
	public ConnectionHandler(Socket socket) throws IOException {
		logger = LogManager.getLogger(getClass());
		this.socket = socket;
		socket.setSoTimeout(Options.SOCKET_TIMEOUT);

		/* set input reader and output writer */
		try {
			this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
			logger.debug("socket reader and writer opened");
		} catch (IOException e) {
			throw new IOException("fial to open input reader and output writer", e);
		}

	}

	public synchronized void send(String message) throws IOException {
		try {
			writer.write(message + "\n");
			logger.debug("send: " + message);
			writer.flush();
		} catch (IOException e) {
			throw new IOException("fail to write message: " + message, e);
		}
	}

	public synchronized String read() throws IOException {
		try {
			String msg = reader.readLine();
			logger.debug("read: " + msg);
			return msg;
		} catch (IOException e) {
			throw new IOException("fail to read message", e);
		}
	}

	public void close() throws IOException {
		try {
			socket.close();
		} catch (IOException e) {
			throw new IOException("fail to close connectionHandler", e);
		}
	}

	public void setTimeout(int ms) throws SocketException {
		socket.setSoTimeout(ms);
	}

	public synchronized Socket getSocket() {
		return this.socket;
	}

	public static void transferFile(InputStream in, OutputStream out) throws IOException {
		BufferedInputStream bis = new BufferedInputStream(in);
		BufferedOutputStream bos = new BufferedOutputStream(out);
		byte[] buffer = new byte[Options.FILE_BUFFER_SIZE];
		int len = 0;
		try {
			while ((len = bis.read(buffer)) >= 0) {
				bos.write(buffer, 0, len);
				staticLogger.debug("sending file...");
				staticLogger.debug("data: " + new String(buffer, StandardCharsets.UTF_8));
			}
			bis.close();
			bos.close();

			staticLogger.debug("sending file loop ends");
		} catch (IOException e) {
			throw new IOException("fail to transfer file", e);
		}
//		finally {
//			/*
//			 * only the file in&outpustream need to be closed, the socket in&outputstream
//			 * are controlled outside
//			 */
//			if (bis != null && in instanceof FileInputStream)
//				bis.close();
//			if (bos != null && out instanceof FileOutputStream)
//				bos.close();
//		}
	}

	public static void checkMessagePattern(String msg, String pattern) throws MessagePatternException {
		if (msg.matches(pattern))
			return;
		else
			throw new CommunicationException.MessagePatternException("the message pttern must be \"" + pattern + "\"");
	}

}
