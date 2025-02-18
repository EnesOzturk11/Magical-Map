import java.io.*;
import java.util.*;

public class Brain {
    static class Node implements Comparable<Node> {
        int x, y; // Coordinates
        double distance; // Distance from the source

        public Node(int x, int y, double distance) {
            this.x = x;
            this.y = y;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    static class Objective {
        int x, y;
        boolean offersHelp;
        List<Integer> helpOptions;

        public Objective(int x, int y, boolean offersHelp, List<Integer> helpOptions) {
            this.x = x;
            this.y = y;
            this.offersHelp = offersHelp;
            this.helpOptions = helpOptions;
        }
    }

    static int[][] map; // The grid representing the map
    static double[][] travelTimes; // Travel times between nodes
    static List<Objective> objectives; // List of objectives
    static int radius; // Line of sight radius
    static int currentX; // Current X position
    static int currentY; // Current Y position
    static int achieveNumber=1;

    public static void main(String[] args) {
        // Read input files
        readLandFile(args[0]);
        readTravelTimeFile(args[1]);
        readMissionFile(args[2]);

        // Start navigating the map
        navigateMap();
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

    static void navigateMap() {
        for (Objective obj : objectives) {
            List<String> pathLog = dijkstra(currentX, currentY, obj.x, obj.y, travelTimes, map);
            for (String log : pathLog) {
                System.out.println(log);
            }

            // Update
            // Update current position to the objective's position
            currentX = obj.x;
            currentY = obj.y;

            // Check if the wizard offers help
            if (obj.offersHelp) {
                int chosenOption = chooseHelpOption(obj);
                System.out.println("Number " + chosenOption + " is chosen!");
                // Change the type of the chosen node to 0 (passable)
                for (int x = 0; x < map.length; x++) {
                    for (int y = 0; y < map[0].length; y++) {
                        if (map[x][y] == obj.helpOptions.get(chosenOption)) {
                            map[x][y] = 0; // Make it passable
                        }
                    }
                }
            }
        }
    }

    static List<String> dijkstra(int startX, int startY, int targetX, int targetY,
                                 double[][] travelTimes, int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        double[][] distances = new double[rows][cols];
        boolean[][] visited = new boolean[rows][cols];
        PriorityQueue<Node> queue = new PriorityQueue<>();
        List<String> pathLog = new ArrayList<>();


        // Initialize distances to infinity
        for (double[] row : distances) Arrays.fill(row, Double.MAX_VALUE);
        distances[startX][startY] = 0;
        queue.add(new Node(startX, startY, 0));

        // Directions for adjacent nodes (up, down, left, right)
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            int x = current.x, y = current.y;

            // Skip already visited nodes
            if (visited[x][y]) continue;
            visited[x][y] = true;

            // Log movement
            pathLog.add("Moving to " + x + "-" + y);

            // If target is reached, log and return
            if (x == targetX && y == targetY) {
                pathLog.add("Objective "+achieveNumber+++" reached!");
                return pathLog;
            }

            // Explore neighbors
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];

                // Check bounds and passability
                if (newX >= 0 && newY >= 0 && newX < rows && newY < cols && map[newX][newY] == 0) {
                    double newDist = distances[x][y] + travelTimes[x * cols + y][newX * cols + newY];
                    if (newDist < distances[newX][newY]) {
                        distances[newX][newY] = newDist;
                        queue.add(new Node(newX, newY, newDist));
                    }
                }

            }
        }

        // If we exit the loop, the path is impassable
        pathLog.add("Path is impassable!");
        return pathLog;
    }

    static int chooseHelpOption(Objective obj) {
        int bestOption = -1;
        double bestDistance = Double.MAX_VALUE;

        // Store the original types of the nodes for restoration later
        int[] originalTypes = new int[obj.helpOptions.size()];

        for (int i = 0; i < obj.helpOptions.size(); i++) {
            int helpType = obj.helpOptions.get(i);
            // Store the original type
            originalTypes[i] = helpType;

            // Change the type to 0 (passable)
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    if (map[x][y] == helpType) {
                        map[x][y] = 0; // Make it passable
                    }
                }
            }

            // Calculate the distance to the objective with the modified map
            List<String> pathLog = dijkstra(currentX, currentY, obj.x, obj.y, travelTimes, map);
            double distance = calculateTotalDistance(pathLog);

            // Restore the original type
            for (int x = 0; x < map.length; x++) {
                for (int y = 0; y < map[0].length; y++) {
                    if (map[x][y] == 0) {
                        // Restore to original type if it was changed
                        for (int j = 0; j < originalTypes.length; j++)
                            if (originalTypes[j] == helpType) {
                                map[x][y] = helpType; // Restore original type
                            }
                    }
                }
            }

            // Check if this option is better
            if (distance < bestDistance) {
                bestDistance = distance;
                bestOption = i; // Store the index of the best option
            }
        }

        return bestOption; // Return the index of the best help option
    }

    static double calculateTotalDistance(List<String> pathLog) {
        double totalDistance = 0.0;
        for (String log : pathLog) {
            if (log.contains("Moving to")) {
                // For simplicity, we assume each move has a distance of 1
                totalDistance += 1; // Adjust this based on actual distances if available
            }
        }
        return totalDistance;
    }
}
