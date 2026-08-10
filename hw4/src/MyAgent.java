import java.awt.Graphics;

import agents.controllers.MarioAIBase;
import behavior.BTContext;
import behavior.BTStatus;
import behavior.BehaviorTree;
import behavior.BehaviorTreeParser;
import engine.core.IEnvironment;
import engine.core.LevelScene;
import engine.graphics.VisualizationComponent;
import engine.input.MarioInput;

public class MyAgent extends MarioAIBase {
    private BehaviorTree tree;
    private String activeAction = "Initializing";
    private String btTrace = "";
    private BTStatus lastStatus = BTStatus.FAILURE;

    private void ensureTreeLoaded() {
        if (tree == null) {
            tree = BehaviorTreeParser.load("behavior/mario_bt.xml");
        }
    }

    @Override
    public MarioInput actionSelectionAI() {
        ensureTreeLoaded();

        BTContext context = new BTContext(mario, tiles, entities, lastInput);

        lastStatus = tree.tick(context);
        activeAction = context.getActiveAction();
        btTrace = context.getTrace();

        return context.input;
    }

    @Override
    public void debugDraw(VisualizationComponent vis, LevelScene level, IEnvironment env, Graphics g) {
        super.debugDraw(vis, level, env, g);

        if (mario == null) {
            return;
        }

        VisualizationComponent.drawStringDropShadow(g, "BT: " + activeAction + " [" + lastStatus + "]", 0, 26, 1);

        if (!btTrace.isEmpty()) {
            // keep trace kinda short
            String shortTrace = btTrace.length() > 105 ? btTrace.substring(0, 105) + "..." : btTrace;

            VisualizationComponent.drawStringDropShadow(g, shortTrace, 0, 27, 1);
        }
    }
}
