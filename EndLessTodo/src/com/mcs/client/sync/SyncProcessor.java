package com.mcs.client.sync;

import static com.mcs.client.sync.ItemState.DEL;
import static com.mcs.client.sync.ItemState.NEW;
import static com.mcs.client.sync.ItemState.UPD;
import static com.mcs.client.sync.SyncCode.INVALID_TIMEANCHOR;
import static com.mcs.client.sync.SyncCode.MSG_CONFIRM;
import static com.mcs.client.sync.SyncCode.MSG_MODE;
import static com.mcs.client.sync.SyncCode.MSG_REQUEST;
import static com.mcs.client.sync.SyncCode.PKG_SYNCITEM;
import static com.mcs.client.sync.SyncCode.PKG_SYNCMODE;
import static com.mcs.client.sync.SyncCode.PKG_TIMEANCHOR;
import static com.mcs.client.sync.SyncCode.SYNC_FAST;
import static com.mcs.client.sync.SyncCode.SYNC_SLOW;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.mcs.client.android.Env;
import com.mcs.client.android.service.MCSService;
import com.mcs.client.mina.MinaClient;
import com.mcs.framework.message.ContentReader;
import com.mcs.framework.message.ContentWriter;
import com.mcs.framework.message.mina.Data;
import com.mcs.todo.db.DBAdapter;
import com.mcs.todo.db.DBConstant;

public class SyncProcessor implements Runnable{
	
	private static final Logger logger = LoggerFactory.getLogger("SyncProcessor");
	// Processor不同的状态
	public static final int STATE_WAIT_MODE	 	= 0;
	public static final int STATE_PROCESSING	= 1;
	
	/** 服务器的id */
	private String mServerId;
	/** 本Processor处理的client的id */
	private String mClientId;
	/** key是用来区分同时同步的不同数据的，比如同步的是联系人，或者同步的是待办，就靠这个区分 */
	private String mDataType;
	
	// 同步相关的变量
	private int mState = STATE_WAIT_MODE;
	/** 同步的模式 */
	private SyncMode mMode;
	/** 启动本Processor的Terminal */
	private SyncTer mTer;
	/** 本Processor对应的SyncSource */
	private SyncSource mSource;
	/** 等待时间，如果过期无反应则退出Processor */
	private long mTimeout = 30; // 30秒
	private MinaClient mMinaClient;
	
	/** SyncProcessor可以同时处理的消息数上限 */
	private static final int MSG_PROCESS_CAPACITY = 10;
	BlockingQueue<Data> mDataQueue = new ArrayBlockingQueue<Data>(MSG_PROCESS_CAPACITY);

	private SyncEngine mEngine;
	
	/**同步完成发送消息到属于SyncActivity的消息队列**/
	public static final int COMPLETE_MSG = 1;
	
	
	/**
	 * @param clientId 同步对象的id
	 * @param dataType Data的key，也是同步的类型。比如同步Todo，则key就是Todo；同步联系人，key就是Contact
	 * @param ter 同步模块
	 * @param handler 
	 * @param position 指示自身在同步中的身份，发起者为1，接受者为2
	 */
	public SyncProcessor(String serverId, String clientId, String dataType, SyncTer ter) {
		
		Log.i("1111111111111", "1111111111111111111");
		this.mServerId	  = serverId;
		this.mClientId 	  = clientId;
		this.mDataType 	  = dataType;
		this.mTer 	  	  = ter;
		this.mSource	  = SyncSourceFactory.getSyncSource(clientId, dataType);
		this.mMinaClient  = Env.getInstance().getMinaClient();
		
		
		// TODO 设置同步的timeout
		setTimeOut(30); // 默认也是30秒 
	}
	
	/** 处理同步的数据 */
	public void process(Data msg) {
		try {
			mDataQueue.offer(msg, mTimeout, TimeUnit.SECONDS);
			// mDataQueue.put(msg);
		} catch (InterruptedException e) {
			reset();
		}
	}
	
	/** 返回Processor当前的状态 */
	public int getProcessorState() {
		return this.mState;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	/**
	 * 载入TimeAnchor
	 * 返回（-1,-1）则是原来TimeAnchor不存在，或者载入失败了
	 */
	TimeAnchor loadTimeAnchor() {
		return mSource.loadTimeAnchor();
	}
	
	/** 保存TimeAnchor */
	void saveTimeAnchor(TimeAnchor anchor) {
		mSource.saveTimeAnchor(anchor);
	}
	
	/** 设置Processor的状态 */
	private void setProcessorState(int state) {
		logger.debug("SyncProcessor change to state: {}", state);
		this.mState = state;
	}
	
	/** 生成ChangeList，此时返回的Item作为对比之用是没有content的，所以必须经过fillItem操作填充value */
	ArrayList<SyncItem> prepareChangeList(SyncMode mode) {
		return mSource.getOptimizedChangedItemState(mode.getSyncTimeline());
	}
	
	/** 根据已有的items的key查询数据库，查到数据并填充SyncItem的content */
	void fillItems(ArrayList<SyncItem> items) {
		mSource.fillItems(items);
	}
		
	/** 根据changeList，过滤出里面需要请求数据的那些Item，主要有New和Update的项 */
	ArrayList<SyncItem> prepareRequestList(ArrayList<SyncItem> changeList) {
		ArrayList<SyncItem> req = new ArrayList<SyncItem>();
		for(SyncItem s: changeList) {
			if(s.getState() == NEW || s.getState() == UPD) {
				req.add(s);
			}
		}
		return req;
	}
	
	ArrayList<SyncItem> getAllItems() {
		return mSource.getAllItems();
	}
	
	/** 应用最终的改变项 */
	void applyChange(ContentReader cr) {
		logger.debug("Apply changes to database.");
		
		ArrayList<SyncItem> add = new ArrayList<SyncItem>();
		ArrayList<SyncItem> del = new ArrayList<SyncItem>();
		ArrayList<SyncItem> udp = new ArrayList<SyncItem>();
		
		for(int i = 0; i < cr.getCount(); i++) {
			if(cr.getPkgType(i).equals(PKG_SYNCITEM)) {
				
				SyncItem item = new SyncItem();
				item.unpack(cr.getPkgValue(i));
				
				switch(item.getState()) {
				case NEW: add.add(item); break;
				case DEL: del.add(item); break;
				case UPD: udp.add(item); break;
				}
			}
		}
		
		mSource.addItems(add);
		mSource.deleteItems(del);
		mSource.updateItems(udp);
	}
	
	/** 发送同步请求 */
	void sendRequest() {
		TimeAnchor anchor = loadTimeAnchor();
		ContentWriter cw = new ContentWriter(MSG_REQUEST, null);
		cw.putPkg(PKG_TIMEANCHOR, anchor.pack());
		logger.debug("Send sync request to server.");
		send(cw.getContent());
	}
	
	/** 发送带有时间的Confirm */
	void sendConfirm(long time) {
		ContentWriter cw = new ContentWriter(MSG_CONFIRM, null);
		// 附带confirm时的TimeAnchor，以便于统一服务器和客户端同步完成的时间
		TimeAnchor anchor = new TimeAnchor(time, INVALID_TIMEANCHOR);
		cw.putPkg(PKG_TIMEANCHOR, anchor.pack());
		send(cw.getContent());
		logger.debug("Send confirm with time: {}.", time);
	}
	
	void sendConfirm() {
		ContentWriter cw = new ContentWriter(MSG_CONFIRM, null);
		send(cw.getContent());
		logger.debug("Send confirm.");
	}
	
	/** 发送消息，这里设计成同步的，主要是为了每一个消息都能顺序发送 */
	synchronized void send(byte[] content) {
		mMinaClient.send(new Data(mDataType, content, mClientId, mServerId));
	}
	
	/** 退出Processor */
	public void exit() {
		done = true;
		// 从Processors表中移除自己
		mTer.remove(mClientId, mDataType);
	}
	
	/** 重置Processor */
	public void reset() {
		mState = STATE_WAIT_MODE;
		mDataQueue.clear();
	}
	
	/** 设置Processor的等待时间 */
	public void setTimeOut(long timeout) {
		this.mTimeout = timeout;
	}
	
	/** 开始同步过程 */
	public void startSync() {
		setProcessorState(STATE_WAIT_MODE);
		sendRequest();
	}
	
	/** 控制是否结束 */
	private boolean done = false;
	
	@Override
	public void run() {
		logger.debug("SyncProcessor: {} start to run.", mClientId);
		Data msg = null;
		try {
			while (!done) {
				// 取得消息，如果没有消息就会在这里等待，也有可能在这里抛出interrupted异常
				msg = mDataQueue.take();
				ContentReader cr = new ContentReader(msg.getContent());
				logger.debug("SyncProcessor process data: {}.", cr.getType());
				
				switch (mState) {
				case STATE_WAIT_MODE:
					// 接受到mode，根据同步的mode启动不同的syncEngine
					logger.debug("Process {}.", cr.getType());
					if (cr.getType().equals(MSG_MODE)) {
						// 解析mode
						for(int i = 0; i < cr.getCount(); i++) {
							if(cr.getPkgType(i).equals(PKG_SYNCMODE)){
								mMode = new SyncMode();
								mMode.unpack(cr.getPkgValue(i));
								break;
							}
						}
						
						if(mMode.getSyncType() == SYNC_SLOW) {
							mEngine = new SlowSync(this, mMode);
						}else if(mMode.getSyncType() == SYNC_FAST) {
							mEngine = new FastSync(this, mMode);
						}
						setProcessorState(STATE_PROCESSING);
						// Engine一定要start一下
						mEngine.start();
					}
					break;
					
				case STATE_PROCESSING:
					if(mEngine.process(cr)) {
						Message mes = new Message();
						mes.what = ItemState.CLEAR_CHANGElIST;						
						MCSService.mServiceHandler.sendMessage(mes);
						exit();
					}
					break;
				}
			}
		} catch (InterruptedException e) {
			logger.debug("Processor {} is interrupted.", mClientId);
			exit();
		}
		logger.debug("Exit processor {}.", mClientId);
	}
}
