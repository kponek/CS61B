import org.junit.Test;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 **/
public class RadixSort {

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     **/
    public static String[] sort(String[] asciis) {
        String[] res = new String[asciis.length];
        sortHelper(asciis, 0, asciis.length - 1, 0, res);
//        for(int i = 0; i < asciis.length; i++){
//            asciis[i] = res[i];
//        }
        return asciis;

    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     * destructive method that changes the passed in array, asciis
     * <p>
     * //
     **/


    private static String printString(String[] arr) {
        String res = "";
        for (String s : arr) {
            res += s + ", ";
        }
//        System.out.println(res);
        return res;
    }

    private static String printArray(int[] arr, int start, int end) {
        String res = "";
        for (int i = start; i < end; i++) {
            res += "i: " + i + " is " + Integer.toString(arr[i]) + ", ";
        }
//        System.out.println(res);
        return res;
    }

    private static boolean indexAlwaysGreater(String[] arr, int ind) {
        for (String s : arr) {
            if (s.length() > ind) {
                return false;
            }
        }
        return true;
    }

    private static void sortHelper(String[] asciis, int start, int end, int index, String[] aux) {
        if (end - start <= 1) {
            return;
        } else {
            int[] count = new int[256 + 2];
            for (int i = start; i <= end; i++) {
                int c = charAt(asciis[i], index);
                count[c + 2]++;
            }

            // transform counts to indicies
            for (int r = 0; r < 256 + 1; r++)
                count[r + 1] += count[r];

            // distribute
            for (int i = start; i <= end; i++) {
                int c = charAt(asciis[i], index);
                aux[count[c + 1]++] = asciis[i];
            }

            // copy back
            for (int i = start; i <= end; i++)
                asciis[i] = aux[i - start];


            // recursively sort for each character (excludes sentinel -1)
            for (int r = 0; r < 256; r++)
                sortHelper(asciis, start + count[r], start + count[r + 1] - 1, index + 1, aux);
        }
    }

    private static int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d >= s.length()) return -1;
        return s.charAt(d);
    }

}
