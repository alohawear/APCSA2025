package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

/**
 * REV Starter Bot TeleOp Mode
 *
 * This OpMode controls the REV Starter Bot during the driver-controlled (TeleOp) period.
 * The robot features a differential drivetrain and a ball launcher with flywheel shooter.
 *
 * Robot Hardware:
 * - 2x Drive Motors (differential/tank drive)
 * - 1x Flywheel Motor (ball launcher with velocity control)
 * - 1x Core Hex Motor (ball feeder mechanism)
 * - 1x Continuous Rotation Servo (hopper agitator)
 *
 * Key Features:
 * - Split-stick arcade drive (left stick = forward/back, right stick = turn)
 * - Velocity-controlled flywheel using DcMotorEx for precision shooting
 * - Automatic shooting sequences that wait for flywheel to reach target velocity
 * - Manual controls for testing and fine-tuning
 *
 * @TeleOp annotation makes this OpMode appear in the TeleOp category on Driver Station
 */
@TeleOp
public class REVStarterBotTeleOpJava extends LinearOpMode {

  // ===== HARDWARE DECLARATIONS =====
  private DcMotor flywheel;     // Shooter flywheel (uses DcMotorEx for velocity control)
  private DcMotor coreHex;      // Ball feeder motor
  private DcMotor leftDrive;    // Left drivetrain motor
  private CRServo servo;        // Continuous rotation servo (hopper agitator)
  private DcMotor rightDrive;   // Right drivetrain motor

  // ===== FLYWHEEL VELOCITY CONSTANTS =====
  // Setting our velocity targets. These values are in ticks per second!
  // Motor encoders measure rotation in "ticks" - one full rotation = specific tick count
  // Higher values = faster spin = farther shot distance
  private static final int bankVelocity = 1300;   // Close-range shot (bank shot off backboard)
  private static final int farVelocity = 1900;    // Medium-range shot
  private static final int maxVelocity = 2200;    // Maximum power shot

  /**
   * Main OpMode Entry Point
   *
   * Initializes hardware, configures motors, waits for start, then enters main control loop.
   *
   * FTC OpMode Lifecycle:
   * 1. INIT - Hardware mapping and configuration (before waitForStart)
   * 2. START - Driver presses play (waitForStart returns)
   * 3. ACTIVE - Main control loop (while opModeIsActive)
   * 4. STOP - Driver presses stop or autonomous time expires
   */
  @Override
  public void runOpMode() {
    // ===== HARDWARE INITIALIZATION =====
    // Map hardware devices from Robot Controller configuration
    flywheel = hardwareMap.get(DcMotor.class, "flywheel");
    coreHex = hardwareMap.get(DcMotor.class, "coreHex");
    leftDrive = hardwareMap.get(DcMotor.class, "leftDrive");
    servo = hardwareMap.get(CRServo.class, "servo");
    rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");

    // ===== MOTOR CONFIGURATION =====
    // Establishing the direction and mode for the motors

    // Flywheel: Use encoder for velocity control
    // RUN_USING_ENCODER enables closed-loop velocity control (maintains consistent speed)
    flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    flywheel.setDirection(DcMotor.Direction.REVERSE);

    // Reverse motors that are mounted backwards
    coreHex.setDirection(DcMotor.Direction.REVERSE);
    leftDrive.setDirection(DcMotor.Direction.REVERSE);

    // Ensures the servo is active and ready (0 = stopped for continuous servo)
    servo.setPower(0);

    // ===== WAIT FOR START =====
    // Blocks here until driver presses START on Driver Station
    waitForStart();

    // ===== MAIN CONTROL LOOP =====
    // opModeIsActive() returns true until STOP is pressed or time expires
    if (opModeIsActive()) {
      while (opModeIsActive()) {
        // Calling our methods while the OpMode is running
        splitStickArcadeDrive();              // Process drive inputs
        setFlywheelVelocity();                // Process shooter inputs
        manualCoreHexAndServoControl();       // Process feeder/agitator inputs

        // Display real-time flywheel diagnostics
        telemetry.addData("Flywheel Velocity", ((DcMotorEx) flywheel).getVelocity());
        telemetry.addData("Flywheel Power", flywheel.getPower());
        telemetry.update();
      }
    }
  }

  /**
   * Split-Stick Arcade Drive Control
   *
   * Implements intuitive driving where:
   * - Left stick Y-axis: Forward/backward movement
   * - Right stick X-axis: Left/right turning
   *
   * This allows simultaneous driving and turning for smooth navigation.
   *
   * Differential Drive Math:
   * - Left motor = y - x  (forward - turn)
   * - Right motor = y + x (forward + turn)
   *
   * Examples:
   * - Push left stick up (y=1, x=0): Both motors = 1 → Forward
   * - Push right stick right (y=0, x=1): Left=−1, Right=1 → Spin right
   * - Both sticks (y=0.5, x=0.5): Left=0, Right=1 → Arc right
   *
   * Why negative left_stick_y?
   * - Gamepad Y-axis: up = negative, down = positive
   * - Negating makes forward feel natural
   */
  private void splitStickArcadeDrive() {
    float x;  // Horizontal input (turning)
    float y;  // Vertical input (forward/backward)

    x = gamepad1.right_stick_x;
    y = -gamepad1.left_stick_y;  // Negate to make forward = positive
    leftDrive.setPower(y - x);
    rightDrive.setPower(y + x);
  }

  /**
   * Manual Control for Ball Feeder System
   *
   * Provides driver override controls for the ball feeding mechanism:
   * - Core Hex Motor: Feeds balls from hopper into flywheel
   * - Agitator Servo: Prevents ball jams in hopper
   *
   * Controls:
   * - Cross (X) Button: Feed balls forward (0.5 power)
   * - Triangle (Y) Button: Reverse feeder (-0.5 power)
   * - D-Pad Left: Agitate counterclockwise
   * - D-Pad Right: Agitate clockwise
   *
   * Use Cases:
   * - Clearing jams in the feeder
   * - Testing feeder speed independently
   * - Fine-tuning ball flow during practice
   */
  private void manualCoreHexAndServoControl() {
    // Manual control for the Core Hex intake
    if (gamepad1.cross) {
      coreHex.setPower(0.5);
    } else if (gamepad1.triangle) {
      coreHex.setPower(-0.5);
    }
    // Manual control for the hopper's servo
    if (gamepad1.dpad_left) {
      servo.setPower(1);
    } else if (gamepad1.dpad_right) {
      servo.setPower(-1);
    }
  }
  
  // ===== FLYWHEEL CONTROL SYSTEM =====

  /**
   * Flywheel Velocity Controller - Main Shooting Interface
   *
   * Manages flywheel speed and shooting sequences based on driver input.
   * Uses a priority-based if/else chain (first match wins).
   *
   * Control Scheme:
   * 1. Options Button: Manual reverse (for clearing jams)
   * 2. Left Bumper: Automatic far shot sequence (1900 ticks/sec)
   * 3. Right Bumper: Automatic bank shot sequence (1300 ticks/sec)
   * 4. Circle Button: Manual flywheel spin-up (bank velocity only)
   * 5. Square Button: Manual flywheel spin-up (max velocity)
   * 6. No Input: Stop everything
   *
   * DcMotorEx vs DcMotor:
   * - DcMotorEx provides setVelocity() for precise speed control
   * - Uses PID controller to maintain constant RPM regardless of load
   * - Regular DcMotor only has setPower() which varies with battery/load
   *
   * Velocity Control Benefits:
   * - Consistent shot distance even as battery drains
   * - Compensates for ball friction when feeding
   * - More accurate scoring
   */
  private void setFlywheelVelocity() {
    if (gamepad1.options) {
      // Emergency reverse (clear jammed balls)
      flywheel.setPower(-0.5);

    } else if (gamepad1.left_bumper) {
      // Automatic far shot (full sequence)
      farPowerAuto();

    } else if (gamepad1.right_bumper) {
      // Automatic bank shot (full sequence)
      bankShotAuto();

    } else if (gamepad1.circle) {
      // Manual flywheel only (bank velocity)
      ((DcMotorEx) flywheel).setVelocity(bankVelocity);

    } else if (gamepad1.square) {
      // Manual flywheel only (max velocity)
      ((DcMotorEx) flywheel).setVelocity(maxVelocity);

    } else {
      // No input: Stop all shooter components
      ((DcMotorEx) flywheel).setVelocity(0);
      coreHex.setPower(0);

      // The check below is in place to prevent stuttering with the servo.
      // It checks if the servo is under manual control!
      if (!gamepad1.dpad_right && !gamepad1.dpad_left) {
        servo.setPower(0);
      }
    }
  }

  /**
   * Bank Shot Automatic Sequence
   *
   * Optimized shooting sequence for close-range shots (touching goal or few inches away).
   * Uses the bank shot technique where balls bounce off the backboard into the goal.
   *
   * Smart Feeding Logic:
   * 1. Immediately spin flywheel to target velocity (1300 ticks/sec)
   * 2. Start agitator servo to prevent jams
   * 3. Wait until flywheel reaches speed (within 50 ticks/sec tolerance)
   * 4. Only then activate feeder to send balls into flywheel
   *
   * Why Wait for Velocity?
   * - Feeding balls into slow flywheel wastes shots (underpowered)
   * - Creates inconsistent shot distance
   * - May cause balls to jam in the mechanism
   *
   * Tolerance Explanation:
   * - Target: 1300 ticks/sec
   * - Threshold: 1250 ticks/sec (1300 - 50)
   * - Allows for small PID oscillations while maintaining accuracy
   *
   * Runs continuously while bumper is held (can shoot multiple balls)
   */
  private void bankShotAuto() {
    // Set target velocity
    ((DcMotorEx) flywheel).setVelocity(bankVelocity);

    // Always run agitator while shooting
    servo.setPower(-1);

    // Smart feeder control: only feed when flywheel is up to speed
    if (((DcMotorEx) flywheel).getVelocity() >= bankVelocity - 50) {
      coreHex.setPower(1);  // Feed balls at full speed
    } else {
      coreHex.setPower(0);  // Wait for flywheel to spin up
    }
  }

  /**
   * Far Power Shot Automatic Sequence
   *
   * Shooting sequence for medium-to-far range shots (several feet from goal).
   * Requires higher flywheel speed for increased projectile velocity.
   *
   * Smart Feeding Logic:
   * 1. Spin flywheel to higher velocity (1900 ticks/sec)
   * 2. Activate agitator servo
   * 3. Wait for flywheel to reach speed (within 100 ticks/sec tolerance)
   * 4. Activate feeder when ready
   *
   * Larger Tolerance (100 vs 50):
   * - Higher speeds have more PID oscillation
   * - Slightly larger tolerance prevents feeder stuttering
   * - Still maintains consistent shot accuracy
   *
   * Usage Note:
   * - May require adjusting deflector angle for optimal trajectory
   * - Test distance in practice to dial in velocity
   *
   * Runs continuously while bumper is held
   */
  private void farPowerAuto() {
    // Set higher target velocity for longer shots
    ((DcMotorEx) flywheel).setVelocity(farVelocity);

    // Always run agitator while shooting
    servo.setPower(-1);

    // Smart feeder control: only feed when flywheel is up to speed
    if (((DcMotorEx) flywheel).getVelocity() >= farVelocity - 100) {
      coreHex.setPower(1);  // Feed balls at full speed
    } else {
      coreHex.setPower(0);  // Wait for flywheel to spin up
    }
  }

}
