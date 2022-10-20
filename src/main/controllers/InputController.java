package main.controllers;

import java.util.Scanner;

public class InputController {
    private static Scanner scan = new Scanner(System.in);

    private InputController() {
        throw new IllegalStateException("Utility Class");
    }

    public static int getInt() {
        return scan.hasNextInt() ? scan.nextInt() : -1;
    }

    public static void clear() {
        scan.nextLine();
    }

    


}
