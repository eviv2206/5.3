package com.company;

import java.util.Scanner;

public class ScannerController {
    private static final Scanner INPUT = new Scanner(System.in);
    public static String findDataFromConsole(){
        return INPUT.nextLine();
    }
}
