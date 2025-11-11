---
title: "Crosswalk Data Analysis System"
description: "A two-class system analyzing real Nashville crosswalk data using ArrayList, file reading, and data analysis algorithms."
date: 2025-11-09
tags: ['week46', 'arraylist', 'file-reading', 'data-analysis', 'object-relationships']
---

# Crosswalk Data Analysis System

A data analysis project using real Nashville Open Data to demonstrate:
- Object-oriented design with multiple classes
- ArrayList operations (add, remove, get, size)
- File reading with Scanner
- Data analysis algorithms (counting, averaging, finding min/max)

## The Data: crosswalk.csv

```csv
id,surveyor,markings,width,condition
1,RL,L,10,P
2,CW,L,10,P
3,JoS,L,10,P
...
```

**Fields:**
- `id` - unique identifier
- `surveyor` - surveyor initials
- `markings` - L (Longitudinal) or T (Transverse)
- `width` - width in feet
- `condition` - P (Poor), F (Fair), or G (Good)

## Class 1: Crosswalk

Represents a single crosswalk segment.

```java
/**
 * Represents a single crosswalk segment from the city's crosswalk survey data.
 *
 * Data fields:
 * - id: unique identifier for the crosswalk segment
 * - surveyor: initials of the person who surveyed this segment
 * - markings: type of markings (L = Longitudinal, T = Transverse)
 * - width: width of the crosswalk in feet
 * - condition: condition rating (P = Poor, F = Fair, G = Good)
 */
public class Crosswalk {
    // Instance variables - private for encapsulation
    private int id;
    private String surveyor;
    private String markings;
    private int width;
    private String condition;

    /**
     * Constructs a new Crosswalk object with the specified attributes.
     *
     * @param id the unique identifier for this crosswalk segment
     * @param surveyor the initials of the surveyor
     * @param markings the type of markings (L or T)
     * @param width the width of the crosswalk in feet
     * @param condition the condition rating (P, F, or G)
     */
    public Crosswalk(int id, String surveyor, String markings, int width, String condition) {
        this.id = id;
        this.surveyor = surveyor;
        this.markings = markings;
        this.width = width;
        this.condition = condition;
    }

    // Getter methods

    public int getId() {
        return id;
    }

    public String getSurveyor() {
        return surveyor;
    }

    public String getMarkings() {
        return markings;
    }

    public int getWidth() {
        return width;
    }

    public String getCondition() {
        return condition;
    }

    /**
     * Returns a string representation of this Crosswalk.
     *
     * @return a formatted string containing all crosswalk information
     */
    public String toString() {
        return "Crosswalk #" + id + ": " + width + "ft wide, " +
               markings + " markings, Condition: " + condition +
               " (Surveyor: " + surveyor + ")";
    }
}
```

## Class 2: CrosswalkAnalyzer

Loads and analyzes crosswalk data from CSV files.

```java
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Analyzes crosswalk data from the city's crosswalk survey.
 * Demonstrates ArrayList usage, file reading, and data analysis algorithms.
 */
public class CrosswalkAnalyzer {
    private ArrayList<Crosswalk> crosswalks;

    /**
     * Constructs a new CrosswalkAnalyzer with an empty list of crosswalks.
     */
    public CrosswalkAnalyzer() {
        crosswalks = new ArrayList<Crosswalk>();
    }

    /**
     * Loads crosswalk data from a CSV file.
     *
     * @param filename the name of the CSV file to read
     * @throws FileNotFoundException if the file cannot be found
     */
    public void loadData(String filename) throws FileNotFoundException {
        File dataFile = new File(filename);
        Scanner fileScanner = new Scanner(dataFile);

        // Skip the header line
        if (fileScanner.hasNextLine()) {
            fileScanner.nextLine();
        }

        // Read each line and create Crosswalk objects
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");

            // Parse the data: id,surveyor,markings,width,condition
            int id = Integer.parseInt(parts[0]);
            String surveyor = parts[1];
            String markings = parts[2];
            int width = Integer.parseInt(parts[3]);
            String condition = parts[4];

            Crosswalk c = new Crosswalk(id, surveyor, markings, width, condition);
            crosswalks.add(c);
        }

        fileScanner.close();
    }

    /**
     * Returns the total number of crosswalks in the dataset.
     *
     * @return the number of crosswalks
     */
    public int getTotalCount() {
        return crosswalks.size();
    }

    /**
     * Adds a crosswalk to the collection.
     * Demonstrates the ArrayList add() method.
     *
     * @param c the Crosswalk object to add
     */
    public void addCrosswalk(Crosswalk c) {
        crosswalks.add(c);
    }

    /**
     * Removes all crosswalks surveyed by a specific person.
     * Demonstrates the ArrayList remove() method.
     * NOTE: When removing items, we must iterate BACKWARDS to avoid skipping elements.
     * We cannot use an enhanced for loop when removing items - it will cause
     * a ConcurrentModificationException.
     *
     * @param surveyorInitials the initials of the surveyor whose crosswalks should be removed
     * @return the number of crosswalks removed
     */
    public int removeCrosswalksBySurveyor(String surveyorInitials) {
        int removedCount = 0;

        // Iterate backwards to safely remove items
        for (int i = crosswalks.size() - 1; i >= 0; i--) {
            if (crosswalks.get(i).getSurveyor().equals(surveyorInitials)) {
                crosswalks.remove(i);
                removedCount++;
            }
        }

        return removedCount;
    }

    /**
     * Counts the number of crosswalks in poor condition.
     * Demonstrates the counting pattern using an enhanced for loop.
     *
     * @return the count of crosswalks with condition "P"
     */
    public int countPoorCondition() {
        int count = 0;
        for (Crosswalk c : crosswalks) {
            if (c.getCondition().equals("P")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the average width of all crosswalks.
     *
     * @return the average width in feet, or 0.0 if no crosswalks
     */
    public double getAverageWidth() {
        if (crosswalks.size() == 0) {
            return 0.0;
        }

        int totalWidth = 0;
        for (Crosswalk c : crosswalks) {
            totalWidth += c.getWidth();
        }

        return (double) totalWidth / crosswalks.size();
    }

    /**
     * Finds the crosswalk with the maximum width.
     *
     * @return the Crosswalk with the greatest width, or null if list is empty
     */
    public Crosswalk getWidestCrosswalk() {
        if (crosswalks.size() == 0) {
            return null;
        }

        Crosswalk widest = crosswalks.get(0);
        for (Crosswalk c : crosswalks) {
            if (c.getWidth() > widest.getWidth()) {
                widest = c;
            }
        }
        return widest;
    }

    /**
     * Finds the crosswalk with the minimum width.
     *
     * @return the Crosswalk with the smallest width, or null if list is empty
     */
    public Crosswalk getNarrowestCrosswalk() {
        if (crosswalks.size() == 0) {
            return null;
        }

        Crosswalk narrowest = crosswalks.get(0);
        for (Crosswalk c : crosswalks) {
            if (c.getWidth() < narrowest.getWidth()) {
                narrowest = c;
            }
        }
        return narrowest;
    }

    /**
     * Prints a summary report of the crosswalk data.
     */
    public void printSummary() {
        System.out.println("=== Crosswalk Survey Summary ===");
        System.out.println("Total crosswalks surveyed: " + getTotalCount());
        System.out.println("Crosswalks in poor condition: " + countPoorCondition());
        System.out.println();

        System.out.println("Width Statistics:");
        System.out.println("  Average width: " + getAverageWidth() + " feet");

        Crosswalk widest = getWidestCrosswalk();
        if (widest != null) {
            System.out.println("  Widest: " + widest.getWidth() + " feet (ID #" + widest.getId() + ")");
        }

        Crosswalk narrowest = getNarrowestCrosswalk();
        if (narrowest != null) {
            System.out.println("  Narrowest: " + narrowest.getWidth() + " feet (ID #" + narrowest.getId() + ")");
        }
    }

    /**
     * Main method to demonstrate the CrosswalkAnalyzer.
     * @throws FileNotFoundException if the data file cannot be found
     */
    public static void main(String[] args) throws FileNotFoundException {
        CrosswalkAnalyzer analyzer = new CrosswalkAnalyzer();
        analyzer.loadData("crosswalk.csv");
        analyzer.printSummary();

        System.out.println();
        System.out.println("=== Sample Data ===");
        System.out.println("First 5 crosswalks:");
        for (int i = 0; i < 5 && i < analyzer.getTotalCount(); i++) {
            System.out.println(analyzer.crosswalks.get(i));
        }
    }
}
```

## Key Concepts Demonstrated

1. **Encapsulation**: Private instance variables with public getter methods
2. **Object Relationships**: CrosswalkAnalyzer contains an ArrayList of Crosswalk objects
3. **File I/O**: Reading CSV data using Scanner
4. **ArrayList Operations**: add(), get(), remove(), size()
5. **Data Analysis Patterns**:
   - Counting pattern (countPoorCondition)
   - Averaging pattern (getAverageWidth)
   - Finding min/max (getNarrowestCrosswalk, getWidestCrosswalk)
6. **Safe Removal**: Iterating backwards when removing from ArrayList

## Expected Output

```
=== Crosswalk Survey Summary ===
Total crosswalks surveyed: 1824
Crosswalks in poor condition: 621

Width Statistics:
  Average width: 9.8 feet
  Widest: 20 feet (ID #20)
  Narrowest: 5 feet (ID #12)
```
