import java.util.*;

/**
 * Class for finding the best anagram of words from a given dictionary.
 *
 * @author Mathew Shields, Alex Lake-Smith
 */
public class Anagrams {

    /** Arraylist of Strings, all the words to find anagrams for. */
    private static ArrayList<String> words = new ArrayList<>();

    /** Arraylist of Strings, the dictionary to make anagrams out of. */
    private static ArrayList<String> dictionary = new ArrayList<>();

    /** Arraylist of Strings, to store the words needed to make the anagram. */
    private static ArrayList<String> output = new ArrayList<>();

    /**
     * Finds the remaining characters when the characters from one string are removed from another.
     *
     * @param Two strings, the words to be compared.
     * @return Null if one string does not contain all the letters of the other, or the remaining letters.
     */
    public static String difference(String word, String dict) {

        if (dict.length() > word.length()) {
            return null;
        }
        StringBuilder difference = new StringBuilder(word);
        char[] x = dict.toCharArray();
        for (char c : x) {
            int i = difference.indexOf("" + c);
            if (i < 0) {
                return null;
            } else {
                difference.deleteCharAt(i);
            }
        }
        return difference.toString();
    }


    /**
     * Recursive method that attempts to find the anagrams of words in the dictionary.
     *
     * @param One string, the word to find an anagram of, and an int, the current index of the dictionary.
     * @return Null if an anagram isn't found, or the word at the current index of the dictionary.
     */
    public static String findAnagrams(String word, int index) {

        String remainder = "", restOfAnagram = "";
        for (int i = index; i < dictionary.size(); i++) {
            if ((remainder = difference(word, dictionary.get(i))) != null) {
                if (!remainder.equals("")) {
                    restOfAnagram = findAnagrams(remainder, i);
                }
                if (restOfAnagram != null) {
                    output.add(dictionary.get(i));
                    return dictionary.get(i);
                }
            }
        }
        return null;
    }

    /**
     * Main method. Reads input and stores it in arraylists. Calls findAnagrams and prints out the output.
     */
    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        String line = "";

        while (!(line = scan.nextLine()).isEmpty()) {
            words.add(line);
        }

        while (scan.hasNextLine()) {
            dictionary.add(scan.nextLine());
        }

        Collections.sort(dictionary, new LengthComparator());

        for (int i = 0; i < words.size(); i++) {
            output.clear();

            findAnagrams(words.get(i), 0);
            System.out.print(words.get(i) + ": ");

            for (int j = output.size() - 1; j >= 0; j--) {
                if (j != 0) {
                    System.out.print(output.get(j) + " ");
                } else {
                    System.out.println(output.get(j));
                }
            }
            if (output.isEmpty()) {
                System.out.println();
            }
        }
    }
}
