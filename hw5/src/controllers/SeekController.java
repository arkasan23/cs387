package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.RotatedRectangle;
import engine.Vector;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.annotation.Target;

public class SeekController extends Controller {

  GameObject target;

  public SeekController(GameObject target) {
    this.target = target;
  }

  public boolean rayCast(Car subject, Game game, double angle) {

    // get the angle of the ray to cast from the car
    double rads = subject.getAngle() + Math.toRadians(angle);
    RotatedRectangle rect = new RotatedRectangle(subject.getX(), subject.getY(), subject.m_img.getWidth() / 2,
        subject.m_img.getHeight() / 2, rads);
    // make the ray the size of the car
    int raySteps = 10;
    int stepSize = 5;

    double dirX = Math.cos(rads);
    double dirY = Math.sin(rads);

    // travel the ray in steps to check for collisions with other objects
    for (int i = 0; i < raySteps; i++) {
      rect.C.x += dirX * stepSize;
      rect.C.y += dirY * stepSize;

      GameObject hit = game.collision(rect);
      // don't want the ray to detect the car or the target
      if (hit != null && hit != subject && hit != this.target) {
        return true;
      }
    }
    return false;
  }

  public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

    double slowRaidus = 150.0;
    double targetRaidus = 15.0;

    // get the foward and right vectors of the car
    Vector forwardVec = new Vector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle())).norm();
    Vector rightVec = forwardVec.right().norm();

    Vector targetPos = new Vector(target.getX(), target.getY());
    Vector carPos = new Vector(subject.getX(), subject.getY());

    double distance = targetPos.sub(carPos).magnitude();

    double throttle = 0;
    double brake = 0;
    double targetSpeed;

    if (distance < targetRaidus) {
      targetSpeed = 0;
    } else if (distance < slowRaidus) {
      targetSpeed = 150 * (distance / slowRaidus);
      // slow the car down when its inside the slow radius
    } else {
      targetSpeed = 150;
    }

    Vector diff = targetPos.sub(carPos).norm().mul(targetSpeed);
    Vector current = new Vector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle())).mul(subject.getSpeed());
    Vector steering = diff.sub(current);

    double rightDot = rightVec.dot(steering) / 150;

    // cast 4 rays at different angles from the car to detect obstacles and adjust
    // the steering accordingly
    boolean rightCast = rayCast(subject, game, 35);
    boolean narrowRayCast = rayCast(subject, game, 15);
    boolean leftCast = rayCast(subject, game, -35);
    boolean narrowLeftCast = rayCast(subject, game, -15);

    // steer right if it detcts obstacles on the left and steer left if it detects
    // obstacles on the right
    if (leftCast || narrowLeftCast) {
      rightDot = -1.0;
    }

    if (rightCast || narrowRayCast) {
      rightDot = 1.0;
    }

    controlVariables[VARIABLE_STEERING] = Math.clamp(-rightDot, -1.0, 1.0);
    double speedDiff = targetSpeed - subject.getSpeed();
    // throttle based on the target speed and the cars acutal speed
    if (speedDiff > 0) {
      throttle = Math.clamp(speedDiff / 150, 0.0, 1.0);
    } else {
      brake = Math.clamp(-speedDiff / 150, 0.0, 1.0);
    }

    controlVariables[VARIABLE_THROTTLE] = throttle;
    controlVariables[VARIABLE_BRAKE] = brake;
  }

}
