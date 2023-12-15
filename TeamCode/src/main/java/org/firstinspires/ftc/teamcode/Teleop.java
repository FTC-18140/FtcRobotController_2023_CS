package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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

        //////////////
        // DRIVING
        //////////////
        if (gamepad1.dpad_up && robot.sensors.dSensor.getDistance(DistanceUnit.CM) < 10) {
            robot.stop();
        }
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