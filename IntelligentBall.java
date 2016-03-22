package edu.nyu.cs.cs101.consumers;

import java.util.ArrayList;

/**
 * An intelligent ball is a <code>Ball</code> that:
 * <ol>
 * <li>Tries to accelerate towards smaller balls that are near it (to consume
 * them)
 * <li>Tries to accelerate away from larger balls that are near it (to evade
 * them)
 * </ol>
 * 
 * TODO: All Intelligent balls should be blue by default
 * 
 * TODO: Write this entire class.
 * 
 * @author sk4144 suebin kim
 */
public class IntelligentBall extends ExpandableBall {
	
    /**
     * Constructor
     * @param xPos
     * 	x position of ball
     * @param yPos
     * 	y position of ball
     * 
     */
	public IntelligentBall(int xPos, int yPos) {
		super(xPos, yPos);
		this.red = 0;
		this.green = 0;
		this.blue = 255;
		//sets color to blue
		super.setColor(red, green, blue);
	}
	
    /**
     * accelerates the intelligent ball towards the closest ball 
     * thats smaller than the intelligent ball
     * @param objects
     * 	array of balls to compare the intelligent balls area to
     */
	public void accelerate(ArrayList<GeometricObject> objects) {
		GeometricObject closestObject = super.getClosest(this, objects);
		if (closestObject.getArea() < this.getArea()) {
			double xPos = closestObject.xPos;
			double yPos = closestObject.yPos;
			super.accelerate(xPos, yPos);
		}
		//if the ball is bigger than the intelligent ball
		//it goes away from it
		else if (closestObject.getArea() > this.getArea())
			this.decelerate(objects);

	}	
	
	/**
     * accelerates the intelligent ball away from the bigger ball 
     * thats close to it
     * @param objects
     * 	array of balls to compare the intelligent balls area to
     */
	public void decelerate(ArrayList<GeometricObject> objects) {
		GeometricObject closestObject = super.getClosest(this, objects);
    	if (closestObject.getArea() > this.getArea()) {
    		double xPos = closestObject.xPos;
        	double yPos = closestObject.yPos;
        	super.decelerate(xPos, yPos);
		}
    	//if the ball is smaller than the intelligent ball 
    	//it goes towards it
		else if (closestObject.getArea() < this.getArea())
			this.accelerate(objects);

	}

	
	
	
    
}
