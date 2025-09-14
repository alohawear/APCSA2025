---
title: "Grabber v0.1"
description: "Telescoping grabber mechanism for picking up and holding objects."
date: 2025-09-14
tags: ['week38', 'robot', 'objects', 'encapsulation', 'instantiation', 'constructors', 'actuators']
---

```java
/**
 * Grabber Class - Controls a telescoping grabber arm with open/close functionality
 * 
 * This class manages the robot's grabber mechanism, including extension/retraction,
 * opening/closing the gripper, and tracking what objects are being held.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-09-14
 */
public class Grabber {  // 'public' means other classes can use this Grabber class
    
    // Constants - 'final' means these values cannot be changed after initialization
    // 'static' means they belong to the class, not individual objects
    // 'public' means other classes can access these constants
    public static final double MAX_EXTENSION_CM = 30.0;     // Maximum extension distance
    public static final double MIN_EXTENSION_CM = 0.0;      // Minimum extension (fully retracted)
    public static final int MAX_GRIP_STRENGTH = 100;        // Maximum grip strength percentage
    public static final int MIN_GRIP_STRENGTH = 0;          // Minimum grip strength percentage
    public static final int DEFAULT_GRIP_STRENGTH = 50;     // Default medium grip strength
    public static final String NO_OBJECT = "none";          // Constant for when no object is held
    
    // Instance variables - each Grabber object has its own copy of these
    // 'private' means only THIS Grabber class can directly access these variables
    private double extensionLength;    // double to store how far grabber is extended (cm)
    private boolean isOpen;            // boolean to track if gripper is open or closed
    private boolean isHolding;         // boolean to track if grabber has an object
    private String heldObject;         // String to store name of object being held
    private boolean isExtending;       // boolean to track if grabber is currently moving
    private int gripStrength;          // int to store grip force (0-100)
    
    /**
     * Constructor - Creates a new Grabber object with default settings
     * Constructor has same name as class and no return type
     * No parameters needed - uses constant default values
     */
    public Grabber() {  // 'public' so other classes can create Grabber objects
        // Initialize all instance variables using our constants where appropriate
        this.extensionLength = MIN_EXTENSION_CM;        // Start fully retracted
        this.isOpen = true;                             // Start with gripper open
        this.isHolding = false;                         // Not holding anything initially  
        this.heldObject = NO_OBJECT;                    // Use constant for no object
        this.isExtending = false;                       // Not currently moving
        this.gripStrength = DEFAULT_GRIP_STRENGTH;      // Use constant for default strength
        
        System.out.println("Grabber initialized - ready to grab objects!");
    }
    
    /**
     * Method to extend the grabber to a specified length
     * @param targetLength How far to extend in centimeters (parameter)
     */
    public void extend(double targetLength) {  // 'public void' - other classes can call, returns nothing
        // Validate that target length is within acceptable range using constants
        if (targetLength < MIN_EXTENSION_CM) {
            targetLength = MIN_EXTENSION_CM;  // Use constant instead of hardcoded 0
        } else if (targetLength > MAX_EXTENSION_CM) {
            targetLength = MAX_EXTENSION_CM;  // Use constant instead of hardcoded value
        }
        
        System.out.println("Extending grabber to " + targetLength + " cm...");
        this.isExtending = true;         // Set flag that grabber is moving
        this.extensionLength = targetLength;  // Update our instance variable
        this.isExtending = false;        // Movement complete
        
        System.out.println("Grabber extended to " + extensionLength + " cm");
        // Method functionality coming soon!
    }
    
    /**
     * Method to fully retract the grabber (bring it back to robot)
     * No parameters needed - always retracts to minimum extension constant
     */
    public void retract() {
        System.out.println("Retracting grabber...");
        extend(MIN_EXTENSION_CM);  // Use constant instead of hardcoded 0.0
        System.out.println("Grabber fully retracted");
    }
    
    /**
     * Method to operate the grabber (open or close the gripper)
     * @param action "open" or "close" (String parameter)
     */
    public void operate(String action) {  // Takes String parameter
        // Convert parameter to lowercase for easier comparison
        action = action.toLowerCase();
        
        if (action.equals("open")) {  // String comparison uses .equals() method
            this.isOpen = true;       // Set boolean flag
            if (isHolding) {          // If we were holding something
                dropObject();         // Drop it when we open
            }
            System.out.println("Grabber opened");
        } else if (action.equals("close")) {
            this.isOpen = false;      // Set boolean flag
            System.out.println("Grabber closed");
            // Method functionality coming soon!
        } else {
            // Invalid parameter - print error message
            System.out.println("Invalid grabber action: " + action + 
                             ". Use 'open' or 'close'");
        }
    }
    
    /**
     * Method to attempt to grab an object
     * @param objectName Name of the object to grab (parameter)
     * @return true if successfully grabbed, false if failed (return value)
     */
    public boolean grabObject(String objectName) {  // 'public boolean' - returns true/false
        if (isOpen) {  // Check if grabber is open
            System.out.println("Cannot grab " + objectName + " - grabber is open!");
            return false;  // Return false to indicate failure
        }
        
        if (isHolding) {  // Check if already holding something
            System.out.println("Cannot grab " + objectName + 
                             " - already holding " + heldObject);
            return false;  // Return false to indicate failure
        }
        
        // Simulate successful grab
        this.isHolding = true;           // Set boolean flag
        this.heldObject = objectName;    // Store object name
        System.out.println("Successfully grabbed: " + objectName);
        // Method functionality coming soon!
        return true;  // Return true to indicate success
    }
    
    /**
     * Method to drop whatever object is currently being held
     * No parameters needed
     */
    public void dropObject() {
        if (!isHolding) {  // '!' means 'not' - if NOT holding anything
            System.out.println("No object to drop");
            return;  // Exit method early
        }
        
        System.out.println("Dropping " + heldObject);
        this.isHolding = false;         // Clear holding flag
        this.heldObject = NO_OBJECT;    // Use constant instead of hardcoded "none"
        // Method functionality coming soon!
    }
    
    /**
     * Method to set the grip strength using constants for validation
     * @param strength Grip strength from MIN_GRIP_STRENGTH to MAX_GRIP_STRENGTH (parameter)
     */
    public void setGripStrength(int strength) {
        // Validate strength is within acceptable range using constants
        if (strength < MIN_GRIP_STRENGTH) {
            strength = MIN_GRIP_STRENGTH;    // Use constant for minimum
        } else if (strength > MAX_GRIP_STRENGTH) {
            strength = MAX_GRIP_STRENGTH;    // Use constant for maximum
        }
        
        this.gripStrength = strength;  // Update instance variable
        System.out.println("Grip strength set to " + strength + "%");
    }
    
    // Getter methods - allow other classes to read our private variables safely
    // These don't change the grabber's state, just return information
    
    /**
     * Getter method for current extension length
     * @return Current extension in centimeters (double)
     */
    public double getExtensionLength() {
        return extensionLength;  // Return our private double variable
    }
    
    /**
     * Getter method for maximum extension limit (returns constant value)
     * @return Maximum extension in centimeters (double)
     */
    public double getMaxExtension() {
        return MAX_EXTENSION_CM;  // Return our constant instead of instance variable
    }
    
    /**
     * Getter method for gripper open/closed state
     * @return true if gripper is open, false if closed (boolean)
     */
    public boolean isOpen() {
        return isOpen;  // Return our private boolean variable
    }
    
    /**
     * Getter method for holding status
     * @return true if holding an object, false if empty (boolean)
     */
    public boolean isHolding() {
        return isHolding;  // Return our private boolean variable
    }
    
    /**
     * Getter method for name of held object
     * @return Name of object being held, or NO_OBJECT constant if empty (String)
     */
    public String getHeldObject() {
        return heldObject;  // Return our private String variable
    }
    
    /**
     * Getter method for extension status
     * @return true if grabber is currently moving, false if stationary (boolean)
     */
    public boolean isExtending() {
        return isExtending;  // Return our private boolean variable
    }
    
    /**
     * Getter method for grip strength
     * @return Current grip strength from MIN_GRIP_STRENGTH to MAX_GRIP_STRENGTH (int)
     */
    public int getGripStrength() {
        return gripStrength;  // Return our private int variable
    }
    
    /**
     * Method to get formatted status information about the grabber
     * @return String containing grabber status details
     */
    public String getStatus() {
        // Use concatenation (+) to build a formatted status string with constants
        return "Grabber [Extended: " + extensionLength + "cm/" + MAX_EXTENSION_CM + 
               "cm, " + (isOpen ? "OPEN" : "CLOSED") + 
               ", Holding: " + heldObject + 
               ", Grip: " + gripStrength + "%]";
    }
    
    /**
     * Method to display detailed grabber diagnostics
     * 'public void' - other classes can call this, returns nothing
     */
    public void displayDiagnostics() {
        System.out.println("\n=== Grabber Diagnostics ===");
        System.out.println("Extension: " + extensionLength + " cm (max: " + MAX_EXTENSION_CM + " cm)");
        System.out.println("Gripper State: " + (isOpen ? "OPEN" : "CLOSED"));
        System.out.println("Holding Object: " + (isHolding ? heldObject : "NO"));
        System.out.println("Grip Strength: " + gripStrength + "% (range: " + 
                         MIN_GRIP_STRENGTH + "-" + MAX_GRIP_STRENGTH + "%)");
        System.out.println("Currently Moving: " + (isExtending ? "YES" : "NO"));
    }
    
    /**
     * Method to check if grabber is at maximum extension using constant
     * @return true if fully extended, false otherwise
     */
    public boolean isFullyExtended() {
        return extensionLength >= MAX_EXTENSION_CM;  // Compare with constant
    }
    
    /**
     * Method to check if grabber is fully retracted using constant
     * @return true if fully retracted, false otherwise  
     */
    public boolean isFullyRetracted() {
        return extensionLength <= MIN_EXTENSION_CM;  // Compare with constant
    }
    
    /**
     * Method to perform grabber maintenance check
     * @return true if grabber is functioning normally, false if problems detected
     */
    public boolean performMaintenanceCheck() {
        System.out.println("Performing maintenance check on grabber...");
        // Method functionality coming soon!
        return true;  // Return true for now (assume all is well)
    }
}
```