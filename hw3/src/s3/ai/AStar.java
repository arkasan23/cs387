package s3.ai;

import java.util.List;
import java.util.ArrayList;
import s3.base.S3;
import s3.entities.S3PhysicalEntity;
import s3.util.Pair;
import s3.ai.Node;

public class AStar {

  public double start_x;
  public double start_y;
  public double goal_x;
  public double goal_y;
  public S3 game;
  public S3PhysicalEntity entity;

  public static int pathDistance(double start_x, double start_y, double goal_x, double goal_y,
      S3PhysicalEntity i_entity, S3 the_game) {
    AStar a = new AStar(start_x, start_y, goal_x, goal_y, i_entity, the_game);
    List<Pair<Double, Double>> path = a.computePath();
    if (path != null)
      return path.size();
    return -1;
  }

  public AStar(double start_x, double start_y, double goal_x, double goal_y,
      S3PhysicalEntity i_entity, S3 the_game) {
    this.start_x = start_x;
    this.start_y = start_y;
    this.goal_x = goal_x;
    this.goal_y = goal_y;
    this.entity = i_entity;
    this.game = the_game;

  }

  private double huristic(double x, double y) {
    return Math.sqrt(Math.pow(x - goal_x, 2) + Math.pow(y - goal_y, 2));
  }

  private List<Pair<Double, Double>> getNeighbors(double x, double y) {
    List<Pair<Double, Double>> neighbors = new ArrayList<>();
    neighbors.add(new Pair<>(x + 1, y));
    neighbors.add(new Pair<>(x - 1, y));
    neighbors.add(new Pair<>(x, y + 1));
    neighbors.add(new Pair<>(x, y - 1));

    return neighbors;
  }

  public List<Pair<Double, Double>> computePath() {
    //
    // System.out.println("A* pathfinding from (" + start_x + ", " + start_y + ") to
    // (" + goal_x + ", " + goal_y + ")");
    //
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> closedList = new ArrayList<>();
    int iteration = 0;
    openList.add(new Node(start_x, start_y, 0, huristic(start_x, start_y), null));

    while (openList.size() > 0) {
      iteration++;
      Node currentNode = openList.get(0);
      for (Node node : openList) {
        if (node.f < currentNode.f) {
          currentNode = node;
        }
      }

      if (currentNode.x == goal_x && currentNode.y == goal_y) {
        List<Pair<Double, Double>> path = new ArrayList<>();
        while (currentNode != null) {
          path.add(0, new Pair<>(currentNode.x, currentNode.y));
          currentNode = currentNode.parent;
        }
        return path;
      }

      openList.remove(currentNode);
      closedList.add(currentNode);

      for (Pair<Double, Double> neighbor : getNeighbors(currentNode.x, currentNode.y)) {

        double neighborX = neighbor.m_a;
        double neighborY = neighbor.m_b;

        if (this.entity.isBlocked(neighborX, neighborY) || closedList.stream()
            .anyMatch(n -> n.x == neighborX && n.y == neighborY)) {
          continue;
        }

        double gCost = currentNode.g + 1;
        double hCost = huristic(neighborX, neighborY);
        Node neighborNode = new Node(neighborX, neighborY, gCost, hCost, currentNode);

        Node existingOpenNode = openList.stream()
            .filter(n -> n.x == neighborX && n.y == neighborY)
            .findFirst()
            .orElse(null);

        if (existingOpenNode == null) {
          openList.add(neighborNode);
        } else if (gCost < existingOpenNode.g) {
          existingOpenNode.g = gCost;
          existingOpenNode.f = gCost + hCost;
          existingOpenNode.parent = currentNode;
        }
      }
    }
    return null;
  }

}
