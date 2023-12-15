package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class fourBar {

    Telemetry telemetry;
    HardwareMap hardwaremap = null;
    Servo gripperLintake = null;
    Servo gripperRintake = null;
    Servo gripperLepositer = null;
    Servo gripperRepositer = null;
    Servo depositerLarm = null;
    Servo depositerRarm = null;
    Servo intakeArm = null;
    Servo wrist = null;
    Servo rotate = null;


    public void init(HardwareMap hwMap, Telemetry telem) {
        try {
            gripperLintake = hwMap.servo.get("gLintake");
        } catch (Exception e) {
            telemetry.addData("gripperLintake not found", 0);
        }

        try {
            gripperRintake = hwMap.servo.get("gRintake");
        } catch (Exception e) {
            telemetry.addData("gripperRintake not found", 0);
        }

        try {
            gripperLepositer = hwMap.servo.get("gLepositer");
        } catch (Exception e) {
            telemetry.addData("gripperLepositer not found", 0);
        }

        try {
            gripperRepositer = hwMap.servo.get("gRepositer");
        } catch (Exception e) {
            telemetry.addData("gripperRepositer not found", 0);
        }

        try {
            depositerLarm = hwMap.servo.get("dLarm");
        } catch(Exception e) {
            telemetry.addData("depositerLarm not found", 0);
        }

        try {
            depositerRarm = hwMap.servo.get("dRarm");
        } catch(Exception e) {
            telemetry.addData("depositerRarm not found", 0);
        }

        try {
            intakeArm = hwMap.servo.get("iArm");
        } catch(Exception e) {
            telemetry.addData("intakeArm not found", 0);
        }

        try {
            wrist = hwMap.servo.get("wrist");
        } catch(Exception e) {
            telemetry.addData("wrist not found", 0);
        }

        try {
            rotate = hwMap.servo.get("rotate");
        } catch(Exception e) {
            telemetry.addData("rotate not found", 0);
        }
    }

    public void gripperIntake(double leftPosition, double rightPosition) {
        gripperLepositer.setPosition(leftPosition);
        gripperRepositer.setPosition(rightPosition);
    }

    public void gripperDepositer(double leftPosition, double rightPosition) {
        gripperLepositer.setPosition(leftPosition);
        gripperRepositer.setPosition(rightPosition);
    }

    public void armMove(double position) {
        depositerLarm.setPosition(position);
        depositerRarm.setPosition(position);
    }

    public void intakeArmMove(double position) {
        intakeArm.setPosition(position);
    }

    public void wristMove(double position) {
        wrist.setPosition(position);
    }

    public void rotateMove(double position) {
        wrist.setPosition(position);
    }




}
