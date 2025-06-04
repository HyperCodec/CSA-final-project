package finalproject.game.entities.character.enemy;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.renderables.sprite.AnimatedSprite;
import finalproject.game.components.tickables.DespawnAfterTime;
import finalproject.game.entities.character.Player;
import finalproject.game.util.rendering.TextureManager;
import org.jetbrains.annotations.NotNull;

public class DyingOrc implements Entity {
    public final Box<Vec2> pos;

    public DyingOrc(Vec2 pos) {
        this.pos = new BasicBox<>(pos);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        AnimatedSprite sprite = new AnimatedSprite(pos, TextureManager.Orc.DEATH_ANIMATION.getImages(), Player.ANIMATION_FRAME_TIME);
        r.addRenderable(sprite);
        r.addTickable(sprite);
        r.addTickable(new DespawnAfterTime(this, sprite.getCycleDuration()));
    }
}
