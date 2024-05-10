package DocumentClasses;

import java.util.Map;

public class OkapiDistance implements DocumentDistance{

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        double dist = 0;

        for (Map.Entry<String, Integer> queryEntry : query.getRawVector().entrySet()) {
            String word = queryEntry.getKey();


            // if word exists in both doc and query compute dist and add
            if (document.contains(word)) {
                int docCount = document.getRawFrequency(word);
                double numDocsContainingTerm = documents.numDocumentsContaining(word);
                double numerator = documents.getSize() - numDocsContainingTerm + 0.5;
                double denominator = numDocsContainingTerm + 0.5;
                double logNumber = numerator / denominator;

                double firstTerm = Math.log(logNumber);

                double secondTerm = (1.2 + 1) * (double)docCount / (1.2 * (1 - 0.75 + 0.75 * (double)document.getTotalWordCount() / documents.getAverageDocumentLength()) + document.getRawFrequency(word));

                double thirdTerm = (double) ((100 + 1) * queryEntry.getValue()) / (double) (100 + queryEntry.getValue());

                dist += firstTerm * secondTerm * thirdTerm;
            }
        }

        return dist;
    }
}
