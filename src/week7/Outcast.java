
import java.util.Iterator;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author johntan
 */
public class Outcast {
    private WordNet wordNet;
    public Outcast(WordNet wordnet){
        wordNet = wordnet;
    }
    
    public String outcast(String[] nouns) throws NullPointerException{
        if(nouns == null){
            throw new NullPointerException();
        }
        String maxNoun = "";
        int maxSum = Integer.MIN_VALUE;
        for (String noun : nouns) {
            int sum = 0;
            for (String nounTwo : nouns) {
                sum += wordNet.distance(noun, nounTwo);
            }
            if(sum > maxSum){
                maxSum = sum;
                maxNoun = noun;
            }
        }
        return maxNoun;
    }
    
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("src/week7/wordnet/synsets.txt", "src/week7/wordnet/hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        In in = new In("src/week7/wordnet/outcast8.txt");
        String[] nouns = in.readAllStrings();
        StdOut.println("src/week7/wordnet/outcast8.txt" + ": " + outcast.outcast(nouns));
    }
}
