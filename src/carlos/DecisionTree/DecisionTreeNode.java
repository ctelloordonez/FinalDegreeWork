package carlos.DecisionTree;

import carlos.helper.Example;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public abstract class DecisionTreeNode {
    public List<Example> examples;

    public DecisionTreeNode(){
        examples = new ArrayList<>();
    }

    abstract DecisionTreeNode makeDecision(Hashtable<String, Object> observations); // Recursively walkthrough the tree

    abstract void printNode(int tab);
}
