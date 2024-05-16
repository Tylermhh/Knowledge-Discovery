package labs;

import DocumentClasses.ItemSet;
import DocumentClasses.Rule;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static labs.Lab5.*;

public class Lab6 {
    public static ArrayList<Rule> rules = new ArrayList<>();

    public static ArrayList<ItemSet> transactions = new ArrayList<>(); //lists of all itemsets
    public static ArrayList<Integer> items = new ArrayList<>(); //lists of all items
    public static HashMap<Integer, ArrayList<ItemSet>> frequentItemSets = new HashMap<>(); //lists frequent imtemsets
    public static HashMap<Integer, ArrayList<ItemSet>> attemptedItemSets = new HashMap<>(); //lists itemsets that failed to make the frequent set
    public static void main(String[] args){
        process("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\shopping_data.txt");
        findFrequentSingleItemSets();
        findFrequentItemSets(5);
        System.out.println(frequentItemSets);
//        ArrayList<Rule> firstGenRules = split(frequentItemSets.get(3).get(0));
        generateRules();
        System.out.println(rules);
    }

    // returns the first 1-item consequent rules
    public static ArrayList<Rule> gen1ItemConsequentRules(ItemSet itemSet){
        ArrayList<Rule> rules = new ArrayList<>();

        for (Integer item : itemSet.getItems()){
            ArrayList<Integer> right = new ArrayList<>();
            right.add(item);
            ItemSet rightSide = new ItemSet(right);      // get the righthand-side
            ItemSet leftSide = itemSet.makeNewSetWithout(right);        // get the lefthand-side of the ruleCandidate

            Rule newRule = new Rule(leftSide, rightSide);
            rules.add(newRule);
        }

        return rules;
    }

    // splits up an itemset into all possible rules (not filtered on confidence yet)
    public static ArrayList<Rule> split(ItemSet itemSet){
        if (itemSet.getSize() <= 1){
            return null;
        }
        HashMap<Integer, ArrayList<Rule>> rulesOrganized = new HashMap<>();
        ArrayList<Rule> allRulesForItemSet = gen1ItemConsequentRules(itemSet);      // start off the final list containint all the rules of different size consequents
        rulesOrganized.put(1, (ArrayList<Rule>) allRulesForItemSet.clone());        // generate the base rules and add to hashmap so we can keep referencing prev-sized consequent rules


        for (int k=2; k<itemSet.getSize(); k++){
            rulesOrganized.put(k, new ArrayList<>());       // initialize new arraylist for the rules of current size we are generating
            ArrayList<Rule> prevRules = rulesOrganized.get(k-1);        // get rules of previous iteration
            for (int i=0; i<prevRules.size()-1; i++){
                for (int j = i+1; j<prevRules.size(); j++){         // get every possible combination
                    Rule firstRule = prevRules.get(i);
                    Rule secondRule = prevRules.get(j);

                    //TODO: need to find a way to do the candidate gen part for rules

                    // try to combine the rules
                    ItemSet firstRightSide = firstRule.getRight();
                    ItemSet secondRightSide = secondRule.getRight();
                    ItemSet leftSide = firstRule.getLeft();

                    if (firstRightSide.equalExceptLast(secondRightSide)){
                        ArrayList<Integer> combinedItemsList = combineLists(firstRightSide, secondRightSide);       // combines the two lists
                        ItemSet newRight = new ItemSet(combinedItemsList);      // make the itemset on the right side of the combined rule

                        ItemSet newLeft = leftSide.makeNewSetWithout(combinedItemsList);    // make the itemset on the leftside by removing the items that got moved to the right
                        Rule newRule = new Rule(newLeft, newRight);     // make the new combined rule
                        rulesOrganized.get(k).add(newRule);     // add the new rule to the list of rules for that size/level. for the sake of the easily accessing the prev layer of rules
                        allRulesForItemSet.add(newRule);        // add to final list that will be returned
                    }
                }
            }
        }
        return allRulesForItemSet;
    }

    // generates all the rules
    public static void generateRules(){
        // loop through all the lists of different-sized freq itemsets
        for (int i=2; i<frequentItemSets.size()+1; i++){
            ArrayList<ItemSet> itemSetList = frequentItemSets.get(i);
            for (ItemSet itemSet : itemSetList){         // loop through all the itemsets of size n
                ArrayList<Rule> ruleCandidates = split(itemSet);        // get all possible rules from the itemset
                for (Rule candidate : ruleCandidates){      // loop through all the generated possible rules
                    if (isMinConfidenceMet(candidate)){         // if the rule satisfies the minimum confidence, add it to the final rules
                        rules.add(candidate);
                    }
                }
            }
        }

    }

    public static boolean isMinConfidenceMet(Rule r){
        double transContainingLeft = numTransactionsContaining(r.getLeft());
        double transContainingRule = numTransactionsContaining(r.getCombined());
        return transContainingRule/transContainingLeft > 0.99;
    }


    // Below are all lab5 functions
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
            frequentItemSets.put(i, new ArrayList<>());
            attemptedItemSets.put(i, new ArrayList<>());
            ArrayList<ItemSet> candidates = candidateGen(i);
//            System.out.println("ItemSets of size " + i);
//            for (ItemSet set : candidates){
//                System.out.print(set.getItems());
//            }
            for (ItemSet freqItemSetCandidate : candidates){
                if (!frequentItemSets.get(i).contains(freqItemSetCandidate) && !attemptedItemSets.get(i).contains(freqItemSetCandidate)){

                    // record attempt to add to frequentItemSets
                    attemptedItemSets.get(i).add(freqItemSetCandidate);

                    double support = numTransactionsContaining(freqItemSetCandidate) / transactions.size();

                    // if support is high enough add it to freqItemSets
                    if (isFrequent(freqItemSetCandidate)){
                        frequentItemSets.get(i).add(freqItemSetCandidate);
                    }
                }
            }

        }
//        Collections.sort(frequentItemSets.get(k));
        return true;
    }

    public static ArrayList<ItemSet> candidateGen(int size){
        ArrayList<ItemSet> prevItemSets = frequentItemSets.get(size - 1);
        ArrayList<ItemSet> candidates = new ArrayList<>();
        ArrayList<ItemSet> prunedCandidates = new ArrayList<>();        //shadow to the candidates list just in case removing while looping causes issues when pruning later


        for (int i=0; i<prevItemSets.size() - 1; i++){
            for (int j=i+1; j<prevItemSets.size(); j++){
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
    public static boolean isFrequent(ItemSet itemSet){
        double support = numTransactionsContaining(itemSet) / transactions.size();
        return support >= 0.01;
    }
}
