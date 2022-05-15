import java.util.*;
import java.io.*;
public class toCompile {
public static void main(String[] args) {
final var al = new  ArrayList<Integer>();
al.add(1);
al.add(2);
al.add(3);
System.out.println(al.get(1));
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
