---
title: "Robot System Class Diagram"
description: "UML class diagram showing the complete robot system structure and relationships."
date: 2025-09-14
tags: ['week38', 'robot', 'objects', 'encapsulation', 'composition', 'uml', 'class-diagram']
---

# Robot System Class Diagram

This diagram shows the complete structure of our robot system, including all classes, their attributes, methods, and relationships.

## Complete Robot System Overview

```mermaid
---
config:
  theme: 'forest'
---

classDiagram
    class Robot {
        +TOTAL_WHEELS: int = 4
        +MAX_POWER_LEVEL: int = 100
        +MIN_POWER_LEVEL: int = 0
        +ROBOT_VERSION: String = "1.0"
        +SAFE_DISTANCE_CM: double = 10.0
        -frontRight: Wheel
        -frontLeft: Wheel
        -rearRight: Wheel
        -rearLeft: Wheel
        -grabber: Grabber
        -distanceSensor: DistanceSensor
        -colorSensor: ColorSensor
        -robotName: String
        -isPoweredOn: boolean
        +Robot(String name)
        +powerOn(): void
        +powerOff(): void
        +moveForward(int power): void
        +moveBackward(int power): void
        +turnLeft(int power): void
        +turnRight(int power): void
        +stopAllWheels(): void
        +shouldStop(): boolean
        +getDistance(): double
        +getColor(): String
        +extendGrabber(double extension): void
        +operateGrabber(String action): void
        +isGrabberHolding(): boolean
        +getName(): String
        +isPoweredOn(): boolean
        +getStatus(): String
        +displayDiagnostics(): void
    }

    class Wheel {
        +MAX_POWER: int = 100
        +MIN_POWER: int = -100
        +NEUTRAL_POWER: int = 0
        +MAX_RPM: int = 1000
        +RPM_CONVERSION_FACTOR: int = 10
        +DIRECTION_FORWARD: String = "forward"
        +DIRECTION_REVERSE: String = "reverse"
        +DIRECTION_STOPPED: String = "stopped"
        -wheelName: String
        -powerLevel: int
        -isRotating: boolean
        -direction: String
        -rotationSpeed: int
        -isBlocked: boolean
        +Wheel(String name)
        +setPower(int power): void
        +increasePower(int increment): void
        +decreasePower(int decrement): void
        +stop(): void
        +reverse(): void
        +setBlocked(boolean blocked): void
        +getName(): String
        +getPower(): int
        +isRotating(): boolean
        +getDirection(): String
        +getRotationSpeed(): int
        +isBlocked(): boolean
        +setName(String newName): void
        +getStatus(): String
        +displayDiagnostics(): void
        +performMaintenanceCheck(): boolean
    }

    class Grabber {
        +MAX_EXTENSION_CM: double = 30.0
        +MIN_EXTENSION_CM: double = 0.0
        +MAX_GRIP_STRENGTH: int = 100
        +MIN_GRIP_STRENGTH: int = 0
        +DEFAULT_GRIP_STRENGTH: int = 50
        +NO_OBJECT: String = "none"
        -extensionLength: double
        -isOpen: boolean
        -isHolding: boolean
        -heldObject: String
        -isExtending: boolean
        -gripStrength: int
        +Grabber()
        +extend(double targetLength): void
        +retract(): void
        +operate(String action): void
        +grabObject(String objectName): boolean
        +dropObject(): void
        +setGripStrength(int strength): void
        +getExtensionLength(): double
        +getMaxExtension(): double
        +isOpen(): boolean
        +isHolding(): boolean
        +getHeldObject(): String
        +isExtending(): boolean
        +getGripStrength(): int
        +getStatus(): String
        +displayDiagnostics(): void
        +isFullyExtended(): boolean
        +isFullyRetracted(): boolean
        +performMaintenanceCheck(): boolean
    }

    class DistanceSensor {
        +MAX_RANGE_CM: double = 400.0
        +MIN_RANGE_CM: double = 2.0
        +OUT_OF_RANGE: double = -1.0
        +MEASUREMENT_ERROR: double = 0.5
        +DEFAULT_SAMPLE_COUNT: int = 3
        -isActive: boolean
        -lastReading: double
        -sampleCount: int
        -isCalibrated: boolean
        -sensorName: String
        +DistanceSensor()
        +DistanceSensor(String name)
        +measureDistance(): double
        -takeSingleReading(): double
        +isObstacleDetected(double threshold): boolean
        +calibrate(): void
        +setActive(boolean active): void
        +setSampleCount(int samples): void
        +isActive(): boolean
        +getLastReading(): double
        +isCalibrated(): boolean
        +getSensorName(): String
        +getSampleCount(): int
        +getMaxRange(): double
        +getMinRange(): double
        +getStatus(): String
        +displayDiagnostics(): void
        +performSelfTest(): boolean
    }

    class ColorSensor {
        +COLOR_RED: String = "red"
        +COLOR_BLUE: String = "blue"
        +COLOR_GREEN: String = "green"
        +COLOR_YELLOW: String = "yellow"
        +COLOR_ORANGE: String = "orange"
        +COLOR_PURPLE: String = "purple"
        +COLOR_WHITE: String = "white"
        +COLOR_BLACK: String = "black"
        +COLOR_UNKNOWN: String = "unknown"
        +MAX_RGB_VALUE: int = 255
        +MIN_RGB_VALUE: int = 0
        +DEFAULT_THRESHOLD: int = 50
        -isActive: boolean
        -lastDetectedColor: String
        -redValue: int
        -greenValue: int
        -blueValue: int
        -threshold: int
        -isCalibrated: boolean
        -sensorName: String
        +ColorSensor()
        +ColorSensor(String name)
        +detectColor(): String
        -takeRGBReading(): void
        -analyzeRGBValues(): String
        +isColorDetected(String targetColor): boolean
        +calibrate(): void
        +setActive(boolean active): void
        +setThreshold(int newThreshold): void
        +getRGBValues(): int[]
        +isActive(): boolean
        +getLastDetectedColor(): String
        +getRedValue(): int
        +getGreenValue(): int
        +getBlueValue(): int
        +getThreshold(): int
        +isCalibrated(): boolean
        +getSensorName(): String
        +getStatus(): String
        +displayDiagnostics(): void
        +performSelfTest(): boolean
    }

    Robot <-- "4" Wheel : contains
    Robot <-- "1" Grabber : contains
    Robot <-- "1" DistanceSensor : contains
    Robot <-- "1" ColorSensor : contains
```

## Key Relationships Explained

### Composition Relationships (<--)
- **Robot contains 4 Wheels**: Each robot has exactly four wheel objects (frontLeft, frontRight, rearLeft, rearRight)
- **Robot contains 1 Grabber**: Each robot has one grabber mechanism for picking up objects
- **Robot contains 1 DistanceSensor**: Each robot has one distance sensor for obstacle detection
- **Robot contains 1 ColorSensor**: Each robot has one color sensor for object identification

### Class Structure Notes

**UML Visibility Notation:**
- **+ (plus sign) = Public**: Accessible from other classes
- **- (minus sign) = Private**: Only accessible within the same class

**Constants (+ prefix - public static final):**
- All classes use `public static final` constants for configuration values
- Examples: `+MAX_POWER: int = 100`, `+SAFE_DISTANCE_CM: double = 10.0`
- Constants improve code maintainability and eliminate "magic numbers"

**Instance Variables (- prefix - private):**
- All instance variables are `private` for proper encapsulation
- Examples: `-wheelName: String`, `-isPoweredOn: boolean`
- Access is controlled through public getter and setter methods

**Public Methods (+ prefix):**
- Public methods provide controlled access to class functionality
- Examples: `+setPower(int power): void`, `+detectColor(): String`
- Form the public interface that other classes can use

**Private Methods (- prefix):**
- Private helper methods are internal implementation details
- Examples: `-takeSingleReading(): double`, `-analyzeRGBValues(): String`
- Cannot be called directly from other classes

**Constructor Overloading:**
- Both sensor classes show constructor overloading with default and named versions

This diagram illustrates the **composition** design pattern where the Robot class is built from multiple component objects, demonstrating how complex systems can be constructed from simpler, reusable parts.