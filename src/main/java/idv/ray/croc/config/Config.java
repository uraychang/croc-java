//package idv.ray.croc.config;
//
//import java.io.IOException;
//import java.lang.Thread.UncaughtExceptionHandler;
//
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
//
//public class Config {
//
//	public enum ClientType {
//		Sender, Receiver
//	}
//
//	public static final int RELAY_PORT = 9001;
//	public static final String RELAY_ADDRESS = "localhost";// TODO: temporarily use localhost
//
//	public static final boolean TRANSFER_ONLY_ON_LOCAL = false;
//	/* socket timeout */
//	public static final int WAIT_RECEIVER_TIMEOUT = 1000 * 60 * 60 * 2;// 2hrs
//	public static final int SOCKET_TIMEOUT = 1000 * 30;
//	/* buffer sizes */
//	public static final int FILE_BUFFER_SIZE = 1024 * 4;
//
//}
