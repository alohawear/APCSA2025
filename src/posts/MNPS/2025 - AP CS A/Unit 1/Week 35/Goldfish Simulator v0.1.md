---

title: "Goldfish Simulator v0.1"
description: "This code demonstrates how to use methods from within the main method."
date: 2025-08-25
tags: ['week35','methods']

---
```java
public class Goldfish {
    private static boolean isFull = false;
    public static void main(String[] args) {
        boolean existsFishFlakes = Math.random() < 0.1;
        boolean needsOxygen = true;
        boolean willForget = Math.random() < 0.5;
        if (existsFishFlakes) {
            eat();
            isFull = true;
        }   
        if (isFull) {
            poop();
            isFull = false;
        }
        if (needsOxygen) {
            swim();
        }
        if (willForget) {
            forget();
        }
    }
    public static void eat() {
        System.out.println("The goldfish ate some fish flakes!");
    }
    public static void swim() {
        System.out.println("The goldfish is swimming around for oxygen.");
    }
    public static void poop() {
        System.out.println("The goldfish pooped because it was full.");
    }
    public static void forget() {
        System.out.println("The goldfish forgot... something...");
    }
}
```