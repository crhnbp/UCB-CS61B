package creatures;
import huglife.Creature;
import huglife.Direction;
import huglife.Action;
import huglife.Occupant;
import huglife.HugLifeUtils;
import java.awt.Color;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    /** red color. */
    private int r;
    /** green color. */
    private int g;
    /** blue color. */
    private int b;

    /** creates clorus with energy equal to E. */
    public Clorus(double e) {
        super("clorus");
        r = 0;
        g = 0;
        b = 0;
        energy = e;
    }

    /** creates a clorus with energy equal to 1. */
    public Clorus() {
        this(1);
    }

    /** The color() method for Cloruses should always return the color red = 34, green = 0, blue = 231. */
    public Color color() {
        r = 34;
        b = 231;
        g = 0;
        return color(r, g, b);
    }

    /** If a Clorus attacks another creature, it should gain that creature's energy. */
    public void attack(Creature c) {
        energy += c.energy();
    }

    /** Cloruses should lose 0.03 units of energy on a MOVE action. */
    public void move() {
        energy -= 0.03;
    }


    /** Cloruses should lose 0.01 units of energy on a STAY action. */
    public void stay() {
        energy -= 0.01;
    }

    /** When a Clorus replicates, it keeps 50% of its energy.
     * The other 50% goes to its offspring.
     * No energy is lost in the replication process.
     */
    public Clorus replicate() {
        this.energy *= 0.5;
        return new Clorus(energy);
    }

    /** Cloruses take exactly the following actions based on NEIGHBORS:
     *  1. If there are no empty squares, the Clorus will STAY (even if there are Plips nearby they could attack).
     *  2. Otherwise, if any Plips are seen, the Clorus will ATTACK one of them randomly.
     *  3. Otherwise, if the Clorus has energy greater than or equal to one, it will REPLICATE to a random empty square.
     *  4. Otherwise, the Clorus will MOVE to a random empty square.
     */
    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empties = getNeighborsOfType(neighbors, "empty");
        if (empties.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else {
            List<Direction> plips = getNeighborsOfType(neighbors, "plip");
            if (plips.size() > 0) {
                Direction moveDir = HugLifeUtils.randomEntry(plips);
                return new Action(Action.ActionType.ATTACK, moveDir);
            }
            else if (energy >= 1) {
                Direction moveDir = HugLifeUtils.randomEntry(empties);
                return new Action(Action.ActionType.REPLICATE, moveDir);
            } else {
                Direction moveDir = HugLifeUtils.randomEntry(empties);
                return new Action(Action.ActionType.MOVE, moveDir);
            }
        }
    }
}
