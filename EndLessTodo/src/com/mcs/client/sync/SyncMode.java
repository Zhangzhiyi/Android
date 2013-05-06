package com.mcs.client.sync;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mcs.framework.message.Packer;

public class SyncMode implements Packer{
	
	private int type;
	private int direction;
	private long last;
	
	public SyncMode(){} 
	
	public SyncMode(int type, int direction, long last) {
		this.type = type;
		this.direction = direction;
		this.last = last;
	}
	
	/** 返回同步的模式，FAST 或者 SLOW */
	public int getSyncType() {
		return type;
	}
	
	/** 返回同步的方向 */
	public int getSyncDirection() {
		return direction;
	}
	
	/** 返回同步check changelog的起始时间 */
	public long getSyncTimeline() {
		return last;
	}

	@Override
	public byte[] pack() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeInt(type);
			dos.writeInt(direction);
			dos.writeLong(last);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	public void unpack(byte[] value) {
		if(value == null) return;
		DataInputStream input = new DataInputStream(new ByteArrayInputStream(value));
		try {
			type = input.readInt();
			direction = input.readInt();
			last = input.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
