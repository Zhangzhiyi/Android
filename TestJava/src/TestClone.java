
public class TestClone {

	/**
	 * @param args
	 * @throws CloneNotSupportedException 
	 */
	public static void main(String[] args) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		CloneFooA fooA1 = new CloneFooA("FooA", 100);
		/**这样赋值是指向同一块内存的**/
		CloneFooA fooA2 = fooA1;
		 if (fooA1 == fooA2) 
			 System.out.println("比较fooA1和fooA2内存地址:相等!");
	     else 
	    	 System.out.println("比较fooA1和fooA2内存地址:不相等!");
		/**fooA2修改了fooA1也会同步修改**/
		fooA2.setAge(101);
		System.out.println("fooA2:agee = " + fooA2.getAge());
		System.out.println("fooA1:agee = " + fooA1.getAge());
		
		/**克隆fooA1,相当于重新申请一块内存**/
		System.out.println("------克隆对象fooA1--------");
		CloneFooA fooA3 = (CloneFooA) fooA1.clone();
		/**fooA3修改了fooA1不会同步修改**/
		fooA3.setAge(102);
		System.out.println("fooA3:" + fooA3.toString());
		System.out.println("fooA1:" + fooA1.toString());
		if (fooA1 == fooA3) 
			 System.out.println("比较fooA1和fooA3内存地址:相等!");
	     else 
	    	 System.out.println("比较fooA1和fooA3内存地址:不相等!");
		System.out.println("------克隆对象fooB1--------");
		CloneFooB fooB1 = new CloneFooB(fooA1, "FooB1");
		CloneFooB fooB2 = (CloneFooB) fooB1.clone();
		if (fooB1 == fooB2) 
			 System.out.println("比较fooB1和fooB2内存地址:相等!");
	     else 
	    	 System.out.println("比较fooB1和fooB2内存地址:不相等!");
		
		/**---注意浅复制和深复制----**/
		if (fooB1.getFooA() == fooB2.getFooA()) 
			System.out.println("比较fooB1和fooB2对象的fooA1内存地址:相等!");
		else
			System.out.println("比较fooB1和fooB2对象的fooA1内存地址:不相等!");
		
		System.out.println(fooB1.toString());
		System.out.println(fooB2.toString());
		System.out.println("修改fooB1里面fooA的值");
		fooB1.getFooA().setAge(1000);
		System.out.println(fooB1.toString());
		System.out.println(fooB2.toString());
		
		/**注意：无论浅复制还是深复制,String内存地址都是相等的,
		 * 因为String没有实现Cloneable接口,而且String是不可变的，一修改内存地址就改变了**/
		if (fooB1.getName() == fooB2.getName()) 
			System.out.println("比较fooB1和fooB2对象的String内存地址:相等!");
		else
			System.out.println("比较fooB1和fooB2对象的String内存地址:不相等!");
		

	}
	
	public static class CloneFooA implements Cloneable{
		private String name;
		private int age;
		public CloneFooA(String name, int age) {
			// TODO Auto-generated constructor stub
			this.name = name;
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "name = " + name + " " + "age = " + age;
		}
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			return super.clone();
		}
	}
	public static class CloneFooB implements Cloneable{
		CloneFooA fooA;
		String name;
		public CloneFooB(CloneFooA fooA, String name) {
			// TODO Auto-generated constructor stub
			this.fooA = fooA;
			this.name = name;
		}
		public CloneFooA getFooA() {
			return fooA;
		}
		public void setFooA(CloneFooA fooA) {
			this.fooA = fooA;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "name = " + name + " | " + "fooA:" + fooA.toString();
		}
		/**对象的浅复制,对象中的引用数据类型不调用clone方法,所以它们的内存地址还是一样的**/
//		@Override
//		protected Object clone() throws CloneNotSupportedException {
//			// TODO Auto-generated method stub
//			return super.clone();
//		}
		/**对象的深复制，对象中的引用数据类型不调用clone方法,所以它们的内存地址不一样的**/
		@Override
		protected Object clone() throws CloneNotSupportedException {
			// TODO Auto-generated method stub
			CloneFooB fooB = (CloneFooB) super.clone();
			if (this.fooA != null) {
				fooB.fooA = (CloneFooA) this.fooA.clone(); 
			}
			return fooB;
		}
	}
}
