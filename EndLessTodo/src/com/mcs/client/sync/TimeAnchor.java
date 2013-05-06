package com.mcs.client.sync;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.mcs.framework.message.Packer;

public class TimeAnchor implements Packer{
	
	private long last = SyncCode.INVALID_TIMEANCHOR;
	private long next = SyncCode.INVALID_TIMEANCHOR;
	
	public TimeAnchor() {}
	
	public TimeAnchor(long last, long next) {
		this.last = last;
		this.next = next;
	}
	
	public long getLast() {
		return last;
	}
	public void setLast(long last) {
		this.last = last;
	}
	public long getNext() {
		return next;
	}
	public void setNext(long next) {
		this.next = next;
	}

	@Override
	public byte[] pack() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		try {
			dos.writeLong(last);
			dos.writeLong(next);
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
			last = input.readLong();
			next = input.readLong();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
