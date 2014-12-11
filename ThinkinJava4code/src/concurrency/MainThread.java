package concurrency;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

//: concurrency/MainThread.java

public class MainThread {

	static class Test1 extends Thread {
		BlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<Object>();

		@Override
		public void run() {
			// TODO Auto-generated method stub

			try {
				while (!isInterrupted()) {
					Object object = blockingQueue.take();
//					synchronized (this) {
//						wait();
//					}
//					sleep(100);
//					System.in.read();
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public static void main(String[] args) throws InterruptedException {
		LiftOff launch = new LiftOff();
//		launch.run();
		Test1 test1 = new Test1();
		System.out.println(test1.getState());
		test1.start();
		while (true) {
			System.out.println(test1.getState() + " " + test1.isAlive());
			TimeUnit.MILLISECONDS.sleep(500);
		}
		
	}
} /*
 * Output: #0(9), #0(8), #0(7), #0(6), #0(5), #0(4), #0(3), #0(2), #0(1),
 * #0(Liftoff!),
 */// :~
