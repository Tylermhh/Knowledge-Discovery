package DocumentClasses;

import java.io.*;
import java.util.*;

public class DocumentCollection implements Serializable {
    HashMap<Integer, TextVector> documents;

    public static  String noiseWordArray[] = {"a", "about", "above", "all", "along",
            "also", "although", "am", "an", "and", "any", "are", "aren't", "as", "at",
            "be", "because", "been", "but", "by", "can", "cannot", "could", "couldn't",
            "did", "didn't", "do", "does", "doesn't", "e.g.", "either", "etc", "etc.",
            "even", "ever", "enough", "for", "from", "further", "get", "gets", "got", "had", "have",
            "hardly", "has", "hasn't", "having", "he", "hence", "her", "here",
            "hereby", "herein", "hereof", "hereon", "hereto", "herewith", "him",
            "his", "how", "however", "i", "i.e.", "if", "in", "into", "it", "it's", "its",
            "me", "more", "most", "mr", "my", "near", "nor", "now", "no", "not", "or", "on", "of", "onto",
            "other", "our", "out", "over", "really", "said", "same", "she",
            "should", "shouldn't", "since", "so", "some", "such",
            "than", "that", "the", "their", "them", "then", "there", "thereby",
            "therefore", "therefrom", "therein", "thereof", "thereon", "thereto",
            "therewith", "these", "they", "this", "those", "through", "thus", "to",
            "too", "under", "until", "unto", "upon", "us", "very", "was", "wasn't",
            "we", "were", "what", "when", "where", "whereby", "wherein", "whether",
            "which", "while", "who", "whom", "whose", "why", "with", "without",
            "would", "you", "your", "yours", "yes"};
    public DocumentCollection (String infile, String doc_or_query){

        this.documents = new HashMap<Integer, TextVector>();

        int query_counter = 1;

        try (BufferedReader br = new BufferedReader(new FileReader(infile))) {
            String line;
            int key = -1;
            while ((line = br.readLine()) != null)
            {
                // just for the very first line
                if (line.startsWith(".I"))
                {
                    String[] splitLine = line.split(" ");
                    if (doc_or_query.equals("document")){
                        key = Integer.parseInt(splitLine[1]);
                        this.documents.put(key, new DocumentVector());
                    }
                    else {
                        key = query_counter;
                        query_counter++;
                        this.documents.put(key, new QueryVector());
                    }
                }
                if (line.startsWith(".W"))
                {
                    int new_key = -1;
                    String body = "";
                    String bodyLine;
                    while ((bodyLine = br.readLine()) != null)
                    {
                        if (bodyLine.startsWith(".I"))
                        {
                            String[] splitLine = bodyLine.split(" ");
                            if (doc_or_query.equals("document")){
                                new_key = Integer.parseInt(splitLine[1]);
                                this.documents.put(new_key, new DocumentVector());
                            }
                            else {
                                new_key = query_counter;
                                query_counter++;
                                this.documents.put(new_key, new QueryVector());
                            }
                            break;
                        }
                        body = body.concat(" " + bodyLine);
                    }
                    String[] parsed = body.toLowerCase().split("[^a-zA-Z]+");
//                    System.out.println(parsed);
                    addWordsToVector(this.documents.get(key), parsed);
                    key = new_key;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TextVector getDocumentById(int id){
        return this.documents.get(id);
    }

    public double getAverageDocumentLength(){
        int totalWordsLength = 0;
        for (Map.Entry<Integer, TextVector> entry : this.documents.entrySet()){
            totalWordsLength += entry.getValue().getTotalWordCount();
        }
        return (double) totalWordsLength /this.documents.size();
    }

    public int getSize(){
        return this.documents.size();
    }

    public Collection<TextVector> getDocuments(){
        return this.documents.values();
    }

    public Set<Map.Entry<Integer, TextVector>> getEntrySet(){
        return this.documents.entrySet();
    }

    public int getDocumentFrequency(String word){
        int documentFrequency = 0;
        for (Map.Entry<Integer, TextVector> entry : this.documents.entrySet()){
            if (entry.getValue().contains(word)){
                documentFrequency++;
            }
        }
        return documentFrequency;
    }

    private boolean isNoiseWord(String word){
        if (Arrays.asList(noiseWordArray).contains(word)){
            return true;
        }
        return false;
    }

    public void addWordsToVector(TextVector rawVector, String[] parsed){

        for (String word : parsed){
            //if word is noise word dont add
            if (isNoiseWord(word) || word.length() < 2){
                continue;
            }
            rawVector.add(word);
        }
    }

    public Map.Entry<String, Integer> highestSingleDocFreq (){

        Map.Entry<String, Integer> max = new AbstractMap.SimpleEntry<>("dummy", 0);

        for (Map.Entry<Integer, TextVector> document : this.documents.entrySet()){
            Map.Entry<String, Integer> documentMax = document.getValue().getMostFrequentWordAndFreq();
            if (documentMax.getValue() > max.getValue()){
                max = documentMax;
            }
        }

        return max;
    }

    public int getDistinctNumWords(){
        int totalDistinctWords = 0;
        for (TextVector document : this.documents.values()){
            totalDistinctWords += document.getDistinctWordCount();
        }
        return totalDistinctWords;
    }

    public int getTotalWords(){
        int total = 0;
        for (Map.Entry<Integer, TextVector> document : this.documents.entrySet()){
            total += document.getValue().getTotalWordCount();
        }

        return total;
    }

    public int numDocumentsContaining(String word){
        int counter = 0;

        for (Map.Entry<Integer, TextVector> document : this.documents.entrySet()){
            if (document.getValue().contains(word)){
                counter++;
            }
        }
        return counter;
    }

    public void normalize(DocumentCollection dc){
        for (Map.Entry<Integer, TextVector> document : this.documents.entrySet()){
            document.getValue().normalize(dc);
        }
    }

}
