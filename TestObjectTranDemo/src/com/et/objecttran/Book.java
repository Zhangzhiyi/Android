package com.et.objecttran;

import android.os.Parcel;
import android.os.Parcelable;
public class Book implements Parcelable {
	private String bookName;
	private String author;
	private int publishTime;
	private static byte[] content;
	
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(int publishTime) {
		this.publishTime = publishTime;
	}
	
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}

	public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
		public Book createFromParcel(Parcel source) {
			Book mBook = new Book();
			mBook.bookName = source.readString();
			mBook.author = source.readString();
			mBook.publishTime = source.readInt();
			mBook.content = source.createByteArray();
			return mBook;
		}
		public Book[] newArray(int size) {
			return new Book[size];
		}
	};
	
	public int describeContents() {
		return 0;
	}
	public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(bookName);
		parcel.writeString(author);
		parcel.writeInt(publishTime);
		parcel.writeByteArray(content);
	}
}