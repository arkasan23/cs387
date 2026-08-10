package behavior;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeNode implements BTNode {
    protected final String name;
    protected final List<BTNode> children = new ArrayList<>();

    protected CompositeNode(String name) {
        this.name = name;
    }

    public void addChild(BTNode child) {
        children.add(child);
    }

    @Override
    public String getName() {
        return name;
    }
}
