package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode  {

    Thunderbot2023 robot = new Thunderbot2023();

//    public void init(HardwareMap ahwMap, Telemetry telem, boolean withVision) {
//        telemetry.addData("Init", "Start");
//
//        robot.init(hardwareMap, telemetry, false);
//
//        telemetry.addData("Init", "Done");
//        //launcher.init(hardwareMap);
//
//
//
//
//    }

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
        // robot.update();
//                if (gamepad1.left_trigger > 0.1) {
//            // TURBO!!!
//            robot.OrientedDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
//        } else if (gamepad1.right_trigger > 0.1) {
//            // 20 percent
//            robot.OrientedDrive(-gamepad1.left_stick_y * 0.2, gamepad1.left_stick_x * 0.2, gamepad1.right_stick_x * 0.2);
//        } else {
//            // Normal Drive
//            double sign = Math.signum(gamepad1.right_stick_x);
//            robot.OrientedDrive(-gamepad1.left_stick_y * 0.6, gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x * 0.8 * gamepad1.right_stick_x * sign);
//        }
        robot.OrientedDrive(-gamepad1.left_stick_y * 0.6, gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x);
    }
}