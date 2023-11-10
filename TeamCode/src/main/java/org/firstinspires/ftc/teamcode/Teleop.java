package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.ejml.equation.IntegerSequence;

import java.awt.font.NumericShaper;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();

    double INIT_WRIST_POS = 1;
    double INIT_ARM_POS = 0.6;
    double ARM_INCREMENT = 0.0025;
    double  WRIST_INCREMENT = 0.01;
    double ARM_POS = 0.6;
    double WRIST_POS = 1;

    double INIT_DELIVERY_POS = 0;
    double INIT_INTAKE_POS = 0;
    @Override
    public void init() {
        telemetry.addData("Init", "Start");


        robot.init(hardwareMap, telemetry, false);

        telemetry.addData("Init", "Done");





        robot.lift.rightArm.setPosition(INIT_ARM_POS);
        robot.lift.leftArm.setPosition(INIT_ARM_POS);

        robot.delivery.wristMove(INIT_WRIST_POS);
        telemetry.addData("Wrist Position", robot.delivery.wrist.getPosition());
        try {
            robot.intake.intakeLift(INIT_INTAKE_POS);
            robot.delivery.deliver.setPosition(INIT_DELIVERY_POS);

            telemetry.addData("Left Intake Position", robot.intake.leftIntake.getPosition());
            telemetry.addData("Right Intake Position", robot.intake.rightIntake.getPosition());
        } catch(Exception e) {
            telemetry.addData("Positions for attachments not found", 0);
        }
    }

    @Override
    public void start() {}

    public void loop() {

        robot.update();

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////// GAMEPAD 1 //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        //////////////
        // DRIVING
        //////////////

        // TODO: FINSIH ORIENTEDDRIVE CODE FOR MEET2
//        if(gamepad1.left_stick_button && gamepad1.right_stick_button) {
//            robot.imu.resetYaw();
//            telemetry.addData("imu: ", "reset");
//        }
//        if (gamepad1.left_trigger > 0.5) {
//            if (gamepad1.right_trigger > 0.5) {
//                //TURBO
//                robot.orientedDrive(-gamepad1.left_stick_y * 0.9, -gamepad1.left_stick_x * 0.9, gamepad1.right_stick_x);
//            } else if (gamepad1.left_trigger > 0.5) {
//                //ACCURATE
//                robot.orientedDrive(-gamepad1.left_stick_y * 0.2, -gamepad1.left_stick_x * 0.2, gamepad1.right_stick_x);
//            } else {
//                //NORMAL
//                robot.orientedDrive(-gamepad1.left_stick_y * 0.6, -gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x);
//            }

        if (gamepad1.right_trigger > 0.2) {
            robot.joystickDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        } else if (gamepad1.left_trigger > 0.2) {
            robot.joystickDrive(-gamepad1.left_stick_y * 0.25, gamepad1.left_stick_x * 0.25, gamepad1.right_stick_x * 0.25);
        } else {
            robot.joystickDrive(-gamepad1.left_stick_y * 0.75, gamepad1.left_stick_x * 0.75, gamepad1.right_stick_x * 0.75);
        }
        telemetry.addData("Left Linear Slide Position", robot.lift.getLiftPosition());
        telemetry.addData("intake lift position", robot.intake.leftIntake.getPosition());
        telemetry.addData("intake right position", robot.intake.rightIntake.getPosition());
        telemetry.addData("leftArm Position", robot.lift.leftArm.getPosition());
        telemetry.addData("rightArm Position", robot.lift.rightArm.getPosition());


        //////////////
        // INTAKE UP & DOWN
        //////////////

//         TODO: presets
        if (gamepad1.left_bumper) {
            robot.intake.leftIntake.setPosition(0);
            robot.intake.rightIntake.setPosition(0.145);
        } else if (gamepad1.right_bumper) {
            robot.intake.leftIntake.setPosition(0.08);
            robot.intake.rightIntake.setPosition(0.225);
        }

        //////////////
        // INTAKE
        //////////////

        if (gamepad1.x) {
            robot.intake.intake.setPower(-0.85);
        } else if (gamepad1.y) {
            robot.intake.intake.setPower(0.85);
        } else {
            robot.intake.intake.setPower(0);
        }

        //////////////
        // RAMP
        //////////////
        if (gamepad1.a) {
            robot.intake.rampMove(1);
        } else if(gamepad1.b) {
            robot.intake.rampMove(-1);
        } else {
            robot.intake.rampMove(0);
        }


        //////////////
        // LAUNCHER
        //////////////

        if (gamepad1.dpad_left && gamepad1.b) {
            robot.delivery.shooter.setPower(1);
        }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////// GAMEPAD 2 //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////
        // WRIST
        /////////////


        ////////////////////
        // WRIST INCREMENT
        ////////////////////

        //Todo: Uncomment this
        if (gamepad2.dpad_down) {
            WRIST_POS += WRIST_INCREMENT;
        } else if (gamepad2.dpad_up) {
            WRIST_POS -= WRIST_INCREMENT;
        }
        WRIST_POS = Range.clip(WRIST_POS, robot.delivery.getWRIST_MIN(), robot.delivery.getWRIST_MAX());
        robot.delivery.wristMove(WRIST_POS);

    telemetry.addData("wrist pos", robot.delivery.wrist.getPosition());

        /////////////
        // ARM
        /////////////

//        if ( gamepad2.x) {
//            robot.lift.armMove(0.699);
//        } else if(gamepad2.b) {
//            robot.lift.armMove(0.93);
//        }
        // TODO: presets
        if (gamepad2.x) {
            ARM_POS += ARM_INCREMENT;
        } else if (gamepad2.b) {
            ARM_POS -= ARM_INCREMENT;
        }
        ARM_POS = Range.clip(ARM_POS, robot.lift.getARM_MIN(), robot.lift.getARM_MAX());
        robot.lift.armMove(ARM_POS);


        //////////////
        // LIFT
        //////////////

        // TODO: presets
        if (gamepad2.left_stick_button  && gamepad2.right_stick_button) {
            if (gamepad2.y) {
                robot.lift.linearNoFatherFig(1);
            } else if (gamepad2.a) {
                robot.lift.linearNoFatherFig(-1);
            } else {
                robot.lift.linearNoFatherFig(0);
            }
        } else {
            if (gamepad2.y) {
                robot.lift.linearMove(1);
            } else if (gamepad2.a) {
                robot.lift.linearMove(-1);
            } else {
                robot.lift.linearMove(0);
            }
        }



        //////////////
        // DELIVERY
        //////////////

        if (gamepad2.right_trigger > 0.2) {
            robot.delivery.drop(1);
        } else {
            robot.delivery.drop(0);
        }
    }
}