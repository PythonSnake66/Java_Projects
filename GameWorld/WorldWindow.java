import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

/**
 * Window that allows the user to visualize the CreatureWorld simulation
 * @author Paul Bouman
 *
 */

public class WorldWindow extends JFrame
{
	private static final long serialVersionUID = 3998702201167042838L;
	
	// The simulation controller
	private Controller controller;
	
	// The panel used for visualizing the world
	private WorldPanel worldPanel;
	
	// Interface Components
	private JButton step;
	private JButton animate;
	private JSpinner spinner;
	private JButton spawn;
	private JButton randomizeWealth;
	
	// If an animation is active it is performed by this Thread
	private Thread running;
	
	/**
	 * Creates a windows that visualizes a CreatureWorld simulation using the provided controller
	 * @param e the controller which manages the creature world simulation displayed
	 *          and controlled by this window
	 */
	public WorldWindow(Controller e)
	{
		super();
		setTitle("CreatureWorld Visualization");
		setSize(800,600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		controller = e;
		init();
	}
	
	/**
	 * Initializes GUI elements
	 */
	private void init()
	{
		setLayout(new BorderLayout());
		
		// The WorldPanel shows the visualization and should be central in the window
		worldPanel = new WorldPanel(controller.getWorld());
		add(worldPanel, BorderLayout.CENTER);
		worldPanel.updateWorld();
		
		// At the bottom we will contruct a panel with some button to control the simulation
		JPanel control = new JPanel();
		
		// Button for a single step
		step = new JButton("Step");
		step.addActionListener(step());
		control.add(step);
		
		// Buton to start an animation
		animate = new JButton("Start");
		animate.addActionListener(animate());
		control.add(animate);
		
		control.add(new JLabel("Sleep time (ms) : "));
		
		// Spinner to control the pauze between animation steps
		spinner = new JSpinner(new SpinnerNumberModel(400,0,10000,200));
		control.add(spinner);
		
		// Button for the purpose of spawning a random creature
		spawn = new JButton("Spawn Random Creature");
		spawn.addActionListener(spawn());
		control.add(spawn);
		
		// Button for the purpose of randomizing the wealth of the cells
		randomizeWealth = new JButton("Randomize Wealth");
		randomizeWealth.addActionListener(increaseWealth());
		control.add(randomizeWealth);
		
		// Adds the control panel to the bottom of the window
		add(control, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates an action listener that can be called by a button to start an animation
	 * @return an ActionListener
	 */
	private ActionListener animate()
	{
		// Create an anonymous inner class
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// If no Thread is active, start an animation
				if (running == null)
				{
					animate.setText("Stop");
					running = new AnimateThread();
					// Start the animation thread
					running.start();
					// disable all other buttons
					step.setEnabled(false);
					spawn.setEnabled(false);
					randomizeWealth.setEnabled(false);
				}
				// If a thread is active, disable the animation
				else
				{
					// set the instance variable to null to indicate the thread should stop
					running = null;
					// Set all butons back to normal
					animate.setText("Start");
					step.setEnabled(true);
					spawn.setEnabled(true);
					randomizeWealth.setEnabled(true);
				}
			}
		};
	}
	
	/**
	* Creates an action listener that can be called by a button to do one step of the simulation
	* @return the ActionListener 
	*/
	private ActionListener step()
	{
		// Create an anonymous inner class
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Let the controller update the states of the creatures and cells
				controller.step();
				// Let the WorldPanel redraw the state of the world
				worldPanel.updateWorld();		
			}
		};
	}
	
	/**
	 * Creates an action listener that can be called by a button to spawn a new creature
	 * @return the ActionListener
	 */
	private ActionListener spawn()
	{
		// Create an anonymous inner class
		return new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Let the controller spawn a random creature
				controller.createRandomCreature();
				// Let the WorldPanel redraw the state of the world
				worldPanel.updateWorld();
			}
		};
	}
	
	/**
	 * Creates an action listener that can be called by a button to randomize the wealth
	 * @return the ActionListener
	 */
	private ActionListener increaseWealth()
	{
		// Create an anonymous inner class
		return new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e)
			{
				// First clear all wealth
				controller.clearWealth();
				// Let the controller randomize the wealth
				controller.randomizeWealth(50);
				// Let the WorldPanel redraw the world
				worldPanel.updateWorld();
			}
		};
	}
	
	/**
	 * Inner class which extends Thread for the purpose of animating the simulation
	 * @author Paul Bouman
	 */
	private class AnimateThread extends Thread
	{
		public AnimateThread()
		{
			super();
			// Let this Thread be a Daemon, so that it will end if all other Threads have ended
			this.setDaemon(true);
		}
		
		@Override
		public void run()
		{
			while (running == this)
			{
				// Let the controller do a step
				controller.step();
				// Let the WorldPanel redraw the state of the world
				worldPanel.updateWorld();
				try
				{
					// Sleep for the specified amount of time
					int time = ((Number)spinner.getValue()).intValue();
					Thread.sleep(time);
				}
				catch (Exception e) {}
			}
		}
	}
	
}
