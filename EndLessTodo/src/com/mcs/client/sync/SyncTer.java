package com.mcs.client.sync;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.os.Handler;

import com.mcs.framework.message.mina.Data;
import com.mcs.framework.terminal.Terminal;
import com.mcs.framework.terminal.TerminalWriteFailedException;

public class SyncTer implements Terminal{
	private static final Logger logger = LoggerFactory.getLogger("SyncTer");

	/** 同步模块的id */
	public static final String SERVER_ID = "01Sync";
	/**线程池**/
	private ExecutorService executor = Executors.newCachedThreadPool();
	private HashMap<String, SyncProcessor> processors = new HashMap<String, SyncProcessor>();
	
	@Override
	public void receiveMessage(Data msg) throws TerminalWriteFailedException {
		// logger.debug("SyncTer receive {} from {}", msg.getKey(), msg.getSrcId());
		// 在客户端这边，同时同步的只可能是不同的dataType，所以当然就是以dataType为Map的Key了
		String key = msg.getKey();
		SyncProcessor p = processors.get(key);
		if(null != p) {
			p.process(msg);
		}
	}
	
	/** 开始一个新dataType的同步过程 
	 * @param handler */
	public void fireSync(String clientId, String dataType) {
		logger.info("Client {} start a new synchronization.", clientId);
		SyncProcessor processor = new SyncProcessor(SERVER_ID, clientId, dataType, this);
		processors.put(dataType, processor);
		executor.execute(processor);
		processor.startSync();
	}
	
	void remove(String id, String key) {
		logger.debug("Remove {}{} from SyncTer.", id, key);
		processors.remove(id + key);
	}
	
	/** 重置SyncTer */
	public void reset() {
		logger.info("Reset sync terminal.");
		processors.clear();
	}
	
}
