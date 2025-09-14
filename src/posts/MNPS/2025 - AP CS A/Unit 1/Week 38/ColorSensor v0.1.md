---
title: "ColorSensor v0.1"
description: "RGB color sensor mounted on grabber for identifying object colors."
date: 2025-09-14
tags: ['week38', 'robot', 'objects', 'encapsulation', 'instantiation', 'constructors', 'sensors']
---

```java
/**
 * ColorSensor Class - Identifies colors of objects using RGB light detection
 * 
 * This class simulates an RGB color sensor mounted on the robot's grabber
 * that can detect and identify the colors of objects positioned beneath it.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-09-14
 */
public class ColorSensor {  // 'public' means other classes can use this ColorSensor class
    
    // Constants - 'final' means these values cannot be changed after initialization
    // 'static' means they belong to the class, not individual objects
    // 'public' means other classes can access these constants
    public static final String COLOR_RED = "red";           // Red color constant
    public static final String COLOR_BLUE = "blue";         // Blue color constant
    public static final String COLOR_GREEN = "green";       // Green color constant
    public static final String COLOR_YELLOW = "yellow";     // Yellow color constant
    public static final String COLOR_ORANGE = "orange";     // Orange color constant
    public static final String COLOR_PURPLE = "purple";     // Purple color constant
    public static final String COLOR_WHITE = "white";       // White color constant
    public static final String COLOR_BLACK = "black";       // Black color constant
    public static final String COLOR_UNKNOWN = "unknown";   // When color cannot be determined
    public static final int MAX_RGB_VALUE = 255;            // Maximum RGB component value
    public static final int MIN_RGB_VALUE = 0;              // Minimum RGB component value
    public static final int DEFAULT_THRESHOLD = 50;         // Default sensitivity threshold
    
    // Instance variables - each ColorSensor object has its own copy of these
    // 'private' means only THIS ColorSensor class can directly access these variables
    private boolean isActive;           // boolean to track if sensor is powered on
    private String lastDetectedColor;   // String to store most recent color reading
    private int redValue;               // int to store red light intensity (0-255)
    private int greenValue;             // int to store green light intensity (0-255)
    private int blueValue;              // int to store blue light intensity (0-255)
    private int threshold;              // int for color detection sensitivity
    private boolean isCalibrated;       // boolean to track if sensor has been calibrated
    private String sensorName;          // String to identify this particular sensor
    
    /**
     * Constructor - Creates a new ColorSensor object with default settings
     * Constructor has same name as class and no return type
     * No parameters needed - uses default values
     */
    public ColorSensor() {  // 'public' so other classes can create ColorSensor objects
        // Initialize all instance variables to starting values
        this.isActive = true;                        // Sensor starts powered on
        this.lastDetectedColor = COLOR_UNKNOWN;      // No initial color reading
        this.redValue = MIN_RGB_VALUE;               // Start with no red detected
        this.greenValue = MIN_RGB_VALUE;             // Start with no green detected
        this.blueValue = MIN_RGB_VALUE;              // Start with no blue detected
        this.threshold = DEFAULT_THRESHOLD;          // Use default sensitivity
        this.isCalibrated = false;                   // Needs calibration
        this.sensorName = "Color Sensor";            // Default name
        
        System.out.println("Color sensor initialized - ready to detect colors");
    }
    
    /**
     * Constructor with custom name - Creates a ColorSensor with specified name
     * @param name Custom name for this sensor (parameter)
     */
    public ColorSensor(String name) {  // Overloaded constructor with parameter
        this();  // Call the default constructor first
        this.sensorName = name;  // Then set the custom name
        System.out.println(name + " color sensor initialized");
    }
    
    /**
     * Method to detect and identify the color of an object
     * Takes RGB readings and determines the dominant color
     * @return String name of detected color, or COLOR_UNKNOWN if cannot determine
     */
    public String detectColor() {  // 'public String' - returns a text string
        if (!isActive) {  // '!' means 'not' - if sensor is not active
            System.out.println("Color sensor is not active!");
            return COLOR_UNKNOWN;  // Return unknown color constant
        }
        
        // Simulate taking RGB readings from the sensor
        takeRGBReading();  // Get new red, green, blue values
        
        // Analyze the RGB values to determine the color
        String detectedColor = analyzeRGBValues();  // Process the readings
        
        this.lastDetectedColor = detectedColor;  // Store for future reference
        
        System.out.println("Color detected: " + detectedColor + 
                         " (R:" + redValue + " G:" + greenValue + " B:" + blueValue + ")");
        
        return detectedColor;  // Return the identified color
    }
    
    /**
     * Private helper method to simulate taking RGB light readings
     * This method is internal to the class - other classes cannot call it directly
     */
    private void takeRGBReading() {  // 'private void' - only this class can call this
        // Simulate sensor readings with random values to mimic real color detection
        this.redValue = (int)(Math.random() * (MAX_RGB_VALUE + 1));    // Random red (0-255)
        this.greenValue = (int)(Math.random() * (MAX_RGB_VALUE + 1));  // Random green (0-255)
        this.blueValue = (int)(Math.random() * (MAX_RGB_VALUE + 1));   // Random blue (0-255)
        
        // Method functionality coming soon!
    }
    
    /**
     * Private helper method to analyze RGB values and determine color
     * Uses thresholds and RGB combinations to identify colors
     * @return String name of the identified color
     */
    private String analyzeRGBValues() {  // 'private String' - internal method that returns text
        // Find which color component is strongest
        int maxValue = Math.max(Math.max(redValue, greenValue), blueValue);  // Find highest RGB value
        int minValue = Math.min(Math.min(redValue, greenValue), blueValue);  // Find lowest RGB value
        
        // Check for grayscale colors first (when all RGB values are similar)
        if (maxValue - minValue < threshold) {  // All RGB values are close together
            if (maxValue > 200) {
                return COLOR_WHITE;  // High values = white
            } else if (maxValue < 50) {
                return COLOR_BLACK;  // Low values = black
            }
        }
        
        // Check for primary and secondary colors based on dominant RGB components
        if (redValue > greenValue + threshold && redValue > blueValue + threshold) {
            return COLOR_RED;  // Red is dominant
        } else if (greenValue > redValue + threshold && greenValue > blueValue + threshold) {
            return COLOR_GREEN;  // Green is dominant
        } else if (blueValue > redValue + threshold && blueValue > greenValue + threshold) {
            return COLOR_BLUE;  // Blue is dominant
        } else if (redValue > threshold && greenValue > threshold && blueValue < threshold) {
            return COLOR_YELLOW;  // Red + Green = Yellow
        } else if (redValue > threshold && blueValue > threshold && greenValue < threshold) {
            return COLOR_PURPLE;  // Red + Blue = Purple
        } else if (redValue > threshold && greenValue > threshold/2 && blueValue < threshold) {
            return COLOR_ORANGE;  // Red + some Green = Orange
        }
        
        return COLOR_UNKNOWN;  // Cannot determine color
        // Method functionality coming soon!
    }
    
    /**
     * Method to check if a specific color is detected
     * @param targetColor The color to look for (parameter)
     * @return true if target color is detected, false otherwise
     */
    public boolean isColorDetected(String targetColor) {  // Returns boolean true/false
        String currentColor = detectColor();  // Take a new color reading
        boolean colorMatch = currentColor.equals(targetColor);  // Compare colors using .equals()
        
        if (colorMatch) {
            System.out.println("Target color " + targetColor + " detected!");
        }
        
        return colorMatch;  // Return true if colors match
    }
    
    /**
     * Method to calibrate the sensor for accurate color detection
     * Calibration improves color identification accuracy
     */
    public void calibrate() {
        System.out.println("Calibrating " + sensorName + "...");
        
        // Take several readings to establish color baselines
        for (int i = 0; i < 10; i++) {  // Take 10 calibration readings
            takeRGBReading();  // Simulate calibration process
        }
        
        this.isCalibrated = true;  // Mark sensor as calibrated
        System.out.println(sensorName + " calibration complete");
        // Method functionality coming soon!
    }
    
    /**
     * Method to activate or deactivate the sensor
     * @param active true to turn on, false to turn off (parameter)
     */
    public void setActive(boolean active) {
        this.isActive = active;  // Update the sensor's active state
        String status = active ? "activated" : "deactivated";  // Conditional operator
        System.out.println(sensorName + " " + status);
    }
    
    /**
     * Method to adjust color detection sensitivity
     * Lower threshold = more sensitive, higher threshold = less sensitive
     * @param newThreshold Sensitivity value (10-100 recommended)
     */
    public void setThreshold(int newThreshold) {
        // Ensure threshold is within reasonable range
        if (newThreshold < 10) {
            newThreshold = 10;  // Minimum sensitivity
        } else if (newThreshold > 100) {
            newThreshold = 100;  // Maximum sensitivity
        }
        
        this.threshold = newThreshold;  // Update the sensitivity threshold
        System.out.println(sensorName + " sensitivity threshold set to " + newThreshold);
    }
    
    /**
     * Method to get current RGB values as an array
     * Useful for advanced color analysis
     * @return int array containing [red, green, blue] values
     */
    public int[] getRGBValues() {  // Returns an array of integers
        return new int[]{redValue, greenValue, blueValue};  // Create and return array
    }
    
    // Getter methods - allow other classes to read our private variables safely
    
    /**
     * Getter method for sensor active status
     * @return true if sensor is active, false if inactive
     */
    public boolean isActive() {
        return isActive;  // Return our private boolean variable
    }
    
    /**
     * Getter method for last detected color
     * @return Most recent color identification as String
     */
    public String getLastDetectedColor() {
        return lastDetectedColor;  // Return our private String variable
    }
    
    /**
     * Getter method for current red light value
     * @return Red component intensity (0-255)
     */
    public int getRedValue() {
        return redValue;  // Return our private int variable
    }
    
    /**
     * Getter method for current green light value
     * @return Green component intensity (0-255)
     */
    public int getGreenValue() {
        return greenValue;  // Return our private int variable
    }
    
    /**
     * Getter method for current blue light value
     * @return Blue component intensity (0-255)
     */
    public int getBlueValue() {
        return blueValue;  // Return our private int variable
    }
    
    /**
     * Getter method for sensitivity threshold
     * @return Current threshold value
     */
    public int getThreshold() {
        return threshold;  // Return our private int variable
    }
    
    /**
     * Getter method for calibration status
     * @return true if sensor has been calibrated, false if not
     */
    public boolean isCalibrated() {
        return isCalibrated;  // Return our private boolean variable
    }
    
    /**
     * Getter method for sensor name
     * @return Name of this sensor
     */
    public String getSensorName() {
        return sensorName;  // Return our private String variable
    }
    
    /**
     * Method to get formatted status information about the sensor
     * @return String containing sensor status details
     */
    public String getStatus() {
        return sensorName + " [Active: " + (isActive ? "YES" : "NO") + 
               ", Calibrated: " + (isCalibrated ? "YES" : "NO") + 
               ", Last Color: " + lastDetectedColor + 
               ", RGB: (" + redValue + "," + greenValue + "," + blueValue + ")" +
               ", Threshold: " + threshold + "]";
    }
    
    /**
     * Method to display detailed sensor diagnostics
     * Shows all sensor information for troubleshooting
     */
    public void displayDiagnostics() {
        System.out.println("\n=== " + sensorName + " Diagnostics ===");
        System.out.println("Status: " + (isActive ? "ACTIVE" : "INACTIVE"));
        System.out.println("Calibrated: " + (isCalibrated ? "YES" : "NO"));
        System.out.println("Last Detected Color: " + lastDetectedColor);
        System.out.println("RGB Values: R=" + redValue + " G=" + greenValue + " B=" + blueValue);
        System.out.println("Sensitivity Threshold: " + threshold);
        System.out.println("Supported Colors: " + COLOR_RED + ", " + COLOR_GREEN + ", " + 
                         COLOR_BLUE + ", " + COLOR_YELLOW + ", " + COLOR_ORANGE + ", " + 
                         COLOR_PURPLE + ", " + COLOR_WHITE + ", " + COLOR_BLACK);
    }
    
    /**
     * Method to perform sensor self-test
     * Checks if sensor is functioning properly
     * @return true if sensor passes all tests, false if problems detected
     */
    public boolean performSelfTest() {
        System.out.println("Performing self-test on " + sensorName + "...");
        
        // Test 1: Check if sensor is active
        if (!isActive) {
            System.out.println("FAIL: Sensor is not active");
            return false;
        }
        
        // Test 2: Take a sample color reading
        takeRGBReading();  // Get RGB values
        if (redValue == 0 && greenValue == 0 && blueValue == 0) {
            System.out.println("FAIL: Cannot get sensor reading");
            return false;
        }
        
        // Test 3: Test color analysis
        String testColor = analyzeRGBValues();
        if (testColor == null) {
            System.out.println("FAIL: Color analysis not working");
            return false;
        }
        
        System.out.println("PASS: " + sensorName + " self-test successful");
        return true;  // All tests passed
        // Method functionality coming soon!
    }
}
```