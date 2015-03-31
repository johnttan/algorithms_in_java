
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
    private Color[][] tempPic;
    private double[][] energyGrid;
    private double[][] tempEnergyGrid;
    private boolean vertical;
    private boolean rightSide;
    private int width;
    private int height;
    
    private double getEnergy(Color[][] picture, int x, int y){
        Color left = new Color(0);
        Color right = new Color(0);
        Color top = new Color(0);
        Color bottom = new Color(0);
        boolean edge = false;
        try {
            left = picture[x - 1][y];
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        try {
            right = picture[x + 1][y];
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        try {
            top = picture[x][y - 1];
        } catch (IndexOutOfBoundsException e) {
            edge = true;
        }
        try {
            bottom = picture[x][y + 1];
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
    
    private double[][] generateEnergy(Color[][] picture){
        double[][] grid = new double[picture.length][picture[0].length];
        
        for(int x=0;x<grid.length;x++){
            for(int y=0;y<grid[0].length;y++){
                grid[x][y] = getEnergy(picture, x, y);
            }
        }
        return grid;
    }
    
    private Color[][] transposeImage(Color[][] pic, boolean toTranspose){
        int oldWidth = pic.length;
        int oldHeight = pic[0].length;
        Color[][] picture = new Color[oldHeight][oldWidth];
        
        for(int x=0;x<oldWidth;x++){
            for(int y=0;y<oldHeight;y++){
                if(toTranspose){
                    picture[oldHeight - 1 - y][x] = pic[x][y];
                }else{
                    picture[y][oldWidth - 1 - x] = pic[x][y];
                }
            }
        }
        tempEnergyGrid = generateEnergy(picture);
        return picture;
    }
    private int nodeID(int x, int y) {
        return x + y * tempPic.length;
    }

    private int[] idToCoord(int id) {
        int[] coord = new int[2];
        coord[0] = id % tempPic.length;
        coord[1] = (id - (id % tempPic.length)) / tempPic.length;
        return coord;
    }
    
    public SeamCarver(Picture picture){
        pic = picture;
        height = pic.height();
        width = pic.width();
        tempPic = new Color[picture.width()][picture.height()];
        for(int i=0;i<picture.width();i++){
            for(int j=0;j<picture.height();j++){
                tempPic[i][j] = picture.get(i, j);
            }
        }
        rightSide = true;
        energyGrid = generateEnergy(tempPic);
        tempEnergyGrid = energyGrid;
    }
    
    public Picture picture(){
        return pic;
    }
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
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
        int[] results = new int[tempPic.length];
        double[] dist = new double[tempPic.length * tempPic[0].length];
        int[] parentEdge = new int[tempPic.length * tempPic[0].length];
        boolean[] visited = new boolean[tempPic.length * tempPic[0].length];
        Queue bfsQueue = new Queue();
        for(int y=0;y<tempPic[0].length;y++){
            int currentV = nodeID(0, y);
            dist[currentV] = 0;   
            parentEdge[currentV] = -1;
            bfsQueue.enqueue(currentV);
            visited[currentV] = true;
        }
        for(int x=1;x<tempPic.length;x++){
            for(int y=0;y<tempPic[0].length;y++){
                dist[nodeID(x, y)] = Double.MAX_VALUE;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            int current = (int) bfsQueue.dequeue();
            int[] coord = idToCoord(current);
            double currentNodeDist = dist[current];

            if(coord[0] < tempPic.length-1){
                if (coord[1] > 0) {
//                    Relax top node
                    int node = nodeID(coord[0] + 1, coord[1] - 1);
                    double oldDist = dist[node];
                    double newDist = currentNodeDist + tempEnergyGrid[coord[0]+1][coord[1]-1];
                    if(oldDist > newDist){
                        dist[node] = newDist;
                        parentEdge[node] = current;
                    }
                    if(!visited[node]){
                        bfsQueue.enqueue(node);
                        visited[node] = true;
                    }
                }
                if (coord[1] < tempPic[0].length - 1) {
                    int node = nodeID(coord[0] + 1, coord[1] + 1);
                    double oldDist = dist[node];
                    double newDist = currentNodeDist + tempEnergyGrid[coord[0] + 1][coord[1] + 1];
                    if (oldDist > newDist) {
                        dist[node] = newDist;
                        parentEdge[node] = current;
                    }
                    if(!visited[node]){
                        visited[node] = true;
                        bfsQueue.enqueue(node);
                    }
                }
                int node = nodeID(coord[0] + 1, coord[1]);
                double oldDist = dist[node];
                double newDist = currentNodeDist + tempEnergyGrid[coord[0] + 1][coord[1]];
                if (oldDist > newDist) {
                    dist[node] = newDist;
                    parentEdge[node] = current;
                }
                if(!visited[node]){
                    bfsQueue.enqueue(nodeID(coord[0] + 1, coord[1]));
                    visited[node] = true;
                }
            }
        }
        int maxNode = 0;
        double maxDist = Double.MAX_VALUE;
        
        for(int i=0;i<tempPic[0].length;i++){
            int node = nodeID(tempPic.length-1, i);
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
