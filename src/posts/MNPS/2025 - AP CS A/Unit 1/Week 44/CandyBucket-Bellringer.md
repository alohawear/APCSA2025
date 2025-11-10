---
title: "CandyBucket Class Bellringer"
description: "Class design exercise creating a CandyBucket class with instance variables, static variables, and a method that returns a boolean to indicate success."
date: 2025-10-27
tags: ['week44', 'class-design', 'static-variables', 'boolean-return']
---

## 2025-10-27 CandyBucket Class Bellringer

![CandyBucket Class Diagram](/assets/images/diagrams/candybucket-class-diagram.svg "UML class diagram for CandyBucket class with static variable")

What's up with addCandy(). It returns a boolean that indicates if candy was successfully added to the bucket! If the bucket can't take any more, then we don't add the candy and we return False. If the bucket can take more, we add the candy and return True.

Okay! Time to write the CandyBucket class! By all means, use BlueJ!

---

## Proposed Implementation

```java
public class CandyBucket {
    // Instance variables
    private String color;
    private int candyCount;
    private double weightInOunces;

    // Static class variable
    private static int maxCandy = 50;

    // Constructor method
    public CandyBucket(String color) {
        this.color = color;
        this.candyCount = 0;
        this.weightInOunces = 0.0;
    }

    // Getter methods
    public String getColor() {
        return color;
    }

    public int getCandyCount() {
        return candyCount;
    }

    public double getWeightInOunces() {
        return weightInOunces;
    }

    public int getMaxCandy() {
        return maxCandy;
    }

    // Setter method
    public void setColor(String color) {
        this.color = color;
    }

    // Other methods
    public boolean addCandy(int pieces) {
        if (candyCount >= maxCandy) {
            return false;  // Bucket is full, can't add more
        }
        candyCount += pieces;
        // Assume each piece weighs about 0.5 ounces on average
        weightInOunces += pieces * 0.5;
        return true;  // Successfully added candy
    }
}
```
