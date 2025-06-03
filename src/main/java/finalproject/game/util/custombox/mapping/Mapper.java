package finalproject.game.util.custombox.mapping;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Mapper<T> {
    T map(T obj);

    @Contract(pure = true)
    static <T> @NotNull Mapper<T> doNothing() {
        return obj -> obj;
    }

    default Mapper<T> andThen(Mapper<T> after) {
        return obj -> after.map(map(obj));
    }
}
