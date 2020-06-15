package carlos.DecisionTree;

import java.util.Hashtable;

public class MultiDecision extends DecisionTreeNode {
    public Hashtable<Object, DecisionTreeNode> daughterNodes;
    public String testValue;

    public MultiDecision(){
        super();
        daughterNodes = new Hashtable<>();
        testValue = null;
    }

    public DecisionTreeNode getBranch(Hashtable<String, Object> observation){
        return daughterNodes.get(observation.get(testValue));
    }

    @Override
    public DecisionTreeNode makeDecision(Hashtable<String, Object> observation) {
        DecisionTreeNode branch = getBranch(observation);
        if(branch != null)
            return branch.makeDecision(observation);
        else
            return null;
    }
}
