package engine;

public class Vector {
  public double x;
  public double y;


  public Vector(double x, double y){
    this.x = x;
    this.y = y;
  }

  public Vector add(Vector other){
    return new Vector(this.x + other.x, this.y + other.y);
  }

  public Vector sub(Vector other){
    return new Vector(this.x - other.y, this.y - other.y);
  }

  public Vector mul(double scalar){
    return new Vector(this.x * scalar, this.y * scalar);
  }

  public double dot(Vector other){
    return this.x * other.x + this.y * other.y;
  }

  public double magnitude(){
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  public Vector norm(){
    double mag = this.magnitude();

    return new Vector(this.x / mag, this.y / mag);

  }

  public Vector right(){
    return new Vector(this.y, -this.x);
  }

  public void print(){
    System.out.println("X: " + this.x  + " Y: " + this.y);
  }
}
