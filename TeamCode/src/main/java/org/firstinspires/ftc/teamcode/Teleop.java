package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();

    double INIT_WRIST_POS = 0;
    double INIT_ARM_POS = 0.9;
    double INIT_DELIVERY_POS = 0;
    double INIT_INTAKE_POS = 0.9;
    double WRIST_INCREMENT = 0.001;
    double WRIST_POSITION;
    @Override
    public void init() {
        telemetry.addData("Init", "Start");

        robot.init(hardwareMap, telemetry, false);

        telemetry.addData("Init", "Done");

        robot.intake.rightIntake.setPosition(INIT_INTAKE_POS);
        robot.intake.leftIntake.setPosition(INIT_INTAKE_POS);
        robot.lift.leftArm.setPosition(INIT_ARM_POS);
        robot.lift.rightArm.setPosition(INIT_ARM_POS);
        try {
            robot.delivery.wrist.setPosition(INIT_WRIST_POS);
            robot.delivery.deliver.setPosition(INIT_DELIVERY_POS);
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
        if(gamepad1.left_stick_button && gamepad1.right_stick_button) {
            robot.imu.resetYaw();
            telemetry.addData("imu: ", "reset");
        }
        if (gamepad1.left_trigger > 0.5) {
            if (gamepad1.right_trigger > 0.5) {
                //TURBO
                robot.orientedDrive(-gamepad1.left_stick_y * 0.9, -gamepad1.left_stick_x * 0.9, gamepad1.right_stick_x);
            } else if (gamepad1.left_trigger > 0.5) {
                //ACCURATE
                robot.orientedDrive(-gamepad1.left_stick_y * 0.2, -gamepad1.left_stick_x * 0.2, gamepad1.right_stick_x);
            } else {
                //NORMAL
                robot.orientedDrive(-gamepad1.left_stick_y * 0.6, -gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x);
            }
        } else {
            robot.joystickDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
        telemetry.addData("Left Linear Slide Position", robot.lift.leftLinear.getCurrentPosition());
        telemetry.addData("Right Linear Slide Position", robot.lift.rightLinear.getCurrentPosition());
        telemetry.addData("intake lift position", robot.intake.leftIntake.getPosition());
        telemetry.addData("intake right position", robot.intake.rightIntake.getPosition());


        //////////////
        // INTAKE UP & DOWN
        //////////////

//         TODO: presets
        if (gamepad1.left_bumper) {
            robot.intake.leftIntake.setPosition(1);
            robot.intake.rightIntake.setPosition(1);
        } else if (gamepad1.right_bumper) {
            robot.intake.leftIntake.setPosition(0.7);
            robot.intake.rightIntake.setPosition(0.7);
        }

        //////////////
        // INTAKE
        //////////////

        if (gamepad1.x) {
            robot.intake.intake.setPower(-1);
        } else if (gamepad1.y) {
            robot.intake.intake.setPower(1);
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
            robot.delivery.launch(1);
        }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////// GAMEPAD 2 //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


        /////////////
        // WRIST
        /////////////

        // TODO: presets
        if (gamepad2.dpad_up) {
            robot.delivery.wrist.setPosition(0);
        } else if (gamepad2.dpad_down) {
            robot.delivery.wrist.setPosition(1);
        }


        ////////////////////
        // WRIST INCREMENT
        ////////////////////

        if (gamepad2.left_trigger > 0.2) {
            WRIST_POSITION += WRIST_INCREMENT;
            robot.delivery.wrist.setPosition(WRIST_POSITION);
        } else if (gamepad2.right_trigger > 0.2) {
            WRIST_POSITION -= WRIST_INCREMENT;
            robot.delivery.wrist.setPosition(WRIST_POSITION);
        }


        /////////////
        // ARM
        /////////////

        // TODO: presets
        if (gamepad2.left_bumper) {
            robot.lift.armMove(0);
        } else if (gamepad2.right_bumper) {
            robot.lift.armMove(0.9);
        }


        //////////////
        // LIFT
        //////////////

        // TODO: presets
        if (gamepad2.y) {
            robot.lift.linearMove(1);
        } else if (gamepad2.a) {
            robot.lift.linearMove(-1);
        } else {
            robot.lift.linearMove(0);
        }


        //////////////
        // DELIVERY
        //////////////

        if (gamepad2.x) {
            robot.delivery.deliver.setPosition(1);
        } else if (gamepad2.y) {
            robot.delivery.deliver.setPosition(0);
        }
    }
}