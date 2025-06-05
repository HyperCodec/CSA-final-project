package finalproject.game.entities.environment;

import finalproject.engine.Camera;
import finalproject.engine.ecs.Entity;
import finalproject.engine.ecs.EntityComponentRegistry;
import finalproject.engine.ecs.Renderable;
import finalproject.engine.util.Vec2;
import finalproject.engine.util.box.BasicBox;
import finalproject.engine.util.box.Box;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class BackgroundImage implements Entity, Renderable {
   public Image image;
   public final Box<Vec2> pos;
   public double parallax;

   public BackgroundImage(Image image, Vec2 pos, double parallax) {
       this.image = image;
       this.pos = new BasicBox<>(pos);
       this.parallax = parallax;
   }

   public BackgroundImage(Image image, double parallax) {
       this(image, Vec2.ZERO, parallax);
   }

    @Override
    public void spawn(@NotNull EntityComponentRegistry r) {
        r.addRenderable(this);
    }

    @Override
    public void render(@NotNull Graphics g, @NotNull Camera mainCamera) {
        Vec2 screenPos = mainCamera.getScreenPos(pos.get(), parallax);
        g.drawImage(image, (int) (screenPos.getX() - (double) image.getWidth(null) / 2), (int) (screenPos.getY() - (double) image.getHeight(null) / 2), null);
    }

    @Override
    public int getLayer() {
        return 1;
    }
}
