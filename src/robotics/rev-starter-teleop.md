---
title: REV Starter Bot TeleOp
permalink: /robotics/rev-starter-teleop/index.html
description: 'Driver-controlled OpMode for REV Starter Bot with velocity-controlled shooter'
---

## REV Starter Bot TeleOp Mode

This OpMode provides driver control for the REV Starter Bot during the TeleOp (driver-controlled) period of an FTC match. It features sophisticated velocity control for consistent shooting and intuitive split-stick arcade driving.

### Robot Hardware

The REV Starter Bot includes:
- **2x Drive Motors** - Differential (tank) drivetrain
- **1x Flywheel Motor** - Ball launcher with velocity control
- **1x Core Hex Motor** - Ball feeder mechanism
- **1x Continuous Rotation Servo** - Hopper agitator

### Key Features

#### Velocity-Controlled Flywheel
Unlike simple power control, this code uses **DcMotorEx** with velocity control to maintain consistent shooting speed:
- **Bank Shot**: 1300 ticks/second (close-range)
- **Far Shot**: 1900 ticks/second (medium-range)
- **Max Power**: 2200 ticks/second (maximum distance)

**Why velocity control?**
- Compensates for battery drain (low battery doesn't affect shot distance)
- Maintains speed even as balls create friction
- More accurate and consistent scoring

#### Smart Feeding System
The automatic shooting sequences wait for the flywheel to reach target velocity before feeding balls:
```java
if (flywheel.getVelocity() >= targetVelocity - 50) {
    coreHex.setPower(1);  // Feed when ready
} else {
    coreHex.setPower(0);  // Wait for spin-up
}
```

This prevents:
- Wasted shots (balls fed into slow flywheel)
- Jamming from balls hitting slow flywheel
- Inconsistent shot distances

#### Split-Stick Arcade Drive
Intuitive driving control:
- **Left Stick Y-axis**: Forward/backward movement
- **Right Stick X-axis**: Left/right turning
- Allows simultaneous driving and turning

### Control Scheme

#### Driving
- **Left Stick**: Push up/down for forward/backward
- **Right Stick**: Push left/right for turning

#### Shooting (Automatic Sequences)
- **Left Bumper**: Far shot (spins flywheel, waits, then feeds)
- **Right Bumper**: Bank shot (close-range shot)

#### Shooting (Manual Control)
- **Circle (○)**: Spin flywheel only (bank velocity)
- **Square (□)**: Spin flywheel only (max velocity)
- **Options**: Reverse flywheel (clear jams)

#### Feeder/Agitator (Manual Override)
- **Cross (✕)**: Feed balls forward
- **Triangle (△)**: Reverse feeder
- **D-Pad Left**: Agitate counterclockwise
- **D-Pad Right**: Agitate clockwise

### Code Highlights

#### Differential Drive Math
```java
float x = gamepad1.right_stick_x;      // Turn input
float y = -gamepad1.left_stick_y;      // Forward input (negated)

leftDrive.setPower(y - x);             // Left = forward - turn
rightDrive.setPower(y + x);            // Right = forward + turn
```

**Examples:**
- Push left stick up (y=1, x=0): Both motors = 1 → Drive forward
- Push right stick right (y=0, x=1): Left=-1, Right=1 → Spin right
- Both sticks (y=0.5, x=0.5): Left=0, Right=1 → Arc right

#### DcMotorEx for Velocity Control
```java
// Cast to DcMotorEx to access velocity methods
((DcMotorEx) flywheel).setVelocity(bankVelocity);

// Read current velocity for diagnostics
double currentVelocity = ((DcMotorEx) flywheel).getVelocity();
```

**Encoder Ticks:**
- Motor encoders count rotations in "ticks"
- Each motor has specific ticks per revolution
- Higher ticks/second = faster spin = farther shot

### Learning Objectives

This code demonstrates:
1. **OpMode lifecycle management** (INIT → START → ACTIVE → STOP)
2. **Hardware abstraction** (mapping code to physical devices)
3. **Advanced motor control** (velocity vs power)
4. **Event-driven programming** (button presses trigger actions)
5. **Telemetry for debugging** (real-time data display)
6. **Control flow logic** (if/else chains for button priority)

### Best Practices Shown

- **Motor direction configuration** - Accounts for physically reversed motors
- **Encoder-based control** - Uses RUN_USING_ENCODER for velocity PID
- **Servo initialization** - Sets servo to known state before match
- **Telemetry updates** - Displays current flywheel velocity and power
- **Anti-stutter logic** - Prevents servo from stuttering under manual control

### Usage Tips

**Testing the Robot:**
1. Configure hardware in Robot Controller app
2. Select this OpMode from TeleOp category
3. Press INIT to initialize hardware
4. Press START to begin driver control
5. Monitor flywheel velocity in Driver Station telemetry

**Tuning Velocities:**
Adjust these constants for your robot:
```java
private static final int bankVelocity = 1300;
private static final int farVelocity = 1900;
private static final int maxVelocity = 2200;
```

Test different values to find optimal shooting speeds for your field setup.

### Full Code

```java
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
public class REVStarterBotTeleOpJava extends LinearOpMode {

  // ===== HARDWARE DECLARATIONS =====
  private DcMotor flywheel;
  private DcMotor coreHex;
  private DcMotor leftDrive;
  private CRServo servo;
  private DcMotor rightDrive;

  // ===== FLYWHEEL VELOCITY CONSTANTS =====
  private static final int bankVelocity = 1300;
  private static final int farVelocity = 1900;
  private static final int maxVelocity = 2200;

  @Override
  public void runOpMode() {
    // ===== HARDWARE INITIALIZATION =====
    flywheel = hardwareMap.get(DcMotor.class, "flywheel");
    coreHex = hardwareMap.get(DcMotor.class, "coreHex");
    leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
    servo = hardwareMap.get(CRServo.class, "servo");
    rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

    // ===== MOTOR CONFIGURATION =====
    flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    flywheel.setDirection(DcMotor.Direction.REVERSE);
    coreHex.setDirection(DcMotor.Direction.REVERSE);
    leftDrive.setDirection(DcMotor.Direction.REVERSE);
    servo.setPower(0);

    waitForStart();

    // ===== MAIN CONTROL LOOP =====
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        splitStickArcadeDrive();
        setFlywheelVelocity();
        manualCoreHexAndServoControl();

        telemetry.addData("Flywheel Velocity", ((DcMotorEx) flywheel).getVelocity());
        telemetry.addData("Flywheel Power", flywheel.getPower());
        telemetry.update();
      }
    }
  }

  private void splitStickArcadeDrive() {
    float x = gamepad1.right_stick_x;
    float y = -gamepad1.left_stick_y;
    leftDrive.setPower(y - x);
    rightDrive.setPower(y + x);
  }

  private void manualCoreHexAndServoControl() {
    if (gamepad1.cross) {
      coreHex.setPower(0.5);
    } else if (gamepad1.triangle) {
      coreHex.setPower(-0.5);
    }
    if (gamepad1.dpad_left) {
      servo.setPower(1);
    } else if (gamepad1.dpad_right) {
      servo.setPower(-1);
    }
  }

  private void setFlywheelVelocity() {
    if (gamepad1.options) {
      flywheel.setPower(-0.5);
    } else if (gamepad1.left_bumper) {
      farPowerAuto();
    } else if (gamepad1.right_bumper) {
      bankShotAuto();
    } else if (gamepad1.circle) {
      ((DcMotorEx) flywheel).setVelocity(bankVelocity);
    } else if (gamepad1.square) {
      ((DcMotorEx) flywheel).setVelocity(maxVelocity);
    } else {
      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);
      if (!gamepad1.dpad_right && !gamepad1.dpad_left) {
        servo.setPower(0);
      }
    }
  }

  private void bankShotAuto() {
    ((DcMotorEx) flywheel).setVelocity(bankVelocity);
    servo.setPower(-1);
    if (((DcMotorEx) flywheel).getVelocity() >= bankVelocity - 50) {
      coreHex.setPower(1);
    } else {
      coreHex.setPower(0);
    }
  }

  private void farPowerAuto() {
    ((DcMotorEx) flywheel).setVelocity(farVelocity);
    servo.setPower(-1);
    if (((DcMotorEx) flywheel).getVelocity() >= farVelocity - 100) {
      coreHex.setPower(1);
    } else {
      coreHex.setPower(0);
    }
  }
}
```

### Related Resources

- [FTC OnBot Java Tutorial](https://ftc-docs.firstinspires.org/en/latest/programming_resources/onbot_java/OnBot-Java-Tutorial.html)
- [DcMotor Documentation](https://ftc-docs.firstinspires.org/en/latest/programming_resources/shared/dc_motor_control/dc-motor-control.html)
- [Gamepad Controls](https://ftc-docs.firstinspires.org/en/latest/programming_resources/shared/gamepad/gamepad.html)

### Download

[Download complete source code](/robotics/teleop-onbot.java)

---

*This example demonstrates professional-level TeleOp programming for FTC competitions.*
