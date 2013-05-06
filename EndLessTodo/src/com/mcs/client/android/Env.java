package com.mcs.client.android;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mcs.client.mina.MinaClient;
import com.mcs.client.sync.SyncTer;


/**
 * 服务器的环境对象，管理全局的对象
 * @author dbds
 */
public class Env {
	private static final Logger logger = LoggerFactory.getLogger("Env");
	// Lazy initialization holder idiom.
	// 利用Jvm的lazy class load，以及static域初始化会加锁的机制
	// 保证线程安全
	private static class EnvHolder {
		public static Env env = new Env();
	}
	
	/** 返回系统的环境对象*/
	public static Env getInstance() {
		// 此处不能用Lazy Initialization
		return EnvHolder.env;
	}
	
	private Env() {
		loadPreference();
		InetAddress serverAddress = null;
		try {
			serverAddress = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		minaClient = new MinaClient(serverAddress, port);
		logger.debug("Create MinaClient: {}", minaClient);
		syncTer = new SyncTer();
		
	}
	
	private String host = "10.0.2.2";
	private int port = 50522;
	
	private void loadPreference() {
		// FIXME 载入设置
	}

	private SyncTer syncTer;
	
	public SyncTer getSyncTer() {
		return syncTer;
	}
	
	private MinaClient minaClient;
	
	public MinaClient getMinaClient() {
		return minaClient;
	}

	private volatile String clientId = "TestClientId";
	/** 返回客户端的id */
	public String getClientId() {
		// FIXME 记得载入客户端的id啊
		return clientId;
	}
	
	public void setClientId(String clientId) {
		logger.info("Set client id as: {}.", clientId);
		this.clientId = clientId;
	}
}
