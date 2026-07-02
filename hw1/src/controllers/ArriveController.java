package controllers;

import engine.Car;
import engine.Game;
import engine.GameObject;
import engine.Vector;

public class ArriveController extends Controller {

    GameObject target;

    public ArriveController(GameObject target){
      this.target = target;
    }
    
    public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

        double slowRaidus = 150.0;
        double targetRaidus = 15.0;
        
        Vector forwardVec = new Vector (Math.cos(subject.getAngle()), Math.sin(subject.getAngle())).norm();
        Vector rightVec = forwardVec.right().norm();

        Vector targetPos = new Vector(target.getX(), target.getY());
        Vector carPos = new Vector(subject.getX(), subject.getY());

        double distance = targetPos.sub(carPos).magnitude();

        double throttle = 0;
        double brake = 0;
        double targetSpeed;

        if (distance < targetRaidus){
          targetSpeed = 0;
        } else if (distance < slowRaidus){
          targetSpeed = 150 * (distance / slowRaidus);
        } else {
          targetSpeed = 150;
        }

        Vector diff = targetPos.sub(carPos).norm().mul(targetSpeed);
        Vector current = new Vector(Math.cos(subject.getAngle()), Math.sin(subject.getAngle())).mul(subject.getSpeed());
        Vector steering = diff.sub(current);
      
        double rightDot = rightVec.dot(steering) / 150;
        controlVariables[VARIABLE_STEERING] = Math.clamp(-rightDot, -1.0, 1.0);

        double speedDiff = targetSpeed - subject.getSpeed();
        if (speedDiff > 0){
          throttle = Math.clamp(speedDiff/ 150, 0.0, 1.0) ;
        } else {
          brake = Math.clamp(-speedDiff/ 150, 0.0, 1.0);
        }

        controlVariables[VARIABLE_THROTTLE] = throttle;
        controlVariables[VARIABLE_BRAKE] = brake;
    }
    
}
