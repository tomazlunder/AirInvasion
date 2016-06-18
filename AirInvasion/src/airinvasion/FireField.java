/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author Tommy
 */
public class FireField {
    int x;
    int y;
    public int ttl;
    Image fire = new ImageIcon(getClass().getResource("/resources/firefield_trans.gif")).getImage();
    
    public FireField(int x, int y){
        this.x = x;
        this.y = y;
        ttl = 60;
    }
    
    
    public void draw(Graphics g){
        g.drawImage(fire, x, y, null);
        ttl--;
    }   
}

