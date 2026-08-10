package behavior;

public class BehaviorTree {
    private final BTNode root;

    public BehaviorTree(BTNode root) {
        this.root = root;
    }

    public BTStatus tick(BTContext context) {
        return root.tick(context);
    }

    public BTNode getRoot() {
        return root;
    }
}
