package finalproject.game.components.renderables;

import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.components.renderables.sprite.Sprite;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class AnimationController extends Sprite {
    Map<String, AnimatedSprite> animations;
    String currentAnimation;

    public AnimationController(Box<Vec2> pos, Map<String, AnimatedSprite> animations, String defaultAnimation) {
        super(pos);

        this.animations = animations;
        currentAnimation = defaultAnimation;
    }

    public AnimationController(Box<Vec2> pos, Map<String, AnimatedSprite> animations) {
        this(pos, animations, null);
    }

    public AnimationController(Box<Vec2> pos) {
        this(pos, new HashMap<>());
    }

    public void setCurrentAnimation(String name) {
        if(name == null) {
            // effectively disable rendering
            currentAnimation = null;
            return;
        }

        if(name.equals(currentAnimation)) return;
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

    public void addTickables(EntityComponentRegistry r) {
        for(AnimatedSprite sprite : animations.values())
            r.addTickable(sprite);
    }

    @Override
    public void renderAtPos(Graphics g, Vec2 pos) {
        AnimatedSprite current = animations.get(currentAnimation);
        if(current != null)
            current.renderAtPos(g, pos);
    }
}
