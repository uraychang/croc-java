package idv.ray.croc.exception;

public class ClientException extends Exception {
	public ClientException(String msg) {
		super(msg);
	}

	public ClientException(String msg, Throwable e) {
		super(msg, e);
	}

	public static class NoRelayConnectedException extends ClientException {
		public NoRelayConnectedException(String msg) {
			super(msg);
		}
	}

	public static class FileTransferException extends ClientException {
		public FileTransferException(String msg, Throwable e) {
			super(msg);
		}
	}
}
