package finalproject.game.util.custombox.mapping.multitype;

public interface TwoWayMapper<I, O> {
    O map(I inner);
    I reverseMap(O outer);

    default <O2> TwoWayMapper<I, O2> andThen(TwoWayMapper<O, O2> other) {
        TwoWayMapper<I, O> this2 = this;
        return new TwoWayMapper<>() {
            @Override
            public O2 map(I inner) {
                return other.map(this2.map(inner));
            }

            @Override
            public I reverseMap(O2 outer) {
                return this2.reverseMap(other.reverseMap(outer));
            }
        };
    }
}
