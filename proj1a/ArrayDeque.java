/**
 * Created by kevin on 1/30/2017.
 */
public class ArrayDeque<Item> {
    private Item[] items;
    private int size;
    private int nextFirst;
    private int nextLast;

    /**
     * Initialize new ArrayDeque with 8 spaces and nothing inside
     */
    public ArrayDeque() {
        items = (Item[]) new Object[8];
        size = 0;
        nextFirst = 0;
        nextLast = 1;
    }

    /**
     * Adds an item to the front of the deque. Special case when the next first
     * is already at the 0 index: move nextFirst to the end at length - 1 index
     */
    public void addFirst(Item newFirst) {
        items[nextFirst] = newFirst;
        size++;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
        //resize
        if (size == items.length) {
            resize(2 * items.length);
        }
    }

    /**
     * Adds an item to the back of the deque. Special case when the last is
     * already at the length - 1 index: move nextLast to the beginning at 0 index
     */
    public void addLast(Item newLast) {
        items[nextLast] = newLast;
        size++;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
        if (size == items.length) {
            resize(2 * items.length);
        }
    }

    /**
     * Checks if the deque is empty. True if size is not 0, otherwise false.
     */
    public boolean isEmpty() {
        return (size == 0);
    }

    /**
     * Returns the size fo the deque
     */
    public int size() {
        return size;
    }

    /**
     * Prints the deque starting at the nextFirst pointer + 1, and ending at
     * the nextLast pointer.
     */
    public void printDeque() {
        int pointer = nextFirst + 1;
        if (pointer == items.length) {
            pointer = 0;
        }
        /*if (pointer == nextLast) {
            System.out.println(items[pointer] + " ");
        }*/
        while (pointer != nextLast) {
            System.out.print(items[pointer] + " ");
            pointer++;
            if (pointer == items.length) {
                pointer = 0;
            }
        }
    }

    /**
     * Removes (aka moves the nextFirst pointer) the first item of the deque
     * Special case when the nextFirst pointer is at the end, then move it to index 0.
     * Return the removed item.
     */
    public Item removeFirst() {
        //change pointer
        nextFirst++;
        if (nextFirst == items.length) {
            nextFirst = 0;
        }
        //null the deleted item and return it
        Item deleted = items[nextFirst];
        items[nextFirst] = null;
        size--;
        if ((((double) size) / ((double) items.length)) < 0.25 && items.length > 16) {
            resize(size / 2);
        }
        return deleted;
    }

    /**
     * Removes (aka moves the nextLast pointer) the last item of the deque
     * Special case when the nextLast pointer is at the beginning, then move it
     * to index length - 1. Return the removed item.
     */
    public Item removeLast() {
        //change pointer
        nextLast--;
        if (nextLast < 0) {
            nextLast = items.length - 1;
        }
        //null the deleted item and return it
        Item deleted = items[nextLast];
        items[nextLast] = null;
        size--;
        if ((((double) size) / ((double) items.length)) < 0.25 && items.length > 16) {
            resize(size / 2);
        }
        return deleted;
    }

    public Item get(int index) {
        int first = nextFirst + 1;
        if (first == items.length) {
            first = 0;
        }
        int listIndex = first + index;
        if ((listIndex) >= items.length) {
            listIndex = listIndex - items.length;
        }
        return items[listIndex];
    }

    private void resize(int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];
        int first = nextFirst + 1;
        int last = nextLast - 1;
        if (first == items.length) {
            first = 0;
        }
        if (last < 0) {
            last = items.length - 1;
        }
        int firstToEnd = items.length - first;
        int beginToLast = last + 1;
        if (last <= first) {
            System.arraycopy(items, first, newItems, 0, firstToEnd);
            System.arraycopy(items, 0, newItems, firstToEnd, first);
        } else {
            System.arraycopy(items, first, newItems, 0, size);
        }
        nextFirst = newItems.length - 1;
        nextLast = size;
        items = newItems;
    }
    /*public static void main(String[] args) {
        ArrayDeque<Integer> ArrayDeque = new ArrayDeque<>();
        ArrayDeque.addLast(0);
        System.out.println(ArrayDeque.removeLast())  ;
        ArrayDeque.addFirst(2);
        System.out.println(ArrayDeque.get(0))      ;
        ArrayDeque.addFirst(4);
        ArrayDeque.addFirst(5);
        System.out.println(ArrayDeque.get(0)  )    ;
        ArrayDeque.addLast(7);
        System.out.println(ArrayDeque.get(1)  )    ;
        ArrayDeque.addLast(9);
        ArrayDeque.addLast(10);
        ArrayDeque.addFirst(11);
        ArrayDeque.addFirst(12);
        System.out.println(ArrayDeque.removeLast() )   ;
        System.out.println(ArrayDeque.removeLast())     ;
        System.out.println(ArrayDeque.removeLast() )     ;
        System.out.println(ArrayDeque.get(2))     ;
        System.out.println(ArrayDeque.removeFirst());
        System.out.println(ArrayDeque.get(0)  )    ;
        System.out.println(ArrayDeque.removeFirst());
        System.out.println(ArrayDeque.get(2));
    }*/

}
