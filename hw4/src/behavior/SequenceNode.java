package behavior;

public class SequenceNode extends CompositeNode {

    public SequenceNode(String name) {
        super(name);
    }

    @Override
    public BTStatus tick(BTContext context) {
        context.visit("Sequence:" + name);
        return tickChild(context, 0);
    }

    private BTStatus tickChild(BTContext context, int index) {
        if (index >= children.size()) {
            return BTStatus.SUCCESS;
        }

        BTStatus status = children.get(index).tick(context);

        // go next if it worked
        if (status == BTStatus.SUCCESS) {
            return tickChild(context, index + 1);
        }

        return status;
    }
}
