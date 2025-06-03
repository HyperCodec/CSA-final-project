package finalproject.game.util.custombox.lazy;

import finalproject.engine.util.box.Box;
import org.jetbrains.annotations.NotNull;

/**
 * Originally stores a default value until
 * get is first called.
 * Useful for expensive-to-compute values that
 * you don't know whether you'll need until runtime.
 * Requires Object.equals() to work
 * if the default is not null.
 * @param <T> The type of the value.
 */
public class Lazy<T> implements Box<T> {
    T val;
    final Generator<T> gen;
    final T defaultVal;

    public Lazy(@NotNull Generator<T> gen) {
        this(gen, gen.getDefault());
    }

    /// override the default value provided by the generator.
    public Lazy(@NotNull Generator<T> gen, T defaultVal) {
        this.gen = gen;
        this.val = defaultVal;
        this.defaultVal = defaultVal;
    }

    public T getDefault() {
        return defaultVal;
    }

    @Override
    public T get() {
        if(val.equals(defaultVal))
            val = gen.generate();

        return val;
    }

    @Override
    public void set(T val) {
        this.val = val;
    }
}
