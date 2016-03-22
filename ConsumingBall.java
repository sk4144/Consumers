package edu.nyu.cs.cs101.consumers;

import java.util.ArrayList;

/**
 * A <code>ConsumingBall</code> is a <code>Ball</code> that is always
 * accelerating in the direction of the closest ball. In other words, it is
 * attempting to consume the closest ball (regardless if that ball is bigger or
 * smaller than it).
 * 
 * TODO: All Consumer balls should be red by default
 * 
 * TODO: Complete the accelerate method
 * 
 * @author sk4144 suebin kim
 */
public class ConsumingBall extends ExpandableBall {

    /**
     * Constructor with arguments
     * 
     * @param xPos location for this <code>Ball</code>
     * @param yPos location for this <code>Ball</code>
     */
    public ConsumingBall(int xPos, int yPos) {
        super(xPos, yPos);
        this.red = 255;
        this.blue = 0;
        this.green = 0;
        super.setColor(red, green, blue);
    }

    @Override
    public String toString() {
        return super.toString() + " is a consuming ball";
    }

    /**
     * Consuming balls always accelerate towards the closest ball.
     * 
     * @param objects list of balls that it may want to try and consume
     */
    void accelerate(ArrayList<GeometricObject> objects) {
    	GeometricObject closestObject = super.getClosest(this, objects);
    	double xPos = closestObject.xPos;
    	double yPos = closestObject.yPos;
    	super.accelerate(xPos, yPos);
    }
}

   