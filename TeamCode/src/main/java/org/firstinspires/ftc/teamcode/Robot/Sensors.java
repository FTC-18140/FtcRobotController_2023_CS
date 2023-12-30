package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Sensors {
    Telemetry  telemetry;
    DistanceSensor dSensor = null;
    ColorSensor cSensor = null;

    public void init(HardwareMap hwMap, Telemetry telem)
    {
        telemetry = telem;
        try {
            dSensor = hwMap.get(DistanceSensor.class, "distance");
        } catch(Exception e) {
            telemetry.addData("Distance Sensor not found", 0);
        }
        try {
            cSensor = hwMap.colorSensor.get("color");
        } catch(Exception e) {
            telemetry.addData("Color Sensor not found", 0);
        }
    }

}
