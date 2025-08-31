---
title: "Dealing with Booleanish Strings"
description: "There's no easy way to tell that 0, No, and False are the same. Here's one way."
date: 2025-08-25
tags: ['week35','boolean','sample']
---

```java
import java.util.Scanner;

public class BooleanHandler {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        System.out.print("Enter yes/no, true/false, or 1/0: ");
        String userInput = input.nextLine().toLowerCase().trim();
        
        // Custom method to handle various boolean representations
        boolean result = parseFlexibleBoolean(userInput);
        System.out.println("You entered: " + result);
        
        input.close();
    }
    public static boolean parseBoolean(String input) {
        input = input.toLowerCase().trim();
        if (input.equals("true") || input.equals("yes") || 
            input.equals("y") || input.equals("1")) {
            return true;
        }
        if (input.equals("false") || input.equals("no") || 
            input.equals("n") || input.equals("0")) {
            return false;
        }
        System.out.println("Invalid input: " + input + ". Defaulting to false.");
        return false;
    }
}
```