
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
    private int width;
    private int height;
    private Queue<Removal> removalQueue;
    
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
    
    private double getEnergy(Color[][] picture, int x, int y){
        Color left = new Color(0);
        Color right = new Color(0);
        Color top = new Color(0);
        Color bottom = new Color(0);

        if(x < 1 || x > picture.length-2 || y < 1 || y > picture[0].length-2){
            return 195075;
        }
        bottom = picture[x][y + 1];
        top = picture[x][y - 1];
        left = picture[x - 1][y];
        right = picture[x + 1][y];
        double ySum = 0;
        double xSum = Math.pow(right.getBlue() - left.getBlue(), 2) + Math.pow(right.getGreen() - left.getGreen(), 2) + Math.pow(right.getRed() - left.getRed(), 2);
        ySum = Math.pow(bottom.getBlue() - top.getBlue(), 2) + Math.pow(bottom.getGreen() - top.getGreen(), 2) + Math.pow(bottom.getRed() - top.getRed(), 2);
 
        return xSum + ySum;
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

    private int nodeID(int x, int y) {
        return x + y * energyGrid.length;
    }

    private int[] idToCoord(int id) {
        int[] coord = new int[2];
        coord[0] = id % energyGrid.length;
        coord[1] = (id - (id % energyGrid.length)) / energyGrid.length;
        return coord;
    }
    
    public SeamCarver(Picture picture){
        pic = picture;
        height = pic.height();
        width = pic.width();
        removalQueue = new Queue<Removal>();
        
        Color[][] tempPic = new Color[picture.width()][picture.height()];
        for(int i=0;i<picture.width();i++){
            for(int j=0;j<picture.height();j++){
                tempPic[i][j] = picture.get(i, j);
            }
        }
        energyGrid = generateEnergy(tempPic);
    }
    
    public Picture picture(){
        if(removalQueue.isEmpty()){
            return pic;
        }
        Color[][] tempPic = new Color[pic.width()][pic.height()];
//        Make copy of pic into Color array
        for(int x=0;x<tempPic.length;x++){
            for(int y=0;y<tempPic[0].length;y++){
                tempPic[x][y] = pic.get(x, y);
            }
        }
        Color[][] newTempPic = tempPic;
        while(!removalQueue.isEmpty()){
            Removal current = removalQueue.dequeue();
            int xMax = tempPic.length;
            int yMax = tempPic[0].length;
            if(current.getVertical()){
                newTempPic = new Color[tempPic.length -1][tempPic[0].length];
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
            }else{
                newTempPic = new Color[tempPic.length][tempPic[0].length-1];
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
        
        pic = new Picture(newTempPic.length, newTempPic[0].length);
        for(int x=0;x<newTempPic.length;x++){
            for(int y=0;y<newTempPic[0].length;y++){
                pic.set(x, y, newTempPic[x][y]);
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
    
    public double energy(int x, int y){
        return energyGrid[x][y];
    }   
    
    public int[] findHorizontalSeam(){
//        Only transpose if orientation is wrong.
        int[] results = new int[energyGrid.length];
        double[] dist = new double[energyGrid.length * energyGrid[0].length];
        int[] parentEdge = new int[energyGrid.length * energyGrid[0].length];
        boolean[] visited = new boolean[energyGrid.length * energyGrid[0].length];
        Queue bfsQueue = new Queue();
        for(int y=0;y<energyGrid[0].length;y++){
            int currentV = nodeID(0, y);
            dist[currentV] = 0;   
            parentEdge[currentV] = -1;
            bfsQueue.enqueue(currentV);
            visited[currentV] = true;
        }
        for(int x=1;x<energyGrid.length;x++){
            for(int y=0;y<energyGrid[0].length;y++){
                dist[nodeID(x, y)] = Double.MAX_VALUE;
            }
        }
        
        while(!bfsQueue.isEmpty()){
            int current = (int) bfsQueue.dequeue();
            int[] coord = idToCoord(current);
            double currentNodeDist = dist[current];

            if(coord[0] < energyGrid.length-1){
                if (coord[1] > 0) {
//                    Relax top node
                    int node = nodeID(coord[0] + 1, coord[1] - 1);
                    double oldDist = dist[node];
                    double newDist = currentNodeDist + energyGrid[coord[0]+1][coord[1]-1];
                    if(oldDist > newDist){
                        dist[node] = newDist;
                        parentEdge[node] = current;
                    }
                    if(!visited[node]){
                        bfsQueue.enqueue(node);
                        visited[node] = true;
                    }
                }
                if (coord[1] < energyGrid[0].length - 1) {
                    int node = nodeID(coord[0] + 1, coord[1] + 1);
                    double oldDist = dist[node];
                    double newDist = currentNodeDist + energyGrid[coord[0] + 1][coord[1] + 1];
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
                double newDist = currentNodeDist + energyGrid[coord[0] + 1][coord[1]];
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
        
        for(int i=0;i<energyGrid[0].length;i++){
            int node = nodeID(energyGrid.length-1, i);
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
        int[] results = new int[energyGrid[0].length];
        double[] dist = new double[energyGrid.length * energyGrid[0].length];
        int[] parentEdge = new int[energyGrid.length * energyGrid[0].length];
        boolean[] visited = new boolean[energyGrid.length * energyGrid[0].length];
        Queue bfsQueue = new Queue();
        for (int x = 0; x < energyGrid.length; x++) {
            int currentV = nodeID(x, 0);
            dist[currentV] = 0;
            parentEdge[currentV] = -1;
            bfsQueue.enqueue(currentV);
            visited[currentV] = true;
        }
        for (int y = 1; y < energyGrid[0].length; y++) {
            for (int x = 0; x < energyGrid.length; x++) {
                dist[nodeID(x, y)] = Double.MAX_VALUE;
            }
        }

        while (!bfsQueue.isEmpty()) {
            int current = (int) bfsQueue.dequeue();
            int[] coord = idToCoord(current);
            double currentNodeDist = dist[current];

            if (coord[1] < energyGrid[0].length - 1) {
                if (coord[0] > 0) {
//                    Relax top node
                    int node = nodeID(coord[0] - 1, coord[1] + 1);
                    double oldDist = dist[node];
                    double newDist = currentNodeDist + energyGrid[coord[0] - 1][coord[1] + 1];
                    if (oldDist > newDist) {
                        dist[node] = newDist;
                        parentEdge[node] = current;
                    }
                    if (!visited[node]) {
                        bfsQueue.enqueue(node);
                        visited[node] = true;
                    }
                }
                if (coord[0] < energyGrid.length - 1) {
                    int node = nodeID(coord[0] + 1, coord[1] + 1);
                    double oldDist = dist[node];
                    double newDist = currentNodeDist + energyGrid[coord[0] + 1][coord[1] + 1];
                    if (oldDist > newDist) {
                        dist[node] = newDist;
                        parentEdge[node] = current;
                    }
                    if (!visited[node]) {
                        visited[node] = true;
                        bfsQueue.enqueue(node);
                    }
                }
                int node = nodeID(coord[0], coord[1] + 1);
                double oldDist = dist[node];
                double newDist = currentNodeDist + energyGrid[coord[0]][coord[1] + 1];
                if (oldDist > newDist) {
                    dist[node] = newDist;
                    parentEdge[node] = current;
                }
                if (!visited[node]) {
                    bfsQueue.enqueue(nodeID(coord[0], coord[1] + 1));
                    visited[node] = true;
                }
            }
        }
        int maxNode = 0;
        double maxDist = Double.MAX_VALUE;

        for (int i = 0; i < energyGrid.length; i++) {
            int node = nodeID(i, energyGrid[0].length -1);
            if (dist[node] < maxDist) {
                maxDist = dist[node];
                maxNode = node;
            }
        }
        
        int count = results.length - 1;
        int[] coord = idToCoord(maxNode);
        results[count] = coord[0];
        count--;
        while (count >= 0) {
            maxNode = parentEdge[maxNode];
            int[] cd = idToCoord(maxNode);

            results[count] = cd[0];
            count--;
        }

        return results;
    }
    
    public void removeHorizontalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
        if(seam == null){
            throw new NullPointerException();
        }

        if (energyGrid[0].length < 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length != energyGrid.length) {
            throw new IllegalArgumentException();
        }
        
        removalQueue.enqueue(new Removal(seam, false));
        
        int yMax;
        int xMax;
        xMax = energyGrid.length;
        yMax = energyGrid[0].length;
        double[][] tempEnergy = new double[xMax][yMax-1];

        for(int x=0;x<xMax;x++){
            int diff = 0;

            for(int y=0;y<yMax;y++){
                if(y == seam[x]){
                    diff = 1;
                }else{
                    tempEnergy[x][y - diff] = energyGrid[x][y];
                }
            }
        }
        energyGrid = tempEnergy;
    }
    
    public void removeVerticalSeam(int[] seam) throws NullPointerException, IllegalArgumentException {
        if(seam == null){
            throw new NullPointerException();
        }

        if (energyGrid.length < 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length != energyGrid[0].length) {
            throw new IllegalArgumentException();
        }
        
        removalQueue.enqueue(new Removal(seam, true));
        
        int yMax;
        int xMax;
        xMax = energyGrid.length;
        yMax = energyGrid[0].length;
        double[][] tempEnergy = new double[xMax-1][yMax];

        for (int y = 0; y < yMax; y++) {
            int diff = 0;
            for (int x = 0; x < xMax; x++) {
                if (x == seam[y]) {
                    diff = 1;
                } else {
                    tempEnergy[x - diff][y] = energyGrid[x][y];
                }
            }
        }
        energyGrid = tempEnergy;
    }
    
    public static void main(String[] args){
        
    }
}
