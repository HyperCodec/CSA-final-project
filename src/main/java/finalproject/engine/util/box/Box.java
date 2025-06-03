package finalproject.engine.util.box;

/**
 * Wraps a value and forces
 * explicit read/write access,
 * so I don't have to make a million
 * getters/setters.
 * @param <T> The type of value to wrap.
 */
public interface Box<T> {
    T get();
    void set(T val);
}
