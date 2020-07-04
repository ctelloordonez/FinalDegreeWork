package carlos.helper;

import carlos.Utils.ArrayUtils;

import java.util.ArrayList;

public class Dataset {
    public Example[] examples;
    public String[] attributes;

    public Dataset(Example[] examples, String[] attributes){
        this.examples = examples;
        this.attributes = attributes;
    }

    public void addData(Dataset newData){
        Example[] aux = new Example[newData.examples.length + examples.length];
        int i = 0;
        for(int j = 0; j < examples.length; j++){
            aux[i] = examples[j];
            i++;
        }
        for(int k= 0; k < newData.examples.length; k++){
            aux[i] = newData.examples[k];
            i++;
        }
        examples = aux;
//        Example[] aux = ArrayUtils.addAll(examples, newData.examples);;
//        this.examples = aux;
    }

    public void printDataset(){
        System.out.println("EXAMPLES:");
        for (Example example : examples){
            example.printExample();
        }
        System.out.println("\nATTRIBUTES:");
        for(String attribute : attributes){
            System.out.print(attribute + "\t");
        }
    }
}
