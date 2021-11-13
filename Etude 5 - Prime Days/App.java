import java.util.ArrayList;
import java.lang.Math;

/**
 * Program which deduces whether a day is truly prime. This means that it has a prime date, and falls in a prime month 
 * and a prime year.
 */
public class App {

    /**
     * Tests if a number is prime.
     *
     * @param a number to be tested.
     * @return true if prime, false otherwise.
     */
    public static boolean isPrime(int n) {

        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if ((n % i) == 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Main method. Gets input from the command line and checks for truly prime days. If any are found,
     * they are outputted.
     *
     * @param args, a number of months of varying lengths.
     */
    public static void main(String[] args) {

        int yearLength = 0;
        int monthLength = 0;

        ArrayList<Integer> input = new ArrayList<Integer>();
     
        for (int x = 0; x < args.length; x++) {
            input.add(Integer.parseInt(args[x]));
            yearLength += Integer.parseInt(args[x]);
        }

        int dayOfYear = input.get(0);
        
        for (int i = 1; i < input.size(); i++) {
            for (int j = 1; j <= input.get(i); j++) {              
                dayOfYear++;              
                if (isPrime(i + 1)) {
                    if (isPrime(j)) {
                        if (isPrime(dayOfYear)) {
                            System.out.println(dayOfYear + ": " + (i + 1) + " " + j);
                        }
                    }
                }
            }
        }
    }
}
