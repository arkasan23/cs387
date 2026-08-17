package test;

import controllers.SeekController;
import controllers.KeyboardController;
import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.GameWindow;
import engine.Marker;
import engine.MarkerRandom;
import engine.Obstacle;
import java.awt.Color;

/**
 *
 * @author santi
 */
public class ObstacleAvoidanceSeekScenario {
  /*
   * Goal of this exercise:
   * - Write a controller for "car" that uses the "Seek" steerig behavior to the
   * green marker,
   * but without bumping into the gray obstacles along the path.
   * 
   */

  public static void main(String args[]) throws Exception {
    Game game = new Game(1100, 800, 50);
    // set up the outside walls:
    game.add(new Obstacle(0, 0, 1100, 5, Color.GRAY));
    game.add(new Obstacle(0, 795, 1100, 5, Color.GRAY));
    game.add(new Obstacle(0, 0, 5, 800, Color.GRAY));
    game.add(new Obstacle(1095, 0, 5, 800, Color.GRAY));

    // set up some inside obstacles
    game.add(new Obstacle(100, 100, 50, 50, Color.GRAY));
    game.add(new Obstacle(100, 300, 50, 50, Color.GRAY));
    game.add(new Obstacle(100, 500, 50, 50, Color.GRAY));
    // game.add(new Obstacle(100,700,50,50,Color.GRAY));

    game.add(new Obstacle(300, 200, 50, 50, Color.GRAY));
    game.add(new Obstacle(300, 400, 50, 50, Color.GRAY));
    game.add(new Obstacle(300, 600, 50, 50, Color.GRAY));
    game.add(new Obstacle(300, 800, 50, 50, Color.GRAY));

    game.add(new Obstacle(500, 100, 50, 50, Color.GRAY));
    game.add(new Obstacle(500, 300, 50, 50, Color.GRAY));
    game.add(new Obstacle(500, 500, 50, 50, Color.GRAY));
    game.add(new Obstacle(500, 700, 50, 50, Color.GRAY));

    game.add(new Obstacle(700, 200, 50, 50, Color.GRAY));
    game.add(new Obstacle(700, 400, 50, 50, Color.GRAY));
    game.add(new Obstacle(700, 600, 50, 50, Color.GRAY));
    game.add(new Obstacle(700, 800, 50, 50, Color.GRAY));

    game.add(new Obstacle(900, 100, 50, 50, Color.GRAY));
    game.add(new Obstacle(900, 300, 50, 50, Color.GRAY));
    game.add(new Obstacle(900, 500, 50, 50, Color.GRAY));
    game.add(new Obstacle(900, 700, 50, 50, Color.GRAY));

    GameObject markerRandom = new MarkerRandom(10, Color.green);
    game.add(markerRandom);
    // use the seek controller to move the car to the marker, but avoid obstacles
    // along the way
    GameObject car = new Car("graphics/bluecar.png", 150, 750, -Math.PI / 2, new SeekController(markerRandom));
    game.add(car);
    GameWindow.newWindow(game);
  }
}
