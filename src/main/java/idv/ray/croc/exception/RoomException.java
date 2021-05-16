package idv.ray.croc.exception;

public class RoomException {

	public static class NoSenderException extends Exception {
		public NoSenderException(String msg) {
			super(msg);
		}
	}

	public static class OverwriteException extends Exception {
		public OverwriteException(String msg) {
			super(msg);
		}
	}

}
