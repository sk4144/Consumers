package edu.nyu.cs.cs101.consumers;

import java.util.ArrayList;

/**
 * An <code>EvadignBall</code> is a <code>Ball</code> that is always trying to
 * move in the direction away from the nearest ball to it.
 * 
 * TODO: All Evading balls should be green by default
 * 
 * TODO: Complete the accelerate method
 * 
 * @author sk4144 suebin kim
 * 
 */
public class EvadingBall extends ExpandableBall {

    /**
     * Constructor with arguments
     * 
     * @param xPos location for this <code>Ball</code>
     * @param yPos location for this <code>Ball</code>
     */
    public EvadingBall(int xPos, int yPos) {
        super(xPos, yPos);
        this.red = 0;
        this.blue = 0;
        this.green = 255;
        super.setColor(red, green, blue);
    }

    /**
     * Always accelerates way from the closest ball.
     * 
     * @param objects list of balls that it may want to try and evade
     */
    void accelerate(ArrayList<GeometricObject> objects) {
    	GeometricObject closestObject = super.getClosest(this, objects);
    	double xPos = closestObject.xPos;
    	double yPos = closestObject.yPos;
    	super.decelerate(xPos, yPos);
    }

}
