package concurrency;

//: concurrency/FixedThreadPool.java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedThreadPool {
	public static void main(String[] args) {
		// Constructor argument is number of threads:
		ExecutorService exec = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++)
			exec.execute(new LiftOff());
		exec.shutdown();
		
		int CPU_COUNT = Runtime.getRuntime().availableProcessors();
		int CORE_POOL_SIZE = CPU_COUNT + 1;
		int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
		int KEEP_ALIVE = 1;
//		MAXIMUM_POOL_SIZE = 128;
		ThreadFactory sThreadFactory = new ThreadFactory() {
			private final AtomicInteger mCount = new AtomicInteger(1);

			public Thread newThread(Runnable r) {
				return new Thread(r, "AsyncTask #" + mCount.getAndIncrement());
			}
		};
		BlockingQueue<Runnable> sPoolWorkQueue = new ArrayBlockingQueue<Runnable>(10);
		Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

		for (int i = 0; i < 30; i++) {
			THREAD_POOL_EXECUTOR.execute(new Runnable() {

				public void run() {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(Thread.currentThread().getName() + " finish");
				}
			});
		}

	}
} /*
 * Output: (Sample) #0(9), #0(8), #1(9), #2(9), #3(9), #4(9), #0(7), #1(8),
 * #2(8), #3(8), #4(8), #0(6), #1(7), #2(7), #3(7), #4(7), #0(5), #1(6), #2(6),
 * #3(6), #4(6), #0(4), #1(5), #2(5), #3(5), #4(5), #0(3), #1(4), #2(4), #3(4),
 * #4(4), #0(2), #1(3), #2(3), #3(3), #4(3), #0(1), #1(2), #2(2), #3(2), #4(2),
 * #0(Liftoff!), #1(1), #2(1), #3(1), #4(1), #1(Liftoff!), #2(Liftoff!),
 * #3(Liftoff!), #4(Liftoff!),
 */// :~
