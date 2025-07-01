//3D Graph and stylers
import com.rinearn.graph3d.RinearnGraph3D;

//2D Graph and Stylers
import org.knowm.xchart.*;
import org.knowm.xchart.style.*;
import org.knowm.xchart.style.colors.*;
import org.knowm.xchart.style.lines.SeriesLines;


//General Imports
import java.awt.*;
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
    public Grapher(double[] x, double[] y) throws InterruptedException{
        //Get endpoints
        Scanner scan = new Scanner(System.in);
        System.out.println("Please choose a starting year");
        double startDate = Double.parseDouble(scan.nextLine());
        System.out.println("Please choose an ending year");
        double endDate = Double.parseDouble(scan.nextLine());
        System.out.println("Please enter a ending year for the approximation");
        double endApproxDate = Double.parseDouble(scan.nextLine());

        //find approx index of endpoints
        int startIndex = -1;
        int endIndex = -1;
        int endApproxIndex = -1;
        boolean foundStart = false;
        boolean foundEnd = false;
        boolean foundEndApprox = false;
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
            if (x[i] >= endApproxDate && !foundEndApprox){
                endApproxIndex = i-1;
                System.out.println("EndApprox: "+endApproxIndex);
                foundEndApprox = true;

            }
        }

        //Create new arrays with data in each time range
        double[] xTruncData = new double[endIndex-startIndex+1];
        double[] yTruncData = new double[endIndex-startIndex+1];
        int count = 0;
        for(int j = startIndex; j<=endIndex; j++){
            xTruncData[count] = x[j];
            yTruncData[count] = y[j];
            count++;
        }
        int endOfApproxDataIndex;
        if(endApproxIndex == -1){
            endOfApproxDataIndex = x.length;
        }else{
            endOfApproxDataIndex = endApproxIndex;
        }
        double[] xAfterData = new double[endOfApproxDataIndex-endIndex+1];
        double[] yAfterData = new double[endOfApproxDataIndex-endIndex+1];
        count = 0;
        for(int l = endIndex; l<=endOfApproxDataIndex; l++){
            xAfterData[count] = x[l];
            yAfterData[count] = y[l];
            count++;
        }
//
//        //Curve fitting
//        Collection<WeightedObservedPoint> points = new ArrayList<>();
//        for(int k = 0; k<xTruncData.length; k++){
//            points.add(new WeightedObservedPoint(1, xTruncData[k], yTruncData[k]));
//        }
//        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(15);
////        fitter = fitter.getO
//        double[] coefficients = fitter.fit(points);

        //Creates a new array list with approximated values
        ArrayList<Double> xApprox = new ArrayList<>();
        ArrayList<Double> yApprox = new ArrayList<>();
        int degree = 15;
        double center = xTruncData[xTruncData.length/2];
        for(double d = startDate; d<endDate; d+= 0.08){
            //Nullify any rounding errors
            d*=100;
            d= Math.round(d);
            d/=100.0;
            xApprox.add(d);
            //System.out.println("d: "+d);
            
        }
        ArrayList<Double> xAfterApprox = new ArrayList<>();
        for(double f = endDate; f<endApproxDate; f+= 0.08){
            //Nullify rounding error
            f*=100;
            f = Math.round(f);
            f/=100.0;

            xAfterApprox.add(f);
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
        chart.getStyler().setXAxisMin();
        chart.getStyler().setXAxisMax();
        chart.getStyler().setYAxisMax(300.0);
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

        //Add data to graph

        //Style individual series
//        dataSeries.setLineColor(XChartSeriesColors.BLUE);
//        dataSeries.setLineStyle(SeriesLines.NONE);
//        futureSeries.setLineColor(XChartSeriesColors.GREEN);
//        futureSeries.setLineStyle(SeriesLines.NONE);
//        approxSeries.setLineStyle(SeriesLines.SOLID);
//        approxSeries.setLineColor(XChartSeriesColors.PURPLE);
//        approxSeries.setSmooth(true);
//        futureApproxSeries.setLineColor(XChartSeriesColors.RED);
//        futureApproxSeries.setLineStyle(SeriesLines.SOLID);
//        futureApproxSeries.setSmooth(true);

        //display chart
        new SwingWrapper<XYChart>(chart).displayChart();
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
}
