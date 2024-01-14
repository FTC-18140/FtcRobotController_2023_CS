package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

@Autonomous
public class AutoRedDownstage extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    int state = 0;
    boolean done = false;

    // Adjust these numbers
    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)

    final double SPEED_GAIN  =  0.02  ;
    final double STRAFE_GAIN =  0.015 ;
    final double TURN_GAIN   =  0.01  ;

    final double MAX_AUTO_SPEED = 0.5;
    final double MAX_AUTO_STRAFE= 0.5;
    final double MAX_AUTO_TURN  = 0.3;

    double stepB = 0;
    public static double stepBLeft = -90;
    public static double stepBCenter = 0;
    public static double stepBRight = 90;
    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private int tagNum;


    /*
     Manually set the camera gain and exposure.
     This can only be called AFTER calling initAprilTag(), and only works for Webcams;
    */
//    private void    setManualExposure(int exposureMS, int gain) {
//        // Wait for the camera to be open, then use the controls
//
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

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, false);

//        if (USE_WEBCAM) {
//            setManualExposure(6, 250);  // Use low exposure time to reduce motion blur
//        }
    }

    /**
     * User-defined init_loop method
     * <p>
     * This method will be called repeatedly during the period between when
     * the init button is pressed and when the play button is pressed (or the
     * OpMode is stopped).
     * <p>
     * This method is optional. By default, this method takes no action.
     */
    @Override
    public void init_loop()
    {
        super.init_loop();
        switch (robot.eyes.getSpikePos())
        {
            case "LEFT":
                tagNum = 4;
                stepB = stepBLeft;
                break;
            case "RIGHT":
                tagNum = 6;
                stepB = stepBRight;
                break;
            default: // default CENTER
                tagNum = 5;
                stepB = stepBCenter;
                break;
        }
        telemetry.addData("Tag Number: ", tagNum );
    }

    @Override
    public void start() {
        robot.resetIMUYaw();
    }

    @Override
    public void loop() {
        robot.update();

        switch (state) {
            case 0:
            if (!done) {
                done = robot.gyroDrive(0, 130, 0.5);
            } else {
                robot.stop();
                done = false;
                state++;
            }
                break;

            // TODO Add the drop pixel command
            case 1:
            if (!done) {
                done = robot.turnTo(stepBCenter, 0.5);
            } else {
                robot.stop();
                done = false;
                state++;
            }
                break;
            case 2:
                if (!done) {
                    //done = robot.drive(stepBCenter, 2.5, 0.5);
                    done = true;
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 3:
                if (!done) {
                        done = robot.intake.setElbowPos(0.185);
                } else {
                    resetRuntime();
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 4:
                if (!done) {
                    if (getRuntime() > 2) {
                        done = robot.intake.dropBoth();
                    }
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 5:
                if (!done) {
                    done = robot.gyroDrive(0, 10, -0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 6:
                if (!done) {
               done = robot.turnTo(-90, 0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 7:
                if (!done) {
                    done = robot.delivery.autoSetWristPos(0.73);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 8:
                if (!done) {
                    done = robot.delivery.setElbowPos(0.275);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 9:
                if (!done) {
                        done = robot.gyroDrive(-90, 171.5, -0.5);
                } else {
                    resetRuntime();
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 10:
                if (!done) {
                    if (getRuntime() > 1)
                        done = robot.delivery.dropBoth();
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 11:
                if (!done) {
                    done = robot.drive(0, 50, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 12:
                if (!done) {
                    done = robot.turnTo(0,0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 13:
                if (!done) {
                    robot.delivery.goTo(Delivery.Positions.AUTO_INIT);
                    done = true;
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 14:
                if (!done) {
                    done = robot.gyroDrive(0, 100, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 15:
                if (!done) {
                    done = robot.turnTo(90, 0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 16:
                if (!done) {
                    done = robot.gyroDrive(90, 50, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            default:
                break;
        }
        telemetry.addData("step", state);
    }
}
