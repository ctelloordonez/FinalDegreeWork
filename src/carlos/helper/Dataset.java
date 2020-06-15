package carlos.helper;

public class Dataset {
    public Example[] examples;
    public String[] attributes;

    public Dataset(Example[] examples, String[] attributes){
        this.examples = examples;
        this.attributes = attributes;
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
