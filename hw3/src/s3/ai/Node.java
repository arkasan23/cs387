
package s3.ai;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import s3.util.Pair;

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
    this.f = g + h;
    this.parent = parent;
  }

  public List<Pair<Double, Double>> getPath() {
    // returns the path from the start node to this node
    List<Pair<Double, Double>> path = new ArrayList<>();

    Node current = this;
    while (current != null) {
      path.add(new Pair<>(current.x, current.y));
      current = current.parent;
    }
    Collections.reverse(path);
    path.remove(0); // remove the start node from the path
    return path;
  }
}
