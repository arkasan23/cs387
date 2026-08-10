package behavior;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public final class BehaviorTreeParser {

    private BehaviorTreeParser() {
    }

    public static BehaviorTree load(String resourcePath) {
        try (InputStream in = open(resourcePath)) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            factory.setExpandEntityReferences(false);

            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(in);
            doc.getDocumentElement().normalize();

            Element rootElement = firstElementChild(doc.getDocumentElement());
            if (rootElement == null) {
                throw new IllegalArgumentException("BehaviorTree XML has no root task");
            }

            return new BehaviorTree(parseNode(rootElement));
        } catch (Exception e) {
            throw new RuntimeException("Could not load behavior tree from " + resourcePath, e);
        }
    }

    private static InputStream open(String resourcePath) throws Exception {
        InputStream in = BehaviorTreeParser.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in != null) {
            return in;
        }

        // try src if it isnt packaged
        File sourceFile = new File("src/" + resourcePath);
        if (sourceFile.exists()) {
            return new FileInputStream(sourceFile);
        }

        throw new IllegalArgumentException("Behavior-tree XML not found: " + resourcePath);
    }

    private static BTNode parseNode(Element e) {
        String tag = e.getTagName();
        String name = e.hasAttribute("name") ? e.getAttribute("name") : tag;

        switch (tag) {
            case "Selector": {
                SelectorNode node = new SelectorNode(name);
                addChildren(node, e);
                return node;
            }
            case "Sequence": {
                SequenceNode node = new SequenceNode(name);
                addChildren(node, e);
                return node;
            }
            case "Condition":
                return new ConditionNode(requiredName(e));
            case "Action":
                return new ActionNode(requiredName(e));
            default:
                throw new IllegalArgumentException("Unknown BT XML tag: " + tag);
        }
    }

    private static void addChildren(CompositeNode parent, Element e) {
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            Node child = nodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                parent.addChild(parseNode((Element) child));
            }
        }
    }

    private static String requiredName(Element e) {
        String name = e.getAttribute("name").trim();
        if (name.isEmpty()) {
            throw new IllegalArgumentException(e.getTagName() + " requires a name attribute");
        }
        return name;
    }

    private static Element firstElementChild(Element e) {
        NodeList nodes = e.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                return (Element) nodes.item(i);
            }
        }
        return null;
    }
}
