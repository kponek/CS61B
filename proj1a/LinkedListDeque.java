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
    }
    public void addFirst(Item newFirst) {
        sentinel = new StuffNode(newFirst, sentinel, sentinel.prev);
        sentinel.next.prev = sentinel;
        size++;
    }
    public void addLast(Item newLast) {
        //check if the DLList is empty
        if (sentinel.prev != null) {
            sentinel.prev = new StuffNode(newLast, sentinel, sentinel.prev.prev);
        }
        //otherwise the DLList is empty so just insert normally
        else {
            sentinel = new StuffNode(newLast,sentinel,sentinel);
        }
        size++;
    }
    public boolean isEmpty() {
        if (sentinel.next == null) {
            return true;
        }
        return false;
    }
    public int size() {
        return size;
    }
    public void printDeque() {
        StuffNode curr = sentinel;
        while(curr != null) {
            System.out.print(curr.getItem());
            curr = curr.next;
        }
    }
    public Item removeFirst() {
        Item removedItem = sentinel.item;
        sentinel.prev = sentinel.next.prev;
        sentinel.next.prev = sentinel.prev;
        sentinel = sentinel.next;
        return removedItem;
    }
    public Item removeLast() {
        Item removedItem = sentinel.prev.item;
        sentinel.prev.prev.next = sentinel;
        sentinel.prev = sentinel.prev.prev;
        return removedItem;
    }
    public Item get(int index) {
        //int count = size;
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
