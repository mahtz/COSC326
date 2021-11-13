import java.util.Scanner;
import java.util.Arrays;

/**
 * Deduces whether a given input string is considered a "woof" or not.
 */
public class Woof {

    /**
     * Checks if the input string contains all valid characters.
     *
     * @param the string to be checked.
     * @return true if valid, false otherwise.
     */
    public static boolean isValid(char[] line) {
        
        boolean valid = true;
        for (char c : line) {
            valid = ((c >= 'p') && (c <= 's') || (c == 'N') || (c == 'C') ||
                     (c == 'A') || (c == 'K') || (c == 'E'));
            if (!valid) {
                break;
            }
        }
        return valid;
    }

    /**
     * Checks if a character is a woof character.
     * 
     * @param the character to be checked.
     * @return true if a woof, false if not.
     */
    public static boolean isNorm(char c) {
    
        boolean woof = true;
        woof = ((c >= 'p') && (c <= 's'));      
        return woof;
    }
    
    /**
     * Checks if a character is a leader character.
     * 
     * @param the character to be checked.
     * @return true if a leader, false if not.
     */
    public static boolean isLeader(char c) {
        
        boolean leader = true;
        leader = ((c == 'C') || (c == 'A') || (c == 'K') || (c == 'E'));
        return leader;
    }

    /**
     * Checks if a character is an N character.
     * 
     * @param the character to be checked.
     * @return true if an N, false if not.
     */
    public static boolean isN(char c) {
        
        boolean n = true;
        n = (c == 'N');
        return n;
    }

    /** 
     * Main method. Reads in the input, and for each line deduces whether the line is a woof.
     */
    public static void main(String[] args) {

        String input = "";
        int woofCount, i = 0;
        char[] mainLine;
        Scanner scan = new Scanner(System.in);
        
        while (scan.hasNextLine()) {
            input = scan.nextLine();
            if (input.equals("")) {
                System.out.println("not woof");
                continue;
            }
            
            mainLine = input.toCharArray();
            woofCount = 1;

            if (!(isValid(mainLine))) {
                System.out.println("not woof");
                continue;
            }
            
            for (i = 0; i < mainLine.length; i++) {
                if (isNorm(mainLine[i])) {
                    woofCount -= 1;
                } else if (isN(mainLine[i])) {
                    continue;
                } else if (isLeader(mainLine[i])) {
                    woofCount += 1;
                }
                if (woofCount <= 0) {
                    break;
                }
            }
            if (woofCount == 0 && i != mainLine.length - 1) {
                System.out.println("not woof");
                continue;
            }
            if (woofCount == 0) {
                System.out.println("woof");
            } else {
                System.out.println("not woof");
            }
        }
    }
}
