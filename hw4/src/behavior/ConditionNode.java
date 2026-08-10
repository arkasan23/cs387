package behavior;

import engine.core.Entity;

public class ConditionNode implements BTNode {
    private final String name;

    public ConditionNode(String name) {
        this.name = name;
    }

    @Override
    public BTStatus tick(BTContext c) {
        c.visit("Condition:" + name);
        return evaluate(c) ? BTStatus.SUCCESS : BTStatus.FAILURE;
    }

    private boolean evaluate(BTContext c) {
        switch (name) {
            case "DangerClose":
                return c.dangerClose();
            case "InvincibleDangerClose":
                return c.invincibleDangerClose();
            case "GapAhead":
                return c.gapAhead();
            case "ObstacleAhead":
                return c.obstacleAhead();
            case "CanShoot":
                return c.mario.mayShoot;
            case "ShootableEnemyAhead":
                return c.nearestShootableEnemyAhead() != null;
            case "SquishyEnemyNearby": {
                Entity e = c.nearestSquishyEnemy();
                return e != null && Math.abs(e.dX) < 88 && Math.abs(e.dY) < 48;
            }
            case "CollectibleNearby":
                return c.nearestCollectibleEntity() != null;
            case "CoinNearby":
                return c.nearestCoinTile() != null;
            case "QuestionBrickNearby":
                return c.nearestQuestionBrick() != null;
            case "IsJumping":
                return c.mario.isJumping();
            case "SafeToSprint":
                return !c.dangerClose() && !c.gapAhead() && !c.obstacleAhead();
            default:
                throw new IllegalArgumentException("Unknown BT condition: " + name);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
