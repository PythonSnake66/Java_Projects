import java.awt.*;

import java.awt.image.BufferedImage;
import javax.swing.*;


/**
 * Visualization class for the color version of Conway's Game of Life - draws the actual game on a panel
 *
 * @author Alek Dimitrov (557824ad) & Andrew Kolenchenko (560495ak) & Anna Mkheidze (568785am) &
 * Monica Panigrahy (493108mp) & Thomas Brandt (561285tb) & Virgjil Karaja (566701vk)
 */
public class ColorPanel extends JPanel
{
    // Since JPanel implements the serializable class some random number should be added.
    private static final long serialVersionUID = -8647476667068294328L;

    // The world to simulate
    private ColorGameOfLife game;

    // The current drawing of the game state
    private BufferedImage currentImage;



    /**
     * Constructor takes a world of which the current state will be drawn by this component
     * @param game the world to be drawn.
     */
    public ColorPanel(ColorGameOfLife game) {
        super();
        this.game = game;
        updateWorld();
    }

    /**
     * Redefine the default painting behavior of a Panel such that the world state is drawn
     * @param g the graphics that we wish to overwrite the currentImage with
     */
    @Override
    public void paintComponent(Graphics g) {
        if (currentImage == null)
        {
            updateWorld();
        }
        if (currentImage != null)
        {
            g.drawImage(currentImage, 0, 0, null);
        }
    }

    /**
     * Builds an image in memory to draw on the component
     */
    public void updateWorld() {
        int w = getWidth();
        int h = getHeight();
        if (w < 1 || h < 1) {
            return;
        }
        BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics g = newImage.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);
        drawCells(g);
        drawGrid(g);

        currentImage = newImage;
        repaint();
    }

    /**
     * Draws the grid according to the number of cells in the world
     * @param gr the graphics to draw on
     */
    private void drawGrid(Graphics gr) {
        gr.setColor(Color.BLACK);
        int[] cellSize = getCellSize();

        //Setting start and end points for the x/y dimensions to largely center the grid lines
        int startPointX = (getWidth() - cellSize[0]*game.getN()) / 2;
        int startPointY = (getHeight()- (cellSize[1]*game.getM())) /2 ;
        int endPointX = getWidth() - startPointX;
        int endPointY = getHeight() - startPointY;

        //Drawing the coloumn lines
        for (int col=0; col <= game.getN(); col++)
        {
            int x = col * cellSize[0] + startPointX;
            gr.drawLine(x, startPointY, x, endPointY);
        }
        //Drawing the row lines
        for (int row=0; row <= game.getM(); row++)
        {
            int y = row * cellSize[1] + startPointY;
            gr.drawLine(startPointX, y, endPointX, y);
        }
    }

    /**
     * Draws the background of the cells based on the color of the cell (or transparent if the cell is deat)
     * @param gr the graphics to draw on
     */
    private void drawCells(Graphics gr) {
        int cellSize[] = getCellSize();

        //Getting start points to center our cell grid
        int startPointX = (getWidth() - cellSize[0]*game.getN()) / 2;
        int startPointY = (getHeight()- (cellSize[1]*game.getM())) /2 ;

        for (int x=0; x < game.getN(); x++) {
            for (int y=0; y < game.getM(); y++) {

                ColorCell c = game.getCell(x, y);

                if (!c.isTransparent()) {
                    //If a cell is alive, the background is set to its color
                    gr.setColor(c.getColor());
                } else {
                    //If a cell is dead, it's transparent (alpha = 0, color doesn't matter)
                    Color transpWhite = new Color(255,255,255,0);
                    gr.setColor(transpWhite);
                }
                //Filling in the cell with the desired color
                gr.fillRect(x * cellSize[0]+startPointX, (y * cellSize[1])+startPointY, cellSize[0], cellSize[1]);
            }
        }
    }

    /**
     * Allows the panel to switch out which game is being played - helpful for dimension changes & resets
     * @param newGame The game that we wish to switch to
     */
    public void changeGame(ColorGameOfLife newGame){
        this.game = newGame;
        updateWorld();
    }

    /**
     * Computes the size of a single cell based on the width/height of the component
     * @return the width and height of a cell (cells are rectangular)
     */
    public int[] getCellSize() {
        int maxWidth = (int) Math.floor((getWidth()+0.0) / game.getN());
        int maxHeight = (int) Math.floor((getHeight() + 0.0) / game.getM());
        return new int[]{maxWidth, maxHeight};
    }
}

