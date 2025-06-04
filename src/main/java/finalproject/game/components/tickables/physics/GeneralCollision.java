package finalproject.game.components.tickables.physics;

import finalproject.engine.ecs.Entity;
import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.BasicBox;
import finalproject.game.components.markers.physics.colliders.AlignableCollider;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.entities.environment.Platform;
import finalproject.game.entities.environment.Wall;
import finalproject.game.util.physics.CardinalDirection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// implements platform and wall collision for this entity.
public class GeneralCollision implements Tickable {
    public final static double COYOTE_JUMP_DURATION = 0.25;

    AlignableCollider collider;
    Box<Vec2> vel;
    Box<Boolean> grounded;
    Box<Boolean> fallThroughPlatforms;
    Box<Double> fallDuration;
    Entity parent;

    public GeneralCollision(AlignableCollider collider, Box<Vec2> vel, Box<Boolean> grounded, Box<Boolean> fallThroughPlatforms, Box<Double> fallDuration, Entity parent) {
        this.collider = collider;
        this.vel = vel;
        this.grounded = grounded;
        this.fallThroughPlatforms = fallThroughPlatforms;
        this.fallDuration = fallDuration;
        this.parent = parent;
    }

    public GeneralCollision(AlignableCollider collider, Box<Vec2> vel, Entity parent) {
        this(collider, vel, new BasicBox<>(false), new BasicBox<>(false), new BasicBox<>(0.0), parent);
    }

    public GeneralCollision(AlignableCollider collider, Box<Vec2> vel) {
        this(collider, vel, null);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        boolean a = performPlatformCollision(world);
        boolean b = performWallCollision(world);
        boolean landedOnGround = a || b;
        if(landedOnGround) return;

        double newFallDuration = fallDuration.get() + dt;
        fallDuration.set(newFallDuration);

        if(newFallDuration > COYOTE_JUMP_DURATION)
            grounded.set(false);
    }

    // probably not the ideal way to do things,
    // but I don't really have time to make it better
    protected void onTouchPlatform(WorldAccessor world, Platform platform) {}

    private boolean performPlatformCollision(@NotNull WorldAccessor world) {
        // kind of slow to query every frame, but storing it is a little annoying
        ArrayList<Platform> platforms = world.findEntitiesOfType(Platform.class);

        Vec2 vel2 = vel.get();
        double velY = vel2.getY();
        boolean landedOnGround = false;

        for (Platform platform : platforms) {
            if(!platform.collider.isColliding(collider)) {
                platform.fallingEntities.remove(parent);
                continue;
            }

            if(fallThroughPlatforms.get()) {
                platform.fallingEntities.add(parent);
                continue;
            }

            onTouchPlatform(world, platform);
            if(platform.fallingEntities.contains(parent)) continue;

            if(velY > 0) {
                vel.set(vel2.subY(velY));
                collider.alignBottom(platform.collider.top() + 1);
                grounded.set(true);
                fallDuration.set(0.0);
            }

            landedOnGround = true;
        }

        return landedOnGround;
    }

    private boolean performWallCollision(@NotNull WorldAccessor world) {
        ArrayList<Wall> walls = world.findEntitiesOfType(Wall.class);

        Vec2 center = collider.getCenter();
        Vec2 vel2 = vel.get();

        for(Wall wall : walls) {
            List<VectorComponent> validCardinals;
            if(wall.collider.contains(center))
                // entity's center is inside the wall, so don't prune cardinals
                validCardinals = List.of(wall.collider.getCardinals());
            else if(collider.outerIntersect(wall.collider))
                // entity only intersects edges, center is outside
                validCardinals = Arrays.stream(wall.collider.getCardinals())
                        .filter(component -> switch(component.getAxis()) {
                            case X -> collider.bottom() >= wall.collider.top() && collider.top() <= wall.collider.bottom();
                            case Y -> collider.right() >= wall.collider.left() && collider.left() <= wall.collider.right();
                        })
                        .toList();
            else {
                // no collision
                if(collider.isColliding(wall.collider)) {
                    System.out.println("this shouldn't be happening");
                }
                continue;
            }

            CardinalDirection wallSide = closestDirection(wall.collider, validCardinals);
            switch(wallSide) {
                case LEFT:
                    if(vel2.getX() > 0)
                        vel.set(vel2.subX(vel2.getX()));

                    collider.alignRight(wall.collider.left());
                    return false;
                case RIGHT:
                    if(vel2.getX() < 0)
                        vel.set(vel2.subX(vel2.getX()));

                    collider.alignLeft(wall.collider.right());
                    return false;
                case UP:
                    if(vel2.getY() > 0)
                        vel.set(vel2.subY(vel2.getY()));

                    collider.alignBottom(wall.collider.top());
                    grounded.set(true);
                    return true;
                case DOWN:
                    if(vel2.getY() < 0)
                        vel.set(vel2.subY(vel2.getY()));
                    collider.alignTop(wall.collider.bottom());
                    return false;
            }
        }

        return false;
    }

    private CardinalDirection closestDirection(@NotNull AlignableCollider wallCollider, List<VectorComponent> validCardinals) {
        VectorComponent closestComponent = collider.getCenter().getClosestComponent(validCardinals);
        return CardinalDirection.fromComponent(closestComponent.sub(wallCollider.getCenter()));
    }

    private void landOnGround() {
        grounded.set(true);
        fallDuration.set(0.0);
    }
}
