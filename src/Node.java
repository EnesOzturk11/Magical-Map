public class Node implements Comparable<Node>{

    public int x, y; // Coordinates
    public double distance; // Distance from the source

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
