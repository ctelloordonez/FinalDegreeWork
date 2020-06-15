package carlos.Utils;

import carlos.DecisionTree.Action;
import carlos.DecisionTree.DecisionTreeNode;
import carlos.DecisionTree.MultiDecision;
import carlos.helper.Example;

import java.util.*;

import static java.lang.Math.log;

public class InductionOfDecisionTrees {

    public static void inductTree(List<Example> examples, List<String> attributes, DecisionTreeNode decisionNode){
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
            return;
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

        // Set the decision node's test
        ((MultiDecision) decisionNode).testValue = bestSplitAttribute;

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

            // Add it to the tree
            ((MultiDecision) decisionNode).daughterNodes.put(attributeValue, daughter);

            // Recurse the algorithm
            inductTree(set, newAttributes,daughter);
        }
    }

    /**
     * @param examples list of examples from which divide subsets
     * @param attribute attribute used as division of the set of examples
     * @return divides a list of examples into several subsets so each of the examples in a subset share the same value for that attribute
     */
    public static List<List<Example>> splitByAttribute(List<Example> examples, String attribute){

        // Create a set of list, so we can access each list by the attribute value
        HashMap<Object, List<Example>> sets = new HashMap<>();

        // For each example
        for (Example example : examples){
            // Get the set of examples with the same value for the given attribute
            List<Example> setOfAttribute = sets.get(example.getValue(attribute));

            // If this set of examples is not initialized...
            if(setOfAttribute == null){
                // ... initialize a new list of examples
                setOfAttribute = new ArrayList<>();
            }

            // Add the example to the set of examples with the same value for the given attribute
            setOfAttribute.add(example);

            // Assign each example to the right set
            sets.put(example.getValue(attribute), setOfAttribute);
        }

        // Convert the dictionary of list to a list of lists
        List<List<Example>> setsAsList = new ArrayList<>(sets.values());

        // Return the sets
        return setsAsList;
    }


    /**
     * @param examples List of examples
     * @return  entropy of the list of examples
     */
    public static float entropy(List<Example> examples){
        int exampleCount = examples.size();

        if(exampleCount == 0) return 0;

        Hashtable<Object, Integer> actionTallies = new Hashtable<>();

        for(Example example : examples){
            int tally = actionTallies.get(example.action) != null ? actionTallies.get(example.action) + 1 : 1;
            actionTallies.put(example.action, tally);
        }

        int actionsCount = actionTallies.size();

        if (actionsCount <= 1) return 0;

        float entropy = 0;

        ArrayList<Integer> actions = Collections.list(actionTallies.elements());

        for(int actionTally : actions){
            float proportion = (float)actionTally / (float)exampleCount;
            entropy -= proportion * log(proportion, actionsCount);
        }
        return entropy;
    }

    /**
     * @param sets List of sets divided by test values
     * @param exampleCount total number of examples
     * @return Entropy of a list of list
     */
//    private static float entropyOfSets(List<List<Example>> sets, int exampleCount){
    public static float entropyOfSets(List<List<Example>> sets, int exampleCount, String splitAttribute){
        float entropy = 0;

        for(List<Example> set : sets){
            float proportion = (float)set.size() / (float)exampleCount;
            float entropySet = entropy(set);
            System.out.println("Entropy of " + set.get(0).getValue(splitAttribute) + ": " + entropySet);
            entropy += proportion * entropySet;
        }

        return entropy;
    }


    /**
     * @param x value
     * @return log2 of x
     */
    private static double log(float x, int actionsCount){
        return Math.log(x) / Math.log(actionsCount);
    }
}
