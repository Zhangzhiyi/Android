public class TestFinal {
	/***
	 * Effective Java 第13条：使类和成员的可访问性最小化
	 * 
	 * 长度非零的数组总是可变的，所以，类具有公有的静态final数组域，或者返回这种域的访问方法，这几乎总是错误的。如果类具有这样的域或者访问方法，
	 * 客户端能够修改数组中的内容，这是安全漏洞的一个常见根源。
	 */
	public static final int[] intVariable = { 1, 2, 3, 4 };
	public static final int[] intVariable2 = { 1, 2, 3, 4 };

	public static void main(String[] args) {
		intVariable[1] = 10; // 数组里面的项还是可以修改
		
		
		
		//当修饰对象引用时，我错误的认为变量指向的对象是不可变的，然而对象是可变的，不可变只是“变量对对象的引用“。就好象C++的常量指针和指针常量
		final B b = new B();
		b.setA(new A());
		b.setNums(100);
	}

	public static class A {

	}

	public static class B {
		A a;
		int nums = 10;
		public A getA() {
			return a;
		}

		public void setA(A a) {
			this.a = a;
		}

		public int getNums() {
			return nums;
		}

		public void setNums(int nums) {
			this.nums = nums;
		}
		
	}
}
