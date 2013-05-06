package com.mcs.client.sync;

import java.util.ArrayList;

public interface SyncSource {
	
	/** 返回所有的Item */
	ArrayList<SyncItem> getAllItems();
	
	/** 
	 * 返回所有timeline之后改变过的Item，这里的Item应该只有Key和State以及TimeBasedSerialId。
	 * 并且changeList应该经过优化，即每个key只能出现一次。只存在最后一次修改。
	 */
	ArrayList<SyncItem> getOptimizedChangedItemState(long timeline);
	
	/** 返回timeline之后所有改变过的item，不经过optimize */
	ArrayList<SyncItem> getChangedItemState(long timeline);
	
	/** 根据给定的空item，查询数据库，将SyncItem填充成完整的数据，但是删除的数据不填充 */
	void fillItems(ArrayList<SyncItem> items);
	
	/** 删除列表中的Item */
	void deleteItems(ArrayList<SyncItem> items);
	
	/** 加入新的Item */
	void addItems(ArrayList<SyncItem> items);
	
	/** 更新列表中的Item */
	void updateItems(ArrayList<SyncItem> items);
	
	/** 保存上一次同步完成时的TimeAnchor */
	void saveTimeAnchor(TimeAnchor anchor);
	
	/** 载入最后一次同步的TimeAnchor */
	TimeAnchor loadTimeAnchor();
}
