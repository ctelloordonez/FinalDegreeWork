package carlos.DecisionTree;

import java.util.Hashtable;

public class Action extends DecisionTreeNode {
    public String action;

    public Action(String action){
        super();
        this.action = action;
    }

    public String getAction(){
        return action;
    }

    @Override
    public DecisionTreeNode makeDecision(Hashtable<String, String> observations) {
        return this;
    }
}
