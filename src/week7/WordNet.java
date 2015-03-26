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
        sapM = new SAP(graph);
    }
    
    public Iterable<String> nouns() {
        Collection c = nounIndex.values();
        return (Iterable<String>) c.iterator();
    }
    
    public boolean isNoun(String word) {
        return nounIndex.containsKey(word);
    }
    
    public int distance(String nounA, String nounB) {
        return sapM.length(nounIndex.get(nounA), nounIndex.get(nounB));
    }
    
    public String sap(String nounA, String nounB) {
        return synSets.get(sapM.ancestor(nounIndex.get(nounA), nounIndex.get(nounB)));
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
