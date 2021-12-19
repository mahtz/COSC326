import java.util.ArrayList;
import java.util.Scanner;
import java.util.ListIterator;
import java.util.Collections;

/**
 * Finds the winner of a series of voting rounds. For each round the number of votes for each
 * candidate are counted and the candidate with the lowest number of votes is eliminated, until
 * a winner is found.
 * 
 * @author Mathew Shields
 * 
 */
public class Pref {

    private static ArrayList<String> list = new ArrayList<>();
    private static ArrayList<Integer> lineLengths = new ArrayList<>();
    private static ArrayList<String> storeCounts = new ArrayList<>();
    private static ArrayList<String> allRounds = new ArrayList<>();
    private static ArrayList<String> compareVotes = new ArrayList<>();
    private static ArrayList<String> sortedCandidates = new ArrayList<>();
    private static String eliminated = "";
    private static String winner = ""; 

    public static void getInput(Scanner scan) {

        String line = "";
        String[] words;
        while (scan.hasNextLine()) {
            int i = 0;
            line = scan.nextLine();
            line = line.replace('.', ' ').trim().replaceAll("\\s{2,}", " ");
            if (line.length() == 0) {
                continue;
            }
            words = line.split(" ");
            for (i = 0; i < words.length; i++) {
                list.add(words[i]);
            }
            lineLengths.add(i);
        }     
    }

    public static void fillStoreCounts() {

        try {
            int pos, index, lineIncr;
            boolean newVoter;
            Integer temp = 0;
            storeCounts.add(list.get(0));
            storeCounts.add("1");
            lineIncr = 1;
            for (int j = lineLengths.get(0); j < list.size(); j += lineLengths.get(lineIncr++)) {
                newVoter = true;
                for (String s : storeCounts) {                
                    if (list.get(j).equals(s)) {
                        index = storeCounts.indexOf(s);
                        temp = Integer.parseInt(storeCounts.get(index + 1));
                        temp++;
                        storeCounts.set(index + 1, temp.toString());
                        newVoter = false;
                        break;
                    }
                }
                if (newVoter) {
                    storeCounts.add(list.get(j));
                    storeCounts.add("1");
                }
            }
            for (int j = 1; j < list.size(); j++) {
                if (!storeCounts.contains(list.get(j))) {
                    storeCounts.add(list.get(j));
                    storeCounts.add("0");
                }
            }
        } catch (Exception e) {
            System.out.println("No input");
            System.exit(0);
        }
    }

    public static Integer getMaxLength(Integer maxLength) {

        for (int i = 0; i < storeCounts.size(); i += 2) {
            if (maxLength < storeCounts.get(i).length()) {
                maxLength = storeCounts.get(i).length();
            }
        }
        return maxLength;
    }

    public static int[] mostVotes() {

        int temp = -1;
        int sum = 0;
        int index = 0;
        for (int i = 1; i < storeCounts.size(); i += 2) {
            sum += Integer.parseInt(storeCounts.get(i));
            if (temp < Integer.parseInt(storeCounts.get(i))) {
                temp = Integer.parseInt(storeCounts.get(i));
                index = i;
            }
        }
        int[] values = {temp, sum, index};
        return values;
    }
       
    public static void main(String[] args) {      

        Scanner scan = new Scanner(System.in);
        getInput(scan);
        
        int numCandidates = 1;
        boolean printRoundFlag, numCandidatesFlag = true;

        for (int numRounds = 0; numRounds < numCandidates; numRounds++) {  

            fillStoreCounts();

            int[] values = mostVotes();
            winner = storeCounts.get(values[2] - 1);

            if (numCandidatesFlag) {
                numCandidates = storeCounts.size()/2;
                numCandidatesFlag = false;
            }
            
            Integer maxLength = 0;
            maxLength = getMaxLength(maxLength);
                    
            for (int i = 0; i < storeCounts.size(); i++) {
                allRounds.add(storeCounts.get(i));
            }

            int numRemaining = 0;
            int round = numRounds + 1;
            int count = 0;
            int tempMin = Integer.parseInt(storeCounts.get(1));
            ArrayList<String> lowestVotes = new ArrayList<>();

            for (int i = 1; i < storeCounts.size(); i += 2) {
                if (tempMin > Integer.parseInt(storeCounts.get(i))) {
                    tempMin = Integer.parseInt(storeCounts.get(i));
                }
            }
            for (int i = 1; i < storeCounts.size(); i += 2) {
                if (Integer.parseInt(storeCounts.get(i)) == tempMin) {
                    count++;
                    lowestVotes.add(storeCounts.get(i - 1));
                }
            }

            int maxSizeOfStoreCounts = storeCounts.size();
            int padding = 0;
            
            printRoundFlag = true;
            while (storeCounts.size() != 0) {
                boolean wasATie = false;
                int tempMax = -1;
                int order = 0;
                for (int i = 1; i < storeCounts.size(); i += 2) {
                    if (tempMax < Integer.parseInt(storeCounts.get(i))) {
                        tempMax = Integer.parseInt(storeCounts.get(i));
                        order = i;
                    }  
                }

                int countTies = 0;
                for (int i = 1; i < storeCounts.size(); i += 2) {
                    if (tempMax == Integer.parseInt(storeCounts.get(i))) {
                        countTies++;
                    }
                }

                padding = 3 + maxLength - storeCounts.get(order - 1).length();
                
                if (countTies > 1) {
                    for (int i = 0; i < storeCounts.size(); i += 2) {
                        if (tempMax == Integer.parseInt(storeCounts.get(i + 1))) {
                            sortedCandidates.add(storeCounts.get(i));
                            storeCounts.remove(i + 1);
                            storeCounts.remove(i);
                            i -= 2;
                        }
                    }
                }
                Collections.sort(sortedCandidates);
                
                if (printRoundFlag) {
                    System.out.println("Round " + round);
                    printRoundFlag = false;
                }
    
                while (sortedCandidates.size() != 0) {
                    padding = 3 + maxLength - sortedCandidates.get(0).length();
                    System.out.printf("%s%" + padding + "c%-10s%n", sortedCandidates.get(0), ' ', tempMax);
                    numRemaining++;
                    sortedCandidates.remove(0);
                    wasATie = true;
                }                                 

                if (count == 1) {           
                    if (storeCounts.size() == 2) {
                        eliminated = storeCounts.get(0);
                    }
                }
                
                if (!wasATie) {
                    System.out.printf("%s%" + padding + "c%-10s%n", storeCounts.get(order - 1), ' ', tempMax);
                    numRemaining++;
                    storeCounts.remove(order);
                    storeCounts.remove(order - 1);
                }
                
                compareVotes.clear();
                if (count > 1 && storeCounts.size() == 0) {
                    for (int i = allRounds.size() - 2; i >= 0; i -= 2) {
                        for (int j = 0; j < lowestVotes.size(); j++) {
                            if (allRounds.get(i).equals(lowestVotes.get(j))) {
                                compareVotes.add(allRounds.get(i));
                                compareVotes.add(allRounds.get(i + 1));          
                            }
                        }
                    }
                }
                sortedCandidates.clear();
            }
            
            if (values[0] / (double) values[1] > 0.5) {
                System.out.println("Winner: " + winner);
                break;
            }

            int track = 0, trackMax = 0, indexTrackMax = 0;

            tieBreaker:
            for (int i = 1; i < compareVotes.size(); i += 2 * count) {
                track = Integer.parseInt(compareVotes.get(i));
                trackMax = track;
                for (int j = i; j < i + 2 * count; j += 2) {
                    if (track > Integer.parseInt(compareVotes.get(j))) {
                        eliminated = compareVotes.get(j - 1);
                        track = Integer.parseInt(compareVotes.get(j));
                        if (count == 2) {
                            break tieBreaker;
                        }
                    }
                    if (trackMax < Integer.parseInt(compareVotes.get(j))) {
                        trackMax = Integer.parseInt(compareVotes.get(j));
                        String name = compareVotes.get(j - 1);
                        if (trackMax > track) {
                            for (int k = j; k < compareVotes.size(); k += 2) {
                                if (compareVotes.get(k - 1).equals(name)) {
                                    compareVotes.remove(k);
                                    compareVotes.remove(k - 1);
                                    k -= 2;
                                }
                            }
                            count--;
                        }
                    }
                }
                if (count == 1) {
                    eliminated = compareVotes.get(i - 1);
                    break;
                } 
            }
            
            if (count > 1 && eliminated.equals("")) {
                System.out.println("Unbreakable tie");
                break;
            }
        
            System.out.println("Eliminated: " + eliminated + "\n");

            int sum, x = 0;
            ListIterator<String> iter = list.listIterator();
            ArrayList<Integer> lineLengthsCopy = new ArrayList<Integer>(lineLengths);
            while (iter.hasNext()) {
                sum = 0;
                if (iter.next().equals(eliminated)) {
                    int y = lineLengths.size();
                    for (int i = 0; i < y; i++) {
                        sum += lineLengths.get(i);
                        if (x < sum) {
                            Integer value = lineLengths.get(i);
                            value--;
                            lineLengthsCopy.set(i, value);
                            break;
                        }
                    }
                    iter.remove();
                }
                x++;
            }
            for (int i = 0; i < lineLengthsCopy.size(); i++) {
                if (lineLengthsCopy.get(i) == 0) {
                    lineLengthsCopy.remove(i);
                    i--;
                }
            }
            eliminated = "";
            lineLengths = lineLengthsCopy;
        }
    }
}
