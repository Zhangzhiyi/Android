package com.mcs.client.sync;

import static com.mcs.client.sync.SyncCode.ACTION_DATA_COMPLETE;
import static com.mcs.client.sync.SyncCode.DIR_BOTH;
import static com.mcs.client.sync.SyncCode.DIR_CLIENT;
import static com.mcs.client.sync.SyncCode.DIR_SERVER;
import static com.mcs.client.sync.SyncCode.MSG_CHANGE_LIST;
import static com.mcs.client.sync.SyncCode.MSG_CONFIRM;
import static com.mcs.client.sync.SyncCode.MSG_DATA;
import static com.mcs.client.sync.SyncCode.MSG_DATA_REQUEST;
import static com.mcs.client.sync.SyncCode.PKG_SYNCITEM;
import static com.mcs.client.sync.SyncCode.PKG_TIMEANCHOR;

import java.util.ArrayList;

import com.mcs.framework.message.ContentReader;
import com.mcs.framework.message.ContentWriter;

public class FastSync extends BaseSyncEngine{
	
	static final int WAIT_CONFIRM 		= 0;
	static final int WAIT_DATA			= 1;
	static final int WAIT_DATA_REQUEST 	= 2;
	
	public FastSync(SyncProcessor proc, SyncMode mode) {
		super(proc, mode);
	}

	@Override
	public void start() {
		switch(mMode.getSyncDirection()) {
		case DIR_CLIENT:
			// 由client负责发送数据，所以就直接发送改变数据给server，然后等待server发过来的confirm，然后结束
			logger.debug("Start fast sync from client side.");
			ArrayList<SyncItem> items = mProcessor.prepareChangeList(mMode);
			mProcessor.fillItems(items);
			ContentWriter cw = new ContentWriter(MSG_DATA, ACTION_DATA_COMPLETE);
			for(SyncItem s: items) {
				cw.putPkg(PKG_SYNCITEM, s.pack());
			}
			logger.debug("Send data to server.");
			mProcessor.send(cw.getContent());
			setEngineState(WAIT_CONFIRM);
			logger.debug("Enter wait confirm state.");
			break;
			
		case DIR_SERVER:
			// 如果由server提供数据，那就直接进入等待数据的状态
			logger.debug("Start fast sync from server side.");
			setEngineState(WAIT_DATA);
			logger.debug("Enter wait data state.");
			break;
			
		case DIR_BOTH:
			// 如果是双向同步，则发送changelist，由server负责对比和精简
			logger.debug("Start fast sync from both side.");
			ArrayList<SyncItem> clientChanges = mProcessor.prepareChangeList(mMode);
			ContentWriter changeCw = new ContentWriter(MSG_CHANGE_LIST, null);
			for(SyncItem s: clientChanges) {
				changeCw.putPkg(PKG_SYNCITEM, s.pack());
			}
			logger.debug("Send changelist to server.");
			mProcessor.send(changeCw.getContent());
			setEngineState(WAIT_DATA_REQUEST);
			logger.debug("Enter wait data request state.");
			break;
		}
	}
	
	@Override
	public boolean process(ContentReader cr) {
		switch(state) {
		case WAIT_CONFIRM:
			if(cr.getType().equals(MSG_CONFIRM)) {
				// 同步完成
				logger.debug("Receive confirm.");
				for(int i = 0; i < cr.getCount(); i++) {
					if(cr.getPkgType(i).equals(PKG_TIMEANCHOR)) {
						TimeAnchor anchor = new TimeAnchor();
						anchor.unpack(cr.getPkgValue(i));
						mProcessor.saveTimeAnchor(anchor);
						break;
					}
				}
				logger.debug("Fast sync completed.");
				return true;
			}else {
				illegalState(cr.getType());
			}
			break;
			
		case WAIT_DATA:
			if(cr.getType().equals(MSG_DATA)) {
				logger.debug("Receive data.");
				mProcessor.applyChange(cr);
				if(cr.getAction().equals(ACTION_DATA_COMPLETE)) {
					logger.debug("Data complete.");
					// 在非DIR_BOTH模式下，需要发送一个confirm，然后等待服务器最终待时间戳的confirm
					if(mMode.getSyncDirection() != DIR_BOTH) {
						mProcessor.sendConfirm();
					}
					setEngineState(WAIT_CONFIRM);
					logger.debug("Enter wait confirm state.");
				}
			}else {
				illegalState(cr.getType());
			}
			break;
		
		case WAIT_DATA_REQUEST:
			if(cr.getType().equals(MSG_DATA_REQUEST)){
				logger.debug("Receive data request.");
				// 解析dataRequest
				ArrayList<SyncItem> reqList = new ArrayList<SyncItem>();
				for(int i = 0; i < cr.getCount(); i++) {
					SyncItem item = new SyncItem();
					item.unpack(cr.getPkgValue(i));
					reqList.add(item);
				}
				
				// 填充数据，然后发送给server
				mProcessor.fillItems(reqList);
				
				ContentWriter dataCw = new ContentWriter(MSG_DATA, ACTION_DATA_COMPLETE);
				for(SyncItem s: reqList) {
					dataCw.putPkg(PKG_SYNCITEM, s.pack());
				}
				
				logger.debug("Send data to server.");
				mProcessor.send(dataCw.getContent());
				setEngineState(WAIT_DATA);
				logger.debug("Enter wait data state.");
			}else {
				illegalState(cr.getType());
			}
			break;
		}
		return false;
	}
}
