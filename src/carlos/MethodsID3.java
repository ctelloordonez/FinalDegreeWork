package carlos;

import carlos.DataManager.FileManager;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static carlos.Utils.InductionOfDecisionTrees.*;

public class MethodsID3 {
    public static void main(String[] args) {
        String datasetFilename = "datasetMF";

        Dataset myDataset = FileManager.ReadJSON(datasetFilename);

        //SplitByAttribute(myDataset);
        EntropyAndInformationGain(myDataset);
    }

    private static void SplitByAttribute(Dataset dataset){
        List<List<Example>> splitSets = new ArrayList<>();
        for(String attribute : dataset.attributes){
            splitSets = splitByAttribute(Arrays.asList(dataset.examples), attribute);
            System.out.println("=============================");
            System.out.println("Division with " + attribute);
            System.out.println("=============================");
            for(List<Example> set : splitSets){
                for(Example example : set){
                    example.printExample();
                }
                System.out.println();
            }
        }
    }

    private static void EntropyAndInformationGain(Dataset dataset){
        // Calculate initial entropy
        float initialEntropy = entropy(Arrays.asList(dataset.examples));
        System.out.println("Entropy of example set: " + initialEntropy);
        System.out.println();

        // If there is no entropy, can't go further
        if(initialEntropy <= 0){
            return;
        }

        // Get the number of examples
        int exampleCount = dataset.examples.length;

        // Go though each attribute
        for(String attribute : dataset.attributes){

            // Perform the split
            List<List<Example>> sets = splitByAttribute(Arrays.asList(dataset.examples), attribute);

            // Find the overall entropy and information gain
            float overallEntropy = entropyOfSets(sets, exampleCount, attribute);
            float informationGain = initialEntropy - overallEntropy;

            System.out.println("Information gain of " + attribute + ": " + informationGain);
            System.out.println();
        }
    }
}
