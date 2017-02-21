package synthesizer;

import java.util.Iterator;

/**
 * Created by kevin on 2/19/2017.
 */
public interface BoundedQueue<T> extends Iterable<T> {
    int capacity();

    int fillCount();

    void enqueue(T x);

    T dequeue();

    T peek();

    Iterator<T> iterator();

    default boolean isEmpty() {
        return fillCount() == 0;
    }

    default boolean isFull() {
        return fillCount() >= capacity();
    }
}
