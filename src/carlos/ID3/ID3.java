package carlos.ID3;

import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.DecisionTreeNode;
import carlos.helper.Example;

import java.util.List;

import static carlos.Utils.InductionOfDecisionTrees.inductTree;

public class ID3 extends DecisionTree {

    static String healthy = "Healthy";
    static String hurt = "Hurt";
    static String inCover = "In Cover";
    static String exposed = "Exposed";
    static String withAmmo = "With Ammo";
    static String empty = "Empty";

    public ID3(DecisionTreeNode root) {
        super(root);
    }

    @Override
    protected void makeTree(List<Example> examples, List<String> attributes, DecisionTreeNode decisionNode){
        inductTree(examples, attributes, decisionNode);
    }
}