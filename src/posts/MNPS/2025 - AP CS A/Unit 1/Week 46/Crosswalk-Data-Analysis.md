---
title: "Crosswalk Data Analysis System"
description: "A two-class system analyzing real Nashville sidewalk data using ArrayList, file reading, and data analysis algorithms."
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

## Class 1: Sidewalk

Represents a single sidewalk segment.

```java
/**
 * Represents a single sidewalk segment from the city's crosswalk survey data.
 *
 * Data fields:
 * - id: unique identifier for the sidewalk segment
 * - surveyor: initials of the person who surveyed this segment
 * - markings: type of markings (L = Longitudinal, T = Transverse)
 * - width: width of the sidewalk in feet
 * - condition: condition rating (P = Poor, F = Fair, G = Good)
 */
public class Sidewalk {
    // Instance variables - private for encapsulation
    private int id;
    private String surveyor;
    private String markings;
    private int width;
    private String condition;

    /**
     * Constructs a new Sidewalk object with the specified attributes.
     *
     * @param id the unique identifier for this sidewalk segment
     * @param surveyor the initials of the surveyor
     * @param markings the type of markings (L or T)
     * @param width the width of the sidewalk in feet
     * @param condition the condition rating (P, F, or G)
     */
    public Sidewalk(int id, String surveyor, String markings, int width, String condition) {
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
     * Returns a string representation of this Sidewalk.
     *
     * @return a formatted string containing all sidewalk information
     */
    public String toString() {
        return "Sidewalk #" + id + ": " + width + "ft wide, " +
               markings + " markings, Condition: " + condition +
               " (Surveyor: " + surveyor + ")";
    }
}
```

## Class 2: SidewalkAnalyzer

Loads and analyzes sidewalk data from CSV files.

```java
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Analyzes sidewalk data from the city's crosswalk survey.
 * Demonstrates ArrayList usage, file reading, and data analysis algorithms.
 */
public class SidewalkAnalyzer {
    private ArrayList<Sidewalk> sidewalks;

    /**
     * Constructs a new SidewalkAnalyzer with an empty list of sidewalks.
     */
    public SidewalkAnalyzer() {
        sidewalks = new ArrayList<Sidewalk>();
    }

    /**
     * Loads sidewalk data from a CSV file.
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

        // Read each line and create Sidewalk objects
        while (fileScanner.hasNextLine()) {
            String line = fileScanner.nextLine();
            String[] parts = line.split(",");

            // Parse the data: id,surveyor,markings,width,condition
            int id = Integer.parseInt(parts[0]);
            String surveyor = parts[1];
            String markings = parts[2];
            int width = Integer.parseInt(parts[3]);
            String condition = parts[4];

            Sidewalk s = new Sidewalk(id, surveyor, markings, width, condition);
            sidewalks.add(s);
        }

        fileScanner.close();
    }

    /**
     * Returns the total number of sidewalks in the dataset.
     *
     * @return the number of sidewalks
     */
    public int getTotalCount() {
        return sidewalks.size();
    }

    /**
     * Adds a sidewalk to the collection.
     * Demonstrates the ArrayList add() method.
     *
     * @param s the Sidewalk object to add
     */
    public void addSidewalk(Sidewalk s) {
        sidewalks.add(s);
    }

    /**
     * Removes all sidewalks surveyed by a specific person.
     * Demonstrates the ArrayList remove() method.
     * NOTE: When removing items, we must iterate BACKWARDS to avoid skipping elements.
     * We cannot use an enhanced for loop when removing items - it will cause
     * a ConcurrentModificationException.
     *
     * @param surveyorInitials the initials of the surveyor whose sidewalks should be removed
     * @return the number of sidewalks removed
     */
    public int removeSidewalksBySurveyor(String surveyorInitials) {
        int removedCount = 0;

        // Iterate backwards to safely remove items
        for (int i = sidewalks.size() - 1; i >= 0; i--) {
            if (sidewalks.get(i).getSurveyor().equals(surveyorInitials)) {
                sidewalks.remove(i);
                removedCount++;
            }
        }

        return removedCount;
    }

    /**
     * Counts the number of sidewalks in poor condition.
     * Demonstrates the counting pattern using an enhanced for loop.
     *
     * @return the count of sidewalks with condition "P"
     */
    public int countPoorCondition() {
        int count = 0;
        for (Sidewalk s : sidewalks) {
            if (s.getCondition().equals("P")) {
                count++;
            }
        }
        return count;
    }

    /**
     * Calculates the average width of all sidewalks.
     *
     * @return the average width in feet, or 0.0 if no sidewalks
     */
    public double getAverageWidth() {
        if (sidewalks.size() == 0) {
            return 0.0;
        }

        int totalWidth = 0;
        for (Sidewalk s : sidewalks) {
            totalWidth += s.getWidth();
        }

        return (double) totalWidth / sidewalks.size();
    }

    /**
     * Finds the sidewalk with the maximum width.
     *
     * @return the Sidewalk with the greatest width, or null if list is empty
     */
    public Sidewalk getWidestSidewalk() {
        if (sidewalks.size() == 0) {
            return null;
        }

        Sidewalk widest = sidewalks.get(0);
        for (Sidewalk s : sidewalks) {
            if (s.getWidth() > widest.getWidth()) {
                widest = s;
            }
        }
        return widest;
    }

    /**
     * Finds the sidewalk with the minimum width.
     *
     * @return the Sidewalk with the smallest width, or null if list is empty
     */
    public Sidewalk getNarrowestSidewalk() {
        if (sidewalks.size() == 0) {
            return null;
        }

        Sidewalk narrowest = sidewalks.get(0);
        for (Sidewalk s : sidewalks) {
            if (s.getWidth() < narrowest.getWidth()) {
                narrowest = s;
            }
        }
        return narrowest;
    }

    /**
     * Prints a summary report of the sidewalk data.
     */
    public void printSummary() {
        System.out.println("=== Sidewalk Survey Summary ===");
        System.out.println("Total sidewalks surveyed: " + getTotalCount());
        System.out.println("Sidewalks in poor condition: " + countPoorCondition());
        System.out.println();

        System.out.println("Width Statistics:");
        System.out.println("  Average width: " + getAverageWidth() + " feet");

        Sidewalk widest = getWidestSidewalk();
        if (widest != null) {
            System.out.println("  Widest: " + widest.getWidth() + " feet (ID #" + widest.getId() + ")");
        }

        Sidewalk narrowest = getNarrowestSidewalk();
        if (narrowest != null) {
            System.out.println("  Narrowest: " + narrowest.getWidth() + " feet (ID #" + narrowest.getId() + ")");
        }
    }

    /**
     * Main method to demonstrate the SidewalkAnalyzer.
     * @throws FileNotFoundException if the data file cannot be found
     */
    public static void main(String[] args) throws FileNotFoundException {
        SidewalkAnalyzer analyzer = new SidewalkAnalyzer();
        analyzer.loadData("crosswalk.csv");
        analyzer.printSummary();

        System.out.println();
        System.out.println("=== Sample Data ===");
        System.out.println("First 5 sidewalks:");
        for (int i = 0; i < 5 && i < analyzer.getTotalCount(); i++) {
            System.out.println(analyzer.sidewalks.get(i));
        }
    }
}
```

## Key Concepts Demonstrated

1. **Encapsulation**: Private instance variables with public getter methods
2. **Object Relationships**: SidewalkAnalyzer contains an ArrayList of Sidewalk objects
3. **File I/O**: Reading CSV data using Scanner
4. **ArrayList Operations**: add(), get(), remove(), size()
5. **Data Analysis Patterns**:
   - Counting pattern (countPoorCondition)
   - Averaging pattern (getAverageWidth)
   - Finding min/max (getNarrowestSidewalk, getWidestSidewalk)
6. **Safe Removal**: Iterating backwards when removing from ArrayList

## Expected Output

```
=== Sidewalk Survey Summary ===
Total sidewalks surveyed: 1824
Sidewalks in poor condition: 621

Width Statistics:
  Average width: 9.8 feet
  Widest: 20 feet (ID #20)
  Narrowest: 5 feet (ID #12)
```
