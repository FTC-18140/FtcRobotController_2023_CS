package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class SpinOpMode extends OpMode {
    SPIN launcher = new SPIN();

    @Override
    public void init() {
        launcher.init(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        if (gamepad1.a) {
            launcher.spin(1);
        }
    }
}
