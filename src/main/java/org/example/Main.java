package org.example;

import java.util.Random;
import java.util.Scanner;

public class Main {
    private static final char PLAYER_1_SIGN = 'X';
    private static final char PLAYER_2_SIGN = '0';
    private static final char EMPTY_CELL = '*';
    private static int fieldXSize;
    private static int fieldYSize;
    private static int winCount;
    private static char[][] field;
    private static final Scanner sc = new Scanner(System.in);
    private static final Random rnd = new Random();

    public static void main(String[] args) {
        fieldInit();
        showField();
        while (true) {

            player1Move();
            if (isWin(PLAYER_1_SIGN, winCount)) {
                showField();
                System.out.println("Player 1 win!");
                break;
            } else {
                showField();
                player2Move();
                showField();
            }
            if (isWin(PLAYER_2_SIGN, winCount)) {
                showField();
                System.out.println("Player 2 win!");
                break;
            }
        }
    }

    /**
     * Field variables initialisation
     */
    private static void fieldInit() {
        fieldXSize = 8;
        fieldYSize = 8;
        winCount = 5;
        field = new char[fieldXSize][fieldYSize];
        for (int x = 0; x < fieldXSize; x++) {
            for (int y = 0; y < fieldYSize; y++) {
                field[x][y] = EMPTY_CELL;
            }
        }
    }

    /**
     * Current field showing
     */
    private static void showField() {
        System.out.print("  ");
        for (int x = 0; x < fieldXSize; x++) {
            System.out.print(' ');
            System.out.print(x + 1);
            System.out.print("  ");
        }
        System.out.println();
        System.out.print(" +");
        for (int x = 0; x < fieldXSize; x++) {
            System.out.print("---+");
        }
//        System.out.println('+');
        System.out.println();
        for (int y = 0; y < fieldYSize; y++) {
            System.out.print(y + 1);
            System.out.print("|");
            for (int x = 0; x < fieldXSize; x++) {
                System.out.printf(" %s |", field[x][y]);
            }
            System.out.println();
            System.out.print(" +");
            for (int x = 0; x < fieldXSize; x++) {
                System.out.print("---+");
            }
            System.out.println();
        }
    }

    /**
     * Player 1 input move coordinates
     */
    private static void player1Move() {
        String playerInputX;
        String playerInputY;
        int x = -1;
        int y = -1;
        do {
            System.out.print("Input column: ");
            playerInputX = sc.nextLine();
            System.out.print("Input row: ");
            playerInputY = sc.nextLine();
            if (playerInputX.matches("[-+]?\\d+") && playerInputY.matches("[-+]?\\d+")){
                x = Integer.parseInt(playerInputX) - 1;
                y = Integer.parseInt(playerInputY) - 1;
            }

        } while (!(isCoordinatesCorrect(x, y) && isCellEmpty(x, y)));
        field[x][y] = PLAYER_1_SIGN;
    }

    /**
     * Player 2 Choosing move coordinates
     */
    private static void player2Move() {
        int xCoordinate = 0;
        int yCoordinate = 0;
        boolean moveFound = false;
        for (int x = 0; x < fieldXSize; x++) {
            for (int y = 0; y < fieldYSize; y++) {
                if (isCellEmpty(x, y) && (countHorizontal(x, y, PLAYER_1_SIGN) == winCount - 1)) {
                    moveFound = true;
                    xCoordinate = x;
                    yCoordinate = y;
                    break;
                }
                if (isCellEmpty(x, y) && (countVertical(x, y, PLAYER_1_SIGN) == winCount - 1)) {
                    moveFound = true;
                    xCoordinate = x;
                    yCoordinate = y;
                    break;
                }
                if (isCellEmpty(x, y) && (countAscendingDiagonal(x, y, PLAYER_1_SIGN) == winCount - 1)) {
                    moveFound = true;
                    xCoordinate = x;
                    yCoordinate = y;
                    break;
                }
                if (isCellEmpty(x, y) && (countDescendingDiagonal(x, y, PLAYER_1_SIGN) == winCount - 1)) {
                    moveFound = true;
                    xCoordinate = x;
                    yCoordinate = y;
                    break;
                }
            }
        }
        if (!moveFound) {
            do {
                xCoordinate = rnd.nextInt(fieldXSize);
                yCoordinate = rnd.nextInt(fieldYSize);
            } while (!isCellEmpty(xCoordinate, yCoordinate));
        }


        field[xCoordinate][yCoordinate] = PLAYER_2_SIGN;

    }

    /**
     * Checking if a cell is empty
     *
     * @param x column coordinate
     * @param y row coordinate
     * @return true if the cell with (x, y) coordinate is empty
     */
    private static boolean isCellEmpty(int x, int y) {
        return field[x][y] == EMPTY_CELL;
    }

    /**
     * Checking if a cell is inside the field borders
     *
     * @param x column coordinate
     * @param y row coordinate
     * @return true if the cell with (x, y) coordinate is inside the field borders
     */
    private static boolean isCoordinatesCorrect(int x, int y) {
        return x > -1 && x < fieldXSize && y > -1 && y < fieldYSize;
    }

    private static boolean isWin(char sign, int goalCount) {
        for (int x = 0; x < fieldXSize; x++) {
            for (int y = 0; y < fieldYSize; y++) {
                if (field[x][y] == sign) {
                    int signCount;
//                    int dy;
                    /*
                     * Verifying horizontal
                     */
                    signCount = 1 + countHorizontal(x, y, sign);

                    if (signCount == goalCount) {
                        return true;
                    }
                    /*
                     * Verifying vertical
                     */
                    signCount = 1 + countVertical(x, y, sign);

                    if (signCount == goalCount) {
                        return true;
                    }

                    /*
                     * Verifying upper diagonal
                     */
                    signCount = 1 + countAscendingDiagonal(x, y, sign);

                    if (signCount == goalCount) {
                        return true;
                    }
                    /*
                     * Verifying bottom diagonal
                     */
                    signCount = 1 + countDescendingDiagonal(x, y, sign);
                    if (signCount == goalCount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * @param x Column of start position for sequence count
     * @param y Row of start position for sequence count
     * @param sign Sign for count
     * @return Quantity of sign in the horizontal sequence
     */
    private static int countHorizontal(int x, int y, char sign) {
        int dx;
        int dy = 0;
        int signCount = 0;
        for (dx = 1; dx < winCount; dx++) {

            int currentX = x + dx;
            int currentY = y + dy;
            if (!isCoordinatesCorrect(currentX, currentY) || field[currentX][currentY] != sign) {
                break;
            } else {
                signCount++;
            }

        }
        return signCount;
    }
    /**
     * @param x Column of start position for sequence count
     * @param y Row of start position for sequence count
     * @param sign Sign for count
     * @return Quantity of sign in the vertical sequence
     */
    private static int countVertical(int x, int y, char sign) {
        int dx = 0;
        int dy;
        int signCount = 0;
        for (dy = 1; dy < winCount; dy++) {

            int currentX = x + dx;
            int currentY = y + dy;
            if (!isCoordinatesCorrect(currentX, currentY) || field[currentX][currentY] != sign) {
                break;
            } else {
                signCount++;
            }

        }
        return signCount;
    }
    /**
     * @param x Column of start position for sequence count
     * @param y Row of start position for sequence count
     * @param sign Sign for count
     * @return Quantity of sign in the ascending diagonal sequence
     */
    private static int countAscendingDiagonal(int x, int y, char sign) {
        int dx = 1;
        int dy;
        int signCount = 0;
        for (dy = 1; dy < winCount; dy++) {

            int currentX = x + dx;
            int currentY = y - dy;
            if (!isCoordinatesCorrect(currentX, currentY) || field[currentX][currentY] != sign) {
                break;
            } else {
                signCount++;
            }
            dx++;
        }
        return signCount;
    }

    /**
     *
     * @param x Column of start position for sequence count
     * @param y Row of start position for sequence count
     * @param sign Sign for count
     * @return Quantity of sign in the descending diagonal sequence
     */
    private static int countDescendingDiagonal(int x, int y, char sign) {
        int dx = 1;
        int dy;
        int signCount = 0;
        for (dy = 1; dy < winCount; dy++) {

            int currentX = x + dx;
            int currentY = y + dy;
            if (!isCoordinatesCorrect(currentX, currentY) || field[currentX][currentY] != sign) {
                break;
            } else {
                signCount++;
            }
            dx++;
        }
        return signCount;
    }
}