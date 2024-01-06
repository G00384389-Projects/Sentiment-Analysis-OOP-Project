package ie.atu.sw;

import java.util.HashMap;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LexiconManager {
    private final HashMap<String, Integer> lexiconMap = new HashMap<>();

    public boolean loadLexicon(String filePath) {
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

    public boolean containsWord(String word) {
        return lexiconMap.containsKey(word);
    }

    public int getScore(String word) {
        return lexiconMap.getOrDefault(word, 0);
    }
}
