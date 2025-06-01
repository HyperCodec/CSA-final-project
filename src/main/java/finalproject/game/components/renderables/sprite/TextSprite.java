package finalproject.game.components.renderables.sprite;

import finalproject.engine.util.box.Box;
import finalproject.engine.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TextSprite extends Sprite {
    Box<String> text;
    Color color;
    Font font;

    public TextSprite(Box<Vec2> pos, Box<String> text, Color color, Font font, int layer) {
        super(pos, layer);

        this.text = text;
        this.color = color;
        this.font = font;
    }

    public TextSprite(Box<Vec2> pos, Box<String> text, Color color, Font font) {
        super(pos);

        this.text = text;
        this.color = color;
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    @Override
    public void renderAtPos(@NotNull Graphics g, @NotNull Vec2 pos) {
        g.setColor(color);
        g.setFont(font);

        g.drawString(text.get(), (int) pos.getX(), (int) pos.getY());
    }
}
