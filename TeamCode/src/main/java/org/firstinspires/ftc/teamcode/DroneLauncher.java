package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DroneLauncher
{

    Telemetry telemetry;

    CRServo launcher = null;

    public void init(HardwareMap hwMap, Telemetry telem) {
        try {
            launcher = hwMap.crservo.get("launcher");
        } catch (Exception e) {
            telemetry.addData("launcher not found", 0);
        }
    }

    public void shoot(double power) {
        launcher.setPower(power);
    }
}
