import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /*
    Takes commands from the user and executes them. Possible commands are
    'read' - reads a file of weather data into the system
    'write' - writes weather data to a file -- overwrites the file if it exists
    'sort' - sorts weather data by the hottest to coldest average temperature
    'append' - writes weather data to a file -- appends data to the file if it exists
    'quit' - ends the program
     */
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<WeatherData> weatherData = null;
        while (true)
        {
            System.out.print("Enter a command: ");
            Scanner sc = new Scanner(System.in);

            String command = sc.next().toLowerCase();
            switch (command)
            {
                case "read":
                {
                    System.out.print("Enter the path to the file: ");
                    String path = sc.next();

                    weatherData = ReadFile(path);
                    PrintWeatherData(weatherData);
                    break;
                }
                case "sort":
                {
                    if (weatherData == null)
                    {
                        System.out.println("Please call 'read' first, before calling 'append'.");
                        break;
                    }

                    SortWeatherData(weatherData);
                    break;
                }
                case "write":
                {
                    if (weatherData == null)
                    {
                        System.out.println("Please call 'read' first, before calling 'write'.");
                        break;
                    }

                    System.out.print("Enter the path to the file: ");
                    String path = sc.next();

                    WriteFile(path, false, weatherData);
                    break;
                }
                case "append":
                {
                    if (weatherData == null)
                    {
                        System.out.println("Please call 'read' first, before calling 'append'.");
                        break;
                    }

                    System.out.print("Enter the path to the file: ");
                    String path = sc.next();

                    WriteFile(path, true, weatherData);
                    break;
                }
                case "quit":
                {
                    return;
                }
                default:
                {
                    System.out.println("Unrecognized command. Possible commands are 'read', 'write', 'append', 'sort', and 'quit'");
                    break;
                }
            }
        }
    }

    /*
    Reads a file from the given path and puts the information into an ArrayList.
    If the file does not exist, the function catches the exception, prints a message
    to the console, and return an empty (not null) array.
     */
    // CHECK!!!
    public static ArrayList<WeatherData> ReadFile(String path) throws FileNotFoundException
    {
        ArrayList<WeatherData> info = new ArrayList<>();
        File data = new File(path);
        if(data.isFile()){
            Scanner scan = new Scanner(data);
            while(scan.hasNext()) {
                String str = scan.nextLine();
                String city = str.substring(0, str.indexOf(", "));
                str = str.substring(str.indexOf(", ") + 1);
                double temp = Double.parseDouble(str.substring(0, str.indexOf(", ")));
                str = str.substring(str.indexOf(", ") + 1);
                double humidity = Double.parseDouble(str);
                WeatherData found = new WeatherData(city, temp, humidity);
                info.add(found);
            }
            return info;
        }
        throw new FileNotFoundException();
    }

    /*
    Prints the weather data ArrayList to the console. Each weather data item should
    go on a new line:

    [City1], [Average Temperature], [Average Humidity]
    [City2], [Average Temperature], [Average Humidity]
    ...
     */
    public static void PrintWeatherData(ArrayList<WeatherData> weatherData)
    {
       for(int i = 0; i<weatherData.size(); i++){
           for(WeatherData info : weatherData){
               System.out.println(info);
           }
           /*String city = weatherData.get(i).getCity();
           double avgTemp = weatherData.get(i).getAverageTemp();
           double avgHumidity = weatherData.get(i).getAverageHumidity();
           System.out.println(city + ", " + avgTemp + ", " + avgHumidity);

            */
       }
    }

    /*
    Sorts the given ArrayList from hottest average temperature to coldest average temperature
     */
    public static void SortWeatherData(ArrayList<WeatherData> weatherData)
    {
        for(int i = 1; i< weatherData.size(); i++){
            WeatherData list = weatherData.get(i);
            int j = i;
            while(j>0 && weatherData.get(j-1).getAverageTemp()<list.getAverageTemp()){
                weatherData.set(j, weatherData.get(j-1));
                j--;
            }
            weatherData.set(j, list);
        }
    }

    /*
    Writes the weather data information into the file with the given path.
    If shouldAppend is false, the function replaces the existing contents of the file
    (if it exists) with the new weatherData. If shouldAppend is true, the function
    adds the weather data to the end of the file.
    If the file cannot be created, the function catches the exception, prints a message
    to the console, and does not try to write to the file.
     */
    public static void WriteFile(String path, boolean shouldAppend, ArrayList<WeatherData> weatherData) throws FileNotFoundException
    {
        File data = new File(path);
        if(!data.isFile()){
            throw new FileNotFoundException("Does not exist.");
        }
        else{
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(path, shouldAppend));//(note to self) check this line
            for(WeatherData info : weatherData){
                printWriter.write(info.toString());
            }
            printWriter.close();;
        }
    }
}
