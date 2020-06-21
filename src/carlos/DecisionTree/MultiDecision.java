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

    @Override
    void printNode(int level) {
        String tabs = "";
        for(int i = 0; i < level; i++){ tabs+= "  "; }
        System.out.println(tabs + "-test value: " + testValue);
        System.out.println(tabs + "+daughter nodes:");
        for(Object key : daughterNodes.keySet()){
            System.out.println(tabs + "  +" + key.toString() + ":");
            daughterNodes.get(key).printNode(level+2);
        }
    }
}
