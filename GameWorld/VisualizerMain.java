
public class VisualizerMain
{
	public static void main(String[] args)
	{
		World world = new World(10, 10);
		Controller controller = new Controller(world, 1234);
		for (int t = 0; t < 1; t++) {
			controller.createRandomCreature();
		}
		controller.randomizeWealth(50);
		WorldWindow ww = new WorldWindow(controller);
		ww.setVisible(true);
	}
}
