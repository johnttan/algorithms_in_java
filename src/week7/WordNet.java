/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
import java.lang.Integer;
import java.util.Enumeration;
import java.util.Hashtable;
/**
 *
 * @author johntan
 */


public class WordNet {
    private TreeMap<String, ArrayList<Integer>> nounIndex;
    private Hashtable<Integer, String> synSets;
    private Digraph graph;
    private int countV;
    
    public WordNet (String synsets, String hypernyms) {
        nounIndex = new TreeMap<String, ArrayList<Integer>>();
        synSets = new Hashtable<Integer, String>();
        
        In scanSyn;
        In scanHyper;
        scanSyn = new In(synsets);
        scanHyper = new In(hypernyms);
        while(scanSyn.hasNextLine()){
            String current = scanSyn.readLine();
            String[] splitCurrent = current.split(",");
            String[] nouns = splitCurrent[1].split(" ");
            synSets.put(Integer.parseInt(splitCurrent[0]), splitCurrent[1]);
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
        int[] distanceStartTable = new int[countV];
        int[] distanceEndTable = new int[countV];
        boolean[] visitedTable = new boolean[countV];
        boolean[] secondTable = new boolean[countV];
        Hashtable<Integer, ArrayList<Integer>> parentTable;
        Hashtable<Integer, Integer> countTable;
        
        parentTable = new Hashtable<Integer, ArrayList<Integer>>();
        countTable = new Hashtable<Integer, Integer>();
        
        ArrayList<Integer> startVertices = nounIndex.get(nounA);
        ArrayList<Integer> endVertices = nounIndex.get(nounB);
        
        Queue<Integer> bfsQueue = new Queue<Integer>();
        
        for(int i=0;i<countV;i++){
            distanceStartTable[i] = Integer.MAX_VALUE;
        }
        
        for (int i = 0; i < countV; i++) {
            distanceEndTable[i] = Integer.MAX_VALUE;
        }
        
        for(Integer v : startVertices){
            bfsQueue.enqueue(v);
            visitedTable[v] = true;
            distanceStartTable[v] = 0;
        }
        
        while(!bfsQueue.isEmpty()){
            Integer currentV = bfsQueue.dequeue();
            for(Integer v : graph.adj(currentV)){
                distanceStartTable[v] = Math.min(distanceStartTable[v], distanceStartTable[currentV] + 1);
                bfsQueue.enqueue(v);
                visitedTable[v] = true;
            }
        }        
        
        for(Integer v : endVertices){
            bfsQueue.enqueue(v);
            distanceEndTable[v] = 0;
            if (!parentTable.contains(v)) {
                parentTable.put(v, new ArrayList<Integer>());
            }
        }
        
        while (!bfsQueue.isEmpty()) {
            Integer currentV = bfsQueue.dequeue();
  
            for (Integer v : graph.adj(currentV)) {
//                Add list of parents for currentV if it has been marked
                if (visitedTable[v]) {
                    secondTable[v] = true;
                }
                if (!parentTable.contains(v)) {
                    parentTable.put(v, new ArrayList<Integer>());
                }
                parentTable.get(currentV).add(v);
                distanceEndTable[v] = Math.min(distanceEndTable[v], distanceEndTable[currentV] + 1);
                bfsQueue.enqueue(v);
            }

        }
        
        for(int i=0;i<secondTable.length;i++){
            if(secondTable[i]){
                if(!countTable.contains(i)){
                    countTable.put(i, 0);
                }
                for(Integer parent : parentTable.get(i)){
                    countTable.put(parent, 1);
                }
            }
        }
        
        int shortestPath = Integer.MAX_VALUE;
        Enumeration keys = countTable.keys();
        
        while(keys.hasMoreElements()){
            Integer key = (Integer) keys.nextElement();
            if(countTable.get(key) == 0 && secondTable[key] && distanceEndTable[key] + distanceStartTable[key] < shortestPath){
                shortestPath = distanceEndTable[key] + distanceStartTable[key];
            }
        }
        return shortestPath;
    }
    
    public String sap(String nounA, String nounB) {
        //        BFS over graph, keep lookup table of (id, distance) to keep track of min distance from nounA
        int[] distanceStartTable = new int[countV];
        int[] distanceEndTable = new int[countV];
        boolean[] visitedTable = new boolean[countV];
        boolean[] secondTable = new boolean[countV];
        Hashtable<Integer, ArrayList<Integer>> parentTable;
        Hashtable<Integer, Integer> countTable;

        parentTable = new Hashtable<Integer, ArrayList<Integer>>();
        countTable = new Hashtable<Integer, Integer>();

        ArrayList<Integer> startVertices = nounIndex.get(nounA);
        ArrayList<Integer> endVertices = nounIndex.get(nounB);

        Queue<Integer> bfsQueue = new Queue<Integer>();

        for (int i = 0; i < countV; i++) {
            distanceStartTable[i] = Integer.MAX_VALUE;
        }

        for (int i = 0; i < countV; i++) {
            distanceEndTable[i] = Integer.MAX_VALUE;
        }

        for (Integer v : startVertices) {
            bfsQueue.enqueue(v);
            visitedTable[v] = true;
            distanceStartTable[v] = 0;
        }

        while (!bfsQueue.isEmpty()) {
            Integer currentV = bfsQueue.dequeue();
            for (Integer v : graph.adj(currentV)) {
                distanceStartTable[v] = Math.min(distanceStartTable[v], distanceStartTable[currentV] + 1);
                bfsQueue.enqueue(v);
                visitedTable[v] = true;
            }
        }

        for (Integer v : endVertices) {
            bfsQueue.enqueue(v);
            distanceEndTable[v] = 0;
            if (!parentTable.contains(v)) {
                parentTable.put(v, new ArrayList<Integer>());
            }
        }

        while (!bfsQueue.isEmpty()) {
            Integer currentV = bfsQueue.dequeue();

            for (Integer v : graph.adj(currentV)) {
//                Add list of parents for currentV if it has been marked
                if (visitedTable[v]) {
                    secondTable[v] = true;
                }
                if (!parentTable.contains(v)) {
                    parentTable.put(v, new ArrayList<Integer>());
                }
                parentTable.get(currentV).add(v);
                distanceEndTable[v] = Math.min(distanceEndTable[v], distanceEndTable[currentV] + 1);
                bfsQueue.enqueue(v);
            }

        }

        for (int i = 0; i < secondTable.length; i++) {
            if (secondTable[i]) {
                if (!countTable.contains(i)) {
                    countTable.put(i, 0);
                }
                for (Integer parent : parentTable.get(i)) {
                    countTable.put(parent, 1);
                }
            }
        }

        int shortestPath = Integer.MAX_VALUE;
        int ancestor = 0;
        Enumeration keys = countTable.keys();

        while (keys.hasMoreElements()) {
            Integer key = (Integer) keys.nextElement();
//            Need to keep shortest ancestor
            if (countTable.get(key) == 0 && secondTable[key] && distanceEndTable[key] + distanceStartTable[key] < shortestPath) {
                shortestPath = distanceEndTable[key] + distanceStartTable[key];
                ancestor = key;
            }
        }
        return synSets.get(ancestor);
    }
    
    public static void main (String[] args) {
        WordNet test = new WordNet("src/week7/wordnet/synsets.txt", "src/week7/wordnet/hypernyms.txt");
        System.out.println(test.distance("white_marlin", "mileage"));
        System.out.println(test.distance("Black_Plague", "black_marlin"));
        System.out.println(test.distance("American_water_spaniel", "histology"));
        System.out.println(test.sap("worm", "bird"));
        System.out.println(test.sap("municipality", "region"));
        System.out.println(test.distance("mebibit", "Ascension"));
    }
}
