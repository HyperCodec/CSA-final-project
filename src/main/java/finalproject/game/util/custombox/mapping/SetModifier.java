package finalproject.game.util.custombox.mapping;

import finalproject.engine.util.box.Box;

/**
 * Same as the read modifier, but applies
 * the wrapper on set instead of get.
 * @param <T> The type of the box.
 */
public class SetModifier<T> extends GetModifier<T> {
    public SetModifier(Box<T> inner, Mapper<T> mapper) {
        super(inner, mapper);
    }

    public SetModifier(Box<T> inner) {
        super(inner);
    }

    public SetModifier(T inner, Mapper<T> mapper) {
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
