import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class keeps, updates and monitors the behaviours of the individual cells.
 *
 * @author 557824ad Alek Dimitrov
 */
public class Cell implements Comparable<Cell> {
    public static final int MAX_PLANTS = 100;
    private int plants;
    private final int x;
    private final int y;
    private final World assignedWorld;
    private List<Creature> creatures = new ArrayList<>();

    /**
     * This constructor creates a cell, that is in a world w, and has the x- and y-coordinates x and y respectively.
     *
     * @param w the world in which the cell is going to exist
     * @param x the x-coordinate of the cell
     * @param y the y-coordinate of the cell
     */
    public Cell(World w, int x, int y) {
        this.plants = 0;
        this.x = x;
        this.y = y;
        this.assignedWorld = w;
    }

    /**
     * This method checks whether a cell with certain x-and y-coordinates exists.
     *
     * @param xCoordinate the x-coordinate of the cell
     * @param yCoordinate the y-coordinate of the cell
     * @return true/false depending on whether there is a cell with the above-mentioned x- and y-coordinates
     */
    public boolean exists(int xCoordinate, int yCoordinate) {
        if (xCoordinate > assignedWorld.getWidth() - 1 || xCoordinate < 0 || yCoordinate > assignedWorld.getHeight() - 1 || yCoordinate < 0) {
            return false;
        }
        return true;
    }

    /**
     * Provides the x-coordinate of the cell.
     *
     * @return the x-coordinate of the cell
     */
    public int getX() {
        return x;
    }

    /**
     * Provides the y-coordinate of the cell.
     *
     * @return the y-coordinate of the cell
     */
    public int getY() {
        return y;
    }

    /**
     * Provides the world (matrix) of the cell.
     *
     * @return the world (matrix) of the cell
     */
    public World getWorld() {
        return assignedWorld;
    }

    /**
     * Provides the number of plants within the cell.
     *
     * @return the number of plants within the cell
     */
    public int getPlants() {
        return plants;
    }

    /**
     * This method changes the number of plants within the cell. The maximum number is 100, the minimum is 0.
     *
     * @param amount the amount of plants to be added/subtracted from the cell
     * @throws IllegalArgumentException if the number of plants in the cell becomes less than 0
     */
    public void changePlants(int amount) throws IllegalArgumentException {
        if (this.getPlants() + amount < 0) {
            throw new IllegalArgumentException();
        }
        if (plants + amount > MAX_PLANTS) {
            plants = MAX_PLANTS;
        } else {
            plants += amount;
        }

    }

    /**
     * Provides the number of herbivores in the cell
     *
     * @return the number of herbivores int the cell
     */
    public int getNumberOfHerbivores() {
        int numberOfHerbivores = 0;
        for (Creature k : this.getCreatures()) {
            if (k instanceof Herbivore) {
                numberOfHerbivores++;
            }
        }
        return numberOfHerbivores;
    }

    /**
     * Adds a creature to the cell.
     *
     * @param c the creature to be added to the cell
     */
    public void addCreature(Creature c) {
        creatures.add(c);
    }

    /**
     * Removes a creature from the cell.
     *
     * @param c the creature to be removed from the cell
     */
    public void removeCreature(Creature c) {
        creatures.remove(c);

    }

    /**
     * Provides an unmodifiable list of all the creatures in the cell.
     *
     * @return the list of all the creatures in the cell
     */
    public List<Creature> getCreatures() {
        return Collections.unmodifiableList(creatures);
    }

    /**
     * Defines a natural ordering of the cells. This ordering is based on the cell preferences of the
     * Herbivore class when using the move() method. If the the this object comes before the cell object received as a parameter
     * in terms of sorting order, the method should return a negative number. If, on the other hand, the this object comes after
     * the object received as a parameter, the method should return a positive number. Otherwise, 0 is returned.
     * Source: Week 4.6.Ordered Objects
     *
     * @param o the cell object with which the this object is going to be compared
     * @return a positive or negative number or 0, which will determine the ordering of the cell objects
     */
    @Override
    public int compareTo(Cell o) {
        if (this.getPlants() != o.getPlants()) {
            return o.getPlants() - this.getPlants();
        }
        return this.getCreatures().size() - o.getCreatures().size();
    }
}