package agents.carlosTello;

import carlos.DataManager.FileManager;
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

    private Dataset dataset;
    private ArrayList<Example> exampleList;

    private int ticksToSave = 0;

    public Recorder(MarioAgent recordedAgent){
        this.recordedAgent = recordedAgent;
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

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMddhhmmss");
//        String datasetFilename = formatter.format(date);
        String datasetFilename = "RecordedMarioDataset";

        Gson gson = new Gson();
        String json = gson.toJson(dataset);

        FileManager.CreateFile(datasetFilename + ".JSON");
        FileManager.WriteToFile(datasetFilename + ".JSON", json);
    }

    private Hashtable<String, Object> getObservation(MarioForwardModel model){
        Hashtable<String, Object> attributes = new Hashtable<>();

        attributes.put("MarioMode", model.getMarioMode());
        attributes.put("OnGround", model.isMarioOnGround());
        attributes.put("MayJump", model.mayMarioJump());
        attributes.put("EnemiesObservation", model.getMarioEnemiesObservation(0));
        attributes.put("SceneObservation", model.getMarioSceneObservation(0));
//        attributes.put("EnemiesPositions", model.getEnemiesFloatPos());
        attributes.put("MarioPosition", model.getMarioScreenTilePos());
        attributes.put("CanJumpHigher", model.getMarioCanJumpHigher());

        return attributes;
    }
}
