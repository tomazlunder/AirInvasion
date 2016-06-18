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
public class Rocket {
    public int x;
    public int y;
    int vector_y = -10;
    int vector_x;
    Image rocket = new ImageIcon(getClass().getResource("/resources/rocket_trans.gif")).getImage();
    
    public Rocket(int x, int y, int vector_x){
        this.x = x + 30 - 3;
        this.y = y + 20 - 11;
        this.vector_x = vector_x;
    }
    
    public void update(){
        y += vector_y;
        if (vector_x != 0){
            if (vector_x < 0) vector_x++;
            else vector_x--;
            x += vector_x;
        }
    }
    
    public void draw(Graphics g){
        g.drawImage(rocket, x, y, null);
    }
}
