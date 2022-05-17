import java.util.*;
import java.io.*;
import java.util.regex.*;
public class main {
	public static void main(String[] args) {
		final Pattern pattern = Pattern.compile("test");
		final objectTest z = new  objectTest(5, "Hi", pattern);
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
