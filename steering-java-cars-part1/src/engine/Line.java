package engine;

import controllers.Controller;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 *
 * @author Ehsan Khosroshahi
 */
public class Line extends GameObject {
    
	double m_endx, m_endy;
	
    public Line(double x, double y, double endx, double endy){
        m_x = x;
        m_y = y;
        m_endx = endx;
        m_endy = endy;
    }
    
      
    public void update(Game game, double delta_t) {
       
    }

    public void draw(Graphics2D g) {
        g.drawLine((int)m_x, (int)m_y, (int)m_endx, (int)m_endy);     
    }

    public RotatedRectangle getCollisionBox() {
        return null;
    }
}
