import java.util.ArrayList;
import java.util.List;

/**
 * This class models the matrix, in which creatures will move and act. The matrix is
 * composed of Cell and in each cell, there can be a number of plants and/or creatures.
 *
 * @author 557824ad Alek Dimitrov
 */
public class World {
    private final int width;
    private final int height;
    private List<Cell> allCells = new ArrayList<>();

    /**
     * This constructor stores the width and height of the world matrix.
     * It also creates all the cells inside the matrix.
     *
     * @param w the width of the matrix
     * @param h the height of the matrix
     */
    public World(int w, int h) {
        this.width = w;
        this.height = h;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                allCells.add(new Cell(this, j, i));
            }
        }

    }

    /**
     * Provides the width of the matrix.
     *
     * @return the width of the matrix
     */
    public int getWidth() {
        return width;
    }

    /**
     * Provides the height of the matrix.
     *
     * @return the height of the matrix
     */
    public int getHeight() {
        return height;
    }

    /**
     * Provides the cell with certain x- and y-coordinates.
     *
     * @param x - the x-coordinate of the needed cell
     * @param y - the y-coordinate of the needed cell
     * @return the cell that is positioned in (x,y)
     * @throws IllegalArgumentException if the coordinates fall outside of the boundaries of the matrix
     */
    public Cell getCell(int x, int y) throws IllegalArgumentException {
        if (x > width - 1 || x < 0 || y > height - 1 || y < 0) {
            throw new IllegalArgumentException();
        }
        return allCells.get(width * y + x);
    }

    /**
     * Provides the list of all creatures in the matrix.
     *
     * @return the list of all creatures in the matrix
     */
    public List<Creature> getCreatures() {
        List<Creature> currentCreatures = new ArrayList<>();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                for (int k = 0; k < this.getCell(j, i).getCreatures().size(); k++) {
                    currentCreatures.add(this.getCell(j, i).getCreatures().get(k));
                }
            }
        }
        return currentCreatures;
    }

    /**
     * Provides the list of all cell in the matrix.
     *
     * @return the list of all cells in the matrix
     */
    public List<Cell> getCellList() {

        List<Cell> currentCells = new ArrayList<>();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                currentCells.add(this.getCell(j, i));
            }
        }
        return currentCells;
    }

}