package com.mcs.client.android.service;
import com.mcs.client.sync.SyncItem;
interface IMCSTranValue{
	void transValue(in List<SyncItem> items, int operation);
}