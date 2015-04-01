
import java.awt.Color;
import java.util.Arrays;

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
    private int width;
    private int height;
    private Queue<Removal> removalQueue;
    private int[][][] tempPic;
    private class Removal{
        private int[] seam;
        private boolean vertical;
        public Removal(int[] seamA, boolean vert){
            seam = seamA;
            vertical = vert;
        }
        public int[] getSeam(){
            return seam;
        }
        public boolean getVertical(){
            return vertical;
        }
    }
    
    private double getEnergy(int[][][] picture, int x, int y){
        int[] left = new int[3];
        int[] right = new int[3];
        int[] top = new int[3];
        int[] bottom = new int[3];

        if(x < 1 || x > picture.length-2 || y < 1 || y > picture[0].length-2){
            return 195075;
        }
        bottom = picture[x][y + 1];
        top = picture[x][y - 1];
        left = picture[x - 1][y];
        right = picture[x + 1][y];
        double ySum = 0;
        double xSum = Math.pow(right[2] - left[2], 2) + Math.pow(right[1] - left[1], 2) + Math.pow(right[0] - left[0], 2);
        ySum = Math.pow(bottom[2] - top[2], 2) + Math.pow(bottom[1] - top[1], 2) + Math.pow(bottom[0] - top[0], 2);
 
        return xSum + ySum;
    }
    
    public SeamCarver(Picture picture){
        height = picture.height();
        width = picture.width();
        removalQueue = new Queue<Removal>();
        
        tempPic = new int[picture.width()][picture.height()][3];
        for(int i=0;i<picture.width();i++){
            for(int j=0;j<picture.height();j++){
                int[] color = new int[3];
                color[0] = picture.get(i, j).getRed();
                color[1] = picture.get(i, j).getGreen();
                color[2] = picture.get(i, j).getBlue();
                tempPic[i][j] = color;
            }
        }
    }
    
    public Picture picture(){
        updateGrids();
        
        Picture pic = new Picture(tempPic.length, tempPic[0].length);
        for(int x=0;x<tempPic.length;x++){
            for(int y=0;y<tempPic[0].length;y++){
                int[] color = tempPic[x][y];
                pic.set(x, y, new Color(color[0], color[1], color[2]));
            }
        }
        return pic;
    }
    
    public int width(){
        return width;
    }
    
    public int height(){
        return height;
    }
    
    public double energy(int x, int y) throws IndexOutOfBoundsException {
        if(x < 0 || x > tempPic.length-1 || y < 0 || y > tempPic[0].length-1){
            throw new IndexOutOfBoundsException();
        }
        return getEnergy(tempPic, x, y);
    }   
    private void updateGrids(){
        if (!removalQueue.isEmpty()) {
            int[][][] newTempPic = new int[tempPic.length][tempPic[0].length][3];
//        Make copy of pic into Color array
            for (int x = 0; x < tempPic.length; x++) {
                for (int y = 0; y < tempPic[0].length; y++) {
                    newTempPic[x][y] = tempPic[x][y];
                }
            }
            while (!removalQueue.isEmpty()) {
                Removal current = removalQueue.dequeue();
                int xMax = tempPic.length;
                int yMax = tempPic[0].length;
                if (current.getVertical()) {
                    newTempPic = new int[tempPic.length - 1][tempPic[0].length][3];
                    for (int y = 0; y < yMax; y++) {
                        int diff = 0;
                        for (int x = 0; x < xMax; x++) {
                            if (x == current.getSeam()[y]) {
                                diff = 1;
                            } else {
                                newTempPic[x - diff][y] = tempPic[x][y];
                            }
                        }
                    }
                    tempPic = newTempPic;
                } else {
                    newTempPic = new int[tempPic.length][tempPic[0].length - 1][3];
                    for (int x = 0; x < xMax; x++) {
                        int diff = 0;
                        for (int y = 0; y < yMax; y++) {
                            if (y == current.getSeam()[x]) {
                                diff = 1;
                            } else {
                                newTempPic[x][y - diff] = tempPic[x][y];
                            }
                        }
                    }
                    tempPic = newTempPic;
                }
            }
        }
    }
    public int[] findHorizontalSeam(){
//        Only transpose if orientation is wrong.
        updateGrids();
        int[] results = new int[tempPic.length];
        double[][] dist = new double[tempPic.length][tempPic[0].length];
        int[][][] parentEdge = new int[tempPic.length][tempPic[0].length][2];
        
        for(int x=0;x<tempPic.length;x++){
            for(int y=0;y<tempPic[0].length;y++){
                dist[x][y] = Double.MAX_VALUE;
                parentEdge[x][y] = new int[]{-1, -1};
            }
        }
        for (int y = 0; y < tempPic[0].length; y++) {
            dist[0][y] = energy(0, y);
        }
        for(int x=1;x<tempPic.length;x++){
            for(int y=0;y<tempPic[0].length;y++){
                double ener = energy(x, y);
                if(y > 0 && dist[x-1][y-1] + ener < dist[x][y]){
                    dist[x][y] = dist[x - 1][y - 1] + ener;
                    parentEdge[x][y][0] = x-1;
                    parentEdge[x][y][1] = y - 1;
                }
                if (y < tempPic[0].length-1 && dist[x - 1][y + 1] + ener < dist[x][y]) {
                    dist[x][y] = dist[x - 1][y + 1] + ener;
                    parentEdge[x][y][0] = x - 1;
                    parentEdge[x][y][1] = y + 1;
                }
                if (dist[x - 1][y] + ener < dist[x][y]) {
                    dist[x][y] = dist[x - 1][y] + ener;
                    parentEdge[x][y][0] = x - 1;
                    parentEdge[x][y][1] = y;
                }
            }
        }
        int[] minNode = new int[2];
        double minDist = Double.MAX_VALUE;
//        Get start of shortest path;
        for(int y=0;y<tempPic[0].length;y++){
            if(dist[tempPic.length-1][y] < minDist){
                minDist = dist[tempPic.length - 1][y];
                minNode[0] = tempPic.length-1;
                minNode[1] = y;
            }
        }
        int count = results.length-1;
        results[count] = minNode[1];
        count --;
        while(count >= 0){
            minNode = parentEdge[minNode[0]][minNode[1]];

            results[count] = minNode[1];
            count--;
        }
        
        return results;
    }
    
    public int[] findVerticalSeam() {
        updateGrids();
        int[] results = new int[tempPic[0].length];
        double[][] dist = new double[tempPic.length][tempPic[0].length];
        int[][][] parentEdge = new int[tempPic.length][tempPic[0].length][2];

        for (int x = 0; x < tempPic.length; x++) {
            for (int y = 0; y < tempPic[0].length; y++) {
                dist[x][y] = Double.MAX_VALUE;
                parentEdge[x][y] = new int[]{-1, -1};
            }
        }
        for (int x = 0; x < tempPic.length; x++) {
            dist[x][0] = energy(x, 0);
        }
        for (int y = 1; y < tempPic[0].length; y++) {
            for (int x = 0; x < tempPic.length; x++) {
                double ener = energy(x, y);
                if (x > 0 && dist[x - 1][y - 1] + ener < dist[x][y]) {
                    dist[x][y] = dist[x - 1][y - 1] + ener;
                    parentEdge[x][y][0] = x - 1;
                    parentEdge[x][y][1] = y - 1;
                }
                if (x < tempPic.length - 1 && dist[x + 1][y - 1] + ener < dist[x][y]) {
                    dist[x][y] = dist[x + 1][y - 1] + ener;
                    parentEdge[x][y][0] = x + 1;
                    parentEdge[x][y][1] = y - 1;
                }
                if (dist[x][y-1] + ener < dist[x][y]) {
                    dist[x][y] = dist[x][y - 1] + ener;
                    parentEdge[x][y][0] = x;
                    parentEdge[x][y][1] = y - 1;
                }
            }
        }
        int[] minNode = new int[2];
        double minDist = Double.MAX_VALUE;
//        Get start of shortest path;
        for (int x = 0; x < tempPic.length; x++) {
            if (dist[x][tempPic[0].length-1] < minDist) {
                minDist = dist[x][tempPic[0].length-1];
                minNode[0] = x;
                minNode[1] = tempPic[0].length-1;
            }
        }
        int count = results.length - 1;
        results[count] = minNode[0];
        count--;
        while (count >= 0) {
            minNode = parentEdge[minNode[0]][minNode[1]];

            results[count] = minNode[0];
            count--;
        }

        return results;
    }
    
    public void removeHorizontalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
        if(seam == null){
            throw new NullPointerException();
        }

        if (height() < 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException();
        }
        height --;
        if(height == 0){
            width = 0;
        }
        removalQueue.enqueue(new Removal(seam, false));
    }
    
    public void removeVerticalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
        if(seam == null){
            throw new NullPointerException();
        }
        if (width() < 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length != height()) {
            throw new IllegalArgumentException();
        }
        width --;
        if(width == 0){
            height = 0;
        }
        removalQueue.enqueue(new Removal(seam, true));
    }
    
    public static void main(String[] args){
        
    }
}
