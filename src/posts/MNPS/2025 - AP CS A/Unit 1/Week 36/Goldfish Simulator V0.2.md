---

title: "Goldfish Simulator v0.2"
description: "An iteration on the Goldfish Simulator. Using methods to simplify code. Passing parameters into methods."
date: 2025-09-02
tags: ['goldfish','methods','parameters','week36']

---

```java
/**
 * Goldfish Simulator v0.2 - Demonstrates methods with parameters
 * 
 * An iteration on the Goldfish Simulator showing how to use methods to simplify code
 * and pass parameters into methods for AP Computer Science A students.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-09-02
 */
public class Goldfish {
    private static boolean isFull = false;
    private static boolean existsFishFlakes = false;
    private static boolean needsOxygen = true;
    private static boolean willForget = false;
    
    public static void main(String[] args) {
        System.out.println("Goldfish Simulator - Starting 10 cycles...");
        
        for (int cycle = 1; cycle <= 10; cycle++) {
            System.out.println("=== Cycle " + cycle + " ===");
            
            if (existsFishFlakes) {
                eat();
            }   
            if (isFull) {
                poop();
            }
            if (needsOxygen) {
                swim(2);
            }
            if (willForget) {
                forget();
            }
            
        }
        
        System.out.println("Goldfish Simulator - Complete!");
    }
    
    public static void eat() {
        System.out.println("The goldfish ate some fish flakes.");
        isFull = true;
        existsFishFlakes = false;
    }
    
    public static void swim(int cm) {
        for (int distance = 1; distance <= cm; distance++){
            System.out.println("The goldfish swam 1 centimeter.");   
        }
        willForget = Math.random() < 0.5;
        existsFishFlakes = Math.random() < 0.1;
    }
    
    public static void poop() {
        System.out.println("The goldfish pooped because it was full.");
        isFull = false;
    }
    
    public static void forget() {
        System.out.println("The goldfish forgot... something...");
    }
}
```
