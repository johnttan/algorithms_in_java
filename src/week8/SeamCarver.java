
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
    private boolean rightSide;
            
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
    
    private void transposeImage(){
        Picture picture = new Picture(pic.height(), pic.width());
        if(rightSide){
            for(int x=0;x<pic.width();x++){
                for(int y=0;y<pic.height();y++){
                    picture.set(pic.height() -1 - x, x, pic.get(x, y));
                }
            }
        }else{
            for(int x=0;x<pic.width();x++){
                for(int y=0;y<pic.height();y++){
                    picture.set(y, pic.width()-1-x, pic.get(x, y));
                }
            }
        }
        pic = picture;
        transpose = false;
    }
    private int nodeID(int x, int y) {
        return x + y * pic.width();
    }

    private int[] idToCoord(int id) {
        int[] coord = new int[2];
        coord[0] = id % pic.width();
        coord[1] = (id - (id % pic.width())) / pic.width();
        return coord;
    }
    public SeamCarver(Picture picture){
        pic = picture;
        rightSide = true;
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
    
    public int[] findHorizontalSeam(){
//        Only transpose if orientation is wrong.
        if(!rightSide){
            transposeImage();
            rightSide = true;
        }
        int[] results = new int[pic.width()];
        double[] dist = new double[pic.width() * pic.height()];
        int[] parentEdge = new int[pic.width() * pic.height()];
        Queue bfsQueue = new Queue();
        for(int y=0;y<pic.height();y++){
            int currentV = nodeID(0, y);
            dist[currentV] = 0;   
            parentEdge[currentV] = -1;
            bfsQueue.enqueue(currentV);

        }
        for(int x=1;x<pic.width();x++){
            for(int y=0;y<pic.height();y++){
                dist[nodeID(x, y)] = Double.MAX_VALUE;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            int current = (int) bfsQueue.dequeue();
            int[] coord = idToCoord(current);
            double currentNodeDist = dist[current];

            if(coord[0] < pic.width()-1){
                if (coord[1] > 0) {
//                    Relax top node
                    double oldDist = dist[nodeID(coord[0] + 1, coord[1] - 1)];
                    double newDist = currentNodeDist + energyGrid[coord[0]+1][coord[1]-1];
                    if(oldDist > newDist){
                        dist[nodeID(coord[0]+1, coord[1]-1)] = newDist;
                        parentEdge[nodeID(coord[0]+1, coord[1]-1)] = current;
                    }
                    bfsQueue.enqueue(nodeID(coord[0] + 1, coord[1] - 1));
                }
                if (coord[1] < pic.height() - 1) {
                    double oldDist = dist[nodeID(coord[0] + 1, coord[1] + 1)];
                    double newDist = currentNodeDist + energyGrid[coord[0] + 1][coord[1] + 1];
                    if (oldDist > newDist) {
                        dist[nodeID(coord[0] + 1, coord[1] + 1)] = newDist;
                        parentEdge[nodeID(coord[0] + 1, coord[1] + 1)] = current;
                    }
                    bfsQueue.enqueue(nodeID(coord[0] + 1, coord[1] + 1));
                }
                double oldDist = dist[nodeID(coord[0] + 1, coord[1])];
                double newDist = currentNodeDist + energyGrid[coord[0] + 1][coord[1]];
                if (oldDist > newDist) {
                    dist[nodeID(coord[0] + 1, coord[1])] = newDist;
                    parentEdge[nodeID(coord[0] + 1, coord[1])] = current;
                }
                bfsQueue.enqueue(nodeID(coord[0] +1, coord[1]));
            }
        }
        int maxNode = 0;
        double maxDist = Double.MAX_VALUE;
        
        for(int i=0;i<pic.height();i++){
            int node = nodeID(pic.width()-1, i);
            if(dist[node] < maxDist){
                maxDist = dist[node];
                maxNode = node;
            }
        }
        int count = results.length-1;
        int[] coord = idToCoord(maxNode);
        results[count] = coord[1];
        count --;
        while(count >= 0){
            maxNode = parentEdge[maxNode];
            int[] cd = idToCoord(maxNode);

            results[count] = cd[1];
            count--;
        }
        
        return results;
    }
    
    public int[] findVerticalSeam(){
        if(rightSide){
            transposeImage();
        }
        int[] horizontalSeam = findHorizontalSeam();
        rightSide = false;
        int[] verticalSeam = new int[pic.width()];
        
        for(int i=0;i<horizontalSeam.length;i++){
            System.out.println(horizontalSeam[i]);
        }
        return verticalSeam;
    }
    
    public void removeHorizontalSeam(int[] seam){
        
    }
    
    public void removeVerticalSeam(int[] seam){
        
    }
    
    public static void main(String[] args){
        
    }
}
