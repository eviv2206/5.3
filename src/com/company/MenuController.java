package com.company;

import java.io.*;
import java.util.Objects;

import static com.company.DeskConstants.*;
import static com.company.FiguresAbbreviationConstants.DESTINATION_POINT_ABBREVIATION;
import static com.company.FiguresAbbreviationConstants.KNIGHT_ABBREVIATION;
import static com.company.MenuConstants.MAX_ITEM_MENU;
import static com.company.MenuConstants.MIN_ITEM_MENU;

public class MenuController {
    private static final ChessDesk desk = new ChessDesk();
    private static String result = null;

    public static void initDesk(){
        desk.initDesk(); }

    public static void showMenu(){
        System.out.println("""
                1. Расставить белые фигуры на доску.
                2. Задать координаты коня.
                3. Задать координаты точки назначения.
                4. Найти количество шагов.
                5. Показать текущую доску.
                6. Сбросить доску.
                7. Загрузить доску из файла.
                8. Сохранить результат в файл.
                9. Инфо о программе.
                10. Выход.
                """);
    }

    public static void doChosenMenuItem(Integer key){
        switch (key){
            case (1) -> arrangeChessPieces();
            case (2) -> addKnight();
            case (3) -> addPointDestination();
            case (4) -> showNumOfKnightMoves();
            case (5) -> showDesk();
            case (6) -> resetDesk();
            case (7) -> findDeskFromFile();
            case (8) -> saveResultToFile();
            case (9) -> showInfoAboutProgram();
        }
    }

    private static void showInfoAboutProgram() {
        InfoController.showInfoAboutProgram();
    }

    private static void resetDesk(){
        desk.resetDesk();
        InfoController.showSuccessfulDelete();
    }

    private static void findDeskFromFile() {
        desk.findDeskFromFile();
    }

    private static void saveResultToFile() {
        if (!Objects.isNull(result)) {
            BufferedWriter writer;
            try {
                writer = new BufferedWriter(new FileWriter(findFilePath()));
                writer.write(result);
                writer.close();
            } catch (IOException e) {
                InfoController.showIOException();
            }
            InfoController.showSuccessfulSave();
        } else {
            System.out.println("Ошибка в сохранении. Результат не вычислен.");
        }
    }

    private static void showNumOfKnightMoves() {
        if (areAllCoordinatesAreEntered()) {
            int numOfMoves = getNumOfKnightMoves();
            if (numOfMoves == Integer.parseInt(EMPTY_CELL_VALUE)) {
                result = "Данная точка не достижима.";
                System.out.println(result);
                resetDesk();
            } else {
                result = "Минимальное количество шагов: " + numOfMoves;
                System.out.println(result);
                resetDesk();
            }
        } else {
            System.out.println("Введены не все точки.");
        }
    }

    private static boolean areAllCoordinatesAreEntered() {
        return !(areKnightCoordinatesEntered() ||
                (desk.getPointDestinationCoordinationX() == INITIAL_KNIGHT_X_COORDINATE
                        && desk.getPointDestinationCoordinationY() == INITIAL_KNIGHT_Y_COORDINATE));
    }

    private static boolean areKnightCoordinatesEntered() {
            return desk.getKnightCoordinationX() == INITIAL_KNIGHT_X_COORDINATE
                && desk.getKnightCoordinationY() == INITIAL_KNIGHT_Y_COORDINATE;
    }

    private static int getNumOfKnightMoves() {
        return desk.getMinKnightMoves();
    }

    private static void addPointDestination() {
        if (!doesChessPieceExist(DESTINATION_POINT_ABBREVIATION)) {
            ChessPiece pointDestination = new ChessPiece();
            findAllCoordinates(pointDestination);
            desk.addPointDestination(pointDestination);
        } else {
            System.out.println("Точка назначения уже выставлена. Если хотите поменять, очистите доску.");
        }
    }

    private static void addKnight() {
        if (!doesChessPieceExist(KNIGHT_ABBREVIATION)) {
            ChessPiece knight = new ChessPiece();
            findAllCoordinates(knight);
            desk.addKnight(knight);
        } else {
            System.out.println("Конь уже выставлен. Если хотите поменять, очистите доску.");
        }
    }

    private static void arrangeChessPieces() {
        System.out.println("Введите кол-во фигур, которые хотите добавить:");
        int numOfChessPieces = findNumOfChessPieces();
        for (int i = 0; i < numOfChessPieces; i++){
            ChessPiece chessPiece = new ChessPiece();
            findAllCoordinates(chessPiece);
            desk.addChessToDesk(chessPiece);
        }
    }

    private static void showDesk(){
        desk.showDesk();
    }

    private static int findNumOfChessPieces(){
        String temp;
        do {
            temp = ScannerController.findDataFromConsole();
        } while (!isNum(temp));
        return Integer.parseInt(temp);
    }

    private static void findAllCoordinates(ChessPiece chessPiece){
        System.out.println("Введите X: ");
        chessPiece.coordinationX = findCoordinate();
        System.out.println("Введите Y: ");
        chessPiece.coordinationY = findCoordinate();
    }

    private static int findCoordinate(){
        String temp;
        do{
            temp = ScannerController.findDataFromConsole();
        } while (!isValidCoordination(temp));
        return Integer.parseInt(temp);
    }

    private static boolean isNum(String key){
        boolean isCorrect = true;
        try{
            Integer.parseInt(key);
        } catch (NumberFormatException e){
            isCorrect = false;
        }
        return isCorrect;
    }

    private static boolean isValidCoordination(String coordination){
        boolean isCorrect = isNum(coordination);
        if (isCorrect && (Integer.parseInt(coordination) < MIN_SIZE_DESK_COORDINATION
                || Integer.parseInt(coordination) > MAX_SIZE_DESK_COORDINATION)){
            isCorrect = false;
        }
        if (!isCorrect){
            showUncorrectedCoordination();
        }
        return isCorrect;
    }

    private static boolean isValidMenuItem(String key){
        boolean isCorrect = isNum(key);
        if (isCorrect && (Integer.parseInt(key) < MIN_ITEM_MENU || Integer.parseInt(key) > MAX_ITEM_MENU)){
            isCorrect = false;
        }
        if (!isCorrect){
            showUncorrectedMenuItem();
        }
        return isCorrect;
    }
    private static void showUncorrectedMenuItem(){
        System.out.println("Выберите корректный пункт меню:");
    }

    private static void showUncorrectedCoordination(){
        System.out.println("Введите координату корректно:");
    }

    public static Integer findMenuItem(){
        String tempItem;
        do {
            tempItem = ScannerController.findDataFromConsole();
        } while (!isValidMenuItem(tempItem));
        return Integer.parseInt(tempItem);
    }

    private static String findFilePath(){
        String filePath;
        FileReader reader = null;
        boolean isIncorrect;
        do {
            InfoController.showEnterFilePath();
            filePath = ScannerController.findDataFromConsole();
            isIncorrect = false;
            try {
                reader = new FileReader(filePath);
            } catch (FileNotFoundException e) {
                InfoController.fileNotFound();
                isIncorrect = true;
            }
        } while (isIncorrect);
        try {
            reader.close();
        } catch (IOException e) {
            InfoController.showIOException();
        }
        return filePath;
    }

    private static boolean doesChessPieceExist(String symbolOfChest){
        return desk.isChessPieceAlreadyExist(symbolOfChest);
    }
}
