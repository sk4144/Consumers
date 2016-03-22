package edu.nyu.cs.cs101.consumers;

import processing.core.*;
import ddf.minim.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * This is a simple game. It is comprised of a set of different balls that are
 * situated on the screen. The way the game works is that as balls run into each
 * other the larger balls will consume the smaller balls. Currently only most of
 * Ball and the ExpandableBall class are implemented.
 * 
 * FEATURES IMPLEMENTED BY @SK4144 : 
 * 	When "Shift" is pressed, balls speed up by .5
 *  When "Alt" is pressed, balls slow down by .3
 *  Music is added - "Loud pipes" by Ratatat
 * 
 * Class hierarchy is:
 * 
 * <pre>
 * GeometricObject (Drawable/Movable)
 *            |---> Ball (Colorable)
 *                         |-------> ExpandableBall (Expandable)
 *                                       |----> ConsumingBall
 *                                       |----> EvadingBall
 *                                       |----> IntelligentBall
 * </pre>
 * 
 * 
 * There are really four kinds of balls:
 * <ul>
 * <li>a user ball controlled by the user which is a basic
 * <code>ExpandableBall</code>
 * <li>a <code>ConsumingBall</code>
 * <li>an <code>EvadingBall</code>
 * <li>an <code>IntelligentBall</code>
 * </ul>
 * 
 * Your job is to update the code in this class and the Ball classes to meet the
 * specifications for each class.
 * 
 * 
 * TODO: Replace the TODO blocks in the following files:
 * <ul>
 * <li>Driver.java</li>
 * <li>Ball.java</li>
 * <li>ConsumingBall.java</li>
 * <li>EvadingBall.java</li>
 * <li>IntelligentBall.java</li>
 * </ul>
 * 
 * @author Andrew Case <acase@cs.nyu.edu>
 * @author SK4144 Suebin Kim
 */

@SuppressWarnings("serial")
public class Driver extends PApplet {
    private static final int WINDOW_WIDTH = 1024;
    private static final int WINDOW_HEIGHT = 768;
    private static final int TEXT_SIZE = 20;
    private static final int TEXT_TIMELIMIT = 10000; // in milliseconds

    // Number of shapes to start with
    private static final int NUM_OBJECTS = 30;

    // Limits for shapes at creation time
    static final double STARTING_MAX_SPEED = .25;
    private static final int STARTING_MAX_SIZE = 30;

    // Player shape attributes at start
    private static final int PLAYER_XPOS = WINDOW_WIDTH / 2;
    private static final int PLAYER_YPOS = WINDOW_HEIGHT / 2;
    private static final int PLAYER_SIZE = STARTING_MAX_SIZE;
    private static final int PLAYER_RED = 0;
    private static final int PLAYER_GREEN = 0;
    private static final int PLAYER_BLUE = 200;

    // Color constants
    private static final int BACKGROUND = 0;
    private static final int FILLER = 200;
    private static final int FOREGROUND = 255;

    // Minimum distance a shape can be generated near the user shape
    private static final double MIN_DISTANCE = 20;

    // Different instructions to give the user
    private static final String INSTRUCTIONS_START = "Move your ball using the arrow keys or mouse clicks";
    private static final String INSTRUCTIONS_END = "Press spacebar to restart";

    // Generic expandable shapes
    private static ArrayList<GeometricObject> objects;
    // User controlled shape
    private static ExpandableBall userBall;

    // text to display to the user
    private String message = INSTRUCTIONS_START;
    // text will be displayed until this time
    private int textEndTime;
    
    Minim minim;
	AudioPlayer player;

    /**
     * Setup runs once when the program starts to initialize the interface to a
     * known state
     */
    public void setup() {
		
		
        size(WINDOW_WIDTH, WINDOW_HEIGHT);
        background(BACKGROUND);
        createObjects();
        textEndTime = millis() + TEXT_TIMELIMIT;
        
        //creating background music
        minim = new Minim(this);
        player = minim.loadFile("Loud Pipes.mp3");
		player.play();
    }

    /**
     * Displays a message in the center of the screen
     * 
     * @param message to display
     * @param endTime till display it till (in regards to a time specified by
     *        the millis() method
     */
    
    public void displayMessage(String message, int endTime) {
        if (endTime > millis()) {
            fill(FOREGROUND);
            textSize(TEXT_SIZE);
            textAlign(CENTER);
            text(message, width / 2, height / 2);
        }
    }

    /**
     * The draw function is called over and over by the processing framework to
     * re-draw the screen
     */
    public void draw() {
        background(BACKGROUND);
        checkForWallCollisions();
        checkForObjectCollisions();
        drawObjects();
        updateMessage();
        displayMessage(message, textEndTime);
    }

    /**
     * Update the instructions/message to present to the user given different
     * situations
     */
    private void updateMessage() {
        // If the user ball has been consumed
        if (userBall == null) {
            message = INSTRUCTIONS_END;
            textEndTime = millis() + TEXT_TIMELIMIT;
        }
    }

    /**
     * Updates the display with the new position for all objects
     */
    private void drawObjects() {
        fill(PLAYER_RED, PLAYER_GREEN, PLAYER_BLUE);
        stroke(FOREGROUND);

        // For each shape
        fill(FILLER);
        for (GeometricObject object : objects) { // skip user-ball
            // Ignore non-existent items
            if (object == null)
                continue;

            if (object instanceof ConsumingBall) {
                ((ConsumingBall) object).accelerate(objects);
            } else if (object instanceof EvadingBall) {
                ((EvadingBall) object).accelerate(objects);
            } else if (object instanceof IntelligentBall) {
               ((IntelligentBall) object).accelerate(objects);
            }

            object.move();
            object.draw(this);
        }
    }

    /**
     * Checks for collisions between objects
     */
    void checkForWallCollisions() {
        // check if any balls have collisions with other objects
        for (GeometricObject shape : objects) {
            if (shape == null)
                continue;

            // Hit the top?
            if ((shape.getY() < 0) && (shape.ySpeed < 0)) {
                shape.reverseY();
            }
            // Hit the bottom?
            if ((shape.getY() > this.height) && (shape.ySpeed > 0)) {
                shape.reverseY();
            }
            // Hit the left?
            if ((shape.getX() < 0) && (shape.xSpeed < 0)) {
                shape.reverseX();
            }
            // Hit the right?
            if ((shape.getX() > this.width) && (shape.xSpeed > 0)) {
                shape.reverseX();
            }
        }
    }

    /**
     * Handles collisions between objects
     */
    void checkForObjectCollisions() {
        // For every ball it will be check against all the other balls
        for (GeometricObject object : objects) {
            if (!(object instanceof Expandable)) {
                continue;
            }
            checkForObjectCollision(object, objects);
        }

        // Remove consumed objects
        Iterator<GeometricObject> iter = objects.iterator();
        while (iter.hasNext()) {
            GeometricObject object = iter.next();
            if (object.getArea() <= 0) {
                System.out.println(object);
                if (object == userBall) {
                    // End message will display if userball is null
                    userBall = null;
                }
                iter.remove();
            }
        }
    }

    /**
     * Checks if object collided with any objects. If a collision occurs one of
     * the objects grows while the other shrinks.
     * 
     * @param object to check for collisions
     * @param objects to check against
     */
    private void checkForObjectCollision(GeometricObject object, ArrayList<GeometricObject> objects) {
        // Check for collisions with all other balls
        for (GeometricObject otherObject : objects) {

            // Currently only checking for collisions between balls
            if (!(object instanceof ExpandableBall) || !(otherObject instanceof ExpandableBall)) {
                continue;
            }

            ExpandableBall ball = (ExpandableBall) object;
            ExpandableBall otherBall = (ExpandableBall) otherObject;

            // Check to see if this ball collided with another ball
            if (Ball.isCollision(ball, otherBall)) {
                // Shrink/grow the appropriate balls
                if (ball.getRadius() > otherBall.getRadius()) {
                    ball.grow();
                    otherBall.shrink();
                } else {
                    ball.shrink();
                    otherBall.grow();
                }
            }
        }
    }

    /**
     * Populates the list of objects with one user controlled ball, and
     * subsequent <code>ExpandableBall</code> objects.
     */
    void createObjects() {
        // Create shapes
        System.out.println("Creating objects...");
        objects = new ArrayList<GeometricObject>();

        // Create the user's ball
        userBall = new ExpandableBall(PLAYER_XPOS, PLAYER_YPOS);
        userBall.setRadius(PLAYER_SIZE);
        userBall.maxSpeed = 5;
        objects.add(userBall);

        // Create other balls
        for (int i = 0; i < NUM_OBJECTS; i++) {
            Ball object = createExpandableBall();
            objects.add(object);
        }

    }

    /**
     * Creates a randomized <code>ExpandableBall</code> somewhere on the screen.
     * The ball will have a random direction and a random speed.
     * 
     * @return a newly created randomized <code>ExpandableBall</code>.
     */
    ExpandableBall createExpandableBall() {
        Random rand = new Random();

        // Creating a ball at random location (but not on top of player)
        int xPos = rand.nextInt(width);
        int yPos = rand.nextInt(height);
        while ((Math.abs(xPos - userBall.getX()) < MIN_DISTANCE)
                || (Math.abs(yPos - userBall.getY()) < MIN_DISTANCE)) {
            xPos = rand.nextInt(width);
            yPos = rand.nextInt(height);
        }

        // Choose a random kind of ball:
        int ballType = rand.nextInt(4);  // How many types of balls there are
        ExpandableBall ball;
        if (ballType == 0) {
            ball = new ExpandableBall(xPos, yPos);
        } else if (ballType == 1) {
            ball = new EvadingBall(xPos, yPos);
        } else if (ballType == 2) {
            ball = new ConsumingBall(xPos, yPos);
        } else {
            ball = new IntelligentBall(xPos, yPos);
        }

        // Random Speed
        double xSpeed = rand.nextFloat() * STARTING_MAX_SPEED;
        double ySpeed = rand.nextFloat() * STARTING_MAX_SPEED;
        ball.setSpeed(xSpeed, ySpeed);

        // Random direction (gives either a 0 or 1; if 0 - negative direction)
        if (rand.nextInt(2) == 0)
            ball.reverseX();
        if (rand.nextInt(2) == 0)
            ball.reverseY();

        // Random size
        float size = rand.nextFloat() * STARTING_MAX_SIZE;
        ball.setRadius(size);

        return ball;

    }

    /**
     * If the mouse is pressed, the ball accelerates in the direction of the
     * click
     */
    public void mousePressed() {
        userBall.accelerate(mouseX, mouseY);
    }

    /**
     * Handle keyboard input
     * Added acceleration option if shift is pressed
     * Deceleration option if alt is pressed
     */
    public void keyPressed() {
        if (key == CODED) {
            System.out.println(userBall);
            if (keyCode == UP) {
                userBall.accelerate(userBall.getX(), userBall.getY() - 1);
            } else if (keyCode == DOWN) {
                userBall.accelerate(userBall.getX(), userBall.getY() + 1);
            } else if (keyCode == LEFT) {
                userBall.accelerate(userBall.getX() - 1, userBall.getY());
            } else if (keyCode == RIGHT) {
                userBall.accelerate(userBall.getX() + 1, userBall.getY());
            } else if (keyCode == SHIFT) {
            	for (GeometricObject ball : objects) {
            		ball.fasterSpeed();
            	}
            } else if (keyCode == ALT) {
            	for (GeometricObject ball : objects) {
            		ball.slowerSpeed();
            	}
            }
        } else if (key == ' ') {
        	player.pause();
            setup();
            
        }
    }
}
