package finalproject.components.tickables.physics;

import finalproject.components.markers.Collider;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Box;
import finalproject.engine.util.Vec2;
import finalproject.entities.environment.Platform;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

// implements platform collision for this entity.
public class Platformer implements Tickable {
    Collider collider;
    Box<Vec2> vel;
    Box<Boolean> grounded;

    public Platformer(Collider collider, Box<Vec2> vel, Box<Boolean> grounded) {
        this.collider = collider;
        this.vel = vel;
        this.grounded = grounded;
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double _dt) {
        ArrayList<Platform> platforms = world.findEntitiesOfType(Platform.class);

        Vec2 vel2 = vel.get();
        double velY = vel2.getY();

        for (Platform platform : platforms) {
            if(!collider.isColliding(platform.collider)) continue;

            if(velY > 0) {
                vel.set(vel2.subY(velY));
                collider.alignBottom(platform.collider.top());
                grounded.set(true);
            }
        }

//        grounded.set(false);
    }
}
