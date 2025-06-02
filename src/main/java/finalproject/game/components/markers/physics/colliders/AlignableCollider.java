package finalproject.game.components.markers.physics.colliders;

import finalproject.engine.util.Axis;
import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.util.physics.CardinalDirection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public abstract class AlignableCollider extends Collider {
    protected AlignableCollider(Box<Vec2> pos) {
        super(pos);
    }

    public Vec2 closestEdgePoint(@NotNull Vec2 point) {
        return point.getClosest(getEdgePoints());
    }

    public VectorComponent[] getCardinals() {
        return new VectorComponent[]{
                new VectorComponent(left(), Axis.X),
                new VectorComponent(right(), Axis.X),
                new VectorComponent(top(), Axis.Y),
                new VectorComponent(bottom(), Axis.Y)
        };
    }

    public Vec2[] getCardinalPoints() {
        Vec2 center = pos.get();

        VectorComponent[] cardinals = getCardinals();
        Vec2[] points = new Vec2[cardinals.length];

        for(int i = 0; i < cardinals.length; i++) {
            VectorComponent component = cardinals[i];
            points[i] = center.withComponent(component);
        }

        return points;
    }


    public abstract void alignBottom(double y);
    public abstract void alignTop(double y);
    public abstract void alignLeft(double x);
    public abstract void alignRight(double x);

    public abstract double left();
    public abstract double right();
    public abstract double top();
    public abstract double bottom();
}
