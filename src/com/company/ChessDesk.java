package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import static com.company.DeskConstants.EMPTY_CELL_VALUE;
import static com.company.FiguresAbbreviationConstants.*;
import static com.company.MenuConstants.MAX_LINE_SIZE_FROM_FILE;

public class ChessDesk {
    private final int row = 8;
    private final int col = 8;
    private final String[][] desk = new String[row][col];
    private int knightCoordinationX = 10;
    private int knightCoordinationY = 10;
    private int pointDestinationCoordinationX = 10;
    private int pointDestinationCoordinationY = 10;

    public int getKnightCoordinationX(){
        return this.knightCoordinationX;
    }

    public int getKnightCoordinationY(){
        return this.knightCoordinationY;
    }

    public int getPointDestinationCoordinationX(){
        return this.pointDestinationCoordinationX;
    }

    public int getPointDestinationCoordinationY(){
        return this.pointDestinationCoordinationY;
    }

    public void initDesk(){
        for (String[] elem : desk) {
            Arrays.fill(elem, "");
        }
    }

    public void showDesk(){
        for (String[] elem : desk) {
            for (String s : elem) {
                System.out.printf("[%2s ] ", s);
            }
            System.out.println();
        }
    }
    public void addChessToDesk(ChessPiece chessPiece){
        if (placeAlreadyTaken(chessPiece)){
            System.out.println("Место уже занято.");
        } else {
            desk[chessPiece.coordinationY - 1][chessPiece.coordinationX - 1] = PAWN_ABBREVIATION;
        }
    }

    public void addKnight(ChessPiece knight){
        if (placeAlreadyTaken(knight)){
            System.out.println("Место уже занято.");
        } else {
            knightCoordinationX = knight.coordinationX - 1;
            knightCoordinationY = knight.coordinationY - 1;
            desk[knight.coordinationY - 1][knight.coordinationX - 1] = KNIGHT_ABBREVIATION;
        }
    }

    public void addPointDestination(ChessPiece pointDestination){
        if (placeAlreadyTaken(pointDestination)){
            System.out.println("Место уже занято.");
        } else {
            pointDestinationCoordinationX = pointDestination.coordinationX - 1;
            pointDestinationCoordinationY = pointDestination.coordinationY - 1;
            desk[pointDestination.coordinationY - 1][pointDestination.coordinationX - 1] = DESTINATION_POINT_ABBREVIATION;
        }
    }

    public boolean placeAlreadyTaken(ChessPiece chessPiece){
        return !Objects.equals(desk[chessPiece.coordinationY - 1][chessPiece.coordinationX - 1], "");
    }

    private void incCell(int x, int y, int prevIndex, String[][] tempDesk){
        prevIndex++;
        if (x > -1 && x < tempDesk.length && y > -1 && y < tempDesk.length) {
            int indexCell = Integer.parseInt(tempDesk[y][x]);
            if (indexCell > -1 && indexCell > prevIndex) {
                tempDesk[y][x] = String.valueOf(prevIndex);
            }
        }
    }


    private void fillMoves(int x, int y, String[][] tempDesk){
        int index = Integer.parseInt(tempDesk[y][x]);
        for (int i = 0; i < tempDesk.length; i++){
            incCell(x + 1, y + 2, index, tempDesk);
            incCell(x + 1, y - 2, index, tempDesk);
            incCell(x - 1, y + 2, index, tempDesk);
            incCell(x - 1, y - 2, index, tempDesk);
            incCell(x + 2, y + 1, index, tempDesk);
            incCell(x + 2, y - 1, index, tempDesk);
            incCell(x - 2, y + 1, index, tempDesk);
            incCell(x - 2, y - 1, index, tempDesk);
        }
    }

    private void fillEmptyCells(String[][] tempDesk) {
        for (int i = 0; i < tempDesk.length; i++) {
            for (int j = 0; j < tempDesk.length; j++) {
                if (!Objects.equals(tempDesk[i][j], PAWN_ABBREVIATION)
                        && !Objects.equals(tempDesk[i][j], KNIGHT_ABBREVIATION)) {
                    tempDesk[i][j] = EMPTY_CELL_VALUE;
                }
            }
        }
    }

    public void resetDesk(){
        for (String[] s : desk){
            Arrays.fill(s, "");
        }
    }

    public String[][] findCopyOfCurrentDesk(){
        String[][] tempDesk = new String[row][col];
        for (int i = 0; i < desk.length; i++){
            System.arraycopy(desk[i], 0, tempDesk[i], 0, desk.length);
        }
        return tempDesk;
    }

    public int getMinKnightMoves(){
        String[][] tempDesk = findCopyOfCurrentDesk();
        fillEmptyCells(tempDesk);
        tempDesk[knightCoordinationY][knightCoordinationX] = "0";
        fillMoves(knightCoordinationX, knightCoordinationY, tempDesk);
        boolean isChanged = !Objects.equals(tempDesk[pointDestinationCoordinationY][pointDestinationCoordinationX], EMPTY_CELL_VALUE);
        int i = 1;
        while (!isChanged && i < 9){
            for (int j = 0; j < tempDesk.length; j++){
                for (int k = 0; k < tempDesk[i].length; k++){
                    if (Integer.parseInt(tempDesk[j][k]) == i) {
                        fillMoves(k, j, tempDesk);
                        isChanged = !Objects.equals(tempDesk[pointDestinationCoordinationY][pointDestinationCoordinationX], EMPTY_CELL_VALUE);
                    }
                }
            }
            i++;
        }
        return Integer.parseInt(tempDesk[pointDestinationCoordinationY][pointDestinationCoordinationX]) ;
    }

    public boolean isChessPieceAlreadyExist(String symbolOfChest) {
        boolean isExist = false;
        int i = 0;
        while (!isExist && i < desk.length){
            for (String j : desk[i]){
                if (Objects.equals(j, symbolOfChest)){
                    isExist = true;
                }
            }
            i++;
        }
        return isExist;
    }

    public void findDeskFromFile(){
        boolean isCorrect = true;
        String temp;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(findFilePath()));
            int i = 0;
            while (isCorrect && i < desk.length){
                temp = reader.readLine();
                if (Objects.isNull(temp) || temp.length() != MAX_LINE_SIZE_FROM_FILE || !isAvailableSymbols(temp)){
                    System.out.println("Файл поврежден.");
                    isCorrect = false;
                } else {
                    String[] arr = createArr(temp);
                    System.arraycopy(arr, 0, desk[i], 0, desk.length);
                }
                i++;
            }
        } catch (IOException e) {
            System.out.println("Файл поврежден.");
            isCorrect = false;
        }
            if (isCorrect) {
                refactorElements();
                System.out.println("Успешно загружено.");
            }
    }

    private void refactorElements(){
        for (int i = 0; i < desk.length; i++){
            for (int j = 0; j < desk.length; j++){
                if (Objects.equals(desk[i][j], KNIGHT_ABBREVIATION)){
                    knightCoordinationX = j;
                    knightCoordinationY = i;
                }
                if (Objects.equals(desk[i][j], DESTINATION_POINT_ABBREVIATION)){
                    pointDestinationCoordinationX = j;
                    pointDestinationCoordinationY = i;
                }
            }
        }
    }

    private boolean  isAvailableSymbols(String temp) {
        boolean isAvailable = true;
        char[] arr = temp.toCharArray();
        int i = 0;
        while (isAvailable && i < arr.length){
            if (arr[i] != KNIGHT_ABBREVIATION.charAt(0) && arr[i] != ' '
                    && arr[i] != '0' && arr[i] != DESTINATION_POINT_ABBREVIATION.charAt(0)) {
                isAvailable = false;
            }
            i++;
        }
        return isAvailable;
    }

    private int findNumOfSpaces(String inputStr){
        int counter = 0;
        char[] cArr = inputStr.toCharArray();
        for (char c : cArr) {
            if (c == ' ') {
                counter++;
            }
        }
        return counter;
    }

    private String[] createArr (String inputStr){
        String[] arr = new String[findNumOfSpaces(inputStr) + 1];
        int counter = 0;
        char[] cArr = inputStr.toCharArray();
        StringBuilder temp = new StringBuilder();
        for (char c : cArr) {
            if (c != ' ') {
                temp.append(c);
            } else {
                    arr[counter] = String.valueOf(temp);
                    counter++;
                    temp.setLength(0);
            }
        }
        arr[counter] = String.valueOf(temp);
        for (int i = 0; i < arr.length; i++){
            if (Objects.equals(arr[i], "0")){
                arr[i] = "";
            }
        }
        return arr;
    }


    private static String findFilePath() {
        String filePath;
        FileReader reader = null;
        boolean isIncorrect;
        do {
            System.out.print("Введите путь к файлу: ");
            filePath = ScannerController.findDataFromConsole();
            isIncorrect = false;
            try {
                reader = new FileReader(filePath);
            } catch (FileNotFoundException e) {
                System.out.println("Файл не найден.");
                isIncorrect = true;
            }
        } while (isIncorrect);
        try {
            reader.close();
        } catch (IOException e) {
            System.err.println("I/O error.");
        }
        return filePath;
    }


}
