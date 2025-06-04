package finalproject.game.entities.environment;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.Collider;
import finalproject.game.entities.Scene;
import finalproject.game.entities.character.Player;
import finalproject.game.util.SceneUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URISyntaxException;

public class Flag implements Entity, Tickable {
    public final Box<Vec2> pos;
    public final Scene sceneToLoad;
    public final Collider collider;

    public Flag(Vec2 pos, String nextLevel) throws IOException, URISyntaxException {
        this.pos = new BasicBox<>(pos);
        sceneToLoad = SceneUtils.parseFromResources(nextLevel);
        // TODO RectCollider from image w/h.
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        // TODO renderable
        r.addMarker(collider);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double _dt) {
        Player player = world.findEntitiesOfType(Player.class).getFirst();

        if(player.collider.isColliding(collider)) {
            SceneUtils.loadScene(world, sceneToLoad);
        }
    }
}
