import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDeque1B {

    @Test
    public void testDeque1B(){

        StudentArrayDeque<Integer> sad = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> ads = new ArrayDequeSolution<>();

        for (int i = 0; i < 10; i ++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                sad.addLast(i);
                ads.addLast(i);
            } else {
                sad.addFirst(i);
                ads.addFirst(i);
            }
        }
        OperationSequence fs = new OperationSequence();

        DequeOperation sadOp1 = new DequeOperation("removeLast");
        fs.addOperation(sadOp1);
        assertEquals(fs.toString(), sad.removeLast(), ads.removeLast());

        DequeOperation sadOp2 = new DequeOperation("removeLast");
        fs.addOperation(sadOp2);
        assertEquals(fs.toString(), sad.removeLast(), ads.removeLast());

        DequeOperation sadOp3 = new DequeOperation("removeFirst");
        fs.addOperation(sadOp3);
        assertEquals(fs.toString(), sad.removeFirst(), ads.removeFirst());

        DequeOperation sadOp4 = new DequeOperation("removeLast");
        fs.addOperation(sadOp4);
        assertEquals(fs.toString(), sad.removeLast(), ads.removeLast());
    }
}
