package finalproject.game.entities.attack;

import finalproject.engine.util.Vec2;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.Rigidbody;
import finalproject.game.components.markers.physics.colliders.AlignableCollider;
import finalproject.game.components.markers.physics.colliders.CircleCollider;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.components.tickables.physics.Drag;
import finalproject.game.components.tickables.physics.Gravity;
import finalproject.game.components.tickables.physics.GeneralCollision;
import finalproject.game.components.tickables.physics.VelocityPositionUpdater;
import finalproject.game.entities.attack.hitbox.Explosion;
import finalproject.game.util.ResourceUtils;
import finalproject.game.util.Timer;
import finalproject.game.util.AudioSource;
import finalproject.game.util.rendering.SpriteSheet;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class Dynamite implements Entity, Tickable {
    final static SpriteSheet DYNAMITE_TEXTURE;

    static {
        try {
            DYNAMITE_TEXTURE = new SpriteSheet(ResourceUtils.readImage(Dynamite.class, "assets/textures/dynamite/dynamite.png"), 3, 3);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static double EXPLOSION_RADIUS = 25;
    public final static double DAMAGE = 50;
    public final static double COLLIDER_RADIUS = 5;

    public final Box<Vec2> pos;
    public final Box<Vec2> vel;
    public final Timer timer;
    public final Entity owner;
    public final AlignableCollider collider;

    public Dynamite(Vec2 pos, Vec2 vel, double time, Entity owner) {
        this.pos = new BasicBox<>(pos);
        this.vel = new BasicBox<>(vel);
        timer = new Timer(time);
        this.owner = owner;
        collider = new CircleCollider(this.pos, COLLIDER_RADIUS);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addMarker(collider);

        Rigidbody rb = new Rigidbody(1, vel);
        r.addMarker(rb);

        r.addTickable(new GeneralCollision(collider, vel));
        r.addTickable(new Drag(0.1, rb));
        r.addTickable(new Gravity(vel));
        r.addTickable(new VelocityPositionUpdater(pos, vel));
        r.addTickable(this);

        List<Image> frames = DYNAMITE_TEXTURE.getImages();
        double frameTime = timer.getDuration() / frames.size();

        AnimatedSprite sprite = new AnimatedSprite(pos, frames, frameTime);
        r.addRenderable(sprite);
        r.addTickable(sprite);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        if(!timer.tick(dt)) return;

        explode(world);
    }

    public void explode(@NotNull WorldAccessor world) {
        try {
            AudioSource explosionSFX = ResourceUtils.readAudio(Dynamite.class, "assets/sounds/explosion.wav", 0);
            explosionSFX.play();
        } catch (UnsupportedAudioFileException | LineUnavailableException | URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        world.addEntity(new Explosion(pos.get(), EXPLOSION_RADIUS, owner, DAMAGE));
        world.destroyEntity(this);
    }
}
