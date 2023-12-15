package org.firstinspires.ftc.teamcode;

import android.graphics.Color;
import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Sensors {
    Telemetry  telemetry;
    DistanceSensor dSensor = null;
    ColorSensor cSensor = null;
    Thunderbot2023 robot = new Thunderbot2023();

    public void init(HardwareMap hwMap, Telemetry telem) {
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
