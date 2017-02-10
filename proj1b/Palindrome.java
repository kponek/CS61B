/**
 * Created by kevin on 2/9/2017.
 */
public class Palindrome {
    /**
     * Builds a Deque where characters in the deque appear
     * in the smae order as in the word
     */
    public static Deque<Character> wordToDeque(String word) {
        ArrayDeque<Character> wordDeque = new ArrayDeque<Character>();
        while (word.length() > 0) {
            wordDeque.addFirst(word.charAt(0));
            word = word.substring(1, word.length() - 1);
        }
        return wordDeque;
    }

    /**
     * Returns true if the given word is a palindrome, false otherwise.
     * A palindrome is a word that is the same forwards and backwards.
     */
    public static boolean isPalindrome(String word) {
        if (word.length() <= 1) {
            return true;
        }
        if (word.charAt(0) != word.charAt(word.length() - 1)) {
            return false;
        }
        return isPalindrome(word.substring(1, word.length() - 1));
    }

    public static boolean isPalindrome(String word, CharacterComparator cc) {
        if (word.length() <= 1) {
            return true;
        }
        if (!cc.equalChars(word.charAt(0), word.charAt(word.length() - 1))) {
            return false;
        }
        return isPalindrome(word.substring(1, word.length() - 1), cc);
    }
}
