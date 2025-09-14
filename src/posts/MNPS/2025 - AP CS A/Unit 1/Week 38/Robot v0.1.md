---
title: "Robot v0.1"
description: "Main robot class that manages four wheels, grabber, and sensors."
date: 2025-09-14
tags: ['week38', 'robot', 'objects', 'encapsulation', 'instantiation', 'constructors', 'composition']
---

```java
/**
 * Robot Class - Controls a four-wheeled robot with grabber and sensors
 * 
 * This class manages the overall robot functionality including movement,
 * grabbing objects, and sensor readings for autonomous navigation.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-09-14
 */
public class Robot {  // 'public' means other classes can use this Robot class
    
    // Constants - 'final' means these values cannot be changed after initialization
    // 'static' means they belong to the class, not individual objects
    // 'public' means other classes can access these constants
    public static final int TOTAL_WHEELS = 4;               // Number of wheels on robot
    public static final int MAX_POWER_LEVEL = 100;          // Maximum power percentage
    public static final int MIN_POWER_LEVEL = 0;            // Minimum power percentage
    public static final String ROBOT_VERSION = "1.0";       // Robot software version
    public static final double SAFE_DISTANCE_CM = 10.0;     // Safe stopping distance
    
    // Instance variables (also called fields or attributes)
    // 'private' means only THIS Robot class can access these variables directly
    // Each Robot object will have its own copy of these variables
    private Wheel frontRight;        // Reference to a Wheel object
    private Wheel frontLeft;         // Reference to a Wheel object  
    private Wheel rearRight;         // Reference to a Wheel object
    private Wheel rearLeft;          // Reference to a Wheel object
    private Grabber grabber;         // Reference to a Grabber object
    private DistanceSensor distanceSensor;  // Reference to a DistanceSensor object
    private ColorSensor colorSensor;        // Reference to a ColorSensor object
    private String robotName;        // String to store the robot's name
    private boolean isPoweredOn;     // boolean to track if robot is on/off
    
    /**
     * Constructor - Special method that runs when we create a new Robot object
     * A constructor has the same name as the class and no return type
     * @param name The name of this robot (parameter passed in)
     */
    public Robot(String name) {  // 'public' so other classes can create Robot objects
        // 'this' refers to the current Robot object being created
        this.robotName = name;      // Assign the parameter to our instance variable
        this.isPoweredOn = false;   // Set initial power state to off
        
        // Initialize all four wheels by creating new Wheel objects
        // The 'new' keyword creates a new object in memory
        this.frontRight = new Wheel("Front Right");
        this.frontLeft = new Wheel("Front Left");
        this.rearRight = new Wheel("Rear Right");
        this.rearLeft = new Wheel("Rear Left");
        
        // Initialize grabber and sensors with new objects
        this.grabber = new Grabber();
        this.distanceSensor = new DistanceSensor();
        this.colorSensor = new ColorSensor();
        
        System.out.println("Robot " + name + " has been assembled!");
    }
    
    /**
     * Method to power on the robot and initialize all systems
     * 'public' means other classes can call this method
     * 'void' means this method doesn't return any value
     */
    public void powerOn() {
        isPoweredOn = true;  // Change the state of our boolean variable
        System.out.println(robotName + " is powering on...");
        // Method functionality coming soon!
    }
    
    /**
     * Method to power off the robot safely
     * 'public void' - accessible to other classes, returns nothing
     */
    public void powerOff() {
        isPoweredOn = false;  // Update our instance variable
        stopAllWheels();      // Call another method in this same class
        System.out.println(robotName + " is shutting down safely.");
        // Method functionality coming soon!
    }
    
    /**
     * Method to move the robot forward at specified power
     * @param power Power level from 0-100 (this is a parameter)
     */
    public void moveForward(int power) {  // 'int power' is a parameter
        // Ensure power level stays within safe operating range
        if (power < MIN_POWER_LEVEL) {
            power = MIN_POWER_LEVEL;  // Set to minimum if too low
        } else if (power > MAX_POWER_LEVEL) {
            power = MAX_POWER_LEVEL;  // Set to maximum if too high
        }
        
        System.out.println(robotName + " moving forward at " + power + "% power");
        // Method functionality coming soon!
    }
    
    /**
     * Method to move the robot backward at specified power
     * @param power Power level from 0-100 (parameter - data passed to method)
     */
    public void moveBackward(int power) {
        // Ensure power level stays within safe operating range
        if (power < MIN_POWER_LEVEL) {
            power = MIN_POWER_LEVEL;  // Set to minimum if too low
        } else if (power > MAX_POWER_LEVEL) {
            power = MAX_POWER_LEVEL;  // Set to maximum if too high
        }
        
        System.out.println(robotName + " moving backward at " + power + "% power");
        // Method functionality coming soon!
    }
    
    /**
     * Method to turn the robot left
     * @param power Power level for turning (parameter)
     */
    public void turnLeft(int power) {
        // Ensure power level stays within safe operating range
        if (power < MIN_POWER_LEVEL) {
            power = MIN_POWER_LEVEL;  // Set to minimum if too low
        } else if (power > MAX_POWER_LEVEL) {
            power = MAX_POWER_LEVEL;  // Set to maximum if too high
        }
        
        System.out.println(robotName + " turning left at " + power + "% power");
        // Method functionality coming soon!
    }
    
    /**
     * Method to turn the robot right
     * @param power Power level for turning (parameter)
     */
    public void turnRight(int power) {
        // Ensure power level stays within safe operating range
        if (power < MIN_POWER_LEVEL) {
            power = MIN_POWER_LEVEL;  // Set to minimum if too low
        } else if (power > MAX_POWER_LEVEL) {
            power = MAX_POWER_LEVEL;  // Set to maximum if too high
        }
        
        System.out.println(robotName + " turning right at " + power + "% power");
        // Method functionality coming soon!
    }
    
    /**
     * Method to stop all wheels immediately
     * No parameters needed - this method takes no input
     */
    public void stopAllWheels() {
        // Set each wheel's power to zero using dot notation
        frontRight.setPower(MIN_POWER_LEVEL);  // Stop front right wheel
        frontLeft.setPower(MIN_POWER_LEVEL);   // Stop front left wheel
        rearRight.setPower(MIN_POWER_LEVEL);   // Stop rear right wheel
        rearLeft.setPower(MIN_POWER_LEVEL);    // Stop rear left wheel
        System.out.println(robotName + " has stopped all wheels");
    }
    
    /**
     * Method to check if robot should stop based on distance sensor reading
     * Compares current distance to safe stopping distance
     * @return true if robot should stop, false if safe to continue
     */
    public boolean shouldStop() {
        double currentDistance = getDistance();  // Get current distance reading from sensor
        boolean tooClose = currentDistance < SAFE_DISTANCE_CM;  // Check if obstacle is too close
        
        if (tooClose) {
            System.out.println("WARNING: Object detected at " + currentDistance + 
                             " cm (safe distance: " + SAFE_DISTANCE_CM + " cm)");
        }
        
        return tooClose;  // Return true if obstacle detected within safe distance
    }
    
    /**
     * Getter method - returns information about the robot's sensors
     * 'public double' means other classes can call this and it returns a double
     * @return Distance in centimeters (return value)
     */
    public double getDistance() {
        return distanceSensor.measureDistance();  // 'return' sends value back to caller
    }
    
    /**
     * Getter method for color sensor reading
     * 'public String' means it returns a String value
     * @return Color name as string (return value)
     */
    public String getColor() {
        return colorSensor.detectColor();  // Call method on colorSensor object
    }
    
    /**
     * Method to extend the grabber to specified position
     * @param extension Extension length in centimeters (parameter)
     */
    public void extendGrabber(double extension) {  // 'double' parameter for decimal numbers
        grabber.extend(extension);  // Call method on our grabber object
    }
    
    /**
     * Method to operate the grabber (open/close)
     * @param action "open" or "close" (String parameter)
     */
    public void operateGrabber(String action) {
        grabber.operate(action);  // Pass the String parameter to grabber method
    }
    
    /**
     * Method to check if grabber is holding an object
     * 'public boolean' means it returns true or false
     * @return true if grabber has an object, false if empty
     */
    public boolean isGrabberHolding() {
        return grabber.isHolding();  // Return the boolean value from grabber
    }
    
    // Getter methods - these allow other classes to access our private variables safely
    // They don't change the object's state, just return information
    
    /**
     * Getter method for robot name
     * @return The robot's name (String)
     */
    public String getName() {
        return robotName;  // Return our private instance variable
    }
    
    /**
     * Getter method for power status  
     * @return true if robot is on, false if off (boolean)
     */
    public boolean isPoweredOn() {
        return isPoweredOn;  // Return our private boolean variable
    }
    
    /**
     * Getter method for front right wheel
     * @return Reference to the frontRight Wheel object
     */
    public Wheel getFrontRight() {
        return frontRight;  // Return reference to our Wheel object
    }
    
    /**
     * Getter method for front left wheel
     * @return Reference to the frontLeft Wheel object
     */
    public Wheel getFrontLeft() {
        return frontLeft;
    }
    
    /**
     * Getter method for rear right wheel
     * @return Reference to the rearRight Wheel object
     */
    public Wheel getRearRight() {
        return rearRight;
    }
    
    /**
     * Getter method for rear left wheel
     * @return Reference to the rearLeft Wheel object  
     */
    public Wheel getRearLeft() {
        return rearLeft;
    }
    
    /**
     * Method to get robot status information as a formatted string
     * @return String with current robot status
     */
    public String getStatus() {
        // Create and return a formatted string using concatenation (+)
        return robotName + " [Power: " + (isPoweredOn ? "ON" : "OFF") + 
               ", Grabber: " + (grabber.isHolding() ? "HOLDING" : "EMPTY") + "]";
    }
    
    /**
     * Method to display detailed robot diagnostics
     * 'public void' - other classes can call this, returns nothing
     */
    public void displayDiagnostics() {
        System.out.println("\n=== " + robotName + " Diagnostics ===");
        System.out.println("Power Status: " + (isPoweredOn ? "ON" : "OFF"));
        System.out.println("Distance Sensor: " + getDistance() + " cm");
        System.out.println("Color Sensor: " + getColor());
        System.out.println("Grabber Status: " + grabber.getStatus());
        System.out.println("Wheel Status:");
        // Call getStatus() method on each wheel object
        System.out.println("  " + frontLeft.getStatus());
        System.out.println("  " + frontRight.getStatus());
        System.out.println("  " + rearLeft.getStatus());
        System.out.println("  " + rearRight.getStatus());
    }
}
```