/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

/**
 *
 * @author Tommy
 */
public class ScorePair {
    public String name;
    public int score;
    
    public ScorePair(String line){
        name = line.substring(0, 3);
        score = Integer.parseInt(line.substring(4));
    }
    
    public String print(){
        return name + " " + Integer.toString(score);
    }
    
}
