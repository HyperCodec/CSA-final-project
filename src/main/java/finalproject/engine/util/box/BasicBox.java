package finalproject.engine.util.box;

/**
 * Stores a reference to an object.
 * Effectively a heap pointer type
 * to keep fields the same across
 * components with immutable types.
 */
public class BasicBox<T> implements Box<T> {
    // could probably make this public instead
    // of using getter/setter but whatever
    private T val;

    public BasicBox(T val) {
        this.val = val;
    }

    @Override
    public T get() {
        return val;
    }

    @Override
    public void set(T val) {
        this.val = val;
    }
}
