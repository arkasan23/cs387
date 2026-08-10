package behavior;

import java.util.ArrayList;
import java.util.List;

import engine.core.Entities;
import engine.core.Entity;
import engine.core.EntityType;
import engine.core.MarioEntity;
import engine.core.Tile;
import engine.core.Tiles;
import engine.input.MarioInput;
import engine.input.MarioKey;

public class BTContext {
    public final MarioEntity mario;
    public final Tiles tiles;
    public final Entities entities;
    public final MarioInput lastInput;
    public final MarioInput input;

    private final List<String> trace = new ArrayList<>();
    private String activeAction = "None";

    public BTContext(MarioEntity mario, Tiles tiles, Entities entities, MarioInput lastInput) {
        this.mario = mario;
        this.tiles = tiles;
        this.entities = entities;
        this.lastInput = lastInput;
        this.input = new MarioInput();
    }

    public void visit(String node) {
        if (trace.size() < 12) {
            trace.add(node);
        }
    }

    public void setActiveAction(String action) {
        this.activeAction = action;
        visit("Action:" + action);
    }

    public String getActiveAction() {
        return activeAction;
    }

    public String getTrace() {
        return String.join(" > ", trace);
    }

    public boolean isSolid(int dx, int dy) {
        Tile t = tiles.tile(dx, dy);
        return t != Tile.NOTHING && t != Tile.COIN_ANIM;
    }

    public boolean gapAhead() {
        // check next 2 floor tiles
        return !isSolid(1, 1) && !isSolid(2, 1);
    }

    public boolean obstacleAhead() {
        return tiles.brick(1, 0) || tiles.brick(1, -1) || tiles.brick(2, 0) || tiles.brick(2, -1);
    }

    public boolean dangerClose() {
        for (Entity e : entities.allEntities) {
            if (e.type.getKind().isDangerous() && e.dX > -28 && e.dX < 54 && e.dY > -38 && e.dY < 30) {
                return true;
            }
        }
        return false;
    }

    public boolean invincibleDangerClose() {
        for (Entity e : entities.allEntities) {
            if (e.type.getKind().isDangerous() && !e.type.getKind().isSquishy() && e.dX > -24 && e.dX < 64 && e.dY > -42 && e.dY < 30) {
                return true;
            }
        }
        return false;
    }

    public Entity nearestDanger() {
        return nearestEntity(false, true, false, false);
    }

    public Entity nearestShootableEnemyAhead() {
        Entity best = null;
        double bestDist = Double.MAX_VALUE;

        for (Entity e : entities.allEntities) {
            if (!e.type.getKind().isDangerous() || !e.type.getKind().isShootable()) {
                continue;
            }
            if (e.dX < 0 || e.dX > 120 || Math.abs(e.dY) > 42) {
                continue;
            }

            double d = distanceSq(e.dX, e.dY);
            if (d < bestDist) {
                bestDist = d;
                best = e;
            }
        }
        return best;
    }

    public Entity nearestSquishyEnemy() {
        return nearestEntity(true, true, false, false);
    }

    public Entity nearestCollectibleEntity() {
        return nearestEntity(false, false, true, false);
    }

    private Entity nearestEntity(boolean requireSquishy, boolean requireDanger, boolean requireCollectible, boolean unused) {
        Entity best = null;
        double bestDist = Double.MAX_VALUE;

        for (Entity e : entities.allEntities) {
            if (e.type == EntityType.MARIO || e.type == EntityType.NOTHING) {
                continue;
            }
            if (requireDanger && !e.type.getKind().isDangerous()) {
                continue;
            }
            if (requireSquishy && !e.type.getKind().isSquishy()) {
                continue;
            }
            if (requireCollectible && !e.type.getKind().isCollectible()) {
                continue;
            }
            if (Math.abs(e.dX) > 128 || Math.abs(e.dY) > 96) {
                continue;
            }

            double d = distanceSq(e.dX, e.dY);
            if (d < bestDist) {
                bestDist = d;
                best = e;
            }
        }
        return best;
    }

    public int[] nearestCoinTile() {
        int[] best = null;
        int bestDist = Integer.MAX_VALUE;
        int halfW = mario.receptiveFieldWidth / 2;
        int halfH = mario.receptiveFieldHeight / 2;

        for (int dx = -halfW; dx <= halfW; dx++) {
            for (int dy = -halfH; dy <= halfH; dy++) {
                if (tiles.tile(dx, dy) != Tile.COIN_ANIM) {
                    continue;
                }

                // dont chase coins too far back
                if (dy > 2 || dy < -5 || dx < -1) {
                    continue;
                }

                int d = dx * dx + 2 * dy * dy;
                if (d < bestDist) {
                    bestDist = d;
                    best = new int[] {dx, dy};
                }
            }
        }
        return best;
    }

    public int[] nearestQuestionBrick() {
        int[] best = null;
        int bestDist = Integer.MAX_VALUE;

        for (int dx = -2; dx <= 5; dx++) {
            for (int dy = -4; dy <= -1; dy++) {
                if (tiles.tile(dx, dy) != Tile.QUESTION_BRICK) {
                    continue;
                }

                int d = dx * dx + dy * dy;
                if (d < bestDist) {
                    bestDist = d;
                    best = new int[] {dx, dy};
                }
            }
        }
        return best;
    }

    public void moveToward(float dx) {
        if (dx < -5) {
            input.press(MarioKey.LEFT);
        } else {
            input.press(MarioKey.RIGHT);
        }
    }

    public void jumpIfPossible() {
        if (mario.mayJump || mario.isJumping()) {
            input.press(MarioKey.JUMP);
        }
    }

    private static double distanceSq(float x, float y) {
        return x * x + y * y;
    }
}
