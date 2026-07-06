package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.RotatedRectangle;
import engine.Vector;

public class SeekController extends Controller {

  GameObject target;

  public SeekController(GameObject target) {
    this.target = target;
  }

  public static boolean rayCast(Car subject, Game game, double angle) {

    double rads = Math.toRadians(angle);

    RotatedRectangle rect = new RotatedRectangle(subject.getX(), subject.getY(), subject.m_img.getWidth() / 2,
        subject.m_img.getHeight() / 2, subject.getAngle());

    int rayStep = 30;

    for (int i = 0; i < rayStep; i++) {
      rect.C.x = rect.C.x + 0.01;

    }

  }

  public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

    // get the foward and right vectors of the car
    Vector forwardVec = new Vector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle())).norm();
    Vector rightVec = forwardVec.right().norm();

    Vector targetPos = new Vector(target.getX(), target.getY());
    Vector carPos = new Vector(subject.getX(), subject.getY());

    Vector diff = targetPos.sub(carPos).norm().mul(150);

    double fowardDot = forwardVec.dot(diff) / 150;
    double rightDot = rightVec.dot(diff) / 150;

    controlVariables[VARIABLE_STEERING] = Math.clamp(-rightDot, -1.0, 1.0);

    double throttle = 0;
    double brake = 0;

    if (fowardDot > 0) {
      throttle = Math.clamp(fowardDot, 0.0, 1.0);
    } else {
      brake = Math.clamp(-fowardDot, 0.0, 1.0);
    }

    controlVariables[VARIABLE_THROTTLE] = throttle;
    controlVariables[VARIABLE_BRAKE] = brake;
  }

}
