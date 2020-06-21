package carlos.ID3;

import carlos.DecisionTree.Action;
import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.DecisionTreeNode;
import carlos.DecisionTree.MultiDecision;
import carlos.helper.Example;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static carlos.Utils.InductionOfDecisionTrees.*;

public class ID3 extends DecisionTree {

    public ID3(DecisionTreeNode root) {
        super(root);
    }

    @Override
    protected DecisionTreeNode makeTree(List<Example> examples, List<String> attributes, DecisionTreeNode decisionNode){
        System.out.println("\n\nMAKE TREE");
        System.out.println("=========");
        System.out.println("Examples:");
        for(Example example: examples){
            example.printExample();
        }
        System.out.println("=========");

        // Calculate initial entropy
        float initialEntropy = entropy(examples);
        System.out.println("Entropy of example set: " + initialEntropy);
        System.out.println();

        // If there is no entropy, can't go further
        if(initialEntropy <= 0){
            return decisionNode;
        }

        // Get the number of examples
        int exampleCount = examples.size();

        // Store the best split
        float bestInformationGain = 0;
        String bestSplitAttribute;
        bestSplitAttribute = null;
        List<List<Example>> bestSets = new ArrayList<>();

        // Go though each attribute
        for(String attribute : attributes){

            // Perform the split
            List<List<Example>> sets = splitByAttribute(examples, attribute);

            // Find the overall entropy and information gain
            float overallEntropy = entropyOfSets(sets, exampleCount, attribute);
            float informationGain = initialEntropy - overallEntropy;

            System.out.println("Information gain of " + attribute + ": " + informationGain);
            System.out.println();

            // Check if this is the best split so far
            if(informationGain > bestInformationGain){
                bestInformationGain = informationGain;
                bestSplitAttribute = attribute;
                bestSets = sets;
            }
        }

        if(bestSplitAttribute != null) {
            // Set the decision node's test
            ((MultiDecision) decisionNode).testValue = bestSplitAttribute;
        }
        else {
            Hashtable<Object, Integer> actionTallies = actionTallies(examples);
            Object bestAction = examples.get(0).action;  // initialize bestAction to first example on the set.
            int bestTally = 0;
            for(Object action : actionTallies.keySet()){
                if(actionTallies.get(action) > bestTally){
                    bestTally = actionTallies.get(action);
                    bestAction = action;
                }
            }
            decisionNode = new Action(bestAction);
            return decisionNode;
        }

        // Remove this best attribute from the list passed to the next iteration
        List<String> newAttributes = new ArrayList<>(attributes);
        newAttributes.remove(bestSplitAttribute);

        // Fill the daughter nodes
        for(List<Example> set : bestSets){
            // Find the value for the attribute in this set
            Object attributeValue = set.get(0).getValue(bestSplitAttribute);

            // Create a daughter node for the tree
            DecisionTreeNode daughter;
            if(entropy(set) == 0){
                daughter = new Action(set.get(0).action);
            }
            else{
                daughter = new MultiDecision();
            }

            // Recurse the algorithm
            daughter = makeTree(set, newAttributes,daughter);
            // Add the daughter node to the tree
            ((MultiDecision) decisionNode).daughterNodes.put(attributeValue, daughter);

        }
        return decisionNode;
    }
}