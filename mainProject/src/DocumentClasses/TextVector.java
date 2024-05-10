package DocumentClasses;

import java.util.*;

public abstract class TextVector {
    HashMap<String, Integer> rawVector;

    public TextVector(){
        this.rawVector = new HashMap<String, Integer>();
    }

    public HashMap<String, Integer> getRawVector(){
        return this.rawVector;
    }


    private Set<Map.Entry<String, Integer>> getRawVectorEntrySet(){
        return this.rawVector.entrySet();
    }

    public void add(String word){
        this.rawVector.put(word, 1 + this.rawVector.getOrDefault(word, 0));
    }


    public boolean contains(String word){
        return rawVector.get(word) != null;
    }

    public int getRawFrequency(String word){
        return this.rawVector.getOrDefault(word, -1);
    }

    //todo
    public int getTotalWordCount(){
        int wordCount = 0;
        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            wordCount += entry.getValue();
        }
        return wordCount;
    }

    //todo
    public int getDistinctWordCount(){
        return this.rawVector.size();
    }

    public int getHighestRawFrequency(){
        Map.Entry<String, Integer> max = this.rawVector.entrySet().iterator().next();
        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            if (entry.getValue() > max.getValue()){
                max = entry;
            }
        }

        return max.getValue();
    }

    public String getMostFrequentWord() {

        //get the first entry in hashmap to set as default max
        Map.Entry<String, Integer> max = this.rawVector.entrySet().iterator().next();
        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            if (entry.getValue() > max.getValue()){
                max = entry;
            }
        }

        return max.getKey();
    }

    public Map.Entry<String, Integer> getMostFrequentWordAndFreq(){
        //get the first entry in hashmap to set as default max
        Map.Entry<String, Integer> max = new AbstractMap.SimpleEntry<>("dummy", 0);
        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            if (entry.getValue() > max.getValue()){
                max = entry;
            }
        }

        return max;
    }

    //todo: ask what getL2Norm should do
    public double getL2Norm(){
        double normFreqSum = 0;

        for (Map.Entry<String, Double> entry : this.getNormalizedVectorEntrySet()) {
            normFreqSum += Math.pow(entry.getValue(), 2);
        }

        return Math.sqrt(normFreqSum);
    }

    public ArrayList<Integer> findClosestDocuments(DocumentCollection documents, DocumentDistance distanceAlg){
        HashMap<Integer, Double> distancesMap = new HashMap<>();
        ArrayList<Double> distancesList = new ArrayList<>();
        ArrayList<Integer> topResults = new ArrayList<>();
        int counter = 0;

        for (Map.Entry<Integer, TextVector> document : documents.getEntrySet()){
            double dist = distanceAlg.findDistance(this, document.getValue(), documents);
            distancesMap.put(document.getKey(), dist);
            distancesList.add(dist);
        }


        Collections.sort(distancesList, Collections.reverseOrder());
//        System.out.println("all distances: " + distancesList);

        List<Double> topDists = distancesList.subList(0, 20);

//        System.out.println("top distances: " + topDists);

        for (Double num : distancesList) {
            for (Map.Entry<Integer, Double> entry : distancesMap.entrySet()) {
                if (entry.getValue().equals(num)) {
                    topResults.add(entry.getKey());
                    counter++;
                    if (counter >= 20){
                        return topResults;
                    }
                }
            }
        }
//        System.out.println("size: " + topResults.size());
        return topResults;
    }

    // returns the normalized frequency for each word
    public abstract Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet();

    // normalize using TF-IDF formula
    public abstract void normalize(DocumentCollection dc);

    // returns normalized frequency of the word
    public abstract double getNormalizedFrequency(String word);



}
