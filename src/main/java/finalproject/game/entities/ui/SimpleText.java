package finalproject.game.entities.ui;

import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import finalproject.game.components.renderables.sprite.TextSprite;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class SimpleText implements Entity {
    public final Box<String> text;
    public final Box<Vec2> pos;

    public SimpleText(String text, Vec2 pos) {
        this.text = new BasicBox<>(text);
        this.pos = new BasicBox<>(pos);
    }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(new TextSprite(pos, text, Color.BLACK, Font.getFont("arial")));
    }
}
