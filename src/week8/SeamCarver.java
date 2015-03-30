
import java.awt.Color;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author johntan
 */
public class SeamCarver {
    private Picture pic;
    private double[][] energyGrid;
    private Topological verticalTopo;
    private Topological horizontalTopo;
    
    private double getEnergy(Picture picture, int x, int y){
        Color left = new Color(0);
        Color right = new Color(0);
        Color top = new Color(0);
        Color bottom = new Color(0);
        boolean edge = false;
        try {
            left = picture.get(x - 1, y);
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        try {
            right = picture.get(x + 1, y);
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        try {
            top = picture.get(x, y - 1);
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        try {
            bottom = picture.get(x, y + 1);
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        if (edge) {
            return 195075;
        } else {
            double xSum = Math.pow(right.getBlue() - left.getBlue(), 2) + Math.pow(right.getGreen() - left.getGreen(), 2) + Math.pow(left.getRed() - left.getRed(), 2);
            double ySum = Math.pow(bottom.getBlue() - top.getBlue(), 2) + Math.pow(bottom.getGreen() - top.getGreen(), 2) + Math.pow(bottom.getRed() - top.getRed(), 2);
            return xSum + ySum;
        }
    }
    
    private double[][] generateEnergy(Picture picture){
        double[][] grid = new double[picture.width()][picture.height()];
        
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                grid[x][y] = getEnergy(picture, x, y);
            }
        }
        return grid;
    }
    
    public SeamCarver(Picture picture){
        pic = picture;
        energyGrid = generateEnergy(pic);
    }
    
    public Picture picture(){
        return pic;
    }
    
    public int width(){
        return pic.width();
    }
    
    public int height(){
        return pic.height();
    }
    
    public double energy(int x, int y){
        return energyGrid[x][y];
    }
    private int nodeID(int x, int y){
        return x + y * pic.height();
    }
    public int[] findHorizontalSeam(){
        int[] results = new int[pic.width()];
        double[] dist = new double[pic.width() * pic.height()];
        int[] parentEdge = new int[pic.width() * pic.height()];
        
        for(int y=0;y<pic.height();y++){
            dist[nodeID(0, y)] = 0;   
            parentEdge[nodeID(0, y)] = -1;
        }
        for(int x=0;x<pic.width()-1;x++){
            for(int y=0;y<pic.height();y++){
                dist[nodeID(x, y)] = Double.MAX_VALUE;
            }
        }
        
        return new int[0];
    }
    
    public int[] findVerticalSeam(){
        return new int[0];
    }
    
    public void removeHorizontalSeam(int[] seam){
        
    }
    
    public void removeVerticalSeam(int[] seam){
        
    }
    
    public static void main(String[] args){
        
    }
}
