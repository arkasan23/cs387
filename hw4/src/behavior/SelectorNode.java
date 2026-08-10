package behavior;

public class SelectorNode extends CompositeNode {

    public SelectorNode(String name) {
        super(name);
    }

    @Override
    public BTStatus tick(BTContext context) {
        context.visit("Selector:" + name);
        return tickChild(context, 0);
    }

    private BTStatus tickChild(BTContext context, int index) {
        if (index >= children.size()) {
            return BTStatus.FAILURE;
        }

        BTStatus status = children.get(index).tick(context);

        // use recusion instead of while
        if (status == BTStatus.FAILURE) {
            return tickChild(context, index + 1);
        }

        return status;
    }
}
