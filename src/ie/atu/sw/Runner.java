package ie.atu.sw;

import java.util.Scanner;
import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Runner {
    private static HashMap<String, Integer> lexiconMap = new HashMap<>();
    private static List<String> tweets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Load Lexicons");
            System.out.println("2. Load Twitter Data");
            System.out.println("3. Analyze Sentiment");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("Enter path to lexicon file:");
                    String lexiconPath = scanner.next();
                    loadLexicon(lexiconPath);
                    break;
                case 2:
                    System.out.println("Enter path to Twitter data file:");
                    String twitterPath = scanner.next();
                    loadTwitterData(twitterPath);
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

    private static void loadLexicon(String filePath) {
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(",");
                String word = parts[0];
                int score = Integer.parseInt(parts[1]);
                lexiconMap.put(word, score);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (Exception e) {
            System.out.println("Error reading file: " + filePath);
        }
    }

    private static void loadTwitterData(String filePath) {
        try (Scanner fileScanner = new Scanner(new File(filePath))) {
            while (fileScanner.hasNextLine()) {
                String tweet = fileScanner.nextLine();
                tweets.add(tweet);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
        } catch (Exception e) {
            System.out.println("Error reading file: " + filePath);
        }
    }

    private static void analyzeSentiment() {
        for (String tweet : tweets) {
            int sentimentScore = 0;
            String[] words = tweet.split("\\s+");
            for (String word : words) {
                word = word.toLowerCase(); // normalize the word
                if (lexiconMap.containsKey(word)) {
                    sentimentScore += lexiconMap.get(word);
                }
            }
            System.out.println("Tweet: " + tweet);
            System.out.println("Sentiment Score: " + sentimentScore);
        }
    }
}
