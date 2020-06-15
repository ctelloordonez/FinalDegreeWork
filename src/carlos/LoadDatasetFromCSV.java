package carlos;

import carlos.DataManager.FileManager;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.io.IOException;

public class LoadDatasetFromCSV {
    public static void main(String[] args) throws IOException {
        System.out.println("Reading dataset from CSV file\n");
        String datasetFilename = "datasetMF";
//        String datasetFilename = "datasetQuinlan";
        Dataset myDataset = FileManager.ReadCSV(datasetFilename);
        for (Example example : myDataset.examples){
            example.printExample();
        }

        System.out.println("\nSerializing dataset as json\n");
        Gson gson = new Gson();
        String json = gson.toJson(myDataset);

        System.out.println("Saving JSON at files");
        FileManager.CreateFile(datasetFilename + ".JSON");
        FileManager.WriteToFile(datasetFilename + ".JSON", json);

        System.out.println("\nDeserializing JSON from files\n");
        String jsonDeserialize = FileManager.ReadFile(datasetFilename + ".JSON");
        Dataset datasetDeserialize = gson.fromJson(jsonDeserialize, Dataset.class);

        System.out.println("\nReading deserialize dataset from JSON");
        datasetDeserialize.printDataset();
    }
}
