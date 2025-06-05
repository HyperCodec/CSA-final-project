package finalproject.game.entities.environment;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.Collider;
import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.entities.Scene;
import finalproject.game.entities.character.Player;
import finalproject.game.entities.ui.Score;
import finalproject.game.util.ResourceUtils;
import finalproject.game.util.SceneUtils;
import finalproject.game.util.rendering.SpriteSheet;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Flag implements Entity, Tickable {
    final static SpriteSheet FLAG_ANIMATION;

    static {
        try {
            FLAG_ANIMATION = new SpriteSheet(ResourceUtils.readImage(Flag.class, "assets/textures/environment/flag.png"), 1, 5);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final Box<Vec2> pos;
    public final Scene sceneToLoad;
    public final Collider collider;

    public Flag(Vec2 pos, String nextLevel) throws IOException, URISyntaxException {
        this.pos = new BasicBox<>(pos);
        sceneToLoad = SceneUtils.parseFromResources(nextLevel);

        Image frame1 = FLAG_ANIMATION.getImages().getFirst();
        collider = new RectCollider(this.pos, new BasicBox<>(new Vec2(frame1.getWidth(null), frame1.getHeight(null))));
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addMarker(collider);
        r.addMarker(collider);
        // kind of just using player constants everywhere lol
        AnimatedSprite sprite = new AnimatedSprite(pos, FLAG_ANIMATION.getImages(), Player.ANIMATION_FRAME_TIME);
        r.addRenderable(sprite);
        r.addTickable(sprite);
        r.addTickable(this);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double _dt) {
        Player player = world.findEntitiesOfType(Player.class).getFirst();

        if(player.collider.isColliding(collider)) {
            world.findEntitiesOfType(Score.class).getFirst().addScore(10);
            SceneUtils.loadScene(world, sceneToLoad);
        }
    }
}
