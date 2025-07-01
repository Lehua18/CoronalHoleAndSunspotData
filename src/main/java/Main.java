//Sunspot Data Imports
import java.io.File;
import java.io.FileNotFoundException;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

//DONKI Data Imports
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

//Other Imports
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Objects;

public class Main {
    private static int count;
    private Map<String, double[]> vars = new HashMap<>();

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
        double[] yrAsDec = new double[allData.size()];
        double[] sunspotNum = new double[allData.size()];
        //and others?
        for (int i = 0; i < allData.size(); i++) {
            yrAsDec[i] = allData.get(i).getYrAsDec();
        }
        for (int j = 0; j < allData.size(); j++) {
            sunspotNum[j] = allData.get(j).getSunspotNum();
        }
        //and others?

        //***!!!Get and Store DONKI Data!!!***
        Scanner scan = new Scanner(System.in);
        String timeKey = "";
        String eventType = "";
        do {
            System.out.println("Please enter the type of event you would like to view:");
            eventType = scan.nextLine();
            //to get correct time key depending on event. Also functions to validate user entry
            timeKey = getTimeString(eventType);
        } while (timeKey.isEmpty());

        //Get data endpoints
        System.out.println("Please enter a start date in the form yyyy-MM-dd");
        String startDate = scan.nextLine();
        System.out.println("Please enter an end date in the form yyyy-MM-dd");
        String endDate = scan.nextLine();

        //creates URL to get JSON data
        String url = "https://kauai.ccmc.gsfc.nasa.gov/DONKI/WS/get/" + eventType + "?startDate=" + startDate + "&endDate=" + endDate;
        JSONArray array = URLToJSON(url);

        //***!!!Get User Input***!!!

        boolean stop = false;
        int dims;
        do {
            System.out.println("How many dimensions would you like your graph to be?");
            dims = Integer.parseInt(scan.nextLine());
            if (dims == 2 || dims == 3) {
                stop = true;
            }
        } while (!stop);
        System.out.println("Here are the currently available quantities:");
        //for loop to print all those guys
        System.out.println("Please choose a quantity to plot on the x axis.");
        String xAxisString = scan.nextLine();
        System.out.println("Please choose a quantity to plot on the y axis.");
        String yAxisString = scan.nextLine();
        if (dims == 3) {
            System.out.println("Please choose a quantity to plot on the z axis.");
            String zAxisString = scan.nextLine();
        }

        //Graph Data
        try {
            Grapher grapher = new Grapher(yrAsDec, sunspotNum);
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

    public static String getTimeString(String eventType) {
        String timeKey = "";
        if (eventType.equals("CME") || eventType.equals("GST")) {
            timeKey = "startTime";
        } else if (eventType.equals("IPS") || eventType.equals("SEP") || eventType.equals("MPC") || eventType.equals("RBE") || eventType.equals("HSS")) {
            timeKey = "eventTime";
        } else if (eventType.equals("FLR")) {
            timeKey = "beginTime";
//        } else if (eventType.equals("ALL")) {
//            timeKey = "ALL";
        } else {
            System.out.println("You have not entered a valid event type, or something else went wrong. Please try again");
        }
        return timeKey;
    }

    public static JSONArray URLToJSON(String URLString){
        try {
            //Convert string to URL
            URI uri = URI.create(URLString);
            URL url = uri.toURL();

            //Connect and pull data from URL and store in String
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                String newJSONString = response.toString();

                //convert String back to JSON Array
                try {
                    return new JSONArray(newJSONString);
                } catch (JSONException e) {
                    System.out.println("JSON creation failed: "+e);
                    return null;
                }

            } else {
                System.out.println("GET request failed. Response code: " + responseCode);
                return null;
            }

        } catch (IOException e) {
            e.printStackTrace(); /*Okay because this is not production software*/
            return null;
        }
    }
}