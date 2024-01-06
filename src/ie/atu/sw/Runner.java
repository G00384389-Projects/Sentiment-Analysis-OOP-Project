package ie.atu.sw;

import java.util.Scanner;
import java.util.concurrent.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Runner {
    private static final HashMap<String, Integer> lexiconMap = new HashMap<>();
    private static final List<String> tweets = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {

        	//	output menu
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
    		
    		//	prompt user to respond
    		System.out.print(ConsoleColour.BLACK_BOLD_BRIGHT);
    		System.out.print("Select Option [1-4]>");
    		System.out.println();
        	
            int choice = scanner.nextInt();
            scanner.nextLine(); // read in scanner line

            //	switch statement for menu options
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

    //	boolean for loading in lexicons from hashmap and initialize score, word, parts and line
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
            //	catch statements for if issue with reading file 
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filePath);
            return false;
            //  catch statements for if file not found
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
        int totalPositive = 0;
        int totalNegative = 0;
        int totalWords = 0;

        for (String tweet : tweets) {
            int sentimentScore = 0;
            String[] words = tweet.split("\\s+");
            for (String word : words) {
                word = word.toLowerCase(); // Normalize the word
                totalWords++;
                if (lexiconMap.containsKey(word)) {
                    int score = lexiconMap.get(word);
                    sentimentScore += score;
                    if (score > 0) {
                        totalPositive++;
                    } else if (score < 0) {
                        totalNegative++;
                    }
                }
            }
            System.out.println("Tweet: " + tweet);
            System.out.println("Sentiment Score: " + sentimentScore);
        }

        System.out.println("Total Positive Words: " + totalPositive);
        System.out.println("Total Negative Words: " + totalNegative);
        double scoreFromTotal = (double) (totalPositive - totalNegative) / totalWords;
        System.out.println("Score from Total (SfT): " + scoreFromTotal);
    }
}
