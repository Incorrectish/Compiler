import java.util.*;
import java.io.*;
public class toCompile {
public static int add( int[] a) {
int sum = 0;
for(var i: a) {
sum += i;
}
return sum;
}
public static void main(String[] args) {
final int[] a = {1, 2, 3, 4, 5};
System.out.println(add(a));
}
	public static String readLine() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	public static int readInt() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextInt();
	}

	public static String readLine(String message) {
		System.out.println(message);
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	public static int readInt(String message) {
		System.out.println(message);
		Scanner scanner = new Scanner(System.in);
		return scanner.nextInt();
	}

		public static double readDouble() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextDouble();
}

	public static double readDouble(String message) {
		System.out.println(message);
		Scanner scanner = new Scanner(System.in);
		return scanner.nextDouble();
	}

}
