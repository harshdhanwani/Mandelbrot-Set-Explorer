package model;

import guiDelegate.MandelbrotSetData;

import java.awt.*;
import java.util.ArrayDeque;
import java.util.Observable;
import java.util.Stack;

/**
 *  This is the simple class whose purpose is to store all the Mandelbrot Set details and perform Mandelbrot Set]
 *  update and reset functions.
 *
 *  The model extends the Observable class and is observed by the GUIDelegate class. This form
 *  of loose coupling permits the delegate (View) to be updated when the model has changed.
 *
 *  @author Student id: 170024238
 *
 *  Reference Source: https://studres.cs.st-andrews.ac.uk/CS5001/Examples/L11-13_GUIs/CS5001_SimpleSwing_MDGuiExample/src/model/SimpleModel.java
 */
public class MandelbrotModel extends Observable {

    private static final int X_RESOLUTION = 900;
    private static final int Y_RESOLUTION = 900;

    // Mandelbrot Data Information
    private int[][] mandelbrotInfo;
    private MandelbrotCalculator mMandelbrotCalculator;
    //X, Y screen resolution
    private int x_resolution, y_resolution;

    //Mandelbrot set parameters required for calculation
    private int maximumIterations;
    private double minumumReal, maximumReal, minimumImaginary, maximumImaginary, defaultRadiusSquared;

    private Color color;
    // Array required in order for the animation logic enhancement
    private ArrayDeque<MandelbrotSetData> animationFrames;
    //Stack used for Undo and Redo
    private Stack<MandelbrotSetData> stackUndo;
    private Stack<MandelbrotSetData> stackRedo;

    public Stack<MandelbrotSetData> getStackUndo() {
        return stackUndo;
    }
    public Stack<MandelbrotSetData> getStackRedo() {
        return stackRedo;
    }

    /**
     * Constructs a new MandelbrotModel instance.
     *
     * Initialises all the parameters required to calculate the Mandelbrot set
     * Initialises Stack for Undo and Redo. Also initialises the ArrayDeque for the
     * Animation frames to perform the animation enhancement.
     */
    public MandelbrotModel(){
        mMandelbrotCalculator = new MandelbrotCalculator();
        this.x_resolution = X_RESOLUTION;
        this.y_resolution = Y_RESOLUTION;
        this.maximumIterations = MandelbrotCalculator.INITIAL_MAX_ITERATIONS;
        this.minumumReal = MandelbrotCalculator.INITIAL_MIN_REAL;
        this.maximumReal = MandelbrotCalculator.INITIAL_MAX_REAL;
        this.minimumImaginary = MandelbrotCalculator.INITIAL_MIN_IMAGINARY;
        this.maximumImaginary = MandelbrotCalculator.INITIAL_MAX_IMAGINARY;
        this.defaultRadiusSquared = MandelbrotCalculator.DEFAULT_RADIUS_SQUARED;
        this.color = Color.WHITE;
        stackUndo = new Stack<>();
        stackRedo = new Stack<>();
        animationFrames = new ArrayDeque<>();

        this.mandelbrotInfo = mMandelbrotCalculator.calcMandelbrotSet(x_resolution,y_resolution,minumumReal,maximumReal,
                minimumImaginary, maximumImaginary, maximumIterations, defaultRadiusSquared);

    }


    public static int getxResolution() {
        return X_RESOLUTION;
    }

    public static int getyResolution() {
        return Y_RESOLUTION;
    }

    public int[][] getMandelbrotInfo() {
        return mandelbrotInfo;
    }

    public void setMandelbrotInfo(int[][] mandelbrotInfo) {
        this.mandelbrotInfo = mandelbrotInfo;
    }

    public int getX_resolution() {
        return x_resolution;
    }

    public void setX_resolution(int x_resolution) {
        this.x_resolution = x_resolution;
    }

    public int getY_resolution() {
        return y_resolution;
    }

    public void setY_resolution(int y_resolution) {
        this.y_resolution = y_resolution;
    }

    public int getMaximumIterations() {
        return maximumIterations;
    }

    public void setMaximumIterations(int maximumIterations) {
        this.maximumIterations = maximumIterations;
    }

    public double getMinumumReal() {
        return minumumReal;
    }

    public void setMinumumReal(double minumumReal) {
        this.minumumReal = minumumReal;
    }

    public double getMaximumReal() {
        return maximumReal;
    }

    public void setMaximumReal(double maximumReal) {
        this.maximumReal = maximumReal;
    }

    public double getMinimumImaginary() {
        return minimumImaginary;
    }

    public void setMinimumImaginary(double minimumImaginary) {
        this.minimumImaginary = minimumImaginary;
    }

    public double getMaximumImaginary() {
        return maximumImaginary;
    }

    public void setMaximumImaginary(double maximumImaginary) {
        this.maximumImaginary = maximumImaginary;
    }

    public double getDefaultRadiusSquared() {
        return defaultRadiusSquared;
    }

    public void setDefaultRadiusSquared(double defaultRadiusSquared) {
        this.defaultRadiusSquared = defaultRadiusSquared;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
    public ArrayDeque<MandelbrotSetData> getAnimationFrames() {
        return animationFrames;
    }

    public void setAnimationFrames(ArrayDeque<MandelbrotSetData> animationFrames) {
        this.animationFrames = animationFrames;
    }

    /**
     * Method to update the Mandelbrot Set by the other classes when the MandelbrotSetData object has to be passed in.
     *
     * @param mMandelbrotSetData Object of the MandelbrotSetData class.
     */
    public void updateMandelbrot(MandelbrotSetData mMandelbrotSetData){
        this.x_resolution = mMandelbrotSetData.getX_resolution();
        this.y_resolution = mMandelbrotSetData.getY_resolution();
        this.maximumIterations = mMandelbrotSetData.getMaximumIterations();
        this.minumumReal = mMandelbrotSetData.getMinumumReal();
        this.maximumReal = mMandelbrotSetData.getMaximumReal();
        this.minimumImaginary = mMandelbrotSetData.getMinimumImaginary();
        this.maximumImaginary = mMandelbrotSetData.getMaximumImaginary();
        this.defaultRadiusSquared = mMandelbrotSetData.getDefaultRadiusSquared();
        this.color = mMandelbrotSetData.getColor();

        this.mandelbrotInfo = mMandelbrotCalculator.calcMandelbrotSet(x_resolution,y_resolution,minumumReal,maximumReal,
                minimumImaginary,maximumImaginary,maximumIterations,defaultRadiusSquared);

        setChanged();
        notifyObservers();
    }

    /**
     * Method to simply update the Mandelbrot data set.
     */
    public void updateMandelbrot(){
        this.mandelbrotInfo = mMandelbrotCalculator.calcMandelbrotSet(x_resolution,y_resolution,minumumReal,maximumReal,
                minimumImaginary,maximumImaginary,maximumIterations,defaultRadiusSquared);

        setChanged();
        notifyObservers();
    }

    /**
     * Method to reset the Mandelbrot Set to its default initial values defined in the Mandelbrot Calculator class.
     *
     */
    public void resetMandelbrot(){
        this.x_resolution = X_RESOLUTION;
        this.y_resolution = Y_RESOLUTION;
        this.maximumIterations = MandelbrotCalculator.INITIAL_MAX_ITERATIONS;
        this.minumumReal = MandelbrotCalculator.INITIAL_MIN_REAL;
        this.maximumReal = MandelbrotCalculator.INITIAL_MAX_REAL;
        this.minimumImaginary = MandelbrotCalculator.INITIAL_MIN_IMAGINARY;
        this.maximumImaginary = MandelbrotCalculator.INITIAL_MAX_IMAGINARY;
        this.defaultRadiusSquared = MandelbrotCalculator.DEFAULT_RADIUS_SQUARED;
        this.color = Color.WHITE;
        stackUndo.clear();
        stackRedo.clear();
        animationFrames.clear();
        this.mandelbrotInfo = mMandelbrotCalculator.calcMandelbrotSet(x_resolution,y_resolution,minumumReal,maximumReal,
                minimumImaginary, maximumImaginary, maximumIterations, defaultRadiusSquared);

        setChanged();
        notifyObservers();
    }


}
