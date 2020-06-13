package carlos.DecisionTree;

import java.util.Hashtable;

public class MultiDecision extends DecisionTreeNode {
    public Hashtable<String, DecisionTreeNode> daughterNodes;
    public String testValue;

    public MultiDecision(){
        super();
        daughterNodes = new Hashtable<>();
        testValue = null;
    }

    public DecisionTreeNode getBranch(Hashtable<String, String> observation){
        return daughterNodes.get(observation.get(testValue));
    }

    @Override
    public DecisionTreeNode makeDecision(Hashtable<String, String> observation) {
        DecisionTreeNode branch = getBranch(observation);
        return branch.makeDecision(observation);
    }
}
