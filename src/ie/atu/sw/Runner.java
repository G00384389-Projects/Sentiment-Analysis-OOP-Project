package ie.atu.sw;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.io.File;
import java.io.FileNotFoundException;

public class Runner {
    private static final HashMap<String, Integer> lexiconMap = new HashMap<>();
    private static final List<String> tweets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Load Lexicons");
            System.out.println("2. Load Twitter Data");
            System.out.println("3. Analyze Sentiment");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over

            switch (choice) {
                case 1:
                    System.out.println("Enter path to lexicon file:");
                    String lexiconPath = scanner.nextLine();
                    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                        scope.fork(() -> loadLexicon(lexiconPath));
                        scope.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                case 2:
                    System.out.println("Enter path to Twitter data file:");
                    String twitterPath = scanner.nextLine();
                    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
                        scope.fork(() -> loadTwitterData(twitterPath));
                        scope.join();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    break;
                case 3:
                    analyzeSentiment();
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

    private static Boolean loadLexicon(String filePath) {
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String word = parts[0].toLowerCase();
                int score = Integer.parseInt(parts[1]);
                lexiconMap.put(word, score);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return false;
        } catch (Exception e) {
            System.out.println("Error reading file: " + filePath);
            return false;
        }
    }

    private static Boolean loadTwitterData(String filePath) {
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                String tweet = fileScanner.nextLine();
                tweets.add(tweet);
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return false;
        } catch (Exception e) {
            System.out.println("Error reading file: " + filePath);
            return false;
        }
    }

    private static void analyzeSentiment() {
        for (String tweet : tweets) {
            int sentimentScore = 0;
            String[] words = tweet.split("\\s+");
            for (String word : words) {
                word = word.toLowerCase(); // Normalize the word
                if (lexiconMap.containsKey(word)) {
                    sentimentScore += lexiconMap.get(word);
                }
            }
            System.out.println("Tweet: " + tweet);
            System.out.println("Sentiment Score: " + sentimentScore);
        }
    }
}
