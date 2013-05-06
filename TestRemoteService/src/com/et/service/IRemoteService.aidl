package com.et.service;
import com.et.service.IRemoteServiceCallback;
interface IRemoteService{

	void registerCallback(IRemoteServiceCallback cb);
	
	void unregisterCallback(IRemoteServiceCallback cb);
}