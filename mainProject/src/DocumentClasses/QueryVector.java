package DocumentClasses;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QueryVector extends TextVector{

    private final HashMap<String, Double> normalizedVector;

    public QueryVector(){
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

            double termFreq = 0.5 + (0.5 * rawFreq / maxFreq);

            // meaning of logNumber = m / dfi
            int numDocsWithWord = dc.numDocumentsContaining(word);

            if (numDocsWithWord == 0){
                this.normalizedVector.put(word, (double) numDocsWithWord);
                continue;
            }

            double logNumber = (double) dc.getSize() / numDocsWithWord;
            double normalizedFreq = termFreq * Math.log(logNumber) / Math.log(2);

            this.normalizedVector.put(word, normalizedFreq);
        }
    }

    @Override
    public double getNormalizedFrequency(String word) {
        return this.normalizedVector.get(word);
    }
}
