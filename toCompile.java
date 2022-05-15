import java.util.*;
import java.io.*;
public class toCompile {
public static void main(String[] args) {
for(int i = (0); i<= 100;i++) {
if(i%5 == 0 && i%3 ==0) {
System.out.println("fizzbuzz");
} else if(i%5 == 0) {
System.out.println("buzz");
} else if(i%3 == 0) {
System.out.println("fizz");
}
}
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
