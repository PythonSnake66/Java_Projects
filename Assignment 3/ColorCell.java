
import java.awt.*;

/**
 * Class that describes the status of each cell of a color version of Conway's Game of Life
 *
 * @author Alek Dimitrov (557824ad) & Andrew Kolenchenko (560495ak) & Anna Mkheidze (568785am) &
 * Monica Panigrahy (493108mp) & Thomas Brandt (561285tb) & Virgjil Karaja (566701vk)
 */
public class ColorCell {
    //Cell home game
    private ColorGameOfLife game;

    //Cell coordinates
    private Integer x;
    private Integer y;

    //Cell color and living state
    private Color c;
    private boolean transparent;

    /**
     * Constructor for a cell, stores the cell's home game, coordinates, color, and state (alive/dead)
     * @param game The game that the specific cell belongs to.
     * @param x The row location of the cell.
     * @param y The column location of the cell.
     * @param c The color object that cell possesses at its birth / creation.
     * @param transparent the boolean that indicates transparency (dead/alive)
     */
    public ColorCell(ColorGameOfLife game, Integer x, Integer y, Color c, boolean transparent) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.c = c;
        this.transparent = transparent;
    }

    /**
     * Method to retrieve the x coordinate of a cell
     * @return The row the cell is located on.
     */
    public Integer getX() {
        return x;
    }

    /**
     * Method to retrieve the y coordinate of a cell
     * @return The column the cell is located on.
     */

    public Integer getY() {
        return y;
    }

    /**
     * Method to retrieve the color of a cell
     * @return The color of the cell.
     */
    public Color getColor(){
        return c;
    }

    /**
     * Method to retrieve state (dead/alive) of a cell (with transparent cells being dead)
     * @return boolean indicating is the cell is transparent.
     */
    public boolean isTransparent(){
        return transparent;
    }

    /**
     * Method to retrieve the game that a cell belongs to
     * @return The game that the cell is located in.
     */
    public ColorGameOfLife getGame() {
        return game;
    }
}
