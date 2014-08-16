package object;

//: object/HelloDate.java
import java.util.*;

/** The first Thinking in Java example program.
 * Displays a string and today's date.
 * @author Bruce Eckel
 * @author www.MindView.net
 * @version 4.0
*/
public class HelloDate {
  /** Entry point to class & application.
   * @param args array of string arguments
   * @throws exceptions No exceptions thrown
  */
  public static void main(String[] args) {
    System.out.println("Hello, it's: ");
    System.out.println(new Date());
    A a = new A();
    A b = a;
    test(a);
    a = null;
    System.out.println(b);
  }
  public static void test(A a){
	  a.setA(100);
  }
 public static class A{
	  int a;

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}
	  
  }
} /* Output: (55% match)
Hello, it's:
Wed Oct 05 14:39:36 MDT 2005
*///:~
