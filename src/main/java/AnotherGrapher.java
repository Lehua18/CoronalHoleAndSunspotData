//3D Graph and stylers
import com.rinearn.graph3d.RinearnGraph3D;

//2D Graph and Stylers
import org.jfree.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYSplineRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.plot.PlotOrientation;



//General Imports
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JFrame;


public class AnotherGrapher {
    private ArrayList<Double> coeff;
    private Map<String, Boolean> vars = new HashMap<>();



    //2D Grapher
    public AnotherGrapher(double[] x, double[] y, double[] x2, double[] y2) throws InterruptedException{
        //Set endpoints
        double startDate = 2010.0;
        double endDate = 2026.0;

        //find approx index of endpoints
        int startIndex = -1;
        int endIndex = -1;
        int endApproxIndex = -1;
        boolean foundStart = false;
        boolean foundEnd = false;
        for(int i = 0; i<x.length; i++){
            if(x[i] >= startDate && !foundStart){
                startIndex = i;
                System.out.println("Start: "+startIndex);
                foundStart = true;
            }
            if (x[i] >= endDate && !foundEnd){
                endIndex = i-1;
                System.out.println("End: "+endIndex);
                foundEnd = true;
            }
        }

        //Create new arrays with data in each time range
        double[] xTruncData = new double[Math.abs(endIndex-startIndex+1)];
        double[] yTruncData = new double[Math.abs(endIndex-startIndex+1)];
        int count = 0;
        for(int j = startIndex; j<=endIndex; j++){
            xTruncData[count] = x[j];
            yTruncData[count] = y[j];
            count++;
        }



        //Create 2D graph
        XYSeries sunspotNum = new XYSeries("sunspotNum");
        for(int i = 0; i<x.length; i++){
            sunspotNum.add(x[i], y[i]);
        }
        XYSeries chArea = new XYSeries("chArea");
        for(int i = 0; i< x2.length; i++){
            chArea.add(x2[i], y2[i]);
        }
        XYSeriesCollection dataset0 = new XYSeriesCollection();
        dataset0.addSeries(sunspotNum);
        XYSeriesCollection dataset1 = new XYSeriesCollection();
        dataset1.addSeries(chArea);

        XYPlot plot = new XYPlot();
        plot.setDataset(0, dataset0);
        plot.setDataset(1, dataset1);

        XYSplineRenderer sr0 = new XYSplineRenderer();
        plot.setRenderer(0, sr0);
        XYSplineRenderer sr1 = new XYSplineRenderer();
        plot.setRenderer(1, sr1);


    }

    //Get coefficients of least squares approx method
    public double squaresApprox(double x, double[] coeff){
        double output = 0;
        for(int b = coeff.length-1; b>=0; b--){
            output+= coeff[b]*Math.pow(x,b);
        }
        return output;
    }


    //Gets the index of the 'center' of an array
    public int centerIndex(double[] arr, double center){
        int centerIndex = -1;
        for(int j = 0; j< arr.length; j++){
            if(arr[j] == center){
                centerIndex = j;
            }
        }
        return centerIndex;
    }

    //Adds all elements in an array
    public double sum(double[] arr){
        double total = 0;
        for (double v : arr) {
            total += v;
        }
        return total;
    }

    //Takes the factorial of a number
    public int factorial(int num){
        int total = 1;
        if(num != 0 && num != 1) {
            for (int i = num; i > 0; i--) {
                total *= i;
            }
        }
        return total;
    }

    public String getTime(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return now.format(formatter);
    }
}
