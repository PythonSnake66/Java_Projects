import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Class that controls and performs simulation steps
 *
 * @author Paul Bouman
 */
public class Controller {
    private World world;
    private Random random;

    /**
     * Constructor which creates a simulation based on a current World state.
     * Random events performed by this simulator are based on a random seed
     *
     * @param w    The world to perform a simulation with
     * @param seed The seed to be used for Random events
     */
    public Controller(World w, long seed) {
        world = w;
        random = new Random(seed);
    }

    /**
     * Getter for obtaining the current state of the world
     *
     * @return the current state of the world
     */
    public World getWorld() {
        return world;
    }

    /**
     * Creates a new Human or a new Zombie at a random spot.
     * Modify this method if you want to play around with the simulator.
     */
    public void createRandomCreature() {
        int x = random.nextInt(world.getWidth());
        int y = random.nextInt(world.getHeight());
        Cell cell = world.getCell(x, y);
        Creature creature;
        if (random.nextDouble() < 0.05) {
            creature = new Carnivore();
        } else {
            creature = new Herbivore(random.nextInt(3) + 1);
        }
        creature.moveTo(cell);
    }

    /**
     * Add either at most n wealth to every cell.
     * Modify this method if you want to play around
     * with the simulation.
     *
     * @param n the maximum amount of Wealth to add
     */
    public void randomizeWealth(int n) {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCell(x, y);
                cell.changePlants(random.nextInt(n));
            }
        }
    }

    /**
     * Clears all wealth in the world
     */
    public void clearWealth() {
        for (int x = 0; x < world.getWidth(); x++) {
            for (int y = 0; y < world.getHeight(); y++) {
                Cell cell = world.getCell(x, y);
                cell.changePlants(-cell.getPlants());
            }
        }
    }


    /**
     * Perform a step in the simulation
     */
    public void step() {
        List<Creature> creatures = world.getCreatures();
        Collections.shuffle(creatures);
        // Let all creatures act
        for (Creature c : creatures) {
            // It is possible that due to an act of another creature
            // by the time the iterator reaches this creature it is not alive any more.
            // Therefore we check if it is still alive
            if (c.isAlive()) {
                c.act();
            }
        }
        // Let all creatures move
        for (Creature c : creatures) {
            if (c.isAlive()) {
                c.move();
            }
        }
    }
}
