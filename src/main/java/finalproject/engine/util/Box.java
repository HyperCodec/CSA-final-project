package finalproject.engine.util;

/**
 * Stores a reference to an object.
 * Effectively a heap pointer type
 * to keep fields the same across
 * components with immutable types.
 */
public class Box<T> {
    private T val;

    public Box(T val) {
        this.val = val;
    }

    public T get() {
        return val;
    }

    public void set(T val) {
        this.val = val;
    }
}
