package labs;

import DocumentClasses.DocumentCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

public class Lab1 {
    public static void main(String[] args) {
        DocumentCollection docs = new DocumentCollection("D:\\College\\Com Sci\\Junior Year\\CSC 466\\Labs\\mainProject\\src\\InputFiles\\documents.txt", "document");

        Map.Entry<String, Integer> max = docs.highestSingleDocFreq();

        System.out.println("Word: " + max.getKey());
        System.out.println("Frequency: " + max.getValue());
        System.out.println("Distinct Number of Words: " + docs.getDistinctNumWords());
        System.out.println("Total Word Count: " + docs.getTotalWords());

        try(ObjectOutputStream os = new ObjectOutputStream(new
                FileOutputStream(new File("./files/docvector")))){
            os.writeObject(docs);
        } catch(Exception e){
            System.out.println(e);
        }

    }
}
