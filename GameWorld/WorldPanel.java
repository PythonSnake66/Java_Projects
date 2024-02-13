import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.JPanel;


/**
 * Visualization class for the Creature simulation
 * @author Paul Bouman
 */

public class WorldPanel extends JPanel
{
	// The diameter of a creature in pixels
	private static final int creatureSize = 15;
	
	// Colors to be used for different types of creatures. If more than six types are created,
	// this arrays needs to be extended.
	private static final Color [] colors = {Color.YELLOW, Color.RED, Color.BLUE,
											Color.ORANGE, Color.PINK, Color.CYAN};

	// Since JPanel implements the serializable class some random number should be added.
	private static final long serialVersionUID = -8647476667068294328L;

	// Map that assigns a creature type to a color
	private Map<Class<?>,Color> colorMap;
	
	// The world to simulate
	private World world;
	
	// The current drawing of the World state
	private BufferedImage currentImage;
	
	/**
	 * Constructor takes a world of which the current state will be drawn by this component
	 * @param w the world to be drawn
	 */
	public WorldPanel(World w)
	{
		super();
		world = w;
		colorMap = new LinkedHashMap<Class<?>,Color>();
		updateWorld();
	}
	
	/**
	 * Redefine the default painting behavior of a Panel such that the world state is drawn
	 */
	@Override
	public void paintComponent(Graphics g)
	{
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
	public void updateWorld()
	{
		int w = getWidth();
		int h = getHeight();
		if (w < 1 || h < 1)
		{
			return;
		}
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics g = newImage.getGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, w, h);
		drawCells(g);
		drawGrid(g);
		drawCreatures(g);
		drawLegend(g);
		currentImage = newImage;
		repaint();
	}
	
	/**
	 * Draws the grid according to the number of cells in the world
	 * @param gr the graphics to draw on
	 */
	private void drawGrid(Graphics gr)
	{
		gr.setColor(Color.BLACK);
		int cellSize = getCellSize();
		int boardHeight = cellSize * world.getHeight();
		int boardWidth = cellSize * world.getWidth();
		for (int col=0; col <= world.getWidth(); col++)
		{
			int x = col * cellSize;
			gr.drawLine(x, 0, x, boardHeight);
		}
		for (int row=0; row <= world.getHeight(); row++)
		{
			int y = row * cellSize;
			gr.drawLine(0, y, boardWidth, y);
		}
	}
	
	/**
	 * Draws the background of the cells based on the amount of wealth available in the cell
	 * @param gr the graphics to draw on
	 */
	private void drawCells(Graphics gr)
	{
		int cellSize = getCellSize();
		for (int x=0; x < world.getWidth(); x++)
		{
			for (int y=0; y < world.getHeight(); y++)
			{
				Cell c = world.getCell(x, y);
				gr.setColor(getWealthColor(c.getPlants()));
				gr.fillRect(x * cellSize, y * cellSize, cellSize, cellSize);
			}
		}
	}
	
	/**
	 * Draws creatures as filled circles with colors based on their types
	 * @param gr the graphics to draw on
	 */
	private void drawCreatures(Graphics gr)
	{
		for (int x = 0; x < world.getWidth(); x++)
		{
			for (int y=0; y < world.getHeight(); y++)
			{
				Cell cell = world.getCell(x, y);
				drawCreaturesInCell(gr,cell);
			}
		}
	}
	
	/**
	 * Helper function for drawing creatures. Draws all creatures in a single cell
	 * @param gr the graphics to draw one
	 * @param cell the cell for which to draw the creatures
	 */
	private void drawCreaturesInCell(Graphics gr, Cell cell)
	{
		int cellSize = getCellSize();
		int numCreatures = cellSize / creatureSize;
		int curBox = 0;
		int cellX = cell.getX() * cellSize;
		int cellY = cell.getY() * cellSize;
		for (Creature c : cell.getCreatures())
		{
			if (curBox < numCreatures*numCreatures)
			{
				Color color = getCreatureColor(c);
				gr.setColor(color);
			
				int row = curBox % numCreatures;
				int col = curBox / numCreatures;
				int locX = cellX + row * creatureSize;
				int locY = cellY + col * creatureSize;
				gr.fillOval(locX, locY, creatureSize, creatureSize);				
			}
			curBox ++;
		}
		
	}
	
	/**
	 * Draws a legend so that the viewer can see which color maps to which creature type
	 * @param g the graphics to draw on
	 */
	private void drawLegend(Graphics g)
	{
		Graphics2D gr = (Graphics2D) g;
		int height = gr.getFontMetrics().getHeight();
		int xStart = getCellSize() * world.getWidth() + 15;
		int curY = 15;
		for(Class<?> cl : colorMap.keySet())
		{
			String name = cl.getName();
			gr.setColor(colorMap.get(cl));
			gr.fillOval(xStart, curY, height, height);
			gr.setColor(Color.BLACK);
			gr.drawString(name, xStart + 2*height, curY + height);
			curY += 2*height;
		}
	}
	
	/**
	 * Computes the size of a single cell based on the width of the component
	 * @return the width and height of a cell (cells are square)
	 */
	private int getCellSize()
	{
		int maxWidth = getWidth() / world.getWidth();
		int maxHeight = getHeight() / world.getHeight();
		return Math.min(maxWidth, maxHeight);
	}
	
	
	/**
	 * Computes a color representing the amount of wealth in a cell
	 * @param wealth the current amount of wealth in the cell
	 * @return the color representing the amount of wealth
	 */
	private Color getWealthColor(int wealth)
	{
		double unitPerWealth = 255d / Cell.MAX_PLANTS;
		int shade = 255 - (int) Math.min(255, wealth * unitPerWealth);
		return new Color(shade,255,shade);
	}
	
	/**
	 * Get the color to draw the circle for a creature in. If no color has been assigned
	 * to a creature type yet, a new color will be picked for this creature.
	 * @param c the creature for which we want to know the color
	 * @return the color
	 */
	private Color getCreatureColor(Creature c)
	{
		Class<?> creatureClass = c.getClass();
		if (colorMap.containsKey(creatureClass))
		{
			return colorMap.get(creatureClass);
		}
		Color color = colors[colorMap.size() % colors.length];
		colorMap.put(creatureClass, color);
		return color;
	}
	
}
