package com.mcs.client.android.service;
import com.mcs.client.sync.SyncItem;

interface IRemoteServiceCallback{

	void callBackValue(int value);
	
	void callBackSyncItem(int operation,inout List<SyncItem> items);
}