import java.util.ArrayList;
import java.util.List;

/**
 * This abstract class models general creature actions.
 *
 * @author 557824ad Alek Dimitrov
 */
public abstract class Creature {
    public final int sight;
    private int currentEnergy;
    private Cell currentCell;
    private List<Cell> visibleCells = new ArrayList<>();

    /**
     * This constructor models a creature. Since by default all Herbivores have 20 energy, this constructor
     * will only be used for creating herbivores.
     *
     * @param sight the sight of the creature
     */
    public Creature(int sight) {
        this.sight = sight;
        this.currentCell = null;
        this.currentEnergy = 20;
    }

    /**
     * We perform a constructor overloading, as in the question it is initially said that every creature
     * should have 20 energy. However, that is not the case for the Carnivore class, so
     * there must be a different constructor for it. We use this constructor only for creating carnivores.
     *
     * @param sight  the sight of the creature
     * @param energy the energy of the creature
     */
    public Creature(int sight, int energy) {
        this.sight = sight;
        this.currentCell = null;
        this.currentEnergy = energy;
    }

    /**
     * This method mimics creature movement. It adds the creature to a certain cell newCell, and removes
     * the creature from its past cell (if a past cell existed).
     *
     * @param newCell the cell to which the creature will move
     */
    public final void moveTo(Cell newCell) {
        if (currentCell == null) {
            newCell.addCreature(this);
            currentCell = newCell;
        } else {
            currentCell.removeCreature(this);
            newCell.addCreature(this);
            currentCell = newCell;
        }
    }

    /**
     * Provides the cell in which the creature is located.
     *
     * @return the cell in which the creature is located
     */
    public final Cell getCurrentCell() {
        return this.currentCell;

    }

    /**
     * This abstract method is used by the Herbivore and Carnivore class to determine
     * to which cell within vision range will they move to.
     */
    public abstract void move();

    /**
     * This abstract method is used by the Herbivore and Carnivore class to perform
     * their respective actions
     */
    public abstract void act();

    /**
     * Provides the sight of the creature
     *
     * @return the sight of the creature
     */
    public final int getSight() {

        return this.sight;
    }

    /**
     * This method removes the creature from the world
     */
    public final void die() {
        if (currentCell != null) {
            currentCell.removeCreature(this);
            currentCell = null;
        }

    }

    /**
     * This method shows whether the creature is alive. By default, creatures without a current cell are
     * not considered alive.
     *
     * @return true/false depending on whether the creature is alive
     */
    public final boolean isAlive() {
        if (currentCell == null) {
            return false;
        }
        return true;
    }

    /**
     * Provides the current energy of the creature
     *
     * @return the current energy of the creature
     */
    public final int getEnergy() {
        return this.currentEnergy;
    }

    /**
     * This method changes the energy of the creature by the specified amount.
     *
     * @param amount the amount of energy to be changed
     */
    public final void changeEnergy(int amount) {
        this.currentEnergy += amount;
        if (this.currentEnergy <= 0) {
            this.die();
        }
    }

    /**
     * Provides the list of all visible cells from the creature's current location.
     *
     * @return the list of all visible cells from the creature's current location
     */
    public final List<Cell> getVisibleCells() {
        for (int i = -sight; i <= sight; i++) {
            for (int j = -sight; j <= sight; j++) {
                if (currentCell.exists(currentCell.getX() + i, currentCell.getY() + j)) {
                    visibleCells.add(currentCell.getWorld().getCell(currentCell.getX() + i, currentCell.getY() + j));
                }
            }
        }
        return visibleCells;

    }
}