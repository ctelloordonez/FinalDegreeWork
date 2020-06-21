package carlos.DecisionTree;

import carlos.helper.Example;

import java.util.List;

public class DecisionTreeBasic extends DecisionTree {
    public DecisionTreeBasic(DecisionTreeNode root) {
        super(root);
    }

    @Override
    protected DecisionTreeNode makeTree(List<Example> examples, List<String> attributes, DecisionTreeNode decisionNode) {
        return decisionNode;
    }
}
