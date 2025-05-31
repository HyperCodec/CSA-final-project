package finalproject.game.components.renderables.sprite;

import finalproject.game.util.Box;
import finalproject.game.util.Vec2;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class TextSprite extends Sprite {
    Box<String> text;
    Color color;
    Font font;

    public TextSprite(Box<Vec2> pos, Box<String> text, Color color, Font font) {
        super(pos);

        this.text = text;
        this.color = color;
        this.font = font;
    }

    @Override
    public void render(@NotNull Graphics g) {
        g.setColor(color);
        g.setFont(font);

        Vec2 pos2 = pos.get();

        g.drawString(text.get(), (int) pos2.getX(), (int) pos2.getY());
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
}
