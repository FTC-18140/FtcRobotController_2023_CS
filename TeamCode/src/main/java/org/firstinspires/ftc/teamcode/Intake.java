package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    Telemetry telemetry;
    HardwareMap hardwaremap = null;

    DcMotor intake = null;
    Servo leftIntake = null;
    Servo rightIntake = null;
    CRServo ramp = null;

    public void init(HardwareMap hwMap, Telemetry telem) {
        intake = hwMap.dcMotor.get("intake");
        intake.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setDirection(DcMotorSimple.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftIntake = hwMap.servo.get("lintake");
        rightIntake = hwMap.servo.get("rintake");

        ramp = hwMap.crservo.get("ramp");
    }

    public void intakeMove(double power) {
        intake.setPower(power);
    }
    public void intakeLift(double position) {
        leftIntake.setPosition(position);
        rightIntake.setPosition(position);
    }
    public void rampMove(double power) {
        ramp.setPower(power);
    }

}
