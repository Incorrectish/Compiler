import java.util.*;
import java.io.*;
public class tictactoe {
private static boolean winO( char[] x) {
return ((x[0] == 'O') && (x[4] == 'O') && (x[8] == 'O')) || ((x[0] == 'O') && (x[1] == 'O') && (x[2] == 'O')) || ((x[2] == 'O') && (x[4] == 'O') && (x[6] == 'O')) || ((x[3] == 'O') && (x[4] == 'O') && (x[5] == 'O')) || ((x[6] == 'O') && (x[7] == 'O') && (x[8] == 'O')) || ((x[0] == 'O') && (x[3] == 'O') && (x[6] == 'O')) || ((x[1] == 'O') && (x[4] == 'O') && (x[7] == 'O')) || ((x[2] == 'O') && (x[5] == 'O') && (x[8] == 'O'));
}
private static boolean boardFilled( char[] x) {
for(var i: x) {
if(i!='X' && i!='O') {
return false;
}
}
return true;
}
private static void printBoard( char[] x) {
System.out.println("+-+-+-+");
System.out.println("|"+x[0]+"|"+x[1]+"|"+x[2]+"|");
System.out.println("+-+-+-+");
System.out.println("|"+x[3]+"|"+x[4]+"|"+x[5]+"|");
System.out.println("+-+-+-+");
System.out.println("|"+x[6]+"|"+x[7]+"|"+x[8]+"|");
System.out.println("+-+-+-+");
}
private static boolean winX( char[] x) {
return ((x[0] == 'X') && (x[4] == 'X') && (x[8] == 'X')) || ((x[0] == 'X') && (x[1] == 'X') && (x[2] == 'X')) || ((x[2] == 'X') && (x[4] == 'X') && (x[6] == 'X')) || ((x[3] == 'X') && (x[4] == 'X') && (x[5] == 'X')) || ((x[6] == 'X') && (x[7] == 'X') && (x[8] == 'X')) || ((x[0] == 'X') && (x[3] == 'X') && (x[6] == 'X')) || ((x[1] == 'X') && (x[4] == 'X') && (x[7] == 'X')) || ((x[2] == 'X') && (x[5] == 'X') && (x[8] == 'X'));
}
public static void main(String[] args) {
final char[] x = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
printBoard(x);
while(true) {
System.out.println("X goes");
var z = readInt()-1;
x[z] = 'X';
if(winX(x)) {
System.out.println("X wins");
return;
}
if(boardFilled(x)) {
break;
}
printBoard(x);
System.out.println("O goes");
z = readInt()-1;
x[z] = 'O';
if(winO(x)) {
System.out.println("O wins");
return;
}
printBoard(x);
if(boardFilled(x)) {
break;
}
}
System.out.println("Tie");
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
