package finalproject.game.components.renderables.ui.bar;

import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.Box;
import finalproject.game.util.Timer;

import java.awt.*;

public class TimerBar extends PercentageBar {
    Timer timer;

    public TimerBar(Box<Vec2> pos, Box<Vec2> dimensions, Timer timer, Color backgroundColor, Color barColor) {
        super(pos, dimensions, backgroundColor, barColor);

        this.timer = timer;
    }

    @Override
    public double getPercentage() {
        return timer.getPercentageElapsed();
    }
}
