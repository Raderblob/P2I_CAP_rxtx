import dtw.TimeWarpInfo;
import timeseries.TimeSeries;
import util.DistanceFunction;
import util.DistanceFunctionFactory;

public class Data {
    public double x,y,z;

    public Data(double x, double y , double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public double compareTo(Data d){

        return Math.sqrt( Math.pow(x-d.x,2) + Math.pow(y-d.y,2)+Math.pow(z-d.z,2));

    }
}
