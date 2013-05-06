package com.mcs.client.android.service;
import com.mcs.client.android.service.IRemoteServiceCallback;
interface IRemoteService{

	void registerCallback(IRemoteServiceCallback cb);
	
	void unregisterCallback(IRemoteServiceCallback cb);
}