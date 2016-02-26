package eu.cloudopting.tosca.utils;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class R10kResultHandler extends DefaultExecuteResultHandler {

	private final Logger log = LoggerFactory.getLogger(R10kResultHandler.class);

	private ExecuteWatchdog watchdog;

	public R10kResultHandler(final ExecuteWatchdog watchdog) {
		this.watchdog = watchdog;
	}

	public R10kResultHandler(final int exitValue) {
		super.onProcessComplete(exitValue);
	}

	@Override
	public void onProcessComplete(final int exitValue) {
		super.onProcessComplete(exitValue);
		log.debug("[resultHandler] The modules downloaded ...");
	}

	@Override
	public void onProcessFailed(final ExecuteException e) {
		super.onProcessFailed(e);
		if (watchdog != null && watchdog.killedProcess()) {
			log.error("[resultHandler] The R10K process timed out");
		} else {
			log.error("[resultHandler] The R10K process failed to do : " + e.getMessage());
		}
	}

}
