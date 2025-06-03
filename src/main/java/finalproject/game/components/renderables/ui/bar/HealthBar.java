package finalproject.game.components.renderables.ui.bar;

import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import finalproject.game.components.markers.Damageable;

import java.awt.*;

public class HealthBar extends PercentageBar {
    Damageable health;

    public HealthBar(Box<Vec2> pos, Box<Vec2> dimensions, Damageable health, Color backgroundColor, Color barColor) {
        super(pos, dimensions, backgroundColor, barColor);
        this.health = health;
    }

    public HealthBar(Box<Vec2> pos, Box<Vec2> dimensions, Damageable health) {
        this(pos, dimensions, health, Color.DARK_GRAY, Color.RED);
    }

    @Override
    public double getPercentage() {
        return health.getPercentHealth();
    }
}
