package guiDelegate;

import model.MandelbrotModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayDeque;
import java.util.Stack;

/**
 * A simple Panel helper class whose purpose is to support the functionality of all events taking place on the panel,
 * such as any Mouse click or Mouse Listener events ( Painting Components, Zoom, Dragging boundary while zooming,
 * implement animation functionality)
 *
 * @author Student Id: 170024238
 *
 */
public class JPanelHelperClass extends JPanel implements MouseListener, MouseMotionListener{

    private MandelbrotModel mMandelbrotModel;
    int[][] mandelbrotInfo;
    int maximumIterations;
    public int coordinateX1, coordinateX2, coordinateY1, coordinateY2;

    private Stack<MandelbrotSetData> stackUndo;
    private Stack<MandelbrotSetData> stackRedo;

    // simple boolean value used in the implementation of zoom feature.
    private boolean selectionToZoom;

    private ArrayDeque<MandelbrotSetData> animationFrames;

    //Default animation frames value
    private static int DEFAULT_ANIMATION_FRAMES = 15;

    /**
     * Instantiate a new JPanelHelperClass object
     *
     * @param mMandelbrotModel the model used to observe, render and update according to the user events
     */
    public JPanelHelperClass(MandelbrotModel mMandelbrotModel){
        super.addMouseListener(this);
        super.addMouseMotionListener(this);
        this.mMandelbrotModel = mMandelbrotModel;
        // setting initial boolean value to false
        selectionToZoom = false;

        stackUndo = mMandelbrotModel.getStackUndo();
        stackRedo = mMandelbrotModel.getStackRedo();

        animationFrames = mMandelbrotModel.getAnimationFrames();
    }

    /**
     * The following method performs ENHANCEMENT 1 and ENHANCEMENT 4.
     * Different color mappings to map the iteration values to different shades of a colour, where higher iteration
     * numbers are mapped to brighter/whiter shades of blue and red until the iteration limit is reached.
     *
     * Permits the user to switch between colour maps at the touch of a button.
     * @param graphics
     */
    @Override
    protected void paintComponent(Graphics graphics){
        super.paintComponent(graphics);

         mandelbrotInfo = mMandelbrotModel.getMandelbrotInfo();
         maximumIterations = mMandelbrotModel.getMaximumIterations();

        for (int i = 0; i < mandelbrotInfo.length ; i++){
            for (int j = 0; j < mandelbrotInfo[i].length; j++){
                if (mandelbrotInfo[i][j] >= maximumIterations){
                    graphics.setColor(Color.BLACK);
                    graphics.drawLine(j, i, j, i);
                } else {
                    float colorValue = (float) mandelbrotInfo[i][j] / mMandelbrotModel.getMaximumIterations();

                    if (mMandelbrotModel.getColor().equals(Color.RED)){
                        graphics.setColor(new Color(colorValue, 0, 0));
                    } else if (mMandelbrotModel.getColor().equals(Color.GREEN)){
                        graphics.setColor(new Color(0, colorValue, 0));
                    } else if (mMandelbrotModel.getColor().equals(Color.BLUE)){
                        graphics.setColor(new Color(0, 0, colorValue));
                    } else if (mMandelbrotModel.getColor().equals(Color.WHITE)){
                        graphics.setColor(Color.WHITE);
                    } else {
                        graphics.setColor(mMandelbrotModel.getColor());
                    }
                    graphics.drawLine(j, i, j, i);
                }
            }

            // calling the method below to draw the boundary line while selecting area to zoom.
            drawZoomSelectionBoundary(graphics);
        }
    }

    /**
     * Method to draw a boundary while selecting an area to zoom.
     *
     * @param graphics graphics object used to set color and draw the rectangle.
     */
    public void drawZoomSelectionBoundary(Graphics graphics){
        int zoomSelectionWidth = Math.abs(coordinateX2 - coordinateX1);
        int zoomSelectionHeight = Math.abs(coordinateY2 - coordinateY1);

        if (!selectionToZoom){
            if (coordinateX2 > coordinateX1 && coordinateY2 > coordinateY1){
                graphics.setColor(Color.BLACK);
                graphics.drawRect(coordinateX1, coordinateY1, zoomSelectionWidth, zoomSelectionHeight);}

            else if (coordinateX2 > coordinateX1 && coordinateY2 < coordinateY1){
                graphics.setColor(Color.BLACK);
                graphics.drawRect(coordinateX1, coordinateY2, zoomSelectionWidth, zoomSelectionHeight);
            }
            else {
                if (coordinateY2 > coordinateY1){
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(coordinateX2, coordinateY1, zoomSelectionWidth, zoomSelectionHeight);
                } else {
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(coordinateX2, coordinateY2, zoomSelectionWidth, zoomSelectionHeight);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        coordinateX1 = mouseEvent.getX();
        coordinateY1 = mouseEvent.getY();
        selectionToZoom = false;
    }

    /**
     * Override method performs the functionality for what happens when mouse pointer is released.
     * Tasks implemented below:
     * - Force the boundary of the coordinates retrieved to be a square if a rectangle is drawn on the panel.
     *
     * - Basic requirement: ZOOM - permits the user to select a square (or rectangle area) on the image which will then
     * be used as the bounds for a new calculation of the Mandelbrot set, thereby effectively zooming in on the image.
     *
     * - ENHANCEMENT 3 - Permits the user to define and view animations of zooming in on the Mandelbrot set
     *
     * @param mouseEvent mouse event object used to retrieve the X and Y coordinates
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

        /**
         * The below lines of code is used to force the boundary of the coordinates retrieved to be a square, if it is
         * a rectangle.
         */
        if (coordinateX1 > coordinateX2){
            int temporary = coordinateX1;
            coordinateX1 = coordinateX2;
            coordinateX2 = temporary;
        }

        if (coordinateY1 > coordinateY2){
            int temporary = coordinateY1;
            coordinateY1 = coordinateY2;
            coordinateY2 = temporary;
        }

        coordinateY2 = coordinateY1 + (coordinateX2 - coordinateX1);

        // Save recent MandelbrotSetData to the Stack to perform Undo functionality
        stackUndo.push(new MandelbrotSetData(mMandelbrotModel));
        stackRedo.clear();

        animationFrames.clear();

        /**
         * The following lines of code work out the integration of Zoom feature along with its animation ratio aspect and
         * its calculations.
         */
        for (int i = 0; i < DEFAULT_ANIMATION_FRAMES; i++){
            MandelbrotSetData mandelbrotSetData = new MandelbrotSetData(stackUndo.peek());


            double realRange = mMandelbrotModel.getMaximumReal() - mMandelbrotModel.getMinumumReal();
            double imaginaryRange = mMandelbrotModel.getMaximumImaginary() - mMandelbrotModel.getMinimumImaginary();

            double animationRatioAspect = (double) (i+1) / DEFAULT_ANIMATION_FRAMES;


            mandelbrotSetData.setMinumumReal(mMandelbrotModel.getMinumumReal() + ((double) coordinateX1 / mMandelbrotModel.getX_resolution()) * realRange * animationRatioAspect);

            mandelbrotSetData.setMaximumImaginary(mMandelbrotModel.getMaximumImaginary() - ((double) (mMandelbrotModel.getY_resolution() - coordinateY2) / mMandelbrotModel.getY_resolution()) * imaginaryRange * animationRatioAspect);

            mandelbrotSetData.setMaximumReal(mMandelbrotModel.getMaximumReal() - ((double) (mMandelbrotModel.getX_resolution() - coordinateX2) / mMandelbrotModel.getX_resolution()) * realRange * animationRatioAspect);

            mandelbrotSetData.setMinimumImaginary(mMandelbrotModel.getMinimumImaginary() + ((double) coordinateY1 / mMandelbrotModel.getY_resolution()) * imaginaryRange * animationRatioAspect);

            animationFrames.add(mandelbrotSetData);
        }

        selectionToZoom = true;

        if (animationFrames.size() > 0){
            mMandelbrotModel.updateMandelbrot(animationFrames.remove());
        }

        System.out.println(coordinateX1 + " " + coordinateY1 + " " + coordinateX2 + " " + coordinateY2 );
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        // get x2, y2 coordinates
        coordinateX2 = mouseEvent.getX();
        coordinateY2 = mouseEvent.getY();
        this.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }
}
