import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

import javax.swing.*;

/**
 * Class that describes the overall GUI of a color version of Conway's Game of Life
 *
 * @author Alek Dimitrov (557824ad) & Andrew Kolenchenko (560495ak) & Anna Mkheidze (568785am) &
 * Monica Panigrahy (493108mp) & Thomas Brandt (561285tb) & Virgjil Karaja (566701vk)
 */
public class ConwayWindow extends JFrame{
    //private static final long serialVersionUID = 3998702201167042838L; ------------ Need this?

    //The game being displayed
    private ColorGameOfLife game;

    // The panel used for visualizing the game state
    private ColorPanel colorPanel;

    // Interface Components
    private JButton updateSize;
    private JButton step;
    private JButton animate;
    private JButton reset;
    private JSpinner spinner;
    private JSpinner colorspin;


    // If an animation is active it is performed by this Thread
    private Thread running;

    /**
     * Constructor for our GUI, begins with a set (though adjustable) size of 800x600
     * @param e The game of life that we wish to display in our GUI
     */
    public ConwayWindow(ColorGameOfLife e)
    {
        super();
        setTitle("Game of Life Visualization");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        game = e;
        init();
    }

    /**
     * Initializes GUI elements
     */
    private void init()
    {
        setLayout(new BorderLayout());

        // The ColorPanel shows the visualization and should be central in the window
        colorPanel = new ColorPanel(game);
        int row = game.getM(); //Setting up the dimensions
        int col = game.getN();
        colorPanel.updateWorld(); // the updating of the panel
        int cellSize[] = colorPanel.getCellSize();

        colorPanel.setLayout(new GridLayout(row, col));
        
        Color transpBlack = new Color(0,0,0,0);

        //Adding clickable invisible buttons overtop the game grid
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                JPanel pan = new JPanel();

                pan.setEnabled(true);
                pan.setBackground(transpBlack);

                pan.setMaximumSize(new Dimension(cellSize[0], cellSize[1]));

                pan.setBorder(BorderFactory.createLineBorder(transpBlack));
                pan.addMouseListener(new BoxListener()); // add a mouse listener to make the panels clickable
                pan.setName(i+","+j);
                colorPanel.add(pan);
            }
        }
        //Placing the game depiction in the center of the window
        add(colorPanel, BorderLayout.CENTER);

        //At the bottom we construct a panel with some button to control the simulation
        JPanel control = new JPanel();

        //Button to update the size of the game to the desired dimensions
        updateSize = new JButton("Update Size:");
        updateSize.addActionListener(updateSize());
        control.add(updateSize);

        //Spinner of options for side length
        spinner = new JSpinner(new SpinnerNumberModel(10,2,100,1));
        control.add(spinner);

        // Button for a single step
        step = new JButton("Step");
        step.addActionListener(step());
        control.add(step);


        // Button to start an animation
        animate = new JButton("Start");
        animate.addActionListener(animate());
        control.add(animate);

        //Button to wipe all living cells off the current grid
        reset = new JButton("Reset");
        reset.addActionListener(reset());
        control.add(reset);

        //Available colors (including dead) for the user to add
        String colors[] = {"Black", "Dead", "White", "Yellow",
                "Red", "Blue", "Green","Orange", "Magenta", "Pink"};

        colorspin = new JSpinner(new SpinnerListModel(colors));
        Component spinEdSpawn= colorspin.getEditor();
        JFormattedTextField jfSpawn= ((JSpinner.DefaultEditor) spinEdSpawn).getTextField();
        jfSpawn.setColumns(8);
        control.add(colorspin);

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
                    running = new ConwayWindow.AnimateThread();
                    // Start the animation thread
                    running.start();
                    // disable all other buttons
                    step.setEnabled(false);
                    colorspin.setEnabled(false);
                    reset.setEnabled(false);
                    updateSize.setEnabled(false);

                }
                // If a thread is active, disable the animation
                else
                {
                    // set the instance variable to null to indicate the thread should stop
                    running = null;
                    // Set all buttons back to normal
                    animate.setText("Start");
                    step.setEnabled(true);
                    colorspin.setEnabled(true);
                    reset.setEnabled(true);
                    updateSize.setEnabled(true);

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

                game.nextGeneration();
                // Let the ColorPanel redraw the state of the world
                colorPanel.updateWorld();
            }
        };
    }

    /**
     * Creates an action listener that can be called by a button to reset the simulation to a blank slate.
     * @return the ActionListener
     */
    private ActionListener reset()
    {
        // Create an anonymous inner class
        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                int M = game.getM();
                int N = game.getN();
                ColorGameOfLife newGame = new ColorGameOfLife(M,N);

                game = newGame;
                // Let the ColorPanel redraw the state of the world
                colorPanel.changeGame(newGame);
            }
        };
    }

    /**
     * Creates an action listener that can be called by a button to update the game size to the desired dimensions.
     * @return the ActionListener
     */
    private ActionListener updateSize()
    {
        // Create an anonymous inner class
        return new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {



                int M = (int) spinner.getValue();
                int N = (int) spinner.getValue();
                ColorGameOfLife newGame = new ColorGameOfLife(M,N);

                game=newGame;
                // Let the ColorPanel redraw the state of the world
                colorPanel.changeGame(newGame);
                int row = M;
                int col = N;
                colorPanel.updateWorld();
                int cellSize[] = colorPanel.getCellSize();


                colorPanel.removeAll();
                colorPanel.revalidate();
                colorPanel.repaint();
                colorPanel.setLayout(new GridLayout(row, col));


                //Initializing a transparent (dead) color
                Color transpBlack = new Color(0,0,0,0);


                for (int i = 0; i < row; i++) {
                    for (int j = 0; j < col; j++) {
                        JPanel pan = new JPanel();

                        pan.setEnabled(true);
                        pan.setBackground(transpBlack);
                        pan.setMaximumSize(new Dimension(cellSize[0], cellSize[1]));

                        pan.setBorder(BorderFactory.createLineBorder(transpBlack));
                        pan.addMouseListener(new BoxListener()); // add a mouse listener to make the panels clickable
                        pan.setName(i+","+j);
                        colorPanel.add(pan);
                    }
                }
                colorPanel.updateWorld();
            }
        };
    }

    /**
     * Inner class which extends Thread for the purpose of animating the simulation
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

                game.nextGeneration();
                // Let the ColorPanel redraw the state of the world
                colorPanel.updateWorld();
                try
                {
                    // Sleep for the specified amount of time
                    int time = 400;
                    Thread.sleep(time);
                }
                catch (Exception e) {}
            }
        }
    }

    /**
     * Inner class which extends MouseAdapter, detecting when one of the squares in the grid of life has been clicked
     */
    private class BoxListener extends MouseAdapter
    {
        public void mouseClicked(MouseEvent me)
        {
            JPanel clickedBox = (JPanel) me.getSource(); // get the reference to the box that was clicked


            String[] indices = clickedBox.getName().split(",");

            Color newColor = new Color(0,0,0);
            boolean transparent = false;

            //Checking the color that the use wishes to set the new cell to
            if(colorspin.getValue().equals("Dead")){
                transparent = true;
            } else{

                String colorToSet = colorspin.getValue()+"";
                colorToSet = colorToSet.toUpperCase();

                try{
                    //The user selected color is used as a method by the Java Color class
                    Field field = Class.forName("java.awt.Color").getField(colorToSet);
                    newColor = (Color)field.get(null);
                }
                catch (IllegalAccessException e){
                    System.out.println("Access Illegal!");
                }
                catch(NoSuchFieldException e){
                    System.out.println("Invalid Field!");
                }
                catch(ClassNotFoundException e){
                    System.out.println("Invalid Class!");
                }
            }
            //Actually updating the new cell
            game.updateSingleCell(Integer.parseInt(indices[1]),Integer.parseInt(indices[0]), newColor, transparent);

            //Redrawing what needs to be redrawn
            colorPanel.updateWorld();
        }
    }

}

