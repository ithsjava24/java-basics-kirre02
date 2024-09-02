package org.example;

import javax.xml.crypto.Data;
import java.util.Scanner;

import static java.lang.System.exit;

public class App {

    public static void main(String[] args) {

        var selection = menu();

        switch (selection) {
            case 1 -> System.out.println("Inmatning");
            case 2 -> System.out.println("Min, Max och Medel");
            case 3 -> System.out.println("Sortera");
            case 4 -> exit(1);
            default -> System.out.println("ogiltigt val");
        };

    }

    private static int menu() {
        int selection;

        Scanner sc = new Scanner(System.in);

        System.out.println("Elpriser");
        System.out.println("-------------------------\n");
        System.out.println("1 - inmatning");
        System.out.println("2 - Min, Max och Medel");
        System.out.println("3 - Sortera");
        System.out.println("4 - Avsluta");

        selection = sc.nextInt();

        return selection;
    }
}
