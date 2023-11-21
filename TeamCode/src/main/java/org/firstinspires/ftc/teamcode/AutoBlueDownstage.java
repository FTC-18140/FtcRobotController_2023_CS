package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous(name = "AutoBlueDown", group = "Autonomous")
public class AutoBlueDownstage extends OpMode {

    Thunderbot2023 robot = new Thunderbot2023();
    int state = 0;
    boolean done = false;
    double INIT_WRIST_POS = 1;
    double INIT_ARM_POS = 0.6;
    double INIT_DELIVERY_POS = 0;
    double INIT_INTAKE_POS = 0;
    @Override
    public void init() {
        robot.init(hardwareMap, telemetry, false);
//        telemetry.addData("Init", "Start");
        try {
            robot.intake.intakeLift(INIT_INTAKE_POS);

            robot.delivery.deliver.setPosition(INIT_DELIVERY_POS);

            robot.lift.rightArm.setPosition(INIT_ARM_POS);
            robot.lift.leftArm.setPosition(INIT_ARM_POS);

            robot.delivery.wristMove(INIT_WRIST_POS);
        } catch(Exception e) {
            telemetry.addData("Positions for attachments not found", 0);
        }
        telemetry.addData("Init", "Done");
    }

    @Override
    public void start() {
    }

    @Override
    public void loop () {
        robot.update();

        switch (state) {
            case 0:
                if (!done) {
                    done = robot.drive(0, 75,  0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 1:
                if (!done) {
                    done = robot.intake.intakeLift(0.175);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                    resetRuntime();
                }
                break;
            case 2:
                if (!done) {
                    robot.intake.intakeMove(0.18);
                    done = getRuntime() > 5;
                } else {
                    robot.intake.intakeMove(0);
                    done = false;
                    state++;
                }
                break;
            case 3:
                if (!done) {
                    done = robot.intake.intakeLift(0);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
            case 4:
                if (!done) {
                    done = robot.drive(270, 155, 0.5);
                } else {
                    robot.stop();
                    done = false;
                    state++;
                }
                break;
//                case 5:
//                    if (!done) {
//                        done = robot.drive(0, 105, 0.5);
//                    } else {
//                        robot.stop();
//                        done = false;
//                        state++;
//                    }
//                    break;
//                case 6:
//                    if (!done) {
//                        done = robot.drive(90, 115, 0.5);
//                    } else {
//                        robot.stop();
//                        done = false;
//                        state++;
//                    }
//                    break;

            default:
                break;
            // TODO test code then add other steps from notebook
        }
        telemetry.addData("step: ", state);
    }

}

