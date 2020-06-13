package carlos;

import carlos.DataManager.FileManager;
import carlos.DecisionTree.MultiDecision;
import carlos.ID4.ID4;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

public class MakeID4 {
    public static void main(String[] args) {

        String datasetFilename = "datasetMF_ID4";
        String decisionTreeFilename = "ID4Shooter";
//        String datasetFilename = "datasetQuinlan";
//        String decisionTreeFilename = "ID4Sport";

        Gson gson = new Gson();
        Dataset myDataset = FileManager.ReadJSON(datasetFilename);

        List<Example> examplesList = new ArrayList<>(Arrays.asList(myDataset.examples));
        List<String> attributesList = new ArrayList<>(Arrays.asList(myDataset.attributes));
        MultiDecision root = new MultiDecision();

        ID4 decisionTree = new ID4(root);
        decisionTree.MakeTree(examplesList, attributesList);

        Hashtable<String, String> a1 = new Hashtable<>();
        a1.put("Health", "Hurt");
        a1.put("Cover", "Exposed");
        a1.put("Ammo", "With Ammo");
        Example e1 = new Example("Defend", a1);

        Hashtable<String, String> a2 = new Hashtable<>();
        a2.put("Health", "Healthy");
        a2.put("Cover", "Exposed");
        a2.put("Ammo", "With Ammo");
        Example e2 = new Example("Run", a2);

        decisionTree.IncrementTree(e1);
        decisionTree.IncrementTree(e2);



        String json = gson.toJson(decisionTree);

        System.out.println("Saving JSON at files");
        FileManager.CreateFile(decisionTreeFilename + ".JSON");
        FileManager.WriteToFile(decisionTreeFilename + ".JSON", json);
    }
}
