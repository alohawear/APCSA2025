---
title: "FTC Robotics: Distance and Color Sensor Navigation"
description: "A FIRST Tech Challenge robot program demonstrating sensor-based navigation using distance and color sensors with complex conditional logic."
date: 2025-09-30
tags: ['week40', 'robotics', 'sensors', 'real-world-application', 'ftc']
---

# FTC Robotics: Distance and Color Sensor Navigation

This code is from FIRST Tech Challenge (FTC) robotics competition, showing how Java is used to control real robots. While it uses the FTC framework (not part of AP CS A), it demonstrates important programming concepts we're learning in class.

## Concepts Demonstrated

- **While loops** - Used extensively for sensor monitoring
- **Conditional logic** - Multiple if/else statements based on sensor readings
- **Instance variables** - Motors and sensors as object references
- **Method calls** - Calling methods like `setPower()`, `getDistance()`, `sleep()`
- **Real-world application** - Java controlling physical hardware

## The Code

```java
/***********************************************************************
*                                                                      *
* OnbotJava Editor is still : beta! Please inform us of any bugs       |
* on our discord channel! https://discord.gg/e7nVjMM                   *
* Only BLOCKS code is submitted when in Arena                          *
*                                                                      *
***********************************************************************/


public class MyFIRSTJavaOpMode extends LinearOpMode {
    DcMotor motorLeft;
    DcMotor motorRight;
    DcMotor frontLeft;
    DcMotor frontRight;
    ColorSensor color1;
    DistanceSensor distance1;
    BNO055IMU imu;

@Override
    public void runOpMode() {
      motorLeft = hardwareMap.get(DcMotor.class, "motorLeft");
      motorRight = hardwareMap.get(DcMotor.class, "motorRight");
      frontLeft = hardwareMap.get(DcMotor.class, "frontLeft");
      frontRight = hardwareMap.get(DcMotor.class, "frontRight");
      color1 = hardwareMap.get(ColorSensor.class, "color1");
      distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
      imu = hardwareMap.get(BNO055IMU.class, "imu");
      // Put initialization blocks here
      waitForStart();
      // Put run blocks here
      motorLeft.setPower(-1);
      motorRight.setPower(1);
      while (opModeIsActive()) {
        // Put loop blocks here
        telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
        telemetry.update();
        if (distance1.getDistance(DistanceUnit.CM) <= 60) {
          break;
        }
      }
      motorLeft.setPower(-0.001);
      motorRight.setPower(1);
      sleep(1100);
      motorLeft.setPower(-1);
      motorRight.setPower(1);
      while (opModeIsActive()) {
        // Put loop blocks here
        telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
        telemetry.update();
        if (distance1.getDistance(DistanceUnit.CM) <= 60) {
          break;
        }
      }
      motorLeft.setPower(-0.001);
      motorRight.setPower(1);
      sleep(1100);
      motorLeft.setPower(-1);
      motorRight.setPower(1);
      while (opModeIsActive()) {
        // Put loop blocks here
        telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
        telemetry.update();
        if (distance1.getDistance(DistanceUnit.CM) <= 90) {
          break;
        }
      }
      motorLeft.setPower(-0.0001);
      motorRight.setPower(1);
      sleep(850);
      motorLeft.setPower(0);
      motorRight.setPower(0);
      while (opModeIsActive()) {
        telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
        telemetry.update();
        if (distance1.getDistance(DistanceUnit.CM) <= 40) {
          while (opModeIsActive()) {
            telemetry.addData("ColorSensor", (color1.blue()));
            telemetry.update();
            if (color1.blue() <= 70) {
              break;
            } else {
              motorLeft.setPower(1);
              motorRight.setPower(-1);
              sleep(1000);
              motorLeft.setPower(0);
              motorRight.setPower(0);
              break;
            }
          }
          break;
        } else if (distance1.getDistance(DistanceUnit.CM) >= 40) {
          motorLeft.setPower(-1);
          motorRight.setPower(1);
        }
      }
      while (opModeIsActive()) {
        telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
        telemetry.update();
        if (distance1.getDistance(DistanceUnit.CM) > 90) {
          while (opModeIsActive()) {
            telemetry.addData("ColorSensor", (color1.blue()));
            if (color1.blue() <= 70) {
              break;
            }
            while (opModeIsActive()) {
              telemetry.addData("ColorSensor", (color1.blue()));
              telemetry.update();
              if (color1.blue() <= 70) {
                break;
              }
              while (opModeIsActive()) {
                telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
                telemetry.update();
                if (distance1.getDistance(DistanceUnit.CM) > 60) {
                  if (distance1.getDistance(DistanceUnit.CM) > 60) {
                    motorLeft.setPower(0);
                    motorRight.setPower(1);
                    sleep(1000);
                    motorLeft.setPower(0);
                    motorRight.setPower(0);
                  }
                }
                break;
              }
              while (opModeIsActive()) {
                telemetry.addData("distance", (distance1.getDistance(DistanceUnit.CM)));
                telemetry.update();
                if (distance1.getDistance(DistanceUnit.CM) < 80) {
                  motorLeft.setPower(1);
                  motorRight.setPower(0);
                  sleep(1000);
                  motorLeft.setPower(0);
                  motorRight.setPower(0);
                  sleep(1000);
                  motorLeft.setPower(-0.1);
                  motorRight.setPower(1);
                  sleep(2000);
                  motorLeft.setPower(-1);
                  motorRight.setPower(1);
                  break;
                } else {
                  motorLeft.setPower(-1);
                  motorRight.setPower(0);
                  sleep(1000);
                  motorLeft.setPower(-0.5);
                  motorRight.setPower(0.5);
                  sleep(450);
                  motorLeft.setPower(-1);
                  motorRight.setPower(0);
                  sleep(1000);
                  motorLeft.setPower(0);
                  motorRight.setPower(1);
                  sleep(1200);
                  motorLeft.setPower(0);
                  motorRight.setPower(0);
                  break;
                }
              }
              break;
            }
            break;
          }
          break;
        }
      }
    }

}
```

## How This Relates to AP CS A

While the FTC framework itself isn't on the AP exam, this code demonstrates:

1. **While loops with conditions** - Similar to what we write in AP CS A
2. **Using objects and calling methods** - Core OOP concepts
3. **Nested control structures** - Multiple levels of if/while statements
4. **Real-world problem solving** - Applying programming to solve actual problems

## Notes for Improvement

This code could be refactored using:
- **Methods** to avoid repetition (DRY principle)
- **Constants** for magic numbers (60, 70, 1100, etc.)
- **Better variable names** for clarity
- **Comments** explaining the robot's strategy

This is a great example of "working code" that could benefit from applying software engineering principles!
