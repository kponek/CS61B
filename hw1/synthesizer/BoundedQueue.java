package synthesizer;

/**
 * Created by kevin on 2/19/2017.
 */
public interface BoundedQueue<T> {
    int capacity();
    int fillCount();
    void enqueue(T x);
    T dequeue();
    T peek();
    default boolean isEmpty() {
        return fillCount() == 0;
    }
    default boolean isFull() {
        return fillCount() >= capacity();
    }
}
