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
        String[] newArr = new String[asciis.length];
        System.arraycopy(asciis, 0, newArr, 0, asciis.length);
        sortHelper(newArr, 0, asciis.length, 0);
        return newArr;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     * destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/

    private static void sortHelper(String[] asciis, int start, int end, int index) {
        if (start == end || (start + 1) == end) {
            return;
        }
        int[] newIndices = setNewIndices(index, asciis, start, end);
        reorderArrayOnIndex(index, asciis, newIndices, start, end);

        for (int sortingIndex = 0; sortingIndex < newIndices.length - 1; sortingIndex++) {
            int startIndex = newIndices[sortingIndex];
            int endIndex = newIndices[sortingIndex + 1];
            int maxLength = getMaxStringLength(asciis, startIndex, endIndex);
            if (startIndex != endIndex && maxLength >= (index + 1)) {
                sortHelper(asciis, start + startIndex, start + endIndex, index + 1);
            }
        }
    }

    private static int getMaxStringLength(String[] arr, int start, int end) {
        int max = Integer.MIN_VALUE;
        for (int index = start; index < end; index++) {
            int wordLength = arr[index].length();
            if (wordLength > max) {
                max = wordLength;
            }
        }
        return Math.max(max, 0);
    }

    // returns the ASCII key of the char at letterIndex in word + 1.
    // if there is no char, returns 0.
    private static int getLetterValFromString(int letterIndex, String word) {
        int letterVal;
        try {
            letterVal = (int) word.charAt(letterIndex);
        } catch (IndexOutOfBoundsException e) {
            //System.out.println(word + " doesn't have " + letterIndex + " length.");
            letterVal = 0;
        }
        return letterVal;
    }

    private static int[] setNewIndices(int letterIndex, String[] arr, int start, int end) {
        int[] newIndices = new int[257];
        String word;
        int letterVal;
        // upating the counts of each ASCII value
        for (int wordIndex = start; wordIndex < end; wordIndex++) {
            word = arr[wordIndex];
            letterVal = getLetterValFromString(letterIndex, word);
            newIndices[letterVal]++;
        }
        // changing to the new indices.
        // start at 1 because 0th elem stays the same.
        //System.out.println("\nnew indices are: ");
        for (int index = 1; index < newIndices.length; index++) {
            newIndices[index] += newIndices[index - 1];
            //System.out.print(newIndices[index] + " ");
        }
        //System.out.print("\n");
        return newIndices;
    }

    // also mutates newIndices array.
    private static void reorderArrayOnIndex(int letterIndex, String[] arr, int[] newIndices,
                                            int start, int end) {
        String[] newArr = new String[end - start];
        String word;
        int letterVal;
        int newIndex;
        //System.out.println("the indices are ");
        //print(newIndices);
        // reversing through array so that sorting works.
        for (int index = end - 1; index >= start; index--) {
            word = arr[index];
            letterVal = getLetterValFromString(letterIndex, word);
            newIndex = --newIndices[letterVal];
            newArr[newIndex] = word;
        }
        // fuck me
        System.arraycopy(newArr, 0, arr, start, newArr.length);
    }

    public static void main(String[] args) {
        String[] words = {"neverland", "will", "the", "before", "at", "need", "swindling",
                "weather", "whether", "thread", "house", "tree", "seven", "eleven", "house",
                "can", "peas", "surrender", "tiger", "theft", "honk", "big", "swindle", "meniscus",
                "how", "enunciate", "cheese", "almight", "swear", "hear", "fart", "pass", "class",
                "molasses", "the"};
        for (String word : words) {
            System.out.print(word + " ");
        }
        System.out.println("\n");
        String[] sorted = RadixSort.sort(words);
        System.out.println("\n");
        for (String word : sorted) {
            System.out.print(word + " ");
        }
        System.out.println("\n");
    }

    private static void print(String[] arr) {
        System.out.println("");
        for (Object elem : arr) {
            System.out.print(elem.toString() + " ");
        }
        System.out.println("");
    }

    private static void print(String[] arr, int start, int end) {
        System.out.println("");
        for (int i = start; i < end; i++) {
            System.out.print(arr[i].toString() + " ");
        }
        System.out.println("");
    }

    private static void print(int[] arr) {
        System.out.println("");
        for (Object elem : arr) {
            System.out.print(elem.toString() + " ");
        }
        System.out.println("");
    }
}