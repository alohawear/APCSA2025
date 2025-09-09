---

title: "goldfish simulator v0.3"
description: "a goldfish simulator with methods that pass parameters."
date: 2025-09-09
tags: ['goldfish','methods','week37','parameters']

---

```java
public class Goldfish {
    private static boolean isFull = false;
    private static boolean existsFishFlakes = false;
    private static boolean needsOxygen = true;
    private static boolean willForget = false;
    private static int swimDistance = 5;
    private static String swimSpeed = "fast";
    
    public static void main(String[] args) {
        System.out.println("Goldfish Simulator - Starting 10 cycles...\n");
        
        for (int cycle = 1; cycle <= 10; cycle++) {
            System.out.println("=== Cycle " + cycle + " ===");
            
            if (existsFishFlakes) {
                eat(3); // Pass number of flakes directly
            }   
            if (isFull) {
                poop(); // No parameters
            }
            if (needsOxygen) {
                swim(swimDistance, swimSpeed); // Pass class variables
            }
            if (willForget) {
                forget();
            }
            
            System.out.println();
        }
        
        System.out.println("Goldfish Simulator - Complete!");
    }
    
    // Method with 1 parameter (integer)
    public static void eat(int flakes) {
        System.out.println("The goldfish ate " + flakes + " fish flakes!");
        isFull = true;
        existsFishFlakes = false;
    }
    
    // Method with 2 parameters (integer and string)
    public static void swim(int dist, String spd) {
        System.out.println("The goldfish is swimming " + spd + " for " + dist + " inches to get oxygen.");
        willForget = Math.random() < 0.5;
        existsFishFlakes = Math.random() < 0.1;
    }
    
    // Method with 0 parameters
    public static void poop() {
        System.out.println("The goldfish pooped because it was full.");
        isFull = false;
    }
    
    // Method with 0 parameters
    public static void forget() {
        System.out.println("The goldfish forgot... something...");
    }
}
```