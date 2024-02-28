package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp
public class ColorTapeStrafe extends OpMode {
    Thunderbot2023 robot = new Thunderbot2023();
    TBDGamepad gp1;
    
    public void init() {
        gp1 = new TBDGamepad(gamepad1);
    }

    public void loop() {
        robot.tapeStrafeDrive(gp1.getLeftX(), gp1.getButton(TBDGamepad.Button.A));
    }
}