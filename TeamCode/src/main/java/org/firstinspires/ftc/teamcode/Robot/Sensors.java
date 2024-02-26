package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
@Disabled
public class Sensors {
    Telemetry  telemetry;
    DistanceSensor dleft = null;
    DistanceSensor dright = null;
    ColorSensor colorleft = null;
    ColorSensor colorright = null;

    public void init(HardwareMap hwMap, Telemetry telem)
    {
        telemetry = telem;
        try {
            dleft = hwMap.get(DistanceSensor.class, "dleft");
            dright = hwMap.get(DistanceSensor.class, "dright");
        } catch(Exception e) {
            telemetry.addData("Distance Sensor(s)", "not found");
        }
        try {
            colorleft = hwMap.colorSensor.get("colorleft");
            colorright = hwMap.colorSensor.get("colorright");
        } catch(Exception e) {
            telemetry.addData("Color Sensor(s)", "not found");
        }
    }

}
