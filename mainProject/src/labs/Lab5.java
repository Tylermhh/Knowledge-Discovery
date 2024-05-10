package labs;

import DocumentClasses.ItemSet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class Lab5 {
    public static ArrayList<ItemSet> transactions = new ArrayList<>(); //lists of all itemsets
    public static ArrayList<Integer> items = new ArrayList<>(); //lists of all items
    public static HashMap<Integer, ArrayList<ItemSet>> frequentItemSets = new HashMap<>(); //lists frequent imtemsets
    public static HashMap<Integer, ArrayList<ItemSet>> attemptedItemSets = new HashMap<>(); //lists itemsets that failed to make the frequent set

    public static void main(String[] args){
        process("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\shopping_data.txt");
        findFrequentSingleItemSets();
        for (ItemSet set : frequentItemSets.get(1)){
            System.out.print(set.getItems());
        }
        System.out.println();
        findFrequentItemSets(2);
//        System.out.println(frequentItemSets.get(1).size());
    }

    public static void process(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null)
            {
                String[] splitLine = line.split(", ");
                ItemSet transaction = new ItemSet();
                transaction.add_items_str(Arrays.copyOfRange(splitLine, 1, splitLine.length));
                transactions.add(transaction);

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean findFrequentItemSets(int k){
        if (k < 2){
            System.out.println("k must be greater than 1");
            return false;
        }
        findFrequentSingleItemSets();

        for (int i=2 ; i<=k; i++){
            ArrayList<ItemSet> candidates = candidateGen(i);
//            System.out.println("ItemSets of size " + i);
//            for (ItemSet set : candidates){
//                System.out.print(set.getItems());
//            }

        }
//        Collections.sort(frequentItemSets.get(k));
        return true;
    }

    public static ArrayList<ItemSet> candidateGen(int size){
        ArrayList<ItemSet> prevItemSets = frequentItemSets.get(size - 1);
        ArrayList<ItemSet> candidates = new ArrayList<>();
        ArrayList<ItemSet> prunedCandidates = new ArrayList<>();        //shadow to the candidates list just in case removing while looping causes issues when pruning later


        for (int i=0; i<prevItemSets.size() - 1; i++){
            for (int j=i; j<prevItemSets.size(); j++){
                ItemSet firstSet = prevItemSets.get(i);
                ItemSet secondSet = prevItemSets.get(j);

                if (firstSet.equalExceptLast(secondSet)){       // if the two Itemsets are equal except for the last elements
                    ArrayList<Integer> combinedItemsList = combineLists(firstSet, secondSet);       // combines the two lists
                    Collections.sort(combinedItemsList);        // sort the combined list just to be safe. should be inherently sorted though

                    // add it to the list of new candidates
                    candidates.add(new ItemSet(combinedItemsList));
                    prunedCandidates.add(new ItemSet(combinedItemsList));
                }
            }
        }

        for (ItemSet candidate : candidates){
            ArrayList<ArrayList<Integer>> sublists = generateSublists(candidate.getItems());        // generate all k-1 sublists of all the candidate itemsets

            for (ArrayList<Integer> singleSublist : sublists){          // loop through all sublists
                ItemSet subItemSet = new ItemSet(singleSublist);
                if (!frequentItemSets.get(size - 1).contains(subItemSet)){      // if sublist isnt in previous list of frequent itemsets, discard it
                    prunedCandidates.remove(candidate);
                }
            }
        }
        return prunedCandidates;        // return filtered candidates with none containing sublists that are not part of previous freq itemsets
    }

    // combines the given two lists assuming they are identical except for last elements
    private static ArrayList<Integer> combineLists(ItemSet firstSet, ItemSet secondSet) {
        ArrayList<Integer> firstList = firstSet.getItems();
        ArrayList<Integer> secondList = secondSet.getItems();
        ArrayList<Integer> combinedItemsList = new ArrayList<>();

        // get copy the first k-1 elemets of the two Itemsets
        for (int i=0; i<firstList.size()-1; i++){
            combinedItemsList.add(firstList.get(i));
        }
//        ArrayList<Integer> combinedItemsList = (ArrayList<Integer>) firstList.subList(0, firstList.size() - 1);

        // append both of the last elements of both sets
        combinedItemsList.add(firstList.get(firstList.size() - 1));
        combinedItemsList.add(secondList.get(firstList.size() - 1));
        return combinedItemsList;
    }

    public static boolean isFrequent(ItemSet itemSet){
        double support = numTransactionsContaining(itemSet) / transactions.size();
        return support >= 0.01;
    }

    public static void findFrequentSingleItemSets(){
        frequentItemSets.put(1, new ArrayList<ItemSet>());
        attemptedItemSets.put(1, new ArrayList<ItemSet>());
        //loop through all transactions
        for (ItemSet transaction : transactions){
            ArrayList<Integer> items = transaction.getItems();

            // loop through all items in each transaction
            for (Integer item : items){
                ItemSet freqItemSetCandidate = new ItemSet();
                freqItemSetCandidate.add_items_int(item);
                // only do stuff if it isn't already in the frequent itemsets, and hasnt previously failed to be frequent
                if (!frequentItemSets.get(1).contains(freqItemSetCandidate) && !attemptedItemSets.get(1).contains(freqItemSetCandidate)){

                    // record attempt to add to frequentItemSets
                    attemptedItemSets.get(1).add(freqItemSetCandidate);

                    double support = numTransactionsContaining(freqItemSetCandidate) / transactions.size();

                    // if support is high enough add it to freqItemSets
                    if (isFrequent(freqItemSetCandidate)){
                        frequentItemSets.get(1).add(freqItemSetCandidate);
                    }
                }
            }
        }

        Collections.sort(frequentItemSets.get(1));

    }

    public static double numTransactionsContaining(ItemSet target){
        double count = 0;

        for (ItemSet transaction : transactions){
            if (transaction.contains(target)){
                count++;
            }
        }

        return count;
    }


    public static ArrayList<ArrayList<Integer>> generateSublists(ArrayList<Integer> array) {
        ArrayList<ArrayList<Integer>> sublists = new ArrayList<>();

        // Iterate through each element in the array
        for (int i = 0; i < array.size(); i++) {
            ArrayList<Integer> sublist = new ArrayList<>();

            // Add all elements except the current one to the sublist
            for (int j = 0; j < array.size(); j++) {
                if (j != i) {
                    sublist.add(array.get(j));
                }
            }

            // Add the sublist to the list of sublists
            sublists.add(sublist);
        }

        return sublists;
    }

}
