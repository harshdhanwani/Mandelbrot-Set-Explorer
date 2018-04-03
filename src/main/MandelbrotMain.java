package main;

import guiDelegate.GUIDelegate;
import model.MandelbrotModel;

/**
 *  This is the main class that runs the Model-Delegate example as a stand-alone GUI Application.
 *
 *  @author Student id: 170024238
 *
 *  Reference Source: https://studres.cs.st-andrews.ac.uk/CS5001/Examples/L11-13_GUIs/CS5001_SimpleSwing_MDGuiExample/src/main/SimpleSwingMain.java
 */
public class MandelbrotMain {

    public static void main(String[] args) {
        // Passing the model to the delegate, so that it can observe, display and change the model
        new GUIDelegate(new MandelbrotModel());
    }
}
