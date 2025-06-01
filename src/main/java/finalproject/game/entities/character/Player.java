package finalproject.game.entities.character;

import finalproject.engine.util.box.BasicBox;
import finalproject.game.components.markers.physics.colliders.AlignableCollider;
import finalproject.game.components.markers.physics.Rigidbody;
import finalproject.game.components.markers.physics.colliders.EllipseCollider;
import finalproject.game.components.renderables.AnimationController;
import finalproject.game.components.renderables.sprite.flippable.FlippableAnimatedSprite;
import finalproject.game.components.tickables.Dash;
import finalproject.game.components.tickables.physics.Drag;
import finalproject.game.components.tickables.physics.Gravity;
import finalproject.game.components.tickables.physics.PlatformCollision;
import finalproject.game.components.tickables.physics.VelocityPositionUpdater;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;
import finalproject.game.entities.attack.Dynamite;
import finalproject.game.util.custombox.BiasedViewBox;
import finalproject.game.util.physics.CardinalDirection;
import finalproject.engine.util.Vec2;
import finalproject.game.util.physics.HorizontalDirection;
import finalproject.game.util.rendering.SpriteSheet;
import finalproject.game.util.rendering.TextureManager;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Map;
import java.util.List;

public class Player implements Entity, Tickable {
    public final static double MOVE_SPEED = 50;
    public final static double FAST_FALL_VELOCITY = 20;
    public final static double JUMP_SPEED = 100;
    public final static double JUMP_END_VELOCITY = 5;
    public final static double MAX_JUMP_TIME = 0.4;
    public final static double DASH_COOLDOWN = 3;
    public final static double DASH_SPEED = 500;
    public final static double DASH_DURATION = 0.25;
    public final static double ANIMATION_FRAME_TIME = 0.1;
    public final static double RENDER_Y_OFFSET = 2;

    // prob shouldn't be making these public, but
    // I hate using getters/setters and entities
    // aren't part of the main abstraction anyway
    public final Box<Vec2> pos;
    public final Box<Vec2> vel = new BasicBox<>(Vec2.ZERO);
    public final Box<Boolean> grounded = new BasicBox<>(false);
    public final Box<Double> fallDuration = new BasicBox<>(0.0);
    public final Box<Boolean> useGravity = new BasicBox<>(true);
    public final Box<Boolean> canMove = new BasicBox<>(true);
    public final Box<HorizontalDirection> facing = new BasicBox<>(HorizontalDirection.RIGHT);
    public final AnimationController animationController;

    double jumpHeldDuration = 0;
    boolean isJumping = false;

    public Player(Vec2 pos) {
        this.pos = new BasicBox<>(pos);
        animationController = setupAnimations();
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        Gravity gravity = new Gravity(vel, useGravity);
        r.addTickable(gravity);

        Rigidbody rb = new Rigidbody(10, vel);
        r.addMarker(rb);

        Box<Vec2> ellipseDims = new BasicBox<>(new Vec2(10, 20));
        AlignableCollider collider = new EllipseCollider(pos, ellipseDims);
        r.addMarker(collider);

        PlatformCollision controller = new PlatformCollision(collider, vel, grounded, fallDuration);
        r.addTickable(controller);

        Drag drag = new Drag(0.25, rb);
        r.addTickable(drag);

        VelocityPositionUpdater updater = new VelocityPositionUpdater(pos, vel);
        r.addTickable(updater);

        Dash dash = new Dash(pos, facing, canMove, DASH_COOLDOWN, DASH_DURATION, DASH_SPEED);
        r.addTickable(dash);
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

//        Sprite sprite = new EllipseSprite(pos, Color.cyan, ellipseDims);
//        r.addRenderable(sprite);
        r.addRenderable(animationController);

        animationController.addTickables(r);
        r.addTickable(this);
    }

    @Override
    public void tick(@NotNull WorldAccessor world, double dt) {
        if(canMove.get()) {
            handleMovement(world, dt);
        }
        handleAttack(world);
    }

    private void handleMovement(@NotNull WorldAccessor world, double dt) {
        HashSet<CardinalDirection> heldDirections = world.getHeldDirections();

        Vec2 pos2 = pos.get();

        boolean isMoving = false;
        CardinalDirection[] lr = {CardinalDirection.LEFT, CardinalDirection.RIGHT};
        for (CardinalDirection dir : lr) {
            if (heldDirections.contains(dir)) {
                pos2 = pos2.add(dir.toVector().mulSingle(MOVE_SPEED * dt));
                facing.set(dir.toHorizontal());
                isMoving = true;
            }
        }

        if(isMoving) {
            animationController.setCurrentAnimation("walk");
        } else if(!animationController.getCurrentAnimationName().equals("hurt")) {
            animationController.setCurrentAnimation("idle");
        }

        if(heldDirections.contains(CardinalDirection.DOWN))
            vel.set(new Vec2(vel.get().getX(), FAST_FALL_VELOCITY));

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

    // TODO replace with sword attack
    public final static double THROW_VELOCITY = 15;
    public final static double DYNAMITE_TIME = 3;

    private void handleAttack(@NotNull WorldAccessor world) {
        if(world.mouseJustStartedClick()) {
            Vec2 pos2 = pos.get();
            Vec2 mousePos = world.getMousePos();
            Vec2 absoluteMousePos = world.getMainCamera().getAbsolutePos(mousePos);

            Vec2 direction = absoluteMousePos.sub(pos2).norm();
            Vec2 projVel = direction.mulSingle(THROW_VELOCITY).add(vel.get());

            world.addEntity(new Dynamite(pos2, projVel, DYNAMITE_TIME, this));
        }
    }

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(List<BufferedImage> frames) {
        return new FlippableAnimatedSprite(new BiasedViewBox<>(pos, pos2 -> pos2.addY(RENDER_Y_OFFSET)), frames, ANIMATION_FRAME_TIME, facing);
    }

    @Contract("_ -> new")
    private @NotNull FlippableAnimatedSprite createAnimation(@NotNull SpriteSheet sheet) {
        return createAnimation(sheet.getBufferedImages());
    }

    @Contract(" -> new")
    private @NotNull AnimationController setupAnimations() {
        return new AnimationController(new BiasedViewBox<>(pos, pos2 -> pos2.addY(RENDER_Y_OFFSET)), Map.of(
                "idle", createAnimation(TextureManager.Player.IDLE_ANIMATION),
                "walk", createAnimation(TextureManager.Player.WALK_ANIMATION),
                "hurt", createAnimation(TextureManager.Player.HURT_ANIMATION)
                // TODO the rest
        ), "idle");
    }
}
