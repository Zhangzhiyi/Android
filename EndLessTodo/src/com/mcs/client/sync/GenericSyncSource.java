package com.mcs.client.sync;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericSyncSource implements SyncSource{
	private static final Logger logger = LoggerFactory.getLogger("SyncSource");
	
	public GenericSyncSource(String id, String dataType) {
		
	}

	@Override
	public ArrayList<SyncItem> getAllItems() {
		logger.info("Get all item from source.");
		ArrayList<SyncItem> all = new ArrayList<SyncItem>();
		// TODO Auto-generated method stub
		return all;
	}
	@Override
	public ArrayList<SyncItem> getOptimizedChangedItemState(long timeline) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public ArrayList<SyncItem> getChangedItemState(long timeline) {
		logger.info("Prepare changelist.");
		ArrayList<SyncItem> changes = new ArrayList<SyncItem>();
		// TODO Auto-generated method stub
		logger.info("Find {} changes.", changes.size());
		return changes;
	}

	@Override
	public void fillItems(ArrayList<SyncItem> items) {
		logger.info("Fill items: {}.", items);
	}

	@Override
	public void deleteItems(ArrayList<SyncItem> items) {
		logger.info("Delete items: {}.", items);
	}

	@Override
	public void addItems(ArrayList<SyncItem> items) {
		logger.info("Add items: {}.", items);
	}

	@Override
	public void updateItems(ArrayList<SyncItem> items) {
		logger.info("Update items: {}.", items);
	}

	@Override
	public TimeAnchor loadTimeAnchor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveTimeAnchor(TimeAnchor anchor) {
		// TODO Auto-generated method stub
		
	}

	

}
