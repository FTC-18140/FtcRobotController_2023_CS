package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();

    @Override
    public void init() {
        telemetry.addData("Init", "Start");

        robot.init(hardwareMap, telemetry, false);

        telemetry.addData("Init", "Done");
    }

    @Override
    public void start() {
    }

    public void loop() {
        robot.update();

        //////////////
        // DRIVING
        //////////////
        if(gamepad1.a) {
            robot.imu.resetYaw();
        } else if (gamepad1.right_trigger > 0.5) {
            //TURBO
            robot.orientedDrive(-gamepad1.left_stick_y * 0.9, -gamepad1.left_stick_x * 0.9, gamepad1.right_stick_x);
        } else if (gamepad1.left_trigger > 0.5) {
            //ACCURATE
            robot.orientedDrive(-gamepad1.left_stick_y * 0.2, -gamepad1.left_stick_x * 0.2, gamepad1.right_stick_x);
        } else {
            //NORMAL
            robot.orientedDrive(-gamepad1.left_stick_y * 0.6, -gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x);
        }

        telemetry.addData("Left Linear Slide Position", robot.lift.leftLinear.getCurrentPosition());
        telemetry.addData("Right Linear Slide Position", robot.lift.rightLinear.getCurrentPosition());

        //////////////
        // INTAKE UP & DOWN
        //////////////

        // TODO: presets
        if (gamepad1.left_bumper) {
            robot.intake.leftIntake.setPosition(0.3);
            robot.intake.rightIntake.setPosition(0.3);
        } else if (gamepad1.right_bumper) {
            robot.intake.leftIntake.setPosition(0.5);
            robot.intake.rightIntake.setPosition(0.5);
        }

        //////////////
        // LAUNCHER
        //////////////

        if (gamepad1.dpad_left && gamepad1.b) {robot.delivery.shooter.setPower(1);}

        //////////////
        // INTAKE
        //////////////

        if (gamepad1.x) {
            robot.intake.intake.setPower(1);
        } else if (gamepad1.y) {
            robot.intake.intake.setPower(-1);
        }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////// GAMEPAD 2 //////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        /////////////
        // WRIST
        /////////////

        // TODO: presets
        if (gamepad2.dpad_up) {
            robot.delivery.wrist.setPosition(0.3);
        } else if (gamepad2.dpad_down) {
            robot.delivery.wrist.setPosition(0.5);
        }

        /////////////
        // ARM
        /////////////

        // TODO: presets
        if (gamepad2.left_bumper) {
            robot.lift.armMove(0.3);
        } else if (gamepad2.right_bumper) {
            robot.lift.armMove(0.5);
        }

        //////////////
        // LIFT
        //////////////

        // TODO: presets
        if (gamepad2.left_bumper) {
            robot.lift.linearUp(0.5);
        } else if (gamepad2.right_bumper) {
            robot.lift.linearDown(0.3);
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