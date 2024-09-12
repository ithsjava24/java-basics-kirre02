package org.example;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Scanner;

class Price {
    private final int price;
    private final String hour;

    public Price(String date, int price) {
        this.price = price;
        this.hour = date;
    }

    public int getPrice() {
        return price;
    }

    public String getHour() {
        return hour;
    }
}

public class App {

    static Price[] prices = new Price[24];

    // Valde att göra en for loop som går igenom alla Price objekt i arrayen och
    // fyller i alla objekten med de olika timmarna på dygnet.
    static {
        for (int i = 0; i <= 23; i++) {
            String timme;
            //I denna if sats så har jag sagt att när loopen är på sista "hoppet" så ska den sätta in 23-24
            // annars så skriver den 23-00.
            if (i == 23) {
                timme = "23-24";
            } else timme = String.format("%02d-%02d", i, (i + 1) % 24);
            prices[i] = new Price(timme, 0);
        }
    }

    public static void main(String[] args) {
        Locale.setDefault(Locale.of("SV", "SE"));

        Scanner sc = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("""
                    Elpriser
                    ========
                    1. Inmatning
                    2. Min, Max och Medel
                    3. Sortera
                    4. Bästa Laddningstid (4h)
                    5. Visualisering
                    e. Avsluta
                    """);

            var selection = sc.nextLine();

            switch (selection) {
                case "1" -> addPrice(sc);
                case "2" -> minMaxAverage();
                case "3" -> sortPrices();
                case "4" -> bestChargeTime();
                case "5" -> System.out.println("visualize");
                case "e", "E" -> exit = true;
                default -> System.out.println("ogiltigt val");
            }
        }
    }

    public static void addPrice(Scanner sc) {
        //en loop som går igenom längden av arrayen så att man kan fylla i priset för varje timme
        for (int i = 0; i < prices.length; i++) {
            prices[i] = new Price(prices[i].getHour(), sc.nextInt());
        }
        sc.nextLine();
    }


    public static void minMaxAverage() {
        // I Arrays.stream(prices) så skapar vi en stream av Price objektet från prices arrayen.
        // I .max(Comparator.comparingInt(Price::getPrice)).orElseThrow() Så letar man upp Price objektet som innehåller
        // den högsta värdet av fältet price som hämtas genom getPrice, skulle det vara så att inget värde finns tillgängligt
        // så kommer den att ge NoSuchElementException.
        Price highest = Arrays.stream(prices).max(Comparator.comparingInt(Price::getPrice)).orElseThrow();

        // Samma sak som i variabeln som ovan skillnaden är att man letar efter det minsta värdet av fältet price.
        Price lowest = Arrays.stream(prices).min(Comparator.comparingInt(Price::getPrice)).orElseThrow();

        //I denna variabel så tar jag priserna som finns i arrayen och slår plussar ihop dem så att jag får summan.
        float sum = Arrays.stream(prices).mapToInt(Price::getPrice).sum();

        float average = sum / prices.length;

        System.out.printf("""
                Lägsta pris: %s, %s öre/kWh
                Högsta pris: %s, %s öre/kWh
                Medelpris: %.2f öre/kWh
                """, lowest.getHour(), lowest.getPrice(), highest.getHour(), highest.getPrice(), average);
    }

    public static void sortPrices() {
        Price[] priceList = Arrays.copyOf(prices, prices.length);
        Arrays.sort(priceList, Comparator.comparingInt(Price::getPrice).reversed());
        for (Price price : priceList) {
            System.out.println(price.getHour() + " " + price.getPrice() + " öre");
        }
    }

    public static void bestChargeTime() {
        int minSum = Integer.MAX_VALUE;
        int bestStartHour = 0;

        // går igenom varje timme som möjligt starttid för 4-timmars blocket
        for (int i = 0; i < prices.length; i++) {
            int currentSum = 0;

            //summerar priserna för varje block.
            for (int j = 0; j < 4; j++) {
                // använder Modulo för att hantera "wrap-around" i arrayen, så om startpunkten är nära slutet
                // så ska arrayens gränser rulla tillbaka till början.
                currentSum += prices[(i + j) % prices.length].getPrice();
            }

            //uppdaterar minsta summan och bästa timmen att start laddningen
            if (currentSum < minSum) {
                minSum = currentSum;
                bestStartHour = i;
            }
        }

        // Räknar ut medelpriset för de fyra bästa timmarna
        float average = minSum / 4.0f;

        // Printar ut resultatet
        System.out.printf("""
                Påbörja laddning klockan %s
                Medelpris 4h: %.1f öre/kWh
                """, prices[bestStartHour].getHour().substring(0, 2), average);
    }
}

