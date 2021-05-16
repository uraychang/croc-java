package idv.ray.croc.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExceptionHandler implements UncaughtExceptionHandler {

	protected Logger logger = LogManager.getLogger();

	public void uncaughtException(Thread t, Throwable e) {
		logger.error("uncaughtException: ", e);

	}

}
