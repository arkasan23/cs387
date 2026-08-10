package behavior;

import engine.core.Entity;
import engine.input.MarioKey;

public class ActionNode implements BTNode {
    private final String name;

    public ActionNode(String name) {
        this.name = name;
    }

    @Override
    public BTStatus tick(BTContext c) {
        c.setActiveAction(name);

        switch (name) {
            case "AvoidDanger":
                return avoidDanger(c);
            case "JumpGap":
                c.input.press(MarioKey.RIGHT);
                c.jumpIfPossible();
                return BTStatus.SUCCESS;
            case "ShootEnemy":
                c.input.press(MarioKey.RIGHT);
                // need new press to shoot
                if (!c.lastInput.isPressed(MarioKey.SPEED)) {
                    c.input.press(MarioKey.SPEED);
                }
                return BTStatus.SUCCESS;
            case "StompEnemy":
                return stompEnemy(c);
            case "CollectPowerup":
                return collectPowerup(c);
            case "CollectCoin":
                return collectCoin(c);
            case "HitQuestionBrick":
                return hitQuestionBrick(c);
            case "JumpObstacle":
                c.input.press(MarioKey.RIGHT);
                c.jumpIfPossible();
                return BTStatus.SUCCESS;
            case "ContinueJumpRight":
                c.input.press(MarioKey.RIGHT);
                c.input.press(MarioKey.JUMP);
                return BTStatus.SUCCESS;
            case "MoveRight":
                c.input.press(MarioKey.RIGHT);
                return BTStatus.SUCCESS;
            case "SprintRight":
                c.input.press(MarioKey.RIGHT);
                c.input.press(MarioKey.SPEED);
                return BTStatus.SUCCESS;
            default:
                throw new IllegalArgumentException("Unknown BT action: " + name);
        }
    }

    private BTStatus avoidDanger(BTContext c) {
        Entity e = c.nearestDanger();
        if (e == null) {
            return BTStatus.FAILURE;
        }

        // jump over stuff we cant stomp
        if (!e.type.getKind().isSquishy()) {
            c.input.press(MarioKey.RIGHT);
            c.jumpIfPossible();
            return BTStatus.SUCCESS;
        }

        // try to stomp normal enemys
        c.moveToward(e.dX);
        c.jumpIfPossible();
        return BTStatus.SUCCESS;
    }

    private BTStatus stompEnemy(BTContext c) {
        Entity e = c.nearestSquishyEnemy();
        if (e == null) {
            return BTStatus.FAILURE;
        }

        c.moveToward(e.dX);
        if (Math.abs(e.dX) < 72 || e.dY < 0) {
            c.jumpIfPossible();
        }
        return BTStatus.SUCCESS;
    }

    private BTStatus collectPowerup(BTContext c) {
        Entity e = c.nearestCollectibleEntity();
        if (e == null) {
            return BTStatus.FAILURE;
        }

        c.moveToward(e.dX);
        if (e.dY < -12) {
            c.jumpIfPossible();
        }
        return BTStatus.SUCCESS;
    }

    private BTStatus collectCoin(BTContext c) {
        int[] coin = c.nearestCoinTile();
        if (coin == null) {
            return BTStatus.FAILURE;
        }

        if (coin[0] < 0) {
            c.input.press(MarioKey.LEFT);
        } else {
            c.input.press(MarioKey.RIGHT);
        }

        if (coin[1] < 0) {
            c.jumpIfPossible();
        }
        return BTStatus.SUCCESS;
    }

    private BTStatus hitQuestionBrick(BTContext c) {
        int[] brick = c.nearestQuestionBrick();
        if (brick == null) {
            return BTStatus.FAILURE;
        }

        if (brick[0] < 0) {
            c.input.press(MarioKey.LEFT);
        } else if (brick[0] > 0) {
            c.input.press(MarioKey.RIGHT);
        }

        c.jumpIfPossible();
        return BTStatus.SUCCESS;
    }

    @Override
    public String getName() {
        return name;
    }
}
