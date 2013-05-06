package com.mcs.client.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSyncEngine implements SyncEngine{

	protected static final Logger logger = LoggerFactory.getLogger("SyncEngine");
	protected int state;
	protected SyncProcessor mProcessor;
	protected SyncMode mMode;
	
	public BaseSyncEngine(SyncProcessor proc, SyncMode mode) {
		this.mProcessor = proc;
		this.mMode = mode;
	}
	
	public void setEngineState(int state) {
		this.state = state;
	}
	
	public int getEngineState() {
		return this.state;
	}
	
	/** 遇到错误的状态 */
	protected void illegalState(String type) {
		logger.error("Error happend: receive {} in state {}.", type, state);
		mProcessor.exit();
	}
}
