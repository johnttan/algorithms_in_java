
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
    
    private double[][] generateEnergy(Picture picture){
        double[][] grid = new double[picture.width()][picture.height()];
        
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                Color left;
                Color right;
                Color top;
                Color bottom;
                boolean edge = false;
                try{
                    left = picture.get(x-1, y);
                }catch(IndexOutOfBoundsException e){
                    edge = true;
                }
                try {
                    right = picture.get(x+1, y);
                } catch (IndexOutOfBoundsException e) {
                    edge = true;
                }
                try {
                    top = picture.get(x, y-1);
                } catch (IndexOutOfBoundsException e) {
                    edge = true;
                }
                try {
                    bottom = picture.get(x, y+1);
                } catch (IndexOutOfBoundsException e) {
                    edge = true;
                }
                if(edge){
                    grid[x][y] = 195075;
                }else{
                    double xSum = Math.pow(right.getBlue() - left.getBlue(), 2) + Math.pow(right.getGreen() - left.getGreen(), 2) + Math.pow(left.getRed() - left.getRed(), 2);
                    double ySum = Math.pow(bottom.getBlue() - top.getBlue(), 2) + Math.pow(bottom.getGreen() - top.getGreen(), 2) + Math.pow(bottom.getRed() - top.getRed(), 2);
                    grid[x][y] = xSum + ySum;
                }
            }
        }
        return grid;
    }
    
    private void generateGridAndGraph(Picture picture){
        energyGrid = generateEnergy(picture);
        EdgeWeightedDigraph verticalGraph = new EdgeWeightedDigraph(picture.height() * picture.width());
        for (int y = 0; y < energyGrid[0].length - 1; y++) {
            for (int x = 0; x < energyGrid.length; x++) {
                int currentV = x + y * energyGrid[0].length;
//                Add left node
                if (x > 0) {
                    DirectedEdge edge = new DirectedEdge(currentV, x - 1 + (y + 1) * energyGrid[0].length, energyGrid[x - 1][y + 1]);
                    verticalGraph.addEdge(edge);
                }
//                Add right node
                if (x < energyGrid.length - 1) {
                    DirectedEdge edge = new DirectedEdge(currentV, x + 1 + (y + 1) * energyGrid[0].length, energyGrid[x + 1][y + 1]);
                    verticalGraph.addEdge(edge);
                }
//                Add center node
                DirectedEdge edge = new DirectedEdge(currentV, x + 1 + (y + 1) * energyGrid[0].length, energyGrid[x + 1][y + 1]);
                verticalGraph.addEdge(edge);
            }
        }
        verticalTopo = new Topological(verticalGraph);
        
        EdgeWeightedDigraph horizontalGraph = new EdgeWeightedDigraph(picture.height() * picture.width());
        for( int x = 0; x < energyGrid.length - 1; x++){
            for (int y=0; y < energyGrid.length; y++){
                int currentV = x + y * energyGrid[0].length;
                
                if(y > 0){
                    DirectedEdge edge = new DirectedEdge(currentV, x + 1 + (y - 1) * energyGrid[0].length, energyGrid[x + 1][y - 1]);
                    horizontalGraph.addEdge(edge);
                }
                
                if(y < energyGrid[0].length - 1){
                    DirectedEdge edge = new DirectedEdge(currentV, x + 1 + (y + 1) * energyGrid[0].length, energyGrid[x + 1][y + 1]);
                    horizontalGraph.addEdge(edge);
                }
                
                DirectedEdge edge = new DirectedEdge(currentV, x + 1 + (y) * energyGrid[0].length, energyGrid[x + 1][y]);
                horizontalGraph.addEdge(edge);
            }
        }
        horizontalTopo = new Topological(horizontalGraph);
    }
    
    public SeamCarver(Picture picture){
        pic = picture;
        generateGridAndGraph(picture);
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
    
    public int[] findHorizontalSeam(){
        
    }
    
    public int[] findVerticalSeam(){
        
    }
    
    public void removeHorizontalSeam(int[] seam){
        
    }
    
    public void removeVerticalSeam(int[] seam){
        
    }
    
    public static void main(String[] args){
        
    }
}
