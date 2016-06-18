package airinvasion;


import java.awt.Graphics;
import javax.swing.JPanel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Tommy
 */
public class Renderer extends JPanel {

    public static final long serialVersionUID = 1L;
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        AirInvasion.airInvasion.repaint(g);
    }
    
    
    
}
