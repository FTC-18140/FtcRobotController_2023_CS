package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class ColorTapeTesting extends OpMode {
    Thunderbot2023 robot = new Thunderbot2023();
    TBDGamepad gp1;

    ColorSensor cright = robot.sensors.colorright;
    ColorSensor cleft = robot.sensors.colorleft;

    public void init() {
        gp1 = new TBDGamepad(gamepad1);
    }

    public void loop() {
        if (cleft.red() > 20 && cright.red() > 20) {
            // TODO: test this first before moving on
            robot.joystickDrive(0, gp1.getLeftX() * 0.2, 0);
        }
    }
}
