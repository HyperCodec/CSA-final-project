package finalproject.game.entities.character.enemy;

import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.game.components.renderables.ui.bar.HealthBar;
import finalproject.game.entities.character.LivingEntity;
import finalproject.game.util.custombox.mapping.GetModifier;
import org.jetbrains.annotations.NotNull;

public abstract class Enemy extends LivingEntity {
    public final static Vec2 HEALTH_BAR_SIZE = new Vec2(25, 3);
    public final static double HEALTH_BAR_OFFSET = 15;

    protected Enemy(Vec2 pos, double maxHealth, Vec2 colliderDims, double mass) {
        super(pos, maxHealth, colliderDims, mass);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        super.spawn(r);
        addHealthBar(r);
    }

    protected void addHealthBar(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new HealthBar(
                new GetModifier<Vec2>(pos, pos2 -> pos2.addY(HEALTH_BAR_OFFSET)),
                new BasicBox<>(HEALTH_BAR_SIZE),
                health
        ));
    }
}
