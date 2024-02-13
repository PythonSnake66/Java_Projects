import java.util.List;

/**
 * This class extends the Creature abstract class and models carnivore behavior.
 *
 * @author 557824ad Alek Dimitrov
 */
public class Carnivore extends Creature {

    /**
     * Creates a carnivore. By default, carnivores have a sight of 2 and energy of 30.
     */
    public Carnivore() {
        super(2, 30);
    }

    /**
     * This method moves the carnivore to its preferred cell.
     */
    @Override
    public void move() {
        List<Cell> visibleCells = this.getVisibleCells();
        int mostHerbivoresCounter = 0;
        int largestSize = 0;
        Cell bestCarnivoreCell = this.getCurrentCell();
        for (Cell k : visibleCells) {
            if (k.getNumberOfHerbivores() >= mostHerbivoresCounter) {
                //We set the largest size to 0 if the number of herbivores in the new cell is strictly
                // higher than the number of herbivores in the best cell so far , as the largest
                // size of a herbivore in cell with less herbivores doesn't matter
                if (k.getNumberOfHerbivores() > mostHerbivoresCounter) {
                    largestSize = 0;
                    bestCarnivoreCell = k;
                    mostHerbivoresCounter = k.getNumberOfHerbivores();
                }

                for (Creature l : k.getCreatures()) {
                    if (l instanceof Herbivore && ((Herbivore) l).getSize() > largestSize) {
                        largestSize = ((Herbivore) l).getSize();
                        //It is necessary to set the bestCarnivoreCell equal to k INSIDE the for loop. This way, the method works
                        //correctly also when the number of herbivores in the best and current cell is equal and we do not have
                        //to create additional if statements for that case.
                        bestCarnivoreCell = k;
                    }
                }

            }
        }
        this.moveTo(bestCarnivoreCell);
    }

    /**
     * This method performs the carnivore's action (eat herbivores).
     */
    @Override
    public void act() {
        //We create this "fake" herbivore to eat which has size 1. This way, we can use the herbivore's getSize() function.
        //The boolean isThereHerbivore tracks whether there are any real herbivores within the visible cells.
        Herbivore herbivoreToEat = new Herbivore(1);
        boolean isThereHerbivore = false;
        for (int i = 0; i < this.getCurrentCell().getCreatures().size(); i++) {
            if (this.getCurrentCell().getCreatures().get(i) instanceof Herbivore) {
                isThereHerbivore = true;
                if (((Herbivore) this.getCurrentCell().getCreatures().get(i)).getSize() >= herbivoreToEat.getSize()) {
                    herbivoreToEat = (Herbivore) this.getCurrentCell().getCreatures().get(i);
                }
            }

        }
        if (!isThereHerbivore) {
            this.changeEnergy(-6);
        } else {
            this.changeEnergy(herbivoreToEat.getEnergy() - 6);
            herbivoreToEat.die();
        }
    }
}
