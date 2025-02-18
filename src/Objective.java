import java.util.ArrayList;
import java.util.List;

public class Objective {

    public int x,y;
    public boolean offersHelp;
    public ArrayList<Integer> helpOptions;

    public Objective(int x, int y, boolean offersHelp, ArrayList<Integer> helpOptions) {
        this.x = x;
        this.y = y;
        this.offersHelp = offersHelp;
        this.helpOptions = helpOptions;
    }
}
