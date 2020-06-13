package carlos.DataManager;

import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;

import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;

public class FileManager {
    private static String directory = "files/";

    public static Dataset ReadCSV(String filename) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(directory + filename + ".csv"));
        String header = br.readLine();
        String[] headers = new String[0];
        if (header != null) {
            headers = header.split(",");
        }
        String example = br.readLine();
        List<Example> examples = new ArrayList<>();
        while(example != null){
            String[] columns = example.split(",");
            String action = columns[0];
            Hashtable<String,String> attributes = new Hashtable<>();
            for(int i = 1; i < columns.length; i++){
                attributes.put(headers[i], columns[i]);
            }
            Example e = new Example(action,attributes);
            examples.add(e);
            example = br.readLine();
        }

        List<String> attributes = new ArrayList<>();
        for(int i = 1; i < headers.length; i++){
            attributes.add(headers[i]);
        }

        Example[] examplesArray = new Example[examples.size()];
        examples.toArray(examplesArray);
        String[] attributesArray = new String[attributes.size()];
        attributes.toArray(attributesArray);

        return new Dataset(examplesArray, attributesArray);
    }

    public static Dataset ReadJSON(String filename){
        String jsonDeserialize = FileManager.ReadFile(filename + ".JSON");

        Gson gson = new Gson();
        Dataset myDataset = gson.fromJson(jsonDeserialize, Dataset.class);
        return myDataset;
    }

    public static void CreateFile(String filename){
        try{
            File myObj = new File(directory + filename);
            if(myObj.createNewFile()){
                System.out.println("File created: " + myObj.getName());
            }
            else {
                System.out.println("File already exists");
            }
        } catch (IOException e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    public static String ReadFile(String filename){
        String fileContent = "";
        try{
            File myObject = new File(directory + filename);
            Scanner myReader = new Scanner(myObject);
            while (myReader.hasNextLine()){
                String data = myReader.nextLine();
                fileContent += data + "\n";
            }
            myReader.close();
            System.out.println("Successfully read from the file");
        } catch (FileNotFoundException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
        return fileContent;
    }

    public static void WriteToFile(String filename, String fileContent){
        try{
            FileWriter myWriter = new FileWriter(directory + filename);
            myWriter.write(fileContent);
            myWriter.close();
            System.out.println("Successfully wrote to the file");
        } catch (IOException e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }
}
