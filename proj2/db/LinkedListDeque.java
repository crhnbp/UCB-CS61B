package db;

public class LinkedListDeque<Item> {

	private class GeNode {
		public Item item;
		public GeNode prev;
		public GeNode next;

		/** GoNode constructor */
		public GeNode(GeNode p, Item i, GeNode n){
			item = i;
			prev = p;
			next = n;
		}
	}

	private GeNode sentinel;
	private GeNode first;
	private GeNode last;
	private int size;

	public LinkedListDeque(){
	 	sentinel = new GeNode(sentinel, null, sentinel);
	 	sentinel.next = sentinel;
	 	sentinel.prev = sentinel;
		size = 0;
	}

	public LinkedListDeque(Item x){
		sentinel = new GeNode(sentinel, null, sentinel);
		sentinel.next = new GeNode(sentinel, x, sentinel);
		size = 1;
	}	

	/** Adds an item to the front of the Deque */
	public void addFirst(Item x){
		first = sentinel.next;
		sentinel.next = new GeNode(sentinel, x, first);
		first.prev = sentinel.next;
		size += 1;
	}

	/** Adds an item to the back of the Deque */
	public void addLast(Item x){
		last = sentinel.prev;
		sentinel.prev = new GeNode(last, x, sentinel);
		last.next = sentinel.prev;
		size += 1;
	}

	/** Returns true if deque is empty, false otherwise */
	public boolean isEmpty(){
		if (sentinel.next == sentinel){
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
		GeNode ptr = sentinel;
		while (ptr.next != sentinel){
			ptr = ptr.next;
			System.out.println(ptr.item);
		}
	}

	/** Removes and returns the item at the front of the Deque */
	/** If no such item exists, returns null */
	public Item removeFirst(){
		if (sentinel.next == sentinel){
			return null;
		}
		first = sentinel.next;
		sentinel.next = first.next;
		first.next.prev = sentinel;
		size -= 1;
		return first.item;
	}

	/** Removes and returns the item at the back of the Deque */
	/** If no such item exists, returns null */
	public Item removeLast(){
		if (sentinel.next == sentinel){
			return null;
		}
		last = sentinel.prev;
		sentinel.prev = last.prev;
		last.prev.next = sentinel;
		size -= 1;
		return last.item;
	}

	/** Gets the item at the given index, where 0 is the front, 1 is the next item, and so forth */
	/** If no such item exists, returns null */
	/** Must not alter the deque! */
	public Item get(int index){
		GeNode ptr = sentinel;
		if (sentinel.next == sentinel){
			return null;
		}
		for (int i = 0; i < size; i++){
			ptr = ptr.next;
			if (i == index){
				return ptr.item;
			}
		}
		return null;
	}

	/** Same as get, but uses recursion */
	public Item getRecursive(int index){
		if (sentinel.next == sentinel){
			return null;
		}
		if (index == 0){
			return sentinel.next.item;
		}
		sentinel.next = sentinel.next.next;
		return getRecursive(index - 1);
	}
}