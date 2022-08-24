package com.company;

import static com.company.MenuConstants.EXIT_KEY;

public class Main {

    public static void main(String[] args) {
        int key;
        MenuController.initDesk();
        do {
            MenuController.showMenu();
            key = MenuController.findMenuItem();
            MenuController.doChosenMenuItem(key);
        } while (key != EXIT_KEY);
        InfoController.showExitLabel();
    }
}
