package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * REV Starter Bot Combined TeleOp and Autonomous OpMode
 *
 * This OpMode allows drivers to select between three modes during initialization:
 * 1. TeleOp - Manual driver control
 * 2. Auto Blue - Blue alliance autonomous routine
 * 3. Auto Red - Red alliance autonomous routine
 *
 * Why Combine TeleOp and Auto?
 * - Single OpMode for entire match (easier to manage)
 * - Share code between modes (no duplication)
 * - Allows quick mode testing without changing OpMode selection
 *
 * Key Features:
 * - Pre-start mode selection using PS/Home button
 * - Encoder-based autonomous driving for accuracy
 * - Timer-based shooting sequences (10-second autonomous limit)
 * - Alliance-specific turning (Blue vs Red mirrored paths)
 *
 * Hardware:
 * - Differential drivetrain with encoders
 * - Velocity-controlled flywheel launcher
 * - Ball feeder and hopper agitator
 *
 * @TeleOp annotation: This appears in TeleOp category, but runs auto if selected
 */
@TeleOp
public class REVStarterBotTeleOpAutoJava extends LinearOpMode {

  // ===== HARDWARE DECLARATIONS =====
  private DcMotor flywheel;
  private DcMotor coreHex;
  private DcMotor leftDrive;
  private CRServo servo;
  private DcMotor rightDrive;

  // ===== FLYWHEEL VELOCITY CONSTANTS =====
  private static final int bankVelocity = 1300;  // Close-range shot velocity
  private static final int farVelocity = 1900;   // Medium-range shot velocity
  private static final int maxVelocity = 2200;   // Maximum shot velocity

  // ===== MODE SELECTION CONSTANTS =====
  private static final String TELEOP = "TELEOP";
  private static final String AUTO_BLUE = "AUTO BLUE";
  private static final String AUTO_RED = " AUTO RED";
  private String operationSelected = TELEOP;  // Default to TeleOp

  // ===== AUTONOMOUS DRIVING CONSTANTS =====
  /**
   * Encoder Conversion Factor
   *
   * Converts inches of wheel travel to encoder ticks.
   *
   * Formula Breakdown:
   * - 28 ticks per motor revolution (REV motor encoder)
   * - 5:3 gear ratio (5 motor rotations = 3 wheel rotations)
   * - 3 inch wheel diameter
   * - π * diameter = circumference
   *
   * Math: (28 ticks/motor_rev) * (5 motor_rev / 3 wheel_rev) / (3π inches/wheel_rev)
   *     = ticks per inch
   */
  private double WHEELS_INCHES_TO_TICKS = (28 * 5 * 3) / (3 * Math.PI);

  // ===== TIMERS =====
  private ElapsedTime autoLaunchTimer = new ElapsedTime();  // Limits autonomous shooting to 10 seconds
  private ElapsedTime autoDriveTimer = new ElapsedTime();   // Timeout for autonomous driving movements

  /**
   * Main OpMode Entry Point with Mode Selection
   *
   * Lifecycle:
   * 1. INIT Phase: Hardware setup and mode selection loop
   * 2. START: User presses play after selecting mode
   * 3. ACTIVE: Execute selected mode (Auto or TeleOp)
   *
   * Mode Selection Interface:
   * - Runs selection loop during INIT (before waitForStart)
   * - Driver cycles modes by pressing PS/Home button
   * - Telemetry displays current selection on Driver Station
   * - Selection is locked in when START is pressed
   */
  @Override
  public void runOpMode() {
    // ===== HARDWARE INITIALIZATION =====
    flywheel = hardwareMap.get(DcMotor.class, "flywheel");
    coreHex = hardwareMap.get(DcMotor.class, "coreHex");
    leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
    servo = hardwareMap.get(CRServo.class, "servo");
    rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

    // ===== MOTOR CONFIGURATION =====
    // Establishing the direction and mode for the motors

    // Enable velocity control on flywheel
    flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    flywheel.setDirection(DcMotor.Direction.REVERSE);

    // Configure motor directions to match physical mounting
    coreHex.setDirection(DcMotor.Direction.REVERSE);
    leftDrive.setDirection(DcMotor.Direction.REVERSE);

    // Ensures the servo is active and ready
    servo.setPower(0);

    // ===== MODE SELECTION LOOP =====
    // On initialization the Driver Station will prompt for which OpMode should be run
    // - Auto Blue, Auto Red, or TeleOp
    // opModeInInit() returns true during INIT phase (before START is pressed)
    while (opModeInInit()) {
      // Update selection if PS button was pressed this iteration
      operationSelected = selectOperation(operationSelected, gamepad1.psWasPressed());
      // Display current selection on Driver Station
      telemetry.update();
    }

    // ===== WAIT FOR START =====
    waitForStart();

    // ===== EXECUTE SELECTED MODE =====
    // Route to appropriate method based on user selection
    if (operationSelected.equals(AUTO_BLUE)) {
      doAutoBlue();
    } else if (operationSelected.equals(AUTO_RED)) {
      doAutoRed();
    } else {
      doTeleOp();
    }
  }

  /**
   * Mode Selection Cycler
   *
   * Implements a circular menu that cycles through operation modes when PS button is pressed.
   *
   * Cycle Order:
   * TELEOP → AUTO BLUE → AUTO RED → TELEOP → ...
   *
   * Parameters:
   * @param state - Current mode selection
   * @param cycleNext - True if PS button was just pressed (use psWasPressed(), not psIsPressed())
   * @return Updated mode selection
   *
   * Why psWasPressed()?
   * - Returns true only on the frame the button was pressed
   * - Prevents rapid cycling from holding button down
   * - Creates single-click behavior
   *
   * Telemetry Display:
   * - Shows instructions for cycling and starting
   * - Displays current selection prominently
   * - Reminds driver to enable AUTO timer for autonomous modes
   *
   * Auto Timer Note:
   * - FTC rules limit autonomous period to 30 seconds
   * - Timer must be enabled manually on Driver Station
   * - Prevents accidental autonomous runs during practice
   */
  private String selectOperation(String state, boolean cycleNext) {
    // Only cycle if PS button was pressed this frame
    if (cycleNext) {
      // Circular state machine
      if (state.equals(TELEOP)) {
        state = AUTO_BLUE;
      } else if (state.equals(AUTO_BLUE)) {
        state = AUTO_RED;
      } else if (state.equals(AUTO_RED)) {
        state = TELEOP;
      } else {
        // Error handling: should never reach here
        telemetry.addData("WARNING", "Unknown Operation State Reached - Restart Program");
      }
    }

    // Display selection interface on Driver Station
    telemetry.addLine("Press Home Button to cycle options");
    telemetry.addData("CURRENT SELECTION", state);

    // Remind driver to enable timer for autonomous
    if (state.equals(AUTO_BLUE) || state.equals(AUTO_RED)) {
      telemetry.addLine("Please remember to enable the AUTO timer!");
    }

    telemetry.addLine("Press START to start your program");
    return state;
  }
  
  // ===== TELEOP MODE =====

  /**
   * TeleOp Main Loop
   *
   * Runs when TeleOp mode is selected. Provides driver control of robot.
   * Identical to standalone TeleOp OpMode, just activated via mode selection.
   *
   * Control Loop:
   * - Processes drive inputs (split-stick arcade)
   * - Manages flywheel velocity and shooting
   * - Handles manual feeder/agitator controls
   * - Displays real-time flywheel diagnostics
   *
   * Runs continuously until STOP is pressed or time expires.
   */
  private void doTeleOp() {
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        // Calling our methods while the OpMode is running
        splitStickArcadeDrive();              // Drive control
        setFlywheelVelocity();                // Shooting control
        manualCoreHexAndServoControl();       // Feeder control

        // Real-time flywheel diagnostics
        telemetry.addData("Flywheel Velocity", ((DcMotorEx) flywheel).getVelocity());
        telemetry.addData("Flywheel Power", flywheel.getPower());
        telemetry.update();
      }
    }
  }
  
  /**
   * Split-Stick Arcade Drive
   *
   * See detailed explanation in teleop-onbot.java
   * Left stick Y: forward/backward, Right stick X: turning
   */
  private void splitStickArcadeDrive() {
    float X;  // Turning input
    float Y;  // Forward/backward input

    X = gamepad1.right_stick_x;
    Y = -gamepad1.left_stick_y;  // Negate for natural forward
    leftDrive.setPower(Y - X);   // Differential drive math
    rightDrive.setPower(Y + X);
  }
  
  /**
   * Manual Feeder/Agitator Control
   * See detailed explanation in teleop-onbot.java
   */
  private void manualCoreHexAndServoControl() {
    // Core Hex intake: Cross = forward, Triangle = reverse
    if (gamepad1.cross) {
      coreHex.setPower(0.5);
    } else if (gamepad1.triangle) {
      coreHex.setPower(-0.5);
    }
    // Hopper servo: D-pad left/right to agitate
    if (gamepad1.dpad_left) {
      servo.setPower(1);
    } else if (gamepad1.dpad_right) {
      servo.setPower(-1);
    }
  }

  /**
   * Flywheel Velocity Controller
   * See detailed explanation in teleop-onbot.java
   */
  private void setFlywheelVelocity() {
    if (gamepad1.options) {
      flywheel.setPower(-0.5);  // Emergency reverse
    } else if (gamepad1.left_bumper) {
      FAR_POWER_AUTO();  // Automatic far shot
    } else if (gamepad1.right_bumper) {
      BANK_SHOT_AUTO();  // Automatic bank shot
    } else if (gamepad1.circle) {
      ((DcMotorEx) flywheel).setVelocity(bankVelocity);  // Manual bank velocity
    } else if (gamepad1.square) {
      ((DcMotorEx) flywheel).setVelocity(maxVelocity);   // Manual max velocity
    } else {
      // Stop everything when no input
      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);
      // Only stop servo if not under manual control
      if (!gamepad1.dpad_right && !gamepad1.dpad_left) {
        servo.setPower(0);
      }
    }
  }

  // ===== AUTOMATIC SHOOTING SEQUENCES =====
  // Shared between TeleOp and Autonomous modes

  /**
   * Bank Shot Automatic Sequence
   * Close-range shot with smart velocity-based feeding
   * See detailed explanation in teleop-onbot.java
   */
  private void BANK_SHOT_AUTO() {
    ((DcMotorEx) flywheel).setVelocity(bankVelocity);
    servo.setPower(-1);
    // Only feed when flywheel is up to speed
    if (((DcMotorEx) flywheel).getVelocity() >= bankVelocity - 100) {
      coreHex.setPower(1);
    } else {
      coreHex.setPower(0);
    }
  }

  /**
   * Far Power Shot Automatic Sequence
   * Medium/long-range shot with smart velocity-based feeding
   * See detailed explanation in teleop-onbot.java
   */
  private void FAR_POWER_AUTO() {
    ((DcMotorEx) flywheel).setVelocity(farVelocity);
    servo.setPower(-1);
    // Only feed when flywheel is up to speed
    if (((DcMotorEx) flywheel).getVelocity() >= farVelocity - 100) {
      coreHex.setPower(1);
    } else {
      coreHex.setPower(0);
    }
  }

  // ===== AUTONOMOUS MODE =====
  // Strategy: Launch pre-loaded balls, then drive off launch line to score points

  /**
   * Encoder-Based Autonomous Drive
   *
   * Drives robot a specified distance using motor encoders for accuracy.
   * Far superior to time-based driving (accounts for battery, friction, wear).
   *
   * How Encoder Navigation Works:
   * 1. Read current encoder positions
   * 2. Calculate target positions (current + distance_in_ticks)
   * 3. Switch motors to RUN_TO_POSITION mode
   * 4. Set motor power (motors automatically drive to target)
   * 5. Wait until motors reach target or timeout expires
   * 6. Stop motors and return to normal mode
   *
   * Parameters:
   * @param speed - Motor power (0.0 to 1.0), use Math.abs() for safety
   * @param leftDistanceInch - Distance for left wheel (negative = backward)
   * @param rightDistanceInch - Distance for right wheel (negative = backward)
   * @param timeout_ms - Maximum time to wait (safety fallback)
   *
   * Differential Turning:
   * - Straight: leftDistanceInch = rightDistanceInch
   * - Turn Right: leftDistanceInch > rightDistanceInch (left travels farther)
   * - Turn Left: rightDistanceInch > leftDistanceInch
   * - Spin in Place: leftDistanceInch = -rightDistanceInch
   *
   * RUN_TO_POSITION Mode:
   * - Motors automatically adjust power to reach target
   * - Built-in PID control maintains speed
   * - isBusy() returns true while motors are moving
   *
   * Safety Features:
   * - Timeout prevents infinite loops if motors stall
   * - opModeIsActive() allows emergency stop
   * - idle() yields CPU time to other FTC systems
   */
  private void autoDrive(double speed, int leftDistanceInch, int rightDistanceInch, int timeout_ms) {
    // Reset safety timeout timer
    autoDriveTimer.reset();

    // Calculate target positions in encoder ticks
    // getCurrentPosition() returns cumulative encoder count since power-on
    leftDrive.setTargetPosition((int) (leftDrive.getCurrentPosition() + leftDistanceInch * WHEELS_INCHES_TO_TICKS));
    rightDrive.setTargetPosition((int) (rightDrive.getCurrentPosition() + rightDistanceInch * WHEELS_INCHES_TO_TICKS));

    // Switch to RUN_TO_POSITION mode (motors automatically drive to target)
    leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    // Set motor power (Math.abs ensures positive power regardless of direction)
    leftDrive.setPower(Math.abs(speed));
    rightDrive.setPower(Math.abs(speed));

    // Wait while motors are driving to target
    // Loop exits when: motors reach target OR timeout expires OR OpMode stops
    while (opModeIsActive() && (leftDrive.isBusy() || rightDrive.isBusy()) && autoDriveTimer.milliseconds() < timeout_ms) {
      idle();  // Yields CPU time to FTC system (prevents watchdog timeout)
    }

    // Stop motors
    leftDrive.setPower(0);
    rightDrive.setPower(0);

    // Return to normal drive mode for TeleOp compatibility
    leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
  }

  /**
   * Blue Alliance Autonomous Routine
   *
   * Optimized scoring sequence for Blue alliance starting position.
   *
   * Autonomous Strategy:
   * 1. Shoot pre-loaded balls for up to 10 seconds
   * 2. Back away from goal (12 inches)
   * 3. Turn left to align with launch line
   * 4. Drive backward off launch line (50 inches)
   *
   * FTC Autonomous Scoring:
   * - 30-second autonomous period at start of match
   * - Pre-loaded balls (typically 3) can be scored
   * - Robot must park off launch line for bonus points
   * - Different paths for Blue vs Red due to field symmetry
   *
   * Why 10-Second Shooting Limit?
   * - Leaves 20 seconds for navigation
   * - Prevents wasting time if feeder jams
   * - Ensures robot has time to park
   *
   * Movement Breakdown:
   * - Step 1: Back up 12" at 50% speed (clear goal zone)
   * - Step 2: Turn left by differential drive (-8" left, +8" right)
   * - Step 3: Drive backward 50" at full speed (cross launch line)
   *
   * Telemetry Usage:
   * - Displays countdown timer during shooting
   * - Helps debug timing issues
   * - Shows autonomous is running (vs frozen)
   */
  private void doAutoBlue() {
    if (opModeIsActive()) {
      // Display which autonomous is running
      telemetry.addData("RUNNING OPMODE", operationSelected);
      telemetry.update();

      // ===== PHASE 1: SHOOT PRE-LOADED BALLS =====
      autoLaunchTimer.reset();
      while (opModeIsActive() && autoLaunchTimer.milliseconds() < 10000) {
        BANK_SHOT_AUTO();  // Continuous shooting with smart feeding
        telemetry.addData("Launcher Countdown", autoLaunchTimer.seconds());
        telemetry.update();
      }

      // Stop all shooter components
      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);
      servo.setPower(0);

      // ===== PHASE 2: NAVIGATE TO PARKING ZONE =====
      // Back Up from goal
      autoDrive(0.5, -12, -12, 5000);

      // Turn Left (Blue alliance turn direction)
      // Left wheel: -8" (backward), Right wheel: +8" (forward) = left turn
      autoDrive(0.5, -8, 8, 5000);

      // Drive off Launch Line (park for bonus points)
      autoDrive(1, -50, -50, 5000);
    }
  }

  /**
   * Red Alliance Autonomous Routine
   *
   * Optimized scoring sequence for Red alliance starting position.
   * IDENTICAL to Blue alliance except turn direction (field symmetry).
   *
   * Key Difference from Blue:
   * - Turn direction is reversed (turns right instead of left)
   * - Left wheel: +8" (forward), Right wheel: -8" (backward) = right turn
   * - This accounts for mirrored field layout
   *
   * All other logic is identical to doAutoBlue()
   * See doAutoBlue() for detailed explanation
   */
  private void doAutoRed() {
    if (opModeIsActive()) {
      // Display which autonomous is running
      telemetry.addData("RUNNING OPMODE", operationSelected);
      telemetry.update();

      // ===== PHASE 1: SHOOT PRE-LOADED BALLS =====
      autoLaunchTimer.reset();
      while (opModeIsActive() && autoLaunchTimer.milliseconds() < 10000) {
        BANK_SHOT_AUTO();  // Continuous shooting with smart feeding
        telemetry.addData("Launcher Countdown", autoLaunchTimer.seconds());
        telemetry.update();
      }

      // Stop all shooter components
      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);
      servo.setPower(0);

      // ===== PHASE 2: NAVIGATE TO PARKING ZONE =====
      // Back Up from goal
      autoDrive(0.5, -12, -12, 5000);

      // Turn Right (Red alliance turn direction - ONLY DIFFERENCE FROM BLUE)
      // Left wheel: +8" (forward), Right wheel: -8" (backward) = right turn
      autoDrive(0.5, 8, -8, 5000);

      // Drive off Launch Line (park for bonus points)
      autoDrive(1, -50, -50, 5000);
    }
  }
}
