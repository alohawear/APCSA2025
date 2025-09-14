---
title: "DistanceSensor v0.1"
description: "Ultrasonic distance sensor for measuring distances to obstacles."
date: 2025-09-14
tags: ['week38', 'robot', 'objects', 'encapsulation', 'instantiation', 'constructors', 'sensors']
---

```java
/**
 * DistanceSensor Class - Measures distances to obstacles using ultrasonic technology
 * 
 * This class simulates an ultrasonic distance sensor that can detect objects
 * and measure their distance from the robot for navigation and obstacle avoidance.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-09-14
 */
public class DistanceSensor {  // 'public' means other classes can use this DistanceSensor class
    
    // Constants - 'final' means these values cannot be changed after initialization
    // 'static' means they belong to the class, not individual objects
    // 'public' means other classes can access these constants
    public static final double MAX_RANGE_CM = 400.0;        // Maximum detection range in centimeters
    public static final double MIN_RANGE_CM = 2.0;          // Minimum detection range in centimeters
    public static final double OUT_OF_RANGE = -1.0;         // Value returned when no object detected
    public static final double MEASUREMENT_ERROR = 0.5;     // Typical measurement accuracy in cm
    public static final int DEFAULT_SAMPLE_COUNT = 3;       // Number of readings to average
    
    // Instance variables - each DistanceSensor object has its own copy of these
    // 'private' means only THIS DistanceSensor class can directly access these variables
    private boolean isActive;           // boolean to track if sensor is powered on
    private double lastReading;         // double to store the most recent distance measurement
    private int sampleCount;            // int for number of readings to average together
    private boolean isCalibrated;       // boolean to track if sensor has been calibrated
    private String sensorName;          // String to identify this particular sensor
    
    /**
     * Constructor - Creates a new DistanceSensor object with default settings
     * Constructor has same name as class and no return type
     * No parameters needed - uses default values
     */
    public DistanceSensor() {  // 'public' so other classes can create DistanceSensor objects
        // Initialize all instance variables to starting values
        this.isActive = true;                      // Sensor starts powered on
        this.lastReading = OUT_OF_RANGE;           // No initial reading
        this.sampleCount = DEFAULT_SAMPLE_COUNT;   // Use default sampling
        this.isCalibrated = false;                 // Needs calibration
        this.sensorName = "Distance Sensor";       // Default name
        
        System.out.println("Distance sensor initialized - range: " + 
                         MIN_RANGE_CM + " to " + MAX_RANGE_CM + " cm");
    }
    
    /**
     * Constructor with custom name - Creates a DistanceSensor with specified name
     * @param name Custom name for this sensor (parameter)
     */
    public DistanceSensor(String name) {  // Overloaded constructor with parameter
        this();  // Call the default constructor first
        this.sensorName = name;  // Then set the custom name
        System.out.println(name + " distance sensor initialized");
    }
    
    /**
     * Method to measure distance to nearest obstacle
     * Simulates taking ultrasonic readings and returns distance
     * @return Distance in centimeters, or OUT_OF_RANGE if no object detected
     */
    public double measureDistance() {  // 'public double' - returns a decimal number
        if (!isActive) {  // '!' means 'not' - if sensor is not active
            System.out.println("Distance sensor is not active!");
            return OUT_OF_RANGE;  // Return special value indicating no reading
        }
        
        // Simulate taking multiple readings and averaging them
        double totalDistance = 0.0;  // Accumulator for adding up readings
        
        for (int i = 0; i < sampleCount; i++) {  // Loop to take multiple samples
            double singleReading = takeSingleReading();  // Get one measurement
            totalDistance += singleReading;  // Add to running total
        }
        
        double averageDistance = totalDistance / sampleCount;  // Calculate average
        this.lastReading = averageDistance;  // Store for future reference
        
        return averageDistance;  // Return the averaged result
    }
    
    /**
     * Private helper method to simulate taking one distance reading
     * This method is internal to the class - other classes cannot call it directly
     * @return Single distance measurement in centimeters
     */
    private double takeSingleReading() {  // 'private' means only this class can call this method
        // Simulate sensor reading with some randomness to mimic real sensor behavior
        double simulatedDistance = Math.random() * MAX_RANGE_CM;  // Random distance
        
        // Add small measurement error to simulate real sensor inaccuracy
        double error = (Math.random() - 0.5) * MEASUREMENT_ERROR * 2;  // Random error
        simulatedDistance += error;
        
        // Ensure reading is within sensor's operating range
        if (simulatedDistance < MIN_RANGE_CM) {
            return MIN_RANGE_CM;  // Clamp to minimum range
        } else if (simulatedDistance > MAX_RANGE_CM) {
            return OUT_OF_RANGE;  // Return out of range value
        }
        
        return simulatedDistance;  // Return the simulated reading
        // Method functionality coming soon!
    }
    
    /**
     * Method to check if an obstacle is detected within specified distance
     * @param thresholdDistance Maximum distance to consider as "obstacle detected"
     * @return true if obstacle detected within threshold, false otherwise
     */
    public boolean isObstacleDetected(double thresholdDistance) {  // Returns boolean
        double currentDistance = measureDistance();  // Take a new measurement
        
        // Check if we got a valid reading and if it's within threshold
        boolean obstacleFound = (currentDistance != OUT_OF_RANGE) && 
                               (currentDistance <= thresholdDistance);
        
        if (obstacleFound) {
            System.out.println("Obstacle detected at " + currentDistance + " cm");
        }
        
        return obstacleFound;  // Return true if obstacle is within threshold distance
    }
    
    /**
     * Method to calibrate the sensor for accurate readings
     * Calibration improves measurement accuracy
     */
    public void calibrate() {
        System.out.println("Calibrating " + sensorName + "...");
        // Take several readings to establish baseline
        for (int i = 0; i < 10; i++) {  // Take 10 calibration readings
            takeSingleReading();  // Simulate calibration process
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
     * Method to set how many readings to average together
     * More samples = more accurate but slower readings
     * @param samples Number of readings to average (1-10)
     */
    public void setSampleCount(int samples) {
        // Ensure sample count is reasonable
        if (samples < 1) {
            samples = 1;  // At least one sample
        } else if (samples > 10) {
            samples = 10;  // No more than 10 samples
        }
        
        this.sampleCount = samples;  // Update the sample count
        System.out.println(sensorName + " sample count set to " + samples);
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
     * Getter method for last distance reading
     * @return Most recent distance measurement in centimeters
     */
    public double getLastReading() {
        return lastReading;  // Return our private double variable
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
     * Getter method for current sample count
     * @return Number of readings being averaged
     */
    public int getSampleCount() {
        return sampleCount;  // Return our private int variable
    }
    
    /**
     * Getter method for maximum detection range
     * @return Maximum range in centimeters
     */
    public double getMaxRange() {
        return MAX_RANGE_CM;  // Return our constant value
    }
    
    /**
     * Getter method for minimum detection range
     * @return Minimum range in centimeters  
     */
    public double getMinRange() {
        return MIN_RANGE_CM;  // Return our constant value
    }
    
    /**
     * Method to get formatted status information about the sensor
     * @return String containing sensor status details
     */
    public String getStatus() {
        return sensorName + " [Active: " + (isActive ? "YES" : "NO") + 
               ", Calibrated: " + (isCalibrated ? "YES" : "NO") + 
               ", Last Reading: " + lastReading + " cm" +
               ", Samples: " + sampleCount + "]";
    }
    
    /**
     * Method to display detailed sensor diagnostics
     * Shows all sensor information for troubleshooting
     */
    public void displayDiagnostics() {
        System.out.println("\n=== " + sensorName + " Diagnostics ===");
        System.out.println("Status: " + (isActive ? "ACTIVE" : "INACTIVE"));
        System.out.println("Calibrated: " + (isCalibrated ? "YES" : "NO"));
        System.out.println("Detection Range: " + MIN_RANGE_CM + " - " + MAX_RANGE_CM + " cm");
        System.out.println("Last Reading: " + lastReading + " cm");
        System.out.println("Sample Count: " + sampleCount);
        System.out.println("Measurement Accuracy: ±" + MEASUREMENT_ERROR + " cm");
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
        
        // Test 2: Take a sample reading
        double testReading = takeSingleReading();
        if (testReading == OUT_OF_RANGE) {
            System.out.println("FAIL: Cannot get sensor reading");
            return false;
        }
        
        System.out.println("PASS: " + sensorName + " self-test successful");
        return true;  // All tests passed
        // Method functionality coming soon!
    }
}
```