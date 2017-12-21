import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> freqTable = new HashMap<>();
        char prev = inputSymbols[0];
        char curr;
        int count = 0;

        for (char c : inputSymbols) {
           curr = c;
           if (curr == prev) {
               count++;
           } else {
               freqTable.put(prev, count);
               prev = curr;
               count = 1;
           }
        }

        return freqTable;
    }

    public static void main(String[] args) {
        char[] inputSymbols = FileUtils.readFile(args[0]);  // Read the file as 8 bit symbols.
        Map<Character, Integer> freqTable = buildFrequencyTable(inputSymbols);  // Build frequency table.
        BinaryTrie biTrie = new BinaryTrie(freqTable);  // Use frequency table to construct a binary decoding trie.
        ObjectWriter ow = new ObjectWriter(args[0] + ".huf");  // Write the binary decoding trie to the .huf file.
        ow.writeObject(biTrie);
        ow.writeObject(inputSymbols.length);
        Map<Character, BitSequence> lookupTable = biTrie.buildLookupTable();  // Use binary trie to create lookup table for encoding.
        List<BitSequence> bitSeq = new ArrayList<>();  // Create a list of bitsequences.
        for (char symbol : inputSymbols) {  // For each 8 bit symbol:
            BitSequence bits = lookupTable.get(symbol);  // Lookup that symbol in the lookup table.
            bitSeq.add(bits);  // Add the appropriate bit sequence to the list of bitsequences.
        }
        BitSequence encodeHuf = BitSequence.assemble(bitSeq);  // Assemble all bit sequences into one huge bit sequence.
        ow.writeObject(encodeHuf);  // Write the huge bit sequence to the .huf file.
    }
}
