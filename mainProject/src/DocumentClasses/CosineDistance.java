package DocumentClasses;

import java.util.Map;

public class CosineDistance implements DocumentDistance{

    public CosineDistance(){}

    @Override
    public double findDistance(TextVector query, TextVector document, DocumentCollection documents) {
        double distance;
        double dot_product = 0;

        double d = document.getL2Norm();
        double q = query.getL2Norm();

        if (d==0 || q==0){
            return 0;
        }

        for (Map.Entry<String, Double> queryEntry : query.getNormalizedVectorEntrySet()) {
            for (Map.Entry<String, Double> docEntry : document.getNormalizedVectorEntrySet()) {
                // to get the matching words to dot product their tf-idfs
                if (queryEntry.getKey().equals(docEntry.getKey())){
                    dot_product += queryEntry.getValue() * docEntry.getValue();
                }
            }
        }

        distance = dot_product/(d * q);
        return distance;
    }
}
