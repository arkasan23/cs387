/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

        double slowRadius = 5.0;
        double targetRadius = 1.0;

        Vector forwardVec = new Vector (Math.cos(subject.getAngle()), Math.sin(subject.getAngle()));
        Vector rightVec = forwardVec.right().norm();

        Vector targetPos = new Vector(target.getX(), target.getY());
        Vector carPos = new Vector(subject.getX(), subject.getY());

        Vector diff = targetPos.sub(carPos).norm();
        double distance = targetPos.sub(carPos).magnitude();
      
        double fowardDot = forwardVec.dot(diff);
        double rightDot = rightVec.dot(diff);

        System.out.println(distance);

        if (distance < targetRadius){
          controlVariables[VARIABLE_THROTTLE] = 0;
          controlVariables[VARIABLE_BRAKE] = 0;
        } 

        if (distance > slowRadius) {
          controlVariables[VARIABLE_THROTTLE] = 1;
          controlVariables[VARIABLE_BRAKE] = 0;
        } else {
          double velocity = 1/ distance * slowRadius;
          controlVariables[VARIABLE_THROTTLE] = 0;
          controlVariables[VARIABLE_BRAKE] = 1;
      
        }

      
        if (rightDot > 0.15){
          controlVariables[VARIABLE_STEERING] = -1;
        } else if (rightDot < -0.15) {
          controlVariables[VARIABLE_STEERING] = 1;
        } else {
          controlVariables[VARIABLE_STEERING] = 0;
        }


    }
    
}
