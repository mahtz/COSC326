import java.util.*;

/**
 * A program which finds the correct results of a series of games, if possible, from a given, incorrect
 * set of game results.
 */
public class FixGames {

    /** Stores all tables as the program finds the valid solutions. */
    private static ArrayList<String[][]> allGames = new ArrayList<>();

    /** Stores all valid solutions. */
    private static ArrayList<String[][]> solutions = new ArrayList<>();
    
    /**
     * Checks that the input table is of valid format and contains valid values.
     *
     * @param a row of the input table, split into a string array, and the number of rows.
     */
    public static void checkValues(String[] splitInput, int numRows) {

        for (int i = 0; i < splitInput.length; i++) {      
            try {
                if (Integer.parseInt(splitInput[i]) > numRows || Integer.parseInt(splitInput[i]) < 1) {
                    System.out.println("Bad values");
                    System.exit(0);
                }
            } catch(Exception e) {
                System.out.println("Bad format");
                System.exit(0);
            }
        }
    }

    /**
     * Prints outs a table.
     *
     * @param the table to be printed.
     */
    public static void print(String[][] table) {

        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /**
     * Makes a copy of a table.
     *
     * @param the table to be copied.
     * @return 2D array of strings, which is a copy of the original.
     */
    public static String[][] copy(String[][] table) {

        String copy[][] = new String[table.length][table.length];
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table.length; j++) {
                copy[i][j] = table[i][j];
            }
        }
        return copy;
    }

    /**
     * Checks that a row of the table is possible.
     * 
     * @param the table, and the index of the row to be checked.
     * @return true if possible, false otherwise. 
     */
    public static boolean checkPossibleRow(String[][] table, int x) {

        int impossibleRow = 0;
        for (int y = 0; y < table[x].length; y++) {
            for (int z = y + 1; z < table[x].length; z++) {
                if (table[x][y].equals(table[x][z])) {
                    impossibleRow++;
                    if (impossibleRow != 1) {
                        return false;
                    }
                }
            }
        } 
        return true;
    }

    /**
     * Counts how many branches can be made from a row. 
     * 
     * @param the table, and index of the row to be checked.
     * @return the number of possible branches.
     */
    public static int numBranches(String[][] table, int x) {

        int count = 0;
        push: 
        for (int y = 0; y < table[x].length; y++) {
            for (int z = y + 1; z < table[x].length; z++) {
                if (table[x][y].equals(table[x][z])) {
                    if (table[table.length - 1][y].equals("0")) {
                        count++;
                    }
                    if (table[table.length - 1][z].equals("0")) {
                        count++;
                    }
                    break push;
                }
            }
        }
        return count;
    }

    /**
     * Pushes the left duplicate element and all subsequent elements of that column down one row.
     *
     * @param the table, and index of the row to be pushed.
     * @return the new, pushed table.
     */             
    public static String[][] pushLeft(String[][] table, int x) {
        
        String temp = "0";
        push:
        for (int y = 0; y < table[x].length; y++) {
            for (int z = y + 1; z < table[x].length; z++) {
                if (table[x][y].equals(table[x][z])) {
                    if (table[table.length - 1][y].equals("0")) {
                        for (int q = table.length - 1; q > x; q--) {
                            temp = table[q - 1][y];
                            table[q][y] = temp;
                        }
                        table[x][y] = "_";
                        break push;
                    }
                }
            }
        }
        return table;
    }

    /**
     * Pushes the right duplicate element and all subsequent elements of that column down one row.
     *
     * @param the table, and index of the row to be pushed.
     * @return the new, pushed table.
     */ 
    public static String[][] pushRight(String[][] table, int x) {

        String temp = "0";
        push:
        for (int y = 0; y < table[x].length; y++) {
            for (int z = y + 1; z < table[x].length; z++) {
                if (table[x][y].equals(table[x][z])) {
                    if (table[table.length - 1][z].equals("0")) {
                        for (int q = table.length - 1; q > x; q--) {
                            temp = table[q - 1][z];
                            table[q][z] = temp;
                        }
                        table[x][z] = "_";
                        break push;
                    }
                }
            }
        }
        return table;
    }

    /**
     * Checks where two tables are the same.
     *
     * @param two tables to be compared.
     * @return true if the tables are equal, false otherwise.
     */
    public static boolean areEqual(String[][] original, String[][] afterPush) {

        for (int i = 0; i < original.length; i++) {
            if (!Arrays.equals(original[i], afterPush[i])) {
                
                return false;
            }           
        }
        return true;      
    } 

    /**
     * Deletes an impossible table from allGames.
     * 
     * @param the index of the table.
     * @return the index decremented by one.
     */
    public static int delete(int index) {

        allGames.remove(index);
        return --index;
    }

    /**
     * Finds the possible solutions, if any, of the input table, by using the other methods above.
     * 
     * @param the input table.
     */
    public static void fixGames(String[][] table) {

        int index = 0, count = 0;
        String[][] duplicate = new String[table.length][table.length];
        ArrayList<Integer> branches = new ArrayList<>();

        branches.add(index);
        branches.add(numBranches(allGames.get(index), index));
        branches.add(0);
        try {
            while (index < table.length) {
                
                if (index != 0 && count == 0) {
                    branches.add(index);
                    branches.add(numBranches(allGames.get(index), index));
                    branches.add(0);
                }
                count = 0;

                //print(allGames.get(index));
                //System.out.println(branches);
                
                if (!checkPossibleRow(allGames.get(index), index)) {
                    
                    int i = branches.size() - 2;

                    index = delete(index);
                    branches.remove(i + 1);
                    branches.remove(i);
                    branches.remove(i - 1);
                    i -= 3;
                    
                    while (branches.get(i) != 2 || branches.get(i + 1) == 1) {
                        index = delete(index);
                        branches.remove(i + 1);
                        branches.remove(i);
                        branches.remove(i - 1);
                        i -= 3;
                        if (i < 1) {
                            if (solutions.isEmpty()) {
                                System.out.println("Inconsistent results");
                                System.exit(0);
                            } else {
                                return;
                            }
                        }
                    }                    
                    branches.set(i + 1, 1);
                }
                
                duplicate = copy(allGames.get(index));
                
                if (!areEqual(duplicate, pushLeft(copy(allGames.get(index)), index))
                    && branches.get(3 * index + 2) == 0) {
                    allGames.add(pushLeft(copy(allGames.get(index)), index));
                    index++;
                    continue;
                } 
                
                if (!areEqual(duplicate, pushRight(copy(allGames.get(index)), index))) { 
                    allGames.add(pushRight(copy(allGames.get(index)), index));
                    index++;
                    continue;
                }
                int numZeroes = 0;
                int indexOfZero = -1;
                for (int z = 0; z < allGames.get(index)[index].length; z++) {
                    if (allGames.get(index)[index][z].equals("0")) {
                        ++numZeroes;
                        if (numZeroes == 1) {
                            indexOfZero = z;
                        }
                    }
                }
                if (numZeroes == 1) {
                    allGames.add(copy(allGames.get(index)));
                    allGames.get(index + 1)[index][indexOfZero] = "_";
                    solutions.add(copy(allGames.get(index + 1)));
                    allGames.remove(index + 1);
                }
                
                count = 0;
                for (int i = branches.size() - 2; i >= 0; i -= 3) {
                    if (branches.get(i) == 2 && branches.get(i + 1) == 0) {
                        count++;
                    }
                }
                
                if (count > 0) {
                    int i = 0;
                    for (i = branches.size() - 2; i > 0; i -= 3) {
                        if (branches.get(i) == 2 && branches.get(i + 1) == 0) {
                            break;
                        }
                        branches.remove(i + 1);
                        branches.remove(i);
                        branches.remove(i - 1);
                        index = delete(index);
                    }
                    branches.set(i + 1, 1);
                }
                if (count == 0) {
                    index++;
                }
            }
        } catch (Exception e) {
            System.out.println("Inconsistent results");
            System.exit(0);
        }
    }

    /**
     * Finds differences between all valid solutions to deduce which contain the same games, just stored 
     * in a different order.
     *
     * @param all valid solutions.
     * @return the number of unique solutions.
      */
    public static int findDifferences(ArrayList<String[][]> solutions) {

        ArrayList<String[][]> sorted = new ArrayList<>();
        ArrayList<String[][]> uniqueSolutions = new ArrayList<>();
        int length = solutions.get(0)[0].length;
        String[][] copy = new String[length][];
        int count = 1;
        
        for (int i = 0; i < solutions.size(); i++) {
            int x = 0;
            for (int j = 0; j < length; j++) {
                if (solutions.get(i)[j][x].equals("_")) {
                    copy[x] = solutions.get(i)[j];
                    ++x;
                    if (x == length) {
                        break;
                    } else {
                        j = -1;
                    }
                }
            }
            sorted.add(copy(copy));
        }
        
        uniqueSolutions.add(copy(sorted.get(0)));

        boolean differentSolution;
        for (int i = 1; i < sorted.size(); i++) {
            differentSolution = true;
            for (int j = 0; j < uniqueSolutions.size(); j++) {
                if (areEqual(sorted.get(i), uniqueSolutions.get(j))) {
                    differentSolution = false;
                    break;
                } else {
                    differentSolution = true;
                }
            }
            if (differentSolution) {
                uniqueSolutions.add(copy(sorted.get(i)));
                count++;
            }
        }
        return count;
    }
    
    /**
     * Main method. Reads in the input and formats it appropriately, then calls fixGames.
     * Also prints the output in the correct format.
     */
    public static void main (String[] args) {
           
        Scanner scan = new Scanner(System.in);
        String input = scan.nextLine();
        String[] splitInput = input.split(" ");

        int numColumns = splitInput.length;
        int numRows = numColumns - 1;

        checkValues(splitInput, numRows);

        String[][] inputTable = new String[numColumns][numColumns];

        for (int x = 0; x < numRows; x++) {
            for (int y = 0; y < numColumns; y++) {
                inputTable[x][y] = splitInput[y];
                if (x == numRows - 1) {                   
                    inputTable[x + 1][y] = "0";
                }
            }
            if (scan.hasNextLine()) {
                input = scan.nextLine();
                if (input.split(" ").length != numColumns || x == numRows - 1) {
                    System.out.println("Bad format");
                    System.exit(0);
                }
                splitInput = input.split(" ");
                checkValues(splitInput, numRows);
            } else if (!scan.hasNextLine() && x != numRows - 1) {
                System.out.println("Bad format");
                System.exit(0);
            }
        }
        
        allGames.add(inputTable);

        fixGames(inputTable);
        
        for (int i = 0; i < solutions.size(); i++) {
            print(solutions.get(i));
        }

        System.out.println("Different results: " + findDifferences(solutions));
    }
}
