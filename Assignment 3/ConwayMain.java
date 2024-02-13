/**
 * Main class to call our Conway's Game of Life GUI and display it
 *
 * @author Alek Dimitrov (557824ad) & Andrew Kolenchenko (560495ak) & Anna Mkheidze (568785am) &
 * Monica Panigrahy (493108mp) & Thomas Brandt (561285tb) & Virgjil Karaja (566701vk)
 */
public class ConwayMain {
    /**
     * Main method to initialize our game of life, builds a 10x10 game of empty cells and displays it
     * @param args unused argument strings
     */
    public static void main(String[] args) {
        int M = 10, N = 10;

        ColorGameOfLife game = new ColorGameOfLife(M, N);
        ConwayWindow ww = new ConwayWindow(game);
        ww.setVisible(true);
}
}