package DocumentClasses;

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


}
