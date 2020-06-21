package carlos.DecisionTree;

import carlos.helper.Example;

import java.util.Hashtable;
import java.util.List;

public abstract class DecisionTree {
    protected DecisionTreeNode root;

    public DecisionTree(DecisionTreeNode root){
        this.root = root;
    }

    public DecisionTreeNode makeDecision(Hashtable<String, Object> observation){
        return root.makeDecision(observation);
    }

    public void MakeTree(List<Example> examples, List<String> attributes){
        root = makeTree(examples, attributes, root);
    }

    public void PrintTree(){
        System.out.println("+root:");
        root.printNode(1);
        System.out.println();
    }

    protected abstract DecisionTreeNode makeTree(List<Example> examples, List<String> attributes, DecisionTreeNode decisionNode);
}

