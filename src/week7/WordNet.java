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
    private SAP sapM;
    
    public WordNet (String synsets, String hypernyms) throws NullPointerException, IllegalArgumentException {
        if(synsets == null || hypernyms == null){
            throw new NullPointerException();
        }
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
        int[] roots = new int[countV];
        for(int i=0;i<roots.length;i++){
            roots[i] = 0;
        }
        while (scanHyper.hasNextLine()) {
            String current = scanHyper.readLine();
            String[] hypers = current.split(",");
//            Add associated hypernyms
            if(hypers.length > 1){
                roots[Integer.parseInt(hypers[0])] ++;
            }
            for(int i=1;i<hypers.length;i++){
                graph.addEdge(Integer.parseInt(hypers[0]), Integer.parseInt(hypers[i]));
            }
        }
        DirectedCycle cycleCheck = new DirectedCycle(graph);
        if (cycleCheck.hasCycle()) {
            throw new IllegalArgumentException();
        }
        int numRoots = 0;
        for(int i=0;i<roots.length;i++){
            if(roots[i] == 0){
                numRoots ++;
            }
        }
        if(numRoots > 1){
            throw new IllegalArgumentException();
        }
        sapM = new SAP(graph);
    }
    
    public Iterable<String> nouns() {
        Collection c = nounIndex.values();
        return (Iterable<String>) c.iterator();
    }
    
    public boolean isNoun(String word) throws NullPointerException {
        if(word == null){
            throw new NullPointerException();
        }
        return nounIndex.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) throws NullPointerException, IllegalArgumentException{
        if(nounA == null || nounB == null){
            throw new NullPointerException();
        }
        if(!isNoun(nounA) || !isNoun(nounB)){
            throw new IllegalArgumentException();
        }
        return sapM.length(nounIndex.get(nounA), nounIndex.get(nounB));
    }
    
    public String sap(String nounA, String nounB) throws NullPointerException, IllegalArgumentException{
        if (nounA == null || nounB == null) {
            throw new NullPointerException();
        }
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        int ind = sapM.ancestor(nounIndex.get(nounA), nounIndex.get(nounB));
        return synSets.get(ind);
    }
    
    public static void main (String[] args) {
//        WordNet test = new WordNet("src/week7/wordnet/synsets.txt", "src/week7/wordnet/hypernyms.txt");
//        System.out.println(test.distance("white_marlin", "mileage"));
//        System.out.println(test.distance("Black_Plague", "black_marlin"));
//        System.out.println(test.distance("American_water_spaniel", "histology"));
//        System.out.println(test.sap("worm", "bird"));
//        System.out.println(test.distance("worm", "bird"));
//        System.out.println(test.sap("municipality", "region"));
//        System.out.println(test.distance("mebibit", "Ascension"));
//        WordNet testTwo = new WordNet("src/week7/wordnet/synsets3.txt", "src/week7/wordnet/hypernyms3InvalidCycle.txt");
    }
}
