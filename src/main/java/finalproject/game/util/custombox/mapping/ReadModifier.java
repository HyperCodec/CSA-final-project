package finalproject.game.util.custombox.mapping;

import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;

/**
 * Wraps a Box and applies a mapper to it.
 * Useful for tracking a value differently in
 * a different place without having to create a new Box.
 * @param <T> The type of the Box.
 */
public class ReadModifier<T> implements Box<T> {
    Box<T> inner;
    Mapper<T> mapper;

    public ReadModifier(T inner, Mapper<T> mapper) {
        this(new BasicBox<>(inner), mapper);
    }

    public ReadModifier(Box<T> inner, Mapper<T> mapper) {
        this.inner = inner;
        this.mapper = mapper;
    }

    public ReadModifier(Box<T> inner) {
        this(inner, Mapper.doNothing());
    }

    public void setMapper(Mapper<T> mapper) {
        this.mapper = mapper;
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
