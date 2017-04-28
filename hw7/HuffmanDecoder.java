import java.util.ArrayList;

/**
 * Created by kevin on 4/28/2017.
 */
public class HuffmanDecoder {
    public static void main(String[] args) {
        String encodedFile = args[0];
        String decodedFile = args[1];
        //1
        ObjectReader or = new ObjectReader(encodedFile);
        Object trie = or.readObject();
        //3
        Object bits = or.readObject();
        //4
        ArrayList<Match> prefixes = new ArrayList<>();
        while (((BitSequence) bits).length() > 0) {
            //4a
            Match m = ((BinaryTrie) trie).longestPrefixMatch((BitSequence) bits);
            //4b
            prefixes.add(m);
            //4c
            bits = ((BitSequence) bits).allButFirstNBits(m.getSequence().length());
        }

        ObjectWriter ow = new ObjectWriter(decodedFile);
        ow.writeObject(prefixes);
    }
}
