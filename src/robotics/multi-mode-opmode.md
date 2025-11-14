---
title: Multi-Mode OpMode Example
permalink: /robotics/multi-mode-opmode/index.html
description: 'Comprehensive FTC OpMode with keyboard, gamepad, and autonomous control'
---

## Multi-Mode Robot Controller

This OpMode demonstrates a flexible robot control system that supports three different operating modes, making it ideal for testing and learning different aspects of FTC robot programming.

### Operating Modes

1. **Keyboard Drive (Mode 0)** - Control via computer keyboard (useful for testing without a gamepad)
2. **Gamepad Drive (Mode 1)** - Standard TeleOp driver control
3. **Autonomous (Mode 2)** - Pre-programmed automatic sequences

### Hardware Components

This example uses:
- **Drivetrain**: Two DC motors for differential (tank) drive
- **Shooter Mechanism**: Flywheel motor with servo-controlled artifact stopper
- **Sensors**: Color sensor, distance sensor, and IMU (gyroscope)
- **Vision**: Webcam with AprilTag detection for autonomous navigation

### Key Concepts Demonstrated

#### AprilTag Vision Processing
The code shows how to set up and use AprilTags (fiducial markers similar to QR codes) for robot positioning during autonomous. The vision system can detect tags and provide:
- Tag ID (identifies which target)
- Range (distance to tag)
- Yaw (horizontal angle to tag)

#### Differential Drive Control
The `processDriveInputs()` method implements differential steering:
- **Left Motor** = vertical + horizontal
- **Right Motor** = vertical - horizontal

This allows the robot to drive forward, turn, or combine both into smooth arcs.

#### Autonomous Sequences
The autonomous mode demonstrates:
- Time-based navigation
- Sequential actions (drive, shoot, repeat)
- Artifact shooting with timed sequences
- Switching between modes during a match

### Code Structure

The code is organized into clear sections:
- Hardware declarations
- Vision initialization
- Mode selection logic
- Drive control methods (keyboard, gamepad, autonomous)
- Shooting mechanisms
- Helper functions

### Educational Value

This example is excellent for learning:
- **LinearOpMode lifecycle**: INIT → START → ACTIVE → STOP
- **Hardware mapping**: How to connect code to physical devices
- **Vision processing**: Setting up cameras and processors
- **State machines**: Managing different robot behaviors
- **Telemetry**: Debugging with real-time data display

### Usage Notes

**To change modes**, modify the `mode` variable in `runOpMode()`:
```java
mode = 0;  // Keyboard drive
mode = 1;  // Gamepad drive
mode = 2;  // Autonomous
```

**Hardware Configuration Requirements:**
- Motors: `driveLeft`, `driveRight`, `shootwheel`
- Servo: `artifactstopper`
- Sensors: `color1`, `distance1`, `imu`
- Camera: `webcam`

### Full Code

```java
/***********************************************************************
*                                                                      *
* OnbotJava Editor is still beta! Please inform us of any bugs        *
* on our discord channel! https://discord.gg/e7nVjMM                  *
* Only BLOCKS code is submitted when in Arena                         *
*                                                                      *
***********************************************************************/

/**
 * FTC OpMode Example - Multi-Mode Robot Controller
 *
 * This OpMode demonstrates a flexible robot control system with three operating modes:
 * 1. Keyboard Drive (mode 0) - Control via keyboard input
 * 2. Gamepad Drive (mode 1) - Control via FTC gamepad
 * 3. Autonomous (mode 2) - Pre-programmed automatic sequences
 *
 * Hardware Components:
 * - Drivetrain: Two DC motors (left/right) for differential drive
 * - Shooter: Flywheel motor and servo-controlled artifact stopper
 * - Sensors: Color sensor, distance sensor, IMU (Inertial Measurement Unit)
 * - Vision: Webcam with AprilTag detection for autonomous navigation
 *
 * Key FTC Concepts:
 * - LinearOpMode: Sequential programming model where code runs top-to-bottom
 * - hardwareMap: FTC's hardware configuration system that maps devices by name
 * - opModeIsActive(): Checks if the OpMode is still running (not stopped by user)
 * - telemetry: Sends data to the Driver Station display for debugging
 */
public class MyFIRSTJavaOpMode extends LinearOpMode {
    // ===== HARDWARE DECLARATIONS =====
    // These are references to the physical hardware devices configured in the Robot Controller

    // Drive Motors - Control robot movement
    DcMotor driveLeft;    // Left side drive motor
    DcMotor driveRight;   // Right side drive motor

    // Shooter Mechanism - Launches game artifacts
    DcMotor shootwheel;           // Flywheel motor that propels artifacts
    Servo artifactstopper;        // Servo that gates artifact flow (0.0 = open, 0.2 = closed)

    // Sensors - Environmental awareness
    ColorSensor color1;           // Detects color of objects or field elements
    DistanceSensor distance1;     // Measures distance to objects
    BNO055IMU imu;               // Gyroscope for orientation and rotation sensing

    // ===== CONTROL VARIABLES =====
    // Vision Processing - AprilTag detection for autonomous navigation
var myVisionPortalBuilder, nArtifacts, myAprilTagDetections, myVisionPortal, horizontalInput, myAprilTagDetection, shootPower, isShooting, verticalInput, myApriltagProcessor, maxDrivePower, myAprilTagProcessorBuilder, mode;
    // myVisionPortal: Camera interface for processing video
    // myApriltagProcessor: Detects AprilTags (fiducial markers used for positioning)

    // Drive Control Variables
    // horizontalInput: Left/right turning input (-1.0 to 1.0)
    // verticalInput: Forward/backward driving input (-1.0 to 1.0)
    // maxDrivePower: Maximum power limit for drive motors (0.0 to 1.0)

    // Shooter Control Variables
    // shootPower: Power level for the flywheel motor
    // isShooting: Flag to prevent multiple simultaneous shooting sequences
    // nArtifacts: Counter for autonomous shooting sequences

    // Mode Selection
    // mode: 0 = keyboard, 1 = gamepad, 2 = autonomous

    /**
     * Initialize Vision Portal for AprilTag Detection
     *
     * Sets up the camera and vision processing pipeline for AprilTag detection.
     * AprilTags are fiducial markers (like QR codes) that help the robot determine
     * its position and orientation on the field during autonomous.
     */
    public void initializeVisionPortal(){
      myVisionPortalBuilder = new VisionPortal.Builder();
      myVisionPortal = (myVisionPortalBuilder.build());
      myVisionPortalBuilder.setCamera(hardwareMap.get(WebcamName.class, "webcam"));
      myAprilTagProcessorBuilder = new AprilTagProcessor.Builder();
      myApriltagProcessor = (myAprilTagProcessorBuilder.build());
      myVisionPortalBuilder.addProcessor(myApriltagProcessor);
    }

    /**
     * Initialize Hardware Settings
     *
     * Configures initial states for motors and servos before the match starts.
     */
    public void inititalSetup(){
      // Put initialization blocks here
      driveLeft.setDirection(DcMotor.Direction.REVERSE);
      isShooting = false;
      // Holds back artifacts until we start shooting
      artifactstopper.setPosition(0.2);
    }

    /**
     * Mode Selector - Routes to Appropriate Control Method
     */
    public void pickMode(){
      // Switch based on the mode variable
      if (mode == 0) {
        keyboardDrive();
      } else if (mode == 1) {
        gamepadDrive();
      } else if (mode == 2) {
        autoDrive();
      }
    }

    /**
     * Keyboard Drive Mode
     *
     * Keyboard Mapping (ASCII codes):
     * - 105 (i): Forward
     * - 107 (k): Backward
     * - 106 (j): Turn Left
     * - 108 (l): Turn Right
     * - 112 (p): Shoot
     */
    public void keyboardDrive(){
      while (opModeIsActive()) {
        // Convert keyboard input to a final direction value
        horizontalInput = keyboard.isPressed(108) - keyboard.isPressed(106);
        verticalInput = keyboard.isPressed(105) - keyboard.isPressed(107);
        processDriveInputs();
        if (keyboard.isPressed(112) && !isShooting) {
          shoot();
        }
        displayVisionPortalData();
      }
    }

    /**
     * Gamepad Drive Mode (TeleOp)
     *
     * Gamepad Controls:
     * - Left Stick Y: Forward/Backward movement
     * - Right Stick X: Left/Right turning
     * - A Button: Shoot artifact
     */
    public void gamepadDrive(){
      while (opModeIsActive()) {
        horizontalInput = gamepad1.right_stick_x;
        verticalInput = gamepad1.left_stick_y;
        processDriveInputs();
        if (gamepad1.a && !isShooting) {
          shoot();
        }
        displayVisionPortalData();
      }
    }

    /**
     * Autonomous Drive Sequence
     *
     * 1. Drive to goal position
     * 2. Shoot 3 pre-loaded artifacts
     * 3. Drive to loading zone and return
     * 4. Shoot 3 more artifacts (if loaded)
     * 5. Switch to keyboard control for remaining time
     */
    public void autoDrive(){
      driveToGoal();
      shootThreeArtifacts();
      driveToLoadingSpotAndBack();
      shootThreeArtifacts();
      keyboardDrive();
    }

    // Additional methods: driveToGoal, shootThreeArtifacts, processDriveInputs,
    // shoot, displayVisionPortalData, etc.
    // (See full source file for complete implementation)

    @Override
    public void runOpMode() {
      // ===== HARDWARE INITIALIZATION =====
      driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
      driveRight = hardwareMap.get(DcMotor.class, "driveRight");
      shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
      artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
      color1 = hardwareMap.get(ColorSensor.class, "color1");
      distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
      imu = hardwareMap.get(BNO055IMU.class, "imu");

      // ===== CONFIGURATION =====
      inititalSetup();
      initializeVisionPortal();
      shootPower = 0.8;
      maxDrivePower = 1;

      // Select operating mode: 0 = keyboard, 1 = gamepad, 2 = autonomous
      mode = 2;

      waitForStart();

      // ===== ACTIVE PERIOD =====
      pickMode();
    }
}
```

### Learning Resources

- [FTC Programming Resources](https://ftc-docs.firstinspires.org/en/latest/programming_resources/index.html)
- [OnBot Java Tutorial](https://ftc-docs.firstinspires.org/en/latest/programming_resources/onbot_java/OnBot-Java-Tutorial.html)
- [AprilTag Detection Guide](https://ftc-docs.firstinspires.org/en/latest/apriltag/vision_portal/apriltag_intro/apriltag-intro.html)

### Download

[Download complete source code](/robotics/onbot.java)

---

*This code example is for educational purposes and demonstrates key FTC programming concepts for Royal Robotics team members.*
