/**
Comparator class for Anagrams.java.

Sorts the dictionary by length in descending order, and then by alphabetical order.

@author Mathew Shields, Alex Lake-Smith
**/

import java.util.Comparator;

public class LengthComparator implements Comparator<String> {
    @Override
    public int compare (String s1, String s2) {
        if (s1.length() != s2.length()) {
            return s2.length() - s1.length();
        }
        return s1.compareTo(s2);
    }
}
