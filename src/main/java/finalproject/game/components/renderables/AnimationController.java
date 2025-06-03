package finalproject.game.components.renderables;

import finalproject.engine.ecs.Tickable;
import finalproject.engine.ecs.WorldAccessor;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.components.renderables.sprite.Sprite;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AnimationController extends Sprite implements Tickable {
    Map<String, AnimatedSprite> animations;
    String currentAnimation;
    boolean cancellable = true;
    boolean ephemeral = false;
    String queuedAnimation = null;
    final String defaultAnimation;

    public AnimationController(Box<Vec2> pos, Map<String, AnimatedSprite> animations, String defaultAnimation) {
        super(pos);

        this.animations = animations;
        currentAnimation = defaultAnimation;
        this.defaultAnimation = defaultAnimation;
    }

    public AnimationController(Box<Vec2> pos, Map<String, AnimatedSprite> animations) {
        this(pos, animations, null);
    }

    public AnimationController(Box<Vec2> pos) {
        this(pos, new HashMap<>());
    }

    /**
     * Sets whether the current animation can be canceled.
     * If it can't be canceled, the newest attempted animation will
     * be queued and then run after the current animation is finished.
     * @param cancellable Whether the current animation can be canceled.
     */
    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCurrentAnimation(String name) {
        if(name == null) {
            // effectively disable rendering
            currentAnimation = null;
            return;
        }

        if(name.equals(currentAnimation)) return;

        if(!cancellable) {
            queuedAnimation = name;
            animations.get(name).reset();
            return;
        }

        forceCurrentAnimation(name);
    }

    // goes back to default animation when the current one ends
    public void setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
    }

    public void forceCurrentAnimation(String name) {
        currentAnimation = name;
        animations.get(currentAnimation).reset();
    }

    public void resetCurrentAnimation() {
        animations.get(currentAnimation).reset();
    }

    public String getCurrentAnimationName() {
        return currentAnimation;
    }

    public AnimatedSprite getCurrentAnimationSprite() {
        return animations.get(currentAnimation);
    }

    public AnimatedSprite getAnimation(String name) {
        return animations.get(name);
    }

    public void addAnimation(String name, AnimatedSprite sprite) {
        animations.put(name, sprite);
    }

    @Override
    public void renderAtPos(Graphics g, Vec2 pos) {
        AnimatedSprite current = animations.get(currentAnimation);
        if(current != null)
            current.renderAtPos(g, pos);
    }

    @Override
    public void tick(WorldAccessor world, double dt) {
        AnimatedSprite current = animations.get(currentAnimation);
        current.tick(world, dt);

        if(current.justFinishedCycle()) {
            cancellable = true;
            if(ephemeral) {
                setCurrentAnimation(defaultAnimation);
                ephemeral = false;
            }
        }

        if(queuedAnimation != null) {
            AnimatedSprite queued = animations.get(queuedAnimation);
            queued.tick(world, dt);

            if(cancellable) {
                setCurrentAnimation(queuedAnimation);
                queuedAnimation = null;
            }
        }
    }
}
