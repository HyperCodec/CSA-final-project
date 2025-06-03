package finalproject.game.util.custombox.mapping.multitype;

import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;

/**
 * More powerful but more annoying-to-use version of the Read/WriteModifier.
 * Allows converting between box types but requires conversions to work both ways.
 * @param <I> Type of the box that's being wrapped.
 * @param <O> Type of the box that this pretends to be.
 */
public class UnboundModifier<I, O> implements Box<O> {
    Box<I> inner;
    TwoWayMapper<I, O> mapper;

    public UnboundModifier(Box<I> inner, TwoWayMapper<I, O> mapper) {
        this.inner = inner;
        this.mapper = mapper;
    }

    public UnboundModifier(I inner, TwoWayMapper<I, O> mapper) {
        this.inner = new BasicBox<>(inner);
        this.mapper = mapper;
    }

    public void setMapper(TwoWayMapper<I, O> mapper) {
        this.mapper = mapper;
    }

    public TwoWayMapper<I, O> getMapper() {
        return mapper;
    }

    @Override
    public O get() {
        return mapper.map(inner.get());
    }

    @Override
    public void set(O outer) {
        inner.set(mapper.reverseMap(outer));
    }
}
