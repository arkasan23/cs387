package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.Vector;

public class SeekController extends Controller {

    GameObject target;

    public SeekController(GameObject target){
      this.target = target;
    }
    
    public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

        Vector forwardVec = new Vector (Math.cos(subject.getAngle()), Math.sin(subject.getAngle())).norm();
        Vector rightVec = forwardVec.right().norm();

        Vector targetPos = new Vector(target.getX(), target.getY());
        Vector carPos = new Vector(subject.getX(), subject.getY());

        Vector diff = targetPos.sub(carPos).norm().mul(150);
      
        double fowardDot = forwardVec.dot(diff) / 150;
        double rightDot = rightVec.dot(diff) / 150;

        controlVariables[VARIABLE_STEERING] = Math.clamp(-rightDot, -1.0, 1.0);

        double throttle = 0;
        double brake = 0;

        if (fowardDot > 0){
          throttle = Math.clamp(fowardDot, 0.0, 1.0);
        } else {
          brake = Math.clamp(-fowardDot, 0.0, 1.0);
        }

        controlVariables[VARIABLE_THROTTLE] = throttle;
        controlVariables[VARIABLE_BRAKE] = brake;
    }
    
}
