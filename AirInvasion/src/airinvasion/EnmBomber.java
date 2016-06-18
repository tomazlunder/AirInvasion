/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.ImageIcon;

/**
 *
 * @author Tommy
 */
public class EnmBomber {
    public int x;
    public int y;
    public int vector_x;
    public int vector_y;
    public int bomb_at;
    public int from_side; //0 - from left , 1 - from right
    public boolean bombed_yet;
    public boolean from_dir; //true from left, false from right
    public final int LOW = 380, MID = 230, HIGH = 80, SPEED = 5;
    public final int HEIGTH = 100, WIDTH = 40;
    Random random = new Random();
    Image bomber;
    
    public EnmBomber(){
        int rnd = random.nextInt(2);
        this.setStart(rnd);
    }
    
    public void update(){
        x += vector_x;
        y += vector_y;
        if (x < -300){
            this.setStart(0);
        }
        if (x > 1100){
            this.setStart(1);
        }
    }
    
    public void draw(Graphics g){
        g.drawImage(bomber, x, y, null);
    
    }
    
    public void setStart(int side){ 
        //Spawn left or spawn right
        bombed_yet = false;
        int bomb_at = (random.nextInt(950)-100);
        
        int heigth = (random.nextInt(37) + 6)*10;
        if (side == 0){ //spawn on left
            bomber = new ImageIcon(getClass().getResource("/resources/bomber_med_left_trans.gif")).getImage();
            this.x = -200;
            this.vector_x = SPEED + random.nextInt(5);
            this.vector_y = 0;
            this.y = heigth;
            
                    
        } else { //spawn on right
            bomber = new ImageIcon(getClass().getResource("/resources/bomber_med_right_trans.gif")).getImage();
            this.vector_x = -SPEED - random.nextInt(5);
            this.x = 1000;
            this.y = heigth;
        }
    }
}
