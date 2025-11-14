---
title: Combined TeleOp and Autonomous
permalink: /robotics/combined-teleop-auto/index.html
description: 'Single OpMode with mode selection for TeleOp and autonomous'
---

## Combined TeleOp and Autonomous OpMode

This advanced OpMode allows drivers to select between TeleOp and autonomous modes during initialization, making it ideal for competitions where you want a single OpMode for the entire match.

### Why Combine TeleOp and Autonomous?

**Benefits:**
- **Single OpMode** for entire match (easier to manage)
- **Code sharing** between modes (no duplication)
- **Quick testing** without changing OpMode selection
- **Alliance-specific** autonomous routines

**Competition Usage:**
- Select AUTO BLUE or AUTO RED for autonomous period
- Robot automatically runs autonomous, then transitions to TeleOp
- Alternatively, select TELEOP for practice or pure driver control

### Mode Selection

During the INIT phase (before pressing START), drivers can cycle through modes:

1. **TELEOP** - Manual driver control only
2. **AUTO BLUE** - Blue alliance autonomous → TeleOp
3. **AUTO RED** - Red alliance autonomous → TeleOp

**To cycle modes**: Press the **PS/Home button** on gamepad

The Driver Station displays the current selection and reminds you to enable the 30-second autonomous timer for auto modes.

### Autonomous Strategy

Both autonomous modes follow this sequence:

#### Phase 1: Shooting (10 seconds max)
- Spins flywheel to bank shot velocity
- Activates agitator servo
- Feeds pre-loaded balls when flywheel is up to speed
- Displays countdown timer on Driver Station

#### Phase 2: Navigation
1. **Back up** from goal (12 inches)
2. **Turn** to align with parking zone
   - Blue alliance: Turn LEFT
   - Red alliance: Turn RIGHT
3. **Drive backward** off launch line (50 inches)

**Why different turns?**
The FTC field is symmetrical, with Blue and Red alliances mirrored. Turning directions must be reversed to navigate to the correct parking zones.

### Encoder-Based Autonomous Driving

Unlike time-based driving (unreliable), this code uses **motor encoders** for accurate navigation.

#### How Encoders Work

Motor encoders count rotations in "ticks":
- REV motors: 28 ticks per motor revolution
- Combined with gear ratio and wheel size to calculate distance
- `WHEELS_INCHES_TO_TICKS` converts inches to encoder ticks

#### autoDrive() Method

```java
autoDrive(speed, leftInches, rightInches, timeout);
```

**Parameters:**
- `speed`: Motor power (0.0 to 1.0)
- `leftInches`: Distance for left wheel (negative = backward)
- `rightInches`: Distance for right wheel (negative = backward)
- `timeout`: Maximum time in milliseconds (safety)

**Examples:**
```java
// Drive straight forward 24 inches
autoDrive(0.8, 24, 24, 5000);

// Drive straight backward 12 inches
autoDrive(0.5, -12, -12, 5000);

// Turn right in place (left forward, right backward)
autoDrive(0.5, 10, -10, 5000);

// Arc to the left (right travels farther)
autoDrive(0.6, 20, 30, 5000);
```

#### RUN_TO_POSITION Mode

The encoder-based driving uses a special motor mode:

1. Calculate target position (current + distance in ticks)
2. Set motors to RUN_TO_POSITION mode
3. Set motor power
4. Motors automatically drive to target using PID control
5. Wait until motors reach target or timeout
6. Stop and return to normal mode

**Benefits over time-based:**
- **Accurate** regardless of battery voltage
- **Consistent** despite floor friction
- **Reliable** as motors wear over time
- **Predictable** path following

### Code Structure

#### Mode Selection Loop (INIT Phase)
```java
while (opModeInInit()) {
    operationSelected = selectOperation(operationSelected, gamepad1.psWasPressed());
    telemetry.update();
}
```

Runs continuously during INIT, allowing driver to cycle modes.

#### Execution Dispatcher (ACTIVE Phase)
```java
if (operationSelected.equals(AUTO_BLUE)) {
    doAutoBlue();
} else if (operationSelected.equals(AUTO_RED)) {
    doAutoRed();
} else {
    doTeleOp();
}
```

Routes to the selected mode after START is pressed.

### Key Programming Concepts

#### State Machine Pattern
The mode selection implements a circular state machine:
```
TELEOP → AUTO_BLUE → AUTO_RED → TELEOP → ...
```

Each press of PS button advances to next state.

#### Button Edge Detection
```java
gamepad1.psWasPressed()  // True only on frame button was pressed
vs
gamepad1.ps              // True while button is held
```

Using `WasPressed()` prevents rapid cycling from holding button.

#### Timer-Based Limits
```java
ElapsedTime autoLaunchTimer = new ElapsedTime();
autoLaunchTimer.reset();
while (opModeIsActive() && autoLaunchTimer.milliseconds() < 10000) {
    // Shoot for max 10 seconds
}
```

Limits shooting phase to avoid wasting autonomous period.

#### Safety Timeouts
Every autonomous movement has a timeout:
```java
autoDrive(0.5, 24, 24, 5000);  // Max 5 seconds
```

Prevents infinite loops if motors stall or encoders fail.

### Learning Objectives

This code teaches:
1. **Pre-start initialization loops** - Running code before START
2. **Mode selection interfaces** - Creating user-friendly OpModes
3. **Encoder navigation** - Accurate autonomous driving
4. **Alliance-specific logic** - Handling field symmetry
5. **Code reuse** - Sharing methods between TeleOp and auto
6. **Timer management** - ElapsedTime for time-based logic
7. **Safe autonomous** - Timeouts and exit conditions

### Hardware Requirements

**Configuration names:**
- `flywheel` - Flywheel motor (DcMotor)
- `coreHex` - Ball feeder motor (DcMotor)
- `leftDrive` - Left drivetrain motor (DcMotor with encoder)
- `rightDrive` - Right drivetrain motor (DcMotor with encoder)
- `servo` - Hopper agitator (CRServo)

**Important:** Drive motors must have encoders connected and functioning for autonomous to work properly.

### Competition Checklist

Before using in competition:

- [ ] Test both autonomous routines (Blue and Red)
- [ ] Verify encoder cables are secure
- [ ] Confirm turning directions are correct for your alliance
- [ ] Tune shooting duration (10 seconds may be too long/short)
- [ ] Test battery voltage doesn't affect encoder driving
- [ ] Practice mode selection during INIT phase
- [ ] Remember to enable 30-second autonomous timer!

### Full Code

```java
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class REVStarterBotTeleOpAutoJava extends LinearOpMode {

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

  // ===== MODE SELECTION CONSTANTS =====
  private static final String TELEOP = "TELEOP";
  private static final String AUTO_BLUE = "AUTO BLUE";
  private static final String AUTO_RED = " AUTO RED";
  private String operationSelected = TELEOP;

  // ===== AUTONOMOUS CONSTANTS =====
  private double WHEELS_INCHES_TO_TICKS = (28 * 5 * 3) / (3 * Math.PI);

  // ===== TIMERS =====
  private ElapsedTime autoLaunchTimer = new ElapsedTime();
  private ElapsedTime autoDriveTimer = new ElapsedTime();

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

    // ===== MODE SELECTION LOOP =====
    while (opModeInInit()) {
      operationSelected = selectOperation(operationSelected, gamepad1.psWasPressed());
      telemetry.update();
    }

    waitForStart();

    // ===== EXECUTE SELECTED MODE =====
    if (operationSelected.equals(AUTO_BLUE)) {
      doAutoBlue();
    } else if (operationSelected.equals(AUTO_RED)) {
      doAutoRed();
    } else {
      doTeleOp();
    }
  }

  /**
   * Mode Selection - Cycles through TELEOP → AUTO BLUE → AUTO RED
   */
  private String selectOperation(String state, boolean cycleNext) {
    if (cycleNext) {
      if (state.equals(TELEOP)) {
        state = AUTO_BLUE;
      } else if (state.equals(AUTO_BLUE)) {
        state = AUTO_RED;
      } else if (state.equals(AUTO_RED)) {
        state = TELEOP;
      }
    }

    telemetry.addLine("Press Home Button to cycle options");
    telemetry.addData("CURRENT SELECTION", state);
    if (state.equals(AUTO_BLUE) || state.equals(AUTO_RED)) {
      telemetry.addLine("Please remember to enable the AUTO timer!");
    }
    telemetry.addLine("Press START to start your program");
    return state;
  }

  /**
   * Encoder-Based Autonomous Driving
   */
  private void autoDrive(double speed, int leftDistanceInch, int rightDistanceInch, int timeout_ms) {
    autoDriveTimer.reset();

    leftDrive.setTargetPosition((int) (leftDrive.getCurrentPosition() + leftDistanceInch * WHEELS_INCHES_TO_TICKS));
    rightDrive.setTargetPosition((int) (rightDrive.getCurrentPosition() + rightDistanceInch * WHEELS_INCHES_TO_TICKS));

    leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    leftDrive.setPower(Math.abs(speed));
    rightDrive.setPower(Math.abs(speed));

    while (opModeIsActive() && (leftDrive.isBusy() || rightDrive.isBusy()) && autoDriveTimer.milliseconds() < timeout_ms) {
      idle();
    }

    leftDrive.setPower(0);
    rightDrive.setPower(0);
    leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
  }

  /**
   * Blue Alliance Autonomous
   */
  private void doAutoBlue() {
    if (opModeIsActive()) {
      telemetry.addData("RUNNING OPMODE", operationSelected);
      telemetry.update();

      // Shoot for 10 seconds
      autoLaunchTimer.reset();
      while (opModeIsActive() && autoLaunchTimer.milliseconds() < 10000) {
        BANK_SHOT_AUTO();
        telemetry.addData("Launcher Countdown", autoLaunchTimer.seconds());
        telemetry.update();
      }

      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);
      servo.setPower(0);

      // Navigate to parking zone
      autoDrive(0.5, -12, -12, 5000);  // Back up
      autoDrive(0.5, -8, 8, 5000);     // Turn left (Blue)
      autoDrive(1, -50, -50, 5000);    // Drive off line
    }
  }

  /**
   * Red Alliance Autonomous (turn direction reversed)
   */
  private void doAutoRed() {
    if (opModeIsActive()) {
      telemetry.addData("RUNNING OPMODE", operationSelected);
      telemetry.update();

      autoLaunchTimer.reset();
      while (opModeIsActive() && autoLaunchTimer.milliseconds() < 10000) {
        BANK_SHOT_AUTO();
        telemetry.addData("Launcher Countdown", autoLaunchTimer.seconds());
        telemetry.update();
      }

      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);
      servo.setPower(0);

      autoDrive(0.5, -12, -12, 5000);  // Back up
      autoDrive(0.5, 8, -8, 5000);     // Turn right (Red)
      autoDrive(1, -50, -50, 5000);    // Drive off line
    }
  }

  // TeleOp and shooting methods same as teleop-onbot.java
  // (See full source for complete implementation)
}
```

### Advanced Concepts

#### Encoder Math Formula
```java
ticks = inches * (28 ticks/motor_rev) * (gear_ratio) / (wheel_circumference)
```

For 3" wheels with 5:3 gear ratio:
```java
WHEELS_INCHES_TO_TICKS = (28 * 5 * 3) / (3 * Math.PI)
                       ≈ 44.56 ticks/inch
```

#### Differential Steering Examples
```java
// Drive straight
autoDrive(0.8, 24, 24, 5000);

// Spin right
autoDrive(0.5, 10, -10, 5000);

// Gentle arc left
autoDrive(0.6, 20, 30, 5000);
```

### Related Resources

- [FTC Autonomous Programming Guide](https://gm0.org/en/latest/docs/software/tutorials/dead-reckoning.html)
- [Encoder Navigation Tutorial](https://ftc-docs.firstinspires.org/en/latest/programming_resources/shared/encoder_navigation/encoder-navigation.html)
- [ElapsedTime Documentation](https://ftc-docs.firstinspires.org/en/latest/programming_resources/shared/timer/timer.html)

### Download

[Download complete source code](/robotics/teleop-and-auto-onbot.java)

---

*This advanced example demonstrates competition-ready OpMode design with mode selection and encoder-based autonomous.*
