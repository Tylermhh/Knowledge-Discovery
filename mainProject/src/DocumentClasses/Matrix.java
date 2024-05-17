package DocumentClasses;

import java.lang.reflect.Array;
import java.util.*;

public class Matrix {
    private ArrayList<ArrayList<Integer>> matrix;
    public Matrix(){
        this.matrix = new ArrayList<>();
    }

    public Matrix(ArrayList<ArrayList<Integer>> data){
        this.matrix = data;
    }

    private int findFrequency(int attribute, int value, ArrayList<Integer> rows){
        return 0;
    }

    private HashSet<Integer> findDifferentValues(int attribute, ArrayList<Integer> rows){
        HashSet<Integer> result = new HashSet<>();
        return result;
    }

    private ArrayList<Integer> findRows(int attribute, int value, ArrayList<Integer> rows){
        ArrayList<Integer> resultRows = new ArrayList<>();

        return resultRows;
    }

    private double log2(double number){
        return Math.log(number) / Math.log(2);
    }

    // calculating entropy for one of the options when we split on an attribute
    private double findEntropy(ArrayList<Integer> rows){
        double entropy = 0;
        int total_count = 0;
        HashMap<Integer, Integer> classesCount = new HashMap<>();
        for (Integer rowNum : rows){
            ArrayList<Integer> row = this.matrix.get(rowNum);       // go through each of the specified rows
            // row.get(4) is the class (1,2,3) in our case. but we use a hashmap so we can support diff num of classes
            classesCount.put(row.get(4), classesCount.getOrDefault(row.get(4), 0) + 1);     // increment the counter for items in the class that the row belongs to
            total_count++;
        }

        // go through all identified classes and calculate total entropy
        for (Map.Entry<Integer, Integer> entry : classesCount.entrySet()){
            double ratio = (double) classesCount.get(entry.getKey()) / total_count;
            entropy += ratio * log2(ratio);
        }

        return entropy; // TODO: convert neg to pos
    }

    private double findEntropy(int attribute, ArrayList<Integer> rows){
        double totalEntropy = 0;
        HashMap<Integer, ArrayList<Integer>> rowsForEachAttributeVal = split(attribute, rows);     // stores the row numbers per attribute category

        for (Map.Entry<Integer, ArrayList<Integer>> entry : rowsForEachAttributeVal.entrySet()){
            double entriesInCateogory = entry.getValue().size();        // number of elements in the node after the split
            ArrayList<Integer> rowsForCategory = entry.getValue();
            totalEntropy += (entriesInCateogory / rows.size()) * findEntropy(rowsForCategory);
        }

        return totalEntropy;    // TODO: convert neg to pos?
    }

    private double findGain(int attribute, ArrayList<Integer> rows){
        double gain = 0;

        double originalEntropy = findEntropy(rows);
        double splitEntropy = findEntropy(attribute, rows);

        return originalEntropy - splitEntropy;
    }

    public double computeIGR(int attribute, ArrayList<Integer> rows){
        double gainRatio = 0;
        double gain = findGain(attribute, rows);
        HashMap<Integer, ArrayList<Integer>> rowsForEachAttributeVal = split(attribute, rows);     // stores the row numbers per attribute category
        double denominator = 0;

        for (Map.Entry<Integer, ArrayList<Integer>> entry : rowsForEachAttributeVal.entrySet()){
            double entriesInCateogory = entry.getValue().size();        // number of elements in the node after the split
            denominator += (entriesInCateogory / rows.size()) * log2(entriesInCateogory / rows.size());
        }

        return gainRatio / denominator;
    }

    public int findMostCommonValue(ArrayList<Integer> rows){
        HashMap<Integer, Integer> valuesCounts = new HashMap<>();
        for (int i=0; i<this.matrix.size(); i++){
            if (rows.contains(i)){      // to ignore the rows not specified in parameter
                ArrayList<Integer> curRow = this.matrix.get(i);     // get the row
                for (Integer value : curRow){       // loop through values in that row
                    if (valuesCounts.containsKey(value)){
                        valuesCounts.put(value, valuesCounts.get(value) + 1);       // increment count of that value
                    } else{
                        valuesCounts.put(value, 1);     // add value into hashmap to keep count
                    }
                }
            }
        }

        return Collections.max(valuesCounts.entrySet(), Map.Entry.comparingByValue()).getKey(); // return the max counted key

    }

    public HashMap<Integer, ArrayList<Integer>> split(int attribute, ArrayList<Integer> rows){
        HashMap<Integer, ArrayList<Integer>> rowsForEachAttributeVal = new HashMap<>();

        for (Integer rowNum : rows){
            ArrayList<Integer> row = this.matrix.get(rowNum);
            int attributeCategory = row.get(attribute);         // get the cateogory that the row gets put under if split on given attribute
            if (rowsForEachAttributeVal.containsKey(attributeCategory)){        // if this attribute val is already seen before, just add the row to list of related rows
                rowsForEachAttributeVal.get(attributeCategory).add(rowNum);
            } else {        // if it is a new attribute value/category, make a new arraylist for it
                ArrayList<Integer> rowsForAttribute = new ArrayList<>();
                rowsForAttribute.add(rowNum);
                rowsForEachAttributeVal.put(attributeCategory, rowsForAttribute);
            }
        }
        return rowsForEachAttributeVal;
    }

    public String toString(){
        return this.matrix.toString();
    }
}
