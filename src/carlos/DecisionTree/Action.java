package carlos.DecisionTree;

import java.util.Hashtable;

public class Action extends DecisionTreeNode {
    public Object action;

    public Action(Object action){
        super();
        this.action = action;
    }

    public Object getAction(){
        return action;
    }

    @Override
    public DecisionTreeNode makeDecision(Hashtable<String, Object> observations) {
        return this;
    }
}
