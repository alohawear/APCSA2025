---

title: "Strings that Look Like Numbers"
description: "We'll often get strings for input that need to be handled as numbers. Java won't automatically convert for you. Here's now to do the conversion."
date: 2025-08-25
tags: ['strings','casting','integer', 'double']

---

```java
import java.util.Scanner;

/**
 * StringParsingDemo - Demonstrates string to number conversion
 * 
 * Shows how to convert strings that look like numbers into actual integer
 * and double values for AP Computer Science A students studying type conversion.
 *
 * @author N. Reveal
 * @author Claude Code (Anthropic AI)
 * @version 2025-08-25
 */
public class StringParsingDemo {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        
        // Get a string from user and convert to integer
        System.out.print("Enter a whole number: ");
        String numberString = input.nextLine();
        int myInt = Integer.parseInt(numberString);
        
        System.out.println("You entered: " + myInt);
        System.out.println("Double it: " + (myInt * 2));
        
        // Get a string from user and convert to double
        System.out.print("Enter a decimal number: ");
        String decimalString = input.nextLine();
        double myDouble = Double.parseDouble(decimalString);
        
        System.out.println("You entered: " + myDouble);
        System.out.println("Add 1.5: " + (myDouble + 1.5));
        
        input.close();
    }
}
```