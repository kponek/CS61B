/**
 * Created by kevin on 1/30/2017.
 */
public class LinkedListDeque<Item> {
    private StuffNode sentinel;
    private int size;

    private class StuffNode {
        private Item item;
        private StuffNode next;
        private StuffNode prev;

        public StuffNode(Item i, StuffNode n, StuffNode p) {
            item = i;
            next = n;
            prev = p;
        }

        public Item getItem() {
            return item;
        }

        public StuffNode getNext() {
            return next;
        }

        public StuffNode getPrev() {
            return prev;
        }
    }

    public LinkedListDeque() {
        size = 0;
        sentinel = new StuffNode(null, null, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public void addFirst(Item newFirst) {
        /*if (sentinel == null) {
            sentinel = new StuffNode(newFirst, null, null);
            sentinel.next = sentinel;
            sentinel.prev = sentinel;
        }*/
        //else {
        sentinel = new StuffNode(newFirst, sentinel, sentinel.prev);
        sentinel.next.prev = sentinel;
        // }
        size++;
    }

    public void addLast(Item newLast) {
        //check if the DLList is empty
        if (sentinel.prev != null) {
            sentinel.prev = new StuffNode(newLast, sentinel, sentinel.prev.prev);
        }
        //otherwise the DLList is empty so just insert normally
        else {
            sentinel = new StuffNode(newLast, sentinel, sentinel);
        }
        size++;
    }

    public boolean isEmpty() {
        return (size == 0);
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        if (sentinel.next == null) {
            return;
        }
        StuffNode curr = sentinel.next;
        while (curr.item != null) {
            System.out.print(curr.getItem());
            curr = curr.next;
        }
    }

    /**
     * Make the last item's next point to the second item, and the second item's
     * prev to point to the last. Then make the sentinel point to the second item.
     * Reduce the size by one and return the removed item
     */
    public Item removeFirst() {
        //for the case of removing from empty list
        if (sentinel.next == null) {
            return null;
        }
        Item removedItem = sentinel.item;
        sentinel.prev.next = sentinel.next;
        sentinel.next.prev = sentinel.prev;
        sentinel = sentinel.next;
        size--;
        return removedItem;
    }

    /**
     * Make the second to last item's next point to the first item, and the first item's prev
     * point to the second to last item. Reduce size by one and return removed item.
     */
    public Item removeLast() {
        //for the case of removing from empty list
        if (sentinel.prev == null) {
            return null;
        }
        Item removedItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        size--;
        return removedItem;

    }

    public Item get(int index) {
        //int count = size;
        if (isEmpty() || index >= size) {
            return null;
        }
        StuffNode curr = sentinel;
        while (index > 0) {
            curr = sentinel.next;
            index--;
        }
        return curr.item;
    }

    public Item getRecursive(int index) {
        return recursiveHelper(index, sentinel);

    }

    private Item recursiveHelper(int index, StuffNode curr) {
        if (index == 0) {
            return curr.item;
        }
        return recursiveHelper(index--, curr.next);
    }
}
