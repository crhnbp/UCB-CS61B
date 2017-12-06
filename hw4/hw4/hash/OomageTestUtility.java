package hw4.hash;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO: Write a utility function that returns true if the given oomages 
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        Map<Integer, Integer> buckets = new HashMap<>();
        int bucketNum;
        int N = oomages.size();
        for (Oomage o : oomages) {
            bucketNum = (o.hashCode() & 0x7FFFFFFF) % M;
            if (buckets.containsKey(bucketNum)) {
                int count = buckets.get(bucketNum) + 1;
                buckets.replace(bucketNum, count);
            } else {
                buckets.put(bucketNum, 1);
            }
        }
        for (int v : buckets.values()) {
            if (v < N / 50 || v > N / 2.5) {
                return false;
            }
        }
        return true;
    }
}
