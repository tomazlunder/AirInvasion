/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Tommy
 */
public class Cannon {
    int x;
    int y;
    Image cannon = new ImageIcon(getClass().getResource("/resources/cannon_trans.gif")).getImage();
    
    public Cannon(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
    public void draw(Graphics g){
        g.drawImage(cannon, x, y, null);
    }
     
     
}
    

