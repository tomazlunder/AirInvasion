/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 *
 * @author Tommy
 */
public class Player {
    public int x, y;
    public int vector_x;
    public int ammo, reload, time_at_top, lives;
    public boolean jumped, top;
    public final int WIDTH = 60, HEIGHT = 40, MAX_SPEED = 8, JUMP_HEIGTH = 30;
    public ArrayList<Rocket> rockets = new ArrayList<>();
    Image truck = new ImageIcon(getClass().getResource("/resources/truck_trans.gif")).getImage();
    Cannon cannon;

    
    public Player(){
        this.x = 400 - WIDTH/2;
        this.y = 660 - HEIGHT/2;
        this.lives = 3;
        this.ammo = 3;
        this.reload = 60; //Number of frames
        this.jumped = false;
        this.cannon = new Cannon(this.x, this.y);
    }
    
    public void update(){
        if (ammo == 0){
            reload--;
            if (reload == 0){
                ammo = 3;
                reload = 60;
            }
        }
        
        if (jumped){
            if (!top){
                if (this.y > 640 - JUMP_HEIGTH/2) this.y-=2;
                else this.y -=1;
                if (this.y == 640 - JUMP_HEIGTH){
                    this.top = true;
                    time_at_top = 6;
                }
            }
            if (top && time_at_top > 0){
                time_at_top --;
            }
            else if (top) {
                if (this.y > 640 - JUMP_HEIGTH/2) this.y+=1;
                else this.y +=2;
                if (this.y == 640) jumped = false;
            }
        }
        
        int temp = x + vector_x;
        if ((temp > 0) && (temp + WIDTH < 799)) x = temp;
        if (temp <= 0) {x = 0; vector_x = 0;}
        if (temp + WIDTH >= 799) {x = 799 - WIDTH; vector_x = 0;}
        
        for (int i = 0; i < rockets.size(); i++){
            Rocket this_rocket = rockets.get(i);
            this_rocket.update();
            if (this_rocket.y < -10){
                rockets.remove(this_rocket);
            }
        }
        cannon.setX(this.x);
        cannon.setY(this.y);
    }
    
    public void jump(){
        this.top = false;
        this.jumped = true;
    }
    
    public void incVectorX(boolean direction){ //false = left, true = right
        if (!direction && vector_x > (0 - MAX_SPEED)) vector_x -= 2;
        if (direction && vector_x < MAX_SPEED) vector_x += 2; 
    }
    
    public void decVectorX(){ 
        if (vector_x > 0) vector_x--;
        if (vector_x < 0) vector_x++;
    }
    
    public void draw(Graphics g){
        //g.setColor(Color.white);
        //g.fillRect(x - WIDTH/2, y - HEIGHT/2, 60, 40);
        g.drawImage(truck, x , y , null);
        cannon.draw(g);
        for (Rocket rocket : rockets){
            rocket.draw(g);
        }
        
        String ammo_str = "Ammo: " + ammo;
        String lives_str = "Lives: " + lives;
        g.setColor(Color.white); 
        g.setFont(new Font("Arial", 1, 25));
        g.drawString(ammo_str, 150, 720);
        g.drawString(lives_str, 150, 745);
        
        
    }
    
    public void shoot(){
        if (ammo > 0){
            ammo--;
            Rocket new_rocket = new Rocket(x, y, vector_x);
            rockets.add(new_rocket);
        }
    }
    
    public ArrayList<Rocket> getRockets(){
        return rockets;
    }
    public void setRockets(ArrayList<Rocket> rockets){
        this.rockets = rockets;
    }
}

