package finalproject.game.util.custombox;

import finalproject.engine.util.box.Box;

/**
 * Wraps a Box and applies a mapper to it.
 * Useful for tracking a value differently in
 * a different place without having to create a new Box.
 * @param <T> The type of the Box.
 */
public class BiasedViewBox<T> implements Box<T> {
    Box<T> inner;
    Mapper<T> mapper;

    public BiasedViewBox(Box<T> inner, Mapper<T> mapper) {
        this.inner = inner;
        this.mapper = mapper;
    }

    public BiasedViewBox(Box<T> inner) {
        this(inner, Mapper.doNothing());
    }

    @Override
    public void set(T val) {
        inner.set(val);
    }

    @Override
    public T get() {
        return mapper.map(inner.get());
    }
}
