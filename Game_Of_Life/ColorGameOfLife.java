import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Class that represents a game state of a color version of Conway's Game of Life
 *
 * @author Alek Dimitrov (557824ad) & Andrew Kolenchenko (560495ak) & Anna Mkheidze (568785am) &
 * Monica Panigrahy (493108mp) & Thomas Brandt (561285tb) & Virgjil Karaja (566701vk)
 */
public class ColorGameOfLife {

    //The game dimensions
    private final Integer M;
    private final Integer N;

    //The current cells that populate the game
    private List<List<ColorCell>> listOfLists;

    /**
     * Constructor that creates a game of the given size, with a default of no living cells
     * @param M Number of rows in the game matrix.
     * @param N Number of columns in the game matrix.
     */
    public ColorGameOfLife(Integer M, Integer N) {
        this.M = M;
        this.N = N;

        this.listOfLists = new ArrayList<>(); //We also initialize a list of colors which is an empty list for now.

        int[][] twoDimenArray = new int[M][N]; //We also initialize one empty matrix that matches the dimensions specified.
        Integer[][] result = Stream.of(twoDimenArray)
                .map(array -> IntStream.of(array).boxed().toArray(Integer[]::new))
                .toArray(Integer[][]::new);

        //Filling list of cells with dead cells
        createCells(result);
    }

    /**
     * Method to retrieve the grid height
     * @return The number of rows.
     */
    public Integer getM() {
        return this.M;
    }

    /**
     * Method to retrieve the grid width
     * @return The number of columns.
     */
    public Integer getN() {
        return this.N;
    }


    /**
     * Method to find the ColorCell present at the given coordinates
     * @param x the column number of the cell we want.
     * @param y the column number of the cell we want.
     * @return The cell that is positioned on coordinate X,Y
     */
    public ColorCell getCell(Integer x, Integer y) {
        return listOfLists.get(x).get(y);
    }

    /**
     * A method that allows the simple initialization of a (black/transparent) game state
     * @param grid the grid of living/dead cells we wish to initialize
     */
    public void createCells(Integer[][] grid) {

        listOfLists.clear(); //Clear up the list from previous iteration.
        for (int i = 0; i < M; i++) { // for every row element
            List<ColorCell> cellList = new ArrayList<>(); // We create a list of colors for it.
            cellList.clear();

            //Starting color is black but transparent (appears white on a white background)
            Color startCol = Color.black;
            for (int j = 0; j < N; j++) {
                if (grid[i][j] == 1){ //if we decided to color the cell it will not be transparent 
                    ColorCell cell = new ColorCell(this, i, j, startCol, false);

                    cellList.add(cell);
                } else { //If we decided not to put color it will be transparent.
                    ColorCell cell = new ColorCell(this, i, j, startCol, true);

                    cellList.add(cell);
                }
            }
            listOfLists.add(cellList); // we do this for every row and thus fill the whole grid.
        }
    }

    /**
     *Method to iterate and update the game state according to the rules of the Game of Life
     */
    public void nextGeneration() {

        List<List<ColorCell>> newGameState = new ArrayList<>(); //We create a new empty list that shows the state of the game.
        List<ColorCell> newCellList = new ArrayList<>(); //List for the new grid of cells
        ArrayList<ColorCell> currentLivingNeighbors = new ArrayList<>(); //List for living neighbours of each cell

        for (int l = 0; l < M; l++) {
            newCellList.clear(); //For every row the list will start anew
            for (int m = 0; m < N; m++) {
                //Finding no. of Neighbours that are alive
                currentLivingNeighbors.clear();
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        //Checks all adjacent cells (that actually exist)
                        if ((l + i >= 0 && l + i < M) && (m + j >= 0 && m + j < N)) {

                            ColorCell neighborCell = listOfLists.get(l + i).get(m + j);

                            //If a neighbor cell is alive, it is added to the list of neighbors
                            if (!(neighborCell.isTransparent()) && ((i!=0)||(j!=0))){
                                currentLivingNeighbors.add(neighborCell);
                            }
                        }
                    }
                }
                int aliveNeighbours = currentLivingNeighbors.size();


                // Implementing the Rules of Life
                if ((!(listOfLists.get(l).get(m).isTransparent())) && (aliveNeighbours < 2)) {
                    //Cell is lonely and dies
                    //Create a new dead cell
                    ColorCell cell = new ColorCell(this, l, m, Color.white, true);
                    newCellList.add(cell);

                }else if ((!(listOfLists.get(l).get(m).isTransparent())) && (aliveNeighbours > 3)) {
                    //Cell is overcrowded and dies
                    ColorCell cell = new ColorCell(this, l, m, Color.white, true);
                    newCellList.add(cell);

                } else if ((listOfLists.get(l).get(m).isTransparent()) && (aliveNeighbours == 3)) {
                    //Dead cell springs to life
                    //Find average color of neighbors
                    int totRed = 0;
                    int totGreen = 0;
                    int totBlue = 0;

                    for (int i = 0; i < 3; i++) {
                        totRed += currentLivingNeighbors.get(i).getColor().getRed();
                        totGreen += currentLivingNeighbors.get(i).getColor().getGreen();
                        totBlue += currentLivingNeighbors.get(i).getColor().getBlue();
                    }
                    //Creating a new color as the average of parent colors
                    Color newColor = new Color(totRed / 3, totGreen / 3, totBlue / 3);

                    //Creating new cell with the average color
                    ColorCell cell = new ColorCell(this, l, m, newColor, false);

                    newCellList.add(cell);
                }else {
                    //If a cell does not die or spring to life, it stays the same
                    ColorCell cell = listOfLists.get(l).get(m);
                    newCellList.add(cell);
                }
            }
            newGameState.add(new ArrayList<>(newCellList));
        }
            //Call an update of the whole game state
            updateCells(newGameState);
        }

    /**
     * Sets the game's record of the game state to be the provided game state
     * @param newGameState The new list of lists with cells that we wish to set as our game state
     */
    public void updateCells(List<List<ColorCell>> newGameState){
        listOfLists.clear(); // The old list is updated with the new one.
        listOfLists = newGameState;
    }


    /**
     * Method to update a single cell without iterating through the game - performs no checks on other cells
     * @param i row number
     * @param j column number
     * @param c new color of cell
     * @param transparent if new cell is dead (transparent)
     */
    public void updateSingleCell(int i, int j, Color c, boolean transparent){
        ColorCell newCell = new ColorCell(this, i, j, c, transparent); //Creating a cell with the given inputs.
        listOfLists.get(i).set(j, newCell); // Replacing the old one with new.
    }
}
