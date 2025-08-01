public class DataSeries {
    private double[] time;
    private double[] data;
//    private String name;

    public DataSeries(double[] time, double[] data){
        this.data = data;
        this.time = time;
//        this.name = name;
    }

    public double[] getData() {
        return data;
    }

    public double[] getTime() {
        return time;
    }

//    public String getName(){
//        return name;
//    }
}
