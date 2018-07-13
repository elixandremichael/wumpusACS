/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wumpusacs;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static wumpusacs.WumpusACS.MASKGOLD;
import static wumpusacs.WumpusACS.MASKUNKNOWN;
import static wumpusacs.WumpusACS.alfa;
import static wumpusacs.WumpusACS.beta;
import static wumpusacs.WumpusACS.random;


/**
 *
 * @author administrador
 */
public class Agent {
    public int contMoviment;
    
    private int line;
    
    private int column;
    
    private int n;                        
    
    public ArrayList<Cell> path;
    
    public int score;    
    
    private double feromonioTop;
    
    private double feromonioBottom;
    
    private double feromonioRight;
    
    private double feromonioLeft;
    
    Agent(int line, int column, int n) {
        this.contMoviment = 0;                        
        this.n = n;
        this.score = n*n;
        this.line = line;
        this.column = column;    
        this.path = new ArrayList<>();
        this.inicializeEnvironment();                       
        
    }
    
    public void inicializeEnvironment() {
        Cell cell = new Cell(0, 0, 0); //linha, coluna, feromonio que nao vai mudar        
        this.path.add(cell);
    }
    
    boolean isKnown(int line, int column) {        
        for(int i = 0; i < path.size(); i++) {            
            if(path.get(i).getI() == line && path.get(i).getJ() == column) return true;
        }
        return false;
    }
    
    public boolean moveTop() {        
        if(line > 0 && !isKnown(line-1,column)) return true;        
        return false;
    }
    
    public boolean moveBottom() {        
        if(line < n-1 && !isKnown(line+1,column)) return true;
        return false;
    }
    
    public boolean moveLeft() {
        if(column > 0 && !isKnown(line,column-1)) return true;
        return false;
    }
    
    public boolean moveRight() {
        if(column < n-1 && !isKnown(line,column+1)) return true;
        return false;
    }             
    
    public int getLine() {        
        return this.line;
    }
    
    public int getColumn() {        
        return this.column;
    }   
    
    public void action() {
        this.contMoviment++;
        System.out.println("Movimento: "+contMoviment);                
        movement();        
        //this.printKnown();        
    }               
    
    void printKnown() {
        for(int i = 0; i < path.size(); i++) {
            path.get(i).print();
        }
    }
        
    
    public void movement(){  
        double pesoTop = 0, pesoBottom = 0, pesoRight = 0, pesoLeft = 0, pesoTotal = 0;
        double distancia = 1/1;   
        int move = 0;
        double pesos[] = new double[4];
        for(int i = 0; i < 4; i++) pesos[i] = 0.0;        
        if(moveTop()) {
            double trs = Math.pow(feromonioTop, alfa);
            double nrs = Math.pow(distancia, beta);
            pesoTop = trs*nrs;
            pesos[0] = pesoTop;
        }
        
        if(moveBottom()) {
            double trs = Math.pow(feromonioBottom, alfa);
            double nrs = Math.pow(distancia, beta);
            pesoBottom = trs*nrs;
            pesos[1] = pesoBottom;
        }
        
        if(moveRight()) {
            double trs = Math.pow(feromonioRight, alfa);
            double nrs = Math.pow(distancia, beta);
            pesoRight = trs*nrs;
            pesos[2] = pesoRight;
        }
        
        if(moveLeft()) {
            double trs = Math.pow(feromonioLeft, alfa);
            double nrs = Math.pow(distancia, beta);
            pesoLeft = trs*nrs;
            pesos[3] = pesoLeft;
        }
        pesoTotal = pesoLeft + pesoRight + pesoBottom + pesoTop;
        Random r = new Random();
        
        double randomValue = r.nextDouble()*pesoTotal;
        double acumulador = 0.0;
        
        System.out.println("peso total: "+pesoTotal);
        System.out.println("random: "+randomValue);
        
        for(int i = 0; i < pesos.length; i++) {
            acumulador += pesos[i];
            if(randomValue < acumulador) {
                move = i + 1;
                break;
            }
        }                
        if(move == 1) line--;
        else if(move == 2) line++;
        else if(move == 3) column++;
        else if(move == 4) column--;
        else movementRondom();
        
        this.path.add(new Cell(line, column, 0));
        
        
    }        
    
    public void movementRondom(){    
        int count = 0;
        while(true) {
            int i = random.nextInt(4);
            if(i == 0 && this.moveTop()) {
                line--;
                return;
            } else if(i == 1 && this.moveRight()){
                column++;
                return;
            } else if(i == 2 && this.moveBottom()){
                line++;
                return;
            } else if(i == 3 && this.moveLeft()){
                column--;
                return;
            }   
            count++;
            
            if(count > 20) {
                System.out.println("Formiga Morta");
                System.exit(0);
            }
        }                
    }
    
    public int randomMovement(boolean positions[]) {        
        while(true) {
            int i = random.nextInt(4);
            if(positions[i])
                return i;
        }
    }
    
    void setGold() {
        this.score += 20;
        int size = this.path.size();        
        this.path.get(size-1).setInfo(MASKGOLD);
    }
    
    void setExit() {
        this.score += 10;
    }
    
    void setDead() {
        this.score -= 50;
    }
    
    void setFeromonioTop(double f) {
        this.feromonioTop = f;
    }
    
    void setFeromonioBottom(double f) {        
        this.feromonioBottom = f;
    }
    
    void setFeromonioRight(double f) {
        this.feromonioRight = f;
    }
    
    void setFeromonioLeft(double f) {
        this.feromonioLeft = f;
    }
}
