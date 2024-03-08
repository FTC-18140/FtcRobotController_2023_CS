package org.firstinspires.ftc.teamcode.Robot;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
public class Sensors {
    Telemetry  telemetry;
    DistanceSensor lDistance = null;
    DistanceSensor rDistance = null;

    public void init(HardwareMap hwMap, Telemetry telem) {
        telemetry = telem;
        try {
            lDistance = hwMap.get(DistanceSensor.class, ("lDistance"));
        } catch (Exception e) {
            telemetry.addData("Left Distance Sensor not found", 0);
        }
        try {
            rDistance = hwMap.get(DistanceSensor.class, ("rDistance"));
        } catch (Exception e) {
            telemetry.addData("Right Distance Sensor not found", 0);
        }
    }

    public void distanceDetect() {
        double leftDistance = lDistance.getDistance(DistanceUnit.CM);
        double rightDistance = rDistance.getDistance(DistanceUnit.CM);

        telemetry.addData("Left Distance = ", leftDistance);
        telemetry.addData("Right Distance =  ", rightDistance);
    }

}
