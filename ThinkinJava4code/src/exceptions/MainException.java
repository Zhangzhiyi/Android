package exceptions;

//: exceptions/MainException.java
import java.io.*;

public class MainException {
	public static void a() throws Exception{
		throw new Exception("123123");
	}
  // Pass all exceptions to the console:
  public static void main(String[] args) throws Exception {
	  
	  try {
		 new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					a();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
    // Open the file:
    FileInputStream file =
      new FileInputStream("MainException.java");
    // Use the file ...
    // Close the file:
    file.close();
  }
} ///:~
