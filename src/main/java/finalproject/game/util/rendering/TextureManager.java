package finalproject.game.util.rendering;

import finalproject.game.util.ResourceUtils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;

// puts all the textures/tilemaps in their
// own static variables for ease of use in JSON.
// could've prob done this with reflection but then
// everything would be super cursed.
public class TextureManager {
    public final static TileMap GRASS_TILE;
    public final static TileMap POPPIES_TILE;
    public final static TileMap DANDELIONS_TILE;

    static {
        try {
            SpriteSheet grassSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/grass.png"), 1, 3);
            ArrayList<BufferedImage> grassImages = grassSheet.getImages();
            Iterator<BufferedImage> grassIter = grassImages.iterator();

            GRASS_TILE = new TileMap(grassIter.next());
            POPPIES_TILE = new TileMap(grassIter.next());
            DANDELIONS_TILE = new TileMap(grassIter.next());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static TileMap GROUND_TILE;
    public final static TileMap MOSSY_GROUND_TILE;
    public final static TileMap SMOOTH_GROUND_TILE;

    static {
        try {
            SpriteSheet groundSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/ground.png"), 1, 3);
            ArrayList<BufferedImage> groundImages = groundSheet.getImages();
            Iterator<BufferedImage> groundIter = groundImages.iterator();

            GROUND_TILE = new TileMap(groundIter.next());
            MOSSY_GROUND_TILE = new TileMap(groundIter.next());
            SMOOTH_GROUND_TILE = new TileMap(groundIter.next());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static TileMap ROCK_TILE;
    public final static TileMap MOSSY_ROCK_TILE;
    public final static TileMap DIRTY_ROCK_TILE;

    static {
        try {
            SpriteSheet rockSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/rock.png"), 1, 3);
            ArrayList<BufferedImage> rockImages = rockSheet.getImages();
            Iterator<BufferedImage> rockIter = rockImages.iterator();

            ROCK_TILE = new TileMap(rockIter.next());
            MOSSY_ROCK_TILE = new TileMap(rockIter.next());
            DIRTY_ROCK_TILE = new TileMap(rockIter.next());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static TileMap SAND_TILE;

    static {
        try {
            SAND_TILE = new TileMap(ResourceUtils.readImage(TextureManager.class, "assets/textures/sand.png"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static TileMap ORANGE_BRICK_WALL_TILE;
    public final static TileMap BROWN_BRICK_WALL_TILE;
    public final static TileMap STONE_BRICK_WALL_TILE;

    static {
        try {
            SpriteSheet wallSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/wall.png"), 1, 3);
            ArrayList<BufferedImage> wallImages = wallSheet.getImages();
            Iterator<BufferedImage> wallIter = wallImages.iterator();

            ORANGE_BRICK_WALL_TILE = new TileMap(wallIter.next());
            BROWN_BRICK_WALL_TILE = new TileMap(wallIter.next());
            STONE_BRICK_WALL_TILE = new TileMap(wallIter.next());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    // probably supposed to be
    // an animation but wtv
    public final static TileMap WATER_1_TILE;
    public final static TileMap WATER_2_TILE;

    static {
        try {
            SpriteSheet waterSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/water.png"), 1, 2);
            ArrayList<BufferedImage> waterImages = waterSheet.getImages();
            Iterator<BufferedImage> waterIter = waterImages.iterator();

            WATER_1_TILE = new TileMap(waterIter.next());
            WATER_2_TILE = new TileMap(waterIter.next());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static TileMap PLANKS_1_TILE;
    public final static TileMap PLANKS_2_TILE;
    public final static TileMap LOG_1_TILE;
    public final static TileMap LOG_2_TILE;

    static {
        try {
            SpriteSheet woodSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/wood.png"), 1, 4);
            ArrayList<BufferedImage> woodImages = woodSheet.getImages();
            Iterator<BufferedImage> woodIter = woodImages.iterator();

            PLANKS_1_TILE = new TileMap(woodIter.next());
            PLANKS_2_TILE = new TileMap(woodIter.next());
            LOG_1_TILE = new TileMap(woodIter.next());
            LOG_2_TILE = new TileMap(woodIter.next());
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final static TileMap PLACEHOLDER_TILE;

    static {
        try {
            PLACEHOLDER_TILE = new TileMap(ResourceUtils.readImage(TextureManager.class, "assets/textures/tiles/placeholder.png"));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
