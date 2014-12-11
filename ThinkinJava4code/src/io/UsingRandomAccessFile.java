package io;

//: io/UsingRandomAccessFile.java
import java.io.*;
import java.nio.IntBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class UsingRandomAccessFile {
	static String file = "rtest.dat";

	static void display() throws IOException {
		RandomAccessFile rf = new RandomAccessFile(file, "r");
		for (int i = 0; i < 7; i++)
			System.out.println("Value " + i + ": " + rf.readDouble());
		System.out.println(rf.readUTF());
		rf.close();
	}

	public static void main(String[] args) throws IOException {
		RandomAccessFile rf = new RandomAccessFile(file, "rw");
		for (int i = 0; i < 7; i++)
			rf.writeDouble(i * 1.414);
		rf.writeUTF("The end of the file");
		rf.close();
		display();
		rf = new RandomAccessFile(file, "rw");
		rf.seek(5 * 8);
		rf.writeDouble(47.0001);
		rf.close();
		display();
		
		rf = new RandomAccessFile("temp.tmp", "rw");
//		rf.setLength(0x8FFFFFF);
		FileChannel fc =rf.getChannel();
//		IntBuffer ib = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size()).asIntBuffer();
//		for (int i = 0; i < ib.capacity(); i++)
//			ib.put(i);
//		fc.close();
		
		MappedByteBuffer map = fc.map(FileChannel.MapMode.READ_WRITE, 0, 4);
		map.putInt(1);
		fc.close();
	}
} /*
 * Output: Value 0: 0.0 Value 1: 1.414 Value 2: 2.828 Value 3: 4.242 Value 4:
 * 5.656 Value 5: 7.069999999999999 Value 6: 8.484 The end of the file Value 0:
 * 0.0 Value 1: 1.414 Value 2: 2.828 Value 3: 4.242 Value 4: 5.656 Value 5:
 * 47.0001 Value 6: 8.484 The end of the file
 */// :~
