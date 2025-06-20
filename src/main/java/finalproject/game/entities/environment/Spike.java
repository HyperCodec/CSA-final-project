package finalproject.game.entities.environment;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.CircleCollider;
import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.sprite.ImageSprite;
import finalproject.game.entities.attack.hitbox.Hitbox;
import finalproject.game.entities.attack.hitbox.RepelHitbox;
import finalproject.game.util.ResourceUtils;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class Spike implements Entity {
    final static Image TEXTURE;

    static {
        try {
            TEXTURE = ResourceUtils.readImage(Spike.class, "assets/textures/environment/spike.png");
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static double DAMAGE = 5;

    public final Box<Vec2> pos;

    public Spike(Vec2 pos) {
        this.pos = new BasicBox<>(pos);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        Hitbox hb = new RepelHitbox(
                pos,
                new CircleCollider(
                        pos,
                        (double) TEXTURE.getWidth(null) / 2
                ),
                null,
                DAMAGE,
                10000
        );
        r.addChildEntity(hb);

        ImageSprite sprite = new ImageSprite(pos, TEXTURE);
        r.addRenderable(sprite);
    }
}
