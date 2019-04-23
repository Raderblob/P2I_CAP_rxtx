import java.util.ArrayList;
import java.util.LinkedList;

public class DataSet {
    public ArrayList<Data> myData;
    public String myName;

    public DataSet(ArrayList<Data> data,String n){
        myData=data;
        myName=n;
    }
    public int myLength(){
        return myData.size();
    }
    public double compareTo(DataSet otherSet){
        double res =0;

        for(int i =0;i< Math.min(myLength(),otherSet.myLength());i++){
            res+= myData.get(i).compareTo(otherSet.myData.get(i));
        }
        return res;
    }
}
