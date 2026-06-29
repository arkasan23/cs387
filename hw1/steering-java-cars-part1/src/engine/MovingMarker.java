package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 *
 * @author santi
 */
public class MovingMarker extends GameObject {
    double m_radius;
    Color m_color;
    RotatedRectangle m_collision_box;
    double t = 0;
    
    public MovingMarker(double radius, Color c) {
 
//        m_x = x;
//        m_y = y;
        m_radius = radius;
        m_color = c;
        m_collision_box = null;
    }

    public void update(Game game, double delta_t) {
    	t += delta_t/3;
    	double theta = 2;
    	m_x = 350*Math.cos(t) + game.m_width/2;
    	m_y = 350*Math.sin(2*t)/2 + game.m_height/2;
//    	m_x = 200*(Math.cos(theta) * Math.cos(t) - Math.sin(theta) * Math.sin(2*t)/2) + game.m_width/2;
//    	m_y = 200*(Math.sin(theta) * Math.cos(t) + Math.cos(theta) * Math.sin(2*t)/2) + game.m_height/2;
    }

    @Override
    public double getX() {
    	// TODO Auto-generated method stub
    	return super.getX();
    }
    @Override
    public double getY() {
    	// TODO Auto-generated method stub
    	return super.getY();
    }
    
    public void draw(Graphics2D g) {
        g.setColor(m_color);
        g.fillOval((int)(m_x-m_radius), (int)(m_y-m_radius), (int)(m_radius*2), (int)(m_radius*2));
    }
    
    public RotatedRectangle getCollisionBox() {
        return m_collision_box;
    }
    
        
}
