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
    private int[][] energyGrid;
    
    private int[][] generateEnergy(Picture picture){
        int[][] grid = new int[picture.width()][picture.height()];
        
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                
            }
        }
    }
    
    public SeamCarver(Picture picture){
        pic = picture;
        energyGrid = generateEnergy(picture);
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
        
    }
    
    public int[] findHorizontalSeam(){
        
    }
    
    public int[] findVerticalSeam(){
        
    }
    
    public void removeHorizontalSeam(int[] seam){
        
    }
    
    public void removeVerticalSeam(int[] seam){
        
    }
}
