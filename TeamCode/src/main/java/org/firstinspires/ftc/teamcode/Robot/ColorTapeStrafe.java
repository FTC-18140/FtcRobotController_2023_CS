package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp (name = "TapeStrafe", group = "Chassis enhancements")
public class ColorTapeStrafe extends OpMode {
    Thunderbot2023 robot = new Thunderbot2023();
    TBDGamepad gp1;
    private boolean toggle = false;

    public void init() {
        gp1 = new TBDGamepad(gamepad1);
    }

    public void loop() {

        if (gp1.getButton(TBDGamepad.Button.A)) {
            toggle = !toggle;
        }

        if (gp1.getTrigger(TBDGamepad.Trigger.LEFT_TRIGGER) >= 0.1 || gp1.getTrigger(TBDGamepad.Trigger.RIGHT_TRIGGER) >= 0.1) {
            robot.turnToBackdrop(gp1.getLeftY(), gp1.getLeftX(),
                    gp1.getTrigger(TBDGamepad.Trigger.LEFT_TRIGGER), gp1.getTrigger(TBDGamepad.Trigger.RIGHT_TRIGGER));
        } else {
            robot.tapeStrafeDrive(gp1.getLeftY(), gp1.getLeftX(), gp1.getRightX(), toggle);
        }

        telemetry.addData("Heading", robot.getHeading());

    }
}