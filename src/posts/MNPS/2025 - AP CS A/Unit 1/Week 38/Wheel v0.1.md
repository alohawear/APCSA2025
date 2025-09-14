---
title: "Wheel v0.1"
description: "Individual wheel control with power management and status monitoring."
date: 2025-09-14
tags: ['week38', 'robot', 'objects', 'encapsulation', 'instantiation', 'constructors', 'actuators']
---

```java
/**
 * Wheel Class - Controls individual robot wheel with power and status management
 * 
 * This class represents a single wheel on the robot, managing its power level,
 * rotation direction, and operational status.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-09-14
 */
public class Wheel {  // 'public' means other classes can use this Wheel class
    
    // Constants - 'final' means these values cannot be changed after initialization
    // 'static' means they belong to the class, not individual objects  
    // 'public' means other classes can access these constants
    public static final int MAX_POWER = 100;            // Maximum power percentage
    public static final int MIN_POWER = -100;           // Minimum power percentage (reverse)
    public static final int NEUTRAL_POWER = 0;          // No power (stopped)
    public static final int MAX_RPM = 1000;             // Maximum rotations per minute
    public static final int RPM_CONVERSION_FACTOR = 10; // Convert power percentage to RPM
    public static final String DIRECTION_FORWARD = "forward";   // Forward direction constant
    public static final String DIRECTION_REVERSE = "reverse";   // Reverse direction constant  
    public static final String DIRECTION_STOPPED = "stopped";   // Stopped direction constant
    
    // Instance variables - each Wheel object has its own copy of these
    // 'private' means only THIS Wheel class can directly access these variables
    private String wheelName;        // String to identify which wheel this is
    private int powerLevel;          // int to store power from -100 to +100
    private boolean isRotating;      // boolean to track if wheel is spinning
    private String direction;        // String to store rotation direction
    private int rotationSpeed;       // int to store current RPM (rotations per minute)
    private boolean isBlocked;       // boolean to track if wheel is stuck/blocked
    
    /**
     * Constructor - Creates a new Wheel object with specified name
     * Constructor has same name as class and no return type
     * @param name The identifier for this wheel (e.g., "Front Left")
     */
    public Wheel(String name) {  // 'public' so other classes can create Wheel objects
        // 'this' refers to the current Wheel object being created
        this.wheelName = name;              // Set the wheel's name from parameter
        this.powerLevel = NEUTRAL_POWER;    // Start with neutral power using constant
        this.isRotating = false;            // Initially not rotating
        this.direction = DIRECTION_STOPPED; // Initial direction state using constant
        this.rotationSpeed = NEUTRAL_POWER; // Start with zero RPM using constant
        this.isBlocked = false;             // Assume wheel is not blocked initially
        
        System.out.println("Wheel " + name + " initialized");
    }
    
    /**
     * Method to set the power level for this wheel
     * Negative values = reverse, positive = forward, 0 = stop
     * @param power Power level from -100 to +100 (parameter)
     */
    public void setPower(int power) {  // 'public void' - other classes can call, returns nothing
        // Ensure power stays within safe operating range
        if (power < MIN_POWER) {
            power = MIN_POWER;  // Limit to minimum if too low
        } else if (power > MAX_POWER) {
            power = MAX_POWER;  // Limit to maximum if too high
        }
        
        this.powerLevel = power;  // Store the power level in instance variable
        
        // Update wheel state based on the power setting
        if (power == NEUTRAL_POWER) {
            this.isRotating = false;                 // Wheel is not moving
            this.direction = DIRECTION_STOPPED;      // Set direction to stopped
            this.rotationSpeed = NEUTRAL_POWER;      // Zero RPM
        } else if (power > NEUTRAL_POWER) {
            this.isRotating = true;                  // Wheel is spinning
            this.direction = DIRECTION_FORWARD;      // Set direction to forward
            this.rotationSpeed = Math.abs(power) * RPM_CONVERSION_FACTOR; // Calculate RPM
        } else {  // power < NEUTRAL_POWER
            this.isRotating = true;                  // Wheel is spinning
            this.direction = DIRECTION_REVERSE;      // Set direction to reverse
            this.rotationSpeed = Math.abs(power) * RPM_CONVERSION_FACTOR; // Calculate RPM (positive)
        }
        
        System.out.println(wheelName + " power set to " + power + "%");
    }
    
    /**
     * Method to gradually increase power level
     * @param increment Amount to increase power by (parameter)
     */
    public void increasePower(int increment) {
        setPower(powerLevel + increment);  // Use existing setPower method
        // Method functionality coming soon!
    }
    
    /**
     * Method to gradually decrease power level  
     * @param decrement Amount to decrease power by (parameter)
     */
    public void decreasePower(int decrement) {
        setPower(powerLevel - decrement);  // Use existing setPower method  
        // Method functionality coming soon!
    }
    
    /**
     * Method to stop the wheel immediately
     * No parameters needed
     */
    public void stop() {
        setPower(NEUTRAL_POWER);  // Set power to zero to stop the wheel
        System.out.println(wheelName + " stopped");
    }
    
    /**
     * Method to reverse the current direction of rotation
     * If going forward, switch to reverse; if reverse, switch to forward
     */
    public void reverse() {
        setPower(-powerLevel);  // Negate the current power level
        System.out.println(wheelName + " direction reversed");
    }
    
    /**
     * Method to simulate wheel getting blocked (stuck on obstacle)
     * @param blocked true if wheel is blocked, false if clear (parameter)
     */
    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;  // Update our instance variable
        if (blocked) {
            System.out.println(wheelName + " is blocked!");
            // Method functionality coming soon!
        } else {
            System.out.println(wheelName + " is clear");
        }
    }
    
    // Getter methods - allow other classes to read our private variables safely
    // These methods don't change the wheel's state, just return information
    
    /**
     * Getter method for wheel name
     * @return The name of this wheel (String)
     */
    public String getName() {
        return wheelName;  // Return our private String variable
    }
    
    /**
     * Getter method for current power level
     * @return Current power level from -100 to +100 (int)
     */
    public int getPower() {
        return powerLevel;  // Return our private int variable
    }
    
    /**
     * Getter method for rotation status
     * @return true if wheel is rotating, false if stopped (boolean)
     */
    public boolean isRotating() {
        return isRotating;  // Return our private boolean variable
    }
    
    /**
     * Getter method for rotation direction
     * @return "forward", "reverse", or "stopped" (String)
     */
    public String getDirection() {
        return direction;  // Return our private String variable
    }
    
    /**
     * Getter method for rotation speed
     * @return Current rotation speed in RPM (int)
     */
    public int getRotationSpeed() {
        return rotationSpeed;  // Return our private int variable
    }
    
    /**
     * Getter method for blocked status
     * @return true if wheel is blocked, false if clear (boolean)
     */
    public boolean isBlocked() {
        return isBlocked;  // Return our private boolean variable
    }
    
    // Setter methods - allow controlled changes to private variables
    // These provide a safe way to modify the wheel's state
    
    /**
     * Setter method to change wheel name
     * @param newName The new name for this wheel (parameter)
     */
    public void setName(String newName) {
        this.wheelName = newName;  // Update our private variable
        System.out.println("Wheel renamed to: " + newName);
    }
    
    /**
     * Method to get formatted status information about this wheel
     * @return String containing wheel status details
     */
    public String getStatus() {
        // Use concatenation (+) to build a formatted status string
        return wheelName + " [Power: " + powerLevel + "%, " +
               "Direction: " + direction + ", " +
               "RPM: " + rotationSpeed + ", " +
               "Blocked: " + (isBlocked ? "YES" : "NO") + "]";
    }
    
    /**
     * Method to display detailed wheel diagnostics
     * 'public void' - other classes can call this, returns nothing
     */
    public void displayDiagnostics() {
        System.out.println("\n=== " + wheelName + " Diagnostics ===");
        System.out.println("Power Level: " + powerLevel + "%");
        System.out.println("Is Rotating: " + (isRotating ? "YES" : "NO"));
        System.out.println("Direction: " + direction);
        System.out.println("Rotation Speed: " + rotationSpeed + " RPM");
        System.out.println("Is Blocked: " + (isBlocked ? "YES" : "NO"));
    }
    
    /**
     * Method to perform wheel maintenance check
     * @return true if wheel is functioning normally, false if problems detected
     */
    public boolean performMaintenanceCheck() {
        System.out.println("Performing maintenance check on " + wheelName + "...");
        // Method functionality coming soon!
        return !isBlocked;  // Return true if not blocked (simple check for now)
    }
}
```