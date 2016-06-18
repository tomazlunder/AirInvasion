/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package airinvasion;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Tommy
 */
public class TopScores{
    //File file;
    ArrayList<ScorePair> top10 = new ArrayList<>();
    File file;
    String path;
   
    public TopScores() throws Exception{
        createSaveDirectory();
    }
    
    public void readEmbedded() throws IOException{
        top10 = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/resources/top_scores.txt")));
        System.out.println("Prebral score iz jar");
        for (int i = 0; i < 10; i++) {
            String line = reader.readLine();
            System.out.printf("%s\n", line);
            top10.add(new ScorePair(line));
        }
        reader.close();
    }
    
    public void readFile() throws IOException{
        top10 = new ArrayList<>();
        Scanner sc = new Scanner(file);
        int i = 0;
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            ScorePair pair = new ScorePair(line);
            top10.add(pair);
            i++;
        }
        sc.close();
        System.out.println("Prebral score iz user");
    }
    
    public void createSaveDirectory() throws IOException {
        path = System.getProperty("user.home") + File.separator + "AirInvasion";
        new File(path).mkdirs();
        file = new File(path  + "/scores.txt");
        boolean is_new = file.createNewFile();
        if (is_new){
            readEmbedded();
            write();
        } else {
            readFile();
        }
}
    
    public void write() throws IOException{
        PrintWriter writer = new PrintWriter(path + "/scores.txt", "UTF-8");
        for (int i = 0; i < 10; i++){
            ScorePair pair = top10.get(i);
            writer.println(pair.print());
            System.out.printf("%s\n", pair.print());
        }
        writer.close();
    }
    
    public void update(String line) throws IOException{
        ScorePair buff1 = new ScorePair(line), buff2;
        boolean replace = false;
        
        for(int i = 0; i < 10; i++){
            if (!replace && top10.get(i).score < buff1.score){
                replace = true;
            }
            if (replace){
                buff2 = top10.get(i);
                top10.set(i, buff1);
                buff1 = buff2;
            }
        }
        if (replace){
            write();
        } 
    }
}
