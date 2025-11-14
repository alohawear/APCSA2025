---
title: Programming Resources
permalink: /robotics/programming/index.html
description: 'FTC robot programming guides and code resources'
---

## Getting Started with FTC Programming

Our team uses OnBot Java to program human control and autonomous operation of the robot.

- **Best for**: Intermediate programmers, quick iterations
- **Pros**: Real Java, browser-based, easy setup, built-in samples
- **Cons**: Limited debugging, requires connection to Robot Controller
- **Tool**: Runs in web browser, integrated with Robot Controller

### Our Team's Approach

**Programming Environment**: [OnBot Java]

**Version Control**: [GitHub - TBD]

---

## Essential Programming Concepts

### Robot Control System Basics

```java
\\ coming soon!
```

### Key Components to Program

1. **Drivetrain** - Movement and navigation
2. **Manipulators** - Arms, claws, intake mechanisms
3. **Sensors** - Distance, color, IMU (gyroscope)
4. **Vision** - Camera, AprilTags, object detection
5. **Autonomous** - Pre-programmed routines

---

## Code Examples & Templates

### Royal Robotics Code Examples

We have three complete, documented code examples for learning FTC programming:

1. **[Multi-Mode OpMode](/robotics/multi-mode-opmode/)** - Comprehensive example with three control modes
   - Keyboard drive (testing without gamepad)
   - Gamepad TeleOp (driver control)
   - Autonomous sequences
   - AprilTag vision processing
   - Differential drive control
   - Shooter mechanism with timed sequences

2. **[REV Starter Bot TeleOp](/robotics/rev-starter-teleop/)** - Professional driver control
   - Split-stick arcade drive
   - Velocity-controlled flywheel (DcMotorEx)
   - Smart feeding system (waits for flywheel spin-up)
   - Manual override controls
   - Real-time telemetry display

3. **[Combined TeleOp and Autonomous](/robotics/combined-teleop-auto/)** - Competition-ready
   - Pre-start mode selection (TeleOp / Auto Blue / Auto Red)
   - Encoder-based autonomous navigation
   - Alliance-specific autonomous routines
   - Timer-based shooting sequences
   - Code reuse between TeleOp and autonomous

Each example includes:
- Detailed explanations of FTC concepts
- Inline code comments
- Hardware requirements
- Control schemes
- Full downloadable source code

---

## Learning Resources

### Official FTC Resources
- [FTC Programming Resources](https://ftc-docs.firstinspires.org/en/latest/programming_resources/index.html)
- [FTC SDK Javadocs](https://javadoc.io/doc/org.firstinspires.ftc)
- [FTC GitHub Repositories](https://github.com/FIRST-Tech-Challenge)

### Community Tutorials
- [Game Manual 0 - Programming](https://gm0.org/en/latest/docs/software/index.html)
- [FTC Programming Tutorial Series (YouTube)](https://www.youtube.com/c/FIRSTTechChallenge)
- [Acme Robotics Advanced Tutorials](https://www.youtube.com/@AcmeRobotics)

### Java Learning
- [Codecademy Java Course](https://www.codecademy.com/learn/learn-java)
- [Oracle Java Tutorials](https://docs.oracle.com/javase/tutorial/)
- [W3Schools Java](https://www.w3schools.com/java/)

---

## Useful Programming Libraries

### Computer Vision
- **EasyOpenCV** - Camera vision processing
- **AprilTag Detection** - Localization and navigation
- **TensorFlow Lite** - Object detection (game pieces)

### Advanced Control
- **Road Runner** - Advanced path following and autonomous
- **FTC Dashboard** - Real-time tuning and debugging
- **MeepMeep** - Autonomous path visualization

### Utilities
- **FTC Library** - Community-contributed utilities
- **PID Controllers** - Precise motor control
- **State Machines** - Complex autonomous logic
