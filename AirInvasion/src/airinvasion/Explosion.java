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
public class Explosion {
    public int x, y;
    public int ttl;
    public boolean is_big;
    Image explosion = new ImageIcon(getClass().getResource("/resources/explosion_trans.gif")).getImage();
    Image big_explosion = new ImageIcon(getClass().getResource("/resources/explosion_big_trans.gif")).getImage();
    public Explosion(int x,int y, boolean is_big){
        this.x = x;
        this.y = y;
        this.ttl = 5;
        this.is_big = is_big;
        
    }
    public void draw(Graphics g){
        if (is_big){
            g.drawImage(big_explosion, x , y, null);
        } else {
            g.drawImage(explosion, x , y, null);
        }
        ttl--;
    }    
}
