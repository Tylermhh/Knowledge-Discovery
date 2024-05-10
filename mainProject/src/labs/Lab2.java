package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.DocumentDistance;
import DocumentClasses.TextVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lab2 {
    public static DocumentCollection documents;
    public static DocumentCollection queries;
    public static HashMap<Integer, ArrayList<Integer>> cosineResults = new HashMap<>();


    public static void main (String[] args){
        documents = new DocumentCollection("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\documents.txt", "document");
        queries = new DocumentCollection("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\queries.txt", "true");

        documents.normalize(documents);
        queries.normalize(documents);

        CosineDistance cosineDistance = new CosineDistance();

        for (Map.Entry<Integer, TextVector> entry : queries.getEntrySet()){
            ArrayList<Integer> closestDocs = entry.getValue().findClosestDocuments(documents, cosineDistance);
            System.out.println("Query " + entry.getKey() + " closest documents:" + closestDocs.toString());
            cosineResults.put(entry.getKey(), closestDocs);
        }
//
//        ArrayList<Integer> closestDocs = queries.getDocumentById(1).findClosestDocuments(documents, cosineDistance);
//
//        System.out.println(closestDocs.toString());
    }

}
