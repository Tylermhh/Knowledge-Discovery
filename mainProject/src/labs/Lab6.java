package labs;

import DocumentClasses.ItemSet;
import DocumentClasses.Rule;

import java.util.ArrayList;
import java.util.HashMap;

import static labs.Lab5.*;

public class Lab6 {
    public static ArrayList<Rule> rules;
    public static HashMap<Integer, ArrayList<Rule>> rulesOrganized = new HashMap<>();

    public static ArrayList<ItemSet> transactions = new ArrayList<>(); //lists of all itemsets
    public static ArrayList<Integer> items = new ArrayList<>(); //lists of all items
    public static HashMap<Integer, ArrayList<ItemSet>> frequentItemSets = new HashMap<>(); //lists frequent imtemsets
    public static HashMap<Integer, ArrayList<ItemSet>> attemptedItemSets = new HashMap<>(); //lists itemsets that failed to make the frequent set
    public static void main(String[] args){
        process("D:\\College\\Com Sci\\Junior Year\\466\\Labs\\mainProject\\src\\InputFiles\\shopping_data.txt");
        findFrequentSingleItemSets();
        findFrequentItemSets(5);

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
        rulesOrganized.put(1, gen1ItemConsequentRules(itemSet));        //generate the base rules and add to hashmap


        for (int k=2; k<itemSet.getSize(); k++){
            ArrayList<Rule> prevRules = rulesOrganized.get(k-1);        // get rules of previous iteration
            for (int i=0; i<prevRules.size()-1; i++){
                for (int j = i+1; j<prevRules.size(); j++){
                    Rule firstRule = prevRules.get(i);      // get every possible combination
                    Rule secondRule = prevRules.get(j);

                    //TODO: need to find a way to do the candidate gen part for rules
//                    ArrayList<ItemSet> combinedRightRules = ;
                }
            }
        }
        return rules;
    }

    // generates all the rules
    public static void generateRules(){

    }

    public static boolean isMinConfidenceMet(Rule r){
        return true;
    }
}
