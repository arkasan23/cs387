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

  private boolean canMoveTo(double x, double y) {
    int newX = (int) x;
    int newY = (int) y;

    if (newX < 0 || newY < 0 || newX >= game.getMap().getWidth() || newY >= game.getMap().getHeight()) {
      return false;
    }

    int origalX = entity.getX();
    int origalY = entity.getY();

    entity.setX(newX);
    entity.setY(newY);

    boolean canMove = game.anyLevelCollision(entity) == null;
    entity.setX(origalX);
    entity.setY(origalY);

    return canMove;
  }

  private Node findNodeInList(List<Node> list, double x, double y) {
    for (Node node : list) {
      if (node.x == x && node.y == y) {
        return node;
      }
    }
    return null;
  }

  public List<Pair<Double, Double>> computePath() {
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> closedList = new ArrayList<>();

    if (start_x == goal_x && start_y == goal_y) {
      return new ArrayList<>();
    }

    if (!canMoveTo(goal_x, goal_y)) {
      return null;
    }

    openList.add(new Node(start_x, start_y, 0, huristic(start_x, start_y), null));

    while (openList.size() > 0) {
      Node currentNode = openList.get(0);
      for (Node node : openList) {
        if (node.f < currentNode.f) {
          currentNode = node;
        }
      }

      if (currentNode.x == goal_x && currentNode.y == goal_y) {
        List<Pair<Double, Double>> path = new ArrayList<>();

        List<Node> nodePath = currentNode.getPath();

        for (int i = 1; i < nodePath.size(); i++) {
          Node node = nodePath.get(i);
          path.add(new Pair<>(node.x, node.y));
        }

        return path;
      }

      openList.remove(currentNode);
      closedList.add(currentNode);

      for (Pair<Double, Double> neighbor : getNeighbors(currentNode.x, currentNode.y)) {

        double neighborX = neighbor.m_a;
        double neighborY = neighbor.m_b;

        if (!canMoveTo(neighborX, neighborY) || findNodeInList(closedList, neighborX, neighborY) != null) {
          continue;
        }

        double gCost = currentNode.g + 1;
        double hCost = huristic(neighborX, neighborY);

        Node openNode = findNodeInList(openList, neighborX, neighborY);

        if (openNode == null) {
          Node newNode = new Node(neighborX, neighborY, gCost, hCost, currentNode);
          openList.add(newNode);
        } else if (gCost < openNode.g) {
          openNode.g = gCost;
          openNode.h = hCost;
          openNode.f = gCost + hCost;
          openNode.parent = currentNode;
        }
      }
    }
    return null;
  }

}
