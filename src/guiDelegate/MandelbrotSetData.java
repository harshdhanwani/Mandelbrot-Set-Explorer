package guiDelegate;

import model.MandelbrotModel;

import java.awt.*;
import java.io.Serializable;

/**
 *  This is the simple class whose purpose is to only store and get all the Mandelbrot Set Data.
 *
 *  The model extends the Serializable class.
 *
 *  @author Student id: 170024238
 *
 *  Reference Source: https://studres.cs.st-andrews.ac.uk/CS5001/Examples/L11-13_GUIs/CS5001_SimpleSwing_MDGuiExample/src/model/SimpleModel.java
 */
public class MandelbrotSetData implements Serializable{

    private int x_resolution, y_resolution;
    private int maximumIterations;
    private double minumumReal, maximumReal, minimumImaginary, maximumImaginary, defaultRadiusSquared;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private Color color;

    /**
     * Constructor that gets all the data from the Model class
     *
     * Initialises all the parameters required in the calculation of Mandelbrot set
     *
     * @param mMandelbrotModel object of the Mandelbrot Model class
     */
    public MandelbrotSetData(MandelbrotModel mMandelbrotModel){
        x_resolution = mMandelbrotModel.getX_resolution();
        y_resolution = mMandelbrotModel.getY_resolution();
        maximumIterations = mMandelbrotModel.getMaximumIterations();
        minumumReal = mMandelbrotModel.getMinumumReal();
        maximumReal = mMandelbrotModel.getMaximumReal();
        minimumImaginary = mMandelbrotModel.getMinimumImaginary();
        maximumImaginary = mMandelbrotModel.getMaximumImaginary();
        defaultRadiusSquared = mMandelbrotModel.getDefaultRadiusSquared();
        color = mMandelbrotModel.getColor();
    }

    /**
     * Constructor to clone the MandelbrotSetData class
     *
     * Initialises all the parameters required in the calculation of Mandelbrot set
     *
     * @param mandelbrotSetData object of the MandelbrotSetData class
     */
    public MandelbrotSetData(MandelbrotSetData mandelbrotSetData){
        x_resolution = mandelbrotSetData.getX_resolution();
        y_resolution = mandelbrotSetData.getY_resolution();
        maximumIterations = mandelbrotSetData.getMaximumIterations();
        minumumReal = mandelbrotSetData.getMinumumReal();
        maximumReal = mandelbrotSetData.getMaximumReal();
        minimumImaginary = mandelbrotSetData.getMinimumImaginary();
        maximumImaginary = mandelbrotSetData.getMaximumImaginary();
        defaultRadiusSquared = mandelbrotSetData.getDefaultRadiusSquared();
        color = mandelbrotSetData.getColor();
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


}
