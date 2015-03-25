/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.lang.Integer;
/**
 *
 * @author johntan
 */


public class WordNet {
    private TreeMap<String, ArrayList<Integer>> nounIndex;
    private Digraph graph;
    private int countV;
    
    public WordNet (String synsets, String hypernyms) {
        nounIndex = new TreeMap<String, ArrayList<Integer>>();
        
        In scanSyn;
        In scanHyper;
        scanSyn = new In(synsets);
        scanHyper = new In(hypernyms);
        countV = 0;
        int countNouns = 0;
        while(scanSyn.hasNextLine()){
            String current = scanSyn.readLine();
            String[] splitCurrent = current.split(",");
            String[] nouns = splitCurrent[1].split(" ");
            countV ++;
//            Put in index that allows logn lookup of associated synsets via noun keys
            for(String noun : nouns) {
                if(!nounIndex.containsKey(noun)){
                    nounIndex.put(noun, new ArrayList<Integer>());
                }
                nounIndex.get(noun).add(Integer.parseInt(splitCurrent[0]));
            }
        }
        graph = new Digraph(countV);
        
        while (scanHyper.hasNextLine()) {
            String current = scanHyper.readLine();
            String[] hypers = current.split(",");
//            Add associated hypernyms
            for(int i=1;i<hypers.length;i++){
                graph.addEdge(Integer.parseInt(hypers[0]), Integer.parseInt(hypers[i]));
            }
        }
    }
    
    public Iterable<String> nouns() {
        Collection c = nounIndex.values();
        return (Iterable<String>) c.iterator();
    }
    
    public boolean isNoun(String word) {
        return nounIndex.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) {
//        BFS over graph, keep lookup table of (id, distance) to keep track of min distance from nounA
        int[] distanceTable = new int[countV];
        boolean[] visitedTable = new boolean[countV];
        ArrayList<Integer> startVertices = nounIndex.get(nounA);
        ArrayList<Integer> endVertices = nounIndex.get(nounB);
        for(int i=0;i<countV;i++){
            distanceTable[i] = Integer.MAX_VALUE;
        }
        Queue<Integer> bfsQueue = new Queue<Integer>();
        for(Integer v : startVertices){
            bfsQueue.enqueue(v);
            visitedTable[v] = true;
            distanceTable[v] = 0;
        }
        
        while(!bfsQueue.isEmpty()){
            Integer currentV = bfsQueue.dequeue();

            for(Integer v : graph.adj(currentV)){
                if(!visitedTable[v]){
                    bfsQueue.enqueue(v);
                    visitedTable[v] = true;
                    distanceTable[v] = Math.min(distanceTable[v], distanceTable[currentV] + 1);
                }
            }
        }        
        Integer minDist = Integer.MAX_VALUE;
        for(Integer v : endVertices){
            System.out.println("ENDV" + distanceTable[v]);
            minDist = Math.min(distanceTable[v], minDist);
        }
        return minDist;
    }
    
    public String sap(String nounA, String nounB) {
        
    }
    
    public static void main (String[] args) {
        WordNet test = new WordNet("src/week7/wordnet/synsets.txt", "src/week7/wordnet/hypernyms.txt");
        System.out.println(test.distance("white_marlin", "mileage"));
    }
}
