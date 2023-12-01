//package org.firstinspires.ftc.teamcode;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Range;
//
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
//import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
//import org.firstinspires.ftc.vision.VisionPortal;
//import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
//import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
//@Autonomous
//public class AutoRedDownstage extends OpMode {
//
//    Thunderbot2023 robot = new Thunderbot2023();
//    Telemetry telemetry;
//    HardwareMap hwMap = null;
//    int state = 0;
//    boolean done = false;
//
//    // Adjust these numbers
//    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)
//
//    final double SPEED_GAIN  =  0.02  ;
//    final double STRAFE_GAIN =  0.015 ;
//    final double TURN_GAIN   =  0.01  ;
//
//    final double MAX_AUTO_SPEED = 0.5;
//    final double MAX_AUTO_STRAFE= 0.5;
//    final double MAX_AUTO_TURN  = 0.3;
//
//    private DcMotor leftFrontDrive   = null;  //  Used to control the left front drive wheel
//    private DcMotor rightFrontDrive  = null;  //  Used to control the right front drive wheel
//    private DcMotor leftBackDrive    = null;  //  Used to control the left back drive wheel
//    private DcMotor rightBackDrive   = null;  //  Used to control the right back drive wheel
//
//    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
//    private static final int DESIRED_TAG_ID = 4;     // Choose the tag you want to approach or set to -1 for ANY tag.
//    private VisionPortal visionPortal;               // Used to manage the video source.
//    private AprilTagProcessor aprilTag;              // Used for managing the AprilTag detection process.
//    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
//    private boolean targetFound = false;
//    private double  drive           = 0.2;
//    private double  strafe          = 0.2;
//    private double  turn            = 0.2;
//
//    /**
//     * Initialize the AprilTag processor.
//     */
//    private void initAprilTag() {
//        // Create the AprilTag processor by using a builder.
//        aprilTag = new AprilTagProcessor.Builder().build();
//
//        // Create the vision portal by using a builder.
//        if (USE_WEBCAM) {
//            visionPortal = new VisionPortal.Builder()
//                    .setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"))
//                    .addProcessor(aprilTag)
//                    .build();
//        } else {
//            visionPortal = new VisionPortal.Builder()
//                    .setCamera(BuiltinCameraDirection.BACK)
//                    .addProcessor(aprilTag)
//                    .build();
//        }
//    }
//
//    /*
//     Manually set the camera gain and exposure.
//     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
//    */
//    private void    setManualExposure(int exposureMS, int gain) {
//        // Wait for the camera to be open, then use the controls
//
//        if (visionPortal == null) {
//            return;
//        }
//
//        // Make sure camera is streaming before we try to set the exposure controls
//        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
//            telemetry.addData("Camera", "Ready");
//            telemetry.update();
//        }
//
//        // Set camera controls unless we are stopping.
//        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
//        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
//            exposureControl.setMode(ExposureControl.Mode.Manual);
//        }
//        exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
//        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
//        gainControl.setGain(gain);
//    }
//    @Override
//    public void init() {
//        robot.init(hwMap, telemetry, false);
//
//        // Initialize the Apriltag Detection process
//        initAprilTag();
//
//        if (USE_WEBCAM) {
//            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
//        }
//    }
//
//    @Override
//    public void start() {
//
//    }
//
//    @Override
//    public void loop() {
//        switch (state) {
//            case 0:
//            if (!done) {
//                done = robot.drive(80, 0.5);
//            } else {
//                robot.stop();
//                done = false;
//                state++;
//            }
//            break;
//
//            // TODO Add the drop pixel command
//            case 1:
//            if (!done) {
//                done = robot.turn(90, 0.5);
//            } else {
//                robot.stop();
//                done = false;
//                state++;
//            }
//            break;
//
//            case 2:
//            if (!done) {
//               done = robot.drive(75, 0.5);
//            } else {
//               robot.stop();
//               done = false;
//               state++;
//            }
//            break;
//
//            // TODO test code then add other steps from notebook
//            case 3:
//            if (!done) {
//                targetFound = false;
//                desiredTag  = null;
//
//                // Step through the list of detected tags and look for a matching tag
//                List<AprilTagDetection> currentDetections = aprilTag.getDetections();
//                for (AprilTagDetection detection : currentDetections) {
//                    if ((detection.metadata != null) &&
//                            ((DESIRED_TAG_ID < 0) || (detection.id == DESIRED_TAG_ID))  ){
//                        targetFound = true;
//                        desiredTag = detection;
//                        break;  // don't look any further.
//                    } else {
//                        telemetry.addData("Unknown Target", "Tag ID %d is not in TagLibrary\n", detection.id);
//                    }
//                }
//
//                // Tell the driver what we see, and what to do.
//                if (targetFound) {
//                    telemetry.addData(">","HOLD Left-Bumper to Drive to Target\n");
//                    telemetry.addData("Target", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
//                    telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
//                    telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
//                    telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
//                } else {
//                    telemetry.addData(">","Drive using joysticks to find valid target\n");
//                }
//
//                // If Left Bumper is being pressed, AND we have found the desired target, Drive to target Automatically .
//                if (gamepad1.left_bumper && targetFound) {
//
//                    // Determine heading, range and Yaw (tag image rotation) error so we can use them to control the robot automatically.
//                    double  rangeError      = (desiredTag.ftcPose.range - DESIRED_DISTANCE);
//                    double  headingError    = desiredTag.ftcPose.bearing;
//                    double  yawError        = desiredTag.ftcPose.yaw;
//
//                    // Use the speed and turn "gains" to calculate how we want the robot to move.
//                    drive  = Range.clip(rangeError * SPEED_GAIN, -MAX_AUTO_SPEED, MAX_AUTO_SPEED);
//                    turn   = Range.clip(headingError * TURN_GAIN, -MAX_AUTO_TURN, MAX_AUTO_TURN) ;
//                    strafe = Range.clip(-yawError * STRAFE_GAIN, -MAX_AUTO_STRAFE, MAX_AUTO_STRAFE);
//
//                    telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
//                } else {
//
//                    // drive using manual POV Joystick mode.  Slow things down to make the robot more controlable.
//                    drive  = -gamepad1.left_stick_y  / 2.0;  // Reduce drive rate to 50%.
//                    strafe = -gamepad1.left_stick_x  / 2.0;  // Reduce strafe rate to 50%.
//                    turn   = -gamepad1.right_stick_x / 3.0;  // Reduce turn rate to 33%.
//                    telemetry.addData("Manual","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);
//                }
//                telemetry.update();
//
//                // Apply desired axes motions to the drivetrain.
//                robot.joystickDrive(-drive, -strafe, -turn);
//
//                done = (desiredTag.ftcPose.range - DESIRED_DISTANCE) == DESIRED_DISTANCE;
//            } else {
//                robot.stop();
//                done = false;
//                state++;
//            }
//        }
//        telemetry.addData("step", state);
//    }
//}
