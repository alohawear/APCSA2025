---

title: "Goldfish Simulator v0.4"
description: "An object enabled goldfish simulator."
date: 2025-09-10
tags: ['week37','simulator','goldfish','methods','objects','parameters']

---
```java
public class Goldfish {
    // Instance variables - each goldfish has its own state
    private String name;
    private boolean isFull;
    private boolean needsOxygen;
    private boolean willForget;
    private int distance;
    private String speed;
    
    /**
     * Constructor to create a new goldfish
     * @param fishName The name of this goldfish
     */
    public Goldfish(String fishName) {
        this.name = fishName;
        this.isFull = false;
        this.needsOxygen = true;
        this.willForget = false;
        this.distance = 5;
        this.speed = "fast";
    }
    
    /**
     * Goldfish eats the specified number of flakes
     * @param flakes Number of flakes to eat
     */
    public void eat(int flakes) {
        System.out.println(name + " ate " + flakes + " fish flakes!");
        isFull = true;
    }
    
    /**
     * Goldfish swims for oxygen
     * @param swimDistance How far to swim
     * @param swimSpeed How fast to swim
     */
    public void swim(int swimDistance, String swimSpeed) {
        System.out.println(name + " is swimming " + swimSpeed + " for " + swimDistance + " centimeters to get oxygen.");
        willForget = Math.random() < 0.5; // Random chance fish will forget something after swimming
        }
    
    /**
     * Goldfish poops when full
     */
    public void poop() {
        System.out.println(name + " pooped because it was full.");
        isFull = false;
    }
    
    /**
     * Goldfish forgets something
     */
    public void forget() {
        System.out.println(name + " forgot... something...");
        willForget = false;
    }
    
    // Getter methods to check goldfish state
    public String getName() {
        return name;
    }
    
    public boolean isFull() {
        return isFull;
    }
    
    public boolean needsOxygen() {
        return needsOxygen;
    }
    
    public boolean willForget() {
        return willForget;
    }
    
    public int getDistance() {
        return distance;
    }
    
    public String getSpeed() {
        return speed;
    }
    
    // Setter methods to modify goldfish state
    public void setDistance(int newDistance) {
        this.distance = newDistance;
    }
    
    public void setSpeed(String newSpeed) {
        this.speed = newSpeed;
    }
    
    /**
     * Get a string representation of this goldfish's current state
     * @return String describing the goldfish
     */
    public String toString() {
        return name + " [Full: " + isFull + "]";
    }
    
}
```