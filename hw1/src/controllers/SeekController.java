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

/**
 *
 * @author santi
 */
public class SeekController extends Controller {


    GameObject target;

    public SeekController(GameObject target){

      this.target = target;

    }

    
    public void update(Car subject, Game game, double delta_t, double[] controlVariables) {

        Vector targetPos = new Vector(target.getX(), target.getY());
        Vector carPos = new Vector(subject.getX(), subject.getY());
        Vector rightVec = carPos.right().norm();

        carPos.print();

        System.out.println(targetPos.dot(rightVec));

      

        
        if (rightVec.dot(targetPos) == 0){
          controlVariables[VARIABLE_STEERING] = 0;
        }
        else if (rightVec.dot(targetPos) > 0){
          controlVariables[VARIABLE_STEERING] = 1;
        } else {
          controlVariables[VARIABLE_STEERING] = -1;
        }

        if (carPos.dot(targetPos) > 0){
          controlVariables[VARIABLE_THROTTLE] = 1;
          controlVariables[VARIABLE_BRAKE] = 0;
        } else {
          controlVariables[VARIABLE_THROTTLE] = 0;
          controlVariables[VARIABLE_BRAKE] = 1;


        }
        
    }
    
}
