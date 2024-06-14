package org.firstinspires.ftc.teamcode.Robot.BlueBot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Bluebot {
    DcMotorEx leftwheel = null;
    DcMotorEx rightwheel = null;

    private Telemetry telemetry = null;

    public void init(HardwareMap ahwMap,Telemetry telem) {

        telemetry = telem;

        try {
            leftwheel = ahwMap.get(DcMotorEx.class, "leftwheel");
            leftwheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftwheel.setDirection(DcMotorSimple.Direction.REVERSE);
            leftwheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
        catch (Exception e) {
            telemetry.addData("leftwheel not found",0);
        }
        try {
            rightwheel = ahwMap.get(DcMotorEx.class, "rightwheel");
            rightwheel.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightwheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightwheel.setDirection(DcMotorSimple.Direction.FORWARD);
            rightwheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
        catch (Exception e) {
            telemetry.addData("rightwheel not found",0);
        }
    }
    public void drive(double leftPower, double rightPower) {
        leftwheel.setPower(leftPower/2);
        rightwheel.setPower(rightPower/2);
    }
}

