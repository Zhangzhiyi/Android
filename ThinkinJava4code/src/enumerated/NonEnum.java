package enumerated;

// : enumerated/NonEnum.java

public class NonEnum {
	public enum Priority {
		LOW, NORMAL, HIGH, IMMEDIATE
	}

	public static void main(String[] args) {
		Class<Integer> intClass = Integer.class;
		try {
			for (Object en : intClass.getEnumConstants())
				System.out.println(en);
		} catch (Exception e) {
			System.out.println(e);
		}
		System.out.println(Priority.LOW.ordinal());
		System.out.println(Priority.HIGH.ordinal());
	}
} /*
 * Output: java.lang.NullPointerException
 */// :~
