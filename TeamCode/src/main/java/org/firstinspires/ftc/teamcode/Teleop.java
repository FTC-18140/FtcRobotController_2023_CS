package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Teleop", group = "Teleop")
public class Teleop extends OpMode
{

    Thunderbot2023 robot = new Thunderbot2023();

    @Override
    public void init() {
        telemetry.addData("Init", "Start");

        robot.init(hardwareMap, telemetry, false);

        telemetry.addData("Init", "Done");


    }

    public void init_loop() {
    }

    @Override
    public void start() {
        robot.start();
    }

    @Override
    public void loop() {
        robot.update();
        /////////////////
        // CHASSIS
        /////////////////
        if (gamepad1.left_trigger > 0.1) {
            // TURBO!!!
            robot.joystickDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        } else if (gamepad1.right_trigger > 0.1) {
            // 20 percent
            robot.joystickDrive(-gamepad1.left_stick_y * 0.2, gamepad1.left_stick_x * 0.2, gamepad1.right_stick_x * 0.2);
        } else {
            // Normal Drive
            double sign = Math.signum(gamepad1.right_stick_x);
            robot.joystickDrive(-gamepad1.left_stick_y * 0.6, gamepad1.left_stick_x * 0.6, gamepad1.right_stick_x * 0.8 * gamepad1.right_stick_x * sign);
        }
    }
}
