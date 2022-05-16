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
