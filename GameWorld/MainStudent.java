import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MainStudent
{

	public static void main(String [] args)
	{
		//testWorld();
		//testCell();
		//testCreature();
		//testHerbivore();
		//testCarnivore();
		//System.out.println(4/3);

	}
	
	public static void testWorld()
	{
		// Testing width and height
		World w = new World(5,3);
		System.out.println(w.getWidth() == 5);
		System.out.println(w.getHeight() == 3);

		// Testing cell coordinates
		System.out.println(w.getCell(1,2).getX() == 1);
		System.out.println(w.getCell(1,2).getY() == 2);

		// Testing if the cells are persistent
		System.out.println(w.getCell(1,1) == w.getCell(1,1));

		// Testing getCellList()
		List<Cell> cells = w.getCellList();
		System.out.println(cells.size() == w.getWidth() * w.getHeight());
		System.out.println(cells.contains(w.getCell(0,0)));
		System.out.println(cells.contains(w.getCell(4,0)));
		System.out.println(cells.contains(w.getCell(0,2)));
		System.out.println(cells.contains(w.getCell(4,2)));

		// Testing getCreatures()
		Carnivore c1 = new Carnivore();
		Carnivore c2 = new Carnivore();
		c1.moveTo(w.getCell(1,2));
		c2.moveTo(w.getCell(2,1));
		List<Creature> allCreatures = w.getCreatures();
		System.out.println(allCreatures.size() == 2);
		System.out.println(allCreatures.contains(c1));
		System.out.println(allCreatures.contains(c2));
	}
	
	public static void testCell() {
		// Testing Cell Order
		World w = new World(4,1);
		Cell a = w.getCell(0,0);
		Cell b = w.getCell(1,0);
		Cell c = w.getCell(2,0);
		Cell d = w.getCell(3,0);
		a.changePlants(20);
		b.changePlants(20);
		new Carnivore().moveTo(b);
		c.changePlants(30);
		new Carnivore().moveTo(c);
		new Carnivore().moveTo(c);
		new Carnivore().moveTo(c);
		d.changePlants(30);
		new Carnivore().moveTo(d);
		new Carnivore().moveTo(d);

		// Sorting the cells according to their natural order
		List<Cell> cells = Arrays.asList(a,b,c,d);
		Collections.sort(cells);

		// Check if the final indices are correct
		System.out.println(a == cells.get(2));
		System.out.println(b == cells.get(3));
		System.out.println(c == cells.get(1));
		System.out.println(d == cells.get(0));	
	}
	
	public static void testCreature() {
		// Check the default stats of a creature using a new Herbivore 
		Herbivore h = new Herbivore(1);
		System.out.println(h.getSight() == 1);
		System.out.println(h.getEnergy() == 20);
		System.out.println(!h.isAlive());
		System.out.println(h.getCurrentCell() == null);

		// Construct a World to check if we can put creatures on it
		World w = new World(5,5);
		Cell c = w.getCell(2, 2);
		h.moveTo(c);
		System.out.println(h.isAlive());
		System.out.println(h.getCurrentCell() == c);
		System.out.println(c.getCreatures().contains(h));
		
		// Checks if the cells at range one are considered visibile
		List<Cell> visible = h.getVisibleCells();
		System.out.println(visible.contains(h.getCurrentCell()));
		System.out.println(visible.contains(w.getCell(1,1)));
		System.out.println(visible.contains(w.getCell(3,3)));
		System.out.println(visible.contains(w.getCell(1,3)));
		System.out.println(visible.contains(w.getCell(3,1)));
		
		System.out.println(!visible.contains(w.getCell(0,0)));
		System.out.println(!visible.contains(w.getCell(4,4)));
		System.out.println(!visible.contains(w.getCell(4,0)));
		System.out.println(!visible.contains(w.getCell(0,4)));
		System.out.println(!visible.contains(w.getCell(2,4)));
		System.out.println(!visible.contains(w.getCell(4,2)));
		
		// Check if a creature behaves correctly when it dies
		h.die();
		System.out.println(!h.isAlive());
		System.out.println(h.getCurrentCell() == null);
		System.out.println(!c.getCreatures().contains(h));
	}
	
	public static void testHerbivore() {
		// Testing Cell Order
		World w = new World(4,2);
		Cell start = w.getCell(1, 0);
		// Initialize Herb the Herbivore
		Herbivore herb = new Herbivore(3);
		herb.moveTo(start);
		// Is the size of Herb stored correctly?
		System.out.println(herb.getSize() == 3);
		
		// Set up some cells to which Herb should move
		Cell a = w.getCell(0,0);
		Cell b = w.getCell(0,1);
		Cell c = w.getCell(1,1);
		Cell d = w.getCell(2,0);
		Cell e = w.getCell(3,0);
		a.changePlants(20);
		b.changePlants(20);
		new Herbivore(1).moveTo(b);
		c.changePlants(30);
		new Herbivore(1).moveTo(c);
		new Herbivore(1).moveTo(c);
		new Herbivore(1).moveTo(c);
		// Cell d will be the best choice, as it has only two herbivores
		d.changePlants(30);
		new Herbivore(1).moveTo(d);
		new Herbivore(1).moveTo(d);
		// Cell e would be best, but it won't be in range
		e.changePlants(50);
		
		// Let Herb move and check if it moves to the correct cell
		herb.move();
		System.out.println(herb.getCurrentCell() == d);
		// Let Herb act, and check if the new numbers are correct
		herb.act();
		System.out.println(herb.getEnergy() == 21);
		System.out.println(d.getPlants() == 30-4);		
	}
	
	public static void testCarnivore() {
		// Build a small world for a Carnivore
		World w = new World(6,1);
		Cell cell;
		cell = w.getCell(0, 0);
		new Herbivore(10).moveTo(cell);
		cell = w.getCell(1, 0);
		new Herbivore(7).moveTo(cell);
		new Herbivore(7).moveTo(cell);
		cell = w.getCell(3, 0);
		new Herbivore(50).moveTo(cell);
		// This cell is the best according to the rule
		cell = w.getCell(4, 0);
		Herbivore eatMe = new Herbivore(10);
		eatMe.moveTo(cell);
		Herbivore dontEat = new Herbivore(1); 
		dontEat.moveTo(cell);
		// This cell would be the best if it wasn't out of sight
		cell = w.getCell(5, 0);
		new Herbivore(60).moveTo(cell);
		new Herbivore(60).moveTo(cell);
		new Herbivore(60).moveTo(cell);
		
		// Create the Carnivore
		Carnivore c = new Carnivore();
		System.out.println(c.getSight() == 2);
		System.out.println(c.getEnergy() == 30);
		cell = w.getCell(2, 0);
		// Move the Carnivore
		c.moveTo(cell);
		c.move();
		// Check if the Carnivore selected the best option

		System.out.println(c.getCurrentCell() == w.getCell(4, 0));

		cell = w.getCell(4, 0);
		// Check if Carnivore behaves correctly when it acts
		c.act();
		System.out.println(c.getEnergy() == 44);
		System.out.println(cell.getCreatures().contains(dontEat));
		System.out.println(cell.getCreatures().contains(c));
		System.out.println(!cell.getCreatures().contains(eatMe));
		System.out.println(!eatMe.isAlive());		
	}
	
}
