package ch.fhnw.mas.se;

import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * OOP Exercise, MAS FHNW
 * @author David Herzig <dave.herzig@gmail.com>
 */
public class Smiley extends JFrame {
    
    public Smiley() {
        super("Smiley");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        
        SmileyPanel sp = new SmileyPanel();
        this.getContentPane().add(sp);
        
        this.setVisible(true);
    }
    
    public static void main(String [] args) {
        new Smiley();
    }
}

class SmileyPanel extends JPanel {
    public void paint(Graphics g) {
        g.drawArc(0, 0, this.getWidth(), this.getHeight(), 0, 360);
    }
}
