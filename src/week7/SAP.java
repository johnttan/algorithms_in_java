/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author johntan
 */
public class SAP {
    private Digraph graph;
    
    public SAP(Digraph G){
        graph = new Digraph(G);
    }
    
    public int length(int v, int w){
//        O(V + E);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
//        O(V + E);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);
        
        int minDist = Integer.MAX_VALUE;
        for(int i=0;i<graph.V();i++){
            if(bfsV.hasPathTo(i) && bfsW.hasPathTo(i)){
                minDist = Math.min(bfsV.distTo(i) + bfsW.distTo(i), minDist);
            }
        }
        return minDist;
    }
    
    public int ancestor(int v, int w){
        //        O(V + E);
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
//        O(V + E);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);

        int minDist = Integer.MAX_VALUE;
        int ancestor = -1;
        for (int i=0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if(dist < minDist){
                    ancestor = i;
                    minDist = dist;
                }
            }
        }
        return ancestor;
    }
    
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);

        int minDist = Integer.MAX_VALUE;
        for (int i=0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                minDist = Math.min(bfsV.distTo(i) + bfsW.distTo(i), minDist);
            }
        }
        return minDist;
    }
    
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(graph, w);

        int minDist = Integer.MAX_VALUE;
        int ancestor = 0;
        for (int i=0; i < graph.V(); i++) {
            if (bfsV.hasPathTo(i) && bfsW.hasPathTo(i)) {
                int dist = bfsV.distTo(i) + bfsW.distTo(i);
                if(dist < minDist){
                    ancestor = i;
                    minDist = dist;
                }
            }
        }
        return ancestor;
    }
    
    public static void main(String[] args){
    }
}
