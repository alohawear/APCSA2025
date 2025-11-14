
/***********************************************************************
*                                                                      *
* OnbotJava Editor is still : beta! Please inform us of any bugs       |
* on our discord channel! https://discord.gg/e7nVjMM                   *
* Only BLOCKS code is submitted when in Arena                          *
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
     *
     * Steps:
     * 1. Create VisionPortal.Builder - manages camera and vision processors
     * 2. Configure camera from hardwareMap (device name: "webcam")
     * 3. Create AprilTagProcessor - analyzes frames for AprilTag markers
     * 4. Attach processor to the vision portal
     *
     * FTC Vision API: VisionPortal manages camera lifecycle and frame processing
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
     *
     * Motor Directions:
     * - REVERSE direction compensates for motors mounted in opposite orientations
     * - Without this, one motor would spin backward when given positive power
     *
     * Servo Position:
     * - Position 0.2 keeps the stopper closed to hold artifacts in the hopper
     * - Position 0.0 would open the gate to allow artifacts through
     *
     * isShooting Flag:
     * - Prevents overlapping shooting sequences that could jam the mechanism
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
     *
     * This method acts as a dispatcher, calling the appropriate control loop
     * based on the mode variable set in runOpMode().
     *
     * Mode 0: Keyboard Drive - For testing without gamepad
     * Mode 1: Gamepad Drive - Standard TeleOp (driver-controlled period)
     * Mode 2: Auto Drive - Autonomous period (robot runs pre-programmed routines)
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
     * Allows robot control via computer keyboard (useful for testing).
     * This mode continuously reads keyboard input and processes drive commands.
     *
     * Keyboard Mapping (ASCII codes):
     * - 105 (i): Forward
     * - 107 (k): Backward
     * - 106 (j): Turn Left
     * - 108 (l): Turn Right
     * - 112 (p): Shoot
     *
     * The subtraction creates directional values:
     * - horizontalInput: j(-1) or l(+1) or neither(0)
     * - verticalInput: i(+1) or k(-1) or neither(0)
     *
     * Loop runs while opModeIsActive() returns true (until STOP is pressed)
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
     * Standard driver-controlled mode using FTC gamepad.
     * Uses "tank" or "arcade" style driving with analog sticks.
     *
     * Gamepad Controls:
     * - Left Stick Y: Forward/Backward movement
     * - Right Stick X: Left/Right turning
     * - A Button: Shoot artifact
     *
     * The gamepad1 object is provided by the FTC SDK and automatically
     * reads input from the primary driver's controller.
     *
     * Why negative left_stick_y?
     * - Gamepad Y-axis is inverted (up = negative, down = positive)
     * - Negating makes forward feel natural (push up = move forward)
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
     * Pre-programmed routine that runs during the autonomous period (first 30 seconds).
     * This sequence demonstrates a complete autonomous strategy:
     *
     * 1. Drive to goal position
     * 2. Shoot 3 pre-loaded artifacts
     * 3. Drive to loading zone and return
     * 4. Shoot 3 more artifacts (if loaded)
     * 5. Switch to keyboard control for remaining time
     *
     * This time-based autonomous is simple but not very accurate. Better approaches
     * would use encoders (counting motor rotations) or vision (AprilTags).
     */
    public void autoDrive(){
      driveToGoal();
      shootThreeArtifacts();
      driveToLoadingSpotAndBack();
      shootThreeArtifacts();
      keyboardDrive();
    }

    /**
     * Drive to Goal - Autonomous Navigation
     *
     * Uses time-based driving to position robot at scoring location.
     *
     * Movement Sequence:
     * 1. Drive forward at full power for 1.2 seconds
     * 2. Turn right (left motor backward, right forward) for 0.23 seconds
     * 3. Stop and pause for 0.5 seconds to stabilize
     *
     * Limitations of Time-Based Driving:
     * - Battery voltage affects speed (low battery = slower)
     * - Floor friction varies (carpet vs. tiles)
     * - Motor wear changes performance over time
     *
     * Better Alternative: Use encoders to measure actual distance traveled
     */
    public void driveToGoal(){
      driveLeft.setPower(1);
      driveRight.setPower(1);
      sleep(1200);
      driveLeft.setPower(-1);
      driveRight.setPower(1);
      sleep(230);
      driveLeft.setPower(0);
      driveRight.setPower(0);
      sleep(500);
    }

    /**
     * Drive to Loading Spot and Return
     *
     * Moves robot to artifact loading zone and waits for human player to load artifacts.
     *
     * Sequence:
     * 1. Back up 1.5 seconds to reach loading zone
     * 2. Wait 10 seconds for human player to load artifacts
     * 3. Drive forward 1.5 seconds to return to shooting position
     * 4. Pause 0.5 seconds to stabilize
     *
     * The 10-second wait allows time for human players to manually load
     * artifacts into the robot's hopper during autonomous.
     */
    public void driveToLoadingSpotAndBack(){
      driveLeft.setPower(-1);
      driveRight.setPower(-1);
      sleep(1500);
      driveLeft.setPower(0);
      driveRight.setPower(0);
      sleep(10000);
      driveLeft.setPower(1);
      driveRight.setPower(1);
      sleep(1500);
      driveLeft.setPower(0);
      driveRight.setPower(0);
      sleep(500);
    }

    /**
     * Shoot Three Artifacts - Autonomous Scoring
     *
     * Fires three artifacts sequentially while checking for early termination.
     *
     * Loop Conditions:
     * - opModeIsActive(): Ensures OpMode hasn't been stopped
     * - nArtifacts > 0: Counts down from 3 to 0
     * - !isShooting: Waits for each shot to complete before starting next
     *
     * The shoot() method includes delays, so this loop waits for each
     * shooting sequence to finish before decrementing the counter.
     */
    public void shootThreeArtifacts(){
      nArtifacts = 3;
      while (opModeIsActive() && nArtifacts > 0) {
        // Put loop blocks here
        if (!isShooting) {
          shoot();
          nArtifacts -= 1;
        }
        displayVisionPortalData();
      }
    }

    /**
     * Process Drive Inputs - Differential Drive Algorithm
     *
     * Converts vertical (forward/back) and horizontal (turn) inputs into
     * individual motor powers using differential steering.
     *
     * Math Explanation:
     * - Left Motor = vertical + horizontal
     * - Right Motor = vertical - horizontal
     *
     * Examples:
     * 1. Forward Only (vertical=1, horizontal=0):
     *    Left=1+0=1, Right=1-0=1 → Both motors forward
     *
     * 2. Turn Right (vertical=0, horizontal=1):
     *    Left=0+1=1, Right=0-1=-1 → Left forward, right backward = spin right
     *
     * 3. Arc Forward-Right (vertical=0.5, horizontal=0.5):
     *    Left=0.5+0.5=1, Right=0.5-0.5=0 → Left fast, right stopped = curve right
     *
     * maxDrivePower acts as a speed limiter (typically 0.5 to 1.0)
     */
    public void processDriveInputs(){
      // Combine inputs to create drive and turn (or both!)
      driveLeft.setPower(verticalInput * maxDrivePower + horizontalInput * maxDrivePower);
      driveRight.setPower(verticalInput * maxDrivePower - horizontalInput * maxDrivePower);
    }
    
    /**
     * Shoot Artifact - Timed Shooting Sequence
     *
     * Controls the complete cycle of shooting one artifact.
     *
     * Shooting Mechanism:
     * - Flywheel spins continuously to build up speed
     * - Servo gate opens to allow one artifact through
     * - Artifact contacts spinning flywheel and is launched
     * - Servo gate closes to prevent multiple artifacts
     *
     * Timing Breakdown:
     * 1. Stop drive motors (prevents misalignment while shooting)
     * 2. Set isShooting flag (prevents overlapping sequences)
     * 3. Open servo gate (position 0.0)
     * 4. Spin flywheel at shootPower
     * 5. Wait 250ms for artifact to pass through
     * 6. Close servo gate (position 0.2)
     * 7. Wait 200ms for gate to fully close
     * 8. Stop flywheel
     * 9. Wait 1.5 seconds for mechanism to settle
     * 10. Clear isShooting flag (allow next shot)
     *
     * Total sequence time: ~2 seconds
     */
    public void shoot(){
      // Don"t move while shooting
      driveLeft.setPower(0);
      driveRight.setPower(0);
      isShooting = true;
      // Let one artifact come through
      artifactstopper.setPosition(0);
      shootwheel.setPower(shootPower);
      sleep(250);
      // Stop the next artifact
      artifactstopper.setPosition(0.2);
      sleep(200);
      shootwheel.setPower(0);
      sleep(1500);
      // Allow for a new shot to be triggered
      isShooting = false;
    }

    /**
     * Display Vision Portal Data - AprilTag Telemetry
     *
     * Reads AprilTag detections from the vision processor and displays them
     * on the Driver Station screen for debugging and navigation.
     *
     * AprilTag Data:
     * - ID: Unique identifier for each tag (helps identify field location)
     * - Range: Distance from camera to tag in inches
     * - Yaw: Horizontal angle to tag in degrees (-180 to +180)
     *
     * How AprilTags Work:
     * - Camera captures video frames
     * - AprilTagProcessor analyzes frames for tag patterns
     * - Detected tags provide position/orientation data
     * - Robot can use this to navigate or align with targets
     *
     * Telemetry:
     * - addData(): Queues data to send to Driver Station
     * - update(): Actually transmits the queued data
     *
     * Loop iterates through all detected tags (can see multiple simultaneously)
     */
    public void displayVisionPortalData(){
      myAprilTagDetections = (myApriltagProcessor.getDetections());
      for (String myAprilTagDetection2 : myAprilTagDetections) {
        myAprilTagDetection = myAprilTagDetection2;
        telemetry.addData("ID", (myAprilTagDetection.id));
        telemetry.addData("Range", (myAprilTagDetection.ftcPose.range));
        telemetry.addData("Yaw", (myAprilTagDetection.ftcPose.yaw));
      }
      telemetry.update();
    }
    
    /**
     * runOpMode() - Main Entry Point
     *
     * This is the required method for all LinearOpModes. The FTC SDK calls this
     * method when the OpMode is selected and initialized.
     *
     * Execution Flow:
     * 1. Hardware Initialization - Map physical devices to software objects
     * 2. Configuration - Set initial values and directions
     * 3. waitForStart() - Pause until driver presses START button
     * 4. Run Control Loop - Execute selected drive mode
     *
     * Hardware Mapping:
     * - hardwareMap.get() retrieves device references by name and type
     * - Names must match those configured in Robot Controller settings
     * - If a name doesn't match, the OpMode will crash with an error
     *
     * Critical FTC Pattern:
     * - All initialization happens BEFORE waitForStart()
     * - All active code happens AFTER waitForStart()
     * - This ensures robot doesn't move during initialization
     *
     * @Override indicates this method is required by the LinearOpMode parent class
     */
    @Override
    public void runOpMode() {
      // ===== HARDWARE INITIALIZATION =====
      // Retrieve hardware devices from Robot Controller configuration
      // Device names (strings) must exactly match the configuration
      driveLeft = hardwareMap.get(DcMotor.class, "driveLeft");
      driveRight = hardwareMap.get(DcMotor.class, "driveRight");
      shootwheel = hardwareMap.get(DcMotor.class, "shootwheel");
      artifactstopper = hardwareMap.get(Servo.class, "artifactstopper");
      color1 = hardwareMap.get(ColorSensor.class, "color1");
      distance1 = hardwareMap.get(DistanceSensor.class, "distance1");
      imu = hardwareMap.get(BNO055IMU.class, "imu");

      // ===== CONFIGURATION =====
      // Configure hardware before match starts
      inititalSetup();
      initializeVisionPortal();

      // Set shooter and drive parameters
      shootPower = 0.8;       // 80% power for flywheel (range: 0.0 to 1.0)
      maxDrivePower = 1;      // Full speed driving (can reduce for precision control)

      // Select operating mode
      // mode 0 = keyboard, 1 = gamepad, 2 = autonomous
      mode = 2;

      // ===== WAIT FOR START =====
      // Critical FTC method: blocks here until driver presses START
      // Robot is initialized but won't move yet
      waitForStart();

      // ===== ACTIVE PERIOD =====
      // This code only runs after START is pressed
      pickMode();
    }
    
}

