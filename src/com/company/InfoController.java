package com.company;

public class InfoController {
    public static void showInfoAboutProgram(){
        System.out.println("""
                Данная программа вычисляет сколько шагов надо сделать коню, чтобы попасть в заданную точку.
                При чтении доски из файла, пустые поля должны иметь значение "0", конь - "K" (латинской), точка назначения - "X" (латинской).
                Другие фигуры расставлются с помощью функции в программе.
                """);
    }

    public static void showSuccessfulDelete(){
        System.out.println("Успешно удалено.");
    }

    public static void showSuccessfulSave(){
        System.out.println("Успешно сохранено.");
    }

    public static void showIOException(){
        System.out.println("I/O exception");
    }

    public static void fileNotFound(){
        System.out.println("Файл не найден");
    }

    public static void showEnterFilePath(){
        System.out.println("Введите путь к файлу:"); // НУЖНО ЛИ ТАК ДЕЛАТЬ?
    }

    public static void showExitLabel(){
        System.out.println("Выход...");
    }
}
