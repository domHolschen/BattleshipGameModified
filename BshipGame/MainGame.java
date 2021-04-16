import java.util.Scanner;
import java.util.Random;

class Board {
    private int[][] arr = new int[10][10];
    private int shipCells = 0;

    public void clearBoard() { //initializes 2d array and sets all values to zero
        shipCells = 0;
        for (int i=0;i<10;i++) { //the iterator i will always be used for x axis of grid
            for (int j=0;j<10;j++) { //the iterator j will always be used for y axis of grid
                arr[i][j] = 0;
            }
        }
    }
    public void addShip(int length, int rootX, int rootY, boolean acceptableLocation) {
        Random rand = new Random();
        if (rand.nextInt(2) == 0) { //horizontal ship
            do {
                rootX = rand.nextInt(11 - length);
                rootY = rand.nextInt(10);
                acceptableLocation = true;
                for (int i = 0; i < length; i++) {
                    if (arr[rootX+i][rootY] != 0) acceptableLocation = false;
                }
            }
            while (acceptableLocation == false);
            for (int i = 0; i < length; i++) {
                arr[rootX+i][rootY] = 1;
            }
        } else { //vertical ship
            do {
                rootX = rand.nextInt(10);
                rootY = rand.nextInt(11 - length);
                acceptableLocation = true;
                for (int i = 0; i < length; i++) {
                    if (arr[rootX][rootY+i] != 0) acceptableLocation = false;
                }
            }
            while (acceptableLocation == false);
            for (int i = 0; i < length; i++) {
                arr[rootX][rootY+i] = 1;
            }
        }
        shipCells += length;
    }
    public int getBoard(int xCoord, int yCoord) {
        return arr[xCoord][yCoord];
    }
    public int getShipCells() {
        return shipCells;
    }
    public void setBoard(int xCoord, int yCoord, int setValue) {
        arr[xCoord][yCoord] = setValue;
    }
    public char displayBoard(int arrValue) {
        if (arrValue <= 1) return '.'; //represents space that has not yet been attacked. 0 means a ship does not exist there and 1 means a ship does exist
        if (arrValue == 2) return 'm'; //represents a miss
        if (arrValue == 3) return 'H'; //represents a hit
        return '?';
    }
    public char getRowName(int rowValue) {
        if (rowValue == 0) return 'A';
        if (rowValue == 1) return 'B';
        if (rowValue == 2) return 'C';
        if (rowValue == 3) return 'D';
        if (rowValue == 4) return 'E';
        if (rowValue == 5) return 'F';
        if (rowValue == 6) return 'G';
        if (rowValue == 7) return 'H';
        if (rowValue == 8) return 'I';
        if (rowValue == 9) return 'J';
        return 'X';
    }
}

public class MainGame {
    public static void main(String[] args) {
        int rowSelected, columnSelected;
        int missiles = 35, hits = 0, megaMissiles = 0, obtainMegaMissiles = 0;
        Board gameBoard = new Board();
        Scanner inp = new Scanner(System.in);
        Random rand2 = new Random();
        gameBoard.clearBoard();
        gameBoard.addShip(5,0,0,false);
        gameBoard.addShip(4,0,0,false);
        gameBoard.addShip(3,0,0,false);
        gameBoard.addShip(3,0,0,false);
        gameBoard.addShip(2,0,0,false);
        while (true) {
            System.out.println("\nHits: "+hits+"/"+gameBoard.getShipCells());
            System.out.println("You have "+(missiles+megaMissiles)+" missiles remaining.");
            if (missiles+megaMissiles <= 0 || hits == gameBoard.getShipCells()) break;
            System.out.print("\n   0   1   2   3   4   5   6   7   8   9");
            for (int j=0;j<10;j++) { //j comes first before i to make it print properly. these loops print the grid.
                if (j > 0) System.out.print("\n");
                System.out.print("\n"+gameBoard.getRowName(j)+"  ");
                for (int i=0;i<10;i++) {
                    System.out.print(gameBoard.displayBoard(gameBoard.getBoard(i,j))+"   ");
                }
            }

            System.out.println("\n\nEnter a coordinate (eg. a0, b3, j4, etc.):");
            if (megaMissiles > 0) System.out.println("!! MEGA MISSILE ARMED AND READY !!");
            String fullInput = inp.nextLine();
            char rowInput = fullInput.charAt(0);
            columnSelected = Character.getNumericValue(fullInput.charAt(1));
            if (rowInput == 'a') rowSelected = 0;
            else if (rowInput == 'b') rowSelected = 1;
            else if (rowInput == 'c') rowSelected = 2;
            else if (rowInput == 'd') rowSelected = 3;
            else if (rowInput == 'e') rowSelected = 4;
            else if (rowInput == 'f') rowSelected = 5;
            else if (rowInput == 'g') rowSelected = 6;
            else if (rowInput == 'h') rowSelected = 7;
            else if (rowInput == 'i') rowSelected = 8;
            else if (rowInput == 'j') rowSelected = 9;
            else rowSelected = 0;
            if (columnSelected < 0 || columnSelected > 9) columnSelected = 0;

            System.out.println("\n\n");

            if (gameBoard.getBoard(columnSelected, rowSelected) == 1) {
                hits++;
                System.out.println("Wow. Color me impressed. You actually HIT me.");
            }
            if (gameBoard.getBoard(columnSelected, rowSelected) == 2) System.out.println("You attacked where you already missed. Good one. Now start trying.");
            if (gameBoard.getBoard(columnSelected, rowSelected) == 3) System.out.println("You can't hit me more than once on the same spot. Do you even know how to play Battleship?");

            if (gameBoard.getBoard(columnSelected, rowSelected) == 0) {
                System.out.println("Oops! You MISSED! Go ahead, try again, dummy.");
                if (rand2.nextInt(6) == 0) { //1 in 6 chance to gain a bonus when you miss
                    if (rand2.nextInt(2) == 0) {
                        int bonusMissiles = rand2.nextInt(3) + 2; //50% chance to gain 2-4 extra missiles
                        missiles += bonusMissiles;
                        System.out.println("Whoa! You found an extra "+bonusMissiles+" missiles at "+fullInput+"!");
                    } else { //50% chance to gain a mega missile
                        obtainMegaMissiles += 1; //uses this variable instead of megaMissiles so you will use it next round instead of when you find one.
                        System.out.println("Oh baby! You found a MEGA MISSILE at "+fullInput+"!!!");
                    }
                }
            }

            if (megaMissiles > 0) {
                for (int i=0; i<9; i++){
                    int megaMissileColumn = columnSelected + (int) Math.floor(i/3) - 1;
                    int megaMissileRow = rowSelected + i%3 - 1;
                    if (megaMissileColumn >= 0 && megaMissileColumn <= 9 && megaMissileRow >= 0 && megaMissileRow <= 9) { //checks if the coordinates are valid
                        if (gameBoard.getBoard(megaMissileColumn, megaMissileRow) == 1) hits++;
                        if (gameBoard.getBoard(megaMissileColumn, megaMissileRow) <= 1) gameBoard.setBoard(megaMissileColumn,megaMissileRow,gameBoard.getBoard(megaMissileColumn, megaMissileRow)+2);
                    }
                }
            } else {
                if (gameBoard.getBoard(columnSelected, rowSelected) <= 1) gameBoard.setBoard(columnSelected,rowSelected,gameBoard.getBoard(columnSelected, rowSelected)+2);
            }

            if (megaMissiles > 0) megaMissiles--;
            else missiles--;
            if (obtainMegaMissiles > 0) {
                megaMissiles += obtainMegaMissiles;
                obtainMegaMissiles = 0;
            }
        }
        if (hits == gameBoard.getShipCells()) {
            System.out.println("I can't believe you beat me... you definitely cheated.");
        } else {
            System.out.println("You ran out of missiles. Nice try, though.");
        }
    }
}