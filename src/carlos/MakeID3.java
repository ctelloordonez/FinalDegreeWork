package carlos;

import carlos.DataManager.FileManager;
import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.MultiDecision;
import carlos.ID3.ID3;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MakeID3 {
    public static void main(String[] args) {

        String datasetFilename = "datasetMF";
        String decisionTreeFilename = "ID3Shooter";
//        String datasetFilename = "datasetQuinlan";
//        String decisionTreeFilename = "ID3Sport";

        Gson gson = new Gson();
        Dataset myDataset = FileManager.ReadJSON(datasetFilename);

        List<Example> examplesList = new ArrayList<>(Arrays.asList(myDataset.examples));
        List<String> attributesList = new ArrayList<>(Arrays.asList(myDataset.attributes));
        MultiDecision root = new MultiDecision();

        DecisionTree dt = new ID3(root);
        dt.MakeTree(examplesList, attributesList);

        String json = gson.toJson(dt);

        System.out.println("Saving JSON at files");
        FileManager.CreateFile(decisionTreeFilename + ".JSON");
        FileManager.WriteToFile(decisionTreeFilename + ".JSON", json);
    }
}
