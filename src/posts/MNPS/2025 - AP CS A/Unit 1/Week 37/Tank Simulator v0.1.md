---

title: "Tank Sumulator v0.1"
description: "A tank simulator for goldfish objects."
date: 2025-09-10
tags: ['goldfish','week37','simulator','objects','methods','parameters']

---
```java
public class Tank {
    private static Goldfish goldfish1;
    private static Goldfish goldfish2;
    private static Goldfish goldfish3;
    private static int flakesCount = 0; // Number of flakes available in tank
    private static int tankCleanliness = 100; // 0-100 scale
    
    /**
     * Main method for running the tank.
     */
    public static void main(String[] args) {
        // Create the three goldfish
        goldfish1 = new Goldfish("Goldie");
        goldfish2 = new Goldfish("Bubbles");
        goldfish3 = new Goldfish("Finn");
        
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
        
        // Handle each goldfish individually
        handleFish(goldfish1);
        handleFish(goldfish2);
        handleFish(goldfish3);
        
        // Clean tank if it gets too dirty
        if (tankCleanliness < 50) {
            cleanTank();
        }
        System.out.println("Tank cleanliness: " + tankCleanliness + "%");
    }
    
    /**
     * Handle behavior for an individual fish during one cycle
     * @param fish The goldfish to handle
     */
    private static void handleFish(Goldfish fish) {
        System.out.println("\n" + fish.getName() + "'s turn:");
        
        // If there are flakes and fish is hungry, let it eat
        if (flakesCount > 0 && !fish.isFull()) {
            int flakesToEat = (int)(Math.random() * 4); // Random 0-3 flakes
            if (flakesToEat > flakesCount) {
                flakesToEat = flakesCount; // Can't eat more than available
            }
            fish.eat(flakesToEat);
            flakesCount -= flakesToEat; // Remove eaten flakes from tank
        }

        // If fish is full, it poops
        if (fish.isFull()) {
            fish.poop();
            tankCleanliness -= 5; // Tank gets dirtier
        }
        
        // Fish swims for oxygen
        if (fish.needsOxygen()) {
            fish.swim(5, "fast");
        }
        
        // Fish might forget something
        if (fish.willForget()) {
            fish.forget();
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
        System.out.println("Fish in tank: 3/3");
        System.out.println("Tank cleanliness: " + tankCleanliness + "%");
        System.out.println("Fish flakes available: " + flakesCount);

        System.out.println("\nFish details:");
        System.out.println("- " + goldfish1.getName() + " (Full: " + goldfish1.isFull() + ")");
        System.out.println("- " + goldfish2.getName() + " (Full: " + goldfish2.isFull() + ")");
        System.out.println("- " + goldfish3.getName() + " (Full: " + goldfish3.isFull() + ")");
    }
}
```