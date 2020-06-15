package carlos.helper;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class Example {
    public Object action;
    Hashtable<String, Object> attributes;

    public Example(Object action, Hashtable<String, Object> attributes){
        this.action = action;
        this.attributes = attributes;
    }

    public Object getValue(String attribute){
        return attributes.get(attribute);
    }

    public Hashtable<String, Object> getObservation(){
        return attributes;
    }

    public List<String> allAttributes(){
        return Collections.list(attributes.keys());
    }

    public void printExample(){
        System.out.print(action + ":");
        for(String attribute : attributes.keySet()){
            System.out.print("\t" + getValue(attribute).toString());
        }
        System.out.println();
    }
}
