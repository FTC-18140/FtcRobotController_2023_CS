package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.TGEVisionProcessor;
import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

import static org.firstinspires.ftc.teamcode.Robot.Thunderbot2023.Direction.LEFT;
import static org.firstinspires.ftc.teamcode.Robot.Thunderbot2023.Direction.RIGHT;

@Autonomous
public class AutoRedRight extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    int state = -1;
    boolean done = false;

    // Adjust these numbers
    final double DESIRED_DISTANCE = 12.0; //  this is how close the camera should get to the target (inches)

    final double SPEED_GAIN = 0.02;
    final double STRAFE_GAIN = 0.015;
    final double TURN_GAIN = 0.01;

    final double MAX_AUTO_SPEED = 0.5;
    final double MAX_AUTO_STRAFE = 0.5;
    final double MAX_AUTO_TURN = 0.3;

    double step0 = 5;
    double step0Left = 60;
    double stepA = 0;
    double stepALeft = -45;
    double  stepARight = 30;
    double stepBDistance = 0;
    double stepBAngle = 0;
    double stepBLDistance = 0;
    double stepBLAngle = -45;
    double stepBRAngle = 30;
    double stepBRDistance = 0;
    double stepD = 75;
    double stepDLeft = 20;
    double stepDRight = 45;
    double stepAwayPixel = 35;
    double stepAwayPixelLeft = 40;
    double stepAwayPixelRight = 40;
    double stepToBackboard = 125;
    double stepToBackboardLeft = 140;
    double stepToBackboardRight = 122.5;
    Thunderbot2023.Direction stepStrafe = RIGHT;
    Thunderbot2023.Direction stepStrafeLeft = RIGHT;
    Thunderbot2023.Direction stepStrafeRight = RIGHT;
    double stepStrafeDistance = 2.5;
    double stepStrafeDistanceLeft = 60;
    double stepStrafeDistanceRight = 15;
    double stepPark = 100;
    double stepParkLeft = 130;
    double stepParkRight = 65;
    double stepForward = 10;
    double stepForwardLeft = 5;
    double stepForwardCenter = 7.5;

    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private int tagNum;


    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, true, true);
        TGEVisionProcessor.theColor = "RED";
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
            case "NOT FOUND":
                step0 = step0Left;
                stepA = stepALeft;
                stepD = stepDLeft;
                stepBAngle = stepBLAngle;
                stepBDistance = stepBLDistance;
                stepToBackboard = stepToBackboardLeft;
                stepStrafe = stepStrafeLeft;
                stepStrafeDistance = stepStrafeDistanceLeft;
                stepPark = stepParkLeft;
                stepAwayPixel = stepAwayPixelLeft;
                stepForward = stepForwardLeft;
                tagNum = 4;
                telemetry.addData("ZONE = LEFT", 0);
                break;
            case "RIGHT":
                step0 = 15;
                stepA = stepARight;
                stepBAngle = stepBRAngle;
                stepBDistance = stepBRDistance;
                stepD = stepDRight;
                stepToBackboard = stepToBackboardRight;
                stepStrafe = stepStrafeRight;
                stepStrafeDistance = stepStrafeDistanceRight;
                stepPark = stepParkRight;
                stepAwayPixel = stepAwayPixelRight;
                stepForward = 10;
                tagNum = 6;
                telemetry.addData("ZONE = RIGHT", 0);
                break;
            default: // default CENTER
                step0 = 15;
                stepA = 0;
                stepBAngle = 0;
                stepBDistance = 0;
                stepD = 77.5;
                stepToBackboard = 137.5;
                stepStrafe = LEFT;
                stepStrafeDistance = 0;
                stepPark = 105;
                stepAwayPixel = 20;
                stepForward = stepForwardCenter;
                tagNum = 5;
                telemetry.addData("ZONE = CENTER", 0);
                break;
        }
        telemetry.addData("Tag Number: ", tagNum );
    }

    @Override
    public void start() {
        robot.resetIMUYaw();
      //  robot.eyes.activateBackCamera();
    }

    @Override
    public void loop() {
        robot.update();
        switch (state) {
            case -1:
                if (!done) {
                    robot.intake.goTo(Intake.Positions.WAIT_TO_INTAKE, false);
                    robot.intake.mandibleOpen();
                    done = true;
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 0:
                if (!done) {
                    done = robot.gyroDrive(0, step0, 0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 1:
                if (!done) {
                    done = robot.turnTo(stepA, 0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 2:
                if (!done) {
                    done = robot.gyroDrive(stepA, stepD, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 3:
                if (!done) {
                    done = robot.turnTo(stepBAngle, 0.25);
                }  else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 4:
                if (!done) {
                    done = robot.gyroDrive(stepBAngle, stepBDistance, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 5:
                if (!done) {
                    if (getRuntime() > 1) {
                        robot.intake.dropBoth();
                        done = true;
                    }
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 6:
                if (!done) {
                    done = robot.gyroDrive(stepBAngle, stepAwayPixel, -0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 7:
                if ( !done) {
                    robot.intake.mandibleClose();
                    done = true;
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 8:
                if (!done) {
                    done = robot.gyroDrive(stepBAngle, stepForward, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 9:

                if (!done) {
                    done = robot.turnTo(-90, 0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 10:
                if (!done) {
                    robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
                    done = true;
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 11:
                if (!done) {
                    done = robot.gyroDrive(-90, stepToBackboard, -0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 12:
                if (!done) {
                    done = robot.strafe(stepStrafe, stepStrafeDistance, 0.5);
                } else {
                    resetRuntime();
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 13:
                if (!done) {
                    if (getRuntime() > 1) {
                        robot.delivery.dropBoth();
                        done = true;
                    }
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 14:
                if (!done) {
                    done = robot.gyroDrive(-90, 7.5, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 15:
                if (!done) {
                    done = robot.strafe(LEFT, stepPark, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 16:
                if (!done) {
                    robot.delivery.goTo(Delivery.Positions.TELE_INIT);
                    done = true;
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 17:
                if (!done) {
                    done = robot.gyroDrive(-90, 25, -0.5);
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
