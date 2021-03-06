package carlos.ID4;

import carlos.DecisionTree.Action;
import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.DecisionTreeNode;
import carlos.DecisionTree.MultiDecision;
import carlos.helper.Example;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import static carlos.Utils.InductionOfDecisionTrees.*;

public class ID4 extends DecisionTree {

    public ID4(DecisionTreeNode root) {
        super(root);
    }

    @Override
    protected DecisionTreeNode makeTree(List<Example> examples, List<String> attributes, DecisionTreeNode decisionNode) {
        System.out.println("\n\nMAKE TREE");
        System.out.println("=========");
        System.out.println("Examples:");
        for(Example example: examples){
            example.printExample();
        }
        System.out.println("=========");

        // Add example to decisionNode's visited examples.
        decisionNode.examples = examples;

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

    public void IncrementTree(Example example){
        root = incrementTree(example, root);
    }

    private DecisionTreeNode incrementTree(Example example, DecisionTreeNode treeNode){
        treeNode.examples.add(example);

        // If the node is a terminal...
        if(treeNode instanceof Action) {
            // .. and the example's action does not match...
            if (!example.action.equals(treeNode.examples.get(0).action)) {
                // ... make the node into a decision
                DecisionTreeNode subTree = new MultiDecision();

                treeNode = makeTree(treeNode.examples,treeNode.examples.get(0).allAttributes(),subTree);
            }
        }

        // If the node is a decision...
        else if(treeNode instanceof MultiDecision) {
            // ... find the new best split attribute
            // Calculate initial entropy
            float initialEntropy = entropy(treeNode.examples);

            // If there is no entropy, can't go further
            if (initialEntropy <= 0) {
                List<Example> auxExamples = treeNode.examples;
                treeNode = new Action(example.action);
                treeNode.examples = auxExamples;
                return treeNode;
            }

            // Get the number of examples
            int exampleCount = treeNode.examples.size();

            // Store the best split
            float bestInformationGain = 0;
            String bestSplitAttribute;
            bestSplitAttribute = null;

            // Go though each attribute
            for (String attribute : treeNode.examples.get(0).allAttributes()) {

                // Perform the split
                List<List<Example>> sets = splitByAttribute(treeNode.examples, attribute);

                // Find the overall entropy and information gain
                float overallEntropy = entropyOfSets(sets, exampleCount, attribute);
                float informationGain = initialEntropy - overallEntropy;

                System.out.println("Information gain of " + attribute + ": " + informationGain);
                System.out.println();

                // Check if this is the best split so far
                if (informationGain > bestInformationGain) {
                    bestInformationGain = informationGain;
                    bestSplitAttribute = attribute;
                }
            }

            // If there is a new best split attribute...
            if (bestSplitAttribute != null && !bestSplitAttribute.equals(((MultiDecision) treeNode).testValue)) {
                // ... make a new subtree from this node.
                System.out.println(bestSplitAttribute + " is better than " + ((MultiDecision) treeNode).testValue);
                List<Example> exampleList = treeNode.examples;
                List<String> attributeList = treeNode.examples.get(0).allAttributes();
                DecisionTreeNode subtree = new MultiDecision();

                treeNode = makeTree(exampleList, attributeList, subtree);;
            }
            // If the best attribute is still the same as before...
            else {
                // ... continue incrementing the tree at the corresponding daughter node.
                DecisionTreeNode daughter;
                if(((MultiDecision)treeNode).getBranch(example.getObservation()) == null){
                    daughter = new MultiDecision();
                }
                else {
                    daughter = ((MultiDecision)treeNode).getBranch(example.getObservation());
                }
                ((MultiDecision)treeNode).daughterNodes.put(example.getObservation().get(((MultiDecision)treeNode).testValue), incrementTree(example,daughter));
            }
        }
        return treeNode;
    }
}
