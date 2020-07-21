package BattleShips;

import java.util.*;
import java.util.logging.XMLFormatter;

public class Main {
    public static void main(String[] args) {
        //variables
        Scanner input = new Scanner(System.in);
        int rows = 10;
        int cols = 10;
        int shipNumbers = 5;
        String[][] board = new String[rows][cols];

        //methods
        gameIntro();
        board(board);
        userPlacement(input, shipNumbers, rows, cols, board);

        computerPlacement(shipNumbers, rows, cols, board);
        battle(input, rows, cols, board);
    }

    //intro to game
    public static void gameIntro() {
        System.out.println("**** Welcome to Battle Ships game ****\n\nRight now, the sea is empty.");
    }

    //initial board set-up
    public static String[][] board(String[][] startBoard) {

        for (int row = 0; row < startBoard.length; row++) {
            for (int col = 0; col < startBoard.length; col++) {
                startBoard[row][col] = "~";
            }
        }
        return printBoard(startBoard);
    }

    //print the board
    public static String[][] printBoard(String[][] updateBoard) {
        System.out.println();
        String formatBoard = "\t0\t1\t2\t3\t4\t5\t6\t7\t8\t9\t";
        System.out.println(formatBoard);
        String[][] replaceHits = new String[updateBoard.length][updateBoard.length];
        for (int row = 0; row < updateBoard.length; row++) {
            System.out.print(row + "|");
            for (int col = 0; col < updateBoard.length; col++) {
                if(updateBoard[row][col] == "$"){
                    replaceHits[row][col] = "$";
                    updateBoard[row][col] = "~";
                }
                System.out.print("\t" + updateBoard[row][col]);
            }
            System.out.println("\t|" + row);
        }
        System.out.println(formatBoard);

        for(int row = 0; row < replaceHits.length; row++){
            for(int col = 0; col < replaceHits.length; col++){
                if(replaceHits[row][col]=="$"){
                    updateBoard[row][col]="$";
                }
            }
        }
        return updateBoard;
    }

    //set-up user placement
    public static void userPlacement(Scanner input, int ships, int rows, int cols, String[][] board) {
        System.out.println("Deploy your ships:");
        for (int i = 0; i < ships; i++) {
            System.out.print("Enter X coordinate for your " + (i + 1) + " ship. ");
            int intY = Integer.parseInt(input.nextLine());
            System.out.print("Enter Y coordinate for your " + (i + 1) + " ship. ");
            int intX = Integer.parseInt(input.nextLine());
                if ((-1 > intX || intX > cols-1) || (-1 > intY || intY > cols-1)){
                    System.err.println("The ship is placed outside of the board! Try again.");
                    i --;
                } else if ((-1 < intX && intX < cols-1) && (-1 < intY && intY < rows-1) && (board[intX][intY] == "~")) {
                    board[intX][intY] = "@";
                } else if (board[intX][intY] == "@") {
                    System.err.println("You already have a ship placed here! Try again.");
                    i --;
                } else {
                    i--;
            }
        }
        printBoard(board);
    }

    //set-up computer placement
    public static void computerPlacement(int ships, int rows, int cols, String[][] board) {
        for (int i = 0; i < ships; i++) {
            Random rand = new Random();
            int compXRand = rand.nextInt(9) + 0;
            int compYRand = rand.nextInt(9) + 0;
            if ((0 <= compXRand && compXRand <= rows) && (0 <= compYRand && compYRand <= cols) && (board[compXRand][compYRand] != "@")) {
                System.out.println("Computer ship " + i + " is deployed.");
                board[compXRand][compYRand] = "$";
            } else {
                i--;
            }
        }
    }

    //main game body
    public static void battle(Scanner input, int rows, int cols, String[][] board) {
        int userShips = 5;
        int compShips = 5;
        boolean stillPlay = true;
        while (stillPlay) {
            String lostShip = userMove(input, rows, cols, board);
            if (lostShip.equals("user")) {
                userShips--;
            } else if (lostShip.equals("comp")) {
                compShips--;
            }
            System.out.println("You have " + userShips + " left.");
            System.out.println("The computer has "+ compShips + " left.");
            lostShip = computerMove(rows, cols, board);
            if (lostShip.equals("user")) {
                userShips--;
            } else if (lostShip.equals("comp")) {
                compShips--;
            }
            if (userShips <= 0 || compShips <= 0){
                gameOver(userShips, compShips);
                stillPlay = false;
            }
        }
    }

    //user moves
    public static String userMove(Scanner input, int rows, int cols, String[][] board) {
        System.out.print("\n\nYOUR MOVE\n");
        boolean cantMove = true;
        String lost = "";
        int moveX = 0;
        int moveY = 0;
        while (cantMove) {
            System.out.print("Enter X coordinate: ");
            moveY = Integer.parseInt(input.nextLine());
            System.out.print("Enter Y coordinate: ");
            moveX = Integer.parseInt(input.nextLine());

            //determine if something exists on that spot
            if ((-1 < moveX && moveX < rows) && (-1 < moveY && moveY < cols)) {
                cantMove = false;
            }
        }
        switch (board[moveX][moveY]) {
            case "~":
                System.err.println("You missed!");
                board[moveX][moveY] = "-";
                break;
            case "@":
                System.err.println("You have sunk your own ship!");
                board[moveX][moveY] = "x";
                lost = "user";
                break;
            case "$":
                System.out.println("You have sunk the computer's ship!");
                board[moveX][moveY] = "!";
                lost = "comp";
                break;
        }
        printBoard(board);
        return lost;
    }

    //computer moves
    public static String computerMove(int rows, int cols, String[][] board) {
        System.out.println("COMPUTER MOVE");
        int moveX = -1008432333;
        int moveY = -1008432333;
        String lost = "";
        while (moveX == -1008432333 || moveY == -1008432333 || board[moveX][moveY] == "!" || board[moveX][moveY] == "-" || board[moveX][moveY] == "x") {
            Random rand = new Random();
            moveX = rand.nextInt(9) + 0;
            moveY = rand.nextInt(9) + 0;
            if ((-1 < moveX && moveX < rows) && (-1 < moveY && moveY < cols)) {
                break;
            }
        }
        switch (board[moveX][moveY]) {
            case "~":
                System.out.println("The computer missed!");
                board[moveX][moveY] = "~";
                break;
            case "$":
                System.out.println("The computer hit its own ship!");
                board[moveX][moveY] = "!";
                lost = "comp";
                break;
            case "@":
                System.err.println("The computer has sunk your ship!");
                board[moveX][moveY] = "x";
                lost = "user";
                break;
        }
        return lost;
    }

    //game over sequence
    public static void gameOver(int userShips, int compShips){
        if(userShips == 0) {
            System.out.println("You have lost all your battle ships\n\nThanks for playing.");
        }
        if(compShips == 0) {
            System.out.println("You won! You have sunk the computer's battle ships\n\nThanks for playing.");
        }
    }
}
