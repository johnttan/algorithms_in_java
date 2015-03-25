/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Collection;
/**
 *
 * @author johntan
 */


public class WordNet {
    private class SynSet {
        private String id;
        private ArrayList<String> edges;
        public SynSet (String id){
            id = id;
            edges = new ArrayList<String>();
        }
        
        public void addEdge (String id){
            edges.add(id);
        }
    }
    private TreeMap nounIndex;
    private TreeMap graph;
    
    public WordNet (String synsets, String hypernyms) {
        nounIndex = new TreeMap();
        graph = new TreeMap();
        
        In scanSyn;
        In scanHyper;
        scanSyn = new In(synsets);
        scanHyper = new In(hypernyms);

        while(scanSyn.hasNextLine()){
            String current = scanSyn.readLine();
            String[] splitCurrent = current.split(",");
            String[] nouns = splitCurrent[1].split(" ");
            SynSet syn = new SynSet(splitCurrent[0]);
//            Put in Graph
            graph.put(splitCurrent[0], syn);
//            Put in index that allows logn lookup of associated synsets via noun keys
            for(String noun : nouns) {
                nounIndex.put(noun, syn);
            }
        }
        
        while (scanHyper.hasNextLine()) {
            String current = scanSyn.readLine();
            String[] hypers = current.split(",");
            SynSet currentSyn = (SynSet) graph.get(hypers[0]);
//            Add associated hypernyms
            for(int i=1;i<hypers.length;i++){
                currentSyn.addEdge(hypers[i]);
            }
        }
        
    }
    
    public Iterable<String> nouns() {
        Collection c = nounIndex.values();
        return (Iterable<String>) c.iterator();
    }
    
    public boolean isNoun(String word) {
        
    }
    
    public int distance(String nounA, String nounB) {
        
    }
    
    public String sap(String nounA, String nounB) {
        
    }
    
    public static void main (String[] args) {
        WordNet test = new WordNet("test", "test");
        System.out.println(test);
    }
}
