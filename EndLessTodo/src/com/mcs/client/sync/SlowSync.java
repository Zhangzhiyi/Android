package com.mcs.client.sync;

import static com.mcs.client.sync.SyncCode.ACTION_DATA_COMPLETE;
import static com.mcs.client.sync.SyncCode.DIR_CLIENT;
import static com.mcs.client.sync.SyncCode.DIR_SERVER;
import static com.mcs.client.sync.SyncCode.MSG_CONFIRM;
import static com.mcs.client.sync.SyncCode.MSG_DATA;
import static com.mcs.client.sync.SyncCode.PKG_SYNCITEM;
import static com.mcs.client.sync.SyncCode.PKG_TIMEANCHOR;

import java.util.ArrayList;

import com.mcs.framework.message.ContentReader;
import com.mcs.framework.message.ContentWriter;

public class SlowSync extends BaseSyncEngine{
	
	static final int WAIT_CONFIRM 	= 0;
	static final int WAIT_DATA		= 1;
	
	public SlowSync(SyncProcessor proc, SyncMode mode) {
		super(proc, mode);
	}
	
	@Override
	public void start() {
		// 如果是服务器发送的模式，就直接发送数据给客户端，设置模式为等待confirm
		if(mMode.getSyncDirection() == DIR_CLIENT) {
			logger.debug("Start slow sync from client side.");
			/*ArrayList<SyncItem> items = mProcessor.prepareChangeList(mMode);
			mProcessor.fillItems(items);*/
			ArrayList<SyncItem> items = mProcessor.getAllItems();
			ContentWriter cw = new ContentWriter(MSG_DATA, ACTION_DATA_COMPLETE);
			for(SyncItem s: items) {
				cw.putPkg(PKG_SYNCITEM, s.pack());
			}
			logger.debug("Send data to server.");
			mProcessor.send(cw.getContent());
			setEngineState(WAIT_CONFIRM);
			logger.debug("Enter wait confirm state.");
			
		}else if(mMode.getSyncDirection() == DIR_SERVER) {
			logger.debug("Start slow sync from server side.");
			setEngineState(WAIT_DATA);
			logger.debug("Enter wait data state.");
		}
	}

	@Override
	public boolean process(ContentReader cr) {
		switch(state) {
		case WAIT_CONFIRM:
			if(cr.getType().equals(MSG_CONFIRM)) {
				logger.debug("Receive confirm.");
				for(int i = 0; i < cr.getCount(); i++) {
					if(cr.getPkgType(i).equals(PKG_TIMEANCHOR)) {
						TimeAnchor anchor = new TimeAnchor();
						anchor.unpack(cr.getPkgValue(i));
						mProcessor.saveTimeAnchor(anchor);
						break;
					}
				}
				logger.debug("Slow sync from client side completed.");
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
					// 收到服务器的数据以后，发送confirm，并等待服务器带同步完成时间的确认
					mProcessor.sendConfirm();
					logger.debug("Slow sync from server side completed.");
					setEngineState(WAIT_CONFIRM);
					logger.debug("Enter wait confirm state.");
				}
			}else {
				illegalState(cr.getType());
			}
			break;
		}
		return false;
	}

}
