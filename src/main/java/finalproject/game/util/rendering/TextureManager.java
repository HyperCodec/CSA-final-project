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
    // splitting this into subclasses makes it
    // easier to find the textures I'm looking for
    // in the IDE autocomplete.
    public static class Environment {
        // splitting up the initialization into a million static
        // blocks was probably not the best idea but whatever
        public final static TileMap GRASS_TILE;
        public final static TileMap POPPIES_TILE;
        public final static TileMap DANDELIONS_TILE;

        static {
            try {
                SpriteSheet grassSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/grass.png"), 1, 3);
                ArrayList<BufferedImage> grassImages = grassSheet.getBufferedImages();
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
                SpriteSheet groundSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/ground.png"), 1, 3);
                ArrayList<BufferedImage> groundImages = groundSheet.getBufferedImages();
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
                SpriteSheet rockSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/rock.png"), 1, 3);
                ArrayList<BufferedImage> rockImages = rockSheet.getBufferedImages();
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
                SAND_TILE = new TileMap(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/sand.png"));
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public final static TileMap ORANGE_BRICK_WALL_TILE;
        public final static TileMap BROWN_BRICK_WALL_TILE;
        public final static TileMap STONE_BRICK_WALL_TILE;

        static {
            try {
                SpriteSheet wallSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/wall.png"), 1, 3);
                ArrayList<BufferedImage> wallImages = wallSheet.getBufferedImages();
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
        public final static TileMap WATER_TILE_1;
        public final static TileMap WATER_TILE_2;

        static {
            try {
                SpriteSheet waterSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/water.png"), 1, 2);
                ArrayList<BufferedImage> waterImages = waterSheet.getBufferedImages();
                Iterator<BufferedImage> waterIter = waterImages.iterator();

                WATER_TILE_1 = new TileMap(waterIter.next());
                WATER_TILE_2 = new TileMap(waterIter.next());
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public final static TileMap PLANKS_TILE_1;
        public final static TileMap PLANKS_TILE_2;
        public final static TileMap LOG_TILE_1;
        public final static TileMap LOG_TILE_2;

        static {
            try {
                SpriteSheet woodSheet = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/wood.png"), 1, 4);
                ArrayList<BufferedImage> woodImages = woodSheet.getBufferedImages();
                Iterator<BufferedImage> woodIter = woodImages.iterator();

                PLANKS_TILE_1 = new TileMap(woodIter.next());
                PLANKS_TILE_2 = new TileMap(woodIter.next());
                LOG_TILE_1 = new TileMap(woodIter.next());
                LOG_TILE_2 = new TileMap(woodIter.next());
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public final static TileMap PLACEHOLDER_TILE;

        static {
            try {
                PLACEHOLDER_TILE = new TileMap(ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/tiles/placeholder.png"));
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }

        public final static BufferedImage BACKGROUND_IMAGE;

        static {
            try {
                BACKGROUND_IMAGE = ResourceUtils.readImage(TextureManager.class, "assets/textures/environment/background.png");
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Player {
        public final static SpriteSheet IDLE_ANIMATION;
        public final static SpriteSheet WALK_ANIMATION;
        public final static SpriteSheet HURT_ANIMATION;
        public final static SpriteSheet MELEE_ATTACK_ANIMATION;
        public final static SpriteSheet BOW_ATTACK_ANIMATION;
        public final static SpriteSheet DEATH_ANIMATION;

        static {
            try {
                IDLE_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/player/idle.png"), 1, 6);
                WALK_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/player/walk.png"), 1, 8);
                HURT_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/player/hurt.png"), 1, 4);
                MELEE_ATTACK_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/player/attack.png"), 1, 6);
                BOW_ATTACK_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/player/bow_attack.png"), 1, 9);
                DEATH_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/player/death.png"), 1, 4);
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Orc {
        public final static SpriteSheet IDLE_ANIMATION;
        public final static SpriteSheet WALK_ANIMATION;
        public final static SpriteSheet HURT_ANIMATION;
        public final static SpriteSheet ATTACK_ANIMATION;
        public final static SpriteSheet DEATH_ANIMATION;

        static {
            try {
                IDLE_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/orc/idle.png"), 1, 6);
                WALK_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/orc/walk.png"), 1, 8);
                HURT_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/orc/hurt.png"), 1, 4);
                ATTACK_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/orc/attack.png"), 1, 6);
                DEATH_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/orc/death.png"), 1, 4);
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class GiantOrc {
        public final static SpriteSheet IDLE_ANIMATION;
        public final static SpriteSheet ATTACK_ANIMATION;

        static {
            try {
                IDLE_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/giant_orc/idle.png"), 1, 6);
                ATTACK_ANIMATION = new SpriteSheet(ResourceUtils.readImage(TextureManager.class, "assets/textures/character/giant_orc/attack.png"), 1, 16);
            } catch (URISyntaxException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
