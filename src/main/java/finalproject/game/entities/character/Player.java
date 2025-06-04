package finalproject.game.entities.character;

import finalproject.engine.Camera;
import finalproject.engine.util.Axis;
import finalproject.engine.util.VectorComponent;
import finalproject.engine.util.box.BasicBox;
import finalproject.game.components.markers.physics.colliders.RectCollider;
import finalproject.game.components.renderables.AnimationController;
import finalproject.game.components.renderables.sprite.flippable.FlippableAnimatedSprite;
import finalproject.game.components.renderables.ui.bar.HealthBar;
import finalproject.game.components.renderables.ui.bar.TimerBar;
import finalproject.game.components.tickables.Dash;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;
import finalproject.game.entities.attack.hitbox.RepelHitbox;
import finalproject.game.entities.environment.Platform;
import finalproject.game.util.Timer;
import finalproject.game.util.custombox.mapping.GetModifier;
import finalproject.engine.util.box.ScreenToAbsolute;
import finalproject.game.util.physics.CardinalDirection;
import finalproject.engine.util.Vec2;
import finalproject.game.util.physics.HorizontalDirection;
import finalproject.game.util.rendering.SpriteSheet;
import finalproject.game.util.rendering.TextureManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.List;

public class Player extends LivingEntity implements Tickable {
    public final static double MOVE_SPEED = 50;
    public final static double FAST_FALL_VELOCITY = 15;
    public final static double JUMP_SPEED = 100;
    public final static double JUMP_END_VELOCITY = 5;
    public final static double MAX_JUMP_TIME = 0.4;
    public final static double DASH_COOLDOWN = 3;
    public final static double DASH_SPEED = 500;
    public final static double DASH_DURATION = 0.25;
    public final static double ANIMATION_FRAME_TIME = 0.1;
    public final static double RENDER_Y_OFFSET = 2;
    public final static double END_LAG = 1;
    public final static double MELEE_DAMAGE = 10;
    public final static double INVINCIBILITY_DURATION = 1.25;
    public final static Vec2 COLLIDER_SIZE = new Vec2(12, 20);
    public final static Vec2 MELEE_HITBOX_SIZE = new Vec2(25, 20);

    // UI elements
    public final static Vec2 HEALTH_BAR_POS = new Vec2(30, 15);
    public final static Vec2 HEALTH_BAR_SIZE = new Vec2(50, 4);
    public final static Vec2 DASH_BAR_POS = new Vec2(30, 20);
    public final static Vec2 DASH_BAR_SIZE = new Vec2(50, 4);

    public final Box<Boolean> canMove = new BasicBox<>(true);
    public final Box<HorizontalDirection> facing = new BasicBox<>(HorizontalDirection.RIGHT);
    HorizontalDirection bufferedDirection = null;
    public final AnimationController animations;

    public final Box<Boolean> attackActive = new BasicBox<>(false);
    public final Box<Boolean> endlag = new BasicBox<>(false);

    Timer attackTimer;
    public final Timer endlagTimer = new Timer(END_LAG);
    public final Timer invincibleTimer = new Timer(INVINCIBILITY_DURATION);

    double jumpHeldDuration = 0;
    boolean isJumping = false;

    public Player(Vec2 pos) {
        super(pos, 100, COLLIDER_SIZE, 50);
        animations = setupAnimations();
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
    protected void onDeath(WorldAccessor world) {
        // TODO death animation
    }

    @Override
    protected void onTouchPlatform(WorldAccessor world, Platform platform) {}

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        super.spawn(r);

        WorldAccessor world = r.getWorldAccessor();

        Camera camera = world.makeCamera(pos);
        world.setMainCamera(camera);

        // converting back and forth between absolute and screen coords
        // probably isn't efficient, but I don't think adding a boolean
        // to check what type of box we're using is any faster.
        HealthBar healthBar = new HealthBar(
                new ScreenToAbsolute(HEALTH_BAR_POS, camera),
                new BasicBox<>(HEALTH_BAR_SIZE),
                health
        );
        r.addRenderable(healthBar);

        Dash dash = new Dash(pos, vel, facing, canMove, DASH_COOLDOWN, DASH_DURATION, DASH_SPEED);
        r.addTickable(dash);

        TimerBar dashBar = new TimerBar(
                new ScreenToAbsolute(DASH_BAR_POS, camera),
                new BasicBox<>(DASH_BAR_SIZE),
                dash.getCooldownTimer(),
                Color.DARK_GRAY,
                Color.CYAN
        );
        r.addRenderable(dashBar);

//        r.subscribeKeyDown("dash", dash::activate);

        // have to use key listener because
        // sometimes as a player you want to
        // be holding a movement direction while
        // you start the dash.
        r.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_Q) {
                    dash.activate();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}
        });

        r.addTickable(animations);
        r.addRenderable(animations);

        r.addTickable(this);

        r.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                faceMouse(camera, e);
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                faceMouse(camera, e);
            }
        });
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        if(intangible.get() && invincibleTimer.tick(dt)) {
            intangible.set(false);
            animations.setCurrentAnimation("idle");
            canMove.set(true);
        }

        if(attackActive.get()) {
            if(attackTimer.tick(dt)) {
                attackActive.set(false);
                animations.setCurrentAnimation("idle");

                if(bufferedDirection != null)
                    facing.set(bufferedDirection);

                bufferedDirection = null;

                endlag.set(true);
                endlagTimer.reset();
            }
        }

        if(endlag.get() && endlagTimer.tick(dt))
            endlag.set(false);

        if(canMove.get()) {
            handleMovement(world, dt);
        }
        handleAttack(world);
    }

    private void faceMouse(@NotNull Camera camera, @NotNull MouseEvent e) {
        Vec2 mousePos = new Vec2(e.getX(), e.getY());
        faceMouse(camera, mousePos);
    }

    private void faceMouse(@NotNull Camera camera, @NotNull Vec2 mousePos) {
        Vec2 absoluteMousePos = camera.getAbsolutePos(mousePos);

        Vec2 delta = absoluteMousePos.sub(pos.get());
        VectorComponent horizontalDist = delta.getComponent(Axis.X);
        HorizontalDirection direction = CardinalDirection.fromComponent(horizontalDist).toHorizontal();

        if(attackActive.get()) {
            bufferedDirection = direction;
            return;
        }

        facing.set(direction);
        vel.set(new Vec2(0, vel.get().getY()));
    }

    private void handleMovement(@NotNull WorldAccessor world, double dt) {
        HashSet<CardinalDirection> heldDirections = world.getHeldDirections();

        Vec2 pos2 = pos.get();

        boolean isMoving = false;
        CardinalDirection[] lr = {CardinalDirection.LEFT, CardinalDirection.RIGHT};
        for (CardinalDirection dir : lr) {
            if (heldDirections.contains(dir)) {
                pos2 = pos2.add(dir.toVector().mul(MOVE_SPEED * dt));
//                facing.set(dir.toHorizontal());
                isMoving = true;
            }
        }

        // can't make attack uncancellable
        // since I want damage trading to
        // be possible, but I don't want
        // movement cancelling it.
        if(!attackActive.get()) {
            if(isMoving) {
                animations.setCurrentAnimation("walk");
            } else {
                animations.setCurrentAnimation("idle");
            }
        }

        if(heldDirections.contains(CardinalDirection.DOWN)) {
            vel.set(new Vec2(vel.get().getX(), FAST_FALL_VELOCITY));
            fallThroughPlatforms.set(true);
        } else {
            fallThroughPlatforms.set(false);
        }

        pos.set(pos2);

        if(heldDirections.contains(CardinalDirection.UP)) {
            if(grounded.get()) {
                isJumping = true;
                grounded.set(false);

                // disable gravity since
                // we're manually handling velocity
                // and don't want to accumulate anything
                // while moving
                useGravity.set(false);
            }

            if(isJumping) {
                jumpHeldDuration += dt;

                if(jumpHeldDuration <= MAX_JUMP_TIME) {
                    pos.set(pos.get().subY(JUMP_SPEED * dt));
                    vel.set(new Vec2(vel.get().getX(), -0.001));
                    return;
                }
            }
        }

        if(isJumping)
            vel.set(new Vec2(vel.get().getX(), -JUMP_END_VELOCITY));
        isJumping = false;
        jumpHeldDuration = 0;
        useGravity.set(true);
    }

    private void handleAttack(@NotNull WorldAccessor world) {
        if(!world.isMouseClicking() || attackActive.get() || endlag.get()) return;

        attack(world);
    }

    private void attack(@NotNull WorldAccessor world) {
        attackActive.set(true);
        attackTimer = new Timer(animations.getAnimation("meleeAttack").getCycleDuration());

        animations.setCurrentAnimation("meleeAttack");

        // TODO slash sound

        // TODO only push in facing
        // direction instead of full
        // RepelHitbox.
        Box<Vec2> hitboxPos = new GetModifier<Vec2>(pos, pos2 -> pos2.add(facing.get().toVector().mul(COLLIDER_SIZE.getX()/2)));
        world.addChildEntity(
                this,
                new RepelHitbox(
                        hitboxPos,
                        new RectCollider(hitboxPos, new BasicBox<>(MELEE_HITBOX_SIZE)),
                        this,
                        attackTimer.getDuration(),
                        MELEE_DAMAGE,
                        10
                )
        );
    }

    // TODO bow attack

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(List<BufferedImage> frames) {
        return new FlippableAnimatedSprite(new GetModifier<Vec2>(pos, pos2 -> pos2.addY(RENDER_Y_OFFSET)), frames, ANIMATION_FRAME_TIME, facing);
    }

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(@NotNull SpriteSheet sheet) {
        return createAnimation(sheet.getBufferedImages());
    }

    @Contract(" -> new")
    private @NotNull AnimationController setupAnimations() {
        return new AnimationController(new GetModifier<Vec2>(pos, pos2 -> pos2.addY(RENDER_Y_OFFSET)), Map.of(
                "idle", createAnimation(TextureManager.Player.IDLE_ANIMATION),
                "walk", createAnimation(TextureManager.Player.WALK_ANIMATION),
                "hurt", createAnimation(TextureManager.Player.HURT_ANIMATION),
                "meleeAttack", createAnimation(TextureManager.Player.ATTACK_ANIMATION),
                "death", createAnimation(TextureManager.Player.DEATH_ANIMATION)
        ), "idle");
    }
}
