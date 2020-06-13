package carlos;

import carlos.DecisionTree.Action;
import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.DecisionTreeStructure;
import carlos.DecisionTree.MultiDecision;

import java.util.Hashtable;

public class DecisionTreeMF {
    public static void main(String[] args) {
        MultiDecision root = new MultiDecision();
        root.testValue = "Ammo";

        Action action1 = new Action("Defend");
        root.daughterNodes.put("Empty", action1);

        MultiDecision decision1 = new MultiDecision();
        root.daughterNodes.put("With Ammo", decision1);
        decision1.testValue = "Cover";

        Action action2 = new Action("Defend");
        Action action3 = new Action("Attack");

        decision1.daughterNodes.put("Exposed", action2);
        decision1.daughterNodes.put("In Cover", action3);

        DecisionTree decisionTree = new DecisionTreeStructure(root);

        Hashtable<String, String> observation = new Hashtable<>();
        observation.put("Ammo", "Empty");
        observation.put("Cover", "In Cover");
        observation.put("Health", "Healthy");

        System.out.println("With attributes:");

        for(String attribute : observation.keySet()){
            System.out.println(attribute + ": " + observation.get(attribute));
        }

        Action a = (Action) decisionTree.makeDecision(observation);
        System.out.println("\nAction is: "+ a.getAction());
    }
}
