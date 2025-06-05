package finalproject.game.entities.character.enemy;

import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Axis;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.AnimationController;
import finalproject.game.components.renderables.sprite.flippable.FlippableAnimatedSprite;
import finalproject.game.components.renderables.ui.bar.HealthBar;
import finalproject.game.entities.attack.Dynamite;
import finalproject.game.entities.attack.hitbox.RepelHitbox;
import finalproject.game.entities.character.Player;
import finalproject.game.entities.environment.Flag;
import finalproject.game.entities.environment.Platform;
import finalproject.game.entities.ui.Score;
import finalproject.game.util.AudioSource;
import finalproject.game.util.ResourceUtils;
import finalproject.game.util.Timer;
import finalproject.game.util.custombox.mapping.GetModifier;
import finalproject.game.util.physics.CardinalDirection;
import finalproject.game.util.physics.HorizontalDirection;
import finalproject.game.util.rendering.SpriteSheet;
import finalproject.game.util.rendering.TextureManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class GiantOrc extends Enemy implements Tickable {
    public final static double HEALTH_BAR_OFFSET = 50;
    public final static Vec2 HEALTH_BAR_SIZE = new Vec2(100, 3);
    public final static double MIN_COOLDOWN_TIME = 0.5;
    public final static double MAX_COOLDOWN_TIME = 3.0;
    public final static Vec2 ATTACK_HITBOX_SIZE = new Vec2(50, 60);
    public final static double ATTACK_DAMAGE = 10;
    public final static double STARTUP_TIME = 5 * Player.ANIMATION_FRAME_TIME;
    public final static double DYNAMITE_SPEED = 10;
    public final static double MIN_DYNAMITE_TIME = 1;
    public final static double MAX_DYNAMITE_TIME = 5;

    public final AnimationController animations;
    public final Box<HorizontalDirection> facing = new BasicBox<>(HorizontalDirection.RIGHT);
    public final Timer invincibleTimer = new Timer(Player.INVINCIBILITY_DURATION);
    public int phase = 1;
    public Timer attackCooldown = new Timer(MAX_COOLDOWN_TIME);
    public final Timer meleeStartupTimer = new Timer(STARTUP_TIME);
    public final Timer attackTimer;
    public boolean attackActive = false;
    public boolean inCooldown = true;
    public boolean inStartup = false;

    public GiantOrc(Vec2 pos) {
        super(pos, 100, new Vec2(70, 80), 10000);

        animations = setupAnimations();
        attackTimer = new Timer(animations.getAnimation("attack").getCycleDuration());
    }

    @Override
    protected void addHealthBar(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new HealthBar(
                new GetModifier<Vec2>(pos, pos2 -> pos2.addY(HEALTH_BAR_OFFSET)),
                new BasicBox<>(HEALTH_BAR_SIZE),
                health
        ));
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        super.spawn(r);

        r.addRenderable(animations);
        r.addTickable(animations);
        r.addTickable(this);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        vel.set(Vec2.ZERO);

        if(intangible.get() && invincibleTimer.tick(dt))
            intangible.set(false);

        Player player = world.findEntitiesOfType(Player.class).getFirst();

        Vec2 playerPos = player.pos.get();

        Vec2 delta = playerPos.sub(pos.get());
        facing.set(CardinalDirection.fromComponent(delta.getComponent(Axis.X)).toHorizontal());

        double _distSq = delta.magSq();

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        if(attackActive && attackTimer.tick(dt)) {
            attackActive = false;
            attackCooldown = new Timer(rand.nextDouble(MIN_COOLDOWN_TIME, MAX_COOLDOWN_TIME));
            inCooldown = true;
            animations.setCurrentAnimation("idle");
        }

        if(inStartup && meleeStartupTimer.tick(dt)) {
            inStartup = false;
            Box<Vec2> hitboxPos = new GetModifier<Vec2>(pos, pos2 -> pos2.add(facing.get().toVector().mul(35)).addY(25));
            world.addChildEntity(this, new RepelHitbox(
                    hitboxPos,
                    new RectCollider(hitboxPos, new BasicBox<>(ATTACK_HITBOX_SIZE)),
                    this,
                    attackTimer.getDuration() - STARTUP_TIME,
                    ATTACK_DAMAGE,
                    10
            ));
        }

        if(inCooldown && attackCooldown.tick(dt)) {
            inCooldown = false;
            int attack = rand.nextInt(phase);

            switch(attack) {
                case 0:
                    meleeAttack();
                    break;
                case 1:
                    dynamiteAttack(world);
                    break;
            }
        }
    }

    private void meleeAttack() {
        attackActive = true;
        inStartup = true;
        meleeStartupTimer.reset();
        attackTimer.reset();

        animations.setCurrentAnimation("attack");
    }

    private void dynamiteAttack(WorldAccessor world) {
        attackActive = true;
        attackTimer.reset();

        ThreadLocalRandom rand = ThreadLocalRandom.current();

        Vec2 spawnPos = pos.get().subY(35);
        for(double angle = 0; angle < Math.PI; angle += Math.PI / 6) {
            double x = Math.cos(angle);
            double y = -Math.sin(angle);

            Vec2 vel = new Vec2(x, y).mul(DYNAMITE_SPEED);

            world.addChildEntity(this, new Dynamite(spawnPos, vel, rand.nextDouble(MIN_DYNAMITE_TIME, MAX_DYNAMITE_TIME), this));
        }
    }

    @Override
    protected void onDamage(WorldAccessor world, double amount) {
        intangible.set(true);
        invincibleTimer.reset();
        AudioSource hurt;
        try {
            hurt = ResourceUtils.readAudio(Player.class, "assets/sounds/orc/hurt.wav", 0);
        } catch (URISyntaxException | UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        hurt.play();
        if(health.getHealth() <= 50)
            phase = 2;
    }

    @Override
    protected void onDeath(WorldAccessor world) {
        AudioSource death;
        try {
            death = ResourceUtils.readAudio(Player.class, "assets/sounds/orc/death.wav", 0);
        } catch (URISyntaxException | UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        death.play();
        world.destroyEntity(this);
        world.findEntitiesOfType(Score.class).getFirst().addScore(5);
        try {
            world.addEntity(new Flag(pos.get(), "levels/win.json"));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onTouchPlatform(WorldAccessor world, Platform platform) {}

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(List<BufferedImage> frames) {
        return new FlippableAnimatedSprite(new GetModifier<Vec2>(pos, pos2 -> pos2.addY(Player.RENDER_Y_OFFSET)), frames, Player.ANIMATION_FRAME_TIME, facing);
    }

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(@NotNull SpriteSheet sheet) {
        return createAnimation(sheet.getBufferedImages());
    }

    @Contract(" -> new")
    private @NotNull AnimationController setupAnimations() {
        return new AnimationController(new GetModifier<Vec2>(pos, pos2 -> pos2.addY(Player.RENDER_Y_OFFSET)), Map.of(
                "idle", createAnimation(TextureManager.GiantOrc.IDLE_ANIMATION),
                "attack", createAnimation(TextureManager.GiantOrc.ATTACK_ANIMATION)
        ), "idle");
    }
}
