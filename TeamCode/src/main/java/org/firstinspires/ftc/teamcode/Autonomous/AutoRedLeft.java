package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot.Delivery;
import org.firstinspires.ftc.teamcode.Robot.Intake;
import org.firstinspires.ftc.teamcode.Robot.Thunderbot2023;

import static org.firstinspires.ftc.teamcode.Robot.Thunderbot2023.Direction.LEFT;
import static org.firstinspires.ftc.teamcode.Robot.Thunderbot2023.Direction.RIGHT;

@Autonomous
@Disabled
public class AutoRedLeft extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    int state = 0;
    boolean done = false;

    // Adjust these numbers

    double stepB = 0;
    public static double stepBLeft = -90;
    public static double stepBCenter = 0;
    public static double stepBRight = 90;
    public static double stepTurnPixel = 0;
    public static double stepTurnPixelLeft = 90;
    public static double stepTurnPixelRight = -90;
    public static double stepTorwardPixel = 60;
    public static double stepTorwardPixelLeft = 10;
    public static double stepTorwardPixelRight = 10;
    private static final boolean USE_WEBCAM = true;  // Set true to use a webcam, or false for a phone camera
    private int tagNum;

    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, false, true);
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
                    done = robot.gyroDrive(0, 100, -0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 1:
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
            case 2:
                if (!done) {
                    done = robot.turnTo(stepTurnPixelLeft, 0.25);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 3:
                if (!done) {
                    done = robot.gyroDrive(stepTurnPixelLeft, stepTurnPixelLeft, 0.5);
                } else {
                    resetRuntime();
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
//            case 3:
//                if (!done) {
//                    if (getRuntime() > 1) {
//                        robot.intake.dropBoth();
//                        done = true;
//                    }
//                    } else {
//                        robot.stop();
//                        done = false;
//                        state++;
//                    }
//                break;
//            case 4:
//                if (!done) {
//                    done = robot.gyroDrive(0, 20, -0.5);
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 5:
//                if (!done) {
//                    robot.intake.mandibleClose();
//                    done = true;
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 6:
//                if (!done) {
//                    done = robot.turnTo(90, 0.25);
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 7:
//                if (!done) {
//                    done = robot.gyroDrive(90, 275, -0.5);
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 8:
//                if (!done) {
//                    robot.delivery.goTo(Delivery.Positions.ALIGN_TO_BACKDROP);
//                    done = true;
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 9:
//                if (!done) {
//                    done = robot.strafe(LEFT, 125, 0.5);
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 10:
//                if (!done) {
//                    done = robot.gyroDrive(90, 25, -0.5);
//                }  else {
//                    resetRuntime();
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 11:
//                if (!done) {
//                    if (getRuntime() > 1) {
//                        robot.delivery.dropBoth();
//                        done = true;
//                    }
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 12:
//                if (!done) {
//                    robot.delivery.goTo(Delivery.Positions.TELE_INIT);
//                    done = robot.strafe(RIGHT, 100, 0.5);
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
//            case 13:
//                if (!done) {
//                    done = robot.gyroDrive(90, 20, -0.5);
//                } else {
//                    robot.stop();
//                    done = false;
//                    state++;
//                }
//                break;
            default:
                break;
        }
        telemetry.addData("step", state);
    }
}
