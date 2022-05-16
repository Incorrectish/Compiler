The syntax of this language is fairly similar to Java and Rust
First definitions:  
Variables can be defined with the let name: type syntax.  
Variables are immutable by default, and type inference has been created.  
For example:
let x = 4;
    ^ created an immutable integer with a value of 4
let y: mut = "Hello";
	^ created a mutable string with a value of "Hello". Note: The space after the colon is IMPORTANT, do not forget it or you will get a syntax error
For objects and arrays, you MUST explicitly define the type
for example:
let z: int[] = [x: i => (i*i)];
	^ created an immutable array with a size of x and default value of the index squared
Conditionals:
These are mostly the same with a couple exceptions:
no parantheses after the "if" and before the "{". In addition you can use the the  
if i: [0, 3) {}
to define the condition if(i>=0 && i<3){}
That same syntax applies to while loops
For for loops however that syntax means something different
for i: [0, 4]; 3 {}
would mean for(int i = 0+3; i<4; i+=3){}
Some more syntax changes:
Macros can be defined with #define something as another thing
use statements import an object from the same computer, specify the full path unless it is in the same folder, so that that object file can also be compiled
import statements import a library. Instead of import java.whatever use import I.whatever
functions are defined with [access modifier] [static] fn [name]([parameter: type], ...) => return type {}
Finally no std; prevents the import of java.io and java.util by default, and also the creation of the readLine(), readInt(), and readDouble methods.
Objects have added a bit of functionality: you can call the getter or setter of an object with ::
for example let x = object::attribute; => let x = object.getAttribute(); && object::attribute = x; => object.setAttribute(x); USING THIS WITHOUT GETTERS AND SETTERS WILL LEAD TO SYNTAX ERRORS. However getters and setters are created by default unless otherwise specified
Finally hashmaps and arraylists have also been changed to be a bit more userfriendly
you can use let v = Hmap:[x]; => let v = Hmap.get(x); or Hmap:[x] = v; => Hmap.replace(x, v); and  let v = arraylist:[x]; => let v = arraylist.get(x); or arraylist:[x] = v; => arraylist.set(x, v); 
Lastly System.out.println() and System.out.print() have been redefined to println() and print() in what I hope is a welcome move.
In addition there is no need to create a main class, just a main function.
Short demo program fizzbuzz
fn main() { 
	for i: [1, 100] { 
		if i%3 == 0 && i%5 == 0 { 
			println("fizzbuzz"); 
		} else if i%3 == 0 { 
			println("fizz"); 
		} else if i%5 == 0 { 
			println("buzz"); 
		} 
	} 
} 
One word of advice: TAKE THE SPACES SERIOUSLY. After any colon and before any { have exactly one space and everything should work out. I included a shell command I [targetFile.txt] to compile and run your program. Just add this line to your bashrc alias I="./language.sh" and everything should work out. 
Tic tace toe game in I 
fn main() { 
    let x: char[] = ['1', '2', '3', '4', '5', '6', '7', '8', '9']; 
    printBoard(x); 
    while true { 
        println("X goes"); 
        let z: mut = readInt()-1; 
        x[z] = 'X'; 
        if winX(x) { 
            println("X wins"); 
            return; 
        } 
        if boardFilled(x) { 
            break; 
        } 
        printBoard(x); 
        println("O goes"); 
        z = readInt()-1; 
        x[z] = 'O'; 
        if winO(x) { 
            println("O wins"); 
            return; 
        } 
        printBoard(x); 
        if boardFilled(x) { 
            break; 
        } 
    } 
    println("Tie"); 
} 

stat fn winX(x: char[]) => boolean { 
    return ((x[0] == 'X') && (x[4] == 'X') && (x[8] == 'X')) || ((x[0] == 'X') && (x[1] == 'X') && (x[2] == 'X')) || ((x[2] == 'X') && (x[4] == 'X') && (x[6] == 'X')) || ((x[3] == 'X') && (x[4] == 'X') && (x[5] == 'X')) || ((x[6] == 'X') && (x[7] == 'X') && (x[8] == 'X')) || ((x[0] == 'X') && (x[3] == 'X') && (x[6] == 'X')) || ((x[1] == 'X') && (x[4] == 'X') && (x[7] == 'X')) || ((x[2] == 'X') && (x[5] == 'X') && (x[8] == 'X'));  
} 


stat fn winO(x: char[]) => boolean {  
    return ((x[0] == 'O') && (x[4] == 'O') && (x[8] == 'O')) || ((x[0] == 'O') && (x[1] == 'O') && (x[2] == 'O')) || ((x[2] == 'O') && (x[4] == 'O') && (x[6] == 'O')) || ((x[3] == 'O') && (x[4] == 'O') && (x[5] == 'O')) || ((x[6] == 'O') && (x[7] == 'O') && (x[8] == 'O')) || ((x[0] == 'O') && (x[3] == 'O') && (x[6] == 'O')) || ((x[1] == 'O') && (x[4] == 'O') && (x[7] == 'O')) || ((x[2] == 'O') && (x[5] == 'O') && (x[8] == 'O')); 
} 

stat fn boardFilled(x: char[]) => boolean { 
    for i: x { 
        if i!='X' && i!='O' { 
            return false; 
        } 
    } 
    return true; 
} 

stat fn printBoard(x: char[]) { 
    println("+-+-+-+"); 
    println("|"+x[0]+"|"+x[1]+"|"+x[2]+"|"); 
    println("+-+-+-+"); 
    println("|"+x[3]+"|"+x[4]+"|"+x[5]+"|"); 
    println("+-+-+-+"); 
    println("|"+x[6]+"|"+x[7]+"|"+x[8]+"|"); 
    println("+-+-+-+"); 
} 
