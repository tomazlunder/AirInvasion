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
public class EnmBomb {
    int x;
    int y;
    int vector_y;
    int vector_x;
    int ticks;
    boolean is_fire;
    Image bomb = new ImageIcon(getClass().getResource("/resources/bomber_bomb_trans.gif")).getImage();
    Image firebomb = new ImageIcon(getClass().getResource("/resources/bomber_firebomb_trans.gif")).getImage();
    
    public EnmBomb(int x, int y, int vector_x, boolean is_fire){
        this.x = x;
        this.y = y;
        this.vector_x = vector_x;
        this.is_fire = is_fire;
        vector_y = 2;
        ticks = 0;
    }
    
    public void update(){
        ticks++;
        y += vector_y;
        x += vector_x;
        
        vector_y ++;
        if (ticks%3 == 0){
            if (vector_x != 0){
                if (vector_x < 0) vector_x++;
                else vector_x--;
        }
        }
        
    }
    
    public void draw(Graphics g){
        if (is_fire == true) {
            g.drawImage(firebomb, x - 4, y - 5, null);
        }else{
            g.drawImage(bomb, x - 4, y - 5, null);
        }
    }   
}