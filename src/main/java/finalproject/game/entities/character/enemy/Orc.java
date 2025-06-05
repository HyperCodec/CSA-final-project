package finalproject.game.entities.character.enemy;

import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Axis;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.AnimationController;
import finalproject.game.components.renderables.sprite.flippable.FlippableAnimatedSprite;
import finalproject.game.components.tickables.ai.FacedBLPathing;
import finalproject.game.components.tickables.ai.PathTweener;
import finalproject.game.entities.attack.hitbox.RepelHitbox;
import finalproject.game.entities.character.Player;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Orc extends Enemy implements Tickable {
    public final static double WALK_SPEED = 25;
    public final static double MIN_PAUSE_TIME = 0.5;
    public final static double MAX_PAUSE_TIME = 5.0;
    public final static double AGGRO_RANGE = 100.0;
    public final static double ATTEMPT_ATTACK_RANGE = 40.0;
    public final static Vec2 ATTACK_HITBOX_SIZE = new Vec2(20, 20);
    public final static double ATTACK_DAMAGE = 10;

    // a lot of this logic is copied directly
    // from Player, might want to make an actual
    // abstraction for it.
    public final AnimationController animations;
    public final Box<HorizontalDirection> facing;

    public final Box<Boolean> canMove = new BasicBox<>(true);
    public final Timer invincibleTimer = new Timer(Player.INVINCIBILITY_DURATION);

    public final PathTweener wanderPathing;
    public final Box<Boolean> isWandering = new BasicBox<>(true);
    public boolean wanderPaused = false;
    public boolean aggro = false;
    public boolean attackActive = false;
    public boolean endlag = false;
    public Timer wanderPauseCooldown;
    public Timer attackTimer;
    public final Timer endlagTimer = new Timer(Player.END_LAG * 2);
    public Platform currentPlatform;

    public Orc(Vec2 pos) {
        super(pos, 40, Player.COLLIDER_SIZE, 50);
        facing = new BasicBox<>(HorizontalDirection.RIGHT);
        animations = setupAnimations();
        wanderPathing = new FacedBLPathing(this.pos, isWandering, new ArrayList<>(), 10.0, WALK_SPEED, facing) {
            @Override
            protected void onReachGoal(WorldAccessor world, double dt) {
//                randomNextNode();
                wanderPathing.reverse();

                // go back to node on path
                wanderPathing.nextNode(world, dt);

                ThreadLocalRandom rand = ThreadLocalRandom.current();
                wanderPaused = true;
                wanderPauseCooldown = new Timer(rand.nextDouble(MIN_PAUSE_TIME, MAX_PAUSE_TIME));
                isWandering.set(false);
                animations.setCurrentAnimation("idle");
            }

            @Override
            public void step(WorldAccessor world, double dt) {
                if(!canMove.get()) return;
                animations.setCurrentAnimation("walk");
                super.step(world, dt);
                vel.set(Vec2.ZERO);
            }
        };
        attackTimer = new Timer(animations.getAnimation("attack").getCycleDuration());
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        super.spawn(r);

        r.addTickable(this);
        r.addRenderable(animations);
        r.addTickable(animations);
        r.addTickable(wanderPathing);
    }

    @Override
    protected void onDamage(WorldAccessor world, double amount) {
        animations.forceCurrentAnimation("hurt");
        animations.setCancellable(false);
        intangible.set(true);
        invincibleTimer.reset();
        canMove.set(false);
        AudioSource hurt;
        try {
            hurt = ResourceUtils.readAudio(Player.class, "assets/sounds/orc/hurt.wav", 0);
        } catch (URISyntaxException | UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        hurt.play();
    }

    @Override
    protected void onDeath(@NotNull WorldAccessor world) {
        world.addEntity(new DyingOrc(pos.get().addY(Player.RENDER_Y_OFFSET)));
        world.destroyEntity(this);
        world.findEntitiesOfType(Score.class).getFirst().addScore(1);

        AudioSource death;
        try {
            death = ResourceUtils.readAudio(Player.class, "assets/sounds/orc/death.wav", 0);
        } catch (URISyntaxException | UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        death.play();
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
        if(intangible.get() && invincibleTimer.tick(dt)) {
            intangible.set(false);
            animations.setCurrentAnimation("idle");
        }

        if(attackActive && attackTimer.tick(dt)) {
            attackActive = false;
            endlag = true;
            endlagTimer.reset();
            canMove.set(true);
            animations.setCurrentAnimation("idle");
        }

        if(endlag && endlagTimer.tick(dt)) {
            endlag = false;
            attackTimer.reset();
        }

        if(wanderPaused && wanderPauseCooldown.tick(dt)) {
            wanderPaused = false;
            isWandering.set(true);
        }

        Player player = world.findEntitiesOfType(Player.class).getFirst();
        Vec2 playerPos = player.pos.get();

        double playerX = playerPos.getX();

        Vec2 delta = playerPos.sub(pos.get());
        double distSq = delta.magSq();

        if(canMove.get() && distSq <= AGGRO_RANGE * AGGRO_RANGE) {
            isWandering.set(false);
            aggro = true;

            VectorComponent horizontal = delta.getComponent(Axis.X);
            facing.set(CardinalDirection.fromComponent(horizontal).toHorizontal());

            List<Vec2> topEdges = currentPlatform.getTopEdges();
            Vec2 leftEdge = topEdges.getFirst();
            Vec2 rightEdge = topEdges.getLast();

            if(!attackActive) {
                if (playerX >= leftEdge.getX() && playerX <= rightEdge.getX() && Math.abs(playerX - pos.get().getX()) >= 5) {
                    vel.set(Vec2.ZERO);
                    pos.set(pos.get().add(facing.get().toVector().mul(WALK_SPEED * dt)));
                    animations.setCurrentAnimation("walk");
                } else {
                    animations.setCurrentAnimation("idle");
                }

                if (!endlag && distSq <= ATTEMPT_ATTACK_RANGE * ATTEMPT_ATTACK_RANGE) {
                    attackActive = true;
                    animations.setCurrentAnimation("attack");
//                attackTimer.reset();

                    AudioSource attackSFX;
                    try {
                        attackSFX = ResourceUtils.readAudio(Player.class, "assets/sounds/slash.wav", 0);
                    } catch (URISyntaxException | UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                        throw new RuntimeException(e);
                    }
                    attackSFX.play();

                    // TODO only push in facing
                    // direction instead of full
                    // RepelHitbox.
                    Box<Vec2> hitboxPos = new GetModifier<Vec2>(pos, pos2 -> pos2.add(facing.get().toVector().mul(Player.COLLIDER_SIZE.getX() / 2)));
                    world.addChildEntity(
                            this,
                            new RepelHitbox(
                                    hitboxPos,
                                    new RectCollider(hitboxPos, new BasicBox<>(ATTACK_HITBOX_SIZE)),
                                    this,
                                    attackTimer.getDuration(),
                                    ATTACK_DAMAGE,
                                    10
                            )
                    );
                }
            }
        } else if(aggro) {
            isWandering.set(true);
            aggro = false;
        }
    }

    @Override
    protected void onTouchPlatform(WorldAccessor world, @NotNull Platform platform) {
        wanderPathing.setPath(platform.getTopEdges());
        currentPlatform = platform;
        if(!intangible.get())
            canMove.set(true);
    }

    // probably want to make a general abstraction for this
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
                "idle", createAnimation(TextureManager.Orc.IDLE_ANIMATION),
                "walk", createAnimation(TextureManager.Orc.WALK_ANIMATION),
                "hurt", createAnimation(TextureManager.Orc.HURT_ANIMATION),
                "attack", createAnimation(TextureManager.Orc.ATTACK_ANIMATION),
                "death", createAnimation(TextureManager.Orc.DEATH_ANIMATION)
        ), "idle");
    }
}
