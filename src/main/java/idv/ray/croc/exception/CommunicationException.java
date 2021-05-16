package idv.ray.croc.exception;

public class CommunicationException extends Exception {

	public CommunicationException(String msg) {
		super(msg);
	}

	public static class MessagePatternException extends CommunicationException {
		public MessagePatternException(String msg) {
			super(msg);
		}
	}
}
