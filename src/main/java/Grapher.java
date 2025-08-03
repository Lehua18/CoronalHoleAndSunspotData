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

    //2D Grapher
    public Grapher(double[] x, double[] y, double[] x2, double[] y2) throws InterruptedException{
        //Set endpoints
        double startDate = 2010.0;
        double endDate = 2026.0;

        //find approx index of endpoints
        int startIndex = -1;
        int endIndex = -1;
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

        //Create new arrays to eliminate "jumps" near the endpoints
        double[] xTruncData = new double[x.length-6];
        double[] yTruncData = new double[y.length-6];
        int count = 0;
        for(int j = 0; j<x.length-6; j++){
            xTruncData[count] = x[j];
            yTruncData[count] = y[j];
            count++;
        }
        double[] x2TruncData = new double[x2.length-8];
        double[] y2TruncData = new double[y2.length-8];
        count = 0;
        for(int j = 6; j<x2.length-2; j++){
            x2TruncData[count] = x2[j];
            y2TruncData[count] = y2[j];
            count++;
        }



        //Create 2D graph
        XYChart chart = new XYChartBuilder().width(1200).height(800).title("Sunspot Number Over Time").xAxisTitle("Year").yAxisTitle("Sunspot Number").build();

        // Customize Chart
        Color greenish = Color.getHSBColor((float)2.29,(float)0.8,(float)0.32);
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
        chart.getStyler().setChartTitleFont(new Font(Font.SERIF, Font.BOLD, 48));
        chart.getStyler().setLegendFont(new Font(Font.SERIF, Font.PLAIN, 24));
        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideE);
        chart.getStyler().setLegendSeriesLineLength(12);
        chart.getStyler().setAxisTitleFont(new Font(Font.SANS_SERIF, Font.ITALIC, 24));
        chart.getStyler().setAxisTickLabelsFont(new Font(Font.SERIF, Font.PLAIN, 18));
        chart.getStyler().setDatePattern("MM-dd");
        chart.getStyler().setDecimalPattern("#0");
        chart.getStyler().setLocale(Locale.ENGLISH);
        chart.getStyler().setYAxisTickLabelsColor(Color.BLUE);
        chart.getStyler().setLegendPadding(15);
        chart.getStyler().setAxisTitlePadding(30);
        chart.getStyler().setChartTitlePadding(30);



        //Add data to graph
        XYSeries chSeries = (XYSeries) chart.addSeries("Coronal Hole Data", x2TruncData, y2TruncData).setYAxisGroup(1);
        XYSeries sunspotSeries = chart.addSeries("Sunspot Data", xTruncData, yTruncData);


        //Yaxis group
        chart.setYAxisGroupTitle(1, "Coronal Hole Area (1/1000s of a Solar Disk)");
        chart.getStyler().setYAxisGroupPosition(1, Styler.YAxisPosition.Right);
        chart.getStyler().setYAxisGroupTickLabelsColorMap(1,greenish);
        chart.getStyler().setYAxisMax(1,80.0);

        //Style individual series
        sunspotSeries.setLineColor(XChartSeriesColors.BLUE);
        sunspotSeries.setLineStyle(SeriesLines.SOLID);
        sunspotSeries.setMarker(SeriesMarkers.NONE);
        chSeries.setLineColor(greenish);
        chSeries.setLineStyle(SeriesLines.SOLID);
        chSeries.setMarker(SeriesMarkers.NONE);
        chSeries.setMarkerColor(greenish);


        //display chart
        new SwingWrapper<XYChart>(chart).displayChart();

        //UNCOMMENT IF THE CHART SHOULD JUST BE DOWNLOADED, NOT SHOWN
//        try {
//            BitmapEncoder.saveBitmap(chart, getTime(), BitmapFormat.PNG);
//        } catch (IOException e) {
//            System.out.println(e.getMessage());
//            throw new RuntimeException(e);
//        }
    }
        //UNCOMMENT IF THE CHART SHOULD JUST BE DOWNLOADED, NOT SHOWN
//    public String getTime(){
//        LocalDateTime now = LocalDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        return now.format(formatter);
//    }

}
