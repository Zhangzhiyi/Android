package com.mcs.client.sync;

import com.mcs.framework.message.ContentReader;

public interface SyncEngine {
	void start();
	/** 如果同步完成则返回true，否则返回false */
	boolean process(ContentReader cr);
	void setEngineState(int state);
	int getEngineState();
}
