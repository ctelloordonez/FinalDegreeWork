package carlos.helper;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class Example {
    public String action;
    Hashtable<String, String> attributes;

    public Example(String action, Hashtable<String, String> attributes){
        this.action = action;
        this.attributes = attributes;
    }

    public String getValue(String attribute){
        return attributes.get(attribute);
    }

    public Hashtable<String, String> getObservation(){
        return attributes;
    }

    public List<String> allAttributes(){
        return Collections.list(attributes.keys());
    }

    public void printExample(){
        System.out.print(action + ":");
        for(String attribute : attributes.keySet()){
            System.out.print("\t" + getValue(attribute));
        }
        System.out.println();
    }
}
