package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DocumentVector extends TextVector{

    private final HashMap<String, Double> normalizedVector;

    public DocumentVector(){
        this.normalizedVector = new HashMap<String, Double>();
    }
    @Override
    public Set<Map.Entry<String, Double>> getNormalizedVectorEntrySet() {
        return this.normalizedVector.entrySet();
    }

    @Override
    public void normalize(DocumentCollection dc) {

        for (Map.Entry<String, Integer> entry : this.rawVector.entrySet()) {
            String word = entry.getKey();

            double rawFreq = this.getRawFrequency(word);
            double maxFreq = this.getHighestRawFrequency();

            // get the term frequency and idf
            double termFreq = rawFreq / maxFreq;

            // logNumber = m / dfi
            double logNumber = (double) dc.getSize() / dc.numDocumentsContaining(word);
            double normalizedFreq = termFreq * Math.log(logNumber) / Math.log(2);
            this.normalizedVector.put(word, normalizedFreq);
        }

    }

    @Override
    public double getNormalizedFrequency(String word) {
        return this.normalizedVector.get(word);
    }
}
