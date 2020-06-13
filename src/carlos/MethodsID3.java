package carlos;

import carlos.DataManager.FileManager;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import static carlos.Utils.InductionOfDecisionTrees.*;

public class MethodsID3 {
    public static void main(String[] args) {
        String datasetFilename = "datasetQuinlan";

        Gson gson = new Gson();
        Dataset myDataset = FileManager.ReadJSON(datasetFilename);

        List<List<Example>> splitSets;
        for(String attribute : myDataset.attributes){
            splitSets = splitByAttribute(Arrays.asList(myDataset.examples), attribute);
            entropyOfSets(splitSets, myDataset.examples.length, attribute);
        }

        entropy(Arrays.asList(myDataset.examples));
    }
}
