package synthesizer;

import java.util.Iterator;

public class ArrayRingBuffer<T> extends AbstractBoundedQueue<T> {
    /* Index for the next dequeue or peek. */
    private int first;            // index for the next dequeue or peek
    /* Index for the next enqueue. */
    private int last;
    /* Array for storing the buffer data. */
    private T[] rb;

    /**
     * Create a new ArrayRingBuffer with the given capacity.
     */
    public ArrayRingBuffer(int capacity) {
        first = 0;
        last = 0;
        fillCount = 0;
        this.capacity = capacity;
        rb = (T[]) new Object[capacity];
    }

    /**
     * Adds x to the end of the ring buffer. If there is no room, then
     * throw new RuntimeException("Ring buffer overflow"). Exceptions
     * covered Monday.
     */
    @Override
    public void enqueue(T x) {
        if (rb[last] != null) {
            throw new RuntimeException("Ring buffer overflow");
        } else {
            rb[last] = x;
            fillCount++;
            lastIncrement();
        }
    }

    /**
     * Dequeue oldest item in the ring buffer. If the buffer is empty, then
     * throw new RuntimeException("Ring buffer underflow"). Exceptions
     * covered Monday.
     */
    public T dequeue() {
        if (fillCount > 0) {
            T dequeued = rb[first];
            rb[first] = null;
            fillCount--;
            firstIncrement();
            return dequeued;
        } else {
            throw new RuntimeException("Ring buffer underflow");
        }
    }

    /**
     * Return oldest item, but don't remove it.
     */
    public T peek() {
        if (isEmpty()) {
            throw new RuntimeException("Ring buffer empty - can't peek");
        }
        return rb[first];
    }

    private class BufferWizard implements Iterator<T> {
        private int notionOfWhereHeIs;

        BufferWizard() {
            notionOfWhereHeIs = 0;
        }

        public boolean hasNext() {
            return (notionOfWhereHeIs < fillCount);
        }

        public T next() {
            T currentThing = rb[notionOfWhereHeIs];
            notionOfWhereHeIs++;
            return currentThing;
        }
    }

    public Iterator<T> iterator() {
        return new BufferWizard();
    }

    private void lastIncrement() {
        last++;
        if (last == capacity) {
            last = 0;
        }
    }

    private void firstIncrement() {
        first++;
        if (first == capacity) {
            first = 0;
        }
    }
}
