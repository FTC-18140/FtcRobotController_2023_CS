package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;
import static java.lang.Math.abs;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SPIN{

    DcMotor spinner = null;
    DcMotor spinner2 = null;


    public void init (HardwareMap ahwMap, Telemetry telem) {
        try {
            spinner = ahwMap.get(DcMotorEx.class, "LeftFront");
            spinner.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            spinner.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spinner.setDirection(DcMotorSimple.Direction.FORWARD);
        } catch (Exception e) {
            telem.addData("hi", " ");
        }

        try {
            spinner2 = ahwMap.get(DcMotorEx.class, "LeftRear");
            spinner2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            spinner2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            spinner2.setDirection(DcMotorSimple.Direction.REVERSE);
        } catch (Exception e){
            telem.addData("hi", " ");
        }
    }

    public void spin (double power) {
        double spinnerPower = power;
        double max = abs(spinnerPower);

        if (abs(spinnerPower) > max)
        {
            max = abs(spinnerPower);
        }

        if (max > 1)
        {
            spinnerPower /= max;
        }

        spinner.setPower(spinnerPower);
        spinner2.setPower(spinnerPower);
    }
}
