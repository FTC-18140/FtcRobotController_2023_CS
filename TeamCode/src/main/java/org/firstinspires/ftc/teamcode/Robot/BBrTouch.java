package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.rev.Rev2mDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class BBrTouch extends OpMode {

    // Define a variable for our sensors
//    Rev2mDistanceSensor distanceLeft;
//    Rev2mDistanceSensor distanceRight;
    ColorSensor colorright;
    ColorSensor colorleft;
    DigitalChannel beamBreakLeft;
    DigitalChannel beamBreakRight;

    int colorRed;
    int colorBlue;

    double distance;

    @Override
    public void init () {
        // TODO: fix these comments to be the correct i2c busses

        //I2C 2
//        distanceLeft = hardwareMap.get(Rev2mDistanceSensor.class, "distanceleft");
//        //I2C 1
//        distanceRight = hardwareMap.get(Rev2mDistanceSensor.class, "distanceright");
        //I2C 3 Control Hub
        colorright = hardwareMap.get(ColorSensor.class, "colorright");
        //I2C 3
        colorleft = hardwareMap.get(ColorSensor.class, "colorleft");
        // Digital 3
        // left and right are switched here so they can be right on the robot
        beamBreakLeft = hardwareMap.get(DigitalChannel.class, "bbrright");
        // Digital 5
        beamBreakRight = hardwareMap.get(DigitalChannel.class, "bbrleft");

    }

    @Override
    public void loop() {
//        telemetry.addData("Distance1", distanceLeft.getDistance(DistanceUnit.CM));
//        telemetry.addData("Distance2", distanceRight.getDistance(DistanceUnit.CM));
//        telemetry.addData("Left Blue ColorV3", colorleft.blue());
//        telemetry.addData("Right Blue ColorV3", colorright.blue());
//        telemetry.addData("Left Red ColorV3", colorleft.red());
//        telemetry.addData("Right Red ColorV3", colorright.red());
        telemetry.addData("Tape Color", TapeLineColor());
//        telemetry.addData("Backdrop distance", GetAverageDistance());
        telemetry.addData("Beam Break Status Left", beamBreakLeft.getState());
        telemetry.addData("Beam Break Status Right", beamBreakRight.getState());

        telemetry.update();

//        colorBlue = ((colorleft.blue() + colorright.blue()) / 2);
//        colorRed = ((colorleft.red() + colorright.red()) / 2);
//
//        distance = ((distanceLeft.getDistance(DistanceUnit.CM) + distanceRight.getDistance(DistanceUnit.CM)) / 2);
    }

    public int TapeLineColor () {

        // 0 = can't tell
        // 1 = blue
        // 2 = red

        int tapeColor = 0;

        if (colorBlue > (colorRed + 250)) {
            tapeColor = 1;

        } else if (colorRed > (colorBlue + 50)) {
            tapeColor = 2;

        }

        return tapeColor;
    }


//    public double GetAverageDistance () {
//        return distance;
//    }

    public boolean LeftBeamBroken () {
        return beamBreakLeft.getState();
    }

    public boolean RightBeamBroken () {
        return beamBreakRight.getState();
    }

}

