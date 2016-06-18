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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

/**
 *
 * @author Tommy
 */

//Main game-logic class
public class AirInvasion implements ActionListener, KeyListener, MouseListener {

    public static AirInvasion airInvasion;
    public Renderer renderer;
    
    //Game version
    public double version = 1.01; 
    
    public Player player;
    public Enemies enemies;
    public TopScores topScores;
    
    //JFrame width and height
    public final int WIDTH = 800, HEIGHT = 800;
    
    //Ticks count frames from starting the game
    public int ticks;
    public int gameStatus = 0;
    public int score;
    public int speed;
    
    public int name_pointer;
    public ArrayList<Character> nameArray;
    public ArrayList<Explosion> explosions;
    public ArrayList<FireField> firefields;
    public boolean speed_increased;
    
    Image game_map = new ImageIcon(getClass().getResource("/resources/map1.gif")).getImage();
    Image menu1 = new ImageIcon(getClass().getResource("/resources/menu1.gif")).getImage();
    Image controls = new ImageIcon(getClass().getResource("/resources/controls.gif")).getImage();
   /* decides what repaint does every tick (Timer)
    *   0-9 ... Menu
    *       0 - Main menu
    *       1 - Scores
    *   10-19 ... Game
    *       10 - Playing
    *       11 - Paused
    *       12 - GameOver
    */
    
    //Keyboard-detection
    public boolean w = false, s = false, a = false, d = false,
            left = false, right = false, space = false, wsad_released;
    
    //Object dimesions for quick use in collision detection
    public final int PLAYER_W = 60, PLAYER_H = 40, ROCKET_W =7, ROCKET_H =23;
    public final int BOMBER_W = 100 , BOMBER_H =40 , BOMB_W =8, BOMB_H=10;
    
    //Main
    public static void main(String[] args) throws Exception {
        airInvasion = new AirInvasion();
    }

   /* Generator
    *   Creates a new jframe and a timer used for Antion Listener (action performed method).
    *   Creates a renderer
    *   Changes the mouse cursor image
    *   Sets the jframe settings
    */
    public AirInvasion() throws Exception {
        JFrame jframe = new JFrame();
        Timer timer = new Timer(20, this);
        renderer = new Renderer();
        topScores = new TopScores();
        
        //Jframe settings
        jframe.add(renderer);
        jframe.setDefaultCloseOperation(jframe.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.setResizable(false);
        jframe.setVisible(true);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
       
        //Changes gameStatus to 0 (Main menu) and starts the timer 
        gameStatus = 0;
        timer.start();
    }

    //Action listener triggered by timer
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStatus == 10) update();
        renderer.repaint();
        if (gameStatus == 12 && !w && !s && !a && !d) wsad_released=true;
    } 
    
    //Game launch (when player clicks PLAY)
    //  Sets the game related variables
    public void start(){
        speed = 200;
        ticks = 0;
        score = 0;
        gameStatus = 10;
        
        name_pointer = 1;
        nameArray = new ArrayList<>();
        explosions = new ArrayList<>();
        firefields = new ArrayList<>();
        
        player = new Player();
        enemies = new Enemies();
        enemies.generateBomber();
    }
    
    //When game is running, updates the game objects every frame
    public void update(){
        this.collisionHandling();
        ticks++;
        if (a) player.incVectorX(false);
        if (d) player.incVectorX(true);
        if (!a && !d) player.decVectorX();
        if (space && !player.jumped) player.jump();
        player.update();
        
        if (enemies.bombers.size() == 0) enemies.generateBomber();
        if (ticks >= speed && (ticks % speed) == 0){enemies.generateBomber();} //SPAWNS BOMBER!        
        
        enemies.update();
        if ( player.lives == 0) {gameStatus=12; wsad_released=false;} 
    }
    
    //GAME-OBJECT COLISION HANDLING
    public void collisionHandling(){
        ArrayList<Rocket> rockets = player.rockets;
        ArrayList<EnmBomber> bombers = enemies.bombers;
        //Checks if any BOMBER is hit
        // For rocket in rocket_array - compare with - for bomber in bomber_array
        for (int i = 0; i < rockets.size(); i++) {
            Rocket rocket = rockets.get(i);
            for (int n = 0; n < bombers.size(); n++) {
               EnmBomber bomber = bombers.get(n);
               if (rectIntersect(rocket.x, rocket.y, ROCKET_W, ROCKET_H,
                       bomber.x, bomber.y, BOMBER_W, BOMBER_H)){
                   score++;
                   
                   //ADDS PLAYER LIVES and INCREASES BOMBER SPAWN SPEED based on SCORE
                   if (score%15==0) player.lives++;
                   if (score%5==0 && score > 81) speed = speed - 15;
                   
                   bombers.remove(bomber);
                   rockets.remove(rocket);
                   
                   Explosion explosion = new Explosion(bomber.x + BOMBER_W/2, bomber.y + BOMBER_H/2, false);
                   explosions.add(explosion);
                   
                   break;
               }
            }
        }
        
        //Updates the rocket array in player class and bombers array in enemies class
        player.setRockets(rockets);
        enemies.setBombers(bombers);
        
        //Checks if player is hit by any enemy bomb
        ArrayList<EnmBomb> bombs = enemies.bombs;
        for (int i = 0; i < bombs.size(); i++){
            EnmBomb bomb = bombs.get(i);
                if (rectIntersect(bomb.x, bomb.y, BOMB_W, BOMB_H,
                       player.x, player.y, PLAYER_W, PLAYER_H)){
                    if (bomb.is_fire == true) {
                        player.lives = 0;
                        Explosion explosion = new Explosion(player.x + PLAYER_W/2, player.y + PLAYER_H/2, true);
                        explosions.add(explosion);
                        
                    } else {
                    player.lives--;
                    bombs.remove(bomb);
                    
                    Explosion explosion = new Explosion(player.x + PLAYER_W/2, player.y + PLAYER_H/2, false);
                    explosions.add(explosion); 
                       
                    }
                }
        }
        
        //Checks if player is in a fire field
        for (int i = 0; i < firefields.size(); i++){
            FireField firefield = firefields.get(i);
                if (rectIntersect(firefield.x, firefield.y, 150, 10,
                       player.x, player.y, PLAYER_W, PLAYER_H)){
                    player.lives--;
                    
                    Explosion explosion = new Explosion(player.x + PLAYER_W/2, player.y + PLAYER_H/2, false);
                    explosions.add(explosion); 
                       
                }
                
        }
        
        
        //Updates the bomb array in enemies class
        enemies.setBombs(bombs);
        
        //Deletes explosions
        for (int i = 0; i < explosions.size(); i++){
            Explosion explosion = explosions.get(i);
            if (explosion.ttl < 0) explosions.remove(explosion);
        }
        //Deletes explosions
        for (int i = 0; i < firefields.size(); i++){
            FireField firefield = firefields.get(i);
            if (firefield.ttl < 0) firefields.remove(firefield);
        }
    }
    
    //Rectangles collision check method
    // First rectangle -top right x1, y1; -width, heigth;
    // Second rectangle -top right x3, y3; -width, heihth;
    public boolean rectIntersect(int x1, int y1, int a_w, int a_h,
        int x3, int y3, int b_w, int b_h){
        //Calculates the other variables
        int x2 = x1 + a_w;
        int y2 = y1 + a_h;
        int x4 = x3 + b_w;
        int y4 = y3 + b_h;
        //If any of these conditions are met, the rectangles are NOT intersecting
        if ( x1 > x4 || x2 < x3 || y1 > y4 || y2 < y3) return false;
        else return true;
    } 
    
   /*Game renderer
    *   Draws the screen based on gameStatus
    *   
    */
    public void repaint(Graphics g){
        //Menu graphics
        if (gameStatus == 0 || gameStatus == 1){            
            g.drawImage(menu1, 0, 0, null);
            g.drawImage(controls, 440, 200, null);
            
            //Author and version
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 20));
            //g.drawString("Made by: Tomaž Lunder", 10, 760);
            g.drawString("v"+version, 740, 760);
            
        }
        //TODO: TOP 10 graphics
        if (gameStatus == 1){
            g.setColor(Color.gray);
            g.drawRect(440, 200, 300, 400);
            g.setColor(Color.gray.brighter());
            g.fillRect(440, 200, 300, 400);
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 28));
            g.drawString("TOP 10 PLAYERS:", 460, 225);
            g.drawString(topScores.top10.get(0).print(), 460, 260);
            g.drawString(topScores.top10.get(1).print(), 460, 295);
            g.drawString(topScores.top10.get(2).print(), 460, 330);
            g.drawString(topScores.top10.get(3).print(), 460, 365);
            g.drawString(topScores.top10.get(4).print(), 460, 400);
            g.drawString(topScores.top10.get(5).print(), 460, 435);
            g.drawString(topScores.top10.get(6).print(), 460, 470);
            g.drawString(topScores.top10.get(7).print(), 460, 505);
            g.drawString(topScores.top10.get(8).print(), 460, 540);
            g.drawString(topScores.top10.get(9).print(), 460, 575);
        }
        
        //If game is running (playing, paused, gameover)
        if (9 < gameStatus && gameStatus < 19){
            g.drawImage(game_map, 0, 0, null);
            
            //Buttons
            g.setColor(Color.white);
            g.drawRect(650, 710, 100, 40);
            g.drawRect(40, 710, 100, 40);
            g.setFont(new Font("Arial", 1, 25));
            g.drawString("MENU", 45, 740);
            g.drawString("PAUSE", 655, 740);
            
            g.setColor(Color.black);
            g.setFont(new Font("Arial", 1, 40));
            String score_text = "SCORE: " + score;
            g.drawString(score_text, 40, 50);
            
            //Draws the player !!! ALSO ammo and lives !!!
            player.draw(g);
            enemies.draw(g);
            for (Explosion explosion : explosions) explosion.draw(g);
            for (FireField firefield : firefields) firefield.draw(g);
            
            //Paused screen
            if (gameStatus == 11){
                g.setColor(Color.black);
                g.setFont(new Font("Arial", 1, 80));
                g.drawString("PAUSED", 235, 350);
                g.setFont(new Font("Arial", 1, 40));
                g.drawString("Click to continue", 235, 390);
            }
            
            //Game over screen
            if (gameStatus == 12){
                g.setColor(Color.red);
                g.setFont(new Font("Arial", 1, 80));
                g.drawString("GAME OVER", 160, 300);
                g.setColor(Color.black);
                g.setFont(new Font("Arial", 1, 35));
                g.drawString("Enter your name and press ENTER", 125, 340);
                
                //Draws the characters in nameArray (Player name _ _ _)
                int shift = 0;
                g.setFont(new Font("Arial", 1, 60));
                g.drawString("_", 290, 455);
                g.drawString("_", 390, 455);
                g.drawString("_", 490, 455);
                for (Character letter : nameArray){
                    String a = Character.toString(letter);
                    g.drawString(a.toUpperCase(), 285 + 100*shift, 450);
                    shift++;
                }
                shift = 0;
            }
            
        }
    }
    
    //Adds a new Explosion object to the explosions ArrayList
    public void addExplosion(int x, int y, boolean is_big){
        Explosion explosion = new Explosion(x, y, is_big);
        explosions.add(explosion);
    }
    
    public void addFirefield(int x, int y){
        FireField firefield = new FireField(x, y);
        firefields.add(firefield);
    }
    
    //Keyboard-handling
    @Override
    public void keyPressed(KeyEvent e) {
    int id = e.getKeyCode();

		if (id == KeyEvent.VK_W)
		{
			w = true;
		}  
                else if (id == KeyEvent.VK_S)
		{
			s = true;
		}  
                else if (id == KeyEvent.VK_A)
		{
			a = true;
                        //System.out.println("a pritisnjen");
		}  
                else if (id == KeyEvent.VK_D)
		{
			d = true;
                        //System.out.println("d pritisnjen");
		}  
                else if (id == KeyEvent.VK_LEFT)
		{
			left = true;
		}  
                else if (id == KeyEvent.VK_RIGHT)
		{
                        right = true;
		}
                else if (id == KeyEvent.VK_SPACE)
		{
			space = true;
		} 
    }

    @Override
    public void keyReleased(KeyEvent e){
        int id = e.getKeyCode();
        if (id == KeyEvent.VK_W)
	{
            w = false;
	}  
        if (id == KeyEvent.VK_S)
	{
            s = false;
	}  
        else if (id == KeyEvent.VK_A)
	{
            a = false;
	}  
        else if (id == KeyEvent.VK_D)
	{
            d = false;
	}  
        else if (id == KeyEvent.VK_LEFT)
	{
            left = false;
	}  
        else if (id == KeyEvent.VK_RIGHT)
	{
            right = false;
	}
        if (id == KeyEvent.VK_SPACE)
	{
            space = false;
	}
        
        if (gameStatus == 12 && nameArray.size() == 3 && e.getKeyCode() == KeyEvent.VK_ENTER){
            String line = "" + nameArray.get(0) + nameArray.get(1) + nameArray.get(2) + " " + Integer.toString(score);
            //System.out.printf("Line = %s\n", line);
            try {
                topScores.update(line.toUpperCase());
                System.out.println("updated");
            } catch (IOException ex) {
                System.out.println("IO EXCEPTION");
            }
            gameStatus = 0;
        }
        
        if (gameStatus == 12 && nameArray.size() > 0 && e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
            nameArray.remove(nameArray.size() - 1);
        }
    }
    
    //Handles typing in the name when the game is over
    @Override
    public void keyTyped(KeyEvent e) {
        int id = e.getKeyCode();
        if (gameStatus == 12 && nameArray.size()< 3 && wsad_released){
            char a = e.getKeyChar();
            if (Character.isLetter(a)){
                nameArray.add(a);
                name_pointer++;
            }
        }
    }
    
    //Mouse handling
    @Override
    public void mousePressed(MouseEvent e) {
        int x = e.getX()-3;
        int y = e.getY()-25;
        System.out.printf("%d, %d\n", x, y);
        //WHEN GAME IS RUNNING
        //If game is paused, unpause
        if (gameStatus == 11) gameStatus = 10;
        
        //If game is not paused or over 
        //  if clicked on "Menu" changes gameStatus to 0
        //  else if clicked on "Pause" changes gameStatus to 11;
        //else attempt to shoot a player rocket (if limit not reached)
        if (gameStatus == 10){
            if (710 < y && y < 750){
                if (40 < x && x < 140) gameStatus = 0;
                else if (650 < x && x < 750) gameStatus = 11;
            } else player.shoot();
        }
    
        //Buttons in the main menu
        //  If clicked "Play" launches the game by calling start();
        //  If clicked "Score" shows top 10 scores
        //  If clicked "Exit" quits the application.
        if (gameStatus == 0 || gameStatus == 1){
            if (120 < x && x < 350){
                if (200 < y && y < 300) start();
                if (350 < y && y < 450) {
                    if (gameStatus ==0) gameStatus = 1;
                    else if (gameStatus ==1) gameStatus = 0;
                }
                
                if (500 < y && y < 600) System.exit(0);
            }
        }
    }

    //Junk
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
}
