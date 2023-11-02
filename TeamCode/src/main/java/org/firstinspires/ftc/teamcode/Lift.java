package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Lift {

    Telemetry telemetry;
    HardwareMap hwMap = null;

    // Defining Motors and Servos
    DcMotor leftLinear = null;
    DcMotor rightLinear = null;
    Servo leftArm = null;
    Servo rightArm = null;

    // Initialize
    public void init(HardwareMap hwMap, Telemetry telem) {
        leftLinear = hwMap.dcMotor.get("lSlide");
        leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftLinear.setDirection(DcMotorSimple.Direction.FORWARD);
        leftLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        rightLinear = hwMap.dcMotor.get("rSlide");
        rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightLinear.setDirection(DcMotorSimple.Direction.FORWARD);
        rightLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftArm = hwMap.servo.get("larm");
        rightArm = hwMap.servo.get("rarm");
    }
    // Uses
    public void linearUp(double power) {
        leftLinear.setPower(power);
        rightLinear.setPower(power);
    }
    public void linearDown(double power) {
        leftLinear.setPower(-power);
        rightLinear.setPower(-power);
    }
    public void armMove(double position) {
        leftArm.setPosition(position);
        rightArm.setPosition(position);
    }
}
