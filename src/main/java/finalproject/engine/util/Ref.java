package finalproject.engine.util;

/**
 * Holds a reference to a value.
 * Useful with clone-on-write (immutable)
 * types since the state can be reflected across
 * all components of an entity without them needing
 * to track the actual entity
 */
public class Ref<T> {
    private T val;

    public Ref(T val) {
        this.val = val;
    }

    public T get() {
        return val;
    }

    public void set(T val) {
        this.val = val;
    }
}
