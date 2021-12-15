package fr.gouv.education.tribu.api.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.logging.LogLevel;
import org.springframework.context.ApplicationContext;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.gouv.education.tribu.api.model.AbstractResponse;
import fr.gouv.education.tribu.api.model.TribuApiResponse;

/**
 * Generic controller for logging and process json response
 * 
 * @author Loïc Billon
 *
 */
public abstract class AbstractWsController {


	protected static final Log LOGGER = LogFactory.getLog("tribu.api");
	protected static final Log STK_LOGGER = LogFactory.getLog("tribu.api-stacktraces");
	
	protected static final String LOG_SEPARATOR = " ";

	
	@Autowired
	protected ApplicationContext context;
    
	/**
	 * 
	 * Default return method
	 * 
	 * If docs is null, return an empty response
	 * 
	 * @param contents some docs
	 * @param query method paramaters (for logging)
	 * @param startTime start time
	 * @param error the error code
	 * @param args the error code args
	 * @return json
	 */
	protected ResponseEntity<String> logAndReturn(AbstractResponse result, String query, String user, Long startTime,
			ContentErrorCode error, Object... args) {
		
		if(result != null) {
			if (result.getContents() != null && result.getContents().isEmpty()) {
				error = ContentErrorCode.WARN_NO_DATA;
			}
		}
		else {
			result = new TribuApiResponse(null);
		}


		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;

		String logMsg = formatLog(query, user, startTime, elapsedTime, error, args);

		if (error.getLevel() == LogLevel.ERROR) {
			result.setTicket(startTime.toString());
			LOGGER.error(logMsg);
		} else if (error.getLevel() == LogLevel.WARN) {
			LOGGER.warn(logMsg);
		} else if (error.getLevel() == LogLevel.INFO) {
			LOGGER.info(logMsg);
		} else if (error.getLevel() == LogLevel.DEBUG) {
			LOGGER.debug(logMsg);
		}

		String resultStr;
		ObjectMapper mapper = new ObjectMapper();

		result.setElapsedTime(elapsedTime);
		result.setCode(error.getCode());
		
		String message = error.getMessage();
		if(args.length > 0) {
			message = String.format(error.getMessage(), args);
		}
		result.setMessage(message);
		
		
		try {
			resultStr = mapper.writeValueAsString(result);
			return new ResponseEntity<String>(resultStr, error.getStatus());

		} catch (JsonProcessingException e) {

			return logStackAndReturn(e, query, user, startTime, ContentErrorCode.ERROR_TECH);
		}

	}
	
	/**
	 * Return method with stacktrace
	 * 
	 * @param e the exception
	 * @param params method paramaters (for logging)
	 * @param startTime start time
	 * @param error the error code
	 * @param args the error code args
	 * @return json
	 */
	protected ResponseEntity<String> logStackAndReturn(Exception e, String query, String user, Long startTime,
			ContentErrorCode error, Object... args) {
		
		
		ResponseEntity<String> retStr = logAndReturn(null, query, user, startTime, error, args);
		
		String logMsg = formatLog(query, user, startTime, null, error, args);

		STK_LOGGER.error(logMsg, e);
		
		return retStr;
		
	}
	
	/**
	 * Produce a log line
	 * 
	 * @param params
	 * @param startTime
	 * @param elapsedTime
	 * @param error
	 * @param args
	 * @return
	 */
	private String formatLog(String query, String user, Long startTime, Long elapsedTime, ContentErrorCode error, Object... args) {
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(error.getCode());
		sb.append(LOG_SEPARATOR);
		
		sb.append(user);
		sb.append(LOG_SEPARATOR);		

		sb.append(query);
		sb.append(LOG_SEPARATOR);
		
		sb.append(elapsedTime);
		sb.append(LOG_SEPARATOR);
		
		if(error.getLevel() == LogLevel.ERROR) {
			sb.append(startTime);
			sb.append(LOG_SEPARATOR);	
		}		
		
		StringBuilder sbErr = new StringBuilder();
		if (StringUtils.isNotBlank(error.getMessage())) {
			
			String message = error.getMessage();
			if(args.length > 0) {
				message = String.format(error.getMessage(), args);
			}
			
			sbErr.append(message);
			
			sbErr.append(LOG_SEPARATOR);
			
			sb.append(sbErr);
			

		}

		
		return sb.toString();
		
	}

}
