package eu.cloudopting.tosca.utils;

import org.apache.commons.exec.DefaultExecuteResultHandler;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteWatchdog;

public class R10kResultHandler extends DefaultExecuteResultHandler {
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
		System.out
				.println("[resultHandler] The modules downloaded ...");
	}

	@Override
	public void onProcessFailed(final ExecuteException e) {
		super.onProcessFailed(e);
		if (watchdog != null && watchdog.killedProcess()) {
			System.err.println("[resultHandler] The R10K process timed out");
		} else {
			System.err
					.println("[resultHandler] The R10K process failed to do : "
							+ e.getMessage());
		}
	}

}
