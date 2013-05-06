package com.mcs.client.sync;


public class SyncSourceFactory {
	public static SyncSource getSyncSource(String id, String dataType) {
		if(dataType.equals(DataType.SYNC_TODO))
			return new TodoSyncSource(id,dataType);
		else
			return new GenericSyncSource(id, dataType);
	}
}
