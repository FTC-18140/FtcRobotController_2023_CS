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



    public double leftSlidePosition = 0;
    public double rightSlidePosition = 0;

    final private double COUNTS_PER_MOTOR_REV = 28; // REV HD Hex motor
    final private double DRIVE_GEAR_REDUCTION = 3.61 * 5.23;  // actual gear ratios of the 4:1 and 5:1 UltraPlanetary gear box modules
    final private double SPOOL_DIAMETER_CM = 3.5;  // slide spool is 35mm in diameter
    final private double COUNTS_PER_CM = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION)
            / (SPOOL_DIAMETER_CM * Math.PI);

    // Initialize
    public void init(HardwareMap hwMap, Telemetry telem) {
        try {
            leftLinear = hwMap.dcMotor.get("lSlide");
            leftLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftLinear.setDirection(DcMotorSimple.Direction.FORWARD);
            leftLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch(Exception e) {
            telemetry.addData("lSlide not found", 0);
        }
        try {
            rightLinear = hwMap.dcMotor.get("rSlide");
            rightLinear.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            rightLinear.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightLinear.setDirection(DcMotorSimple.Direction.FORWARD);
            rightLinear.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        } catch(Exception e) {
            telemetry.addData("rSlide not found", 0);
        }

        try {
            leftArm = hwMap.servo.get("larm");
            rightArm = hwMap.servo.get("rarm");
            rightArm.setDirection(Servo.Direction.FORWARD);
            leftArm.setDirection(Servo.Direction.REVERSE);
        } catch(Exception e) {
            telemetry.addData("larm and rarm not detected", 0);
        }
    }

    public void linearPower(double power) {
        leftLinear.setPower(power);
        rightLinear.setPower(power);
    }

    // Uses
    public void linearMove(double power) {
        double y = (getLiftPosition() / 50) + 0.25;
        double yHigh = ((50 - getLiftPosition()) / 60) + 0.02;

        if (getLiftPosition() > 47) {
            linearPower(power * yHigh);
        } else if (getLiftPosition() < 3) {
            linearPower(power * y);
        } else if (getLiftPosition() > 50) {
            linearPower( power * 0.05);
        } else if (getLiftPosition() < 0) {
            linearPower(0.2);
        } else {
            linearPower(power);
        }
    }

    public void armMove(double position) {
        leftArm.setPosition(position);
        rightArm.setPosition(position);
    }

    public void update() {
        leftSlidePosition = leftLinear.getCurrentPosition();
        rightSlidePosition = rightLinear.getCurrentPosition();
        leftArm.getPosition();
        rightArm.getPosition();
    }

        public double getLiftPosition() {
            return 0.5 * (leftSlidePosition + rightSlidePosition) / COUNTS_PER_CM;
        }

        public void liftPresetTape1 () {
            if (getLiftPosition() < 5) {
                linearMove(0.1);
            }
        }
    }
