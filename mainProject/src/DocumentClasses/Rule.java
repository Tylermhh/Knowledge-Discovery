package DocumentClasses;

import java.util.ArrayList;

public class Rule {
    private ItemSet left, right;

    public Rule(){
        this.left = new ItemSet();
        this.right = new ItemSet();
    }

    public Rule(ItemSet left, ItemSet right){
        this.left = left;
        this.right = right;
    }

    public ItemSet getLeft(){
        return this.left;
    }

    public ItemSet getRight(){
        return this.right;
    }

    // returns the combined itemset containing both left and right sides of rule
    public ItemSet getCombined(){
        ItemSet result = new ItemSet((ArrayList<Integer>) this.left.getItems().clone());
        for (Integer item : this.right.getItems()){
            result.getItems().add(item);
        }
        return result;
    }

    public String toString(){
        return this.left.toString() + "->" + this.right.toString();
    }

}
