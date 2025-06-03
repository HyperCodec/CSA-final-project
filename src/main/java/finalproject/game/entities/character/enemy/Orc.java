package finalproject.game.entities.character.enemy;

import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.Axis;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.renderables.AnimationController;
import finalproject.game.components.renderables.sprite.flippable.FlippableAnimatedSprite;
import finalproject.game.entities.character.Player;
import finalproject.game.util.Timer;
import finalproject.game.util.custombox.mapping.ReadModifier;
import finalproject.game.util.physics.CardinalDirection;
import finalproject.game.util.physics.HorizontalDirection;
import finalproject.game.util.rendering.SpriteSheet;
import finalproject.game.util.rendering.TextureManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class Orc extends Enemy implements Tickable {
    // a lot of this logic is copied directly
    // from Player, might want to make an actual
    // abstraction for it.
    public final AnimationController animations;
    public final Box<HorizontalDirection> facing;

    public final Box<Boolean> canMove = new BasicBox<>(true);
    public final Timer invincibleTimer = new Timer(Player.INVINCIBILITY_DURATION);

    public Orc(Vec2 pos) {
        super(pos, 40, Player.COLLIDER_SIZE, 50);
        facing = new BasicBox<>(HorizontalDirection.RIGHT);
        animations = setupAnimations();
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        super.spawn(r);

        r.addTickable(this);
        r.addRenderable(animations);
        r.addTickable(animations);
    }

    @Override
    protected void onDamage(WorldAccessor world, double amount) {
        animations.forceCurrentAnimation("hurt");
        animations.setCancellable(false);
        intangible.set(true);
        invincibleTimer.reset();
        canMove.set(false);
    }

    @Override
    protected void onDeath(@NotNull WorldAccessor world) {
        // TODO death animation
        // i think for the enemy death
        // animations it can be pretty straightforward.
        // could just spawn an empty AnimatedSprite entity
        // with a despawn cooldown and immediately destroy this entity.
        world.destroyEntity(this);
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
        if(intangible.get() && invincibleTimer.tick(dt)) {
            intangible.set(false);
            animations.setCurrentAnimation("idle");
            canMove.set(true);
        }

        // always track player
        Player player = world.findEntitiesOfType(Player.class).getFirst();
        Vec2 playerPos = player.pos.get();

        Vec2 delta = playerPos.sub(pos.get());
        VectorComponent horizontal = delta.getComponent(Axis.X);
        facing.set(CardinalDirection.fromComponent(horizontal).toHorizontal());

        // TODO movement and attacks
    }

    // probably want to make a general abstraction for this
    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(List<BufferedImage> frames) {
        return new FlippableAnimatedSprite(new ReadModifier<Vec2>(pos, pos2 -> pos2.addY(Player.RENDER_Y_OFFSET)), frames, Player.ANIMATION_FRAME_TIME, facing);
    }

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(@NotNull SpriteSheet sheet) {
        return createAnimation(sheet.getBufferedImages());
    }

    @Contract(" -> new")
    private @NotNull AnimationController setupAnimations() {
        return new AnimationController(new ReadModifier<Vec2>(pos, pos2 -> pos2.addY(Player.RENDER_Y_OFFSET)), Map.of(
                "idle", createAnimation(TextureManager.Orc.IDLE_ANIMATION),
                "walk", createAnimation(TextureManager.Orc.WALK_ANIMATION),
                "hurt", createAnimation(TextureManager.Orc.HURT_ANIMATION),
                "meleeAttack", createAnimation(TextureManager.Orc.ATTACK_ANIMATION),
                "death", createAnimation(TextureManager.Orc.DEATH_ANIMATION)
        ), "idle");
    }
}
