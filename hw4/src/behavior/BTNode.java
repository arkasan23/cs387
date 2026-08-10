package behavior;

public interface BTNode {
    BTStatus tick(BTContext context);
    String getName();
}
