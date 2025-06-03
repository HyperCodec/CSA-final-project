package finalproject.game.util.custombox.lazy;

public interface Generator<T> {
    T generate();
    default T getDefault() {
        return null;
    }
}
