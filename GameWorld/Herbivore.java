import java.util.Collections;
import java.util.List;

/**
 * This class extends the Creature abstract class and models herbivore behavior.
 *
 * @author 557824ad Alek Dimitrov
 */
public class Herbivore extends Creature {
    private final int size;

    /**
     * Creates a herbivore with the specified size. By default herbivores have a sight of 1, energy of 20 and positive size.
     *
     * @param size the size of the herbivore
     * @throws IllegalArgumentException if the size of the herbivore is less than, or equal to 0
     */
    public Herbivore(int size) throws IllegalArgumentException {
        super(1);
        if (size <= 0) {
            throw new IllegalArgumentException();
        }
        this.size = size;

    }

    /**
     * Provides the size of the herbivore
     *
     * @return the size of the herbivore
     */
    public int getSize() {
        return size;
    }

    /**
     * This method moves the herbivore to its preferred cell.
     */
    @Override
    public void move() {
        List<Cell> visibleCells = this.getVisibleCells();
        Collections.sort(visibleCells);
        this.moveTo(visibleCells.get(0));
    }

    /**
     * This method performs the herbivore's action (eat plants).
     */
    @Override
    public void act() {
        //Since size is an int variable, we do not need to round down in the following formula:
        int plantsEaten = Math.min(this.getCurrentCell().getPlants(), 2 * size * size / (1 + size));
        this.getCurrentCell().changePlants(-plantsEaten);
        this.changeEnergy(plantsEaten - size);
    }
}