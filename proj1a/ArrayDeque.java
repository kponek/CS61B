/**
 * Created by kevin on 1/30/2017.
 */
public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /** Initialize new ArrayDeque with 8 spaces and nothing inside */
    public ArrayDeque() {
        items = (Item []) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /** Adds an item to the front of the deque. Special case when the next first
     *  is already at the 0 index: move nextFirst to the end at length - 1 index*/
    public void addFirst(Item newFirst) {
        items[nextFirst] = newFirst;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        }
        else {
            nextFirst--;
        }
    }
    /** Adds an item to the back of the deque. Special case when the last is
     *  already at the length - 1 index: move nextLast to the beginning at 0 index */
    public void addLast(Item newLast) {
        items[nextLast] = newLast;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        }
        else {
            nextLast++;
        }
    }
    /** Checks if the deque is empty. True if size is not 0, otherwise false. */
    public boolean isEmpty() {
        return (size == 0);
    }
    /** Returns the size fo the deque */
    public int size() {
        return size;
    }
    /** Prints the deque starting at the nextFirst pointer + 1, and ending at
     *  the nextLast pointer. */
    public void printDeque() {
        int pointer = nextFirst + 1;
        int end = nextLast;
        if (pointer == items.length - 1) {
            pointer = 0;
        }
        while (pointer != end) {
            System.out.print(items[pointer]);
            if (pointer == items.length - 1) {
                pointer = 0;
            }
            else {
                pointer++;
            }
        }
    }
    /** Removes (aka moves the nextFirst pointer) the first item of the deque
     *  Special case when the nextFirst pointer is at the end, then move it to index 0.
     *  Return the removed item.*/
    public Item removeFirst() {
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        }
        else {
            nextFirst++;
        }
        return items[nextFirst];
    }
    /** Removes (aka moves the nextLast pointer) the last item of the deque
     *  Special case when the nextLast pointer is at the beginning, then move it
     *  to index length - 1. Return the removed item.*/
    public Item removeLast() {
        if (nextLast == 0) {
            nextLast = items.length - 1;
        }
        else {
            nextLast--;
        }
        return items[nextLast];
    }
    public Item get(int index) {
        return items[index];
    }
}
