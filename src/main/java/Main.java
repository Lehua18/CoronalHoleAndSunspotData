//Sunspot Data Imports
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

//Other Imports
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    private static int count;
    private static Map<String, DataSeries> vars = new HashMap<>();
   // private static ArrayList<Double> yrAsDec = new ArrayList<>();

    public static void main(String[] args) {
        //***!!!Get and store sunspot Data!!!***
        ArrayList<DataPoint> allData = null;
        try {
            Scanner scan1 = new Scanner(uploadTxtFile());
            allData = new ArrayList<>();
            do {
                count = 0;
                ArrayList<String> dataPoint = new ArrayList<>();
                String line = scan1.nextLine();
                int length = line.length();
                while (count < length) {
                    dataPoint.add(getValuesFromTxt(line));
                }
                allData.add(new DataPoint(dataPoint));

            } while (scan1.hasNext());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            System.exit(1);
        }
        double[] sunspotNum = new double[allData.size()];
        double[] yrAsDec = new double[allData.size()];
        for (int i = 0; i < allData.size(); i++) {
            yrAsDec[i] = allData.get(i).getYrAsDec();
        }
        for (int j = 0; j < allData.size(); j++) {
            sunspotNum[j] = allData.get(j).getSunspotNum();
        }

        ArrayList<Double>[] csvData = getValuesFromCsv(uploadCsvFile());

        //Graph Data
        try {
            Grapher grapher = new Grapher(yrAsDec, sunspotNum, arrListToArr(csvData[0]), arrListToArr(csvData[1]));
        } catch (InterruptedException e) {
            System.out.println("Something went wrong: " + e);
        }
    }


    //upload txt file
    public static File uploadTxtFile(){
        //create JFileChooser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open File");

        //Filters files shown to only txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt", "text");
        chooser.setFileFilter(filter);

        //Open chooser and get file
        int returnValue = chooser.showOpenDialog(new JFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println(file.getName());
            return file;
        }else{
            System.out.println("Something went wrong: Code "+returnValue);
            return null;
        }
    }

    public static File uploadCsvFile(){
        //create JFileChooser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Open File");

        //Filters files shown to only csv files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv", "CSV");
        chooser.setFileFilter(filter);

        //Open chooser and get file
        int returnValue = chooser.showOpenDialog(new JFrame());
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            System.out.println(file.getName());
            return file;
        }else{
            System.out.println("Something went wrong: Code "+returnValue);
            return null;
        }
    }

    public static String getValuesFromTxt(String line) {
        boolean stop = false;
        StringBuilder data = new StringBuilder();
        while (!stop) {
            if (count >= line.length()){
                //failsafe
                stop = true;
            }else if (line.charAt(count) != ' ') {
                //Adds character to StringBuilder if the character is not a space.
                data.append(line.charAt(count));
            }else if(count == line.length()-1) {
                //If the provisional index column is blank, append a '.' to the StringBuilder so that the data is compiled correctly
                data.append('.');
                stop = true;
            }else if(line.charAt(count) == ' ' && !data.isEmpty()){
                //stops the function once the function reaches another space.
                stop = true;
            }
            count++;
        }
     //   System.out.println("DATA: "+data);
        return data.toString();
    }

    public static ArrayList<Double>[] getValuesFromCsv(File file){
        try {
            ArrayList<Double>[] arr = new ArrayList[2];
            Scanner scan = new Scanner(file);
            ArrayList<Double> vals = new ArrayList<>();
            ArrayList<Double> yrAsDec = new ArrayList<>();
            scan.nextLine(); //So titles aren't taken as data
            while (scan.hasNextLine()){
                String line = scan.nextLine();
                String[] fields = line.split(",");
                if(fields.length == 4) {
                    yrAsDec.add(yrToDecYr(Integer.parseInt(fields[0]), Integer.parseInt(fields[1]), Integer.parseInt(fields[2])));
                    vals.add(Double.parseDouble(fields[3]));
                }
                }
            arr[0] = yrAsDec;
            arr[1] = vals;
            return arr;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double yrToDecYr(int year, int month, int  day){
        double decYear = year;
        if(year == 2012 || year == 2016 || year == 2020 || year == 2024){
            if(month == 2){
                decYear += 0.0847;
            } else if (month == 3) {
                decYear+=0.1639;
            } else if (month == 4) {
                decYear+=0.2486;
            } else if (month == 5) {
                decYear+=0.3306;
            } else if (month==6) {
                decYear+=0.4153;
            } else if (month==7) {
                decYear+=0.4973;
            } else if (month==8) {
                decYear+=0.5820;
            } else if (month==9) {
                decYear+=0.6667;
            } else if (month==10) {
                decYear+=0.7486;
            } else if (month==11) {
                decYear+= 0.8333;
            }else{
                decYear+=0.9153;
            }
        }else{
            if(month == 2){
                decYear += 0.0849;
            } else if (month == 3) {
                decYear+=0.1616;
            } else if (month == 4) {
                decYear+=0.2466;
            } else if (month == 5) {
                decYear+=0.3288;
            } else if (month==6) {
                decYear+=0.4137;
            } else if (month==7) {
                decYear+=0.4958;
            } else if (month==8) {
                decYear+=0.5808;
            } else if (month==9) {
                decYear+=0.6658;
            } else if (month==10) {
                decYear+=0.7479;
            } else if (month==11) {
                decYear+= 0.8329;
            }else{
                decYear+=0.9151;
            }
        }
        //Practically equal
        decYear+= day*0.0027;

        return decYear;
    }

    public static double[] arrListToArr(ArrayList<Double> arrList){
        double[] arr = new double[arrList.size()];
        for(int i = 0; i<arrList.size(); i++){
            arr[i] = arrList.get(i);
        }
        return arr;
    }



}