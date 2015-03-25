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
        boolean[] secondTable = new boolean[countV];
        ArrayList<ArrayList<Integer>> parentTable;
        ArrayList<Integer> countTable;
        
        parentTable = new ArrayList<ArrayList<Integer>>();
        countTable = new ArrayList<Integer>();
        
        ArrayList<Integer> startVertices = nounIndex.get(nounA);
        ArrayList<Integer> endVertices = nounIndex.get(nounB);
        
        Queue<Integer> bfsQueue = new Queue<Integer>();
        
        for(Integer v : startVertices){
            bfsQueue.enqueue(v);
            visitedTable[v] = true;
        }
        
        while(!bfsQueue.isEmpty()){
            Integer currentV = bfsQueue.dequeue();

            for(Integer v : graph.adj(currentV)){
                bfsQueue.enqueue(v);
                visitedTable[v] = true;
            }
        }        
        
        for(Integer v : endVertices){
            bfsQueue.enqueue(v);
            if(visitedTable[v]){
                secondTable[v] = true;
            }
        }
        
        while (!bfsQueue.isEmpty()) {
            Integer currentV = bfsQueue.dequeue();

            for (Integer v : graph.adj(currentV)) {
//                Add list of parents for currentV
                if(secondTable[currentV]){
                    parentTable.get(currentV).add(v);
                }
                bfsQueue.enqueue(v);
//                If it has been visited in traversal of start nodes, then mark it here
                if(visitedTable[v]){
                    secondTable[v] = true;
                }
            }
        }
        
        for(int i=0;i<secondTable.length;i++){
            if(secondTable[i]){
                for(Integer parent : parentTable.get(i)){
                    countTable.add(parent, 1);
                }
            }
        }
        
        
    }
    
    public String sap(String nounA, String nounB) {
        
    }
    
    public static void main (String[] args) {
        WordNet test = new WordNet("src/week7/wordnet/synsets.txt", "src/week7/wordnet/hypernyms.txt");
        System.out.println(test.distance("white_marlin", "mileage"));
    }
}
