import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kevin on 4/28/2017.
 */
public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new HashMap<>();
        for (char c : inputSymbols) {
            if (map.containsKey(c)) {
                map.put(c, map.get(c) + 1);
            } else {
                map.put(c, 1);
            }
        }
        return map;
    }

    public static void main(String[] args) {
        String filename = "watermelonsugar.txt";
        //1
        char[] chars = FileUtils.readFile(filename);
        //2
        Map<Character, Integer> frequencies = buildFrequencyTable(chars);
        //3
        BinaryTrie trie = new BinaryTrie(frequencies);
        //4
        ObjectWriter trieWrite = new ObjectWriter(filename + ".huf");
        trieWrite.writeObject(trie);
        //6
        Map<Character, BitSequence> lookTable = trie.buildLookupTable();
        //7
        ArrayList<BitSequence> bitSequences = new ArrayList<>();
        /*for (BitSequence b: lookTable.values()) {
            bitSequences.add(b);
        }*/
        //8
        for (char c : chars) {
            //if (c != ' ') {
            bitSequences.add(lookTable.get(c));
            //}
        }
        //9
        BitSequence bits = BitSequence.assemble(bitSequences);
        //10
        trieWrite.writeObject(bits);
    }
}
