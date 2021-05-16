package idv.ray.croc.exception;

public class RelayException extends Exception {

	public RelayException(String msg, Throwable e) {
		super(msg, e);
	}

	public RelayException() {
		super();
	}

	public static class ServerInitException extends RelayException {
		public ServerInitException(String msg, Throwable e) {
			super(msg, e);
		}
	}

}
