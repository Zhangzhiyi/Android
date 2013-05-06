package concurrency;

//: concurrency/SynchronizedEvenGenerator.java
// Simplifying mutexes with the synchronized keyword.
// {RunByHand}

public class
SynchronizedEvenGenerator extends IntGenerator {
  private int currentEvenValue = 0;
  public synchronized int next() {
    ++currentEvenValue;
    System.out.println(Thread.currentThread().toString() + "currentEvenValue:" + currentEvenValue);
    //貌似调用Thread.yield();没有释放掉获得的锁
    Thread.yield(); // Cause failure faster
    ++currentEvenValue;
    System.out.println(Thread.currentThread().toString() + "currentEvenValue:" + currentEvenValue);
    return currentEvenValue;
  }
  public static void main(String[] args) {
    EvenChecker.test(new SynchronizedEvenGenerator());
  }
} ///:~
