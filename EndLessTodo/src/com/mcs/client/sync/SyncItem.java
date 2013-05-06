package com.mcs.client.sync;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import android.os.Parcel;
import android.os.Parcelable;

import com.mcs.framework.message.Packer;
import com.mcs.framework.utils.Bytes;

import static com.mcs.client.sync.ItemState.*;

public class SyncItem implements Packer ,Parcelable{
	
    /** The key of this item.  */
    private String key = "";
    
    /** The mime-type of this item. Default is text/plain.  */
    private String type = "Null";
    
    /** The state of this item ([N]ew, [U]pdated, [D]eleted) */
    private char state = INVALID;
    
    /** The time of last operation. */
    private long TimeBasedSerialId = -1;

	/** The content of this item */
    private byte[] content = null;
    
    public SyncItem() {}
    
    /**
     * Basic constructor. Only the key is required, the others
     * are set to a default and can be set later.
     */
    public SyncItem(String key) {
        this(key, "text/plain", null, NEW, System.currentTimeMillis());
    }
    
    /**
     * Full contructor. All the item's fields are passed by the caller.
     */
    public SyncItem(String key, String type, byte[] content, char state, long TimeBasedSerialId) {
        this.key = key;
        this.type = type;
        this.state = state;
        this.TimeBasedSerialId = TimeBasedSerialId;
        this.content = content;
    }

    /**
     * Copy constructor. The item is created using the values from another
     * one.
     */
    public SyncItem(SyncItem that) {
        this.key = that.key;
        this.type = that.type;
        this.state = that.state;
        this.TimeBasedSerialId = that.TimeBasedSerialId;
        this.content = that.content;
    }

    //----------------------------------------------------------- Public methods
    
    /**
     * Get the current key
     */
    public String getKey() {
        return this.key;
    }
    
    /**
     * Set the current key
     */
    public void setKey(String key) {
        this.key = key;
    }
    
    /**
     * Get the item type
     */
    public String getType() {
        return this.type;
    }
    
    /**
     * Set the item type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * Get the item state
     */
    public char getState() {
        return this.state;
    }
    
    /**
     * Set the item state
     */
    public void setState(char state) {
        this.state = state;
    }

    public long getLastOperationTime() {
		return TimeBasedSerialId;
	}

	public void setLastOperationTime(long TimeBasedSerialId) {
		this.TimeBasedSerialId = TimeBasedSerialId;
	}
	
    /**
     * Get the content of this item
     */
    public byte[] getContent() {
        return this.content;
    }
    
    /**
     * Set the content of this item
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

	@Override
	public byte[] pack() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(baos);
		byte[] value = null;
		try {
			value = Bytes.fromString(key);
			if(null != value) {
				dos.writeInt(value.length);
				dos.write(value, 0, value.length);
			}else dos.writeInt(0);
			value = Bytes.fromString(type);
			if(null != value) {
				dos.writeInt(value.length);
				dos.write(value, 0, value.length);
			}else dos.writeInt(0);
			dos.writeChar(state);
			value = Bytes.fromLong(TimeBasedSerialId);
			if(null != value) {
				dos.writeInt(value.length);
				dos.write(value, 0, value.length);
			}else dos.writeInt(0);
			if(null != content) {
				dos.writeInt(content.length);
				dos.write(content, 0, content.length);
			}else dos.writeInt(0);
		} catch (IOException e) {
			// TODO 解码出现错误应该做些什么？
			e.printStackTrace();
		}
		return baos.toByteArray();
	}

	@Override
	public void unpack(byte[] value) {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(value));
		int length = 0;
		byte[] body = null;
		try {
			length = dis.readInt();
			if(length != 0) {
				body = new byte[length];
				dis.read(body, 0, length);
				key = new String(body);
			}
			length = dis.readInt();
			if(length != 0) {
				body = new byte[length];
				dis.read(body, 0, length);
				type = new String(body);
			}
			state = dis.readChar();
			length = dis.readInt();
			if(length != 0) {
				body = new byte[length];
				dis.read(body, 0, length);
				TimeBasedSerialId = Bytes.toLong(body);
			}
			length = dis.readInt();
			if(length != 0) {
				content = new byte[length];
				dis.read(content, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static final Parcelable.Creator<SyncItem> CREATOR = new Parcelable.Creator<SyncItem>(){

		@Override
		public SyncItem createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			SyncItem mSyncItem = new SyncItem();
			mSyncItem.key = in.readString();
			mSyncItem.type = in.readString();
			mSyncItem.state = (char) in.readInt();
			mSyncItem.TimeBasedSerialId = in.readLong();
			mSyncItem.content = in.createByteArray();
			return mSyncItem;
		}

		@Override
		public SyncItem[] newArray(int size) {
			// TODO Auto-generated method stub
			return new SyncItem[size];
		}
		
	};
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(key);
		dest.writeString(type);
		dest.writeInt(state);
		dest.writeLong(TimeBasedSerialId);
		dest.writeByteArray(content);
	}
}
