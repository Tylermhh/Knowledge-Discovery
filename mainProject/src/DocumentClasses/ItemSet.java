package DocumentClasses;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import static java.lang.System.exit;

public class ItemSet implements Comparable {
    private ArrayList<Integer> items;

    public ItemSet(){
        this.items = new ArrayList<>();
    }
    public ItemSet(ArrayList<Integer> initialList){
        this.items = initialList;
    }

    public void add_items_str(String[] items){
        for (String item : items){
            this.items.add(Integer.valueOf(item));
        }
    }

    public void add_items_int(Integer item){
        this.items.add(item);
    }

    public ArrayList<Integer> getItems(){
        return this.items;
    }

    public boolean equals(ItemSet obj) {
        if (obj.items.size() != this.items.size()){
            return false;
        }

        for (Integer item : this.items){
            if (!obj.items.contains(item)){
                return false;
            }
        }

        return true;
    }

    // returns true if the itemsets are identical
    @Override
    public boolean equals(Object obj) {
        ItemSet itemSet = obj instanceof ItemSet ? ((ItemSet) obj) : null;
        if (itemSet == null){
            return false;
        }

        if (itemSet.items.size() != this.items.size()){
            return false;
        }

        for (Integer item : this.items){
            if (!itemSet.items.contains(item)){
                return false;
            }
        }

        return true;
    }

    // check if an itemset is part of another itemset
    // eg if we do a.contains(b) where a=[0, 1, 2] and b=[0, 2] it should return true
    public boolean contains(ItemSet target){
        if (target.items.size() > this.items.size()){
            return false;
        }
        for (Integer targetItem : target.items){
            if (!this.items.contains(targetItem)){
                return false;
            }
        }
        return true;
    }

    // returns true if the two itemsets are identical except for the last elements
    public boolean equalExceptLast (ItemSet target){

        // if not same size, just false
        if (target.items.size() > this.items.size()){
            return false;
        }

        for (int i=0; i<this.items.size() - 1; i++){
            int first = this.items.get(i);
            int second = target.items.get(i);
            if (first != second){
                return false;
            }
        }

        return true;
    }

    public int getSize(){
        return this.items.size();
    }

    @Override
    public int compareTo(Object obj) {

        ItemSet parameterObj = obj instanceof ItemSet ? ((ItemSet) obj) : null;

        for (int i=0; i<this.items.size(); i++){
            // if we encounter a difference in itemsets return appropriate
            // 1 if current obj is greater than parameterObj, -1 otherwise
            if (this.items.get(i) > parameterObj.items.get(i)){
                return 1;
            }
            else if (this.items.get(i) < parameterObj.items.get(i)){
                return -1;
            }
        }
        // if there were no differences in the itemsets, they are equal so return 0
        return 0;

    }

    public ItemSet makeNewSetWithout(ArrayList<Integer> exclusions){
        int count = 0;
        ItemSet newSet = new ItemSet();
        for (Integer item : this.items){
            if (!exclusions.contains(item)){    // if item is not part of the exclusion list
                newSet.items.add(item);     // add the item to the newSet to be returned
            } else {
                count++;    // just to check if we are trying to exclude something that isnt even in there
            }
        }
//        if (count > 0){
//            System.out.println("tried to exclude something that isnt in there");
//            exit(1);
//        }
        return newSet;
    }


    public String toString() {
        return this.items.toString();
    }

    public boolean supportsRule(Rule r){


        return true;
    }
}
