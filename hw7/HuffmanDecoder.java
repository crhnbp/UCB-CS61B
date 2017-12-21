
public class HuffmanDecoder {
    public static void main(String[] args) {
        ObjectReader or = new ObjectReader(args[0]);
        Object x = or.readObject();
        Object y = or.readObject();
        Object z = or.readObject();
        BinaryTrie huffTrie = (BinaryTrie) x;  // Read the Huffman coding trie.
        int numOfSymbols = (int) y;  // If applicable, read the number of symbols.
        BitSequence bits = (BitSequence) z;  // Read the massive bit sequence corresponding to the original txt.
        char[] originTxt = new char[numOfSymbols];
        for (int i = 0; i < numOfSymbols; i++) {  // Repeat until there are no more symbols:
            Match m = huffTrie.longestPrefixMatch(bits);  // Perform a longest prefix match on the massive sequence.
            originTxt[i] = m.getSymbol();  // Record the symbol in some data structure.
            bits = bits.allButFirstNBits(m.getSequence().length());  // Create a new bit sequence containing the remaining unmatched bits.
        }
        FileUtils.writeCharArray(args[1], originTxt);  // Write the symbols in some data structure to the specified file.
    }
}
