import java.util.*;
import java.io.*;
public class toCompile {
public static void main(String[] args) {
for(var i = (1); i<= 100; i+=1) {
if(i%3 == 0 && i%5 == 0) {
System.out.println("fizzbuzz");
} else if(i%3 == 0) {
System.out.println("fizz");
} else if(i%5 == 0) {
System.out.println("buzz");
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
