package ie.atu.sw;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class TwitterDataManager {
    private final List<String> tweets = new ArrayList<>();
    private LexiconManager lexiconManager;

    public TwitterDataManager(LexiconManager lexiconManager) {
        this.lexiconManager = lexiconManager;
    }

    public boolean loadTwitterData(String filePath) {
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

    public void analyzeSentiment() {
        int totalPositive = 0;
        int totalNegative = 0;
        int totalWords = 0;

        for (String tweet : tweets) {
            int sentimentScore = 0;
            String[] words = tweet.split("\\s+");
            for (String word : words) {
                word = word.toLowerCase(); // Normalize the word
                totalWords++;
                if (lexiconManager.containsWord(word)) {
                    int score = lexiconManager.getScore(word);
                    sentimentScore += score;
                    if (score > 0) {
                        totalPositive++;
                    } else if (score < 0) {
                        totalNegative++;
                    }
                }
            }
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
            System.out.println("Tweet: " + tweet);
            System.out.println(ConsoleColour.BLUE_BOLD);
            System.out.println("Sentiment Score: " + sentimentScore);
            System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
        }
        System.out.println(ConsoleColour.BLUE_BOLD);
        System.out.println("Total Positive Words: " + totalPositive);
        System.out.println("Total Negative Words: " + totalNegative);
        double scoreFromTotal = (double) (totalPositive - totalNegative) / totalWords;
        System.out.println("Score from Total (SfT): " + scoreFromTotal);
        System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
    }
}
