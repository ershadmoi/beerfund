package com.junaid.beerfund.utils;

import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.junaid.beerfund.BeerFundReader;
import com.junaid.beerfund.BeerFundWriter;

/**
 * A Helper that wraps the {@link LambdaLogger}
 * @author Ershad
 */
public class LambdaLoggerHelper {
	// A static self-reference used in a singleton
	private static final LambdaLoggerHelper instance = new LambdaLoggerHelper();
	
	// The lambda logger
	private LambdaLogger lambdaLogger;
	
	// Private constructor to prevent object creation
	// for singleton
	private LambdaLoggerHelper() { }
	
	/**
	 * Accessor to get the current logging instance reference
	 * held by this helper
	 * @return		The wrapped singleton logger instance
	 */
	public static LambdaLoggerHelper getLoggerInstance() {
		return instance;
	}
	
	/**
	 * Logs the provided string if not null to the underlying
	 * lambda logger
	 * @param logEntry 	String to be logged
	 */
	public void log(final String logEntry) {
		if(lambdaLogger != null) {
			lambdaLogger.log(logEntry);
		}
	}
	
	/**
	 * An initializer called at {@link BeerFundReader} or {@link BeerFundWriter}
	 * handleRequest() lifecycle stage
	 * @param lambdaLogger
	 */
	public static void initialize(final LambdaLogger lambdaLogger) {
		instance.lambdaLogger = lambdaLogger;
	}
}
