package agents.carlosTello;

import carlos.DataManager.FileManager;
import carlos.DecisionTree.Action;
import carlos.DecisionTree.DecisionTree;
import carlos.DecisionTree.DecisionTreeNode;
import carlos.DecisionTree.MultiDecision;
import carlos.ID3.ID3;
import carlos.Utils.ArrayUtils;
import carlos.helper.Dataset;
import carlos.helper.Example;
import com.google.gson.Gson;
import engine.core.MarioAgent;
import engine.core.MarioForwardModel;
import engine.core.MarioTimer;
import engine.helper.MarioActions;

import java.util.*;

public class Agent implements MarioAgent {
    private boolean[] action;

    private Dataset dataset;
    private DecisionTree decisionTree;

    public Agent(String datasetFileName){
        this.dataset = loadDataset(datasetFileName);
    }

    @Override
    public void initialize(MarioForwardModel model, MarioTimer timer) {
        this.action = new boolean[MarioActions.numberOfActions()];

        List<Example> examplesList = new ArrayList<>(Arrays.asList(dataset.examples));
        List<String> attributesList = new ArrayList<>(Arrays.asList(dataset.attributes));
        MultiDecision root = new MultiDecision();

        DecisionTree dt = new ID3(root);
        dt.MakeTree(examplesList, attributesList);
        dt.PrintTree();

        this.decisionTree = dt;
    }

    @Override
    public boolean[] getActions(MarioForwardModel model, MarioTimer timer) {
        Hashtable<String,Object> attributes = getObservation(model);
        DecisionTreeNode node = decisionTree.makeDecision(attributes);
        if(node != null && node instanceof Action){
            ArrayList<Boolean> actionList = (ArrayList<Boolean>) ((Action) node).action;
            int index = 0;
            for(Object button : actionList){
                action[index] = (boolean) button;
                index++;
            }
        }
        return action;
    }

    @Override
    public String getAgentName() {
        return "CarlosTelloAgent";
    }

    public Dataset loadDataset(String datasetFilename){

        Gson gson = new Gson();
        String jsonDeserialize = FileManager.ReadFile(datasetFilename + ".JSON");
        Dataset datasetDeserialize = gson.fromJson(jsonDeserialize, Dataset.class);
        return datasetDeserialize;
    }

    private Hashtable<String, Object> getObservation(MarioForwardModel model){
        Hashtable<String, Object> attributes = new Hashtable<>();

        attributes.put("OnGround", model.isMarioOnGround());
        attributes.put("MayJump", model.mayMarioJump());
        attributes.put("CanJumpHigher", model.getMarioCanJumpHigher());
        attributes.put("SmallMarioObservation", ArrayUtils.matrixToArrayList(ArrayUtils.getSubmatrix(model.getMarioCompleteObservation(2,2),
                9,5,13,9)));
        attributes.put("SmallGroundMarioObservation", ArrayUtils.matrixToArrayList(ArrayUtils.getSubmatrix(model.getMarioSceneObservation(2),
                9,9,13,10)));
        return attributes;
    }

    private ArrayList<ArrayList<Double>> matrixToArrayList(int[][] array){
        ArrayList<ArrayList<Double>> arrayLists = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            ArrayList<Double> aux = new ArrayList<>();
            for(int j = 0; j < array[0].length; j++){
                aux.add(j, (double) array[i][j]);
            }
            arrayLists.add(i, aux);
        }
        return arrayLists;
    }

    private ArrayList<Double> arrayToArrayList(int[] array){
        ArrayList<Double> arrayList = new ArrayList<>();
        for(int i = 0; i < array.length; i++){
            arrayList.add(i, (double) array[i]);
        }
        return arrayList;
    }
}
