import java.util.Arrays;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra
 * @version 1.4 - April 14, 2016
 *
 **/
public class RadixSort
{
    private static int R = 256;

    /**
     * Does Radix sort on the passed in array with the following restrictions:
     *  The array can only have ASCII Strings (sequence of 1 byte characters)
     *  The sorting is stable and non-destructive
     *  The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     **/

    /** LSD radix sort is good for fixed-length string. */
    /** MSD radix sort is good for variable-length strings. */
    public static String[] LSDSort(String[] asciis, int index) {

        int[] count = new int[R];
        for (int i = 0; i < asciis.length; i++) {
            char c = asciis[i].charAt(asciis[i].length() - index - 1);
            int ac = (int) c;
            count[ac] += 1;
        }

        int curr = 0;
        int prev;
        for (int i = 0; i < R; i++) {
            if (count[i] > 0) {
                prev = count[i];
                count[i] = curr;
                curr += prev;
            }
        }

        String[] sorted = new String[asciis.length];
        for (String s : asciis) {
            int i = (int) s.charAt(s.length() - index - 1);
            int pos = count[i];
            sorted[pos] = s;
            count[i]++;
        }

        return sorted;
    }

    public static String[] MSDSort(String[] asciis) {

        int max = 0;
        for (String as : asciis) {
            if (as.length() > max) {
                max = as.length();
            }
        }
        int N = asciis.length;
        MSDSortHelper(asciis, 0, N, 0);
        return asciis;
    }

    /**
     * Radix sort helper function that recursively calls itself to achieve the sorted array
     *  destructive method that changes the passed in array, asciis
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void MSDSortHelper(String[] asciis, int start, int end, int index) {

        if (end - start == 1) {
            return;
        }

        int[] count = new int[R];
        for (int i = start; i < end; i++) {
            char c = asciis[i].charAt(index);
            int ac = (int) c;
            count[ac] += 1;
        }

        int[] accCount = new int[R];
        int curr = 1;
        int prev;
        for (int i = 0; i < R; i++) {
            if (count[i] > 0) {
                prev = count[i];
                accCount[i] = curr;
                curr += prev;
            }
        }

        String[] sorted = new String[end - start];
        for (int i = start; i < end; i++) {
            int f = (int) asciis[i].charAt(index);
            int pos = accCount[f];
            sorted[pos - 1] = asciis[i];
            accCount[f]++;
        }

        for (int i = start; i < end; i++) {
            asciis[i] = sorted[i - start];
        }

        index++;
        for (int i = 0; i < R; i++) {
            if (count[i] > 0) {
                MSDSortHelper(asciis, start+accCount[i]-count[i]-1, start+accCount[i]-1, index);
            }
        }
    }

    private static String[] LSDSortHelper(String[] asciis) {
        int max = asciis[0].length();    // Store the max length of String

        int k = 0;
        while (k < max) {
            asciis = LSDSort(asciis, k++);
        }
        return asciis;
    }

    public static void main(String[] args) {
        String[] testArr1 = {"alan",  "hello", "success!", "donald", "hcldlo","hellen", "123", "!!"};
        String[] testArr2 = {"delta", "caddy","allen", "alpha", "apple"};
        String[] result1 = MSDSort(testArr1);
        String[] result2 = LSDSortHelper(testArr2);
        System.out.println(Arrays.toString(result1));
        System.out.println(Arrays.toString(result2));
    }
}
