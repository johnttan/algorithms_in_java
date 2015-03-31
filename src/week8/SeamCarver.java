
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
    private Picture tempPic;
    private double[][] energyGrid;
    private double[][] tempEnergyGrid;
    private boolean vertical;
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
            double xSum = Math.pow(right.getBlue() - left.getBlue(), 2) + Math.pow(right.getGreen() - left.getGreen(), 2) + Math.pow(right.getRed() - left.getRed(), 2);
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
    
    private Picture transposeImage(Picture pic, boolean toTranspose){
        Picture picture = new Picture(pic.height(), pic.width());
        for(int x=0;x<pic.width();x++){
            for(int y=0;y<pic.height();y++){
                if(toTranspose){
                    picture.set(pic.height() - 1 - y, x, pic.get(x, y));
                }else{
                    picture.set(y, pic.width() - 1 - x, pic.get(x, y));
                }
            }
        }
        tempEnergyGrid = generateEnergy(picture);
        return picture;
    }
    private int nodeID(int x, int y) {
        return x + y * tempPic.width();
    }

    private int[] idToCoord(int id) {
        int[] coord = new int[2];
        coord[0] = id % tempPic.width();
        coord[1] = (id - (id % tempPic.width())) / tempPic.width();
        return coord;
    }
    
    public SeamCarver(Picture picture){
        pic = picture;
        tempPic = new Picture(picture);
        rightSide = true;
        energyGrid = generateEnergy(pic);
        tempEnergyGrid = energyGrid;
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
        if(!rightSide && !vertical){
            tempPic = transposeImage(tempPic, rightSide);
            rightSide = true;
        }
        int[] results = new int[tempPic.width()];
        double[] dist = new double[tempPic.width() * tempPic.height()];
        int[] parentEdge = new int[tempPic.width() * tempPic.height()];
        Queue bfsQueue = new Queue();
        for(int y=0;y<tempPic.height();y++){
            int currentV = nodeID(0, y);
            dist[currentV] = 0;   
            parentEdge[currentV] = -1;
            bfsQueue.enqueue(currentV);

        }
        for(int x=1;x<tempPic.width();x++){
            for(int y=0;y<tempPic.height();y++){
                dist[nodeID(x, y)] = Double.MAX_VALUE;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            int current = (int) bfsQueue.dequeue();
            int[] coord = idToCoord(current);
            double currentNodeDist = dist[current];

            if(coord[0] < tempPic.width()-1){
                if (coord[1] > 0) {
//                    Relax top node
                    double oldDist = dist[nodeID(coord[0] + 1, coord[1] - 1)];
                    double newDist = currentNodeDist + tempEnergyGrid[coord[0]+1][coord[1]-1];
                    if(oldDist > newDist){
                        dist[nodeID(coord[0]+1, coord[1]-1)] = newDist;
                        parentEdge[nodeID(coord[0]+1, coord[1]-1)] = current;
                    }
                    bfsQueue.enqueue(nodeID(coord[0] + 1, coord[1] - 1));
                }
                if (coord[1] < tempPic.height() - 1) {
                    double oldDist = dist[nodeID(coord[0] + 1, coord[1] + 1)];
                    double newDist = currentNodeDist + tempEnergyGrid[coord[0] + 1][coord[1] + 1];
                    if (oldDist > newDist) {
                        dist[nodeID(coord[0] + 1, coord[1] + 1)] = newDist;
                        parentEdge[nodeID(coord[0] + 1, coord[1] + 1)] = current;
                    }
                    bfsQueue.enqueue(nodeID(coord[0] + 1, coord[1] + 1));
                }
                double oldDist = dist[nodeID(coord[0] + 1, coord[1])];
                double newDist = currentNodeDist + tempEnergyGrid[coord[0] + 1][coord[1]];
                if (oldDist > newDist) {
                    dist[nodeID(coord[0] + 1, coord[1])] = newDist;
                    parentEdge[nodeID(coord[0] + 1, coord[1])] = current;
                }
                bfsQueue.enqueue(nodeID(coord[0] +1, coord[1]));
            }
        }
        int maxNode = 0;
        double maxDist = Double.MAX_VALUE;
        
        for(int i=0;i<tempPic.height();i++){
            int node = nodeID(tempPic.width()-1, i);
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
        vertical = true;
        if(rightSide){
            tempPic = transposeImage(tempPic, rightSide);
        }
        int[] horizontalSeam = findHorizontalSeam();
        vertical = false;
        int[] verticalSeam = new int[pic.height()];
        
        for(int i=0;i<verticalSeam.length;i++){
            verticalSeam[verticalSeam.length-1-i] = horizontalSeam[i];
        }

        return verticalSeam;
    }
    
    public void removeHorizontalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
        if(seam == null){
            throw new NullPointerException();
        }
        if(pic.height() < 1){
            throw new IllegalArgumentException();
        }
        if(seam.length != pic.width()){
            throw new IllegalArgumentException();
        }
        Picture newPic = new Picture(pic.width(), pic.height() - 1);
        for(int x=0;x<pic.width();x++){
            int diff = 0;

            for(int y=0;y<pic.height();y++){
                if(y == seam[x]){
                    diff = 1;
                }else{
                    newPic.set(x, y-diff, pic.get(x, y));
                }
            }
        }
        pic = newPic;
        if(!rightSide){
            tempPic = new Picture(transposeImage(pic, true));

        }else{
            tempPic = new Picture(pic);
            tempEnergyGrid = generateEnergy(tempPic);
        }
    }
    
    public void removeVerticalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
        if(seam == null){
            throw new NullPointerException();
        }
        if(pic.width() < 1){
            throw new IllegalArgumentException();
        }
        if(seam.length != pic.height()){
            throw new IllegalArgumentException();
        }
        Picture newPic = new Picture(pic.width()-1, pic.height());
        for (int y = 0; y < pic.height(); y++) {
            int diff = 0;
            for (int x = 0; x < pic.width(); x++) {
                if (x == seam[y]) {
                    diff = 1;
                } else {
                    newPic.set(x-diff, y, pic.get(x, y));
                }
            }
        }
        pic = newPic;
        if (!rightSide) {
            tempPic = new Picture(transposeImage(pic, true));
        } else {
            tempPic = new Picture(pic);
            tempEnergyGrid = generateEnergy(tempPic);
        }
    }
    
    public static void main(String[] args){
        
    }
}
