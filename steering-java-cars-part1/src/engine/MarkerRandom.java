package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

/**
 *
 * @author santi
 */
public class MarkerRandom extends GameObject {
    double m_radius;
    Color m_color;
    RotatedRectangle m_collision_box;
   
    public MarkerRandom(double radius, Color c) {
    	Random random = new Random();
        // Generate a random index between 0 and 2.
    	if (random.nextBoolean()) {
    		m_x = random.nextInt(900)+100;
    		m_y = 60;}
    	else {
    		m_x = 1040;
    		m_y = random.nextInt(600)+100;
        }
        
        m_radius = radius;
        m_color = c;
        m_collision_box = null;
    }

    public void update(Game game, double delta_t) {
    }

    public void draw(Graphics2D g) {
        g.setColor(m_color);
        g.fillOval((int)(m_x-m_radius), (int)(m_y-m_radius), (int)(m_radius*2), (int)(m_radius*2));
    }
    
    public RotatedRectangle getCollisionBox() {
        return m_collision_box;
    }
    
        
}
