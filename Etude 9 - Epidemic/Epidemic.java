import java.util.Scanner;
import java.util.ArrayList;

/**
 * Given a universe containing sick, immune, and normal cells, this program fills in all the cells
 * that can get sick. A cell can get sick if it is not immune, and two of it's adjacent cells are sick.
 */
public class Epidemic {

    private static char[][] universe;
    private static ArrayList<String> temp = new ArrayList<String>();
    private static int length = 0;
    private static boolean topSick;
    private static boolean bottomSick;
    private static boolean leftSick;
    private static boolean rightSick;

    /**
     * Fills the universe based on the input.
     *
     * @param the Scanner object.
     */
    public static void fillUniverse (Scanner scan) {

        String input = "";
        temp.clear();
            
        while (scan.hasNextLine()) {
            input = scan.nextLine();
            if (input.equals("")) {
                break;
            } else {
                temp.add(input);
                length = input.length();
            }
        }
        universe = new char[temp.size()][length];
        for (int i = 0; i < temp.size(); i++) {
            for (int j = 0; j < length; j++) {
                universe[i][j] = temp.get(i).charAt(j);
            }
        }
    }

    /**
     * Main method. Infects all cells that are able to get sick, and prints out the output.
     */
    public static void main (String[] args) {

        Scanner scan = new Scanner(System.in);

        while (scan.hasNextLine()) {

            fillUniverse(scan);
            int k = 0;

            while (k < temp.size() * length) {
                for (int i = 0; i < temp.size(); i++) {
                    for (int j = 0; j < length; j++) {
                        if (universe[i][j] == '.') {
                            topSick = (i != 0) && universe[i - 1][j] == 'S';
                            bottomSick = (i != temp.size() - 1) && universe[i + 1][j] == 'S';
                            leftSick = (j != 0) && universe[i][j - 1] == 'S';
                            rightSick = (j != length - 1) && universe[i][j + 1] == 'S';

                            if (topSick && bottomSick || topSick && leftSick || topSick && rightSick ||
                                bottomSick && leftSick || bottomSick && rightSick || leftSick && rightSick) {
                                universe[i][j] = 'S';
                            }     
                        }
                    }
                }
                k++;
            }

            for (int x = 0; x < temp.size(); x++) {
                for (int y = 0; y < length; y++) {
                    System.out.print(universe[x][y]);
                }
                System.out.print("\n");
            }
            if (scan.hasNextLine()) {
                System.out.println();
            }
        }
    }
}
