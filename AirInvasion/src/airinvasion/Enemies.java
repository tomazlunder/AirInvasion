/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;


/**
 *
 * @author Tommy
 */
public class Enemies {
    public ArrayList<EnmBomber> bombers = new ArrayList<>();
    public ArrayList<EnmBomb> bombs = new ArrayList<>();
    public ArrayList<Explosion> bomb_explosions = new ArrayList<>();
    boolean is_fire;
    //public ArrayList<EnmBombs> bombs;
    
    Random random = new Random();
            
    public Enemies(){
        
    }
    
    public void update(){
        for (int i = 0; i < bombers.size(); i++){
            EnmBomber bomber = bombers.get(i);
            bomber.update();
            if  (!bomber.bombed_yet) {
                is_fire = false;
                int fire_num = random.nextInt(7);
                if (bomber.vector_x > 0 && bomber.x + bomber.WIDTH/2 > bomber.bomb_at) {
                    bomber.bombed_yet = true;
                    if (fire_num == 1) is_fire = true;
                    EnmBomb bomb = new EnmBomb(bomber.x + bomber.WIDTH/2 + 8 , bomber.y+24, bomber.vector_x+5, is_fire);
                    bombs.add(bomb);
                    //System.out.printf("Bombed, %d\n", bomber.bomb_at);
                }
                else if (bomber.vector_x < 0 && ((bomber.x + bomber.WIDTH/2) < (780 - bomber.bomb_at))) {
                    bomber.bombed_yet = true;
                    if (fire_num == 1) is_fire = true;
                    EnmBomb bomb = new EnmBomb(bomber.x + bomber.WIDTH/2 + 35, bomber.y+28, bomber.vector_x-5, is_fire);
                    bombs.add(bomb);
                    //System.out.printf("Bombed R, %d\n", -780+bomber.bomb_at);
                }
            }
        }
        for (int i = 0; i < bombs.size(); i++){
            EnmBomb bomb = bombs.get(i);
            bomb.update();
            if (bomb.y > 680) {
                bombs.remove(bomb);
                AirInvasion.airInvasion.addExplosion(bomb.x +2, 670, false);
                if (bomb.is_fire == true) AirInvasion.airInvasion.addFirefield(bomb.x+2-75, 670);
            }
        }
    }
    
    public void draw(Graphics g){
        if (bombers.size() > 0){
            for (EnmBomber bomber : bombers){
                bomber.draw(g);
            }
        }
        for (EnmBomb bomb : bombs) bomb.draw(g);
    }
    
    public void generateBomber(){
        EnmBomber bomber = new EnmBomber();
        bombers.add(bomber);
    }
    
    public void setBombers(ArrayList<EnmBomber> bombers){
        this.bombers = bombers;
    }
    
    public void setBombs(ArrayList<EnmBomb> bombs){
        this.bombs = bombs;
    }
}

