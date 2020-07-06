package agents.carlosTello;

import carlos.DataManager.FileManager;
import carlos.Utils.ArrayUtils;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;
import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.text.SimpleDateFormat;
import java.util.*;

public class Recorder implements MarioAgent {
    private boolean[] action;
    private MarioAgent recordedAgent;
    private String datasetFilename;

    private Dataset dataset;
    private ArrayList<Example> exampleList;

    private int ticksToSave = 0;

    public Recorder(MarioAgent recordedAgent, String datasetFilename){
        this.recordedAgent = recordedAgent;
        this.datasetFilename = datasetFilename;
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.recordedAgent.initialize(model, timer);
        this.action = new boolean[MarioActions.numberOfActions()];
        exampleList = new ArrayList<>();
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        action = recordedAgent.getActions(model, timer);
       //if (ticksToSave <= 0){
        exampleList.add(new Example(action, getObservation(model)));
        //ticksToSave = 3;
        //}
        //ticksToSave--;
        return action;
    }

    @Override
    public String getAgentName() {
        return "CarlosTelloRecorder";
    }

    public void SaveObservations(){
        Example[] examples = exampleList.toArray(new Example[exampleList.size()]);
        String[] attributes = exampleList.get(0).allAttributes().toArray(new String[examples[0].allAttributes().size()]);
        dataset = new Dataset(examples, attributes);

//        Date date = new Date();
//        SimpleDateFormat formatter = new SimpleDateFormat("MMddhhmmss");
//        String datasetFilename = formatter.format(date);
//        String datasetFilename = "RecordedMarioDatasetGenerate";

        Gson gson = new Gson();

        String prevDataset = FileManager.ReadFile(datasetFilename + ".JSON");
        if(prevDataset != ""){
            Dataset previousDataset = gson.fromJson(prevDataset, Dataset.class);

            Example[] aux = new Example[previousDataset.examples.length + dataset.examples.length];
            int i = 0;
            for(int j = 0; j < examples.length; j++){
                aux[i] = dataset.examples[j];
                i++;
            }
            for(int k= 0; k < previousDataset.examples.length; k++){
                aux[i] = previousDataset.examples[k];
                i++;
            }
            dataset.examples = aux;
//            dataset.addData(previousDataset);
        }

        String json = gson.toJson(dataset);
        FileManager.CreateFile(datasetFilename + ".JSON");
        FileManager.WriteToFile(datasetFilename + ".JSON", json);
        System.out.println(dataset.examples.length);
    }

    private Hashtable<String, Object> getObservation(MarioForwardModel model){
        Hashtable<String, Object> attributes = new Hashtable<>();

        attributes.put("MarioMode", model.getMarioMode());
        attributes.put("OnGround", model.isMarioOnGround());
        attributes.put("MayJump", model.mayMarioJump());
        attributes.put("CanJumpHigher", model.getMarioCanJumpHigher());
        attributes.put("EnemiesObservation",
                ArrayUtils.matrixToArrayList(ArrayUtils.getSubmatrix(model.getMarioEnemiesObservation(2),
                        6,5,16,11)));
        attributes.put("GroundObservation",
                ArrayUtils.matrixToArrayList(ArrayUtils.getSubmatrix(model.getMarioSceneObservation(2),
                        8,9,16,10)));
        attributes.put("ForwardObservation",
                ArrayUtils.matrixToArrayList(ArrayUtils.getSubmatrix(model.getMarioSceneObservation(2),
                        8,6,16,9)));
        attributes.put("TopObservation",
                ArrayUtils.matrixToArrayList(ArrayUtils.getSubmatrix(model.getMarioSceneObservation(2),
                        8,5,11,11)));
//        attributes.put("EnemiesObservation", model.getMarioEnemiesObservation(0));
//        attributes.put("SceneObservation", model.getMarioSceneObservation(0));
//        attributes.put("EnemiesPositions", model.getEnemiesFloatPos());
//        attributes.put("MarioPosition", ArrayUtils.arrayToArrayList(model.getMarioScreenTilePos()));

        return attributes;
    }
}
