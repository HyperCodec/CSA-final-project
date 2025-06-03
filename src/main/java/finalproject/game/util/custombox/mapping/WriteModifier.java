package finalproject.game.util.custombox.mapping;

import finalproject.engine.util.box.Box;

/**
 * Same as the read modifier, but applies
 * the wrapper on set instead of get.
 * @param <T> The type of the box.
 */
public class WriteModifier<T> extends ReadModifier<T> {
    public WriteModifier(Box<T> inner, Mapper<T> mapper) {
        super(inner, mapper);
    }

    public WriteModifier(Box<T> inner) {
        super(inner);
    }

    public WriteModifier(T inner, Mapper<T> mapper) {
        super(inner, mapper);
    }

    @Override
    public void set(T val) {
        inner.set(mapper.map(val));
    }

    @Override
    public T get() {
        return inner.get();
    }
}
