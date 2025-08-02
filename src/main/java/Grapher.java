//3D Graph and stylers
import com.rinearn.graph3d.RinearnGraph3D;

//2D Graph and Stylers
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;
import org.knowm.xchart.style.colors.*;
import org.knowm.xchart.style.lines.SeriesLines;
import org.knowm.xchart.XYChart;         // For XY charts
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.Marker;
import org.knowm.xchart.style.markers.SeriesMarkers;
//import org.knowm.xchart.;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;



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


public class Grapher {
    private ArrayList<Double> coeff;
    private Map<String, Boolean> vars = new HashMap<>();

    //3D Grapher
    public Grapher(double[] x, double[] y, double[] z) {
        RinearnGraph3D graph = new RinearnGraph3D();
    }

    //2D Grapher
    public Grapher(double[] x, double[] y, double[] x2, double[] y2) throws InterruptedException{
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
        XYChart chart = new XYChartBuilder().width(800).height(600).title("Sunspot Number Over Time").xAxisTitle("Year").yAxisTitle("Sunspot Number").build();

        // Customize Chart
        chart.getStyler().setPlotBackgroundColor(Color.WHITE);
        chart.getStyler().setPlotGridLinesVisible(true);
        chart.getStyler().setPlotGridLinesColor(Color.GRAY);
        chart.getStyler().setChartBackgroundColor(Color.WHITE);
        chart.getStyler().setLegendBackgroundColor(Color.WHITE);
        chart.getStyler().setLegendBorderColor(Color.BLACK);
        chart.getStyler().setChartFontColor(Color.BLACK);
        chart.getStyler().setChartTitleBoxVisible(false);
        chart.getStyler().setChartTitleBoxBorderColor(null);
        chart.getStyler().setAxisTickPadding(0);
        chart.getStyler().setXAxisMin(startDate);
        chart.getStyler().setXAxisMax(endDate);
        chart.getStyler().setYAxisMax(200.0);
        chart.getStyler().setYAxisMin(0.0);
        chart.getStyler().setAxisTickMarkLength(15);
        chart.getStyler().setPlotMargin(20);
        chart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.BOLD, 24));
        chart.getStyler().setLegendFont(new Font(Font.SERIF, Font.PLAIN, 18));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setLegendSeriesLineLength(12);
        chart.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.ITALIC, 18));
        chart.getStyler().setAxisTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 11));
        chart.getStyler().setDatePattern("MM-dd");
        chart.getStyler().setDecimalPattern("#0");
        chart.getStyler().setLocale(Locale.ENGLISH);
        chart.getStyler().setYAxisTickLabelsColor(Color.BLUE);



        //Add data to graph
        XYSeries chSeries = (XYSeries) chart.addSeries("Coronal Hole Data", x2, y2).setYAxisGroup(1);
        XYSeries sunspotSeries = chart.addSeries("Sunspot Data", x, y);


        //Yaxis group
        chart.setYAxisGroupTitle(1, "Coronal Hole Area (1/1000s of a Solar Disk)");
        chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
        chart.getStyler().setYAxisGroupTickLabelsColorMap(1,Color.GREEN);
        chart.getStyler().setYAxisMax(1,80.0);

        //Style individual series
        sunspotSeries.setLineColor(XChartSeriesColors.BLUE);
        sunspotSeries.setLineStyle(SeriesLines.SOLID);
        sunspotSeries.setMarker(SeriesMarkers.NONE);
        chSeries.setLineColor(XChartSeriesColors.GREEN);
        chSeries.setLineStyle(SeriesLines.SOLID);
        chSeries.setMarker(SeriesMarkers.NONE);
        chSeries.setMarkerColor(Color.GREEN);


        //display chart
        new SwingWrapper<XYChart>(chart).displayChart();
//        try {
//            BitmapEncoder.saveBitmap(chart, getTime(), BitmapFormat.PNG);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
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
