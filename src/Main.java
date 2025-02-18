import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    static int[][] map; // The grid representing the map
    static double[][] travelTimes; // Travel times between nodes
    static List<Objective> objectives; // List of objectives
    static int radius; // Line of sight radius
    static int currentX; // Current X position
    static int currentY; // Current Y position
    static StringBuilder stringBuilder=new StringBuilder();// Output text content

    public static void main(String[] args) throws IOException {
        // Read input files
        readLandFile(args[0]);
        readTravelTimeFile(args[1]);
        readMissionFile(args[2]);

        // Start navigating the map
        navigateMap();
        BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter("output.txt"));
        bufferedWriter.write(stringBuilder.toString());

    }

    static void readLandFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String[] sizes = br.readLine().split(" ");
            int rows = Integer.parseInt(sizes[0]);
            int cols = Integer.parseInt(sizes[1]);
            map = new int[rows][cols];

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int x = Integer.parseInt(parts[0]);
                int y = Integer.parseInt(parts[1]);
                int type = Integer.parseInt(parts[2]);
                map[x][y] = type;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void readTravelTimeFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            int size = map.length * map[0].length;
            travelTimes = new double[size][size];

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                String[] locations=parts[0].split(",");
                String[] firstNode=locations[0].split("-");
                int x1 = Integer.parseInt(firstNode[0]);
                int y1 = Integer.parseInt(firstNode[1]);
                String[] secondNode=locations[1].split("-");
                int x2 = Integer.parseInt(secondNode[0]);
                int y2 = Integer.parseInt(secondNode[1]);
                double time = Double.parseDouble(parts[1]); // Change to double

                travelTimes[x1 * map[0].length + y1][x2 * map[0].length + y2] = time;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Error parsing number: " + e.getMessage());
        }
    }

    static void readMissionFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            radius = Integer.parseInt(br.readLine());
            String[] startCoords = br.readLine().split(" ");
            currentX = Integer.parseInt(startCoords[0]);
            currentY = Integer.parseInt(startCoords[1]);
            objectives = new ArrayList<>();

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                int objX = Integer.parseInt(parts[0]);
                int objY = Integer.parseInt(parts[1]);
                boolean offersHelp = parts.length > 2;
                ArrayList<Integer> helpOptions = new ArrayList<>();

                if (offersHelp) {
                    for (int i = 2; i < parts.length; i++) {
                        helpOptions.add(Integer.parseInt(parts[i]));
                    }
                }
                objectives.add(new Objective(objX, objY, offersHelp, helpOptions));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void navigateMap(){

        for (Objective objective:objectives){
            int targetX=objective.x;
            int targetY=objective.y;

        }
    }

    static void Dijkstra(int startX, int startY, int targetX, int targetY,double[][] travelTimes, int[][] map, int radius){

        int rows = map.length;
        int cols = map[0].length;
        double[][] distances = new double[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        MyMinHeap<Node> myMinHeap=new MyMinHeap<>();

        for (double[] row : distances) Arrays.fill(row, Double.MAX_VALUE);
        distances[startX][startY] = 0;
        myMinHeap.insert(new Node(startX, startY, 0));
    }
}