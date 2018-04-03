package guiDelegate;

import model.MandelbrotModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Observable;
import java.util.Observer;
import java.util.Stack;

/**
 * The GUIDelegate class whose purpose is to render relevant state information stored in the model and make changes
 * to the model state based on user events.
 *
 * This class uses Swing to display the model state when the model changes. This is the view aspect of the delegate class.
 * It also listens for user input events (in the listeners defined below), translates these to appropriate calls to methods
 * defined in the model class so as to make changes to the model. This is the controller aspect of the delegate class.
 * The class implements Observer in order to permit it to be added as an observer of the model class.
 * When the model calls notifyObservers() (after executing setChanged())
 * the update(...) method below is called in order to update the view of the model.
 *
 * @author Student Id: 170024238
 *
 * Reference Source : https://studres.cs.st-andrews.ac.uk/CS5001/Examples/L11-13_GUIs/CS5001_SimpleSwing_MDGuiExample/src/guiDelegate/SimpleGuiDelegate.java
 *
 */
public class GUIDelegate implements Observer{

    private static final int FRAME_HEIGHT = 900;
    private static final int FRAME_WIDTH = 900;
    private static final int TEXT_HEIGHT = 12;
    private static final int TEXT_WIDTH = 12;

    private JFrame mainJFrame;
    private JToolBar jToolBar;
    private JButton btnChangeColor, btnUndo, btnRedo, btnReset, btnUpdate;
    private JTextField inputIterations;
    private JTextArea output_field;
    private JMenuBar jMenuBar;
    JLabel iterationsLabel;
    JPopupMenu colorPopup;

    private Stack<MandelbrotSetData> stackUndo;
    private Stack<MandelbrotSetData> stackRedo;

    private MandelbrotModel mandelbrotModel;
    private JPanelHelperClass jPanelHelperClass;

    private ArrayDeque<MandelbrotSetData> animationFrames;

    /**
     * Instantiate a new GUIDelegate object
     * @param mandelbrotModel the Model to observe, render, and update according to user events
     */
    public GUIDelegate(MandelbrotModel mandelbrotModel){
        this.mandelbrotModel = mandelbrotModel;
        //setting up the main frame for the GUI
        this.mainJFrame = new JFrame();
        jMenuBar = new JMenuBar();
        jToolBar = new JToolBar();
        inputIterations = new JTextField(TEXT_WIDTH);
        output_field = new JTextArea(TEXT_WIDTH, TEXT_HEIGHT);
        output_field.setEditable(false);

        // Calling the initComponents() method to initialise all the GUI components.
        initComponents();

        // initialising the stack used for Undo and Redo and the ArrayDeque reuired for Animations
        stackUndo = mandelbrotModel.getStackUndo();
        stackRedo = mandelbrotModel.getStackRedo();

        // ENHANCEMENT 3 - Animations
        animationFrames = mandelbrotModel.getAnimationFrames();

        //add the delegate UI component as an observer of the model so as to detect changes in the model and update the
        //GUI view accordingly
        mandelbrotModel.addObserver(this);
    }

    /**
     * Initialises the toolbar to contain the buttons, label, input field, etc. and adds the toolbar to the main frame.
     * Listeners are created for the buttons and text field which translate user events to model object method calls (controller aspect of the delegate)
     */
    public void initToolbar(){

        // Listener for Undo button
        btnUndo = new JButton("Undo");
        btnUndo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveToRedoStack();
                mandelbrotModel.updateMandelbrot(stackUndo.pop());
            }
        });
        btnUndo.setEnabled(false);

        // Listener for Reset button
        btnReset = new JButton("Reset");
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            mandelbrotModel.resetMandelbrot();
            }
        });

        // Listener for Redo button
        btnRedo = new JButton("Redo");
        btnRedo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                stackUndo.push(new MandelbrotSetData(mandelbrotModel));
                mandelbrotModel.updateMandelbrot(stackRedo.pop());
            }
        });
        btnRedo.setEnabled(false);

        iterationsLabel = new JLabel("Number of Iterations: ");

        // Listener for input interactions text field button
        inputIterations.addKeyListener(new KeyListener() {  // to translate key event for the text filed into appropriate model method call
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    btnUpdate.doClick();
                }
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        // Basic requirement: Permit the user to alter the value used for maxIterations so as to enhance the precision od the Mandelbrot image when zooming as they desire.
        btnUpdate = new JButton("Update");
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try{
                    saveToUndoStack();
                    mandelbrotModel.setMaximumIterations(Integer.parseInt(inputIterations.getText()));
                    mandelbrotModel.updateMandelbrot();
                    inputIterations.setText(""); // clear the input box in the GUI view
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        // Listener for the pop up when clicked on Change Color button.
         colorPopup = new JPopupMenu();
         colorPopup.add(new JMenuItem(new AbstractAction("RED") {
             @Override
             public void actionPerformed(ActionEvent actionEvent) {
               saveToUndoStack();
                mandelbrotModel.setColor(Color.RED);
                mandelbrotModel.updateMandelbrot();
             }
         }));

        colorPopup.add(new JMenuItem(new AbstractAction("GREEN") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveToUndoStack();
                mandelbrotModel.setColor(Color.GREEN);
                mandelbrotModel.updateMandelbrot();

            }
        }));

        colorPopup.add(new JMenuItem(new AbstractAction("BLUE") {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                saveToUndoStack();
                mandelbrotModel.setColor(Color.BLUE);
                mandelbrotModel.updateMandelbrot();
            }
        }));



        // Listener for Change Color button
        btnChangeColor = new JButton("Change color");
        btnChangeColor.addMouseListener(new MouseListener() {
           @Override
           public void mouseClicked(MouseEvent mouseEvent) {

           }

           @Override
           public void mousePressed(MouseEvent mouseEvent) {
               colorPopup.show(mouseEvent.getComponent(), mouseEvent.getX(), mouseEvent.getY());

           }

           @Override
           public void mouseReleased(MouseEvent mouseEvent) {

           }

           @Override
           public void mouseEntered(MouseEvent mouseEvent) {

           }

           @Override
           public void mouseExited(MouseEvent mouseEvent) {

           }
       });

        // adding all components to the toolbar
        jToolBar.add(btnChangeColor);
        jToolBar.add(btnReset);
        jToolBar.add(btnUndo);
        jToolBar.add(btnRedo);
        jToolBar.add(iterationsLabel);
        jToolBar.add(inputIterations);
        jToolBar.add(btnUpdate);
        mainJFrame.add(jToolBar, BorderLayout.NORTH);
    }

    /**
     * ENHANCEMENT 2 - Save and Load - permit parameter settings and potentially the computed image to be saved and
     * loaded to/from file thereby permitting a saved image to be re-loaded and the user to continue exploring
     * the Mandelbrot set from that position onwards.
     * Sets up File menu with Load and Save entries
     * The Load and Save actions would normally be translated to appropriate model method calls similar to the way the code does this
     * above in @see #setupToolbar().
     */
    public void initMenu(){
        JMenu file = new JMenu ("File");
        JMenuItem load = new JMenuItem ("Load");
        JMenuItem save = new JMenuItem ("Save");
        file.add(load);
        file.add(save);
        jMenuBar.add (file);

        // Listener to Load a file
        load.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // should call appropriate method in model class if you want it to do something useful
               JFileChooser jFileChooser = new JFileChooser();
               File currentDirectory = new File(System.getProperty("user.dir"));
               jFileChooser.setCurrentDirectory(currentDirectory);

               int openFileSelector = jFileChooser.showOpenDialog(jFileChooser);
               if (openFileSelector == JFileChooser.APPROVE_OPTION){
                   File f = jFileChooser.getSelectedFile();

                   try{
                       saveToUndoStack();

                       FileInputStream fileInputStream = new FileInputStream(f);
                       ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

                       mandelbrotModel.updateMandelbrot((MandelbrotSetData) objectInputStream.readObject());
                       objectInputStream.close();
                       fileInputStream.close();

                   } catch (Exception exception){
                       exception.printStackTrace();
                   }
               }
            }
        });

        //Listener to Save a file to the current directory
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                // should call appropriate method in model class if you want it to do something useful

                JFileChooser jFileChooser = new JFileChooser();
                File currentDirectory = new File(System.getProperty("user.dir"));
                jFileChooser.setCurrentDirectory(currentDirectory);

                int openFileSelector = jFileChooser.showOpenDialog(jFileChooser);
                if (openFileSelector == JFileChooser.APPROVE_OPTION){
                    File f = jFileChooser.getSelectedFile();
                    try{
                        FileOutputStream fileOutputStream = new FileOutputStream(f);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(new MandelbrotSetData(mandelbrotModel));
                        objectOutputStream.close();
                        fileOutputStream.close();
                    } catch (Exception exception){
                        exception.printStackTrace();
                    }
                }

            }
        });
        // add menubar to frame
        mainJFrame.setJMenuBar(jMenuBar);
    }

    /**
     * Method to setup the menu and toolbar components
     */
    public void initComponents(){
        initMenu();
        initToolbar();
        jPanelHelperClass = new JPanelHelperClass(mandelbrotModel);
        jPanelHelperClass.setBackground(Color.WHITE);

        mainJFrame.add(jPanelHelperClass, BorderLayout.CENTER);
        mainJFrame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        mainJFrame.setVisible(true);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    /**
     * Save recent MandelbrotSetData to the Stack to perform Undo functionality
     *
     */
    public void saveToUndoStack(){
        stackUndo.push(new MandelbrotSetData(mandelbrotModel));
        stackRedo.clear();
    }

    /**
     * Save recent MandelbrotSetData to the Stack to perform Redo functionality
     *
     */
    public void saveToRedoStack(){
        stackRedo.push(new MandelbrotSetData(mandelbrotModel));
    }


    /**
     * This method contains code to update the GUI view when the model changes
     * The method is called when the model changes (i.e. when the model executes setChanged() and notifyObservers())
     * Any parameters passed to notifyObservers @see java.util.Observer#update(java.util.Observable, java.lang.Object)
     * are passed to update.
     * The code in update should get hold of the model state it requires and update the relevant GUI components so that
     * an updated view of the model is displayed on screen.
     * For this simple example, the only state information we need from the model is what is in the model's text buffer and the
     * only GUI view element we need to update is the text area used for output.
     *
     * NOTE: In a more complex program, the model may hold information on a variety of objects, such as various shapes, their positions, etc.
     * and the GUI view would then have to get hold of all that state info and produce a graphical representation of theses objects.
     * As a result, the update method would have to get hold of various bits of model state and then
     * call the relevant methods (defined in the GUI code) to render the objects.
     *
     */
    @Override
    public void update(Observable observable, Object o) {
        SwingUtilities.invokeLater(new Runnable(){
            public void run(){

                if (!stackUndo.isEmpty()){
                    btnUndo.setEnabled(true);
                } else {
                    btnUndo.setEnabled(false);
                }
                if (!stackRedo.isEmpty()){
                    btnRedo.setEnabled(true);
                } else {
                    btnRedo.setEnabled(false);
                }
               jPanelHelperClass.repaint();

                // updates the mandelbrot set according to the ANIMATION frame size.
                if (animationFrames.size() > 0){
                    mandelbrotModel.updateMandelbrot(animationFrames.remove());
                }
            }
        });
    }
}
