package finalproject.game.components.tickables.physics;

import finalproject.game.components.markers.physics.colliders.CharacterCollider;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.game.util.Box;
import finalproject.engine.Vec2;
import finalproject.game.entities.environment.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

// implements platform collision for this entity.
public class CharacterController implements Tickable {
    public final static double COYOTE_JUMP_DURATION = 0.25;

    CharacterCollider collider;
    Box<Vec2> vel;
    Box<Boolean> grounded;
    Box<Double> fallDuration;

    public CharacterController(CharacterCollider collider, Box<Vec2> vel, Box<Boolean> grounded, Box<Double> fallDuration) {
        this.collider = collider;
        this.vel = vel;
        this.grounded = grounded;
        this.fallDuration = fallDuration;
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        // kind of slow to query every frame, but storing it is a little annoying
        ArrayList<Platform> platforms = world.findEntitiesOfType(Platform.class);

        Vec2 vel2 = vel.get();
        double velY = vel2.getY();

        for (Platform platform : platforms) {
            if(!platform.collider.isColliding(collider)) continue;

            if(velY > 0) {
                vel.set(vel2.subY(velY));
                collider.alignBottom(platform.collider.top() + 1);
                grounded.set(true);
                fallDuration.set(0.0);
            }

            return;
        }

        double newFallDuration = fallDuration.get() + dt;
        fallDuration.set(newFallDuration);

        if(newFallDuration > COYOTE_JUMP_DURATION)
            grounded.set(false);
    }
}
