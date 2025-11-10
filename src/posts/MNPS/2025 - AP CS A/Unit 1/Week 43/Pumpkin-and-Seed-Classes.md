---
title: "Pumpkin and Seed Classes - Object Composition"
description: "Two related classes demonstrating object composition, where a Pumpkin contains an array of Seed objects."
date: 2025-10-23
tags: ['week43', 'class-design', 'arrays', 'object-composition', 'encapsulation']
---

# Pumpkin and Seed Classes - Object Composition

A Halloween-themed example demonstrating **object composition** - when one class contains objects of another class as instance variables.

## Class Diagram

![Pumpkin and Seed Class Diagram](/assets/images/diagrams/pumpkin-seed-class-diagram.svg "UML class diagram showing Pumpkin and Seed classes with composition relationship")

## The Seed Class

Represents a single pumpkin seed.

```java
public class Seed {
    // Instance variables
    private int mass;
    private boolean sprouted;

    // Constructor
    public Seed(int mass, boolean sprouted) {
        this.mass = mass;
        this.sprouted = sprouted;
    }

    // Getters
    public int getMass() {
        return mass;
    }

    public boolean getSprouted() {
        return sprouted;
    }

    // Method
    public void sprout() {
        sprouted = true;
    }
}
```

## The Pumpkin Class

Represents a pumpkin that contains an array of seeds.

```java
public class Pumpkin {
    // Instance variables
    private String color;
    private int mass;
    private double circumference;
    private boolean isCarved;
    private Seed[] seeds;

    // Constructor
    public Pumpkin(String color, int mass, double circumference, boolean isCarved) {
        this.color = color;
        this.mass = mass;
        this.circumference = circumference;
        this.isCarved = isCarved;
    }

    // Getters
    public String getColor() {
        return color;
    }

    public int getMass() {
        return mass;
    }

    public double getCircumference() {
        return circumference;
    }

    public boolean getIsCarved() {
        return isCarved;
    }

    public Seed[] getSeeds() {
        return seeds;
    }

    // Setters
    public void setSetColor(String color) {
        this.color = color;
    }

    public void setMass(int mass) {
        this.mass = mass;
    }

    public void setCircumference(double circumference) {
        this.circumference = circumference;
    }

    public void setIsCarved(boolean isCarved) {
        this.isCarved = isCarved;
    }

    public void setSeeds(Seed[] seeds) {
        this.seeds = seeds;
    }

    // Method
    public int countSeeds() {
        if (seeds == null) {
            return 0;
        }
        return seeds.length;
    }
}
```

## Key Concepts

1. **Object Composition**: Pumpkin contains an array of Seed objects
   - The Pumpkin class has a `Seed[] seeds` instance variable
   - This creates a "has-a" relationship: a Pumpkin *has* Seeds

2. **Encapsulation**: All instance variables are private
   - Access controlled through public getter and setter methods
   - Follows AP CS A best practices

3. **Array as Instance Variable**: `Seed[] seeds`
   - Not initialized in constructor (starts as `null`)
   - Must be set later using `setSeeds()`
   - Check for `null` before using (see `countSeeds()`)

4. **Boolean Getters**:
   - `getIsCarved()` and `getSprouted()` return boolean values
   - Common pattern for yes/no properties

5. **Mutator Method**: `sprout()`
   - Changes the internal state of a Seed object
   - Sets `sprouted` to `true`

## Example Usage

```java
// Create some seeds
Seed[] mySeeds = new Seed[100];
for (int i = 0; i < mySeeds.length; i++) {
    mySeeds[i] = new Seed(5, false);  // 5 grams, not sprouted
}

// Create a pumpkin
Pumpkin myPumpkin = new Pumpkin("Orange", 5000, 85.0, false);
myPumpkin.setSeeds(mySeeds);

// Check how many seeds
System.out.println("This pumpkin has " + myPumpkin.countSeeds() + " seeds");

// Carve the pumpkin
myPumpkin.setIsCarved(true);

// Plant a seed
mySeeds[0].sprout();
System.out.println("First seed sprouted: " + mySeeds[0].getSprouted());
```

## Important Note

Notice the typo in `setSetColor()` - the method name has an extra "Set". This is a common mistake when writing code! In a real project, this should be `setColor()`. This shows the importance of:
- Careful naming conventions
- Testing your code
- Code reviews
