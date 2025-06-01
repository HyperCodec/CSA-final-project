package finalproject.game.util.custombox;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Mapper<T> {
    T map(T obj);

    @Contract(pure = true)
    static <T> @NotNull Mapper<T> doNothing() {
        return obj -> obj;
    }
}
