package labs;

import DocumentClasses.CosineDistance;
import DocumentClasses.DocumentCollection;
import DocumentClasses.OkapiDistance;
import DocumentClasses.TextVector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Lab3 {

    public static DocumentCollection documents;
    public static DocumentCollection queries;
    static HashMap<Integer, ArrayList<Integer>> okapiResults = new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> cosineResults = new HashMap<>();
    static HashMap<Integer, ArrayList<Integer>> humanJudgement;


    public static void main (String[] args){

        documents = new DocumentCollection("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\documents.txt", "document");
        queries = new DocumentCollection("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\queries.txt", "true");
        humanJudgement = parseHumanJudegement("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\human_judgement.txt");

        documents.normalize(documents);
        queries.normalize(documents);

        CosineDistance cosineDistance = new CosineDistance();
        OkapiDistance okapiDistance = new OkapiDistance();

        int count = 0;

        // calculate and store all relevant docs in rank from most to least relevant (top 20)
        for (Map.Entry<Integer, TextVector> entry : queries.getEntrySet()){
            if (count >= 20){
                break;
            }
            ArrayList<Integer> closestDocsCosine = entry.getValue().findClosestDocuments(documents, cosineDistance);
            ArrayList<Integer> closestDocsOkapi = entry.getValue().findClosestDocuments(documents, okapiDistance);
            System.out.println("Query " + entry.getKey() + " closest documents:" + closestDocsOkapi.toString());
            cosineResults.put(entry.getKey(), closestDocsCosine);
            okapiResults.put(entry.getKey(), closestDocsOkapi);
            count++;
        }

        System.out.println("Cosine MAP = " +
                computeMAP(humanJudgement, cosineResults));
        System.out.println("Okapi MAP = " +
                computeMAP(humanJudgement, okapiResults));

    }

    private static double computeMAP(HashMap<Integer, ArrayList<Integer>> humanJudgement, HashMap<Integer, ArrayList<Integer>> computerResults) {

        double MAP = 0;
        int count = 0;
        for (Map.Entry<Integer, ArrayList<Integer>> compEntry : computerResults.entrySet()){
            if (count >= 20){
                break;
            }
            Integer currQuery = compEntry.getKey();
            ArrayList<Integer> currHumanResults = humanJudgement.get(currQuery);
            double totalCurDocs = 0;
            double totalCurRelevant = 0;
            double totalPrecision = 0;
            for (Integer compDoc : compEntry.getValue()){
                totalCurDocs++;
                if (currHumanResults.contains(compDoc)){
                    totalCurRelevant++;
                    totalPrecision += totalCurRelevant / totalCurDocs;
                }

            }
            double queryMAP = totalPrecision / (double) currHumanResults.size();
            MAP += queryMAP;
            count++;
        }

        return MAP/20;
    }

    public static HashMap<Integer, ArrayList<Integer>> parseHumanJudegement(String filePath){
        HashMap<Integer, ArrayList<Integer>> humanJudgeParsed = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] splitLine = line.split(" ");
                int query = Integer.parseInt(splitLine[0]);
                int document = Integer.parseInt(splitLine[1]);
                int relevance = Integer.parseInt(splitLine[2]);

                if (relevance > 0 && relevance < 4){
                    if (humanJudgeParsed.containsKey(query)){  // if we already have the query initialized in humanjudgement
                        humanJudgeParsed.get(query).add(document);  // just add to the end of arraylist of relevant docs
                    } else {
                        ArrayList<Integer> newList = new ArrayList<>();  // initialize the list to add to hashmap
                        newList.add(document);   // add to list of relevant docs for query
                        humanJudgeParsed.put(query, newList);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return humanJudgeParsed;

    }



}
