---
title: "How Math.random() Works"
description: "This code demonstrates how random number generation work with Math.random()."
date: 2025-08-25
tags: ['week34','Math.random()']
---
```java
public class RandomExamples {
    
    // Static variables - available to all methods in this class
    static boolean isLucky = false;
    static double randomValue = 0.0;
    static String result = "";
    
    public static void main(String[] args) {
        // Main method - program starts here
        
        // Basic Math.random() - returns 0.0 to 0.999...
        randomValue = Math.random();
        System.out.println("Random decimal: " + randomValue);
        
        // Random integer from 1 to 6 (dice roll)
        int diceRoll = (int)(Math.random() * 6) + 1;
        System.out.println("Dice roll: " + diceRoll);
        
        // Random integer from 1 to 10
        int randomTen = (int)(Math.random() * 10) + 1;
        System.out.println("Random 1-10: " + randomTen);
        
        // Call our custom method
        generateLuckyNumber();
    }
    
    public static void generateLuckyNumber() {
        // Generate random number from 1 to 100
        int luckyNumber = (int)(Math.random() * 100) + 1;
        
        // Check if it's "lucky" (greater than 75)
        isLucky = luckyNumber > 75;
        
        if (isLucky) {
            result = "Lucky number: " + luckyNumber + " - You're lucky!";
        } else {
            result = "Number: " + luckyNumber + " - Try again!";
        }
        
        System.out.println(result);
    }
    
}
```