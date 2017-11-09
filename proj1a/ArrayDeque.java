public class ArrayDeque<Item> {

    private Item[] items;
    private int size;
    private int nextfirst;
    private int nextlast;

    public ArrayDeque(){
        items = (Item[]) new Object[8];
        size = 0;
        nextfirst = items.length / 2;
        nextlast = nextfirst + 1;
    }

    /** Resizes the underlying array to the target capacity */
    private void resize(int capacity){
        Item[] a = (Item[]) new Object[capacity];
        System.arraycopy(items, 0, a, 0, size);
        items = a;
    }

    /** Inserts an item into the front of the Deque */
    public void addFirst(Item x){
        if (size == items.length) {
            resize(size * 2);
        }

        items[nextfirst] = x;
        size += 1;
        nextfirst -= 1;
    }

    /** Inserts an item into the back of the Deque */
    public void addLast(Item x) {
        if (size == items.length) {
            resize(size * 2);
        }

        items[nextlast] = x;
        size += 1;
        nextlast += 1;
    }

    /** Returns true if deque is empty, false otherwise */
    public boolean isEmpty(){
        if (size == 0){
            return true;
        }
        return false;
    }

    /** Returns the number of items in the Deque */
    public int size(){
        return size;
    }

    /** Prints the items in the Deque from first to last, separated by a space */
    public void printDeque(){
        for (int i = 0; i < size; i++){
            System.out.println(items[i]);
        }
    }

    /** Removes and returns the item at the front of the Deque */
    /** If no such item exists, returns null */
    public Item removeFirst(){
        Item x = items[nextfirst + 1];
        items[nextfirst + 1] = null;
        size -= 1;
        nextfirst += 1;
        return x;
    }

    /** Removes and returns the item at the back of the Deque */
    /** If no such item exists, returns null */
    public Item removeLast(){
        Item x = items[nextlast - 1];
        items[nextlast - 1] = null;
        size -= 1;
        nextlast -= 1;
        return x;
    }

    /** Gets the ith item in the list (0 is the front). */
    public Item get(int index){
        return items[index];
    }
}