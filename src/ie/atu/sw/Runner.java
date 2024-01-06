package ie.atu.sw;

import java.util.Scanner;
import java.util.concurrent.*;

public class Runner {
    private static final LexiconManager lexiconManager = new LexiconManager();
    private static final TwitterDataManager twitterDataManager = new TwitterDataManager(lexiconManager);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println(ConsoleColour.RED_BOLD);
            System.out.println("************************************************************");
            System.out.println("*     ATU - Dept. of Computer Science & Applied Physics    *");
            System.out.println("*                                                          *");
            System.out.println("*             Virtual Threaded Sentiment Analyser          *");
            System.out.println("*                                                          *");
            System.out.println("************************************************************");
            System.out.println("(1) Specify Lexicon path");
            System.out.println("(2) Specify Twitter Data path");
            System.out.println("(3) Analyze Sentiment");
            System.out.println("(4) Quit");

            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.print("Select Option [1-4]>");
            System.out.println();

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter path to lexicon file:");
                    String lexiconPath = scanner.nextLine();
                    lexiconManager.loadLexicon(lexiconPath);
                    break;
                case 2:
                    System.out.println("Enter path to Twitter data file:");
                    String twitterPath = scanner.nextLine();
                    twitterDataManager.loadTwitterData(twitterPath);
                    break;
                case 3:
                    twitterDataManager.analyzeSentiment();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 4.");
            }
        }
    }
}
