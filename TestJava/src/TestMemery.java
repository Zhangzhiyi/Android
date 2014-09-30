public class TestMemery {

	// ia是一个静态变量，在这里它作为一个int数组的引用。
	static int[] ia = new int[1024 * 1024];

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 int i = 0;
	        do {
	            if (3 == i++) {
	                ia = null;
	                System.out.println("release");
	            }
	            System.gc();
	        } while (i<6);
	}
}
