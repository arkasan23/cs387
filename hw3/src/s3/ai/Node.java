
package s3.ai;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Node {
  public double x;
  public double y;
  public double g;
  public double h;
  public double f;
  public Node parent;

  public Node(double x, double y, double g, double h, Node parent) {
    this.x = x;
    this.y = y;
    this.g = g;
    this.h = h;
    this.parent = parent;
  }

  public double f() {
    return g + h;
  }

  public List<Node> getPath() {
    List<Node> path = new ArrayList<>();
    Node current = this;
    while (current != null) {
      path.add(current);
      current = current.parent;
    }
    Collections.reverse(path);
    return path;
  }

}
