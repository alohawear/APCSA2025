---

title: "Tank Sumulator v0.1"
description: "A tank simulator for goldfish objects."
date: 2025-09-10
tags: ['goldfish','week37','simulator']

---
```java
public class Tank {
    private static Goldfish[] fish = new Goldfish[10]; // Static array to hold up to 10 goldfish
    private static int fishCount = 0; // Track how many fish are currently in the tank
    private static int flakesCount = 0; // Number of flakes available in tank
    private static int tankCleanliness = 100; // 0-100 scale
    
    /**
     * Main method for running the tank.
     */
    public static void main(String[] args) {
        // Add some fish to the tank
        addFish("Goldie");
        addFish("Bubbles");
        addFish("Finn");
        
        // Display initial status
        displayTankStatus();
        
        // Run simulation with 5 cycles
        System.out.println("Starting Tank Simulation for 5 cycles...\n");
        
        for (int cycle = 1; cycle <= 5; cycle++) {
            System.out.println("\n======= CYCLE " + cycle + " =======");
            runCycle();
            System.out.println("\n--- End of Cycle " + cycle + " ---");
        }
        System.out.println("\nSimulation complete!");

        // Display final status
        displayTankStatus();
    }

    /**
     * Run one simulation cycle for all fish in the tank
     */
    public static void runCycle() {
        System.out.println("\n--- Running Tank Cycle ---");
        
        // Let each fish behave based on current tank conditions
        for (int i = 0; i < fishCount; i++) {
            System.out.println("\n" + fish[i].getName() + "'s turn:");
            
            // If there are flakes and fish is hungry, let it eat
            if (flakesCount > 0 && !fish[i].isFull()) {
                int flakesToEat = (int)(Math.random() * 4); // Random 0-3 flakes
                if (flakesToEat > flakesCount) {
                    flakesToEat = flakesCount; // Can't eat more than available
                }
                fish[i].eat(flakesToEat);
                flakesCount -= flakesToEat; // Remove eaten flakes from tank
                }

            // If fish is full, it poops
            if (fish[i].isFull()) {
                fish[i].poop();
                tankCleanliness -= 5; // Tank gets dirtier
            }
            
            // Fish swims for oxygen
            if (fish[i].needsOxygen()) {
                fish[i].swim(5, "fast");
            }
            
            // Fish might forget something
            if (fish[i].willForget()) {
                fish[i].forget();
            }
        
        }
        // Clean tank if it gets too dirty
        if (tankCleanliness < 50) {
            cleanTank();
        }
        System.out.println("Tank cleanliness: " + tankCleanliness + "%");
    }

    /**
     * Add a new goldfish to the tank
     * @param name The name of the new goldfish
     * @return true if fish was added, false if tank is full
     */
    public static boolean addFish(String name) {
        if (fishCount < fish.length) {
            fish[fishCount] = new Goldfish(name);
            fishCount++;
            System.out.println(name + " has been added to the tank!");
            return true;
        } else {
            System.out.println("Tank is full! Cannot add " + name);
            return false;
        }
    }
    
    /**
     * Drop fish flakes into the tank for all fish to eat
     */
    public static void dropFlakes() {
        int newFlakes = (int)(Math.random() * 8) + 3; // Random 3-10 flakes
        flakesCount += newFlakes;
        System.out.println(newFlakes + " fish flakes have been dropped into the tank! Total flakes: " + flakesCount);
    }
    
    /**
     * Clean the tank (resets cleanliness)
     */
    public static void cleanTank() {
        tankCleanliness = 100;
        System.out.println("The tank has been cleaned!");
    }
    
    /**
     * Display information about all fish in the tank
     */
    public static void displayTankStatus() {
        System.out.println("\n=== Tank Status ===");
        System.out.println("Fish in tank: " + fishCount + "/" + fish.length);
        System.out.println("Tank cleanliness: " + tankCleanliness + "%");
        System.out.println("Fish flakes available: " + flakesCount);

        System.out.println("\nFish details:");
        for (int i = 0; i < fishCount; i++) {
            System.out.println("- " + fish[i].getName() + " (Full: " + fish[i].isFull()+ ")");
        }
    }
}
```