package carlos;

import carlos.DataManager.FileManager;
import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.MultiDecision;
import carlos.ID3.ID3;
import carlos.ID4.ID4;
import carlos.Utils.ArrayUtils;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ID3vsID4 {

    private static long[] makeID4(List<Example> examples){
        long[] elapsedTimes = new long[examples.size()];
        MultiDecision root = new MultiDecision();
        DecisionTree dt = new ID4(root);
        for(int i = 0; i < examples.size(); i++) {
            System.out.println("****************************************************************");
            long startTime = System.nanoTime();
            ((ID4)dt).IncrementTree(examples.get(i));
            dt.PrintTree();
            long endTime = System.nanoTime();
            elapsedTimes[i] = endTime-startTime;
            System.out.println("Increment time = " + (elapsedTimes[i]) + "ns");
            System.out.println("****************************************************************");
        }
        return  elapsedTimes;
    }

    private static long[] makeID3(List<Example> examples, List<String> attributes){
        long[] elapsedTimes = new long[examples.size()];
        MultiDecision root = new MultiDecision();
        DecisionTree dt = new ID3(root);
        for(int i = 0; i < examples.size(); i++){
            System.out.println("****************************************************************");
            List<Example> examplesRange = ArrayUtils.copyOfRange((ArrayList<Example>) examples,0,i);
            long startTime = System.nanoTime();
            dt.MakeTree(examplesRange, attributes);
            dt.PrintTree();
            long endTime = System.nanoTime();
            elapsedTimes[i] = endTime-startTime;
            System.out.println("Increment time = " + (elapsedTimes[i]) + "ns");
            System.out.println("****************************************************************");
        }
        return elapsedTimes;
    }

    public static void main(String[] args) {

        String datasetFilename = "MarioDatasetComplete";

        Dataset myDataset = FileManager.ReadJSON(datasetFilename);

        List<Example> examplesList = new ArrayList<>(Arrays.asList(myDataset.examples));
        List<String> attributesList = new ArrayList<>(Arrays.asList(myDataset.attributes));

        long[] id3ElapsedTimes = makeID3(examplesList,attributesList);
        long[] id4ElapsedTimes = makeID4(examplesList);

        for(int i = 0; i < id3ElapsedTimes.length;i++){
            System.out.println("Increment " + (i+1) + ":");
            System.out.println("\tID3 = " + id3ElapsedTimes[i]);
            System.out.println("\tID4 = " + id4ElapsedTimes[i]);
        }
    }
}
